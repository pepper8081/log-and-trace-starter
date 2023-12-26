package ru.pepper8081.logandtrace.service.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import ru.pepper8081.logandtrace.config.LogConfig;
import ru.pepper8081.logandtrace.config.TraceConfig;
import ru.pepper8081.logandtrace.model.log.LogRecord;
import ru.pepper8081.logandtrace.service.converter.ToStringConverter;
import ru.pepper8081.logandtrace.service.log.EnhancedLogger;

@Service
@ConditionalOnBean(value = LogConfig.class)
@ConditionalOnMissingBean(value = TraceConfig.class)
public class LogAroundAspectService extends LogAroundAspect {

    private final EnhancedLogger logger;

    @Autowired
    public LogAroundAspectService(EnhancedLogger logger, ToStringConverter toStringConverter) {
        super(toStringConverter);
        this.logger = logger;
    }

    @Override
    public LogRecord doBefore(LogRecord logRecord) {
        logger.beginLog(logRecord);
        return logRecord;
    }

    @Override
    public LogRecord doAfter(LogRecord logRecord) {
        logger.endLog(logRecord);
        return logRecord;
    }

    @Override
    public LogRecord doOnException(LogRecord logRecord) {
        logger.errorLog(logRecord);
        return logRecord;
    }

    @Override
    public LogRecord doFinally(LogRecord logRecord) {
        return logRecord;
    }
}