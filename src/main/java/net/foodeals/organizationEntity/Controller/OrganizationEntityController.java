package net.foodeals.organizationEntity.Controller;

import com.lowagie.text.DocumentException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import net.foodeals.common.dto.request.UpdateReason;
import net.foodeals.common.dto.response.UpdateDetails;
import net.foodeals.contract.domain.entities.enums.ContractStatus;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.organizationEntity.application.dtos.requests.CreateAssociationDto;
import net.foodeals.organizationEntity.application.dtos.responses.*;
import net.foodeals.organizationEntity.application.dtos.requests.CreateAnOrganizationEntityDto;
import net.foodeals.organizationEntity.application.services.OrganizationEntityService;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;
import net.foodeals.organizationEntity.domain.entities.enums.OrganizationsType;
import net.foodeals.organizationEntity.domain.exceptions.AssociationCreationException;
import net.foodeals.organizationEntity.domain.exceptions.AssociationUpdateException;
import net.foodeals.organizationEntity.infrastructure.seeders.ModelMapper.OrganizationEntityModelMapper;
import net.foodeals.payment.application.dto.response.PartnerInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/organizations")
public class OrganizationEntityController {
    private final OrganizationEntityService organizationEntityService;
    private final OrganizationEntityModelMapper modelMapper;

    public OrganizationEntityController(OrganizationEntityService organizationEntityService, OrganizationEntityModelMapper modelMapper) {
        this.organizationEntityService = organizationEntityService;
        this.modelMapper = modelMapper;
    }


        @GetMapping("/associations/form-data/{id}")
        @Transactional
        public ResponseEntity<AssociationFormData> getAssociationFormData(@PathVariable UUID id) {
            OrganizationEntity organizationEntity = organizationEntityService.findById(id);
            return new ResponseEntity<>(this.modelMapper.mapToAssociationFormData(organizationEntity), HttpStatus.OK);
        }

