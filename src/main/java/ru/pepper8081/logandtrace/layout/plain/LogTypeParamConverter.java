package ru.pepper8081.logandtrace.layout.plain;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ru.pepper8081.logandtrace.model.log.LogRecord;

public final class LogTypeParamConverter extends LogParamConverter {
    @Override
    protected String logKey() {
        return LogRecord.MdcKeys.logType;
    }

    @Override
    protected String valueIfAbsent(ILoggingEvent e) {
        return "";
    }
}
