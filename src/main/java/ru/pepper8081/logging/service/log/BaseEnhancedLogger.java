package ru.pepper8081.logging.service.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import ru.pepper8081.logging.model.LogRecord;

import java.util.Map;

import static ru.pepper8081.logging.model.LogType.*;

@Slf4j
@RequiredArgsConstructor
public final class BaseEnhancedLogger implements EnhancedLogger {

    @Override
    public void beginLog(LogRecord record) {
        Object logType = record.logType();

        if (logType.equals(BEGIN.value()) || logType.equals(BOTH.value())) {
            this.log(record.withLogType(BEGIN.value()));
        }

    }

    @Override
    public void endLog(LogRecord record) {
        Object logType = record.logType();
        if (logType.equals(END.value()) || logType.equals(BOTH.value())) {
            this.log(record.withLogType(END.value()));
        }
    }

    @Override
    public void errorLog(LogRecord record) {
        this.log(record);
    }

    private void log(LogRecord record) {
        Map<String, String> mdcMap = record.toCustomMdcMap();
        mdcMap.forEach(MDC::put);
        switch (record.logLevel()) {
            case DEBUG -> log.debug(record.message());
            case INFO -> log.info(record.message());
            case TRACE -> log.trace(record.message());
            case WARN -> log.warn(record.message());
            case ERROR -> log.error(record.message(), record.throwable());
            default -> log.warn("Try to log with unexpected level '{}'", record.logLevel());
        }
        mdcMap.forEach((key, value) -> MDC.remove(key));
    }

}
