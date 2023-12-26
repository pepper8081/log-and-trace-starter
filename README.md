# Spring Boot Starter Logging

## Features
- Generalized format logging into std.out/std.err
  - json
  ```
  {
    "timestamp":"2023-10-11T20:35:17.637",
    "level":"INFO",
    "thread":"main",
    "logger":"ru.pepper8081.logandtrace.service.impl.ProxiedTestService",
    "message":"Executed user request",
    "logType":"begin",
    "inputParams":"input-value",
    "event":"event-name",
    "outputValue": "output-value",
    "traceId":"794f8831f722d9e26afd66b46f53865d",
    "spanId":"d07cf96d846d84cc"}
  ```
  - plain
- Generalized format trace exporting into opentelemetry-collector


## Settings
### Set up `application.properties`
```
### LOGGING
logging.config=classpath:logback.xml
### TRACING
spring.sleuth.otel.config.trace-id-ratio-based=1.0
spring.sleuth.otel.exporter.otlp.endpoint=${OTEL_COLLECTOR_ENDPOINT}
spring.sleuth.tx.enabled=true
spring.sleuth.enabled=true
```
#### `spring.sleuth.enabled` enable/disable tracing functionality
### Set up `logback.xml`
```
<?xml version = "1.0" encoding = "UTF-8"?>
<configuration scan="true" scanPeriod="30 seconds">
    <include resource="ru/pepper8081/logandtrace/appender.xml"/>

    <logger name="org.zalando.logbook.Logbook" level="trace"/>
    <logger name="com.github.dockerjava" level="warn"/>
    <logger name="org.testcontainers" level="info"/>

    <root level="info">
<!--        <appender-ref ref="PLAIN_CONSOLE"/>-->
        <appender-ref ref="JSON_CONSOLE"/>
    </root>
</configuration>

```

## Usage
### `@Logging` annotation on method with a set of any arguments and any return type including void
#### Arguments and return object should implement `toString` fine.
#### Using `ThreadLocal` context to find out a parent span.
```
@Logging(event = "eventName", message = "specific event message")
public Object execute(Object o1, Object o2, Object o3) {
    // any transformations
    return o;
}
```

### `@Logging` annotation on method with any `Traceable` argument and `Traceable` return type object
#### `Traceable` implementation should have fine `toString`.
#### Using `Traceable` interface to find out parent span before method execution and refresh that after.
```
@Logging(event = "eventName", message = "specific event message")
public <A extends Traceable> A execute(A a) {
    // any transformations
    return a;
}
```

### Manual log and trace management
#### `Traceable` implementation should have fine `toString`.
```
@Autowired
private AroundService<LogRecord> logService;

public void execute() {
  LogRecord logRecord = new LogRecord(
                level, message,
                event, logger,
                logType, new LogRecord.InputParams(args, args.toString()),
                new LogRecord.OutputValue(null, null);
  
  logRecord = logService.doBefore(logRecord);
  // do anything
  logRecord = logService.doAfter(logRecord.withResult(
                    new LogRecord.OutputValue(res, res.toString())));
  this.doOnFinally(logRecord);                  
}

```