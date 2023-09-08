package org.pepper8081.logandtrace.service.impl;

import org.pepper8081.logandtrace.model.AnyRequest;
import org.pepper8081.logandtrace.annotaton.PlatformLog;
import org.pepper8081.logandtrace.model.LogType;

public class DependentTestService {

    @PlatformLog(event = "executeDependentService",
            message = "Execute dependent service", logType = LogType.BEGIN)
    AnyRequest asIs(AnyRequest l) {
//        throw new RuntimeException("any exception message");
        return l;
    }
}
