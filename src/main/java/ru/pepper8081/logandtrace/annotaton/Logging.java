package ru.pepper8081.logandtrace.annotaton;


import ru.pepper8081.logandtrace.model.log.LogLevel;
import ru.pepper8081.logandtrace.model.log.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {

    String event();

    LogLevel level() default LogLevel.INFO;

    String message() default "";

    LogType logType() default LogType.BEGIN;

}