    @PostMapping(value = "/partners/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> addAnOrganizationEntity(@RequestPart("dto") CreateAnOrganizationEntityDto createAnOrganizationEntityDto, @RequestPart(value = "logo", required = false) MultipartFile logo, @RequestPart(value = "cover", required = false) MultipartFile cover) throws Exception {
        try {
            OrganizationEntity  organizationEntity = this.organizationEntityService.createAnewOrganizationEntity(createAnOrganizationEntityDto, logo, cover);
            if (organizationEntity == null) {
                throw new Exception("Failed to create organization entity");
            }
            return new ResponseEntity<>(organizationEntity.getType().equals(EntityType.DELIVERY_PARTNER) ? this.modelMapper.mapDeliveryPartners(organizationEntity) : this.modelMapper.mapOrganizationEntity(organizationEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new Exception("Failed to create partner: ");
        }
    }


    @PostMapping(value = "/associations/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> createAssociation(
            @RequestPart("dto") CreateAssociationDto createAssociationDto,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "cover", required = false) MultipartFile cover) {
        try {
            UUID associationId = this.organizationEntityService.createAssociation(createAssociationDto, logo, cover);
            return ResponseEntity.ok(associationId);
        } catch (AssociationCreationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PutMapping(value = "associations/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> updateAssociation(
            @PathVariable("id") UUID id,
            @Valid  @RequestPart("dto") CreateAssociationDto updateAssociationDto,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "cover", required = false) MultipartFile cover) {
        try {
            UUID updatedAssociationId = this.organizationEntityService.updateAssociation(id, updateAssociationDto, cover, logo);
            return ResponseEntity.ok(updatedAssociationId);
        } catch (AssociationUpdateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping(value = "/partners/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> updateOrganizationEntity(@PathVariable("id") UUID id, @RequestPart("dto") CreateAnOrganizationEntityDto updateOrganizationEntityDto, @RequestPart(value = "logo", required = false) MultipartFile logo, @RequestPart(value = "cover", required = false) MultipartFile cover) throws Exception {
        try {
            OrganizationEntity organizationEntity = this.organizationEntityService.updateOrganizationEntity(id, updateOrganizationEntityDto, logo, cover);
            return new ResponseEntity<>(organizationEntity.getType().equals(EntityType.DELIVERY_PARTNER) ? this.modelMapper.mapDeliveryPartners(organizationEntity) : this.modelMapper.mapOrganizationEntity(organizationEntity), HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/partners/form-data/{id}")
    @Transactional
    public ResponseEntity<?> getFormData(@PathVariable("id") UUID id) {
        OrganizationEntity organizationEntity = this.organizationEntityService.getOrganizationEntityById(id);
        return new ResponseEntity<>(organizationEntity.getType().equals(EntityType.DELIVERY_PARTNER) ?   this.modelMapper.convertToDeliveryFormData(organizationEntity) : this.modelMapper.convertToFormData(organizationEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Transactional
    public ResponseEntity<Void> deleteOrganization(
            @PathVariable UUID uuid,
            @RequestBody UpdateReason request) {
        organizationEntityService.deleteOrganization(uuid, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/partners/deleted")
    @Transactional
    public ResponseEntity<Page<?>> getDeletedOrganizationsPaginated(Pageable pageable, @RequestParam(value = "type", required = true) List<EntityType> type) {
        Page<OrganizationEntity> deletedOrganizations = organizationEntityService.getDeletedOrganizationsPaginated(pageable, type);

        // Create sets of related types
        Set<EntityType> associationTypes = Set.of(EntityType.ASSOCIATION, EntityType.FOOD_BANK_ASSO, EntityType.FOOD_BANK);
        Set<EntityType> deliveryTypes = Set.of(EntityType.DELIVERY_PARTNER);
        Set<EntityType> partnerTypes = Set.of(EntityType.NORMAL_PARTNER, EntityType.PARTNER_WITH_SB);

        // Check if type list has any common elements with our defined sets
        if (!Collections.disjoint(type, associationTypes)) {
            return new ResponseEntity<>(deletedOrganizations.map(d -> this.modelMapper.mapToAssociation(d)), HttpStatus.OK);
        }
        else if (!Collections.disjoint(type, deliveryTypes)) {
            return new ResponseEntity<>(deletedOrganizations.map(d -> this.modelMapper.mapDeliveryPartners(d)), HttpStatus.OK);
        }
        else if (!Collections.disjoint(type, partnerTypes)) {
            return new ResponseEntity<>(deletedOrganizations.map(d -> this.modelMapper.mapOrganizationEntity(d)), HttpStatus.OK);
        }
        else {
            return null;
        }
    }

    @GetMapping("/partners/{id}")
    @Transactional
    public ResponseEntity<OrganizationEntityDto> getOrganizationEntityById(@PathVariable("id") UUID id) {
        OrganizationEntity organizationEntity = this.organizationEntityService.getOrganizationEntityById(id);
        OrganizationEntityDto organizationEntityDto = this.modelMapper.mapOrganizationEntity(organizationEntity);
        return new ResponseEntity<OrganizationEntityDto>(organizationEntityDto, HttpStatus.OK);
    }

    @GetMapping("/{uuid}/deletion-details")
    @Transactional
    public ResponseEntity<Page<UpdateDetails>> getDeletionDetails(@PathVariable UUID uuid, Pageable pageable) {
        Page<UpdateDetails> deletionDetails = organizationEntityService.getDeletionDetails(uuid, pageable);
        return ResponseEntity.ok(deletionDetails);
    }

    /**
     * Retrieve a list of organization entities with the given filters.
     *
     * @param startDate the start date for the filter, if provided
     * @param endDate the end date for the filter, if provided
     * @param types the types of the organization entity
     * @param names the names of the organization entity
     * @param solutions the solutions of the organization entity
     * @param cityId the city id of the organization entity
     * @param collabId the collaborator id of the organization entity
     * @param email the email of the organization entity
     * @param phone the phone of the organization entity
     * @param deletedAt whether the organization entity is deleted or not
     * @param organizationsType which type of organization entities to return
     * @param status the contract status of the organization entity
     * @param pageable the pageable object
     * @return a list of organization entities with the given filters
     */
    @GetMapping("/partners")
    @Transactional
    public ResponseEntity<?> getOrganizationEntities(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(value = "types", required = true)
            List<EntityType> types,

            @RequestParam(value = "names", required = false)
            String names,

            @RequestParam(value = "solutions", required = false)
            List<String> solutions,

            @RequestParam(value = "cityId", required = false)
            UUID cityId,

            @RequestParam(value = "collabId", required = false)
            Long collabId,

            @RequestParam(value = "email", required = false)
            String email,

            @RequestParam(value = "phone", required = false)
            String phone,

            @RequestParam(value = "deletedAt", required = true)
            Boolean deletedAt,

            @RequestParam(value = "organizationsType", required = true)
            OrganizationsType organizationsType,

            @RequestParam(value = "status", required = false)
            ContractStatus status, // New parameter for contract status

            Pageable pageable
    ) {
        List<String> namesList = names != null
                ? Arrays.stream(names.split(","))
                .collect(Collectors.toList())
                : null;


        OrganizationEntityFilter filter = OrganizationEntityFilter.builder()
                .startDate(startDate != null ? startDate.atStartOfDay(ZoneOffset.UTC).toInstant() : null)
                .endDate(endDate != null ? endDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant() : null)
                .types(types)
                .names(namesList)
                .email(email)
                .phone(phone)
                .solutions(solutions != null ? solutions : new ArrayList<String>())
                .cityId(cityId)
                .collabId(collabId)
                .deletedAt(deletedAt)
                .contractStatus(status) // Set the contract status in the filter
                .build();

        Page<OrganizationEntity> organizationEntities = organizationEntityService.getOrganizationEntitiesFilters(filter, pageable);
        return switch (organizationsType) {
            case PARTNERS -> ResponseEntity.ok(organizationEntities.map(modelMapper::mapOrganizationEntity));
            case DELIVERIES -> ResponseEntity.ok(organizationEntities.map(this.modelMapper::mapDeliveryPartners));
            case ASSOCIATIONS -> ResponseEntity.ok(organizationEntities.map(this.modelMapper::mapToAssociation));
            default -> ResponseEntity.badRequest().body("Invalid organization type");
        };
    }

    @GetMapping("/partners/search")
    @Transactional
    public ResponseEntity<Page<PartnerInfoDto>> searchPartners(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "id", required = false) UUID id,
            @RequestParam(name = "types", required = false) List<EntityType> types,
            @RequestParam(name = "deleted", required = false, defaultValue = "false") boolean includeDeleted,
            Pageable pageable) {
        return ResponseEntity.ok(organizationEntityService.searchPartnersByName(id, name, types, pageable, includeDeleted).map(this.modelMapper::convertToPartnerInfoDto));
    }


    @PostMapping(value = "/partners/validate/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<String> validateOrganizationEntity(@PathVariable("id") UUID id, @RequestPart("document") MultipartFile file) throws Exception {
        try {
            return new ResponseEntity<String>(this.organizationEntityService.validateOrganizationEntity(id, file), HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("failed to validate organization entity: ");
        }
    }

    @GetMapping("/partners/contracts/{id}")
    @Transactional
    public ResponseEntity<byte[]> getContractDocument(@PathVariable("id") UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("Content-Disposition", "attachment; filename=contract.pdf");

        return new ResponseEntity<byte[]>(this.organizationEntityService.getContractDocument(id), headers, HttpStatus.OK);
    }

    @GetMapping("/cities/search")
    @Transactional
    public ResponseEntity<Page<CityResponse>> searchCities(
            @RequestParam(name = "city") String cityName,
@RequestParam(name = "types", required = false) List<EntityType> types,
            @RequestParam(name = "country") String countryName,
            Pageable pageable) {
        return ResponseEntity.ok(organizationEntityService.searchCitiesByOrganizationAddress(types, cityName, countryName, pageable).map(this.modelMapper::convertToCityResponse));
    }


}

// 1 week
