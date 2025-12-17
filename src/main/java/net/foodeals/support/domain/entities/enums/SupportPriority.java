package net.foodeals.support.domain.entities.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SupportPriority {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    URGENT("urgent");

    private final String value;

    SupportPriority(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

