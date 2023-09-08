package org.pepper8081.logandtrace.layout.plain;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.pepper8081.logandtrace.model.LogParam;

public final class LogTypeParamConverter extends LogParamConverter {
    @Override
    protected LogParam logKey() {
        return LogParam.LOG_TYPE;
    }

    @Override
    protected String valueIfAbsent(ILoggingEvent e) {
        return "";
    }
}
