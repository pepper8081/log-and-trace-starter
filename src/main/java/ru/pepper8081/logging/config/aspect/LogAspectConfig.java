package ru.pepper8081.logging.config.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import ru.pepper8081.logging.config.LogConfig;
import ru.pepper8081.logging.config.TraceConfig;
import ru.pepper8081.logging.model.LogRecord;
import ru.pepper8081.logging.service.converter.ToStringConverter;
import ru.pepper8081.logging.service.log.EnhancedLogger;

@Configuration
@ConditionalOnBean(value = LogConfig.class)
@ConditionalOnMissingBean(value = TraceConfig.class)
public class LogAspectConfig extends LogAroundAspect {

    private final EnhancedLogger logger;

    @Autowired
    public LogAspectConfig(EnhancedLogger logger, ToStringConverter toStringConverter) {
        super(toStringConverter);
        this.logger = logger;
    }

    @Override
    protected LogRecord doBefore(LogRecord logRecord) {
        logger.beginLog(logRecord);
        return logRecord;
    }

    @Override
    protected LogRecord doAfter(LogRecord logRecord) {
        logger.endLog(logRecord);
        return logRecord;
    }

    @Override
    protected LogRecord doOnException(LogRecord logRecord) {
        logger.errorLog(logRecord);
        return logRecord;
    }

    @Override
    protected LogRecord doFinally(LogRecord logRecord) {
        return logRecord;
    }
}