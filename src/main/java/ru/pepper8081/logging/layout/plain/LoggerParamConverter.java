package ru.pepper8081.logging.layout.plain;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ru.pepper8081.logging.model.LogRecord;

public final class LoggerParamConverter extends LogParamConverter {
    @Override
    protected String logKey() {
        return LogRecord.MdcKeys.fixedLogger;
    }

    @Override
    protected String valueIfAbsent(ILoggingEvent e) {
        return e.getLoggerName();
    }
}