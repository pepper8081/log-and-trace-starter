package ru.pepper8081.logging.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.pepper8081.logging.annotaton.Logging;
import ru.pepper8081.logging.model.AnyRequest;
import ru.pepper8081.logging.model.AnyResponse;
import ru.pepper8081.logging.model.LogType;
import ru.pepper8081.logging.service.TestService;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnusedReturnValue")
@RequiredArgsConstructor
public class ProxiedTestService implements TestService {

    private final DependentTestService dependentTestService;

    @Logging(event = "executeSingleRequest", message = "Execute single request", logType = LogType.BOTH)
    @Override
    @SneakyThrows
    public AnyResponse execute(AnyRequest ar) {
        Objects.requireNonNull(ar);
        String r = this.dependentTestService.doAnything(ar);
        return new AnyResponse(ar.id(), Instant.now(), r);
    }

    @SneakyThrows
    @Override
    @Logging(event = "executeVoid", message = "Execute void method", logType = LogType.BOTH)
    public void execute() {
    }

    @Logging(event = "executeDoubleRequest", message = "Execute double request", logType = LogType.BOTH)
    @Override
    public List<AnyResponse> execute(AnyRequest ar1, AnyRequest ar2) {
        Objects.requireNonNull(ar1);
        Objects.requireNonNull(ar2);
        return Stream.of(ar1, ar2).map(this::execute).collect(Collectors.toList());
    }

}
