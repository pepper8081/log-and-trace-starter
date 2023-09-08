package org.pepper8081.logandtrace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pepper8081.logandtrace.model.AnyRequest;
import org.pepper8081.logandtrace.model.AnyResponse;
import org.pepper8081.logandtrace.service.INoProxiedTestService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class NoProxiedTestService implements INoProxiedTestService {

    private final DependentTestService dependentTestService;

    @Override
    public AnyResponse execute(AnyRequest dd) {
        log.info("Method invoked. Params: [{}]", dd);
        AnyRequest m = this.dependentTestService.asIs(dd);
        try {
            return new AnyResponse(m.id(), Instant.now(), m.id());
        } catch (Exception e) {
            log.error("Error was occurred. Data: {}", dd);
            throw e;
        }
    }

    @Override
    public void execute() {
        log.info("Method invoked with no params");
    }

    @Override
    public List<AnyResponse> execute(AnyRequest dd1, AnyRequest dd2) {
        log.info("Method invoked: Data: [{}, {}]", dd1, dd2);
        return Stream.of(dd1, dd2).map(m -> {
            AnyRequest d = this.dependentTestService.asIs(m);
            try {
                return new AnyResponse(m.id(), Instant.now(), d.id());
            } catch (Exception e) {
                log.error("Error was occurred. Data: {}", m);
                throw e;
            }
        }).collect(Collectors.toList());
    }
}
