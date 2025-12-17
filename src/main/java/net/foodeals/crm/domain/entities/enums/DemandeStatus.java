package net.foodeals.crm.domain.entities.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DemandeStatus {
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String value;
    DemandeStatus(String v){ this.value = v; }
    @JsonValue public String getValue(){ return value; }
}
