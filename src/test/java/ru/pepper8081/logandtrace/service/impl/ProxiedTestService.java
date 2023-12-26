package ru.pepper8081.logandtrace.service.impl;

import lombok.RequiredArgsConstructor;
import ru.pepper8081.logandtrace.annotaton.Logging;
import ru.pepper8081.logandtrace.model.AnyMessage;
import ru.pepper8081.logandtrace.model.log.LogType;
import ru.pepper8081.logandtrace.service.TestService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("UnusedReturnValue")
@RequiredArgsConstructor
public class ProxiedTestService implements TestService {

    private final DependentTestService dependentTestService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Logging(event = "executeSingleRequest", message = "Execute single request", logType = LogType.BOTH)
    @Override
    public AnyMessage execute(AnyMessage ar) {
        Objects.requireNonNull(ar);
        AnyMessage r = this.dependentTestService.doAnything(ar);
        return AnyMessage.from(r, r.getPayload() + "-new");
    }

    @Logging(event = "executeWithSeparateThread", message = "Execute request with separate thread", logType = LogType.BOTH)
    @Override
    public AnyMessage executeWithSeparateThread(AnyMessage ar) throws ExecutionException, InterruptedException {
        Objects.requireNonNull(ar);
        Future<AnyMessage> submit = executorService.submit(() -> this.dependentTestService.doAnything(ar));
        AnyMessage r = submit.get();
        return AnyMessage.from(r, r.getPayload() + "-new");
    }

    @Override
    @Logging(event = "executeVoid", message = "Execute void method", logType = LogType.BOTH)
    public void execute() {
    }

    @Logging(event = "executeDoubleRequest", message = "Execute double request", logType = LogType.BOTH)
    @Override
    public List<AnyMessage> execute(AnyMessage ar1, AnyMessage ar2) {
        Objects.requireNonNull(ar1);
        Objects.requireNonNull(ar2);
        return Stream.of(ar1, ar2).map(this::execute).collect(Collectors.toList());
    }

}
