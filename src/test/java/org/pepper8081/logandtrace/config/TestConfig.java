package org.pepper8081.logandtrace.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.pepper8081.logandtrace.service.IProxiedTestService;
import org.pepper8081.logandtrace.service.impl.DependentTestService;
import org.pepper8081.logandtrace.service.impl.NoProxiedTestService;
import org.pepper8081.logandtrace.service.impl.ProxiedTestService;


@Configuration
@EnableAutoConfiguration
public class TestConfig {

    @Bean
    public DependentTestService dependentTestService() {
        return new DependentTestService();
    }


    @Bean
    public IProxiedTestService proxiedTestService() {
        return new ProxiedTestService(dependentTestService());
    }

    @Bean
    public NoProxiedTestService noProxiedTestService() {
        return new NoProxiedTestService(dependentTestService());
    }


}