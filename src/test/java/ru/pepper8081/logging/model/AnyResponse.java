package ru.pepper8081.logging.model;

import java.time.Instant;

public record AnyResponse(String requestId, Instant occurredOn, Object result) {}
