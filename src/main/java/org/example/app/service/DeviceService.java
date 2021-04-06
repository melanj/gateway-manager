package org.example.app.service;

import org.example.app.dto.DeviceDTO;
import org.example.app.model.Device;
import org.example.app.model.Gateway;
import org.example.app.repository.DeviceRepository;
import org.example.app.repository.GatewayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final GatewayRepository gatewayRepository;

    public DeviceService(final DeviceRepository deviceRepository,
                         final GatewayRepository gatewayRepository) {
        this.deviceRepository = deviceRepository;
        this.gatewayRepository = gatewayRepository;
    }

    public List<DeviceDTO> findAll(Optional<Long> gatewayId) {
        List<Device> deviceList;
        if (gatewayId.isPresent()) {
            Gateway gateway = gatewayRepository.findById(gatewayId.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            deviceList = deviceRepository.findByGatewayId(gateway.getId());
        } else {
            deviceList = deviceRepository.findAll();
        }
        return deviceList
                .stream()
                .map(device -> mapToDTO(device, new DeviceDTO()))
                .collect(Collectors.toList());
    }

    public DeviceDTO get(final Long id) {
        return deviceRepository.findById(id)
                .map(device -> mapToDTO(device, new DeviceDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final DeviceDTO deviceDTO) {
        final Device device = new Device();
        mapToEntity(deviceDTO, device);
        return deviceRepository.save(device).getId();
    }

    public void update(final Long id, final DeviceDTO deviceDTO) {
        final Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(deviceDTO, device);
        deviceRepository.save(device);
    }

    public void delete(final Long id) {
        deviceRepository.deleteById(id);
    }

    private DeviceDTO mapToDTO(final Device device, final DeviceDTO deviceDTO) {
        deviceDTO.setId(device.getId());
        deviceDTO.setUid(device.getUid());
        deviceDTO.setVendor(device.getVendor());
        deviceDTO.setDateCreated(device.getDateCreated());
        deviceDTO.setStatus(device.getStatus());
        deviceDTO.setGateway(device.getGateway() == null ? null : device.getGateway().getId());
        return deviceDTO;
    }

    private Device mapToEntity(final DeviceDTO deviceDTO, final Device device) {
        device.setUid(deviceDTO.getUid());
        device.setVendor(deviceDTO.getVendor());
        device.setDateCreated(deviceDTO.getDateCreated());
        device.setStatus(deviceDTO.getStatus());
        if (deviceDTO.getGateway() != null &&
                (device.getGateway() == null || !device.getGateway().getId().equals(deviceDTO.getGateway()))) {
            final Gateway gateway = gatewayRepository.findById(deviceDTO.getGateway())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "gateway not found"));
            device.setGateway(gateway);
        }
        return device;
    }

}
