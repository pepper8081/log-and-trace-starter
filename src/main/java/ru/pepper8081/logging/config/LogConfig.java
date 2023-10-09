package ru.pepper8081.logging.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.pepper8081.logging.service.converter.BaseToStringConverter;
import ru.pepper8081.logging.service.converter.ToStringConverter;
import ru.pepper8081.logging.service.log.BaseEnhancedLogger;
import ru.pepper8081.logging.service.log.EnhancedLogger;

@Configuration
public class LogConfig {

    @Bean
    public EnhancedLogger enhancedLogger() {
        return new BaseEnhancedLogger();
    }

    @Bean
    public ToStringConverter toStringConverter() {
        return new BaseToStringConverter();
    }

}
