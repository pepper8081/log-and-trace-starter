package org.pepper8081.logandtrace.service;

import org.pepper8081.logandtrace.model.AnyRequest;
import org.pepper8081.logandtrace.model.AnyResponse;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public interface INoProxiedTestService {

    AnyResponse execute(AnyRequest dd);
    List<AnyResponse> execute(AnyRequest dd1, AnyRequest dd2);
    void execute();


}
