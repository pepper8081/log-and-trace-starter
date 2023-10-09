package ru.pepper8081.logging.annotaton;


import ru.pepper8081.logging.model.LogLevel;
import ru.pepper8081.logging.model.LogType;

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

    boolean trace() default true;

    LogType logType() default LogType.BEGIN;

}
