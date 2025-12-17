package net.foodeals.crm.domain.entities.enums;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DemandeType {
    DLC("dlc"),
    MARKETPRO("marketpro"),
    DONATE("donate"),
    ASSOCIATION("association");

    private final String value;
    DemandeType(String v){ this.value = v; }
    @JsonValue public String getValue(){ return value; }
}
