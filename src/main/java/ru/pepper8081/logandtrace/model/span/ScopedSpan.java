package ru.pepper8081.logandtrace.model.span;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import org.springframework.lang.NonNull;

public record ScopedSpan(@NonNull Span span, @NonNull Scope scope) {
    public void close() {
        this.scope.close();
        this.span.end();
    }

    public String getSpanId() {
        return this.span.getSpanContext().getSpanId();
    }

    public String getTraceId() {
        return this.span.getSpanContext().getTraceId();
    }

}
