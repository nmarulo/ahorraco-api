package dev.nmarulo.ahorraco_api.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppProperties {
    
    @Value("${app.http.path.prefix:/api}")
    private String pathPrefix;
    
    @Value("${app.cors.allowed-origins:}")
    private String[] corsAllowedOrigins;
    
    @Value("${app.web.base-url:}")
    private String webBaseUrl;
    
}
