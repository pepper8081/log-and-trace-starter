package ru.pepper8081.logandtrace.service.aspect;

import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import ru.pepper8081.logandtrace.config.TraceConfig;
import ru.pepper8081.logandtrace.model.Traceable;
import ru.pepper8081.logandtrace.model.log.LogRecord;
import ru.pepper8081.logandtrace.model.span.ScopedSpan;
import ru.pepper8081.logandtrace.service.converter.ToStringConverter;
import ru.pepper8081.logandtrace.service.log.EnhancedLogger;

@Service
@ConditionalOnBean(value = TraceConfig.class)
public class TraceLogAroundAspectService extends LogAroundAspectService {

    private final Tracer tracer;

    @Autowired
    public TraceLogAroundAspectService(EnhancedLogger logger,
                                       Tracer tracer,
                                       ToStringConverter toStringConverter) {
        super(logger, toStringConverter);
        this.tracer = tracer;
    }

    @Override
    public LogRecord doBefore(LogRecord logRecord) {

        SpanBuilder spanBuilder = tracer.spanBuilder(logRecord.event())
                .setAttribute("message", logRecord.message())
                .setSpanKind(SpanKind.SERVER);
        Object input = logRecord.inputParams().originalInput();
        if (input instanceof Traceable traceable) {
            if (traceable.isTraceableDefined()) {
                Scope parentScope = Span.wrap(
                        SpanContext.createFromRemoteParent(
                                traceable.getTraceId(),
                                traceable.getSpanId(),
                                TraceFlags.getSampled(),
                                TraceState.getDefault()
                        )).makeCurrent();
                ScopedSpan scopedSpan = new ScopedSpan(spanBuilder.startSpan(), parentScope);
                traceable.refreshTraceableBy(scopedSpan);
                return super.doBefore(logRecord.withSpan(scopedSpan));
            } else {
                Span span = spanBuilder.startSpan();
                ScopedSpan scopedSpan = new ScopedSpan(span, span.makeCurrent());
                traceable.refreshTraceableBy(scopedSpan);
                return super.doBefore(logRecord.withSpan(scopedSpan));
            }
        } else {
            Span span = spanBuilder.startSpan();
            return super.doBefore(logRecord.withSpan(
                    new ScopedSpan(span, span.makeCurrent()))
            );
        }

    }

    @Override
    public LogRecord doAfter(LogRecord logRecord) {
        Object value = logRecord.outputValue().originalValue();
        if (value instanceof Traceable traceable && logRecord.scopedSpan() != null) {
            traceable.setSpanId(logRecord.scopedSpan().getSpanId());
            traceable.setTraceId(logRecord.scopedSpan().getTraceId());
        }
        return super.doAfter(logRecord);
    }

    @Override
    public LogRecord doOnException(LogRecord logRecord) {
        LogRecord exceptionLogRecord = super.doOnException(logRecord);
        if (exceptionLogRecord.scopedSpan() != null) {
            exceptionLogRecord.scopedSpan().span()
                    .recordException(logRecord.throwable())
                    .setAttribute("error", Boolean.TRUE.toString());
        }
        return exceptionLogRecord;
    }

    @Override
    public LogRecord doFinally(LogRecord logRecord) {
        LogRecord onFinallyLogRecord = super.doFinally(logRecord);
        ScopedSpan scopedSpan = onFinallyLogRecord.scopedSpan();
        if (scopedSpan != null) {
            scopedSpan.close();
        }
        return onFinallyLogRecord;
    }
}