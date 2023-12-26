package ru.pepper8081.logandtrace.model;

import ru.pepper8081.logandtrace.model.span.ScopedSpan;

public interface Traceable {

    String getSpanId();

    String getTraceId();

    void setSpanId(String s);

    void setTraceId(String s);

    default boolean isTraceableDefined() {
        return this.getSpanId() != null && this.getTraceId() != null;
    }

    default void refreshTraceableBy(ScopedSpan s) {
        this.setSpanId(s.getSpanId());
        this.setTraceId(s.getTraceId());
    }

}
