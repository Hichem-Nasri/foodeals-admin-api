package net.foodeals.organizationEntity.application.dtos.requests;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.foodeals.contract.application.DTo.upload.SolutionsContractDto;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerDraftRequest {

    private EntityType entityType;

    private String entityName;

    private Boolean commissionPayedBySubEntities;

    private EntityAddressDto entityAddressDto;

    private List<String> solutions;

    private List<String> features;

    private String commercialNumber;

    private Integer managerId;

    private ContactDto contactDto;

    private List<String> activities;

    private Integer maxNumberOfSubEntities;

    private Integer maxNumberOfAccounts;

    private Float minimumReduction;

    private List<SolutionsContractDto> solutionsContractDto;

    private Boolean oneSubscription;

    private EntityBankInformationDto entityBankInformationDto;

    private List<CoveredZonesDto> coveredZonesDtos;

    private List<DeliveryPartnerContract> deliveryPartnerContract;

    private Boolean subscriptionPayedBySubEntities;
}
