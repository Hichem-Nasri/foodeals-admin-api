package net.foodeals.organizationEntity.Controller;

import lombok.AllArgsConstructor;
import net.foodeals.common.dto.request.UpdateReason;
import net.foodeals.common.dto.response.UpdateDetails;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.organizationEntity.application.dtos.requests.SubEntityFilter;
import net.foodeals.organizationEntity.application.dtos.responses.AssociationsSubEntitiesDto;
import net.foodeals.organizationEntity.application.dtos.responses.OrganizationSubEntitiesResponse;
import net.foodeals.organizationEntity.application.dtos.responses.PartnerSubEntityDto;
import net.foodeals.organizationEntity.application.dtos.responses.SubEntityResponse;
import net.foodeals.organizationEntity.application.services.OrganizationEntityService;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;
import net.foodeals.organizationEntity.domain.entities.enums.OrganizationsType;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;
import net.foodeals.payment.application.dto.response.PartnerInfoDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/sub-entities")
@AllArgsConstructor
public class SubEntityController {

    private final SubEntityService subEntityService;
    private final ModelMapper mapper;
    private final OrganizationEntityRepository organizationEntityRepository;

    @GetMapping("/organizations/{id}")
    @Transactional
    public ResponseEntity<OrganizationSubEntitiesResponse> partnerSubEntities(
            Pageable pageable,
            @PathVariable("id") UUID id,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(value = "organizationsType", required = true)
            OrganizationsType organizationsType,

            @RequestParam(value = "names", required = false)
            String names,

            @RequestParam(value = "types", required = true)
            List<SubEntityType> types,

            @RequestParam(value = "solutions", required = false)
            List<String> solutions,

            @RequestParam(value = "cityId", required = false)
            UUID cityId,

            @RequestParam(value = "collabId", required = false)
            Integer collabId,

            @RequestParam(value = "email", required = false)
            String email,

            @RequestParam(value = "phone", required = false)
            String phone,

            @RequestParam(value = "deletedAt", required = true)
            Boolean deletedAt
    ) {
        List<String> namesList = names != null
                ? Arrays.stream(names.split(","))
                .collect(Collectors.toList())
                : null;

        // Create a filter object with the provided parameters
        SubEntityFilter filter = SubEntityFilter.builder()
                .startDate(startDate != null ? startDate.atStartOfDay(ZoneOffset.UTC).toInstant() : null)
                .endDate(endDate != null ? endDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant() : null)
                .names(namesList)
                .email(email)
                .phone(phone)
                .solutions(solutions != null ? solutions : new ArrayList<String>())
                .cityId(cityId)
                .types(types)
                .collabId(collabId)
                .deletedAt(deletedAt)
                .build();

        // Fetch the sub-entities based on the filter
        Page<SubEntity> subEntities = this.subEntityService.subEntitiesFilters(pageable, id, filter);
        OrganizationEntity organizationEntity = this.organizationEntityRepository.getEntity(id).orElseThrow(() -> new RuntimeException("Organization entity not found"));
        PartnerInfoDto partnerInfo = new PartnerInfoDto(organizationEntity.getId(), organizationEntity.getName(), organizationEntity.getAvatarPath(), organizationEntity.getAddress().getRegion().getCity().getName());
        return switch (organizationsType) {
            case ASSOCIATIONS -> new ResponseEntity<>(new OrganizationSubEntitiesResponse(partnerInfo, subEntities.map(subEntity -> this.mapper.map(subEntity, AssociationsSubEntitiesDto.class))), HttpStatus.OK);
            case PARTNERS -> new ResponseEntity<>(new OrganizationSubEntitiesResponse(partnerInfo, subEntities.map(subEntity -> this.mapper.map(subEntity, PartnerSubEntityDto.class))), HttpStatus.OK);
            default -> null;
        };
    }


    @GetMapping("/search")
    @Transactional
    public ResponseEntity<Page<PartnerInfoDto>> searchsubEntities(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "id", required = false) UUID id,
            @RequestParam(name = "organizationId", required = false) UUID organizationId,
            @RequestParam(name = "types", required = false) List<SubEntityType> types,
            @RequestParam(name = "deleted", required = false, defaultValue = "false") boolean includeDeleted,
            Pageable pageable) {
        return ResponseEntity.ok(subEntityService.searchSubEntitiesByName(id, name, types, pageable, includeDeleted, organizationId).map(subEntity -> new PartnerInfoDto(subEntity.getId(), subEntity.getName(), subEntity.getAvatarPath(), subEntity.getAddress().getRegion().getCity().getName())));
    }

    @GetMapping("/{uuid}/deletion-details")
    @Transactional
    public ResponseEntity<Page<UpdateDetails>> getDeletionDetails(@PathVariable UUID uuid, Pageable pageable) {
        Page<UpdateDetails> deletionDetails = subEntityService.getDeletionDetails(uuid, pageable);
        return ResponseEntity.ok(deletionDetails);
    }

    @DeleteMapping("/{uuid}")
    @Transactional
    public ResponseEntity<Void> deleteSubentity(
            @PathVariable UUID uuid,
            @RequestBody UpdateReason request) {
        subEntityService.deleteSubentity(uuid, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cities/search")
    @Transactional
    public ResponseEntity<Page<CityResponse>> searchCities(
            @RequestParam(name = "city") String cityName,
            @RequestParam(name = "organizationId") UUID organizationId,
            Pageable pageable) {
        return ResponseEntity.ok(subEntityService.searchCitiesBySubEntityAddress(cityName, organizationId, pageable).map(c -> mapper.map(c, CityResponse.class)));
    }


}