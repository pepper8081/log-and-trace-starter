package org.pepper8081.logandtrace.annotaton;


import org.pepper8081.logandtrace.model.LogLevel;
import org.pepper8081.logandtrace.model.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlatformLog {

    String event();

    LogLevel level() default LogLevel.INFO;

    String message() default "";

    boolean trace() default true;

    LogType logType() default LogType.BEGIN;

}
