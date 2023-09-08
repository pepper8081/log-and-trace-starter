package org.pepper8081.logandtrace.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.pepper8081.logandtrace.annotaton.PlatformLog;
import org.pepper8081.logandtrace.model.LogLevel;
import org.pepper8081.logandtrace.model.LogParam;
import org.pepper8081.logandtrace.model.LogRecord;
import org.pepper8081.logandtrace.model.LogType;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 42)
public abstract class PlatformLogAroundAspect {

    @Around("@annotation(a) && execution(* *(..))")
    public Object around(ProceedingJoinPoint joinPoint, PlatformLog a) throws Throwable {

        String message = a.message();
        String eventName = a.event();
        LogLevel level = a.level();
        LogType logType = a.logType();

        Object args;
        if (joinPoint.getArgs().length == 0) args = null;
        else if (joinPoint.getArgs().length == 1) args = joinPoint.getArgs()[0];
        else args = joinPoint.getArgs();

        Map<LogParam, Object> params = new HashMap<>();
        params.put(LogParam.EVENT, eventName);
        params.put(LogParam.LOGGER, joinPoint.getTarget().getClass().getName());
        params.put(LogParam.LOG_TYPE, logType.value());
        params.put(LogParam.INPUT_PARAMS, args);

        LogRecord logRecord = new LogRecord(level, message, params, a.trace());
        try {
            this.before(logRecord);
            Object res = joinPoint.proceed();
            params.put(LogParam.OUTPUT_VALUE, res);
            this.after(logRecord);
            return res;
        } catch (Throwable t) {
            this.onException(new LogRecord(logRecord, t));
            throw t;
        } finally {
            this.onFinally(logRecord);
        }
    }

    protected abstract void before(LogRecord logRecord);

    protected abstract void after(LogRecord logRecord);

    protected abstract void onException(LogRecord logRecord);

    protected abstract void onFinally(LogRecord logRecord);

}