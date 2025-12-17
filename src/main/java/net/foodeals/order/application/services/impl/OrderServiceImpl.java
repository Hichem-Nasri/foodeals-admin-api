package net.foodeals.order.application.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.contract.application.service.CommissionService;
import net.foodeals.contract.domain.entities.Commission;
import net.foodeals.delivery.application.services.DeliveryService;
import net.foodeals.delivery.domain.enums.DeliveryStatus;
import net.foodeals.donate.domain.entities.enums.DonationType;
import net.foodeals.location.application.services.AddressService;
import net.foodeals.offer.application.services.OfferService;
import net.foodeals.offer.domain.entities.Box;
import net.foodeals.offer.domain.entities.BoxItem;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.offer.domain.entities.PublisherInfo;
import net.foodeals.offer.domain.enums.PublisherType;
import net.foodeals.offer.domain.repositories.BoxRepository;
import net.foodeals.offer.domain.repositories.DealRepository;
import net.foodeals.order.application.dtos.requests.OrderRequest;
import net.foodeals.order.application.dtos.responses.OrderDetailsDTO;
import net.foodeals.order.application.dtos.responses.OrderListResponseDto;
import net.foodeals.order.application.dtos.responses.OrderSummaryDto;
import net.foodeals.order.application.dtos.responses.PartnerInfoDto;
import net.foodeals.order.application.dtos.responses.ProductInOrderDTO;
import net.foodeals.order.application.dtos.responses.ProductInfoDto;
import net.foodeals.order.application.services.CouponService;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.Transaction;
import net.foodeals.order.domain.enums.*;
import net.foodeals.order.domain.exceptions.OrderNotFoundException;
import net.foodeals.order.domain.repositories.OrderRepository;
import net.foodeals.organizationEntity.application.services.OrganizationEntityService;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.organizationEntity.domain.entities.Contact;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.payment.application.dto.response.OperationsDto;
import net.foodeals.payment.application.dto.response.ProductInfo;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository repository;

	private final CouponService couponService;
	private final AddressService addressService;
	private final UserService userService;
	private final OfferService offerService;
	private final BoxRepository boxRepository;
	private final DealRepository dealRepository;
	private final DeliveryService deliveryService;
	private final SubEntityService subEntityService;
	private final OrganizationEntityService organizationEntityService;
	private final CommissionService commissionService;

	@Override
	@Transactional
	public List<Order> findByOfferPublisherInfoIdAndDate(UUID publisherId, Date date) {
		return this.repository.findOrdersByPublisherIdAndOrderDate(publisherId, date);
	}

	@Override
	@Transactional
	public Page<Order> findByOfferPublisherInfoIdAndDateAndStatus(UUID publisherId, Date date, OrderStatus status,
			TransactionStatus transactionStatus, Pageable pageable) {
		return this.repository.findOrdersByPublisherIdAndOrderDateAndStatusAndTransactionStatus(publisherId, date,
				status, transactionStatus, pageable);
	}

	@Override
	@Transactional
	public Page<Order> findByOfferPublisherInfoIdAndDate(UUID publisherId, Date date, Pageable pageable) {
		return this.repository.findOrdersByPublisherIdAndOrderDate(publisherId, date, pageable);
	}

	@Override
	@Transactional
	public List<Order> findByOfferPublisherInfoIdAndDateAndStatus(UUID publisherId, Date date, OrderStatus status,
			TransactionStatus transactionStatus) {
		return this.repository.findOrdersByPublisherIdAndOrderDateAndStatusAndTransactionStatus(publisherId, date,
				status, transactionStatus);
	}

	@Override
	@Transactional
	public Page<Order> findOrdersByOrganizationAndDeliveryStatusAndCriteria(UUID organizationId,
			DeliveryStatus deliveryStatus, Date orderDate, OrderStatus orderStatus, TransactionStatus transactionStatus,
			Pageable pageable) {
		return this.repository.findOrdersByOrganizationAndDeliveryStatusAndCriteria(organizationId, deliveryStatus,
				orderDate, orderStatus, transactionStatus, pageable);
	}

	@Override
	@Transactional
	public List<Order> findOrdersByOrganizationAndDeliveryStatusAndCriteria(UUID organizationId,
			DeliveryStatus deliveryStatus, Date orderDate, OrderStatus orderStatus,
			TransactionStatus transactionStatus) {
		return this.repository.findOrdersByOrganizationAndDeliveryStatusAndCriteria(organizationId, deliveryStatus,
				orderDate, orderStatus, transactionStatus);
	}

	@Override
	@Transactional
	public Page<OperationsDto> getOperationsByOrderId(UUID orderId, Pageable pageable) {
		Order order = this.repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
		List<Order> orders = this.repository.findOrdersByDeliveryId(order.getDelivery().getId());
		List<OperationsDto> operationsDtos = new ArrayList<>();
		UUID organizationId = order.getOffer().getPublisherInfo().type().equals(PublisherType.PARTNER_SB)
				? this.subEntityService.getEntityById(order.getOffer().getPublisherInfo().id()).getOrganizationEntity()
						.getId()
				: order.getOffer().getPublisherInfo().id();
		Commission commission = this.commissionService.getCommissionByPartnerId(organizationId);
		for (Order sameDeliveryOrder : orders) {
			Transaction transaction = sameDeliveryOrder.getTransaction();
			boolean isCash = transaction.getType().equals(TransactionType.CASH);
			Price amount = transaction.getPrice();
			Price cashAmount = isCash ? amount : Price.ZERO(Currency.getInstance("MAD"));
			Price cardAmount = isCash ? Price.ZERO(Currency.getInstance("MAD")) : amount;
			BigDecimal commissionRate = BigDecimal.valueOf(isCash ? commission.getCash() : commission.getCard())
					.divide(BigDecimal.valueOf(100));
			Price commissionAmount = new Price(commissionRate.multiply(amount.amount()), Currency.getInstance("MAD"));
			Price cashCommission = isCash ? commissionAmount : Price.ZERO(Currency.getInstance("MAD"));
			Price cardCommission = isCash ? Price.ZERO(Currency.getInstance("MAD")) : commissionAmount;
			ProductInfo productInfo = new ProductInfo(sameDeliveryOrder.getOffer().getTitle(),
					sameDeliveryOrder.getOffer().getImagePath());
			OperationsDto operationsDto = new OperationsDto(OrderDeliveryType.SINGLE, productInfo,
					sameDeliveryOrder.getId(), sameDeliveryOrder.getOffer().getSalePrice(),
					sameDeliveryOrder.getQuantity(), cashAmount, cashCommission, cardAmount, cardCommission);
			operationsDtos.add(operationsDto);
		}
		return new PageImpl<>(operationsDtos, pageable, operationsDtos.size());
	}

	@Override
	@Transactional
	public List<Order> findAll() {
		return repository.findAll();
	}

	@Override
	@Transactional
	public Page<Order> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	@Transactional
	public Page<Order> findAll(Pageable pageable) {
		return null;
	}

	@Override
	@Transactional
	public Order findById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
	}

	@Override
	@Transactional
	public Order create(OrderRequest request) {
		/*
		 * final User client = userService.findById(request.clientId()); final Offer
		 * offer = offerService.findById(request.offerId());
		 * 
		 * final Order order = Order.create(request.price(), request.type(),
		 * request.status(), client, offer);
		 * 
		 * if (request.type().equals(OrderType.DELIVERY)) { order
		 * .setShippingAddress(addressService.create(request.shippingAddress()))
		 * .setDelivery(deliveryService.create(request.delivery())); }
		 * 
		 * if (request.couponId() != null) { order.setCoupon(
		 * couponService.findById(request.couponId()) ); }
		 */
		return null;
	}

	@Override
	@Transactional
	public Order update(UUID id, OrderRequest request) {
		return null;
	}

	@Override
	@Transactional
	public void delete(UUID id) {
		if (repository.existsById(id))
			throw new OrderNotFoundException(id);

		repository.softDelete(id);
	}

	@Override
	public OrderListResponseDto getOrdersWithStats(Pageable pageable) {
		Page<Order> orders = repository.findAll(pageable);

		long seenCount = repository.countBySeen(true);
		long notSeenCount = repository.countBySeen(false);
		long deliveredCount = repository.countByStatus(OrderStatus.COMPLETED);
		long notDeliveredCount = repository.countByStatusNot(OrderStatus.COMPLETED);

		List<OrderSummaryDto> content = new ArrayList<>();

		for (Order order : orders) {
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

			String email = null;
			String phone = null;
			if (order.getOffer().getOrganizationEntity() != null) {
				List<Contact> contacts = order.getOffer().getOrganizationEntity().getContacts();
				if (contacts != null && !contacts.isEmpty()) {
					Contact mainContact = contacts.get(0);
					email = mainContact.getEmail();
					phone = mainContact.getPhone();
				}
			}

			List<ProductInfoDto> productDtos = new ArrayList<>();
			for (Product product : products) {
				ProductInfoDto dto = new ProductInfoDto();
				dto.setName(product.getName());
				dto.setImageUrl(product.getProductImagePath());
				productDtos.add(dto);
			}

			OrderSummaryDto summary = new OrderSummaryDto();
			if (order.getTransaction() != null) {
				summary.setRef(order.getTransaction().getReference());
			}
			summary.setId(order.getId());
			summary.setSeen(order.isSeen());
			summary.setCollectionDate(null);
			summary.setPartnerName(order.getOffer().getOrganizationEntity() != null
					? order.getOffer().getOrganizationEntity().getName()
					: "empty");
			summary.setClientEmail(email);
			summary.setClientPhone(phone);
			summary.setClientId(order.getClient().getId());
			summary.setClientName(
					order.getClient().getName().firstName() + " " + order.getClient().getName().lastName());
			summary.setProducts(productDtos);
			if (order.getDonate() != null) {
				summary.setDonationType(order.getDonate().getDonationType().name());
			}

			content.add(summary);
		}

		OrderListResponseDto orderDto = new OrderListResponseDto();
		orderDto.setOrders(content);
		orderDto.setTotalSeenOrders(seenCount);
		orderDto.setTotalNotSeenOrders(notSeenCount);
		orderDto.setTotalDeliveredOrders(deliveredCount);
		orderDto.setTotalNotDeliveredOrders(notDeliveredCount);

		return orderDto;
	}

	public OrderListResponseDto getOrdersWithStats(Pageable pageable, String clientType) {
		Page<Order> orders = switch (clientType.toLowerCase()) {
		case "client" -> repository.findAllByClientIsNotNull(pageable);
		case "pro" -> repository.findAllByClientProIsNotNull(pageable);
		default -> repository.findAll(pageable);
		};

		long seenCount = repository.countBySeen(true);
		long notSeenCount = repository.countBySeen(false);
		long deliveredCount = repository.countByStatus(OrderStatus.COMPLETED);
		long notDeliveredCount = repository.countByStatusNot(OrderStatus.COMPLETED);

		List<OrderSummaryDto> content = new ArrayList<>();

		for (Order order : orders) {
			List<Product> products = new ArrayList<>();

			try {
				Deal deal = dealRepository.getDealByOfferId(order.getOffer().getId());
				if (deal != null && deal.getProduct() != null)
					products.add(deal.getProduct());
			} catch (Exception ignored) {
			}

			try {
				Box box = boxRepository.getBoxByOfferId(order.getOffer().getId());
				if (box != null && box.getBoxItems() != null) {
					for (BoxItem item : box.getBoxItems())
						if (item.getProduct() != null)
							products.add(item.getProduct());
				}
			} catch (Exception ignored) {
			}

			// Contact partenaire (inchangé)
			String partnerEmail = null, partnerPhone = null;
			if (order.getOffer().getOrganizationEntity() != null) {
				List<Contact> contacts = order.getOffer().getOrganizationEntity().getContacts();
				if (contacts != null && !contacts.isEmpty()) {
					Contact c = contacts.get(0);
					partnerEmail = c.getEmail();
					partnerPhone = c.getPhone();
				}
			}

			Double price =null;
			// Produits -> DTO
			List<ProductInfoDto> productDtos = new ArrayList<>();
			for (Product p : products) {
				ProductInfoDto dto = new ProductInfoDto();
				dto.setName(p.getName());
				dto.setImageUrl(p.getProductImagePath());
				productDtos.add(dto);
			}

			// --------- NOUVELLE PARTIE: mapping client en fonction de clientType ---------
			String clientName = null, clientEmail = null, clientPhone = null;
			Integer clientId = null; // attention: SubEntity.id est souvent UUID; on ne l’expose pas ici

			String resolvedType = clientType.equalsIgnoreCase("all") ? (order.getClientPro() != null ? "pro" : "client")
					: clientType.toLowerCase();

			if ("pro".equals(resolvedType)) {
				// Remplir depuis SubEntity (clientPro)
				if (order.getClientPro() != null) {
					clientName = order.getClientPro().getName();
					clientEmail = order.getClientPro().getEmail();
					clientPhone = order.getClientPro().getPhone();
					// clientId -> on laisse null si types différents; si besoin, change le type de
					// clientId en String/UUID
				}
			} else {
				// Remplir depuis User (client particulier)
				if (order.getClient() != null) {
					clientId = order.getClient().getId(); // si c’est un Integer chez toi
					String fn = order.getClient().getName() != null ? order.getClient().getName().firstName() : "";
					String ln = order.getClient().getName() != null ? order.getClient().getName().lastName() : "";
					clientName = (fn + " " + ln).trim();
					clientEmail = order.getClient().getEmail();
					clientPhone = order.getClient().getPhone();
				}
			}
			// ---------------------------------------------------------------------------

			OrderSummaryDto summary = new OrderSummaryDto();
			if (order.getTransaction() != null)
				summary.setRef(order.getTransaction().getReference());
			summary.setId(order.getId());
			summary.setSeen(order.isSeen());
			summary.setCollectionDate(null);
			summary.setPartnerEmail(partnerEmail);
			summary.setPartnerPhone(partnerPhone);
			summary.setPartnerName(order.getOffer().getOrganizationEntity() != null
					? order.getOffer().getOrganizationEntity().getName()
					: "empty");
			summary.setClientEmail(clientEmail != null ? clientEmail : partnerEmail); // fallback si besoin
			summary.setClientPhone(clientPhone != null ? clientPhone : partnerPhone); // fallback si besoin
			summary.setClientId(clientId);
			summary.setClientName(clientName);
			summary.setProducts(productDtos);
			price=order.getPrice().amount().doubleValue();
			summary.setPrice(price);
			if (order.getDonate() != null)
				summary.setDonationType(order.getDonate().getDonationType().name());
			// Optionnel: exposer orderStatus/deliveryStatus si utile
			summary.setOrderStatus(order.getStatus() != null ? order.getStatus().name() : null);
			summary.setDeliveryStatus(order.getDelivery() != null ? order.getDelivery().getStatus().name() : null);

			content.add(summary);
		}

		OrderListResponseDto orderDto = new OrderListResponseDto();
		orderDto.setOrders(content);
		orderDto.setTotalSeenOrders(seenCount);
		orderDto.setTotalNotSeenOrders(notSeenCount);
		orderDto.setTotalDeliveredOrders(deliveredCount);
		orderDto.setTotalNotDeliveredOrders(notDeliveredCount);

		orderDto.setCurrentPage(orders.getNumber() + 1);
		orderDto.setTotalPages(orders.getTotalPages());
		orderDto.setTotalItems(orders.getTotalElements());

		return orderDto;
	}

	@Override
	public OrderDetailsDTO getOrderDetails(UUID id) {
		Order order = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Commande introuvable"));
		Deal deal = dealRepository.getDealByOfferId(order.getOffer().getId());
		Box box = boxRepository.getBoxByOfferId(order.getOffer().getId());
		List<Product> products = new ArrayList<>();
		if (deal != null) {
			products.add(deal.getProduct());
		}
		if (box != null) {
			for (BoxItem item : box.getBoxItems()) {
				products.add(item.getProduct());
			}
		}

		return OrderDetailsDTO.builder().id(order.getId()).ref(order.getTransaction().getReference())
				.status(order.getStatus().name()).collectionDate(order.getCreatedAt())
				.collectionTime(order.getCreatedAt()).promoCode(null)
				.deliveryAddress(order.getClient().getAddress().getAddress()).phoneNumber(order.getClient().getPhone())
				.quantity(order.getQuantity()).deliveryMethod("Delivery") // TODO
				.contactName(order.getClient().getName().firstName() + " " + order.getClient().getName().lastName())
				.paymentMethod("CARD")// TODO
				.contactPhone(order.getClient().getPhone()).deliveryCompany("Non renseigné") // TODO
				.deliveryDriverName(order.getDelivery().getDeliveryBoy().getName().firstName())
				.deliveryDriverPhone(order.getDelivery().getDeliveryBoy().getPhone()).category("Non renseigné")
				.seen(order.isSeen())
				.products(
						products.stream()
								.map(p -> ProductInOrderDTO.builder().id(p.getId()).ref(p.getBarcode())
										.productName(p.getName()).quantity(p.getBoxItems().size()).build())
								.toList())
				.build();
	}

	public Page<OrderSummaryDto> getOrdersForClient(String clientId, String clientType, Pageable pageable) {
	    Page<Order> orders;

	    if ("client".equalsIgnoreCase(clientType)) {
	        orders = repository.findByClient_Id(Integer.valueOf(clientId), pageable);
	    } else if ("pro".equalsIgnoreCase(clientType)) {
	        orders = repository.findByClientPro_Id(UUID.fromString(clientId), pageable);
	    } else {
	        throw new IllegalArgumentException("Invalid clientType: must be 'client' or 'pro'");
	    }

	    switch (clientType.toLowerCase()) {
        case "client" -> {
            Integer parsedId;
            try {
                parsedId = Integer.valueOf(clientId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid clientId for client (expected Integer)");
            }
            orders = repository.findByClient_Id(parsedId, pageable);
        }
        case "pro" -> {
            UUID parsedId;
            try {
                parsedId = UUID.fromString(clientId);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid clientId for pro (expected UUID)");
            }
            orders = repository.findByClientPro_Id(parsedId, pageable);
        }
        default -> throw new IllegalArgumentException("clientType must be 'client' or 'pro'");
    }
	    
	    return orders.map(order -> {
	        List<Product> products = new ArrayList<>();

	        // 🔹 Récupérer produits via Deal
	        try {
	            Deal deal = dealRepository.getDealByOfferId(order.getOffer().getId());
	            if (deal != null && deal.getProduct() != null) {
	                products.add(deal.getProduct());
	            }
	        } catch (Exception ignored) {}

	        // 🔹 Récupérer produits via Box
	        try {
	            Box box = boxRepository.getBoxByOfferId(order.getOffer().getId());
	            if (box != null && box.getBoxItems() != null) {
	                for (BoxItem item : box.getBoxItems()) {
	                    if (item.getProduct() != null) {
	                        products.add(item.getProduct());
	                    }
	                }
	            }
	        } catch (Exception ignored) {}

	        // 🔹 Contacts partenaire
	        String partnerEmail = null, partnerPhone = null, partnerName = "empty";
	        if (order.getOffer().getOrganizationEntity() != null) {
	            partnerName = order.getOffer().getOrganizationEntity().getName();
	            List<Contact> contacts = order.getOffer().getOrganizationEntity().getContacts();
	            if (contacts != null && !contacts.isEmpty()) {
	                Contact c = contacts.get(0);
	                partnerEmail = c.getEmail();
	                partnerPhone = c.getPhone();
	            }
	        }

	        // 🔹 Mapping client (User vs Pro)
	        String clientName = null, clientEmail = null, clientPhone = null;
	        Integer clientIdInt = null; 
	        String   partnerAdress=null;

	        if ("pro".equalsIgnoreCase(clientType)) {
	            if (order.getClientPro() != null) {
	                clientName = order.getClientPro().getName();
	                clientEmail = order.getClientPro().getEmail();
	                clientPhone = order.getClientPro().getPhone();
	                partnerAdress=order.getOffer().getOrganizationEntity().getAddress().getAddress();	            }
	        } else {
	            if (order.getClient() != null) {
	                clientIdInt = order.getClient().getId();
	                String fn = order.getClient().getName() != null ? order.getClient().getName().firstName() : "";
	                String ln = order.getClient().getName() != null ? order.getClient().getName().lastName() : "";
	                clientName = (fn + " " + ln).trim();
	                clientEmail = order.getClient().getEmail();
	                clientPhone = order.getClient().getPhone();
	            }
	        }

	        // 🔹 Produits → DTO
	        List<ProductInfoDto> productDtos = products.stream().map(p -> {
	            ProductInfoDto dto = new ProductInfoDto();
	            dto.setName(p.getName());
	            dto.setImageUrl(p.getProductImagePath());
	            return dto;
	        }).toList();

	        // 🔹 Construire OrderSummaryDto
	        OrderSummaryDto summary = new OrderSummaryDto();
	        if (order.getTransaction() != null) {
	            summary.setRef(order.getTransaction().getReference());
	        }
	        summary.setId(order.getId());
	      Double  price=order.getPrice().amount().doubleValue();
	      summary.setPrice(price);
		    summary.setPartnerName(partnerName);
		    summary.setPartnerEmail(clientEmail);
		    summary.setPartnerPhone(clientPhone);
		    summary.setPartnerAddress(partnerAdress);
	        summary.setSeen(order.isSeen());
	        summary.setCollectionDate(null);
	        summary.setPartnerName(partnerName);
	        summary.setClientEmail(clientEmail != null ? clientEmail : partnerEmail);
	        summary.setClientPhone(clientPhone != null ? clientPhone : partnerPhone);
	        summary.setClientId(clientIdInt);
	        summary.setClientName(clientName);
	        summary.setProducts(productDtos);
	        if (order.getDonate() != null) {
	            summary.setDonationType(order.getDonate().getDonationType().name());
	        }
	        summary.setOrderStatus(order.getStatus() != null ? order.getStatus().name() : null);
	        summary.setDeliveryStatus(order.getDelivery() != null ? order.getDelivery().getStatus().name() : null);
	        summary.setClientType(order.getClientPro() != null ? "pro" : "client");

	        return summary;
	    });
	}

	@Override
	public Page<OrderSummaryDto> searchOrders(String name, Pageable pageable) {
	    Page<Order> orders = repository.searchOrdersByProductName(name, pageable);
	    return orders.map(this::toSummary);
	}

	private OrderSummaryDto toSummary(Order order) {
	    List<Product> products = new ArrayList<>();

	    // Produits via Deal
	    try {
	        Deal deal = dealRepository.getDealByOfferId(order.getOffer().getId());
	        if (deal != null && deal.getProduct() != null) {
	            products.add(deal.getProduct());
	        }
	    } catch (Exception ignored) {}

	    // Produits via Box
	    try {
	        Box box = boxRepository.getBoxByOfferId(order.getOffer().getId());
	        if (box != null && box.getBoxItems() != null) {
	            for (BoxItem item : box.getBoxItems()) {
	                if (item.getProduct() != null) {
	                    products.add(item.getProduct());
	                }
	            }
	        }
	    } catch (Exception ignored) {}

	    // Mapping produits → DTO
	    List<ProductInfoDto> productDtos = new ArrayList<>();
	    Double price =null;
	    for (Product p : products) {
	        ProductInfoDto dto = new ProductInfoDto();
	        dto.setName(p.getName());
	        dto.setImageUrl(p.getProductImagePath());
	        productDtos.add(dto);
	    }

	    // Contact partenaire
	    String partnerName = "empty";
	    String partnerEmail = null;
	    String partnerPhone = null;
	    String partnerAdress=null;
	    if (order.getOffer().getOrganizationEntity() != null) {
	        partnerName = order.getOffer().getOrganizationEntity().getName();
	        partnerAdress=order.getOffer().getOrganizationEntity().getAddress().getAddress();
	        if (order.getOffer().getOrganizationEntity().getContacts() != null
	                && !order.getOffer().getOrganizationEntity().getContacts().isEmpty()) {
	            Contact c = order.getOffer().getOrganizationEntity().getContacts().get(0);
	            partnerEmail = c.getEmail();
	            partnerPhone = c.getPhone();
	        }
	    }

	    // Client (User ou Pro)
	    String clientName = null, clientEmail = null, clientPhone = null;
	    Integer clientId = null;

	    if (order.getClient() != null) { // client classique
	        clientId = order.getClient().getId();
	        String fn = order.getClient().getName() != null ? order.getClient().getName().firstName() : "";
	        String ln = order.getClient().getName() != null ? order.getClient().getName().lastName() : "";
	        clientName = (fn + " " + ln).trim();
	        clientEmail = order.getClient().getEmail();
	        clientPhone = order.getClient().getPhone();
	    } else if (order.getClientPro() != null) { // client pro
	        clientName = order.getClientPro().getName();
	        clientEmail = order.getClientPro().getEmail();
	        clientPhone = order.getClientPro().getPhone();
	    }

	    // Build summary
	    OrderSummaryDto summary = new OrderSummaryDto();
	    if (order.getTransaction() != null) {
	        summary.setRef(order.getTransaction().getReference());
	    }
	    summary.setId(order.getId());
		price=order.getPrice().amount().doubleValue();
	    summary.setPrice(price);
	    summary.setPartnerName(partnerName);
	    summary.setPartnerEmail(partnerEmail);
	    summary.setPartnerPhone(partnerPhone);
	    summary.setPartnerAddress(partnerAdress);
	    summary.setSeen(order.isSeen());
	    summary.setCollectionDate(null); // si tu veux gérer après
	    summary.setPartnerName(partnerName);
	    summary.setClientEmail(clientEmail != null ? clientEmail : partnerEmail);
	    summary.setClientPhone(clientPhone != null ? clientPhone : partnerPhone);
	    summary.setClientId(clientId);
	    summary.setClientName(clientName);
	    summary.setProducts(productDtos);
	    if (order.getDonate() != null) {
	        summary.setDonationType(order.getDonate().getDonationType().name());
	    }
	    summary.setOrderStatus(order.getStatus() != null ? order.getStatus().name() : null);
	    summary.setDeliveryStatus(order.getDelivery() != null ? order.getDelivery().getStatus().name() : null);

	    return summary;
	}



	

}
