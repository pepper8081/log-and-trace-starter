package ru.pepper8081.logandtrace.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.pepper8081.logandtrace.service.converter.BaseToStringConverter;
import ru.pepper8081.logandtrace.service.converter.ToStringConverter;
import ru.pepper8081.logandtrace.service.log.BaseEnhancedLogger;
import ru.pepper8081.logandtrace.service.log.EnhancedLogger;

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
