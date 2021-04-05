package org.example.app.service;

import java.util.List;
import java.util.stream.Collectors;
import org.example.app.model.Gateway;
import org.example.app.dto.GatewayDTO;
import org.example.app.repository.GatewayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class GatewayService {

    private final GatewayRepository gatewayRepository;

    public GatewayService(final GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    public List<GatewayDTO> findAll() {
        return gatewayRepository.findAll()
                .stream()
                .map(gateway -> mapToDTO(gateway, new GatewayDTO()))
                .collect(Collectors.toList());
    }

    public GatewayDTO get(final Long id) {
        return gatewayRepository.findById(id)
                .map(gateway -> mapToDTO(gateway, new GatewayDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final GatewayDTO gatewayDTO) {
        final Gateway gateway = new Gateway();
        mapToEntity(gatewayDTO, gateway);
        return gatewayRepository.save(gateway).getId();
    }

    public void update(final Long id, final GatewayDTO gatewayDTO) {
        final Gateway gateway = gatewayRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(gatewayDTO, gateway);
        gatewayRepository.save(gateway);
    }

    public void delete(final Long id) {
        gatewayRepository.deleteById(id);
    }

    private GatewayDTO mapToDTO(final Gateway gateway, final GatewayDTO gatewayDTO) {
        gatewayDTO.setId(gateway.getId());
        gatewayDTO.setSerial(gateway.getSerial());
        gatewayDTO.setName(gateway.getName());
        gatewayDTO.setIpv4Address(longToIp(gateway.getIpv4Address()));
        return gatewayDTO;
    }

    private Gateway mapToEntity(final GatewayDTO gatewayDTO, final Gateway gateway) {
        gateway.setSerial(gatewayDTO.getSerial());
        gateway.setName(gatewayDTO.getName());
        gateway.setIpv4Address(ipToLong(gatewayDTO.getIpv4Address()));
        return gateway;
    }

    public int ipToLong(String ipAddress) {
        long result = 0;
        String[] ipAddressInArray = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {

            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            result |= ip << (i * 8);
        }
        return (int)(result - Integer.MAX_VALUE);
    }

    public String longToIp(int src) {
        long ip = Integer.MAX_VALUE + (long) src;
        StringBuilder result = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            result.insert(0,Long.toString(ip & 0xff));
            if (i < 3) {
                result.insert(0,'.');
            }
            ip = ip >> 8;
        }
        return result.toString();
    }

}
