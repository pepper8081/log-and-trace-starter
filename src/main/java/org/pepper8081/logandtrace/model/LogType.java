package org.pepper8081.logandtrace.model;

public enum LogType {
    BEGIN("begin"),
    END("end"),
    BOTH("both");
    private final String key;

    LogType(String key) {
        this.key = key;
    }

    public String value() {
        return key;
    }
}
