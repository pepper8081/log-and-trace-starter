package ru.pepper8081.logging.layout.plain;

import ch.qos.logback.classic.pattern.NamedConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public abstract class LogParamConverter extends NamedConverter {

    @Override
    public final String getFullyQualifiedName(ILoggingEvent e) {
        String v = e.getMDCPropertyMap().get(this.logKey());
        if (v == null)
            return this.valueIfAbsent(e);
        return v;
    }

    protected abstract String logKey();

    protected abstract String valueIfAbsent(ILoggingEvent e);

}
