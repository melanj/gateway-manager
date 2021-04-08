package org.example.app.model;

import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class DeviceTest {

    @Test
    public void testGetterSetters() {
        final Device device = new Device();
        final Gateway gateway = new Gateway();
        device.setId(100L);
        device.setUid(16001L);
        device.setVendor("Hello");
        device.setStatus(Status.ONLINE);
        device.setDateCreated(LocalDate.MIN);
        device.setGateway(gateway);
        assertEquals(device.getId().longValue(), 100L);
        assertEquals(device.getUid().longValue(), 16001L);
        assertEquals(device.getVendor(), "Hello");
        assertEquals(device.getStatus(), Status.ONLINE);
        assertEquals(device.getDateCreated(), LocalDate.MIN);
        assertEquals(device.getGateway(), gateway);
    }
}