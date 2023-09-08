package org.pepper8081.logandtrace.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.pepper8081.logandtrace.model.AnyRequest;
import org.pepper8081.logandtrace.model.AnyResponse;
import org.pepper8081.logandtrace.service.IProxiedTestService;
import org.pepper8081.logandtrace.annotaton.PlatformLog;
import org.pepper8081.logandtrace.model.LogType;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnusedReturnValue")
@RequiredArgsConstructor
public class ProxiedTestService implements IProxiedTestService {

    private final DependentTestService dependentTestService;

    @PlatformLog(event = "executeOneRequest", message = "Execute one request on proxy service",
            logType = LogType.BOTH)
    @Override
    @SneakyThrows
    public AnyResponse execute(AnyRequest dd) {
        AnyRequest m = this.dependentTestService.asIs(dd);
        return new AnyResponse(m.id(), Instant.now(), m.id());
    }

    @SneakyThrows
    @Override
    @PlatformLog(event = "anyEvent", message = "Execute void method on proxy service", logType = LogType.BOTH)
    public void execute() {
    }

    @PlatformLog(event = "executeTwoRequests",
            message = "Execute two requests on proxy service", logType = LogType.BOTH)
    @Override
    public List<AnyResponse> execute(AnyRequest dd1, AnyRequest dd2) {
        return Stream.of(dd1, dd2).map(this::execute).collect(Collectors.toList());
    }

}
