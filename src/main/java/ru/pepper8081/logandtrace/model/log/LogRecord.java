package ru.pepper8081.logandtrace.model.log;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.pepper8081.logandtrace.model.span.ScopedSpan;

import java.util.HashMap;
import java.util.Map;

import static ru.pepper8081.logandtrace.utils.AllUtils.putIfNotNull;

public record LogRecord(
        @NonNull LogLevel logLevel,
        @NonNull String message,
        @NonNull String event,
        @NonNull Class<?> fixedLogger,
        @NonNull LogType logType,
        @NonNull InputParams inputParams,
        @NonNull OutputValue outputValue,
        @Nullable Throwable throwable,
        @Nullable ScopedSpan scopedSpan
) {
    public LogRecord(LogLevel level, String message,
                     String event, Class<?> fixedLogger,
                     LogType logType, InputParams inputParams,
                     OutputValue outputValue) {
        this(level, message,
                event, fixedLogger,
                logType, inputParams,
                outputValue, null,
                null);
    }

    public LogRecord withResult(OutputValue r) {
        return new LogRecord(this.logLevel, this.message,
                this.event, this.fixedLogger,
                this.logType, this.inputParams,
                r, this.throwable, this.scopedSpan);
    }

    public LogRecord withException(Throwable t) {
        return new LogRecord(LogLevel.ERROR, this.message,
                this.event, this.fixedLogger,
                this.logType, this.inputParams,
                this.outputValue, t, this.scopedSpan);
    }

    public LogRecord withLogType(LogType logType) {
        return new LogRecord(this.logLevel, this.message,
                this.event, this.fixedLogger,
                logType, this.inputParams,
                this.outputValue, this.throwable, this.scopedSpan);
    }

    public LogRecord withSpan(ScopedSpan span) {
        return new LogRecord(this.logLevel, this.message,
                this.event, this.fixedLogger,
                this.logType, this.inputParams,
                this.outputValue, this.throwable, span);
    }

    public Map<String, String> toCustomMdcMap() {
        Map<String, String> map = new HashMap<>();
        putIfNotNull(map, MdcKeys.logType, this.logType.value());
        putIfNotNull(map, MdcKeys.inputParams, this.inputParams.stringInput());
        putIfNotNull(map, MdcKeys.event, this.event);
        putIfNotNull(map, MdcKeys.outputValue, this.outputValue.stringValue());
        putIfNotNull(map, MdcKeys.fixedLogger, this.fixedLogger.getName());
        putIfNotNull(map, MdcKeys.spanId, this.scopedSpan != null
                ? this.scopedSpan.span().getSpanContext().getSpanId()
                : null);
        putIfNotNull(map, MdcKeys.traceId, this.scopedSpan != null
                ? this.scopedSpan.span().getSpanContext().getTraceId()
                : null);
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

    public record InputParams(
            @Nullable Object originalInput,
            @Nullable String stringInput
    ) {
    }

    public record OutputValue(
            @Nullable Object originalValue,
            @Nullable String stringValue
    ) {
    }

}