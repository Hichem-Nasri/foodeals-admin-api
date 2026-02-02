package net.foodeals.organizationEntity.application.dtos.responses;

import net.foodeals.contract.domain.entities.enums.ContractStatus;

import java.util.UUID;

public record PartnerDraftResponse(UUID id, ContractStatus status) {
}
