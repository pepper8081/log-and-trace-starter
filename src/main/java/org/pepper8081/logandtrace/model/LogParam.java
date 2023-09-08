package org.pepper8081.logandtrace.model;

public enum LogParam {
    EVENT("event"),
    LOGGER("fixedLogger"),
    INPUT_PARAMS("inputParams"),
    OUTPUT_VALUE("outputValue"),
    SPAN_ID("spanId"),
    TRACE_ID("traceId"),
    LOG_TYPE("logType");

    private final String label;

    LogParam(String key) {
        this.label = key;
    }

    public String key() {
        return this.label;
    }

}
