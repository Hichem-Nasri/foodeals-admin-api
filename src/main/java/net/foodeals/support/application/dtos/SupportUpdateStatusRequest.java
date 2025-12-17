package net.foodeals.support.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.foodeals.support.domain.entities.enums.SupportStatus;


public record SupportUpdateStatusRequest(
    SupportStatus status,
    String note,
    @JsonProperty("notify_user") Boolean notifyUser
) {}
