package org.pepper8081.logandtrace.config;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(name = "spring.sleuth.enabled", havingValue = "true", matchIfMissing = true)
public class TraceConfig {

    @Bean
    @Primary
    public Resource customizedOtelResource(@Autowired ObjectProvider<List<Supplier<Resource>>> resourceProviders,
                                    @Value("${spring.product.name}") String productName,
                                    @Value("${spring.application.name}") String appName) {
        String formattedProductName = Arrays.stream(productName.split("-"))
                .map(String::toLowerCase)
                .map(m -> m.substring(0, 1).toUpperCase() + m.substring(1))
                .collect(Collectors.joining());
        String formattedAppName = Arrays.stream(appName.split("-"))
                .map(String::toLowerCase)
                .map(m -> m.substring(0, 1).toUpperCase() + m.substring(1))
                .collect(Collectors.joining());

        Resource resource = Resource.getDefault()
                .merge(
                        Resource.create(
                                Attributes.of(ResourceAttributes.SERVICE_NAME, formattedProductName + "." + formattedAppName))
                );
        List<Supplier<Resource>> resourceCustomizers = resourceProviders.getIfAvailable(ArrayList::new);
        for (Supplier<Resource> provider : resourceCustomizers) {
            resource = resource.merge(provider.get());
        }
        return resource;
    }

}
