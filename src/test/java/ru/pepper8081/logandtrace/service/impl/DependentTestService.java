package ru.pepper8081.logandtrace.service.impl;

import ru.pepper8081.logandtrace.annotaton.Logging;
import ru.pepper8081.logandtrace.model.AnyMessage;

public class DependentTestService {

    @Logging(event = "executeDependentService", message = "Execute dependent service")
    AnyMessage doAnything(AnyMessage ar) {
        return AnyMessage.from(ar, ar.getPayload() + "-new");
    }
}
