package ru.pepper8081.logandtrace.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import ru.pepper8081.logandtrace.annotaton.Logging;
import ru.pepper8081.logandtrace.model.log.LogLevel;
import ru.pepper8081.logandtrace.model.log.LogRecord;
import ru.pepper8081.logandtrace.model.log.LogType;
import ru.pepper8081.logandtrace.service.AroundService;
import ru.pepper8081.logandtrace.service.converter.ToStringConverter;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 42)
public abstract class LogAroundAspect implements AroundService<LogRecord> {

    private final ToStringConverter toStringConverter;

    public LogAroundAspect(ToStringConverter toStringConverter) {
        this.toStringConverter = toStringConverter;
    }

    @Around("@annotation(a) && execution(* *(..))")
    protected Object around(ProceedingJoinPoint joinPoint, Logging a) throws Throwable {

        String message = a.message();
        String event = a.event();
        LogLevel level = a.level();
        LogType logType = a.logType();
        Class<?> logger = joinPoint.getTarget().getClass();

        Object args;
        if (joinPoint.getArgs().length == 0) args = null;
        else if (joinPoint.getArgs().length == 1) args = joinPoint.getArgs()[0];
        else args = joinPoint.getArgs();

        LogRecord logRecord = new LogRecord(
                level, message,
                event, logger,
                logType, new LogRecord.InputParams(args, toStringConverter.convert(args)),
                new LogRecord.OutputValue(null, null)
        );
        try {
            logRecord = this.doBefore(logRecord);
            Object res = joinPoint.proceed();
            logRecord = this.doAfter(logRecord.withResult(
                    new LogRecord.OutputValue(res, toStringConverter.convert(res))));
            return res;
        } catch (Throwable t) {
            logRecord = this.doOnException(logRecord.withException(t));
            throw t;
        } finally {
            this.doFinally(logRecord);
        }
    }

}