package net.foodeals.support.domain.entities.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseType {
    ADMIN_RESPONSE("admin_response"),
    INTERNAL_NOTE("internal_note");

    private final String value;

    ResponseType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
