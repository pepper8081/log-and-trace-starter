package org.pepper8081.logandtrace.service.log;

import org.pepper8081.logandtrace.model.LogLevel;
import org.pepper8081.logandtrace.model.LogParam;

import java.util.Map;

public interface EnhancedLogger {

    void beginLog(LogLevel level, String message, Map<LogParam, Object> params);

    void endLog(LogLevel level, String message, Map<LogParam, Object> params);

    void errorLog(String message, Map<LogParam, Object> params, Throwable t);

}
