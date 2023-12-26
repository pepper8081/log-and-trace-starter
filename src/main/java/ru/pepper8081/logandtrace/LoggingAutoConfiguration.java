package ru.pepper8081.logandtrace;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@ComponentScan(value = "ru.pepper8081.logandtrace")
@PropertySource("classpath:logging.properties")
public class LoggingAutoConfiguration {
}