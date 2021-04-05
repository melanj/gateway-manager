package org.example.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan(basePackages = "org.example.app.model")
@EnableJpaRepositories(basePackages = "org.example.app.repository")
@EnableTransactionManagement
public class JpaConfig
{
}
