package org.pepper8081.logandtrace.model;

import java.time.Instant;

public record AnyResponse(String requestId, Instant occurredOn, Object result) {}
