package ru.pepper8081.logging.model;

import org.springframework.cloud.sleuth.ScopedSpan;

import java.util.HashMap;
import java.util.Map;

import static ru.pepper8081.logging.utils.AllUtils.putSkippingNulls;

public record LogRecord(
        LogLevel logLevel,
        String message,
        String event,
        String fixedLogger,
        String logType,
        String inputParams,
        String outputValue,
        Throwable throwable,
        boolean traceEnabled,
        ScopedSpan span
) {
    public LogRecord(LogLevel level, String message,
                     String event, String fixedLogger,
                     String logType, String inputParams,
                     boolean traceEnabled) {
        this(level, message,
                event, fixedLogger,
                logType, inputParams,
                null, null,
                traceEnabled, null);
    }

    public LogRecord withResult(String r) {
        return new LogRecord(this.logLevel, this.message,
                this.event, this.fixedLogger,
                this.logType, this.inputParams,
                r, this.throwable,
                this.traceEnabled, this.span);
    }

    public LogRecord withException(Throwable t) {
        return new LogRecord(this.logLevel, this.message,
                this.event, this.fixedLogger,
                this.logType, this.inputParams,
                this.outputValue, t,
                this.traceEnabled, this.span);
    }

    public LogRecord withLogType(String logType) {
        return new LogRecord(this.logLevel, this.message,
                this.event, this.fixedLogger,
                logType, this.inputParams,
                this.outputValue, this.throwable,
                this.traceEnabled, this.span);
    }

    public LogRecord withSpan(ScopedSpan span) {
        return new LogRecord(this.logLevel, this.message,
                this.event, this.fixedLogger,
                this.logType, this.inputParams,
                this.outputValue, this.throwable,
                this.traceEnabled, span);
    }

    public Map<String, String> toCustomMdcMap() {
        Map<String, String> map = new HashMap<>();
        putSkippingNulls(map, MdcKeys.logType, this.logType);
        putSkippingNulls(map, MdcKeys.inputParams, this.inputParams);
        putSkippingNulls(map, MdcKeys.event, this.event);
        putSkippingNulls(map, MdcKeys.outputValue, this.outputValue);
        putSkippingNulls(map, MdcKeys.fixedLogger, this.fixedLogger);
        return map;
    }


    public static class MdcKeys {
        public static final String event = "event";
        public static final String fixedLogger = "fixedLogger";
        public static final String logType = "logType";
        public static final String inputParams = "inputParams";
        public static final String outputValue = "outputValue";
        public static final String traceId = "traceId";
        public static final String spanId = "spanId";

    }

}