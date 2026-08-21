package dev.nmarulo.ahorraco_api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Habilita la auditoría de JPA.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
    
}
