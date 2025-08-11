package com.pluralsight.ticket_api.repository;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@ComponentScan(basePackages = {
        "com.pluralsight.ticket_api.repository", // pour les repositories comme AgentRepository
        "com.pluralsight.ticket_api.DAO.jdbc"     // si tu veux garder ce package aussi
})
@EnableJpaRepositories(basePackages = "com.pluralsight.ticket_api.repository")
@EntityScan(basePackages = "com.pluralsight.ticket_api.model")
@EnableAutoConfiguration
public class DatabaseContextTest {
}
