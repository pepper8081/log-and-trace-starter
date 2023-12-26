package ru.pepper8081.logandtrace.service;

import ru.pepper8081.logandtrace.model.AnyMessage;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("UnusedReturnValue")
public interface TestService {

    AnyMessage execute(AnyMessage ar);

    AnyMessage executeWithSeparateThread(AnyMessage ar) throws ExecutionException, InterruptedException;

    List<AnyMessage> execute(AnyMessage ar1, AnyMessage ar2);

    void execute();

}
