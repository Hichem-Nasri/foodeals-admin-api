package net.foodeals.user.application.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.dto.request.UpdateReason;
import net.foodeals.common.dto.response.UpdateDetails;
import net.foodeals.common.entities.DeletionReason;
import net.foodeals.common.services.EmailService;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.location.application.services.AddressService;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.entities.Region;
import net.foodeals.location.domain.entities.State;
import net.foodeals.location.domain.repositories.AddressRepository;
import net.foodeals.location.domain.repositories.CityRepository;
import net.foodeals.location.domain.repositories.CountryRepository;
import net.foodeals.location.domain.repositories.RegionRepository;
import net.foodeals.location.domain.repositories.StateRepository;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.BoxItem;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.enums.VehicleType;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.organizationEntity.domain.entities.Contact;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;
import net.foodeals.payment.application.dto.response.PartnerInfoDto;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.Rayon;
import net.foodeals.product.domain.repositories.RayonRepository;
import net.foodeals.user.application.dtos.requests.UserFilter;
import net.foodeals.user.application.dtos.requests.UserRequest;
import net.foodeals.user.application.dtos.responses.AbsenceDto;
import net.foodeals.user.application.dtos.responses.ClientDto;
import net.foodeals.user.application.dtos.responses.ClientPageResponseDto;
import net.foodeals.user.application.dtos.responses.ClientResponseDto;
import net.foodeals.user.application.dtos.responses.ClientStatsDto;
import net.foodeals.user.application.dtos.responses.CollaboratorAddDto;
import net.foodeals.user.application.dtos.responses.CollaboratorDetailsResponseDto;
import net.foodeals.user.application.dtos.responses.CollaboratorRefDto;
import net.foodeals.user.application.dtos.responses.CollaboratorResponseDTO;
import net.foodeals.user.application.dtos.responses.CollaboratorStatisticsDTO;
import net.foodeals.user.application.dtos.responses.DeliveryAddressDto;
import net.foodeals.user.application.dtos.responses.DeliveryPartnerUserDto;
import net.foodeals.user.application.dtos.responses.DocumentDto;
import net.foodeals.user.application.dtos.responses.DriverDto;
import net.foodeals.user.application.dtos.responses.DriverLocationResponseDto;
import net.foodeals.user.application.dtos.responses.LocationDriverDto;
import net.foodeals.user.application.dtos.responses.LocationDto;
import net.foodeals.user.application.dtos.responses.OrderItemDto;
import net.foodeals.user.application.dtos.responses.OrderTrackingResponseDto;
import net.foodeals.user.application.dtos.responses.OrdersClientResponseDto;
import net.foodeals.user.application.dtos.responses.OrganizationInfo;
import net.foodeals.user.application.dtos.responses.RestaurantDto;
import net.foodeals.user.application.dtos.responses.SimpleUserDto;
import net.foodeals.user.application.dtos.responses.StoreResponseDto;
import net.foodeals.user.application.dtos.responses.TimelineEventDto;
import net.foodeals.user.application.dtos.responses.UserInfoDto;
import net.foodeals.user.application.dtos.responses.UserProfileDTO;
import net.foodeals.user.application.dtos.responses.UserSearchOrganizationFilter;
import net.foodeals.user.application.dtos.responses.UserSearchSubentityFilters;
import net.foodeals.user.application.dtos.responses.WorkingHoursDTO;
import net.foodeals.user.application.services.RoleService;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.Absence;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.entities.UserInternalDocument;
import net.foodeals.user.domain.entities.UserPersonalDocument;
import net.foodeals.user.domain.entities.UserStatus;
import net.foodeals.user.domain.entities.WorkingHours;
import net.foodeals.user.domain.exceptions.UserNotFoundException;
import net.foodeals.user.domain.repositories.AbsenceRepository;
import net.foodeals.user.domain.repositories.RoleRepository;
import net.foodeals.user.domain.repositories.UserInternalDocumentRepository;
import net.foodeals.user.domain.repositories.UserPersonalDocumentRepository;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;

