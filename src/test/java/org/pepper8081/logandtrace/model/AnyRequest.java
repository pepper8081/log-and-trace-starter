package org.pepper8081.logandtrace.model;

import java.time.Instant;

public record AnyRequest(String id, Instant occurredOn, MethodType method, Object params) {
}

