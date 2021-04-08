package org.example.app.util;

import org.example.app.dto.DeviceDTO;
import org.example.app.dto.GatewayDTO;
import org.example.app.model.Device;
import org.example.app.model.Gateway;
import org.example.app.repository.GatewayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class ConversionUtils {

    private ConversionUtils() {
        
    }

    public static DeviceDTO deviceToDTO(final Device device, final DeviceDTO deviceDTO) {
        deviceDTO.setId(device.getId());
        deviceDTO.setUid(device.getUid());
        deviceDTO.setVendor(device.getVendor());
        deviceDTO.setDateCreated(device.getDateCreated());
        deviceDTO.setStatus(device.getStatus());
        deviceDTO.setGateway(device.getGateway().getId());
        return deviceDTO;
    }

    public static Device dtoToDevice(final DeviceDTO deviceDTO, final Device device, GatewayRepository gatewayRepository) {
        device.setUid(deviceDTO.getUid());
        device.setVendor(deviceDTO.getVendor());
        device.setDateCreated(deviceDTO.getDateCreated());
        device.setStatus(deviceDTO.getStatus());
        if ((device.getGateway() == null || !device.getGateway().getId().equals(deviceDTO.getGateway()))) {
            final Gateway gateway = gatewayRepository.findById(deviceDTO.getGateway())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "gateway not found"));
            device.setGateway(gateway);
        }
        return device;
    }

    public static GatewayDTO gatewayToDTO(final Gateway gateway, final GatewayDTO gatewayDTO) {
        gatewayDTO.setId(gateway.getId());
        gatewayDTO.setSerial(gateway.getSerial());
        gatewayDTO.setName(gateway.getName());
        gatewayDTO.setIpv4Address(longToIp(gateway.getIpv4Address()));
        return gatewayDTO;
    }

    public static Gateway dtoToGateway(final GatewayDTO gatewayDTO, final Gateway gateway) {
        gateway.setSerial(gatewayDTO.getSerial());
        gateway.setName(gatewayDTO.getName());
        gateway.setIpv4Address(ipToLong(gatewayDTO.getIpv4Address()));
        return gateway;
    }

    public static int ipToLong(String ipAddress) {
        long result = 0;
        String[] ipAddressInArray = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {

            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            result |= ip << (i * 8);
        }
        return (int) (result - Integer.MAX_VALUE);
    }

    public static String longToIp(int src) {
        long ip = Integer.MAX_VALUE + (long) src;
        StringBuilder result = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            result.insert(0, Long.toString(ip & 0xff));
            if (i < 3) {
                result.insert(0, '.');
            }
            ip = ip >> 8;
        }
        return result.toString();
    }


}