@Service
@Transactional
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
	private final UserRepository repository;
	private final RoleRepository roleRepository;
	private final RayonRepository rayonRepository;
	private final AddressRepository addressRepository;
	private final AbsenceRepository absenceRepository;
	private final RegionRepository regionRepository;
	private final StateRepository stateRepository;
	private final CountryRepository countryRepository;
	private final CityRepository cityRepository;
	private final OrderRepository orderRepository;
	private final DealRepository dealRepository;
	private final BoxRepository boxRepository;
	private final UserPersonalDocumentRepository personalDocumentRepository;
	private final UserInternalDocumentRepository internalDocumentRepository;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final OrganizationEntityRepository organizationEntityRepository;
	private final AddressService addressService;
	private final EmailService emailService;

	@Value("${upload.directory}")
	private String uploadDir;

	@Transactional
	public UserProfileDTO mapUserToUserProfileDTO(User user) {
		UserProfileDTO dto = new UserProfileDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setAvatarPath(user.getAvatarPath());
		dto.setEmail(user.getEmail());
		dto.setPhone(user.getPhone());
		dto.setRole(user.getRole().getName());
		dto.setOrganization(user.getOrganizationEntity().getName());
		dto.setStatus(user.getStatus());
		dto.setGender(user.getGender());
		dto.setNationalId(user.getNationalId());
		dto.setNationality(user.getNationality());

		dto.setWorkingHours(mapWorkingHoursToDTO(user.getWorkingHours()));
		dto.setOrganizationInfo(mapUserToOrganizationInfo(user));

		return dto;
	}

	@Transactional
	public OrganizationInfo mapUserToOrganizationInfo(User user) {
		Contact responsibleContact = null;
		String phone = null;
		String email = null;

		PartnerInfoDto organizationPartner = user.getOrganizationEntity() != null
				? new PartnerInfoDto(user.getOrganizationEntity().getId(), user.getOrganizationEntity().getName(),
						user.getOrganizationEntity().getAvatarPath(),
						user.getOrganizationEntity().getAddress().getRegion().getCity().getName())
				: null;

		PartnerInfoDto subentityPartner = user.getSubEntity() != null ? new PartnerInfoDto(user.getSubEntity().getId(),
				user.getSubEntity().getName(), user.getSubEntity().getAvatarPath(),
				user.getSubEntity().getAddress().getRegion().getCity().getName()) : null;

		/*
		 * String rayon = user.getRayon() != null ? user.getRayon().getName() : null;
		 */
		SimpleUserDto manager = user.getResponsible() != null ? new SimpleUserDto(user.getResponsible().getId(),
				user.getResponsible().getName(), user.getResponsible().getAvatarPath()) : null;

		String city = user.getAddress() != null && user.getAddress().getRegion() != null
				? user.getAddress().getRegion().getCity().getName()
				: null;

		String region = user.getAddress() != null && user.getAddress().getRegion() != null
				? user.getAddress().getRegion().getName()
				: null;

		List<String> solutions = user.getSolutions() != null
				? user.getSolutions().stream().map(Solution::getName).collect(Collectors.toList())
				: null;

		if (user.getSubEntity() != null && !user.getSubEntity().getContacts().isEmpty()) {
			responsibleContact = user.getSubEntity().getContacts().stream().filter(Contact::isResponsible).findFirst()
					.orElse(user.getSubEntity().getContacts().get(0));
		} else if (user.getOrganizationEntity() != null && !user.getOrganizationEntity().getContacts().isEmpty()) {
			responsibleContact = user.getOrganizationEntity().getContacts().stream().filter(Contact::isResponsible)
					.findFirst().orElse(user.getOrganizationEntity().getContacts().get(0));
		}

		if (responsibleContact != null) {
			phone = responsibleContact.getPhone();
			email = responsibleContact.getEmail();
		}

		return new OrganizationInfo(organizationPartner, subentityPartner, null, manager, city, region, solutions,
				phone, email);
	}

	@Transactional
	public List<WorkingHoursDTO> mapWorkingHoursToDTO(List<WorkingHours> workingHours) {
		return workingHours.stream().map(wh -> {
			WorkingHoursDTO dto = new WorkingHoursDTO();
			dto.setDayOfWeek(wh.getDayOfWeek().name());
			dto.setMorningStart(wh.getMorningStart());
			dto.setMorningEnd(wh.getMorningEnd());
			dto.setAfternoonStart(wh.getAfternoonStart());
			dto.setAfternoonEnd(wh.getAfternoonEnd());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Page<Object> getUsersByOrganization(UUID organizationId, UserFilter filter, Pageable pageable) {
		Page<User> userPage = this.repository.findByOrganizationIdWithFilters(organizationId, filter, pageable);

		return userPage.map(user -> {
			if (filter.getEntityTypes() != null && filter.getEntityTypes().contains(EntityType.DELIVERY_PARTNER)) {
				return this.modelMapper.map(user, DeliveryPartnerUserDto.class);
			} else {
				LocalDateTime localDateTime = LocalDateTime.ofInstant(user.getCreatedAt(), ZoneId.systemDefault());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/y");
				String createdAt = localDateTime.format(formatter);

				return new UserInfoDto(createdAt, user.getId(), user.getRole().getName(), user.getName(),
						user.getAddress().getRegion().getCity().getName(), user.getAddress().getRegion().getName(),
						user.getAvatarPath(), user.getEmail(), user.getPhone());
			}
		});
	}

	@Override
	@Transactional
	public Page<UserInfoDto> getUsersBySubEntity(UUID subEntityId, UserFilter filter, Pageable pageable) {
		Page<User> userPage = this.repository.findBySubEntityIdWithFilters(subEntityId, filter, pageable);

		return userPage.map(user -> {
			LocalDateTime localDateTime = LocalDateTime.ofInstant(user.getCreatedAt(), ZoneId.systemDefault());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/y");
			String createdAt = localDateTime.format(formatter);

			return new UserInfoDto(createdAt, user.getId(), user.getRole().getName(), user.getName(),
					user.getAddress().getRegion().getCity().getName(), user.getAddress().getRegion().getName(),
					user.getAvatarPath(), user.getEmail(), user.getPhone());
		});
	}

	@Override
	@Transactional
	public Page<User> getSellsManagers(String name, Pageable pageable) {
		// final String email =
		// SecurityContextHolder.getContext().getAuthentication().getName();
		// Optional<User> user = this.repository.findByEmail(email);
		// if (user.isPresent()) {
		// return
		// this.repository.getSellsManagers(user.get().getOrganizationEntity().getId(),
		// name, "SALES_MANAGER", pageable);
		// } else {
		// throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
		// }
		return this.repository.findByRoleNameAndNameContaining("SALES_MANAGER", name, pageable);
	}

	@Override
	@Transactional
	public List<User> findAll() {
		return repository.findAll();
	}

	@Override
	@Transactional
	public Page<User> filterUsersOrganization(UserSearchOrganizationFilter filter, Pageable pageable) {
		return repository.findWithFiltersOrganization(filter, pageable);
	}

	@Override
	@Transactional
	public Page<User> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	@Transactional
	public Page<User> findAll(Pageable pageable) {
		return null;
	}

	@Override
	@Transactional
	public User findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@Override
	@Transactional
	public User findByEmail(String email) {
		return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
	}

	@Override
	@Transactional
	public User create(UserRequest request) {
		final User user = mapRelationsAndEncodePassword(modelMapper.map(request, User.class), request);
		return this.repository.save(user);
	}

	@Override
	@Transactional
	public User update(Integer id, UserRequest request) {
		final User existingUser = findById(id);
		modelMapper.map(request, existingUser);
		final User user = mapRelationsAndEncodePassword(existingUser, request);
		return repository.save(user);
	}

	@Override
	public void delete(Integer integer) {
	}

	@Override
	@Transactional
	public void delete(Integer id, UpdateReason reason) {
//        User user = findById(id);
//        user.markDeleted(reason);
//        this.repository.save(user);
	}

	@Transactional
	private User mapRelationsAndEncodePassword(User user, UserRequest request) {
		final Role role = roleService.findByName(request.roleName());
		user.setRole(role).setPassword(passwordEncoder.encode(user.getPassword()));
		user = this.repository.save(user);
		if (request.organizationEntityId() != null) {
			final OrganizationEntity organizationEntity = this.organizationEntityRepository
					.findById(request.organizationEntityId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
							"Organization not found with id " + request.organizationEntityId()));
			if (organizationEntity != null) {
				user.setOrganizationEntity(organizationEntity);
				organizationEntity.getUsers().add(user);
				Address address = organizationEntity.getAddress();
				user.setAddress(address);
				this.organizationEntityRepository.save(organizationEntity);
			}
		}
		return user;
	}

	@Override
	@Transactional
	public void deleteUser(Integer id, UpdateReason reason) {
		User user = this.repository.getEntity(id).orElseThrow(() -> new UserNotFoundException(id));
		DeletionReason deletionReason = DeletionReason.builder().details(reason.details()).reason(reason.reason())
				.type(reason.action()).build();
		user.getDeletionReasons().add(deletionReason);
		user.markDeleted(reason.action());
		this.repository.save(user);
	}

	@Override
	@Transactional
	public User save(User user) {
		return this.repository.save(user);
	}

	@Transactional
	@Override
	public Page<UpdateDetails> getDeletionDetails(Integer id, Pageable page) {
		User user = this.repository.getEntity(id).orElseThrow(() -> new UserNotFoundException(id));

		List<DeletionReason> deletionReasons = user.getDeletionReasons();

		int start = (int) page.getOffset();
		int end = Math.min(start + page.getPageSize(), deletionReasons.size());
		List<DeletionReason> content = deletionReasons.subList(start, end);

		return new PageImpl<>(content, page, deletionReasons.size())
				.map(d -> new UpdateDetails(d.getType(), d.getDetails(), d.getReason(), Date.from(d.getCreatedAt())));
	}

	@Override
	@Transactional
	public Page<City> findCitiesUsersByEntityTypeAndCityName(UUID id, String cityName, Pageable pageable) {
		return this.repository.findCitiesUsersByOrganizationIdAndCityName(id, cityName, pageable);
	}

	@Override
	@Transactional
	public Page<Region> findRegionsUsersByEntityTypeAndRegionName(UUID id, String regionName, Pageable pageable) {
		return this.repository.findRegionsUsersByOrganizationIdAndRegionName(id, regionName, pageable);
	}

	@Override
	@Transactional
	public Page<Region> findRegionsUsersBySubentityAndRegionName(UUID id, String regionName, Pageable pageable) {
		return this.repository.findRegionsUsersBySubentityIdAndRegionName(id, regionName, pageable);
	}

	@Override
	@Transactional
	public Page<User> filterUsersSubentity(UserSearchSubentityFilters filter, Pageable pageable) {
		return this.repository.findWithFiltersSubentity(filter, pageable);
	}

	@Override
	@Transactional
	public Page<City> findCitiesUsersBySubentityAndCityName(UUID id, String cityName, Pageable pageable) {
		return this.repository.findCitiesUsersBySubentityIdAndCityName(id, cityName, pageable);
	}

	@Override
	@Transactional
	public Page<User> getClientsData(Pageable page) {
		String roleName = "CLIENT";
		return this.repository.findByRoleName(roleName, page);
	}

	public ClientDto toClientDto(User user) {
		ClientDto client = this.modelMapper.map(user, ClientDto.class);

		client.setNumberOfCommands(user.getOrders().size());
		return client;
	}

	@Override
	@Transactional
	public Long countDeliveryUsersByOrganizationId(UUID id) {
		return this.repository.countDeliveryUsersByOrganizationId(id);
	}

	@Override
	@Transactional
	public Page<User> getOrganizationUsers(Pageable pageable, UUID id) {
		return this.repository.findByOrganizationEntity_Id(id, pageable);
	}

	@Override
	@Transactional
	public User createOrganizationEntityUser(UserRequest userRequest) {
		User user = this.create(userRequest);
//        String receiver = user.getEmail();
//        String subject = "Foodeals account validation";
//        String message = "You're account has been validated\n Your email : " + user.getEmail() + " \n" + " Your password : " + userRequest.password();
//        this.emailService.sendEmail(receiver, subject, message);
		return user;
	}

	@Override
	@Transactional
	public Integer countByRole(Role role) {
		return this.repository.countByRoleAndDeletedAtIsNull(role);
	}

	@Transactional
	public ClientPageResponseDto getClients(Pageable pageable) {
		List<User> allUsers = repository.findAll();
		List<User> clientsUsers = allUsers.stream()
				.filter(user -> user.getRole() != null && "CLIENT".equals(user.getRole().getName())).toList();

		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), clientsUsers.size());
		List<User> pagedClients = clientsUsers.subList(start, end);

		Page<User> clientPage = new PageImpl<>(pagedClients, pageable, clientsUsers.size());

		List<ClientResponseDto> clients = clientPage.stream().map(user -> {
			Order lastOrder = orderRepository.findFirstByClientIdOrderByCreatedAtDesc(user.getId()).orElse(null);

			String ref = lastOrder != null && lastOrder.getTransaction() != null
					? lastOrder.getTransaction().getReference()
					: null;

			Double reduction = lastOrder != null && lastOrder.getCoupon() != null
					? lastOrder.getCoupon().getDiscount().doubleValue()
					: null;

			Address address = user.getAddress();
			String country = (address != null && address.getRegion() != null && address.getRegion().getCity() != null
					&& address.getRegion().getCity().getState() != null
					&& address.getRegion().getCity().getState().getCountry() != null)
							? address.getRegion().getCity().getState().getCountry().getName()
							: null;

			List<Order> userOrders = orderRepository.findAllByClientId(user.getId());

			List<OrdersClientResponseDto> commandDtos = userOrders.stream().map(order -> {
				List<Product> products = new ArrayList<>();

				try {
					Deal deal = dealRepository.getDealByOfferId(order.getOffer().getId());
					if (deal != null && deal.getProduct() != null) {
						products.add(deal.getProduct());
					}
				} catch (Exception ignored) {
				}

				try {
					Box box = boxRepository.getBoxByOfferId(order.getOffer().getId());
					if (box != null && box.getBoxItems() != null) {
						for (BoxItem item : box.getBoxItems()) {
							if (item.getProduct() != null) {
								products.add(item.getProduct());
							}
						}
					}
				} catch (Exception ignored) {
				}

				String name = products.stream().map(Product::getName).filter(Objects::nonNull)
						.collect(Collectors.joining(" + "));

				Double totalAmount = 0.0;
				totalAmount += order.getOffer().getPrice().amount().doubleValue();
				return new OrdersClientResponseDto(order.getId(), order.getTransaction().getReference(), name,
						order.getPrice().amount().doubleValue(), order.getStatus().name(),
						LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.systemDefault()),
						order.getOffer().getSubEntity() != null
								? new StoreResponseDto(order.getOffer().getSubEntity().getId().toString(),
										order.getOffer().getSubEntity().getName(),
										order.getOffer().getSubEntity().getAvatarPath(),
										order.getOffer().getSubEntity().getAddress().getAddress())
								: null,
						order.getOffer().getDeliveryFee().doubleValue(), totalAmount,
						order.getOffer().getModalityPaiement() != null ? order.getOffer().getModalityPaiement().name()
								: null,
						"0",
						// Street (Adresse)
						order.getClient().getAddress() != null ? order.getClient().getAddress().getAddress() : null,

						// City (Ville)
						order.getClient().getAddress() != null && order.getClient().getAddress().getRegion() != null
								? order.getClient().getAddress().getRegion().getCity().getName()
								: null,

						// Postal Code (Code postal)
						null,

						// Country (Pays)
						order.getClient().getAddress() != null && order.getClient().getAddress().getRegion() != null
								&& order.getClient().getAddress().getRegion().getCity() != null
								&& order.getClient().getAddress().getRegion().getCity().getState() != null
								&& order.getClient().getAddress().getRegion().getCity().getState().getCountry() != null
										? order.getClient().getAddress().getRegion().getCity().getState().getCountry()
												.getName()
										: null

				);
			}).toList();

			List<OrderItemDto> itemDtos = new ArrayList<>();
			for (Order order : userOrders) {
				int i = 0;
				try {
					Deal deal = dealRepository.getDealByOfferId(order.getOffer().getId());
					if (deal != null && deal.getProduct() != null) {
						i++;
						itemDtos.add(new OrderItemDto("item-00" + i, deal.getProduct().getName(), 1,
								order.getOffer().getPrice().amount().doubleValue()));
					}
				} catch (Exception ignored) {
				}

				try {
					Box box = boxRepository.getBoxByOfferId(order.getOffer().getId());
					if (box != null && box.getBoxItems() != null) {
						for (BoxItem item : box.getBoxItems()) {
							itemDtos.add(new OrderItemDto("item-00" + i++, item.getProduct().getName(), 1,
									order.getOffer().getPrice().amount().doubleValue()));
						}
					}
				} catch (Exception ignored) {
				}

			}

			return new ClientResponseDto(user.getId(),
					lastOrder != null ? LocalDateTime.ofInstant(lastOrder.getCreatedAt(), ZoneId.systemDefault())
							: null,
					ref, user.getAvatarPath(), user.getName().firstName() + " " + user.getName().lastName(),
					LocalDate.ofInstant(user.getCreatedAt(), ZoneId.systemDefault()), reduction, user.getSource(),
					country,
					address != null && address.getRegion() != null ? address.getRegion().getCity().getName() : null,
					address != null ? address.getAddress() : null, user.getEmail(), user.getPhone(), commandDtos,
					itemDtos);
		}).toList();

		long totalUsers = clientsUsers.size();
		long internalUsers = repository.countBySource("INTERNE");
		long externalUsers = repository.countBySource("EXTERNE");

		ClientStatsDto stats = new ClientStatsDto(totalUsers, internalUsers, externalUsers);

		return new ClientPageResponseDto(stats, clients, pageable.getPageNumber(), pageable.getPageSize(), totalUsers,
				(int) Math.ceil((double) totalUsers / pageable.getPageSize()));
	}

	@Override
	public ClientResponseDto getClient(Integer id) {
		User user = repository.findById(id).get();
		Order lastOrder = orderRepository.findFirstByClientIdOrderByCreatedAtDesc(user.getId()).orElse(null);

		String ref = lastOrder != null && lastOrder.getTransaction() != null ? lastOrder.getTransaction().getReference()
				: null;

		Double reduction = lastOrder != null && lastOrder.getCoupon() != null
				? lastOrder.getCoupon().getDiscount().doubleValue()
				: null;

		Address address = user.getAddress();
		String country = address != null && address.getRegion() != null && address.getRegion().getCity() != null
				&& address.getRegion().getCity().getState() != null
				&& address.getRegion().getCity().getState().getCountry() != null
						? address.getRegion().getCity().getState().getCountry().getName()
						: null;

		return new ClientResponseDto(user.getId(),
				lastOrder != null ? LocalDateTime.ofInstant(lastOrder.getCreatedAt(), ZoneId.systemDefault()) : null,
				ref, user.getAvatarPath(), user.getName().firstName() + " " + user.getName().lastName(),
				LocalDate.ofInstant(user.getCreatedAt(), ZoneId.systemDefault()), reduction, user.getSource(), // todo
				country,
				address != null && address.getRegion() != null ? address.getRegion().getCity().getName() : null,
				address != null ? address.getAddress() : null, user.getEmail(), user.getPhone(), null, null);
	}

	@Override
	public void archiveClient(Integer id, String reason, String motif) {
		User user = repository.findById(id).orElse(null);
		if (user != null) {

			user.setDeletedAt(Instant.now());
			repository.save(user);
		}
	}

	@Override
	public Page<CollaboratorResponseDTO> getCollaborators(Pageable pageable) {
		return repository.findAllCollaborators(pageable).map(user -> CollaboratorResponseDTO.builder().id(user.getId())
				.ref("USER-" + user.getId()).hireDate(LocalDate.ofInstant(user.getCreatedAt(), ZoneId.systemDefault()))
				.fullName(user.getName().firstName() + " " + user.getName().lastName())
				.jobTitle(user.getRole() != null ? user.getRole().getName() : "").phone(user.getPhone())
				.email(user.getEmail())
				.country(user.getAddress() != null
						? "Maroc"
						: "")
				.city(user.getAddress() != null ? user.getAddress().getRegion().getCity().getName() : "")
				.grossSalary(user.getGrossDeclaration()).netSalary(user.getNetDeclaration())
				// à mettre à jour avec UserContract
				.totalAbsence(user.getAbsences() != null ? user.getAbsences().size() : 0).totalLeave(0) // future
																										// implémentation
				.build());
	}

	@Override
	public CollaboratorStatisticsDTO getCollaboratorStatistics() {
		return CollaboratorStatisticsDTO.builder().totalCollaborators(repository.countCollaborators())
				.totalRecrutement(repository.countRecrutement()).totalPrestataires(repository.countPrestataires())
				.build();
	}

	@Override
	public CollaboratorDetailsResponseDto getCollaboratorDetails(Integer id) {
		User user = repository.findById(id).orElseThrow(() -> new RuntimeException("Collaborator not found"));

		List<UserPersonalDocument> personalDocs = personalDocumentRepository.findByUserId(user.getId());
		List<UserInternalDocument> internalDocs = internalDocumentRepository.findByUserId(user.getId());

		CollaboratorDetailsResponseDto dto = toResponseDto(user);
		dto.setPersonalDocuments(personalDocs.stream().map(this::toDocumentPersonalDto).toList());
		dto.setInternalDocuments(internalDocs.stream().map(this::toDocumentInternalDto).toList());

		return dto;
	}

	public CollaboratorDetailsResponseDto toResponseDto(User user) {
		return CollaboratorDetailsResponseDto.builder().id(user.getId()).avatar(user.getAvatarPath())
				.civility(user.getGender() != null ? user.getGender().name() : null)
				.firstName(user.getName() != null ? user.getName().firstName() : null)
				.lastName(user.getName() != null ? user.getName().lastName() : null).nationality(user.getNationality())
				.cin(user.getNationalId()).phone(user.getPhone()).email(user.getEmail())
				.address(user.getAddress() != null ? user.getAddress().getAddress() : null)
				.city(user.getAddress() != null && user.getAddress().getRegion() != null
						? user.getAddress().getRegion().getCity().getName()
						: null)
				.region(user.getAddress() != null && user.getAddress().getRegion() != null
						? user.getAddress().getRegion().getName()
						: null)
				.country("Maroc")
				.state("Casablanca-settat")
				.role(user.getRole() != null ? user.getRole().getName() : null)
				.responsible(user.getResponsible() != null
						? new CollaboratorRefDto(user.getResponsible().getId(), user.getResponsible().getName())
						: null)
				.department(user.getRayon() != null ? user.getRayon().getName() : null)
				.netSalary(user.getNetDeclaration()).grossSalary(user.getGrossDeclaration())
				.contractType(user.getContractType())
				.workingHours(user.getWorkingHours() != null
						? user.getWorkingHours().stream().map(this::toWorkingHourDto).toList()
						: List.of())

				.internalDocuments(user.getInternalDocuments() != null
						? user.getInternalDocuments().stream().map(this::toDocumentInternalDto).toList()
						: List.of())
				.personalDocuments(user.getPersonalDocuments() != null
						? user.getPersonalDocuments().stream().map(this::toDocumentPersonalDto).toList()
						: List.of())
				.build();
	}

	public WorkingHoursDTO toWorkingHourDto(WorkingHours entity) {
		return WorkingHoursDTO.builder().dayOfWeek(entity.getDayOfWeek().name()).morningStart(entity.getMorningStart())
				.morningEnd(entity.getMorningEnd()).afternoonStart(entity.getAfternoonStart())
				.afternoonEnd(entity.getAfternoonEnd()).build();
	}

	public DocumentDto toDocumentInternalDto(UserInternalDocument doc) {
		return DocumentDto.builder().id(doc.getId()).name(doc.getName()).path(doc.getPath())
				.uploadedAt(doc.getCreatedAt()).build();
	}

	public DocumentDto toDocumentPersonalDto(UserPersonalDocument doc) {
		return DocumentDto.builder().id(doc.getId()).name(doc.getName()).path(doc.getPath())
				.uploadedAt(doc.getCreatedAt()).build();
	}

	@Override
	@Transactional
	public CollaboratorDetailsResponseDto createCollaborator(CollaboratorAddDto dto, List<String> internalDocuments,
			List<String> personalDocuments) {
		User user = toEntity(dto, internalDocuments, personalDocuments);
		user.setStatus(UserStatus.ACTIVE); // ACTIVE

		// Optionnel : s'assurer que le rôle est bien COLLABORATOR
		Role role = user.getRole();
		if (role == null || !"COLLABORATOR".equals(role.getName())) {
			role = roleRepository.findByName("COLLABORATOR")
					.orElseThrow(() -> new RuntimeException("Role COLLABORATOR not found"));
			user.setRole(role);
		}

		user = repository.save(user);
		return toResponseDto(user);
	}

	public User toEntity(CollaboratorAddDto dto, List<String> internalDocsPaths, List<String> personalDocsPaths) {
		User user = new User();

		user.setAvatarPath(dto.getAvatar());
		user.setGender(dto.getCivility());
		user.setName(new Name(dto.getFirstName(), dto.getLastName()));
		user.setNationality(dto.getNationality());
		user.setNationalId(dto.getCin());

		user.setPhone(dto.getPhone());
		user.setEmail(dto.getEmail());
		user.setNetDeclaration(dto.getNetSalary());
		user.setGrossDeclaration(dto.getGrossSalary());
		user.setContractType(dto.getContractType());

		// Departement

		Rayon rayon = rayonRepository.findByName(dto.getDepartment());
		if (rayon == null) {
			rayon = new Rayon();
			rayon.setName(dto.getDepartment());
			rayon = rayonRepository.save(rayon);
			user.setRayon(rayon);
		}
		// Adresse simple (ville et adresse)
		Address address = new Address();
		address.setAddress(dto.getAddress());

		Country country = countryRepository.findByName(dto.getCountry());

		State state = stateRepository.findByName(dto.getState());
		state.setCountry(country);
		state = stateRepository.save(state);

		City city = cityRepository.findByName(dto.getCity());
		city.setState(state);
		city = cityRepository.save(city);

		Region region = regionRepository.findByName(dto.getRegion());
		region.setCity(city);
		region = regionRepository.save(region);
		address.setRegion(region);
		address = addressRepository.save(address);
		user.setAddress(address);

		// Rôle (nom uniquement, assumé que l'objet complet est chargé en service)
		Role role = roleRepository.findByName("COLLABORATOR").get();
		user.setRole(role);

		// Responsable (si fourni)
		User responsible = repository.findById(dto.getResponsible()).orElse(null);
		user.setResponsible(responsible);

		// Working Hours
		if (dto.getWorkingHours() != null) {
			List<WorkingHours> whList = dto.getWorkingHours().stream().map(wd -> {
				WorkingHours wh = new WorkingHours();
				wh.setDayOfWeek(DayOfWeek.valueOf(wd.getDayOfWeek()));
				wh.setMorningStart(wd.getMorningStart());
				wh.setMorningEnd(wd.getMorningEnd());
				wh.setAfternoonStart(wd.getAfternoonStart());
				wh.setAfternoonEnd(wd.getAfternoonEnd());
				wh.setUser(user);
				return wh;
			}).collect(Collectors.toList());
			user.setWorkingHours(whList);
		}

		// Personal documents
		if (personalDocsPaths.size() > 0) {
			List<UserPersonalDocument> personalDocs = personalDocsPaths.stream().map(path -> {
				UserPersonalDocument d = new UserPersonalDocument();
				d.setName(path.substring(path.lastIndexOf("/") + 1));
				d.setPath(path);
				d.setUser(user);
				return d;
			}).collect(Collectors.toList());
			user.setPersonalDocuments(personalDocs);
		}

		// Internal documents
		if (internalDocsPaths.size() > 0) {
			List<UserInternalDocument> internalDocs = internalDocsPaths.stream().map(path -> {
				UserInternalDocument d = new UserInternalDocument();
				d.setName(path.substring(path.lastIndexOf("/") + 1));
				d.setPath(path);
				d.setUser(user);
				return d;
			}).collect(Collectors.toList());
			user.setInternalDocuments(internalDocs);
		}

		return user;
	}

	@Override
	@Transactional
	public CollaboratorDetailsResponseDto updateCollaborator(Integer id, CollaboratorAddDto dto,
			List<String> internalDocuments, List<String> personalDocuments) {
		User user = repository.findById(id).orElseThrow(() -> new RuntimeException("Collaborator not found"));

		final User updatedUser = updateEntityFromDto(user, dto);

		// Reset documents & working hours
		updatedUser.getPersonalDocuments().clear();
		updatedUser.getInternalDocuments().clear();
		updatedUser.getWorkingHours().clear();

		if (internalDocuments.size() > 0) {
			internalDocuments.forEach(path -> {
				UserInternalDocument d = new UserInternalDocument();
				d.setName(path.substring(path.lastIndexOf("/") + 1));
				d.setPath(path);
				d.setUser(updatedUser);
				updatedUser.getInternalDocuments().add(d);
			});
		}

		if (personalDocuments.size() > 0) {
			personalDocuments.forEach(path -> {
				UserPersonalDocument p = new UserPersonalDocument();
				p.setName(path.substring(path.lastIndexOf("/") + 1));
				p.setPath(path);
				p.setUser(updatedUser);
				updatedUser.getPersonalDocuments().add(p);
			});
		}

		if (dto.getWorkingHours() != null) {
			dto.getWorkingHours().forEach(w -> {
				WorkingHours wh = new WorkingHours();
				wh.setUser(updatedUser);
				wh.setDayOfWeek(DayOfWeek.valueOf(w.getDayOfWeek()));
				wh.setMorningStart(w.getMorningStart());
				wh.setMorningEnd(w.getMorningEnd());
				wh.setAfternoonStart(w.getAfternoonStart());
				wh.setAfternoonEnd(w.getAfternoonEnd());
				updatedUser.getWorkingHours().add(wh);
			});
		}

		User savedUser = repository.save(updatedUser);
		return toResponseDto(savedUser);
	}

	public User updateEntityFromDto(User user, CollaboratorAddDto dto) {
		user.setName(new Name(dto.getFirstName(), dto.getLastName()));
		user.setAvatarPath(dto.getAvatar());
		user.setGender(dto.getCivility());
		user.setNationality(dto.getNationality());
		user.setNationalId(dto.getCin());
		user.setPhone(dto.getPhone());
		user.setEmail(dto.getEmail());
		user.setGrossDeclaration(dto.getGrossSalary());
		user.setNetDeclaration(dto.getNetSalary());
		user.setContractType(dto.getContractType());

		// Address
		if (dto.getAddress() != null || dto.getCity() != null) {

			Address address = addressRepository.findByAddress(dto.getAddress()).orElse(null);
			if (address != null) {
				user.setAddress(address);
			} else {
				address = new Address();
				address.setAddress(dto.getAddress());
			}

			Country country = countryRepository.findByName(dto.getCountry());

			State state = stateRepository.findByName(dto.getState());
			state.setCountry(country);
			state = stateRepository.save(state);

			City city = cityRepository.findByName(dto.getCity());
			city.setState(state);
			city = cityRepository.save(city);

			Region region = regionRepository.findByName(dto.getRegion());
			region.setCity(city);
			region = regionRepository.save(region);
			address.setRegion(region);
			address = addressRepository.save(address);
		}

		// Role
		Role role = roleRepository.findByName(dto.getRole()).get();
		if (role != null) {
			user.setRole(role);
		}

		// Responsable
		if (dto.getResponsible() != null) {
			User responsible = repository.findById(dto.getResponsible()).get();
			user.setResponsible(responsible);
		}

		return user;
	}

	@Override
	public void archiveCollaborator(Integer id, String reason, String motif) {
		User user = repository.findById(id).orElse(null);
		if (user != null) {

			user.setDeletedAt(Instant.now());
			repository.save(user);
		}

	}

	@Override
	public AbsenceDto createAbsence(Integer userId, Integer validateById, AbsenceDto dto) {
		User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		Absence absence = new Absence();
		absence.setUser(user);
		absence.setStartDate(dto.startDate());
		absence.setEndDate(dto.endDate());
		absence.setReason(dto.reason());
		absence.setJustificationPath(dto.justificationPath());
		User validateBy = repository.findById(validateById).orElse(null);
		if (validateBy != null) {
			absence.setValidatedBy(validateBy);
		}
		absence = absenceRepository.save(absence);
		return toAbsenceDto(absence);
	}

	@Override
	public List<AbsenceDto> getAbsences(Integer userId) {
		List<Absence> absences = absenceRepository.findByUserId(userId);
		return absences.stream().map(this::toAbsenceDto).collect(Collectors.toList());
	}

	public AbsenceDto toAbsenceDto(Absence a) {
		return AbsenceDto.builder().id(a.getId()).startDate(a.getStartDate()).endDate(a.getEndDate())
				.reason(a.getReason()).justificationPath(a.getJustificationPath())
				.validatedBy(a.getValidatedBy() != null
						? a.getValidatedBy().getName().firstName() + " " + a.getValidatedBy().getName().lastName()
						: null)
				.validatedByAvatar(a.getValidatedBy() != null ? a.getValidatedBy().getAvatarPath() : null).build();
	}

	@Override
	public String saveFile(MultipartFile file) {

		Path path = Paths.get(uploadDir, file.getOriginalFilename());
		try {
			Files.createDirectories(path.getParent());
			Files.write(path, file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Problem while saving the file.", e);
		}

		return file.getOriginalFilename();
	}

	@Override
	public Page<CollaboratorResponseDTO> getArchivedCollaborators(Pageable pageable) {
		return repository.findAllArchivedCollaborators(pageable)
				.map(user -> CollaboratorResponseDTO.builder().id(user.getId()).ref("USER-" + user.getId())
						.hireDate(LocalDate.ofInstant(user.getCreatedAt(), ZoneId.systemDefault()))
						.fullName(user.getName().firstName() + " " + user.getName().lastName())
						.jobTitle(user.getRole() != null ? user.getRole().getName() : "").phone(user.getPhone())
						.email(user.getEmail())
						.country(user.getAddress() != null
								? user.getAddress().getRegion().getCity().getState().getCountry().getName()
								: "")
						.city(user.getAddress() != null ? user.getAddress().getRegion().getCity().getName() : "")
						.grossSalary(user.getGrossDeclaration()).netSalary(user.getNetDeclaration())
						// à mettre à jour avec UserContract
						.totalAbsence(user.getAbsences() != null ? user.getAbsences().size() : 0).totalLeave(0) // future
																												// implémentation
						.build());
	}

	@Override
	public User getConnectedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String email = userDetails.getUsername();
			return repository.findByEmail(email).get();
		}

		return null;

	}

	@Override
	public Page<User> findByRoleName(String roleName, Pageable page) {

		return repository.findByRoleName(roleName, page);
	}

	@Override
	public OrderTrackingResponseDto getOrderTrackingDetails(String clientId, String commandId) {

		Order order = orderRepository.findByIdAndClientId( UUID.fromString(commandId), 
				 Integer.valueOf(clientId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		OrderTrackingResponseDto dto = new OrderTrackingResponseDto();
		dto.setOrderId(order.getId().toString());
		dto.setClientId(order.getClient().getId().toString());
		dto.setStatus(order.getStatus().name());
	

		dto.setCurrentLocation(new LocationDto(order.getDelivery().getDeliveryBoy()
				.getCoordinates().latitude(), order.getDelivery().getDeliveryBoy().getCoordinates().longitude(),
				order.getDelivery().getDeliveryBoy().getAddress().getAddress()));

		dto.setDeliveryAddress(new DeliveryAddressDto(order.getClient().getCoordinates().latitude(),
				order.getClient().getCoordinates().longitude(),
				order.getClient().getAddress().getAddress()+" "+order.getClient().getAddress().getRegion().getCity().getName(),
				order.getClient().getName().firstName()+" "+order.getClient().getName().lastName(),
				order.getClient().getPhone()));

		dto.setDriver(new DriverDto(order.getDelivery().getDeliveryBoy().getId().toString(),
				order.getDelivery().getDeliveryBoy().getName().firstName()+" "+ 
						order.getDelivery().getDeliveryBoy().getName().lastName(),
						order.getDelivery().getDeliveryBoy().getPhone(),
						order.getDelivery().getDeliveryBoy().getVehicleType().name(), order.getDelivery().getDeliveryBoy().getRadius()));

		dto.setRestaurant(new RestaurantDto(order.getOffer().getSubEntity().getName(),
				order.getOffer().getSubEntity().getAddress().getAddress(),
				order.getOffer().getSubEntity().getPhone()));

		dto.setTimeline(order.getTrackingSteps().stream()
				.map(step -> new TimelineEventDto(step.getStatus(),
						DateTimeFormatter.ISO_INSTANT.format(step.getTimestamp()), step.getDescription()))
				.collect(Collectors.toList()));

		return dto;
	}

	@Override
	public DriverLocationResponseDto getDriverLocation(Integer clientId, UUID commandId) {
	    Order order = orderRepository.findByIdAndClientId(commandId, clientId)
	        .orElseThrow(() -> new RuntimeException("Order not found"));

	    User driver = Optional.ofNullable(order.getDelivery())
	        .map(Delivery::getDeliveryBoy)
	        .orElseThrow(() -> new RuntimeException("No driver assigned"));

	    Coordinates driverCoords = driver.getCoordinates();
	    Coordinates clientCoords = order.getClient().getCoordinates();

	    if (driverCoords == null || clientCoords == null) {
	        throw new RuntimeException("Missing coordinates");
	    }

	    double distanceKm = calculateDistanceKm(driverCoords, clientCoords);

	    int speedKmh=0;
	    if(driver.getVehicleType().equals(VehicleType.MOTO)) {
	    	speedKmh=35;
	    }
	    
	    if(driver.getVehicleType().equals(VehicleType.BICYCLE)) {
	    	speedKmh=25;
	    }
	    
	  

	    double timeHours = distanceKm / speedKmh;
	    long estimatedMinutes = (long) (timeHours * 60);

	    Instant estimatedArrival = Instant.now().plus(Duration.ofMinutes(estimatedMinutes));

	    return new DriverLocationResponseDto(
	        driver.getId().toString(),
	        new LocationDriverDto(driverCoords.latitude(), driverCoords.longitude(), 45, 25, 10),
	        Instant.now().toString(),
	        estimatedArrival.toString()
	    );
	}
	
	public double calculateDistanceKm(Coordinates c1, Coordinates c2) {
	    final int R = 6371; // Rayon Terre en km

	    double latDistance = Math.toRadians(c2.latitude() - c1.latitude());
	    double lonDistance = Math.toRadians(c2.longitude() - c1.longitude());

	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	        + Math.cos(Math.toRadians(c1.latitude())) * Math.cos(Math.toRadians(c2.latitude()))
	        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    return R * c;
	}

	



}