package ru.pepper8081.logging.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import ru.pepper8081.logging.annotaton.Logging;
import ru.pepper8081.logging.model.LogLevel;
import ru.pepper8081.logging.model.LogRecord;
import ru.pepper8081.logging.service.converter.ToStringConverter;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 42)
public abstract class LogAroundAspect {

    private final ToStringConverter toStringConverter;

    public LogAroundAspect(ToStringConverter toStringConverter) {
        this.toStringConverter = toStringConverter;
    }

    @Around("@annotation(a) && execution(* *(..))")
    public Object around(ProceedingJoinPoint joinPoint, Logging a) throws Throwable {

        String message = a.message();
        String event = a.event();
        LogLevel level = a.level();
        String logType = a.logType().value();
        String logger = joinPoint.getTarget().getClass().getName();

        Object args;
        if (joinPoint.getArgs().length == 0) args = null;
        else if (joinPoint.getArgs().length == 1) args = joinPoint.getArgs()[0];
        else args = joinPoint.getArgs();

        LogRecord logRecord = new LogRecord(
                level, message,
                event, logger,
                logType, toStringConverter.convert(args),
                a.trace());
        try {
            logRecord = this.doBefore(logRecord);
            Object res = joinPoint.proceed();
            logRecord = this.doAfter(logRecord.withResult(toStringConverter.convert(res)));
            return res;
        } catch (Throwable t) {
            logRecord = this.doOnException(logRecord.withException(t));
            throw t;
        } finally {
            this.doFinally(logRecord);
        }
    }

    protected abstract LogRecord doBefore(LogRecord logRecord);

    protected abstract LogRecord doAfter(LogRecord logRecord);

    protected abstract LogRecord doOnException(LogRecord logRecord);

    protected abstract LogRecord doFinally(LogRecord logRecord);

}