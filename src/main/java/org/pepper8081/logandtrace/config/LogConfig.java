package org.pepper8081.logandtrace.config;


import org.pepper8081.logandtrace.service.converter.BaseToStringConverter;
import org.pepper8081.logandtrace.service.converter.ToStringConverter;
import org.pepper8081.logandtrace.service.log.BaseEnhancedLogger;
import org.pepper8081.logandtrace.service.log.EnhancedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

    @Bean
    public EnhancedLogger platformLogger(@Autowired ToStringConverter toStringConverter) {
        return new BaseEnhancedLogger(toStringConverter);
    }

    @Bean
    public ToStringConverter toStringConverter() {
        return new BaseToStringConverter();
    }

}
