package ru.pepper8081.logging.layout.plain;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ru.pepper8081.logging.model.LogRecord;

public final class EventParamConverter extends LogParamConverter {
    @Override
    protected String logKey() {
        return LogRecord.MdcKeys.event;
    }

    @Override
    protected String valueIfAbsent(ILoggingEvent e) {
        return "";
    }
}
