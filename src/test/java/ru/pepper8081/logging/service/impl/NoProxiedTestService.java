package ru.pepper8081.logging.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.pepper8081.logging.model.AnyRequest;
import ru.pepper8081.logging.model.AnyResponse;
import ru.pepper8081.logging.service.TestService;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class NoProxiedTestService implements TestService {

    private final DependentTestService dependentTestService;

    @Override
    public AnyResponse execute(AnyRequest ar) {
        try {
            log.info("Method invoked. Params: [{}]", ar);
            Objects.requireNonNull(ar);
            String r = this.dependentTestService.doAnything(ar);
            return new AnyResponse(ar.id(), Instant.now(), r);
        } catch (Exception e) {
            log.error("Error was occurred. Data: {}", ar);
            throw e;
        }
    }

    @Override
    public void execute() {
        log.info("Method invoked with no params");
    }

    @Override
    public List<AnyResponse> execute(AnyRequest ar1, AnyRequest ar2) {
        log.info("Method invoked: Data: [{}, {}]", ar1, ar2);
        Objects.requireNonNull(ar1);
        Objects.requireNonNull(ar2);
        return Stream.of(ar1, ar2).map(m -> {
            try {
                String r = this.dependentTestService.doAnything(m);
                return new AnyResponse(m.id(), Instant.now(), r);
            } catch (Exception e) {
                log.error("Error was occurred. Data: {}", m);
                throw e;
            }
        }).collect(Collectors.toList());
    }
}
