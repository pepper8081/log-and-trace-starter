package ru.pepper8081.logandtrace.service.log;

import ru.pepper8081.logandtrace.model.log.LogRecord;

public interface EnhancedLogger {

    void beginLog(LogRecord record);

    void endLog(LogRecord record);

    void errorLog(LogRecord record);

}
