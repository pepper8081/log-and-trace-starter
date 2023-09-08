package org.pepper8081.logandtrace.model;

import java.util.Map;

public record LogRecord(
        LogLevel level,
        String message,
        Map<LogParam, Object> params,
        boolean traceEnabled,
        Throwable throwable
) {

    public LogRecord(LogLevel level, String message, Map<LogParam, Object> params, boolean traceEnabled) {
        this(level, message, params, traceEnabled, null);
    }

    public LogRecord(LogRecord record, Throwable t) {
        this(record.level(), record.message(), record.params(), record.traceEnabled(), t);
    }
}