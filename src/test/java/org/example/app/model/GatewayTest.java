package org.example.app.model;

import org.example.app.util.ConversionUtils;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class GatewayTest {

    @Test
    public void testGetterSetters() {
        final Set<Device> devices = new HashSet<>();
        devices.add(new Device());
        final Gateway gateway = new Gateway();
        gateway.setId(100L);
        gateway.setName("my-gateway");
        gateway.setSerial("1900080");
        gateway.setIpv4Address(ConversionUtils.ipToLong("192.168.1.1"));
        gateway.setGatewayDevices(devices);
        assertEquals(gateway.getId().longValue(), 100L);
        assertEquals(gateway.getName(), "my-gateway");
        assertEquals(gateway.getSerial(), "1900080");
        assertEquals(ConversionUtils.longToIp(gateway.getIpv4Address()), "192.168.1.1");
        assertEquals(gateway.getGatewayDevices(), devices);
    }
}