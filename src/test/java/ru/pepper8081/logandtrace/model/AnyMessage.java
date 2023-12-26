package ru.pepper8081.logandtrace.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode
@ToString
@Getter
public final class AnyMessage implements Traceable {
    private final String id;
    private final Instant occurredOn;
    private final String payload;
    private volatile String traceId;
    private volatile String spanId;

    public AnyMessage(String id, Instant occurredOn, String payload) {
        this.id = id;
        this.occurredOn = occurredOn;
        this.payload = payload;
        this.traceId = null;
        this.spanId = null;
    }

    public AnyMessage(String id, Instant occurredOn, String payload, String traceId, String spanId) {
        this.id = id;
        this.occurredOn = occurredOn;
        this.payload = payload;
        this.traceId = traceId;
        this.spanId = spanId;
    }

    @Override
    public void setSpanId(String s) {
        this.spanId = s;
    }

    @Override
    public void setTraceId(String s) {
        this.traceId = s;
    }

    public static AnyMessage from(AnyMessage ar, String p) {
        return new AnyMessage(UUID.randomUUID().toString(), Instant.now(), p,
                ar.getTraceId(), ar.getSpanId());
    }

}

