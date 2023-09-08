package org.pepper8081.logandtrace.config.aspect;

import org.pepper8081.logandtrace.service.log.EnhancedLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Configuration;
import org.pepper8081.logandtrace.config.TraceConfig;
import org.pepper8081.logandtrace.model.LogParam;
import org.pepper8081.logandtrace.model.LogRecord;

@Configuration
@ConditionalOnBean(value = TraceConfig.class)
public class TraceAspectConfig extends LogAspectConfig {

    private final Tracer tracer;

    @Autowired
    public TraceAspectConfig(EnhancedLogger logger, Tracer tracer) {
        super(logger);
        this.tracer = tracer;
    }

    @Override
    protected void before(LogRecord logRecord) {
        if (logRecord.traceEnabled())
            tracer.startScopedSpan(logRecord.params().get(LogParam.EVENT).toString())
                    .tag("message", logRecord.message());
        super.before(logRecord);

    }

    @Override
    protected void after(LogRecord logRecord) {
        super.after(logRecord);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void onException(LogRecord logRecord) {
        super.onException(logRecord);
        if (tracer.currentSpan() != null)
            tracer.currentSpan().error(logRecord.throwable()).tag("error", "true");
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void onFinally(LogRecord logRecord) {
        super.onFinally(logRecord);
        if (tracer.currentSpan() != null)
            tracer.currentSpan().end();
    }
}