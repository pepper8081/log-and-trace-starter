package org.pepper8081.logandtrace.layout.json;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import org.pepper8081.logandtrace.model.LogParam;

import java.util.Map;

public final class EnhancedJsonLayout extends JsonLayout {

    @Override
    protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        Map<String, String> mdc = event.getMDCPropertyMap();
        if (!mdc.isEmpty())
            for (LogParam c : LogParam.values()) {
                String value = mdc.get(c.key());
                map.put(c.key(), value);
            }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> toJsonMap(ILoggingEvent event) {
        Map<String, Object> map = super.toJsonMap(event);
        String fixedLogger = ((String) map.get(LogParam.LOGGER.key()));
        if (fixedLogger != null)
            add(LOGGER_ATTR_NAME, this.includeLoggerName, fixedLogger, map);
        map.remove(LogParam.LOGGER.key());
        return map;
    }
}
