package ru.pepper8081.logging.service.impl;

import ru.pepper8081.logging.annotaton.Logging;
import ru.pepper8081.logging.model.AnyRequest;

public class DependentTestService {

    @Logging(event = "executeDependentService", message = "Execute dependent service")
    String doAnything(AnyRequest ar) {
        return "any-result";
    }
}
