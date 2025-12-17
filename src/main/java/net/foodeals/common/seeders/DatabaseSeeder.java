package net.foodeals.common.seeders;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.location.domain.entities.*;
import net.foodeals.location.domain.repositories.*;
import net.foodeals.notification.domain.entity.AdminNotification;
import net.foodeals.notification.domain.entity.AdminNotificationPreferences;
import net.foodeals.notification.domain.entity.AdminNotificationState;
import net.foodeals.notification.domain.enums.AdminNotificationPriority;
import net.foodeals.notification.domain.enums.AdminNotificationSourceApp;
import net.foodeals.notification.domain.enums.AdminNotificationType;
import net.foodeals.notification.domain.repositories.AdminNotificationPreferencesRepository;
import net.foodeals.notification.domain.repositories.AdminNotificationRepository;
import net.foodeals.notification.domain.repositories.AdminNotificationStateRepository;
import net.foodeals.organizationEntity.domain.entities.Contact;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.organizationEntity.domain.repositories.OrganizationEntityRepository;
import net.foodeals.organizationEntity.domain.repositories.SubEntityRepository;
import net.foodeals.payment.domain.entities.Payment;
import net.foodeals.payment.domain.repository.PaymentRepository;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.entities.ProductSubCategory;
import net.foodeals.product.domain.entities.Rayon;
import net.foodeals.product.domain.enums.CreatedBy;
import net.foodeals.product.domain.repositories.ProductCategoryRepository;
import net.foodeals.product.domain.repositories.ProductRepository;
import net.foodeals.product.domain.repositories.ProductSubCategoryRepository;
import net.foodeals.product.domain.repositories.RayonRepository;
import net.foodeals.support.domain.entities.SupportAttachment;
import net.foodeals.support.domain.entities.SupportResponse;
import net.foodeals.support.domain.entities.SupportTicket;
import net.foodeals.support.domain.entities.enums.ResponseType;
import net.foodeals.support.domain.entities.enums.SupportPriority;
import net.foodeals.support.domain.entities.enums.SupportStatus;
import net.foodeals.support.domain.repositories.SupportAttachmentRepository;
import net.foodeals.support.domain.repositories.SupportResponseRepository;
import net.foodeals.support.domain.repositories.SupportTicketRepository;
import net.foodeals.offer.domain.entities.*;
import net.foodeals.offer.domain.enums.*;
import net.foodeals.offer.domain.repositories.*;
import net.foodeals.order.domain.entities.Coupon;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.TrackingStep;
import net.foodeals.order.domain.entities.Transaction;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.VehicleType;
import net.foodeals.order.domain.repositories.*;
import net.foodeals.user.domain.entities.Absence;
import net.foodeals.user.domain.entities.Role;
import net.foodeals.user.domain.entities.User;
import net.foodeals.user.domain.entities.UserInternalDocument;
import net.foodeals.user.domain.entities.UserPermission;
import net.foodeals.user.domain.entities.UserPersonalDocument;
import net.foodeals.user.domain.entities.UserStatus;
import net.foodeals.user.domain.entities.WorkingHours;
import net.foodeals.user.domain.entities.enums.Gender;
import net.foodeals.user.domain.repositories.RoleRepository;
import net.foodeals.user.domain.repositories.UserPermissionRepository;
import net.foodeals.user.domain.repositories.UserRepository;
import net.foodeals.user.domain.valueObjects.Name;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.contract.domain.entities.Commission;
import net.foodeals.contract.domain.entities.Contract;
import net.foodeals.contract.domain.entities.SolutionContract;
import net.foodeals.contract.domain.entities.Subscription;
import net.foodeals.contract.domain.entities.enums.ContractStatus;
import net.foodeals.contract.domain.repositories.CommissionRepository;
import net.foodeals.contract.domain.repositories.ContractRepository;
import net.foodeals.contract.domain.repositories.SolutionContractRepository;
import net.foodeals.contract.domain.repositories.SubscriptionRepository;
import net.foodeals.crm.domain.entities.CrmDemande;
import net.foodeals.crm.domain.entities.CrmDemandeDocument;
import net.foodeals.crm.domain.entities.CrmDemandeHistory;
import net.foodeals.crm.domain.entities.enums.DemandeStatus;
import net.foodeals.crm.domain.entities.enums.DemandeType;
import net.foodeals.crm.domain.repositories.CrmDemandeDocumentRepository;
import net.foodeals.crm.domain.repositories.CrmDemandeHistoryRepository;
import net.foodeals.crm.domain.repositories.CrmDemandeRepository;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.delivery.domain.enums.DeliveryStatus;
import net.foodeals.delivery.domain.repositories.DeliveryRepository;
import net.foodeals.home.domain.entities.HomePageFeaturedDeal;
import net.foodeals.home.domain.entities.HomeSorting;
import net.foodeals.home.domain.entities.HomepageAnnouncement;
import net.foodeals.home.domain.entities.HomepageCategory;
import net.foodeals.home.domain.entities.HomepageHero;
import net.foodeals.home.domain.entities.HomepageTestimonial;
import net.foodeals.home.domain.entities.PersonalizedBestSeller;
import net.foodeals.home.domain.repositories.HomePageFeaturedDealRepository;
import net.foodeals.home.domain.repositories.HomeSortingRepository;
import net.foodeals.home.domain.repositories.HomepageAnnouncementRepository;
import net.foodeals.home.domain.repositories.HomepageCategoryRepository;
import net.foodeals.home.domain.repositories.HomepageHeroRepository;
import net.foodeals.home.domain.repositories.HomepageTestimonialRepository;
import net.foodeals.home.domain.repositories.PersonalizedBestSellerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

	private final OrganizationEntityRepository organizationRepo;
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final ProductRepository productRepo;
	private final DealRepository dealRepo;
	private final CouponRepository couponRepo;
	private final BoxRepository boxRepo;
	private final BoxItemRepository boxItemRepo;
	private final OfferRepository offerRepo;
	private final TransactionRepository transactionRepo;
	private final PaymentRepository paymentRepo;
	private final CommissionRepository commissionRepo;
	private final SubscriptionRepository subscriptionRepo;
	private final SolutionContractRepository solutionContractRepo;
	private final ContractRepository contractRepo;
	private final OrderRepository orderRepo;
	private final CountryRepository countryRepo;
	private final StateRepository stateRepo;
	private final CityRepository cityRepo;
	private final RegionRepository regionRepo;
	private final AddressRepository addressRepo;
	private final DeliveryRepository deliveryRepo;
	private final SubEntityRepository subEntityRepo;
	private final TrackingStepRepository trackingStepRepo;
	private final UserPermissionRepository userPermissionRepo;
	private final HomepageHeroRepository heroRepository;
	private final HomePageFeaturedDealRepository featuredDealRepository;
	private final HomepageCategoryRepository categoryRepository;
	private final HomepageTestimonialRepository testimonialRepository;
	private final HomepageAnnouncementRepository announcementRepository;
	private final HomeSortingRepository homeSortingRepo;
	private final PersonalizedBestSellerRepository personalizedBestSellerRepo;
	private final SupportTicketRepository supportTicketRepo;
	private final SupportAttachmentRepository supportAttachmentRepo;
	private final SupportResponseRepository supportResponseRepo;
	private final CrmDemandeRepository crmDemandeRepo;
	private final CrmDemandeDocumentRepository crmDemandeDocumentRepo;
	private final CrmDemandeHistoryRepository crmDemandeHistoryRepo;
	private final ProductCategoryRepository productCategoryRepo;
	private final ProductSubCategoryRepository productSubCategoryRepo;
	private final RayonRepository rayonRepo;
    private final AdminNotificationRepository adminNotificationRepo;
    private final AdminNotificationStateRepository adminNotificationStateRepo;
    private final AdminNotificationPreferencesRepository adminNotificationPreferencesRepo;
    private final PasswordEncoder passwordEncoder;

	@PostConstruct
	@Transactional
	public void seed() {
		// === Create Organizations ===
		List<OrganizationEntity> organizations = new ArrayList<>();

		for (int i = 1; i <= 2; i++) {
			String orgName = "Partner " + i;
			OrganizationEntity org = organizationRepo.findByName(orgName);

			if (org == null) {
				org = new OrganizationEntity();
				org.setName(orgName);

				// Adresse fictive au Maroc
				Address address = new Address();
				address.setAddress("Rue Exemple " + i + ", Casablanca");
				address.setIframe(null);
				address.setCoordinates(new Coordinates(33.5731f, -7.5898f)); // Casablanca

				// Créer ou récupérer la région "Grand Casablanca"
				Region region = regionRepo.findByName("Grand Casablanca");
				if (region == null) {
					region = new Region();
					region.setName("Grand Casablanca");

					// Lier à une ville (optionnel)
					City city = cityRepo.findByName("Casablanca");
					if (city == null) {
						city = new City();
						city.setName("Casablanca");
						city = cityRepo.save(city);
					}
					region.setCity(city);
					region = regionRepo.save(region);
				}

				address.setRegion(region);
				address = addressRepo.save(address);
				org = organizationRepo.save(org);
				org.setAddress(address);

				// Contact
				Contact contact = new Contact();
				contact.setEmail("contact" + i + "@partner.com");
				contact.setPhone("060000000" + i);
				contact.setOrganizationEntity(org);
				org.setContacts(Collections.singletonList(contact));

				// ==== Subscription ====
				Subscription sub = new Subscription();
				// sub.setOrganizationEntity(org);
				sub.setStartDate(LocalDate.now().minusMonths(2));
				sub.setEndDate(LocalDate.now().plusMonths(10));
				Price subPrice = new Price(new BigDecimal("199.0"), Currency.getInstance("MAD")); // montant mensuel
				sub.setAmount(subPrice);
				sub = subscriptionRepo.save(sub);
				org.getSubscriptions().add(sub);

				// ==== Commission ====
				Commission commission = new Commission();
				commission.setCash(50f);
				commission.setCard(70f);
				commission.setDeliveryAmount(30f);
				commission.setDeliveryCommission(10f);
				commission = commissionRepo.save(commission);

				// ==== Contract & SolutionContract ====
				Contract contract = new Contract();
				contract.setName("Contrat " + org.getName());
				contract.setContractStatus(ContractStatus.VALIDATED);

				contract = contractRepo.save(contract);

				org.setContract(contract);

				SolutionContract solutionContract = new SolutionContract();

				solutionContract = solutionContractRepo.save(solutionContract);
				solutionContract.setContract(contract);
				solutionContract.setCommission(commission);
				solutionContract = solutionContractRepo.save(solutionContract);
				contract.getSolutionContracts().add(solutionContract);

				Contract contract2 = contractRepo.save(contract);

				org.setContract(contract2);
				org = organizationRepo.save(org);

				// ==== Payment ====
				Payment payment = new Payment();
				payment.setPaymentsWithCard(1500.0);
				payment.setPaymentsWithCash(1200.0);
				payment = paymentRepo.save(payment);

				// ==== Transaction ====
				Transaction tx = new Transaction();
				Price txPrice = new Price(new BigDecimal("800.0"), Currency.getInstance("MAD"));
				tx.setPrice(txPrice);
				tx = transactionRepo.save(tx);

				org = organizationRepo.save(org);
			}

			organizations.add(org);
		}

		// === Données financières réalistes ===
		for (OrganizationEntity org : organizations) {

		}

		// === Address hierarchy ===
		Country country = countryRepo.findByName("Maroc");
		if (country == null) {
			country = countryRepo.save(new Country(null, "Maroc", new ArrayList<>()));
		}

		State state = stateRepo.findByName("Casablanca-Settat");
		if (state == null) {
			state = new State(null, "Casablanca-Settat", new ArrayList<>(), country);
			state = stateRepo.save(state);
			state.setCountry(country);
			state = stateRepo.save(state);
		}

		City city = cityRepo.findByName("Casablanca");
		if (city == null) {
			city = new City(null, "Casablanca", state, new ArrayList<>());
			city = cityRepo.save(city);
			city.setState(state);
			city = cityRepo.save(city);
		}

		Region region = regionRepo.findByName("Maarif");
		if (region == null) {
			region = new Region();
			region.setName("Maarif");
			region.setCity(city);
			region = regionRepo.save(region);
		}

		// === Users with addresses ===
		List<User> clients = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			String email = "client-foodeals" + i + "@mail.com";
			User user = userRepo.findByEmail(email).orElse(null);
			if (user == null) {
				user = new User();
				user.setName(new Name("Client" + i, ""));
				user.setEmail(email);
				user.setPhone("070000000" + i);
				user.setSource(i % 2 == 0 ? "EXTERNE" : "INTERNE");
				Address address = new Address();
				address.setAddress("12 rue Liberté, App " + i);
				address.setRegion(region);
				address = addressRepo.save(address);
				user = userRepo.save(user);
				user.setAddress(address);
				user.setEmailVerified(true);
				user.setStatus(UserStatus.ACTIVE);
				user.setCoordinates(new Coordinates(33.5735f, -7.5899f)); // Position fictive
				user.setRadius(5);
				user = userRepo.save(user);
				user.setRole(roleRepo.findByName("CLIENT").get());
				user = userRepo.save(user);

			}
			clients.add(user);
		}

		List<SubEntity> subEntities = new ArrayList<>();

		for (int i = 1; i <= 2; i++) {
			SubEntity subEntity = new SubEntity();
			subEntity.setName("SubEntity " + i);
			subEntity.setEmail("sub" + i + "@foodeals.com");
			subEntity.setPhone("060100200" + i);
			subEntity.setType(SubEntityType.PARTNER_SB);
			subEntity.setSubEntityStatus(SubEntityStatus.ACTIVE);

			Address subAddress = new Address();
			subAddress.setAddress("Rue Sub " + i);
			subAddress.setRegion(region); // réutilise celle de "Maarif"
			subAddress.setCoordinates(new Coordinates(33.57f, -7.62f));
			subAddress = addressRepo.save(subAddress);

			subEntity.setAddress(subAddress);
			subEntity.setOrganizationEntity(organizations.get(i % organizations.size()));
			subEntities.add(subEntity);
		}
		subEntities = subEntityRepo.saveAll(subEntities);

		// === Ensure Admin user exists (used as product creator) ===
		User adminUser = userRepo.findByEmail("admin@foodeals.com").orElse(null);
		if (adminUser == null) {
			adminUser = new User();
			adminUser.setName(new Name("Admin", "Foodeals"));
			adminUser.setEmail("admin@foodeals.com");
			adminUser.setPhone("0600000000");
			adminUser.setEmailVerified(true);
            adminUser.setPassword(passwordEncoder.encode("foodeals123"));
            adminUser.setStatus(UserStatus.ACTIVE);
			// adresse légère
			Address adminAddr = new Address();
			adminAddr.setAddress("HQ Foodeals");
			adminAddr.setRegion(region); // réutilise la region créée plus haut
			adminAddr = addressRepo.save(adminAddr);
			adminUser.setAddress(adminAddr);

			// rôle ADMIN (création si absent)
			Role adminRole = roleRepo.findByName("ADMIN").orElse(null);
			if (adminRole == null) {
				adminRole = new Role();
				adminRole.setName("ADMIN");
				adminRole = roleRepo.save(adminRole);
			}
			adminUser = userRepo.save(adminUser);
			adminUser.setRole(adminRole);
			adminUser = userRepo.save(adminUser);
		}

		// 1) Préparer des catégories/sous-catégories managées
		ProductCategory catBakery = productCategoryRepo.findByName("Boulangerie")
				.orElseGet(() -> productCategoryRepo.save(new ProductCategory("Boulangerie", "boulangerie")));

		ProductCategory catEpicerie = productCategoryRepo.findByName("Épicerie")
				.orElseGet(() -> productCategoryRepo.save(new ProductCategory("Épicerie", "epicerie")));

		ProductSubCategory scViennoiseries = productSubCategoryRepo.findByNameAndCategory("Viennoiseries", catBakery)
				.orElseGet(() -> productSubCategoryRepo
						.save(ProductSubCategory.create("Viennoiseries", "viennoiseries", catBakery)));

		ProductSubCategory scPains = productSubCategoryRepo.findByNameAndCategory("Pains", catBakery)
				.orElseGet(() -> productSubCategoryRepo.save(ProductSubCategory.create("Pains", "pains", catBakery)));

		ProductSubCategory scConserves = productSubCategoryRepo.findByNameAndCategory("Conserves", catEpicerie)
				.orElseGet(() -> productSubCategoryRepo
						.save(ProductSubCategory.create("Conserves", "conserves", catEpicerie)));

		ProductSubCategory scBoissons = productSubCategoryRepo.findByNameAndCategory("Boissons", catEpicerie).orElseGet(
				() -> productSubCategoryRepo.save(ProductSubCategory.create("Boissons", "boissons", catEpicerie)));

		// === Rayons ===
		List<Rayon> rayons = new ArrayList<>();
		if (rayonRepo.count() == 0) {
			Rayon epicerie = new Rayon();
			epicerie.setName("Épicerie");

			Rayon boissons = new Rayon();
			boissons.setName("Boissons");

			Rayon boulangerie = new Rayon();
			boulangerie.setName("Boulangerie");

			Rayon hygiene = new Rayon();
			hygiene.setName("Hygiène");

			rayons = rayonRepo.saveAll(Arrays.asList(epicerie, boissons, boulangerie, hygiene));
		} else {
			rayons = rayonRepo.findAll();
		}

		// 2) Créer les produits en référencant des entités MANAGÉES (pas de proxies)
		List<Product> products = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			String name = "Product " + i;
			Product product = productRepo.findByName(name).orElse(null);
			if (product == null) {
				product = new Product();
				product.setName(name);
				product.setTitle("Titre " + i);
				product.setDescription("Description du produit " + i);
				product.setBarcode("BC-" + (1000 + i));
				product.setBrand(i % 2 == 0 ? "BrandX" : "BrandY");
				product.setProductImagePath("/images/p" + i + ".jpg");

				// Créateur
				if (i % 2 == 0) {
					product.setCreatedBy(CreatedBy.ADMIN);
					product.setCreatedByUser(adminUser); // adminUser doit être MANAGÉ (trouvé/sauvé dans la même
															// transaction)
					product.setCreatedBySubEntity(null);
				} else {
					SubEntity owner = subEntities.get((i - 1) % subEntities.size()); // doit être MANAGÉ
					product.setCreatedBy(CreatedBy.PARTNER);
					product.setCreatedByUser(null);
					product.setCreatedBySubEntity(owner);
				}

				// Choisir une sous-catégorie et sa catégorie (toutes MANAGÉES car issues de
				// save/find)
				ProductSubCategory sub = switch ((i - 1) % 4) {
				case 0 -> scViennoiseries;
				case 1 -> scPains;
				case 2 -> scConserves;
				default -> scBoissons;
				};
				product.setSubcategory(sub);
				product.setCategory(sub.getCategory());
				Rayon rayon = rayons.get((i - 1) % rayons.size());
				product.setRayon(rayon);
				product = productRepo.save(product);
			}
			products.add(product);
		}

		// === Deals ===
		List<Deal> deals = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Deal deal = new Deal();
			deal.setProduct(products.get(i));
			deal.setCategory(Category.BREAD_AND_PASTRIES);
			deal.setDealStatus(i % 2 == 0 ? DealStatus.AVAILABLE : DealStatus.UNVAVAILABLE);
			Random random = new Random();

			// Puis dans la boucle :
			int randomQuantity = random.nextInt(100) + 1; // entre 1 et 100 inclus
			deal.setQuantity(randomQuantity);
			Offer offer = new Offer();
			offer.setModalityPaiement(i % 2 == 0 ? ModalityPaiement.CARD : ModalityPaiement.CASH);
			offer.setDeliveryFee(200L);
			offer.setOrganizationEntity(organizations.get(i % organizations.size()));
			offer.setSubEntity(subEntities.get(i % subEntities.size()));
			Random rand = new Random();
			double randomPriceValue = 50 + (150 - 50) * rand.nextDouble(); // entre 50 et 150 MAD
			Price price = new Price(BigDecimal.valueOf(randomPriceValue), Currency.getInstance("MAD"));
			offer.setPrice(price);
			offer = offerRepo.save(offer);
			deal.setOffer(offer);
			deal.setPublishAs(i % 2 == 0 ? PublishAs.BAKERIES_PASTRIES : PublishAs.SUPERMARKETS_HYPERMARKETS);
			deal = dealRepo.save(deal);
			offer.setDeal(deal);
			offerRepo.save(offer);
			deals.add(deal);
		}

		// === Boxes ===
		List<Box> boxes = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Box box = new Box();
			Offer offer = new Offer();
			offer.setModalityPaiement(i % 2 == 0 ? ModalityPaiement.CARD : ModalityPaiement.CASH);
			offer.setDeliveryFee(200L);
			offer.setOrganizationEntity(organizations.get(i % organizations.size()));
			offer.setSubEntity(subEntities.get(i % subEntities.size()));
			Random rand = new Random();
			double randomPriceValue = 50 + (150 - 50) * rand.nextDouble(); // entre 50 et 150 MAD
			Price price = new Price(BigDecimal.valueOf(randomPriceValue), Currency.getInstance("MAD"));
			offer.setPrice(price);
			offer = offerRepo.save(offer);
			box.setOffer(offer);
			box.setPublishAs(i % 2 == 0 ? PublishAs.BAKERIES_PASTRIES : PublishAs.SUPERMARKETS_HYPERMARKETS);
			box.setCategory(Category.BREAD_AND_PASTRIES);
			box.setBoxStatus(i % 2 == 0 ? BoxStatus.AVAILABLE : BoxStatus.UNVAVAILABLE);
			box = boxRepo.save(box);
			offer.setBox(box);
			offerRepo.save(offer);
			boxes.add(box);
			for (int j = 0; j < 2; j++) {
				BoxItem item = new BoxItem();
				item.setProduct(products.get((i + j) % products.size()));
				item.setBox(box);
				boxItemRepo.save(item);
			}
		}

		// === DeliveryBoy ===

		User deliveryBoy = userRepo.findByEmail("deliveryboy@mail.com").orElse(null);
		if (deliveryBoy == null) {
			deliveryBoy = new User();
			deliveryBoy.setCoordinates(new Coordinates(33.5800f, -7.6100f)); // Position fictive
			deliveryBoy.setRadius(15);
			deliveryBoy.setVehicleType(VehicleType.MOTO);
			deliveryBoy.setName(new Name("Livreur", "1"));
			deliveryBoy.setEmail("deliveryboy@mail.com");
			deliveryBoy.setPhone("0600112233");
			deliveryBoy.setAddress(clients.get(0).getAddress());
			deliveryBoy = userRepo.save(deliveryBoy);
		}
		// === Delivery ===
		Delivery delivery = new Delivery();
		delivery.setDeliveryBoy(deliveryBoy);
		delivery.setStatus(DeliveryStatus.PENDING);
		delivery = deliveryRepo.save(delivery);

		// === Orders ===
		for (int i = 0; i < 3; i++) {
			String trxRef = "TRX-000" + i;
			if (!transactionRepo.findByReference(trxRef).isPresent()) {
				Order order = new Order();
				order.setClient(clients.get(i));
				Random rand = new Random();
				double randomPriceValue = 50 + (150 - 50) * rand.nextDouble(); // entre 50 et 150 MAD
				Price price = new Price(BigDecimal.valueOf(randomPriceValue), Currency.getInstance("MAD"));
				order.setPrice(price);
				order.setOrderSource(i % 2 == 0 ? OrderSource.DEAL_PRO : OrderSource.PRO_MARKET);
				order.setOffer(i % 2 == 0 ? deals.get(0).getOffer() : boxes.get(0).getOffer());
				order.setQuantity(2);
				order.setSeen(i % 2 == 0);
				order.setStatus(i % 2 == 0 ? OrderStatus.COMPLETED : OrderStatus.IN_PROGRESS);
				order.setDelivery(delivery);
				Transaction transaction = new Transaction();
				transaction.setReference(trxRef);
				transaction.setOrder(order);
				transactionRepo.save(transaction);
				order.setTransaction(transaction);
				orderRepo.save(order);
			}
		}
		// === Orders avec clientPro ===
		for (int i = 0; i < 2; i++) {
			String trxRefPro = "TRX-PRO-000" + i;
			if (!transactionRepo.findByReference(trxRefPro).isPresent()) {
				Order orderPro = new Order();
				orderPro.setClientPro(subEntities.get(i % subEntities.size())); // Associe une SubEntity
				Random rand = new Random();
				double randomPriceValue = 100 + (300 - 100) * rand.nextDouble(); // entre 100 et 300 MAD
				Price pricePro = new Price(BigDecimal.valueOf(randomPriceValue), Currency.getInstance("MAD"));
				orderPro.setPrice(pricePro);
				orderPro.setOrderSource(OrderSource.DEAL_PRO);
				orderPro.setOffer(deals.get(0).getOffer());
				orderPro.setQuantity(5);
				orderPro.setSeen(false);
				orderPro.setStatus(i % 2 == 0 ? OrderStatus.COMPLETED : OrderStatus.IN_PROGRESS);
				orderPro.setDelivery(delivery);

				Transaction transactionPro = new Transaction();
				transactionPro.setReference(trxRefPro);
				transactionPro.setOrder(orderPro);
				transactionRepo.save(transactionPro);

				orderPro.setTransaction(transactionPro);
				orderRepo.save(orderPro);
			}
		}

		// === Coupons ===
		for (int i = 1; i <= 3; i++) {
			String code = "FOOD" + i + "DEAL";
			Coupon coupon = couponRepo.findByCode(code).orElse(null);
			if (coupon == null) {
				coupon = new Coupon();
				coupon.setCode(code);
				coupon.setName("Coupon spécial " + i);
				coupon.setDiscount(10f * i); // Exemple : 10%, 20%, 30%
				coupon.setEndsAt(null); // ou une date d’expiration si tu préfères
				coupon.setIsEnabled(true); // Active par défaut
				couponRepo.save(coupon);
			}
		}
		// == timelines==
		List<Order> allOrders = orderRepo.findAll();
		for (Order order : allOrders) {
			List<TrackingStep> steps = new ArrayList<>();

			steps.add(new TrackingStep(order, "order_placed", "Commande passée avec succès",
					Instant.now().minusSeconds(3600)));
			steps.add(new TrackingStep(order, "preparing", "Préparation en cours", Instant.now().minusSeconds(2700)));
			steps.add(new TrackingStep(order, "ready_for_pickup", "Commande prête à être récupérée",
					Instant.now().minusSeconds(1800)));
			steps.add(new TrackingStep(order, "picked_up", "Commande récupérée par le livreur",
					Instant.now().minusSeconds(900)));
			steps.add(new TrackingStep(order, "in_transit", "Commande en route", Instant.now().minusSeconds(300)));

			trackingStepRepo.saveAll(steps);
		}
		List<User> collaborators = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			String email = "collaborator" + i + "@foodeals.com";
			User user = userRepo.findByEmail(email).orElse(null);
			if (user == null) {
				user = new User();
				user.setName(new Name("Collab", "Num" + i));
				user.setEmail(email);
				user.setPhone("06111222" + i);
				user.setEmailVerified(true);
				user.setStatus(UserStatus.ACTIVE);
				user.setGender(Gender.MALE);
				user.setDateOfBirth(LocalDate.of(1990 + i, 5, i));
				user.setNationality("Marocaine");
				user.setNationalId("CIN1234" + i);

				country = countryRepo.findByName("Maroc");
				if (country == null) {
					country = countryRepo.save(new Country(null, "Maroc", new ArrayList<>()));
				}

				state = stateRepo.findByName("Casablanca-Settat");
				if (state == null) {
					state = new State(null, "Casablanca-Settat", new ArrayList<>(), country);
					state = stateRepo.save(state);
					state.setCountry(country);
					state = stateRepo.save(state);
				}

				city = cityRepo.findByName("Casablanca");
				if (city == null) {
					city = new City(null, "Casablanca", state, new ArrayList<>());
					city = cityRepo.save(city);
					city.setState(state);
					city = cityRepo.save(city);
				}

				region = regionRepo.findByName("Maarif");
				if (region == null) {
					region = new Region();
					region.setName("Maarif");
					region.setCity(city);
					region = regionRepo.save(region);
				}
				Address address = new Address();
				address.setAddress("Résidence Bureau Collab " + i);
				address.setRegion(region);
				address = addressRepo.save(address);
				user.setAddress(address);

				User responislbe = userRepo.findByEmail("mohammed.el moussaoui@example.com").get();
				user.setResponsible(responislbe);

				// Absence
				Absence absence = new Absence();
				absence.setUser(user);
				absence.setStartDate(LocalDate.now().minusDays(3));
				absence.setEndDate(LocalDate.now());
				absence.setReason("Congé maladie");
				absence.setJustificationPath("/documents/justificatif.pdf");
				absence.setValidatedBy(user.getResponsible());
				user.getAbsences().add(absence);

				// Documents personnels
				UserPersonalDocument pd = new UserPersonalDocument();
				pd.setUser(user);
				pd.setName("CIN Recto");
				pd.setPath("/documents/personal/cin-recto.pdf");
				user.getPersonalDocuments().add(pd);

				// Documents internes
				UserInternalDocument doc = new UserInternalDocument();
				doc.setUser(user);
				doc.setName("Contrat Travail");
				doc.setPath("/documents/internal/contrat-travail.pdf");
				user.getInternalDocuments().add(doc);

				// Contrat
				user.setGrossDeclaration(Double.parseDouble("8000.0"));
				user.setNetDeclaration(Double.parseDouble("6000.0"));
				user.setContractType("CDI");

				// Role
				Role role = roleRepo.findByName("COLLABORATOR").orElse(null);
				if (role == null) {
					role = new Role();
					role.setName("COLLABORATOR");
					role = roleRepo.save(role);
				}

				// Horaire travail
				List<WorkingHours> hours = new ArrayList<>();
				for (DayOfWeek day : DayOfWeek.values()) {
					WorkingHours wh = new WorkingHours();
					wh.setUser(user);
					wh.setDayOfWeek(day);
					wh.setMorningStart("08:00");
					wh.setMorningEnd("12:00");
					wh.setAfternoonStart("13:00");
					wh.setAfternoonEnd("17:00");
					hours.add(wh);
				}
				user.setWorkingHours(hours);

				user = userRepo.save(user);
				user.setRole(role);
				user = userRepo.save(user);
				collaborators.add(user);
			}
		}

		// === Support Tickets ===
		List<SupportTicket> tickets = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			SupportTicket t = new SupportTicket();
			t.setResponseCount(3);
			t.setSubject(i % 2 == 0 ? "Payment Issue" : "Account Problem");
			t.setMessage("Message de test pour le ticket " + i);
			t.setStatus(i % 3 == 0 ? SupportStatus.IN_PROGRESS.name() : SupportStatus.PENDING.name());
			t.setPriority(i % 2 == 0 ? SupportPriority.HIGH.getValue() : SupportPriority.MEDIUM.getValue());
			t.setCategory(i % 2 == 0 ? "payment" : "account");
			t.setUserId(clients.get(0).getId());
			t.setUserName(clients.get(0).getName().lastName() + "" + clients.get(0).getName().lastName());
			t.setUserAvatar(clients.get(0).getAvatarPath());
			t.setUserEmail(clients.get(0).getEmail());
			t.setUserPhone(clients.get(0).getPhone());
			t.setUserAccountCreated(clients.get(0).getCreatedAt().atOffset(ZoneOffset.UTC));
			t = supportTicketRepo.save(t);

			// Attachements
			SupportAttachment att = new SupportAttachment();
			att.setTicket(t);
			att.setFilename("screenshot" + i + ".png");
			att.setUrl("https://example.com/files/screenshot" + i + ".png");
			att.setSize(1024000L);
			att.setMimeType("image/png");
			supportAttachmentRepo.save(att);
			if (collaborators.size() == 0) {
				int page = 0; // première page
				int size = 10; // nombre d'éléments par page

				Page<User> collaboratorPage = userRepo.findAllCollaborators(PageRequest.of(page, size));
				collaborators = collaboratorPage.getContent();
			}
			// Réponses
			SupportResponse r = new SupportResponse();
			r.setTicket(t);
			r.setMessage("Merci pour votre message, nous traitons votre demande.");
			r.setType(ResponseType.ADMIN_RESPONSE.getValue());
			r.setAuthorId(collaborators.get(0).getId());
			r.setAuthorEmail(collaborators.get(0).getEmail());
			r.setAuthorName(
					collaborators.get(0).getName().firstName() + " " + collaborators.get(0).getName().lastName());
			r.setAuthorRole(collaborators.get(0).getRole().getName());
			r.setIsInternal(true);
			supportResponseRepo.save(r);

			tickets.add(t);
		}

		// === Ajouter permissions personnalisées pour chaque collaborateur
		List<String> defaultPermissions = List.of("marketing.coupons", "website.blog", "settings.supplements");

		for (User collaborator : collaborators) {
			for (String perm : defaultPermissions) {
				UserPermission userPermission = new UserPermission();
				userPermission.setUser(collaborator);
				userPermission.setPermission(perm);
				userPermission.setGranted(true);
				userPermissionRepo.save(userPermission);
			}
		}

		// === Hero Section ===
		HomepageHero hero = new HomepageHero();
		hero.setTitle("Welcome to FoodDeals");
		hero.setSubtitle("Discover amazing food deals in your area");
		hero.setBackgroundImage("https://example.com/hero-bg.jpg");
		hero.setCtaText("Explore Deals");
		hero.setCtaLink("/deals");
		hero.setActive(true);
		heroRepository.save(hero);

		// === Featured Deal ===
		HomePageFeaturedDeal featuredDeal = new HomePageFeaturedDeal();
		featuredDeal.setTitle("Pizza Special");
		featuredDeal.setDescription("50% off on all pizzas");
		featuredDeal.setImage("https://example.com/pizza.jpg");
		featuredDeal.setOriginalPrice(25.99);
		featuredDeal.setDiscountedPrice(12.99);
		featuredDeal.setRestaurant("Mario's Pizza");
		featuredDeal.setRestaurantId(UUID.randomUUID()); // remplace par un ID réel si dispo
		featuredDeal.setActive(true);
		featuredDeal.setClassement(1);
		featuredDealRepository.save(featuredDeal);

		// === Category ===
		HomepageCategory category = new HomepageCategory();
		category.setName("Pizza");
		category.setIcon("https://example.com/pizza-icon.png");
		category.setActive(true);
		category.setClassement(1);
		categoryRepository.save(category);

		// === Testimonial ===
		HomepageTestimonial testimonial = new HomepageTestimonial();
		testimonial.setCustomerName("John Doe");
		testimonial.setRating(5);
		testimonial.setComment("Great deals and excellent service!");
		testimonial.setAvatar("https://example.com/avatar.jpg");
		testimonial.setActive(true);
		testimonial.setClassement(1);
		testimonialRepository.save(testimonial);

		// === Announcement ===
		HomepageAnnouncement announcement = new HomepageAnnouncement();
		announcement.setTitle("New Restaurant Added");
		announcement.setMessage("Check out our latest partner restaurant");
		announcement.setType("info");
		announcement.setActive(true);
		announcement.setExpiresAt(Instant.parse("2025-08-31T23:59:59Z"));
		announcementRepository.save(announcement);

		// === HomeSorting ===
		if (homeSortingRepo.count() == 0) {
			List<HomeSorting> homeSortings = List.of(createSorting("deal", 1, country, state, city),
					createSorting("box", 2, country, state, city), createSorting("hotel", 3, country, state, city),
					createSorting("architecture", 4, country, state, city),
					createSorting("boulangerie", 5, country, state, city));
			homeSortingRepo.saveAll(homeSortings);
		}

		// === Personalized Best Sellers ===
		if (personalizedBestSellerRepo.count() == 0) {
			List<PersonalizedBestSeller> sellers = List.of(
					createBestSeller("Sample Deal", "/img/sample.jpg", 123, 100, 4.5f, country, state, city),
					createBestSeller("Top Box", "/img/box.jpg", 88, 70, 4.7f, country, state, city));
			personalizedBestSellerRepo.saveAll(sellers);
		}

		// === CRM Demandes (seed) ===
		if (crmDemandeRepo.count() == 0) {
			// Demande 1
			CrmDemande d1 = new CrmDemande();
			d1.setId(UUID.randomUUID());
			d1.setType(DemandeType.MARKETPRO);
			d1.setCompanyName("Restaurant Le Gourmet");
			d1.setActivity(new LinkedHashSet<>(List.of("Restaurant", "Traiteur")));
			d1.setCountry("France");
			d1.setCity("Paris");
			d1.setDate(OffsetDateTime.now(ZoneOffset.UTC).minusDays(2));
			d1.setResponsable("Jean Dupont");
			d1.setAddress("123 Rue de la Paix, 75001 Paris");
			d1.setEmail("jean.dupont@legourmet.fr");
			d1.setPhone("+33 1 42 60 30 30");
			d1.setStatus(DemandeStatus.PENDING);
			d1.setNotes("Demande pour partenariat restaurant");

			CrmDemandeDocument d1doc1 = new CrmDemandeDocument();
			d1doc1.setId(UUID.randomUUID());
			d1doc1.setName("business_license.pdf");
			d1doc1.setUrl("/uploads/documents/business_license.pdf");
			d1doc1.setType("license");
			d1.addDocument(d1doc1); // <--- attache au parent (setDemande fait par helper)

			CrmDemandeHistory d1h1 = new CrmDemandeHistory();
			d1h1.setId(UUID.randomUUID());
			d1h1.setAction("created");
			d1h1.setPerformedBy("system");
			d1h1.setTimestamp(OffsetDateTime.now());
			d1h1.setDetails("Demande créée");
			d1.addHistory(d1h1); // <---

			CrmDemandeHistory d1h2 = new CrmDemandeHistory();
			d1h2.setId(UUID.randomUUID());
			d1h2.setAction("status_changed");
			d1h2.setPerformedBy("admin_123");
			d1h2.setTimestamp(OffsetDateTime.now());
			d1h2.setDetails("Statut changé vers 'pending'");
			d1.addHistory(d1h2); // <---

			// Demande 2 (même pattern)
			CrmDemande d2 = new CrmDemande();
			d2.setId(UUID.randomUUID());
			d2.setType(DemandeType.DLC);
			d2.setCompanyName("Boulangerie La Mie");
			d2.setActivity(new LinkedHashSet<>(List.of("Boulangerie")));
			d2.setCountry("France");
			d2.setCity("Lyon");
			d2.setDate(OffsetDateTime.now(ZoneOffset.UTC).minusDays(1));
			d2.setResponsable("Alice Martin");
			d2.setAddress("45 Rue Mercière, 69002 Lyon");
			d2.setEmail("contact@lamie.fr");
			d2.setPhone("+33 4 72 00 00 00");
			d2.setStatus(DemandeStatus.APPROVED);
			d2.setNotes("DLC proche, intérêt pour écoulement rapide");

			CrmDemandeDocument d2doc1 = new CrmDemandeDocument();
			d2doc1.setId(UUID.randomUUID());
			d2doc1.setName("kbis.pdf");
			d2doc1.setUrl("/uploads/documents/kbis.pdf");
			d2doc1.setType("legal");
			d2.addDocument(d2doc1);

			CrmDemandeHistory d2h1 = new CrmDemandeHistory();
			d2h1.setId(UUID.randomUUID());
			d2h1.setAction("created");
			d2h1.setPerformedBy("system");
			d2h1.setTimestamp(OffsetDateTime.now());
			d2h1.setDetails("Demande créée");
			d2.addHistory(d2h1);

			CrmDemandeHistory d2h2 = new CrmDemandeHistory();
			d2h2.setId(UUID.randomUUID());
			d2h2.setAction("status_changed");
			d2h2.setPerformedBy("admin_456");
			d2h2.setTimestamp(OffsetDateTime.now());
			d2h2.setDetails("Statut changé vers 'approved'");
			d2.addHistory(d2h2);

			// Demande 3 (association, sans doc)
			CrmDemande d3 = new CrmDemande();
			d3.setId(UUID.randomUUID());
			d3.setType(DemandeType.ASSOCIATION);
			d3.setCompanyName("Association Les Restos du Cœur");
			d3.setActivity(new LinkedHashSet<>(List.of("Association", "Collecte")));
			d3.setCountry("France");
			d3.setCity("Marseille");
			d3.setDate(OffsetDateTime.now(ZoneOffset.UTC).minusDays(3));
			d3.setResponsable("Karim Ben");
			d3.setAddress("12 Rue de la Solidarité, 13001 Marseille");
			d3.setEmail("contact@restoscoeur.fr");
			d3.setPhone("+33 4 91 00 00 00");
			d3.setStatus(DemandeStatus.REJECTED);
			d3.setNotes("Dossier incomplet (pièce identité manquante)");

			CrmDemandeHistory d3h1 = new CrmDemandeHistory();
			d3h1.setId(UUID.randomUUID());
			d3h1.setAction("created");
			d3h1.setPerformedBy("system");
			d3h1.setTimestamp(OffsetDateTime.now());
			d3h1.setDetails("Demande créée");
			d3.addHistory(d3h1);

			CrmDemandeHistory d3h2 = new CrmDemandeHistory();
			d3h2.setId(UUID.randomUUID());
			d3h2.setAction("status_changed");
			d3h2.setPerformedBy("admin_789");
			d3h2.setTimestamp(OffsetDateTime.now());
			d3h2.setDetails("Statut changé vers 'rejected' (dossier incomplet)");
			d3.addHistory(d3h2);

			// Persister uniquement les parents (cascade => documents + history)
			crmDemandeRepo.saveAll(List.of(d1, d2, d3));
			// Optionnel pour forcer l'ordre d’insertion immédiat
			crmDemandeRepo.flush();
		}

        // ================================
        // === ADMIN NOTIFICATIONS SEED ===
        // ================================

        List<User> admins = userRepo.findAll().stream()
                .filter(u -> u.getRole() != null && "ADMIN".equals(u.getRole().getName()))
                .toList();

        if (!admins.isEmpty() && adminNotificationRepo.count() == 0) {

            // 1️⃣ Préférences par défaut
            for (User admin : admins) {
                adminNotificationPreferencesRepo.findByAdminId(admin.getId())
                        .orElseGet(() -> {
                            AdminNotificationPreferences prefs = new AdminNotificationPreferences();
                            prefs.setAdmin(admin);
                            return adminNotificationPreferencesRepo.save(prefs);
                        });
            }

            // 2️⃣ Notifications exemples
            AdminNotification n1 = new AdminNotification();
            n1.setType(AdminNotificationType.ORDER);
            n1.setTitle("Nouvelle commande reçue");
            n1.setMessage("Commande #CMD-2025-001 de Jean Dupont");
            n1.setIcon("shopping-cart");
            n1.setActionUrl("/orders/123e4567-e89b-12d3-a456-426614174000");
            n1.setPriority(AdminNotificationPriority.MEDIUM);
            n1.setSourceApp(AdminNotificationSourceApp.CLIENT_APP);
            n1.setMetadataJson("""
        {
          "orderId": "123e4567-e89b-12d3-a456-426614174000",
          "clientName": "Jean Dupont",
          "orderRef": "CMD-2025-001",
          "totalAmount": 250.50
        }
    """);

            AdminNotification n2 = new AdminNotification();
            n2.setType(AdminNotificationType.SUPPORT);
            n2.setTitle("Nouveau ticket de support");
            n2.setMessage("Ticket #SUP-789 : Problème de paiement");
            n2.setIcon("help-circle");
            n2.setActionUrl("/support/tickets/789");
            n2.setPriority(AdminNotificationPriority.HIGH);
            n2.setSourceApp(AdminNotificationSourceApp.SYSTEM);
            n2.setMetadataJson("""
        {
          "ticketId": "SUP-789",
          "userName": "Marie Martin",
          "priority": "HIGH"
        }
    """);

            AdminNotification n3 = new AdminNotification();
            n3.setType(AdminNotificationType.PARTNER);
            n3.setTitle("Nouveau partenaire à valider");
            n3.setMessage("Restaurant 'Le Gourmet' demande validation");
            n3.setIcon("store");
            n3.setActionUrl("/partners/validation");
            n3.setPriority(AdminNotificationPriority.URGENT);
            n3.setSourceApp(AdminNotificationSourceApp.ADMIN_APP);
            n3.setMetadataJson("""
        {
          "partnerName": "Le Gourmet",
          "action": "REGISTRATION_REQUEST"
        }
    """);

            List<AdminNotification> notifications =
                    adminNotificationRepo.saveAll(List.of(n1, n2, n3));

            // 3️⃣ States (unread pour chaque admin)
            List<AdminNotificationState> states = new ArrayList<>();

            for (AdminNotification notif : notifications) {
                for (User admin : admins) {
                    AdminNotificationState s = new AdminNotificationState();
                    s.setNotification(notif);
                    s.setAdmin(admin);
                    s.setRead(false);
                    states.add(s);
                }
            }

            adminNotificationStateRepo.saveAll(states);
        }


    }

	private HomeSorting createSorting(String name, int rank, Country country, State state, City city) {
		HomeSorting hs = new HomeSorting();
		hs.setName(name);
		hs.setRank(rank);
		hs.setCountry(country);
		hs.setState(state);
		hs.setCity(city);
		hs.setOrderClass(rank);
		return hs;
	}

	private PersonalizedBestSeller createBestSeller(String name, String image, int totalSales, int completedOrders,
			float rating, Country country, State state, City city) {
		PersonalizedBestSeller bs = new PersonalizedBestSeller();
		bs.setName(name);
		bs.setImage(image);
		bs.setTotalSales(totalSales);
		bs.setCompletedOrders(completedOrders);
		bs.setRating(rating);
		bs.setCountry(country);
		bs.setState(state);
		bs.setCity(city);
		return bs;
	}

}