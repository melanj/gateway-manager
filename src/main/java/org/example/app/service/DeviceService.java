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

import static org.example.app.util.ConversionUtils.deviceToDTO;
import static org.example.app.util.ConversionUtils.dtoToDevice;


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
                .map(device -> deviceToDTO(device, new DeviceDTO()))
                .collect(Collectors.toList());
    }

    public DeviceDTO get(final Long id) {
        return deviceRepository.findById(id)
                .map(device -> deviceToDTO(device, new DeviceDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final DeviceDTO deviceDTO) {
        List<DeviceDTO> devicesInGateway = findAll(Optional.of(deviceDTO.getGateway()));
        if (devicesInGateway.size() >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You have exceeded the maximum number of devices allowed for this gateway.");
        }
        if(findAll(Optional.empty()).stream().anyMatch(f -> f.getUid().equals(deviceDTO.getUid()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A device with same UID already exist.");
        }
        final Device device = new Device();
        dtoToDevice(deviceDTO, device, gatewayRepository);
        return deviceRepository.save(device).getId();
    }

    public void update(final Long id, final DeviceDTO deviceDTO) {
        final Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        dtoToDevice(deviceDTO, device, gatewayRepository);
        deviceRepository.save(device);
    }

    public void delete(final Long id) {
        deviceRepository.deleteById(id);
    }
}
