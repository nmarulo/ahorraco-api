package dev.nmarulo.ahorraco_api.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {
    
    private final AppProperties appProperties;
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //Configura el prefijo.
        configurer.addPathPrefix(this.appProperties.getPathPrefix(),
                                 HandlerTypePredicate.forAnnotation(RestController.class));
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(this.appProperties.getPathPrefix() + "/**")
                .allowedOrigins(this.appProperties.getCorsAllowedOrigins())
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
    
}
