package ru.pepper8081.logandtrace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.pepper8081.logandtrace.model.AnyMessage;
import ru.pepper8081.logandtrace.service.TestService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class NoProxiedTestService implements TestService {

    private final DependentTestService dependentTestService;

    @Override
    public AnyMessage execute(AnyMessage ar) {
        try {
            log.info("Method invoked. Params: [{}]", ar);
            Objects.requireNonNull(ar);
            AnyMessage r = this.dependentTestService.doAnything(ar);
            return AnyMessage.from(r, r.getPayload() + "-new");
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
    public AnyMessage executeWithSeparateThread(AnyMessage ar) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<AnyMessage> execute(AnyMessage ar1, AnyMessage ar2) {
        log.info("Method invoked: Data: [{}, {}]", ar1, ar2);
        Objects.requireNonNull(ar1);
        Objects.requireNonNull(ar2);
        return Stream.of(ar1, ar2).map(m -> {
            try {
                AnyMessage r = this.dependentTestService.doAnything(m);
                return AnyMessage.from(r, r.getPayload() + "-new");
            } catch (Exception e) {
                log.error("Error was occurred. Data: {}", m);
                throw e;
            }
        }).collect(Collectors.toList());
    }
}
