package org.pepper8081.logandtrace.layout.plain;

import ch.qos.logback.classic.pattern.NamedConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.pepper8081.logandtrace.model.LogParam;

public abstract class LogParamConverter extends NamedConverter {

    @Override
    public final String getFullyQualifiedName(ILoggingEvent e) {
        String v = e.getMDCPropertyMap().get(this.logKey().key());
        if (v == null)
            return this.valueIfAbsent(e);
        return v;
    }

    protected abstract LogParam logKey();

    protected abstract String valueIfAbsent(ILoggingEvent e);

}
