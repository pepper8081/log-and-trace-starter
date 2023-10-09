package ru.pepper8081.logging.service.log;

import ru.pepper8081.logging.model.LogRecord;

public interface EnhancedLogger {

    void beginLog(LogRecord record);

    void endLog(LogRecord record);

    void errorLog(LogRecord record);

}
