package ru.pepper8081.logandtrace.layout.json;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

import java.util.Map;

import static ru.pepper8081.logandtrace.model.log.LogRecord.MdcKeys;

public final class EnhancedJsonLayout extends JsonLayout {

    @Override
    protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        Map<String, String> mdc = event.getMDCPropertyMap();
        if (!mdc.isEmpty()) {
            map.put(MdcKeys.logType, mdc.get(MdcKeys.logType));
            map.put(MdcKeys.inputParams, mdc.get(MdcKeys.inputParams));
            map.put(MdcKeys.event, mdc.get(MdcKeys.event));
            map.put(MdcKeys.outputValue, mdc.get(MdcKeys.outputValue));
            map.put(MdcKeys.fixedLogger, mdc.get(MdcKeys.fixedLogger));
            map.put(MdcKeys.traceId, mdc.get(MdcKeys.traceId));
            map.put(MdcKeys.spanId, mdc.get(MdcKeys.spanId));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> toJsonMap(ILoggingEvent event) {
        Map<String, Object> map = super.toJsonMap(event);
        String fixedLogger = ((String) map.get(MdcKeys.fixedLogger));
        if (fixedLogger != null)
            add(LOGGER_ATTR_NAME, this.includeLoggerName, fixedLogger, map);
        map.remove(MdcKeys.fixedLogger);
        return map;
    }
}
