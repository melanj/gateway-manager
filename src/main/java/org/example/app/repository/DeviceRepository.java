package org.example.app.repository;

import org.example.app.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByGatewayId(long id);
}
