package ru.pepper8081.logging.service;

import ru.pepper8081.logging.model.AnyRequest;
import ru.pepper8081.logging.model.AnyResponse;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public interface TestService {

    AnyResponse execute(AnyRequest ar);
    List<AnyResponse> execute(AnyRequest ar1, AnyRequest ar2);
    void execute();

}
