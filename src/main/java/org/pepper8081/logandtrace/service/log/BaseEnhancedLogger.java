package org.pepper8081.logandtrace.service.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.pepper8081.logandtrace.model.LogLevel;
import org.pepper8081.logandtrace.model.LogParam;
import org.pepper8081.logandtrace.service.converter.ToStringConverter;

import java.util.Map;

import static org.pepper8081.logandtrace.model.LogType.*;

@Slf4j
@RequiredArgsConstructor
public final class BaseEnhancedLogger implements EnhancedLogger {

    private final ToStringConverter toStringConverter;

    @Override
    public void beginLog(LogLevel level, String message, Map<LogParam, Object> params) {
        Object logType = params.get(LogParam.LOG_TYPE);

        if (logType.equals(BEGIN.value()) || logType.equals(BOTH.value())) {
            String args = toStringConverter.convert(params.get(LogParam.INPUT_PARAMS));
            params.put(LogParam.INPUT_PARAMS, args);
            params.put(LogParam.LOG_TYPE, BEGIN.value());
            this.log(level, message, params, null);
            params.put(LogParam.LOG_TYPE, logType);
        }

    }

    @Override
    public void endLog(LogLevel level, String message, Map<LogParam, Object> params) {
        Object logType = params.get(LogParam.LOG_TYPE);
        if (logType.equals(END.value()) || logType.equals(BOTH.value())) {
            String result = toStringConverter.convert(params.get(LogParam.OUTPUT_VALUE));
            params.put(LogParam.OUTPUT_VALUE, result);
            params.put(LogParam.LOG_TYPE, END.value());
            this.log(level, message, params, null);
            params.put(LogParam.LOG_TYPE, logType);
        }
    }

    @Override
    public void errorLog(String message, Map<LogParam, Object> params, Throwable t) {
        Object logType = params.get(LogParam.LOG_TYPE);
        params.put(LogParam.LOG_TYPE, BEGIN.value());
        this.log(LogLevel.ERROR, message, params, t);
        params.put(LogParam.LOG_TYPE, logType);
    }

    private void log(LogLevel level,
                     String message,
                     Map<LogParam, Object> params,
                     Throwable t) {
        params.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .forEach(e -> MDC.put(e.getKey().key(), String.valueOf(e.getValue())));
        switch (level) {
            case DEBUG -> log.debug(message);
            case INFO -> log.info(message);
            case TRACE -> log.trace(message);
            case WARN -> log.warn(message);
            case ERROR -> log.error(message, t);
            default -> log.warn("Try to log with unexpected level '{}'. Params: {}", level, params);
        }
        params.forEach((key, value) -> MDC.remove(key.key()));
    }

}
