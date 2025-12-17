package net.foodeals.support.domain.entities.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SupportStatus {
    PENDING("PENDING"),     // New ticket awaiting review
    IN_PROGRESS("IN_PROGRESS"),  // Ticket being worked on
    RESOLVED("RESOLVED"),   // Issue has been resolved
    CLOSED("CLOSED"),    // Ticket is closed
    REOPENED("REOPENED");   // Previously closed ticket that was reopened
    
    
    
    private final String value;

    SupportStatus(String value) {
        this.value = value;
    }
    
    @JsonValue
    public String getValue() {
        return value;
    }
}