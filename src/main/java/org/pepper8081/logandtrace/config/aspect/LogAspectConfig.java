package org.pepper8081.logandtrace.config.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.pepper8081.logandtrace.config.LogConfig;
import org.pepper8081.logandtrace.config.TraceConfig;
import org.pepper8081.logandtrace.model.LogRecord;
import org.pepper8081.logandtrace.service.log.EnhancedLogger;

@Configuration
@ConditionalOnBean(value = LogConfig.class)
@ConditionalOnMissingBean(value = TraceConfig.class)
public class LogAspectConfig extends PlatformLogAroundAspect {

    private final EnhancedLogger logger;

    @Autowired
    public LogAspectConfig(EnhancedLogger logger) {
        this.logger = logger;
    }

    @Override
    protected void before(LogRecord logRecord) {
        logger.beginLog(logRecord.level(), logRecord.message(), logRecord.params());
    }

    @Override
    protected void after(LogRecord logRecord) {
        logger.endLog(logRecord.level(), logRecord.message(), logRecord.params());
    }

    @Override
    protected void onException(LogRecord logRecord) {
        logger.errorLog(logRecord.throwable().getMessage(), logRecord.params(), logRecord.throwable());
    }

    @Override
    protected void onFinally(LogRecord logRecord) {
    }
}