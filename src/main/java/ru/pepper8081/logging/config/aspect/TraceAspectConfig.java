package ru.pepper8081.logging.config.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.sleuth.ScopedSpan;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Configuration;
import ru.pepper8081.logging.config.TraceConfig;
import ru.pepper8081.logging.model.LogRecord;
import ru.pepper8081.logging.service.converter.ToStringConverter;
import ru.pepper8081.logging.service.log.EnhancedLogger;

@Configuration
@ConditionalOnBean(value = TraceConfig.class)
public class TraceAspectConfig extends LogAspectConfig {

    private final Tracer tracer;

    @Autowired
    public TraceAspectConfig(EnhancedLogger logger, Tracer tracer, ToStringConverter toStringConverter) {
        super(logger, toStringConverter);
        this.tracer = tracer;
    }

    @Override
    protected LogRecord doBefore(LogRecord logRecord) {
        if (logRecord.traceEnabled()) {
            ScopedSpan span = this.tracer.startScopedSpan(logRecord.event())
                    .tag("message", logRecord.message());
            return super.doBefore(logRecord.withSpan(span));
        }
        return super.doBefore(logRecord);
    }

    @Override
    protected LogRecord doAfter(LogRecord logRecord) {
        return super.doAfter(logRecord);
    }

    @Override
    protected LogRecord doOnException(LogRecord logRecord) {
        LogRecord exceptionLogRecord = super.doOnException(logRecord);
        if (exceptionLogRecord.span() != null) {
            exceptionLogRecord.span().error(logRecord.throwable())
                    .tag("error", Boolean.TRUE.toString());
        }
        return exceptionLogRecord;
    }

    @Override
    protected LogRecord doFinally(LogRecord logRecord) {
        LogRecord onFinallyLogRecord = super.doFinally(logRecord);
        if (onFinallyLogRecord.span() != null) {
            onFinallyLogRecord.span().end();
        }
        return onFinallyLogRecord;
    }
}