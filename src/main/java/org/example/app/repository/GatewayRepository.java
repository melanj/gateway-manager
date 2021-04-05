package org.example.app.repository;

import org.example.app.model.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GatewayRepository extends JpaRepository<Gateway, Long> {
}
