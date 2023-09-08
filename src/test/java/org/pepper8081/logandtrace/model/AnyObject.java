package org.pepper8081.logandtrace.model;

import java.util.UUID;

public record AnyObject(UUID id, AnyData anyData) {
    public record AnyData(String d1, Long d2) {
    }
}
