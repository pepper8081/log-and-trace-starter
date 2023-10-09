package ru.pepper8081.logging.model;

import java.time.Instant;

public record AnyRequest(String id, Instant occurredOn) {
}

