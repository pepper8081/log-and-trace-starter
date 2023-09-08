package org.pepper8081.logandtrace.layout.plain;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.pepper8081.logandtrace.model.LogParam;

public final class LoggerParamConverter extends LogParamConverter {
    @Override
    protected LogParam logKey() {
        return LogParam.LOGGER;
    }

    @Override
    protected String valueIfAbsent(ILoggingEvent e) {
        return e.getLoggerName();
    }
}