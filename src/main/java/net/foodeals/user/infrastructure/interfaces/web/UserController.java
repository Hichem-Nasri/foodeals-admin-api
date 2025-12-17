package net.foodeals.user.infrastructure.interfaces.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.Utils.GeoUtils;
import net.foodeals.common.dto.request.UpdateReason;
import net.foodeals.common.dto.response.UpdateDetails;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.location.application.dtos.responses.CityResponse;
import net.foodeals.location.application.dtos.responses.RegionResponse;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.TrackingStep;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.order.domain.repositories.TrackingStepRepository;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.payment.application.dto.response.PartnerInfoDto;
import net.foodeals.user.application.dtos.requests.SubentityUsersResponse;
import net.foodeals.user.application.dtos.requests.UpdateStatusRequest;
import net.foodeals.user.application.dtos.requests.UserFilter;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.dtos.responses.*;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.awt.desktop.SystemEventListener;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;
	private final ModelMapper mapper;
	private final OrganizationEntityRepository organizationRepository;
	private final SubEntityRepository subEntityRepository;
	private final OrderRepository orderRepository;
	private final TrackingStepRepository trackingStepRepository;

	@GetMapping("organizations/search")
	@Transactional
	public ResponseEntity<Page<SimpleUserDto>> searchUsersOrganizations(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "types") List<EntityType> types, Pageable pageable) {
		UserSearchOrganizationFilter filter = new UserSearchOrganizationFilter(name, types);
		Page<User> users = service.filterUsersOrganization(filter, pageable);
		Page<SimpleUserDto> userResponses = users.map(u -> this.mapper.map(u, SimpleUserDto.class));
		return ResponseEntity.ok(userResponses);
	}

	@GetMapping("/subentities/search")
	@Transactional
	public ResponseEntity<Page<SimpleUserDto>> searchUsersSubentities(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = true, name = "organizationId") UUID organizationId, Pageable pageable) {
		UserSearchSubentityFilters filter = new UserSearchSubentityFilters(name, organizationId);
		Page<User> users = service.filterUsersSubentity(filter, pageable);
		Page<SimpleUserDto> userResponses = users.map(u -> this.mapper.map(u, SimpleUserDto.class));
		return ResponseEntity.ok(userResponses);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> deleteUser(@PathVariable Integer id, @RequestBody UpdateReason request) {
		service.deleteUser(id, request);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{uuid}/deletion-details")
	@Transactional
	public ResponseEntity<Page<UpdateDetails>> getDeletionDetails(@PathVariable Integer uuid, Pageable pageable) {
		Page<UpdateDetails> deletionDetails = service.getDeletionDetails(uuid, pageable);
		return ResponseEntity.ok(deletionDetails);
	}

	@GetMapping("/organizations/{organizationId}")
	@Transactional
	public ResponseEntity<OrganizationUsersResponse> getUsersByOrganization(
			@PathVariable("organizationId") UUID organizationId, @PageableDefault(size = 10) Pageable pageable,

			@RequestParam(value = "names", required = false) String names,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "roleName", required = false) String roleName,
			@RequestParam(value = "entityTypes", required = true) List<EntityType> entityTypes,
			@RequestParam(value = "solutions", required = false) List<String> solutions,

			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(value = "deletedAt", required = true) Boolean deletedAt) {

		List<String> namesList = names != null ? Arrays.stream(names.split(",")).collect(Collectors.toList()) : null;

		// Retrieve organization
		OrganizationEntity organization = organizationRepository.findById(organizationId)
				.orElseThrow(() -> new EntityNotFoundException("Organization not found with ID: " + organizationId));

		// Create PartnerInfoDto
		PartnerInfoDto partnerInfoDto = PartnerInfoDto.builder().id(organization.getId()).name(organization.getName())
				.avatarPath(organization.getAvatarPath())
				.city(organization.getAddress().getRegion().getCity().getName()).build();

		// Create filter
		UserFilter filter = UserFilter.builder().names(namesList).phone(phone).city(city).region(region).email(email)
				.roleName(roleName).entityTypes(entityTypes)
				.solutions(solutions != null ? solutions : new ArrayList<String>())
				.startDate(startDate != null ? startDate.atStartOfDay(ZoneOffset.UTC).toInstant() : null)
				.endDate(endDate != null ? endDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant() : null)
				.deletedAt(deletedAt).build();

		// Get users
		Page<Object> userInfoPage = this.service.getUsersByOrganization(organizationId, filter, pageable);

		// Create and return response
		return ResponseEntity.ok(new OrganizationUsersResponse(partnerInfoDto, userInfoPage));
	}

	@GetMapping("/subentities/{subentityId}")
	@Transactional
	public ResponseEntity<SubentityUsersResponse> getSubentitiesUsers(@PathVariable("subentityId") UUID subentityId,
			@PageableDefault(size = 10) Pageable pageable,
			@RequestParam(value = "names", required = false) String names,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "roleName", required = false) String roleName,
			@RequestParam(value = "subEntityTypes", required = true) List<SubEntityType> entityTypes,
			@RequestParam(value = "solutions", required = false) List<String> solutions,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(value = "deletedAt", required = true) Boolean deletedAt) {

		SubEntity subEntity = subEntityRepository.findById(subentityId)
				.orElseThrow(() -> new EntityNotFoundException("Subentity not found with ID: " + subentityId));

		List<String> namesList = names != null ? Arrays.stream(names.split(",")).collect(Collectors.toList()) : null;

		// Create PartnerInfoDto
		PartnerInfoDto partnerInfoDto = PartnerInfoDto.builder().id(subEntity.getId()).name(subEntity.getName())
				.avatarPath(subEntity.getAvatarPath()).city(subEntity.getAddress().getRegion().getCity().getName())
				.build();
		UserFilter filter = UserFilter.builder().names(namesList).phone(phone).city(city).region(region).email(email)
				.roleName(roleName).subEntityTypes(entityTypes)
				.solutions(solutions != null ? solutions : new ArrayList<String>())
				.startDate(startDate != null ? startDate.atStartOfDay(ZoneOffset.UTC).toInstant() : null)
				.endDate(endDate != null ? endDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant() : null)
				.deletedAt(deletedAt).build();

		Page<UserInfoDto> userInfoPage = this.service.getUsersBySubEntity(subentityId, filter, pageable);
		return ResponseEntity.ok(new SubentityUsersResponse(partnerInfoDto, userInfoPage));
	}

	@GetMapping("/sells-managers")
	@Transactional
	public ResponseEntity<Page<SimpleUserDto>> sellsManagers(@RequestParam(required = false, name = "name") String name,
			Pageable pageable) {
		Page<User> users = service.getSellsManagers(name, pageable);
		Page<SimpleUserDto> userResponses = users.map(u -> this.mapper.map(u, SimpleUserDto.class));
		return ResponseEntity.ok(userResponses);
	}

	@GetMapping
	@Transactional
	public ResponseEntity<List<UserResponse>> getAll() {
		final List<UserResponse> responses = service.findAll().stream()
				.map(user -> mapper.map(user, UserResponse.class)).toList();
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/page/{pageNumber}/{pageSize}")
	@Transactional
	public ResponseEntity<Page<UserResponse>> getAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize) {
		final Page<UserResponse> responses = service.findAll(pageNumber, pageSize)
				.map(user -> mapper.map(user, UserResponse.class));
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity<UserResponse> getById(@PathVariable Integer id) {
		final UserResponse response = mapper.map(service.findById(id), UserResponse.class);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/email/{email}")
	@Transactional
	public ResponseEntity<UserResponse> getByEmail(@PathVariable String email) {
		final UserResponse response = mapper.map(service.findByEmail(email), UserResponse.class);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {
		final UserResponse response = mapper.map(service.create(request), UserResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	@Transactional
	public ResponseEntity<UserResponse> update(@PathVariable Integer id, @RequestBody @Valid UserRequest request) {
		final UserResponse response = mapper.map(service.update(id, request), UserResponse.class);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/clients")
	@Transactional
	public ResponseEntity<Page<ClientDto>> getClientsData(Pageable page) {
		Page<User> clients = this.service.getClientsData(page);
		Page<ClientDto> clientDtos = clients.map(this.service::toClientDto);
		return new ResponseEntity<Page<ClientDto>>(clientDtos, HttpStatus.OK);
	}

	@GetMapping("/delivery-partners/{id}")
	@Transactional
	public ResponseEntity<DeliveryUsersResponse> getDeliveryUsers(Pageable pageable, @PathVariable("id") UUID id) {

		// Retrieve organization
		OrganizationEntity organization = organizationRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Organization not found with ID: " + id));

		// Create PartnerInfoDto
		PartnerInfoDto partnerInfoDto = PartnerInfoDto.builder().id(organization.getId()).name(organization.getName())
				.avatarPath(organization.getAvatarPath())
				.city(organization.getAddress().getRegion().getCity().getName()).build();

		Page<User> users = this.service.getOrganizationUsers(pageable, id);
		Page<DeliveryPartnerUserDto> deliveyPartnerUsersDtos = users
				.map(user -> this.mapper.map(user, DeliveryPartnerUserDto.class));
		return new ResponseEntity<DeliveryUsersResponse>(
				new DeliveryUsersResponse(partnerInfoDto, deliveyPartnerUsersDtos), HttpStatus.OK);
	}

	@GetMapping("/associations/{id}")
	@Transactional
	public ResponseEntity<Page<AssociationsUsersDto>> getAssociationUsers(Pageable pageable,
			@PathVariable("id") UUID id) {
		Page<User> users = this.service.getOrganizationUsers(pageable, id);
		Page<AssociationsUsersDto> associationsUsersDtoPage = users
				.map(user -> this.mapper.map(user, AssociationsUsersDto.class));
		return new ResponseEntity<Page<AssociationsUsersDto>>(associationsUsersDtoPage, HttpStatus.OK);
	}

	@GetMapping("/{userId}/profile")
	@Transactional
	public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Integer userId) {
		User user = service.findById(userId);
		return ResponseEntity.ok(this.service.mapUserToUserProfileDTO(user));
	}

	@GetMapping("/cities/organizations/search")
	@Transactional
	public ResponseEntity<Page<CityResponse>> searchCitiesOrganizations(@RequestParam(name = "city") String cityName,
			@RequestParam(name = "organizationId") UUID organizationId,

			Pageable pageable) {
		return ResponseEntity.ok(service.findCitiesUsersByEntityTypeAndCityName(organizationId, cityName, pageable)
				.map(city -> this.mapper.map(city, CityResponse.class)));
	}

	@GetMapping("/regions/organizations/search")
	@Transactional
	public ResponseEntity<Page<RegionResponse>> searchRegionsOrganizations(
			@RequestParam(name = "region") String regionName,
			@RequestParam(name = "organizationId") UUID organizationId,

			Pageable pageable) {
		return ResponseEntity.ok(service.findRegionsUsersByEntityTypeAndRegionName(organizationId, regionName, pageable)
				.map(region -> this.mapper.map(region, RegionResponse.class)));
	}

	@GetMapping("/cities/subentities/search")
	@Transactional
	public ResponseEntity<Page<CityResponse>> SubentityUsersSearchCities(@RequestParam(name = "city") String cityName,
			@RequestParam(name = "subentityId") UUID subentityId,

			Pageable pageable) {
		return ResponseEntity.ok(service.findCitiesUsersBySubentityAndCityName(subentityId, cityName, pageable)
				.map(city -> this.mapper.map(city, CityResponse.class)));
	}

	@GetMapping("/regions/subentities/search")
	@Transactional
	public ResponseEntity<Page<RegionResponse>> searchRegionsSubentities(
			@RequestParam(name = "region") String regionName, @RequestParam(name = "subentityId") UUID subentityId,

			Pageable pageable) {
		return ResponseEntity.ok(service.findRegionsUsersBySubentityAndRegionName(subentityId, regionName, pageable)
				.map(region -> this.mapper.map(region, RegionResponse.class)));
	}

	@GetMapping(value = "/all-clients", produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<ClientPageResponseDto> getClients(@PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(service.getClients(pageable));
	}

	@GetMapping(value = "/clients/{id}")
	public ResponseEntity<ClientResponseDto> getClient(@PathVariable Integer id) {

		return ResponseEntity.ok(service.getClient(id));
	}

	@DeleteMapping("/clients/{id}")
	public ResponseEntity<Void> archiveClient(@PathVariable Integer id, @RequestParam(name = "reason") String reason,
			@RequestParam(name = "motif") String motif) {
		service.archiveClient(id, reason, motif);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/clients/{clientId}/commands/{commandId}/tracking")
	public ResponseEntity<Map<String, Object>> getOrderTracking(@PathVariable("clientId") String clientId,
			@PathVariable("commandId") String commandId) {

		OrderTrackingResponseDto tracking = service.getOrderTrackingDetails(clientId, commandId);
		Map<String, Object> response = new HashMap();
		response.put("success", true);
		response.put("data", tracking);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/clients/{clientId}/{commandId}/driver-location")
	public ResponseEntity<Map<String, Object>> getDriverLocation(@PathVariable Integer clientId,
			@PathVariable UUID commandId) {

		DriverLocationResponseDto locationDto = service.getDriverLocation(clientId, commandId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("data", locationDto);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/clients/{clientId}/commands/{commandId}/status")
	public ResponseEntity<?> updateStatus(@PathVariable Integer clientId, @PathVariable UUID commandId,
			@RequestBody UpdateStatusRequest request) {
		Order order = orderRepository.findByIdAndClientId(commandId, clientId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		order.setStatus(OrderStatus.valueOf(request.getStatus().toUpperCase()));
		order.setUpdatedAt(Instant.now());

		// TrackingStep (optionnel)
		TrackingStep step = new TrackingStep();
		step.setOrder(order);
		step.setStatus(order.getStatus().name());
		step.setTimestamp(Instant.now());
		step.setLatitude(request.getLatitude());
		step.setLongitude(request.getLongitude());
		step.setDescription(request.getNotes());
		trackingStepRepository.save(step);

		orderRepository.save(order);

		return ResponseEntity.ok(Map.of("success", true, "message", "Order status updated successfully", "data",
				Map.of("orderId", order.getId().toString(), "status", order.getStatus().name().toLowerCase(),
						"updatedAt", order.getUpdatedAt().toString())));
	}

	@GetMapping("/clients/{clientId}/commands/{commandId}/route")
	@Transactional
	public ResponseEntity<?> getRoute(
	    @PathVariable Integer clientId,
	    @PathVariable UUID commandId
	) {
	    Order order = orderRepository.findByIdAndClientId(commandId, clientId)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

	    User driver = order.getDelivery().getDeliveryBoy();

	    Coordinates from = driver.getCoordinates();
	    Coordinates to = order.getClient().getCoordinates();

	    double distanceKm = GeoUtils.haversine(from, to);
	    String duration = GeoUtils.estimateDuration(distanceKm, driver.getVehicleType().name());

	    return ResponseEntity.ok(Map.of(
	        "success", true,
	        "data", Map.of(
	            "route", Map.of(
	                "waypoints", List.of(
	                    Map.of("latitude", from.latitude(), "longitude", from.longitude(), "type", "current_location"),
	                    Map.of("latitude", to.latitude(), "longitude", to.longitude(), "type", "destination")
	                ),
	                "distance", String.format("%.1f km", distanceKm),
	                "estimatedDuration", duration,
	                "polyline", "encoded_polyline_string_for_map_rendering" // mock pour l’instant
	            )
	        )
	    ));
	}

	
	@GetMapping("/collaborators")
	public Page<CollaboratorResponseDTO> getCollaborators(@PageableDefault(size = 10) Pageable pageable) {
		return service.getCollaborators(pageable);
	}

	@GetMapping("/archived-collaborators")
	public Page<CollaboratorResponseDTO> getArchivedCollaborators(@PageableDefault(size = 10) Pageable pageable) {
		return service.getArchivedCollaborators(pageable);
	}

	@GetMapping("/collaborators/statistics")
	public CollaboratorStatisticsDTO getStatistics() {
		return service.getCollaboratorStatistics();
	}

	@PostMapping(path = "/collaborators/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CollaboratorDetailsResponseDto> create(@RequestPart("collaborator") CollaboratorAddDto dto,
			@RequestPart(value = "avatar", required = false) MultipartFile avatar,
			@RequestPart(value = "internalDocument", required = false) List<MultipartFile> internalDocument,
			@RequestPart(value = "personalDocument", required = false) List<MultipartFile> personalDocument) {

		String avatarPath = null;
		if (avatar != null) {
			avatarPath = service.saveFile(avatar);
			dto.setAvatar(avatarPath);
		}

		List<String> internalDocumentPaths = new ArrayList<>();
		if (internalDocument != null) {
			for (MultipartFile file : internalDocument) {
				internalDocumentPaths.add(service.saveFile(file));
			}
		}

		List<String> personalDocumentPaths = new ArrayList<>();
		if (personalDocument != null) {
			for (MultipartFile file : personalDocument) {
				personalDocumentPaths.add(service.saveFile(file));
			}
		}

		CollaboratorDetailsResponseDto created = service.createCollaborator(dto, internalDocumentPaths,
				personalDocumentPaths);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/collaborators/update/{id}")
	public ResponseEntity<CollaboratorDetailsResponseDto> updateCollaborator(@PathVariable Integer id,
			@RequestPart("collaborator") CollaboratorAddDto dto,
			@RequestPart(value = "avatar", required = false) MultipartFile avatar,
			@RequestPart(value = "internalDocument", required = false) List<MultipartFile> internalDocument,
			@RequestPart(value = "personalDocument", required = false) List<MultipartFile> personalDocument) {

		String avatarPath = null;
		if (avatar != null) {
			avatarPath = service.saveFile(avatar);
			dto.setAvatar(avatarPath);
		}

		List<String> internalDocumentPaths = new ArrayList<>();
		if (internalDocument != null) {
			for (MultipartFile file : internalDocument) {
				internalDocumentPaths.add(service.saveFile(file));
			}
		}

		List<String> personalDocumentPaths = new ArrayList<>();
		if (personalDocument != null) {
			for (MultipartFile file : personalDocument) {
				personalDocumentPaths.add(service.saveFile(file));
			}
		}

		CollaboratorDetailsResponseDto updated = service.updateCollaborator(id, dto, internalDocumentPaths,
				personalDocumentPaths);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/collaborators/{id}")
	public ResponseEntity<Void> archiveCollaborator(@PathVariable Integer id,
			@RequestParam(name = "reason") String reason, @RequestParam(name = "motif") String motif) {
		service.archiveCollaborator(id, reason, motif);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/collaborators/details/{id}")
	public ResponseEntity<CollaboratorDetailsResponseDto> getCollaboratorDetails(@PathVariable Integer id) {
		CollaboratorDetailsResponseDto dto = service.getCollaboratorDetails(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/collaborators/{id}/absences")
	public List<AbsenceDto> getAbsences(@PathVariable Integer id) {
		return service.getAbsences(id);
	}

	@PostMapping("/collaborators/{id}/{id2}/absences")
	public AbsenceDto createAbsence(@PathVariable Integer id, @PathVariable Integer id2, @RequestBody AbsenceDto dto) {
		return service.createAbsence(id, id2, dto);
	}

	@GetMapping("/by-category/{categorie}")
	public ResponseEntity<Page<UserResponse>> getAllByCategorie(@PathVariable String categorie, Pageable pageable) {
		List<User> users = new ArrayList<>();
		List<User> clients = new ArrayList<>();
		List<User> regionalManagers = new ArrayList<>();
		List<User> managers = new ArrayList<>();

		if (categorie.equalsIgnoreCase("all")) {
			List allUsersPage = service.findAll();
			List<UserResponse> userResponses = allUsersPage.stream().map(user -> mapper.map(user, UserResponse.class))
					.toList();

			Page<UserResponse> responsePage = new PageImpl<>(userResponses, pageable, allUsersPage.size());
			return ResponseEntity.ok(responsePage);
		}
		if (categorie.equalsIgnoreCase("client")) {
			// Charger tous les utilisateurs des deux rôles
			clients = service.findByRoleName("CLIENT", pageable).getContent();
		}

		if (categorie.equalsIgnoreCase("partner")) {
			// Charger tous les utilisateurs des deux rôles
			regionalManagers = service.findByRoleName("MANAGER_REGIONALE", pageable).getContent();
			managers = service.findByRoleName("MANAGER", pageable).getContent();
		}
		// Fusion sans doublons
		Set<User> mergedUsers = new HashSet();
		mergedUsers.addAll(regionalManagers);
		mergedUsers.addAll(managers);
		mergedUsers.addAll(clients);
		List<User> mergedList = new ArrayList<>(mergedUsers);

		// Pagination en mémoire
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), mergedList.size());
		if (start >= end)
			return ResponseEntity.ok(Page.empty());

		List<User> pagedUsers = mergedList.subList(start, end);
		List<UserResponse> userResponses = pagedUsers.stream().map(user -> mapper.map(user, UserResponse.class))
				.toList();

		Page<UserResponse> responsePage = new PageImpl<>(userResponses, pageable, mergedList.size());
		return ResponseEntity.ok(responsePage);

	}

}