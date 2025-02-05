package net.foodeals.payment.application.services.impl;//package net.foodeals.payment.application.services;
//
//import jakarta.transaction.Transactional;
//import net.foodeals.contract.application.service.CommissionService;
//import net.foodeals.contract.application.service.DeadlinesService;
//import net.foodeals.contract.application.service.SubscriptionService;
//import net.foodeals.contract.domain.entities.Commission;
//import net.foodeals.contract.domain.entities.Deadlines;
//import net.foodeals.contract.domain.entities.Subscription;
//import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
//import net.foodeals.payment.application.dto.response.CommissionPaymentDto;
//import net.foodeals.payment.application.dto.response.DeadlinesDto;
//import net.foodeals.payment.application.dto.response.PartnerInfoDto;
//import net.foodeals.payment.application.dto.response.SubscriptionPaymentDto;
//import net.foodeals.payment.domain.entities.Payment;
//import net.foodeals.payment.domain.entities.Enum.PartnerType;
//import net.foodeals.payment.domain.repository.PaymentRepository;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PaymentServiceImpl {
//    private final PaymentRepository paymentRepository;
//    private final ModelMapper modelMapper;
//    private final CommissionService commissionService;
//    private final SubscriptionService subscriptionService;
//    private final DeadlinesService deadlinesService;
//
//
//    public PaymentServiceImpl(PaymentRepository paymentRepository, ModelMapper modelMapper, CommissionService commissionService, SubscriptionService subscriptionService, DeadlinesService deadlinesService) {
//        this.paymentRepository = paymentRepository;
//        this.modelMapper = modelMapper;
//        this.commissionService = commissionService;
//        this.subscriptionService = subscriptionService;
//        this.deadlinesService = deadlinesService;
//    }
//
//    public Page<Payment> getCommissionPayments(Pageable page) {
//        return this.paymentRepository.findAll(page);
//    }
//
//    @Transactional
//    public CommissionPaymentDto toCommissionPaymentDto(Payment payment) {
//        CommissionPaymentDto paymentDto = this.modelMapper.map(payment, CommissionPaymentDto.class);
//        OrganizationEntity organizationEntity = payment.getPartnerType() == PartnerType.PARTNER ? payment.getOrganizationEntity()
//                    : payment.getSubEntity().getOrganizationEntity();
//
//        Commission commission = this.commissionService.getCommissionByPartnerName(organizationEntity.getName());
//
//        Double commissionTotal = ((Double)(commission.getCard().doubleValue() / 100)) * payment.getPaymentsWithCard() + ((Double)(commission.getCash().doubleValue() / 100)) * payment.getPaymentsWithCash();
//        Double difference = payment.getPaymentsWithCard() - commissionTotal;
//        Double toPay = difference < 0 ? 0 : difference;
//        paymentDto.setToPay(toPay.toString());
//        Double toReceive = difference < 0 ? difference : 0;
//        paymentDto.setToReceive(toReceive.toString());
//        paymentDto.setFoodealsCommission(commissionTotal.toString());
//        PartnerInfoDto partnerInfoDto = new PartnerInfoDto(organizationEntity.getName(), organizationEntity.getAvatarPath());
//        paymentDto.setPartnerType(payment.getPartnerType());
//        paymentDto.setPartnerInfoDto(partnerInfoDto);
//        return paymentDto;
//    }
//
//    public Page<Subscription> getSubscriptionPayments(Pageable page) {
//        return this.subscriptionService.findAll(page);
//    }
//
//    public SubscriptionPaymentDto toSubscriptionPaymentDto(Subscription subscription) {
//        SubscriptionPaymentDto subscriptionPaymentDto = this.modelMapper.map(subscription, SubscriptionPaymentDto.class);
//        List<Deadlines> deadlines = subscription.getDeadlines();
//        List<DeadlinesDto> deadlinesDtos =  deadlines.stream().map(this.deadlinesService::toDeadlineDto).toList();
//
//        List<String> solutions = subscription.getSolutionContracts().stream().map(solutionContract -> solutionContract.getSolution().getName()).toList();
//
//        subscriptionPaymentDto.setSolutions(solutions);
//
//        subscriptionPaymentDto.setDeadlinesDtoList(deadlinesDtos);
////        String partnerName = subscription.getPartnerType() == PartnerType.ORGANIZATION_ENTITY ? subscription.getOrganizationEntity().getName()
////                                        : subscription.getSubEntity().getName();
//        String partnerName = "test";
////        String avatarPath = subscription.getPartnerType() == PartnerType.ORGANIZATION_ENTITY ? subscription.getOrganizationEntity().getAvatarPath()
////                : subscription.getSubEntity().getAvatarPath();
//
//        String avatarPath = "12";
//
//        subscriptionPaymentDto.setTotalAmount(subscription.getAmount().amount());
//        PartnerInfoDto partnerInfoDto = PartnerInfoDto.builder().name(partnerName)
//                .avatarPath(avatarPath)
//                .build();
//        subscriptionPaymentDto.setPartnerType(subscription.getPartnerType());
//        subscriptionPaymentDto.setPartnerInfoDto(partnerInfoDto);
//        return subscriptionPaymentDto;
//    }
//}

import jakarta.transaction.Transactional;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.contract.application.service.CommissionService;
import net.foodeals.contract.application.service.DeadlinesService;
import net.foodeals.contract.application.service.SubscriptionService;
import net.foodeals.contract.domain.entities.Commission;
import net.foodeals.contract.domain.entities.Deadlines;
import net.foodeals.contract.domain.entities.Subscription;
import net.foodeals.contract.domain.entities.enums.DeadlineStatus;
import net.foodeals.contract.domain.entities.enums.SubscriptionStatus;
import net.foodeals.delivery.application.services.DeliveryService;
import net.foodeals.delivery.domain.enums.DeliveryStatus;
import net.foodeals.offer.domain.enums.PublisherType;
import net.foodeals.order.application.services.OrderService;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.order.domain.entities.Transaction;
import net.foodeals.order.domain.enums.OrderDeliveryType;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.TransactionStatus;
import net.foodeals.order.domain.enums.TransactionType;
import net.foodeals.organizationEntity.application.services.OrganizationEntityService;
import net.foodeals.organizationEntity.application.services.SubEntityService;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.payment.application.dto.request.PaymentRequest;
import net.foodeals.payment.application.dto.request.PaymentType;
import net.foodeals.payment.application.dto.request.ReceiveDto;
import net.foodeals.payment.application.dto.response.*;
import net.foodeals.payment.application.services.PaymentService;
import net.foodeals.payment.application.services.utils.PaymentProcessor;
import net.foodeals.payment.domain.entities.Enum.PaymentDirection;
import net.foodeals.payment.domain.entities.Enum.PaymentResponsibility;
import net.foodeals.payment.domain.entities.Enum.PaymentStatus;
import net.foodeals.payment.domain.entities.PartnerCommissions;
import net.foodeals.payment.domain.entities.Enum.PartnerType;
import net.foodeals.payment.domain.entities.PartnerI;
import net.foodeals.payment.domain.entities.PaymentMethod;
import net.foodeals.payment.domain.repository.PartnerCommissionsRepository;
import net.foodeals.user.application.dtos.responses.SimpleUserDto;
import net.foodeals.user.domain.entities.User;
import org.apache.coyote.BadRequestException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PartnerCommissionsRepository partnerCommissionsRepository;
    private final ModelMapper modelMapper;
    private final CommissionService commissionService;
    private final DeliveryService deliveryService;
    private final SubscriptionService subscriptionService;
    private final DeadlinesService deadlinesService;
    private final Map<String, PaymentProcessor> processors;
    private final SubEntityService subEntityService;
    private final OrganizationEntityService organizationEntityService;
    private final OrderService orderService;

    public PaymentServiceImpl(PartnerCommissionsRepository partnerCommissionsRepository, ModelMapper modelMapper, CommissionService commissionService, DeliveryService deliveryService, SubscriptionService subscriptionService, DeadlinesService deadlinesService, List<PaymentProcessor> processorList, SubEntityService subEntityService, OrganizationEntityService organizationEntityService, OrderService orderService) {
        this.partnerCommissionsRepository = partnerCommissionsRepository;
        this.modelMapper = modelMapper;
        this.commissionService = commissionService;
        this.deliveryService = deliveryService;
        this.subscriptionService = subscriptionService;
        this.deadlinesService = deadlinesService;
        this.processors = processorList.stream()
                .collect(Collectors.toMap(
                        processor -> {
                            // Extract the original name without CGLIB proxy suffixes
                            String fullClassName = processor.getClass().getName();
                            String simpleName = fullClassName.contains("$$")
                                    ? fullClassName.substring(0, fullClassName.indexOf("$$"))
                                    : fullClassName;

                            // Get the simple class name and convert to lowercase
                            String processorName = simpleName.substring(simpleName.lastIndexOf('.') + 1)
                                    .toLowerCase()
                                    .replace("processor", "");

                            return processorName;
                        },
                        Function.identity()
                ));
        this.subEntityService = subEntityService;
        this.organizationEntityService = organizationEntityService;
        this.orderService = orderService;
    }

    public List<PartnerCommissions> getCommissionPayments(int year, int month) {
        return this.partnerCommissionsRepository.findCommissionsByDateAndPartnerInfoTypeNot(year, month, PartnerType.DELIVERY_PARTNER);
    }


//    @Transactional
//    public CommissionPaymentDto toCommissionPaymentDto(List<PartnerCommissions> partnerCommissions) {
//        List<CommissionPaymentDto> commissionPaymentDtos  = partnerCommissions.this.modelMapper.map(partnerCommissions, CommissionPaymentDto.class);
////        CommissionPaymentDto paymentDto = this.modelMapper.map(partnerCommissions, CommissionPaymentDto.class);
////        OrganizationEntity organizationEntity = partnerCommissions.getPartnerType() == PartnerType.PARTNER ? partnerCommissions.getOrganizationEntity()
////                    : partnerCommissions.getSubEntity().getOrganizationEntity();
////
////        Commission commission = this.commissionService.getCommissionByPartnerName(organizationEntity.getName());
////
////        Double commissionTotal = ((Double)(commission.getCard().doubleValue() / 100)) * partnerCommissions.getPaymentsWithCard() + ((Double)(commission.getCash().doubleValue() / 100)) * partnerCommissions.getPaymentsWithCash();
////        Double difference = partnerCommissions.getPaymentsWithCard() - commissionTotal;
////        Double toPay = difference < 0 ? 0 : difference;
////        paymentDto.setToPay(toPay.toString());
////        Double toReceive = difference < 0 ? difference : 0;
////        paymentDto.setToReceive(toReceive.toString());
////        paymentDto.setFoodealsCommission(commissionTotal.toString());
////        PartnerInfoDto partnerInfoDto = new PartnerInfoDto(organizationEntity.getName(), organizationEntity.getAvatarPath());
////        paymentDto.setPartnerType(partnerCommissions.getPartnerType());
////        paymentDto.setPartnerInfoDto(partnerInfoDto);
////        return paymentDto;
//    }

    @Transactional
    public SubscriptionPaymentDto toSubscriptionPaymentDto(Subscription subscription) {
//        SubscriptionPaymentDto subscriptionPaymentDto = this.modelMapper.map(subscription, SubscriptionPaymentDto.class);
//        List<Deadlines> deadlines = subscription.getDeadlines();
//        List<DeadlinesDto> deadlinesDtos =  deadlines.stream().map(this.deadlinesService::toDeadlineDto).toList();
//
//        List<String> solutions = subscription.getSolutionContracts().stream().map(solutionContract -> solutionContract.getSolution().getName()).toList();
//
//        subscriptionPaymentDto.setSolutions(solutions);
//
//        subscriptionPaymentDto.setDeadlinesDtoList(deadlinesDtos);
////        String partnerName = subscription.getPartnerType() == PartnerType.ORGANIZATION_ENTITY ? subscription.getOrganizationEntity().getName()
////                                        : subscription.getSubEntity().getName();
//        String partnerName = "test";
////        String avatarPath = subscription.getPartnerType() == PartnerType.ORGANIZATION_ENTITY ? subscription.getOrganizationEntity().getAvatarPath()
////                : subscription.getSubEntity().getAvatarPath();
//
//        String avatarPath = "12";
//
//        subscriptionPaymentDto.setTotalAmount(subscription.getAmount().amount());
//        PartnerInfoDto partnerInfoDto = PartnerInfoDto.builder().id(UUID.randomUUID()).name(partnerName)
//                .avatarPath(avatarPath)
//                .build();
//        subscriptionPaymentDto.setPartnerType(subscription.getPartnerType());
//        subscriptionPaymentDto.setPartnerInfoDto(partnerInfoDto);
        return null;
    }
    @Override
    @Transactional
            public List<String> getAvailableMonthsByPartner(UUID partnerId) {
            return partnerCommissionsRepository.findDistinctMonthsByPartner(partnerId);
        }

    @Override
    @Transactional
    public List<Integer> getAvailableYearsByPartner(UUID partnerId) {
        return partnerCommissionsRepository.findDistinctYearsByPartner(partnerId);
    }
    @Override
    @Transactional
        public List<String> getAvailableMonthsByOrganization(UUID organizationId) {
            return partnerCommissionsRepository.findDistinctMonthsByOrganization(organizationId);
        }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public PaymentResponse processPayment(PaymentRequest request, MultipartFile document, PaymentType type) throws BadRequestException {
        PaymentProcessor processor = processors.get(request.paymentMethod().toLowerCase());
        if (processor == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + request.paymentMethod());
        }
        return processor.process(request, document, type);
    }

    // convert to dto
       // case of partner with subentities.

    @Override
    @Transactional
    public Page<CommissionPaymentDto> convertCommissionToDto(Page<PartnerCommissions> payments) {
        Page<CommissionPaymentDto> paymentDtos = payments.map(payment -> {
            if (payment.getPartnerInfo().type().equals(PartnerType.SUB_ENTITY)) {
                SubEntity subEntity = this.subEntityService.getEntityById(payment.getPartnerInfo().id());
                payment.setPartner(subEntity);
            } else {
                OrganizationEntity organizationEntity = this.organizationEntityService.findById(payment.getPartnerInfo().id());
                payment.setPartner(organizationEntity);
            }
            CommissionPaymentDto dto = null;
            if (payment.getPartnerInfo().type().equals(PartnerType.PARTNER_SB)) {
                List<CommissionPaymentDto> childs = payment.getSubEntityCommissions().stream().map((PartnerCommissions p) -> {
                    if (p.getPartnerInfo().type().equals(PartnerType.SUB_ENTITY)) {
                        SubEntity subEntity = this.subEntityService.getEntityById(p.getPartnerInfo().id());
                        p.setPartner(subEntity);
                    } else {
                        OrganizationEntity organizationEntity = this.organizationEntityService.findById(p.getPartnerInfo().id());
                        p.setPartner(organizationEntity);
                    }
                    return  this.modelMapper.map(p, CommissionPaymentDto.class);
                }).collect(Collectors.toList());

                dto = new CommissionPaymentDto();
                Currency defaultCurrency = Currency.getInstance("MAD");
                Price totalCommission = !childs.isEmpty()
                        ? childs.stream()
                        .map(p -> p.getFoodealsCommission())
                        .reduce(Price.ZERO(defaultCurrency), (price1, price2) -> Price.add((Price)price1, (Price)price2))
                        : Price.ZERO(defaultCurrency);

                Price toPayTotal = !childs.isEmpty()
                        ? childs.stream()
                        .map(p -> p.getToPay())
                        .reduce(Price.ZERO(defaultCurrency), (price1, price2) -> Price.add((Price)price1, (Price)price2))
                        : Price.ZERO(defaultCurrency);

                Price toReceiveTotal = !childs.isEmpty()
                        ? childs.stream()
                        .map(p -> p.getToReceive())
                        .reduce(Price.ZERO(defaultCurrency), (price1, price2) -> Price.add((Price)price1, (Price)price2))
                        : Price.ZERO(defaultCurrency);

                BigDecimal differnce = toPayTotal.amount().subtract(toReceiveTotal.amount());
                Price toPay = differnce.compareTo(BigDecimal.ZERO) < 0 ?
                        Price.ZERO(defaultCurrency) : new Price(differnce, defaultCurrency);
                Price toReceive = differnce.compareTo(BigDecimal.ZERO) < 0 ?
                        new Price(differnce.abs(), defaultCurrency) : Price.ZERO(defaultCurrency);

                Price totalAmount = !childs.isEmpty()
                        ? childs.stream()
                        .map(p -> p.getTotalAmount())
                        .reduce(Price.ZERO(defaultCurrency), (price1, price2) -> Price.add((Price)price1, (Price)price2))
                        : Price.ZERO(defaultCurrency);
                dto.setId(payment.getId());
                dto.setEntityId(payment.getPartnerInfo().id());
                PartnerInfoDto partnerInfoDto = new PartnerInfoDto(payment.getPartnerInfo().id(), payment.getPartner().getName(), payment.getPartner().getAvatarPath(), payment.getPartner().getCity());
                dto.setPartnerInfoDto(partnerInfoDto);
                dto.setPartnerType(payment.getPartnerInfo().type());
                dto.setOrganizationId(payment.getPartnerInfo().id());
                SimpleDateFormat formatter = new SimpleDateFormat("M/yyyy");
                dto.setDate(formatter.format(payment.getDate()));
                dto.setTotalAmount(totalAmount);
                dto.setFoodealsCommission(totalCommission);
                dto.setToPay(toPay);
                dto.setToReceive(toReceive);
                dto.setPaymentStatus(payment.getPaymentStatus());
                dto.setPayable(payment.getPaymentResponsibility().equals(PaymentResponsibility.PAYED_BY_PARTNER));
                dto.setCommissionPayedBySubEntities(payment.getPaymentResponsibility().equals(PaymentResponsibility.PAYED_BY_SUB_ENTITIES));
            } else {
                dto = this.modelMapper.map(payment, CommissionPaymentDto.class);
            }
            return dto;
        });
        return paymentDtos;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void receiveSubscription(ReceiveDto receiveDto) {
        Deadlines deadlines = this.deadlinesService.findById(receiveDto.id());

        if (deadlines == null) {
            throw  new ResourceNotFoundException("deadline not found with id " + receiveDto.id().toString());
        }

        if (deadlines.getSubscription().getPartner().type().equals(PartnerType.PARTNER_SB)) {
            Set<Deadlines> childs = deadlines.getSubEntityDeadlines().stream().map(c -> {
                c.setStatus(DeadlineStatus.CONFIRMED_BY_FOODEALS);
                Subscription subscription = c.getSubscription();

                subscription.setSubscriptionStatus(SubscriptionStatus.VALID);
                subscription.getDeadlines().forEach(d -> {
                    if (c.getId() != d.getId() && !d.getStatus().equals(DeadlineStatus.CONFIRMED_BY_FOODEALS)) {
                        subscription.setSubscriptionStatus(SubscriptionStatus.IN_PROGRESS);
                    }
                });
                this.subscriptionService.save(subscription);
                return  c;
            }).collect(Collectors.toSet());
            this.deadlinesService.saveAll(new ArrayList<>(childs));
        } else if (deadlines.getSubscription().getPartner().type().equals(PartnerType.SUB_ENTITY)) {
            Deadlines parent = deadlines.getParentPartner();
            parent.setStatus(DeadlineStatus.CONFIRMED_BY_FOODEALS);
            parent.getSubEntityDeadlines().forEach(c -> {
                if (c.getId() != deadlines.getId() && !c.getStatus().equals(DeadlineStatus.CONFIRMED_BY_FOODEALS)) {
                    parent.setStatus(DeadlineStatus.IN_VALID);
                }
            });
            Subscription subscription = parent.getSubscription();

            subscription.setSubscriptionStatus(SubscriptionStatus.VALID);
            subscription.getDeadlines().forEach(d -> {
                if (parent.getId() != d.getId() && !d.getStatus().equals(DeadlineStatus.CONFIRMED_BY_FOODEALS)) {
                    subscription.setSubscriptionStatus(SubscriptionStatus.IN_PROGRESS);
                }
            });
            this.subscriptionService.save(subscription);
            this.deadlinesService.save(parent);
        }
        deadlines.setStatus(DeadlineStatus.CONFIRMED_BY_FOODEALS);

        Subscription subscription = deadlines.getSubscription();

        subscription.setSubscriptionStatus(SubscriptionStatus.VALID);
        subscription.getDeadlines().forEach(d -> {
            if (deadlines.getId() != d.getId() && !d.getStatus().equals(DeadlineStatus.CONFIRMED_BY_FOODEALS)) {
                subscription.setSubscriptionStatus(SubscriptionStatus.IN_PROGRESS);
            }
        });
        this.subscriptionService.save(subscription);
        this.deadlinesService.save(deadlines);
    }


    @Override
    public PaymentFormData getFormData(UUID id, PaymentType type) {
        return switch (type) {
            case COMMISSION -> this.getCommissionFormData(id);
            case SUBSCRIPTION -> this.getSubscriptionFormData(id);
            default -> null;
        };
    }

    @Override
    @Transactional
    public PaymentFormData getCommissionFormData(UUID id) {
        PartnerCommissions partnerCommission = this.partnerCommissionsRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("commission not found with id " + id.toString()));
        PartnerInfoDto partnerInfoDto = null;
        if (partnerCommission.getPartnerInfo().type().equals(PartnerType.SUB_ENTITY)) {
            SubEntity subEntity = this.subEntityService.getEntityById(partnerCommission.getPartnerInfo().id());
            partnerInfoDto = new PartnerInfoDto(subEntity.getId(), subEntity.getName(), subEntity.getAvatarPath(), subEntity.getCity());
        } else {
            OrganizationEntity partner = this.organizationEntityService.findById(partnerCommission.getPartnerInfo().id());
            partnerInfoDto = new PartnerInfoDto(partner.getId(), partner.getName(), partner.getAvatarPath(), partner.getCity());
        }
        User emitter = partnerCommission.getEmitter();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/y");
        PaymentMethod paymentMethod = partnerCommission.getPaymentMethod();
        String date = formatter.format(paymentMethod.getOperationDate());
        String document = paymentMethod.getDocumentPath();
        Price price = paymentMethod.getPrice();
        return new PaymentFormData(paymentMethod.getType(), partnerInfoDto, emitter.getName(), price, document , date);
    }

    @Override
    public PaymentFormData getSubscriptionFormData(UUID id) {
        return null;
    }

    @Override
    @Transactional
    public MonthlyOperationsDto monthlyOperations(UUID id, int year, int month, Pageable page, PartnerType type) {
        return switch (type) {
            case PARTNER_SB, NORMAL_PARTNER, SUB_ENTITY -> partner(id, year, month, page);
            case DELIVERY_PARTNER -> deliveryPartner(id, year, month, page);
            default -> null;
        };
    }

    @Transactional
    private MonthlyOperationsDto deliveryPartner(UUID id, int year, int month, Pageable page) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
    Date date = calendar.getTime();

    PartnerCommissions commissions = partnerCommissionsRepository.findCommissionsByDateAndPartnerInfoId(year, month, id);
    List<Order> orders = orderService.findOrdersByOrganizationAndDeliveryStatusAndCriteria(id, DeliveryStatus.DELIVERED, date, OrderStatus.COMPLETED, TransactionStatus.COMPLETED);
    PaymentStatistics statistics = getPaymentStatistics(Collections.singletonList(commissions));

    System.out.println(orders);

    PartnerInfoDto partnerInfoDto = getPartnerInfoDto(commissions);
    Commission commission = commissionService.getCommissionByPartnerId(commissions.getPartnerInfo().organizationId());

    Map<UUID, List<Order>> ordersByDelivery = orders.stream()
            .filter(order -> order.getDelivery() != null)
            .collect(Collectors.groupingBy(order -> order.getDelivery().getId()));

    System.out.println(orders);


    List<DeliveryOperationDto> operationsDtos = new ArrayList<>();

        ordersByDelivery.forEach((deliveryId, deliveryOrders) -> {
        Order firstOrder = deliveryOrders.get(0);

        OrderDeliveryType type = deliveryOrders.size() > 1 ? OrderDeliveryType.MULTIPLE : OrderDeliveryType.SINGLE;

        Price totalCashAmount = Price.ZERO(Currency.getInstance("MAD"));
        Price totalCardAmount = Price.ZERO(Currency.getInstance("MAD"));
        Price totalCashCommission = Price.ZERO(Currency.getInstance("MAD"));
        Price totalCardCommission = Price.ZERO(Currency.getInstance("MAD"));
        int quantity = 0;

        UUID publisherOrganizationId = firstOrder.getOffer().getPublisherInfo().type().equals(PartnerType.SUB_ENTITY) ? this.subEntityService.getEntityById(firstOrder.getOffer().getPublisherInfo().id()).getOrganizationEntity().getId() : firstOrder.getOffer().getPublisherInfo().id();


        for (Order order : deliveryOrders) {
            Transaction transaction = order.getTransaction();
            boolean isCash = transaction.getType().equals(TransactionType.CASH);

            Price amount = transaction.getPrice();
            totalCashAmount = Price.add(totalCashAmount, isCash ? amount : Price.ZERO(Currency.getInstance("MAD")));
            totalCardAmount = Price.add(totalCardAmount, isCash ? Price.ZERO(Currency.getInstance("MAD")) : amount);

            quantity += order.getQuantity();
        }
            SimpleUserDto deliveryBoy = new SimpleUserDto(
                    firstOrder.getDelivery().getDeliveryBoy().getId(),
                    firstOrder.getDelivery().getDeliveryBoy().getName(),
                    firstOrder.getDelivery().getDeliveryBoy().getAvatarPath()
            );

            ProductInfo productInfo = new ProductInfo(firstOrder.getOffer().getTitle(), firstOrder.getOffer().getImagePath());
            DeliveryOperationDto operationsDto = new DeliveryOperationDto(
                    type,
                    productInfo,
                    firstOrder.getId(),
                    type.equals(OrderDeliveryType.MULTIPLE) ? Price.ZERO(Currency.getInstance("MAD")) : firstOrder.getOffer().getSalePrice(),
                    quantity,
                    totalCashAmount,
                    totalCardAmount,
                    new Price(BigDecimal.valueOf(commission.getDeliveryCommission()), Currency.getInstance("MAD")),
                    deliveryBoy
            );
        operationsDtos.add(operationsDto);
    });
        operationsDtos.sort(Comparator.comparing(dto -> orders.stream()
            .filter(order -> order.getId().equals(dto.id()))
            .findFirst()
                .map(Order::getCreatedAt)
                .orElse(null)));

    int totalElements = operationsDtos.size();
    int start = (int) page.getOffset();
    int end = Math.min(start + page.getPageSize(), totalElements);
    List<DeliveryOperationDto> paginatedOperationsDtos = operationsDtos.subList(start, end);

    Page<DeliveryOperationDto> dtoPage = new PageImpl<>(paginatedOperationsDtos, page, totalElements);


    DeliveryPaymentDto dto = this.convertToDeliveryCommission(commissions);
     PaymentsDetailsDto detailsDto = new PaymentsDetailsDto(
                commissions.getId(),
                true,
                dto.getStatus(),
                dto.getToPay().amount().compareTo(dto.getToReceive().amount()) > 0 ?
                        PaymentDirection.FOODEALS_TO_PARTENER :
                        PaymentDirection.PARTNER_TO_FOODEALS
        );
        return new MonthlyOperationsDto(partnerInfoDto, statistics, dtoPage, detailsDto);
    }

    private MonthlyOperationsDto partner(UUID id, int year, int month, Pageable page) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        Date date = calendar.getTime();

        PartnerCommissions commissions = partnerCommissionsRepository.findCommissionsByDateAndPartnerInfoId(year, month, id);
        List<Order> orders = orderService.findByOfferPublisherInfoIdAndDateAndStatus(id, date, OrderStatus.COMPLETED, TransactionStatus.COMPLETED);
        PaymentStatistics statistics = getPaymentStatistics(Collections.singletonList(commissions));

        PartnerInfoDto partnerInfoDto = getPartnerInfoDto(commissions);
        Commission commission = commissionService.getCommissionByPartnerId(commissions.getPartnerInfo().organizationId());

        System.out.println(orders);

        Map<UUID, List<Order>> ordersByDelivery = orders.stream()
                .filter(order -> order.getDelivery() != null)
                .collect(Collectors.groupingBy(order -> order.getDelivery().getId()));

        List<OperationsDto> operationsDtos = new ArrayList<>();

        ordersByDelivery.forEach((deliveryId, deliveryOrders) -> {
            Order firstOrder = deliveryOrders.get(0);
            OrderDeliveryType type = deliveryOrders.size() > 1 ? OrderDeliveryType.MULTIPLE : OrderDeliveryType.SINGLE;

            Price totalCashAmount = Price.ZERO(Currency.getInstance("MAD"));
            Price totalCardAmount = Price.ZERO(Currency.getInstance("MAD"));
            Price totalCashCommission = Price.ZERO(Currency.getInstance("MAD"));
            Price totalCardCommission = Price.ZERO(Currency.getInstance("MAD"));
            int quantity = 0;

            for (Order order : deliveryOrders) {
                Transaction transaction = order.getTransaction();
                boolean isCash = transaction.getType().equals(TransactionType.CASH);

                Price amount = transaction.getPrice();
                totalCashAmount = Price.add(totalCashAmount, isCash ? amount : Price.ZERO(Currency.getInstance("MAD")));
                totalCardAmount = Price.add(totalCardAmount, isCash ? Price.ZERO(Currency.getInstance("MAD")) : amount);
                BigDecimal commissionRate = BigDecimal.valueOf(isCash ? commission.getCash() : commission.getCard()).divide(BigDecimal.valueOf(100));
                Price commissionAmount = new Price(commissionRate.multiply(amount.amount()), Currency.getInstance("MAD"));

                totalCashCommission = Price.add(totalCashCommission, isCash ? commissionAmount : Price.ZERO(Currency.getInstance("MAD")));
                totalCardCommission = Price.add(totalCardCommission, isCash ? Price.ZERO(Currency.getInstance("MAD")) : commissionAmount);

                quantity += order.getQuantity();
            }

            ProductInfo productInfo = new ProductInfo(firstOrder.getOffer().getTitle(), firstOrder.getOffer().getImagePath());
            OperationsDto operationsDto = new OperationsDto(type, productInfo, firstOrder.getId(),
                    type.equals(OrderDeliveryType.MULTIPLE) ? Price.ZERO(Currency.getInstance("MAD")) : firstOrder.getOffer().getSalePrice(),
                    quantity, totalCashAmount, totalCashCommission, totalCardAmount, totalCardCommission);
            operationsDtos.add(operationsDto);
        });

        orders.stream()
                .filter(order -> order.getDelivery() == null)
                .forEach(order -> {
                    Transaction transaction = order.getTransaction();
                    boolean isCash = transaction.getType().equals(TransactionType.CASH);

                    Price amount = transaction.getPrice();
                    Price cashAmount = isCash ? amount : Price.ZERO(Currency.getInstance("MAD"));
                    Price cardAmount = isCash ? Price.ZERO(Currency.getInstance("MAD")) : amount;

                    BigDecimal commissionRate = BigDecimal.valueOf(isCash ? commission.getCash() : commission.getCard()).divide(BigDecimal.valueOf(100));
                    Price commissionAmount = new Price(commissionRate.multiply(amount.amount()), Currency.getInstance("MAD"));

                    Price cashCommission = isCash ? commissionAmount : Price.ZERO(Currency.getInstance("MAD"));
                    Price cardCommission = isCash ? Price.ZERO(Currency.getInstance("MAD")) : commissionAmount;

                    ProductInfo productInfo = new ProductInfo(order.getOffer().getTitle(), order.getOffer().getImagePath());
                    OperationsDto operationsDto = new OperationsDto(
                            OrderDeliveryType.SINGLE,
                            productInfo,
                            order.getId(),
                            order.getOffer().getSalePrice(),
                            order.getQuantity(),
                            cashAmount,
                            cashCommission,
                            cardAmount,
                            cardCommission
                    );
                    operationsDtos.add(operationsDto);
                });

        operationsDtos.sort(Comparator.comparing(dto -> orders.stream()
                .filter(order -> order.getId().equals(dto.id()))
                .findFirst()
                .map(Order::getCreatedAt)
                .orElse(null)));

        int totalElements = operationsDtos.size();
        int start = (int) page.getOffset();
        int end = Math.min(start + page.getPageSize(), totalElements);
        List<OperationsDto> paginatedOperationsDtos = operationsDtos.subList(start, end);

        Page<OperationsDto> dtoPage = new PageImpl<>(paginatedOperationsDtos, page, totalElements);

        CommissionPaymentDto dto = this.modelMapper.map(commissions, CommissionPaymentDto.class);
        PaymentsDetailsDto detailsDto = new PaymentsDetailsDto(dto.getId(), dto.isPayable(), dto.getPaymentStatus(), dto.getToPay().amount().compareTo(dto.getToReceive().amount()) > 0 ? PaymentDirection.FOODEALS_TO_PARTENER : PaymentDirection.PARTNER_TO_FOODEALS);
        return new MonthlyOperationsDto(partnerInfoDto, statistics, dtoPage, detailsDto);

    }

    private PartnerInfoDto getPartnerInfoDto(PartnerCommissions commissions) {
        if (commissions.getPartnerInfo().type().equals(PartnerType.SUB_ENTITY)) {
            SubEntity subEntity = subEntityService.getEntityById(commissions.getPartnerInfo().id());
            return new PartnerInfoDto(subEntity.getId(), subEntity.getName(), subEntity.getAvatarPath(), subEntity.getCity());
        } else {
            OrganizationEntity partner = organizationEntityService.findById(commissions.getPartnerInfo().id());
            return new PartnerInfoDto(partner.getId(), partner.getName(), partner.getAvatarPath(), partner.getCity());
        }
    }

//    private MonthlyOperationsDto partner(UUID id, int year, int month, Pageable page) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month - 1, 1);
//        Date date = calendar.getTime();
//        PartnerCommissions commissions = this.partnerCommissionsRepository.findCommissionsByDateAndPartnerInfoId(year, month, id);
//        List<Order> orders = this.orderService.findByOfferPublisherInfoIdAndDateAndStatus(id, date, OrderStatus.COMPLETED, TransactionStatus.COMPLETED);
//        PaymentStatistics statistics = this.getPaymentStatistics(new ArrayList<>(List.of(commissions)));
//        PartnerInfoDto  partnerInfoDto;
//        if (commissions.getPartnerInfo().type().equals(PartnerType.SUB_ENTITY)) {
//            SubEntity subEntity = this.subEntityService.getEntityById(commissions.getPartnerInfo().id());
//            partnerInfoDto = new PartnerInfoDto(subEntity.getId(), subEntity.getName(), subEntity.getAvatarPath());
//        } else {
//            OrganizationEntity partner = this.organizationEntityService.findById(commissions.getPartnerInfo().id());
//            partnerInfoDto = new PartnerInfoDto(partner.getId(), partner.getName(), partner.getAvatarPath());
//        }
//
//        Page<OperationsDto> operationsDtos = orders.map(o -> {
//            Transaction transaction = o.getTransaction();
//            UUID organizationId = o.getOffer().getPublisherInfo().type().equals(PublisherType.PARTNER_SB) ? this.subEntityService.getEntityById(id).getOrganizationEntity().getId() : id;
//            Commission commission = this.commissionService.getCommissionByPartnerId(organizationId);
//            Price cashAmount = transaction.getType().equals(TransactionType.CASH) ? transaction.getPrice() : Price.ZERO(Currency.getInstance("MAD"));
//            Price cardAmount = transaction.getType().equals(TransactionType.CASH) ? Price.ZERO(Currency.getInstance("MAD")) : transaction.getPrice();
//            Price cashCommission = transaction.getType().equals(TransactionType.CASH)
//                    ? new Price(BigDecimal.valueOf(commission.getCash()).divide(BigDecimal.valueOf(100)).multiply(transaction.getPrice().amount()), Currency.getInstance("MAD"))
//                    : Price.ZERO(Currency.getInstance("MAD"));
//            Price cardCommission = transaction.getType().equals(TransactionType.CASH)
//                    ? Price.ZERO(Currency.getInstance("MAD"))
//                    : new Price(BigDecimal.valueOf(commission.getCard()).divide(BigDecimal.valueOf(100)).multiply(transaction.getPrice().amount()), Currency.getInstance("MAD"));
//            Price amount = transaction.getPrice();
//            ProductInfo productInfo = new ProductInfo(o.getOffer().getTitle(), o.getOffer().getImagePath());
//            return new OperationsDto(productInfo, o.getId(), amount, o.getQuantity(), cashAmount, cashCommission, cardAmount, cardCommission);
//        });
//        CommissionPaymentDto dto = this.modelMapper.map(commissions, CommissionPaymentDto.class);
//        PaymentsDetailsDto detailsDto = new PaymentsDetailsDto(dto.getId(), dto.isPayable(), dto.getPaymentStatus(), dto.getToPay().amount().compareTo(dto.getToReceive().amount()) > 0 ? PaymentDirection.FOODEALS_TO_PARTENER : PaymentDirection.PARTNER_TO_FOODEALS);
//        return new MonthlyOperationsDto(partnerInfoDto, statistics, operationsDtos, detailsDto);
//    }

    @Override
    public List<PartnerCommissions> getCommissionPaymentsByOrganizationId(UUID id, int year, int month) {
        return this.partnerCommissionsRepository.findCommissionsByDateAndOrganizationAndPartnerInfoTypeNot(year, month, id, PartnerType.DELIVERY_PARTNER);
    }

    @Override
    @Transactional
    public CommissionDto getCommissionResponse(List<PartnerCommissions> commissions, Pageable page) {
        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), commissions.size());
        List<PartnerCommissions> pageContent = commissions.subList(start, end);
        Page<PartnerCommissions> commissionsPage = new PageImpl<>(pageContent, page, pageContent.size());
        Page<CommissionPaymentDto> commissionsDtos = this.convertCommissionToDto(commissionsPage);
        PaymentStatistics statistics = this.getPaymentStatistics(commissions);
        return new CommissionDto(statistics, commissionsDtos);
    }

    @Override
    @Transactional
    public PaymentStatistics getPaymentStatistics(List<PartnerCommissions> commissions) {
        AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> totalCommission = new AtomicReference<>(BigDecimal.ZERO);

        commissions.stream().forEach(partnerCommissions -> {
            if (!partnerCommissions.getPartnerInfo().type().equals(PartnerType.PARTNER_SB)) {
                if (partnerCommissions.getPartnerInfo().type().equals(PartnerType.SUB_ENTITY)) {
                    partnerCommissions.setPartner(this.subEntityService.getEntityById(partnerCommissions.getPartnerInfo().id()));
                } else {
                    partnerCommissions.setPartner(this.organizationEntityService.findById(partnerCommissions.getPartnerInfo().id()));
                }
                UUID organizationId = !partnerCommissions.getPartner().getPartnerType().equals(PartnerType.SUB_ENTITY) ? partnerCommissions.getPartner().getId() : ((SubEntity) partnerCommissions.getPartner()).getOrganizationEntity().getId();
                Commission commission = this.commissionService.getCommissionByPartnerId(organizationId);
                if (partnerCommissions.getPartnerInfo().type().equals(PartnerType.DELIVERY_PARTNER)) {
                    long orderCount = this.deliveryService.countDeliveriesByDeliveryPartnerAndDate(partnerCommissions.getPartner().getId(), partnerCommissions.getDate());
                    total.updateAndGet(current -> current.add(new BigDecimal(orderCount * commission.getDeliveryAmount())));
                    totalCommission.updateAndGet(current -> current.add(new BigDecimal(orderCount * commission.getDeliveryCommission())));
                } else {
                    List<Order> orderList = this.orderService.findByOfferPublisherInfoIdAndDateAndStatus(partnerCommissions.getPartner().getId(), partnerCommissions.getDate(), OrderStatus.COMPLETED, TransactionStatus.COMPLETED);
                    List<Transaction> transactions = orderList.stream()
                            .map(order -> order.getTransaction())
                            .collect(Collectors.toList());
                    BigDecimal paymentsWithCash = transactions.stream().filter(transaction -> transaction.getType().equals(TransactionType.CASH))
                            .map(transaction -> transaction.getPrice().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal paymentsWithCard = transactions.stream().filter(transaction -> transaction.getType().equals(TransactionType.CARD))
                            .map(transaction -> transaction.getPrice().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
                    Double commissionTotal = ((Double) (commission.getCard().doubleValue() / 100)) * paymentsWithCard.doubleValue() + ((Double) (commission.getCash().doubleValue() / 100)) * paymentsWithCash.doubleValue();
                    total.updateAndGet(current -> current.add(paymentsWithCash.add(paymentsWithCard)));
                    totalCommission.updateAndGet(current -> current.add(new BigDecimal(commissionTotal)));
                }
            }
        });
        return new PaymentStatistics(new Price(total.get().setScale(3, RoundingMode.HALF_UP), Currency.getInstance("MAD")), new Price(totalCommission.get().setScale(3, RoundingMode.HALF_UP),Currency.getInstance("MAD")));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public PaymentResponse receive(ReceiveDto receiveDto, PaymentType type) throws Exception {
        try {
            if (type.equals(PaymentType.COMMISSION)) {
                this.receiveCommission(receiveDto);
            } else {
                this.receiveSubscription(receiveDto);
            }
            return new PaymentResponse("payment validated successfully");
        } catch (Exception e) {
            throw new Exception("failed to process payment:");
        }

    }

    @Override
    @Transactional
    public void receiveCommission(ReceiveDto receiveDto) throws BadRequestException {
        PartnerCommissions commission = this.partnerCommissionsRepository.findById(receiveDto.id()).orElseThrow(() -> new ResourceNotFoundException("commission not found with id " + receiveDto.id().toString()));
        if (commission.getPartnerInfo().type().equals(PartnerType.PARTNER_SB)) {
            Set<PartnerCommissions> childs = commission.getSubEntityCommissions().stream().map(c -> {
                c.setPaymentStatus(PaymentStatus.VALIDATED_BY_BOTH);
                return  c;
            }).collect(Collectors.toSet());
            this.partnerCommissionsRepository.saveAll(childs);
        } else if (commission.getPartnerInfo().type().equals(PartnerType.SUB_ENTITY)) {
            PartnerCommissions parent = commission.getParentPartner();
            parent.setPaymentStatus(PaymentStatus.VALIDATED_BY_BOTH);
            parent.getSubEntityCommissions().forEach(c -> {
                if (c.getId() != commission.getId() && !c.getPaymentStatus().equals(PaymentStatus.VALIDATED_BY_BOTH)) {
                    parent.setPaymentStatus(PaymentStatus.IN_VALID);
                }
            });
            this.partnerCommissionsRepository.save(parent);
        }
        commission.setPaymentStatus(PaymentStatus.VALIDATED_BY_BOTH);
        this.partnerCommissionsRepository.save(commission);
    }

    @Override
    @Transactional
    public SubscriptionPaymentDto getSubscriptionResponse(int year, Pageable page, UUID id) {
        // 1. Fetch subscriptions for the given year
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        // 2. Get all subscriptions for the year
        List<Subscription> subscriptions = subscriptionService.findByStartDateBetweenAndSubscriptionStatusNot(startOfYear, endOfYear, SubscriptionStatus.NOT_STARTED, id);

        // 3. Calculate total statistics
        Price totalSubscriptions = calculateTotalSubscriptions(subscriptions);
        Price totalDeadlines = calculateTotalDeadlines(subscriptions);
        SubscriptionStatistics statistics = new SubscriptionStatistics(totalSubscriptions, totalDeadlines);

        // 4. Group subscriptions by partner
        Map<UUID, List<Subscription>> subscriptionsByPartner = subscriptions.stream()
                .collect(Collectors.groupingBy(s -> s.getPartner().id()));

        // 5. Create SubscriptionsListDto for each partner
        List<SubscriptionsListDto> subscriptionsList = subscriptionsByPartner.entrySet().stream()
                .map(entry -> createSubscriptionsListDto(entry.getKey(), entry.getValue(), year))
                .collect(Collectors.toList());

        // 6. Create final page
        Page<SubscriptionsListDto> subscriptionsPage = new PageImpl<>(
                subscriptionsList,
                page,
                subscriptions.size()
        );

        return new SubscriptionPaymentDto(statistics, subscriptionsPage);
    }

    @Override
    @Transactional
    public List<String> getAvailableMonths() {
            return partnerCommissionsRepository.findDistinctMonths();
        }

        @Override
        @Transactional
        public Page<PartnerInfoDto> searchPartnersByNameCommission(String name, List<PartnerType> types, Pageable pageable, UUID id) {
            if (id != null) {
                // If an ID is provided, fetch the specific partner
                Optional<PartnerCommissions> partnerOptional = partnerCommissionsRepository.findCommissionsByPartnerInfoId(id);
                if (partnerOptional.isPresent()) {
                    PartnerCommissions partner = partnerOptional.get();
                    PartnerInfoDto partnerInfoDto = mapCommissionToPartnerInfoDto(partner);
                    return new PageImpl<>(Collections.singletonList(partnerInfoDto), pageable, 1);
                } else {
                    return new PageImpl<>(Collections.emptyList(), pageable, 0); // No partner found
                }
            } else {
                // If no ID is provided, perform the search by name and types
                return partnerCommissionsRepository.findByPartnerNameAndTypeIn(name, types, pageable).map(this::mapCommissionToPartnerInfoDto);
            }
        }

    @Override
    @Transactional
    public Page<PartnerInfoDto> searchPartnersByNameSubscription(String name, List<PartnerType> types, Pageable pageable, UUID id) {
        if (id != null) {
            // If an ID is provided, fetch the specific partner
            Optional<Subscription> partnerOptional = subscriptionService.findSubscriptionByPartnerInfoId(id, types);
            if (partnerOptional.isPresent()) {
                Subscription partner = partnerOptional.get();
                PartnerInfoDto partnerInfoDto = mapSubscriptionToPartnerInfoDto(partner);
                return new PageImpl<>(Collections.singletonList(partnerInfoDto), pageable, 1);
            } else {
                return new PageImpl<>(Collections.emptyList(), pageable, 0); // No partner found
            }
        } else {
            // If no ID is provided, perform the search by name and types
            return subscriptionService.findByPartnerNameAndTypeIn(name, types, pageable).map(this::mapSubscriptionToPartnerInfoDto);
        }
    }

        private PartnerInfoDto mapCommissionToPartnerInfoDto(PartnerCommissions partner) {
            if (partner.getPartnerInfo().type().equals(PartnerType.SUB_ENTITY)) {
                SubEntity subEntity = this.subEntityService.getEntityById(partner.getPartnerInfo().id());
                return new PartnerInfoDto(subEntity.getId(), subEntity.getName(), subEntity.getAvatarPath(), subEntity.getCity());
            } else {
                OrganizationEntity organizationEntity = this.organizationEntityService.findById(partner.getPartnerInfo().id());
                return new PartnerInfoDto(organizationEntity.getId(), organizationEntity.getName(), organizationEntity.getAvatarPath(), organizationEntity.getCity());
            }
        }

    private PartnerInfoDto mapSubscriptionToPartnerInfoDto(Subscription partner) {
        if (partner.getPartner().type().equals(PartnerType.SUB_ENTITY)) {
            SubEntity subEntity = this.subEntityService.getEntityById(partner.getPartner().id());
            return new PartnerInfoDto(subEntity.getId(), subEntity.getName(), subEntity.getAvatarPath(), subEntity.getCity());
        } else {
            OrganizationEntity organizationEntity = this.organizationEntityService.findById(partner.getPartner().id());
            return new PartnerInfoDto(organizationEntity.getId(), organizationEntity.getName(), organizationEntity.getAvatarPath(), organizationEntity.getCity());
        }
    }



    @Override
    @Transactional
    public List<Integer> getAvailableYears() {
        return subscriptionService.findDistinctYears();
    }

    @Override
    @Transactional
    public SubscriptionDetails getSubscriptionDetails(int year, UUID id) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        List<Subscription> subscriptions = subscriptionService.findByStartDateBetweenAndSubscriptionStatusNotAndId(startOfYear, endOfYear, SubscriptionStatus.NOT_STARTED, id);

        Subscription firstSubscription = subscriptions.get(0);
        PartnerI partner = !firstSubscription.getPartner().type().equals(PartnerType.SUB_ENTITY) ? this.organizationEntityService.findById(firstSubscription.getPartner().id()) : this.subEntityService.getEntityById(firstSubscription.getPartner().id());
        subscriptions.forEach(s -> {
            s.setPartnerI(partner);
        });
        List<SubscriptionsDto> subscriptionsDto =  subscriptions.stream()
                .map(s -> {
                    return this.mapToSubscriptionsDto(s);
                })
                .collect(Collectors.toList());
        PartnerInfoDto partnerInfoDto = new PartnerInfoDto(partner.getId(), partner.getName(), partner.getAvatarPath(), partner.getCity());

        return new SubscriptionDetails(partnerInfoDto, subscriptionsDto);
    }

    @Override
    @Transactional
    public DeliveryPaymentResponse getDeliveryPayments(int year, Pageable page, UUID id) {
        List<PartnerCommissions> commissions = this.partnerCommissionsRepository.findCommissionsByDateAndOrganization(year, id);
        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), commissions.size());
        List<PartnerCommissions> pageContent = commissions.subList(start, end);
        Page<PartnerCommissions> commissionsPage = new PageImpl<>(pageContent, page, pageContent.size());
        Page<DeliveryPaymentDto> commissionsDtos = commissionsPage.map(this::convertToDeliveryCommission);
        PaymentStatistics statistics = this.getPaymentStatistics(commissions);
        return new DeliveryPaymentResponse(statistics, commissionsDtos);
        }

    @Override
    @Transactional
    public DeliveryPaymentDto convertToDeliveryCommission(PartnerCommissions commission) {
        Commission commissionDetails = this.commissionService.getCommissionByPartnerId(commission.getPartnerInfo().id());
        Currency mad = Currency.getInstance("MAD");
        SimpleDateFormat formatter = new SimpleDateFormat("M/yyyy");

// Fetch orders based on the single commission details
        List<Order> orderList = this.orderService.findOrdersByOrganizationAndDeliveryStatusAndCriteria(
                commission.getPartnerInfo().id(),
                DeliveryStatus.DELIVERED,
                commission.getDate(),
                OrderStatus.COMPLETED,
                TransactionStatus.COMPLETED
        );

// Count deliveries by delivery partner and date
        long orderCount = this.deliveryService.countDeliveriesByDeliveryPartnerAndDate(commission.getPartnerInfo().id(), commission.getDate());

// Group orders by delivery ID (or any unique identifier for the delivery)
        Map<UUID, List<Order>> groupedOrders = orderList.stream()
                .collect(Collectors.groupingBy(order -> order.getDelivery().getId()));

// Process only the first order for each delivery
        List<Transaction> transactions = groupedOrders.values().stream()
                .map(orders -> orders.get(0).getTransaction()) // Get the transaction of the first order for each delivery
                .collect(Collectors.toList());

        Float paymentsWithCash = transactions.stream()
                .filter(transaction -> transaction.getType().equals(TransactionType.CASH))
                .count() * commissionDetails.getDeliveryAmount();

        Float paymentsWithCard = transactions.stream()
                .filter(transaction -> transaction.getType().equals(TransactionType.CARD))
                .count() * commissionDetails.getDeliveryAmount();

        Double commissionTotal = (transactions.stream().filter(transaction -> transaction.getType().equals(TransactionType.CARD)).count() +
                transactions.stream().filter(transaction -> transaction.getType().equals(TransactionType.CASH)).count()) *
                Double.valueOf(commissionDetails.getDeliveryCommission());

        Double difference = paymentsWithCard - commissionTotal;
        Double toPay = Math.max(difference, 0);
        Double toReceive = difference < 0 ? Math.abs(difference) : 0;

        return new DeliveryPaymentDto(
                formatter.format(commission.getDate()),
                new Price(new BigDecimal(commissionDetails.getDeliveryAmount()), mad),
                new Price(new BigDecimal(commissionDetails.getDeliveryCommission()), mad),
                Long.valueOf(orderCount),
                new Price(new BigDecimal(orderCount * commissionDetails.getDeliveryCommission()), mad),
                new Price(new BigDecimal(toPay), mad),
                new Price(new BigDecimal(toReceive), mad),
                commission.getPaymentStatus()
        );
    }

    @Transactional
    private Price calculateTotalSubscriptions(List<Subscription> subscriptions) {
        List<Subscription> filteredSubscriptions = subscriptions.stream()
                .filter(subscription ->
                        subscription.getPartner().type().equals(PartnerType.NORMAL_PARTNER)
                                || (subscription.getPartner().type().equals(PartnerType.SUB_ENTITY)))
                .collect(Collectors.toList());

// Then check if the filtered list is empty and calculate accordingly

        return filteredSubscriptions.isEmpty()
                ? Price.ZERO(Currency.getInstance("MAD"))
                : filteredSubscriptions.stream()
                .map(Subscription::getAmount)
                .reduce(Price.ZERO(Currency.getInstance("MAD")), Price::add);
    }

    @Transactional
    private Price calculateTotalSubscriptionsOfPrincipal(UUID id, int year) {

        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        List<Subscription> subscriptions =  subscriptionService.findByStartDateBetweenAndSubscriptionStatusNot(startOfYear, endOfYear, SubscriptionStatus.NOT_STARTED, id);

        List<Subscription> filter =  subscriptions.stream().filter(s -> s.getPartner().type().equals(PartnerType.SUB_ENTITY)).collect(Collectors.toList());

        return filter.isEmpty()
                ? Price.ZERO(Currency.getInstance("MAD"))
                : filter.stream()
                .map(Subscription::getAmount)
                .reduce(Price.ZERO(Currency.getInstance("MAD")), Price::add);
    }

    // case 1 -<  some sub not has all subscriptions ->
    @Transactional
    private Price calculateTotalDeadlines(List<Subscription> subscriptions) {
// First, get the filtered subscriptions
        List<Subscription> filteredSubscriptions = subscriptions.stream()
                .filter(subscription ->
                        subscription.getPartner().type().equals(PartnerType.NORMAL_PARTNER)
                                || (subscription.getPartner().type().equals(PartnerType.SUB_ENTITY)))
                .collect(Collectors.toList());

// Then check if the filtered list is empty and calculate accordingly
        Price totalAmount;
        if (filteredSubscriptions.isEmpty()) {
            totalAmount = Price.ZERO(Currency.getInstance("MAD"));
        } else {
            totalAmount = filteredSubscriptions.stream()
                    .map(s -> s.getDeadlines().get(0).getAmount())
                    .reduce(Price.ZERO(Currency.getInstance("MAD")), Price::add);
        }

        return totalAmount;
    }

    @Transactional
    private SubscriptionsListDto createSubscriptionsListDto(UUID partnerId, List<Subscription> partnerSubscriptions, int year) {
        // Get partner information from the first subscription
        Subscription firstSubscription = partnerSubscriptions.get(0);
        PartnerI partner = !firstSubscription.getPartner().type().equals(PartnerType.SUB_ENTITY) ? this.organizationEntityService.findById(firstSubscription.getPartner().id()) : this.subEntityService.getEntityById(firstSubscription.getPartner().id());
        partnerSubscriptions.forEach(s -> {
            s.setPartnerI(partner);
        });

        PartnerInfoDto partnerInfoDto = new PartnerInfoDto(
                partner.getId(),
                partner.getName(),
                partner.getAvatarPath(),
                partner.getCity()
        );

        // Calculate total amount for this partner
        Price partnerTotal = partner.getPartnerType().equals(PartnerType.PARTNER_SB) ? calculateTotalSubscriptionsOfPrincipal(partnerId, year) :  calculateTotalSubscriptions(partnerSubscriptions);

        return new SubscriptionsListDto(
                partner.getPartnerType().equals(PartnerType.SUB_ENTITY) ? ((SubEntity) partner).getContract().getId() : ((OrganizationEntity) partner).getContract().getId(),
                partnerInfoDto,
                firstSubscription.getPartner().type(),
                partnerTotal,
                partnerSubscriptions.stream()
                        .flatMap(subscription -> subscription.getSolutions().stream())
                        .map(Solution::getName)  // assuming Solution class has a getName() method
                        .distinct()  // to remove duplicates if any
                        .collect(Collectors.toList()),
                partner.getPartnerType().equals(PartnerType.NORMAL_PARTNER) ||
                        (partner.subscriptionPayedBySubEntities()  && partner.getPartnerType().equals(PartnerType.SUB_ENTITY))
                ||  (!partner.subscriptionPayedBySubEntities()  && partner.getPartnerType().equals(PartnerType.PARTNER_SB))
        );
    }

    // create ->

    @Transactional
    private SubscriptionsDto mapToSubscriptionsDto(Subscription subscription) {
        List<DeadlinesDto> deadlinesDto = subscription.getDeadlines().stream()
                .sorted(Comparator.comparing(Deadlines::getCreatedAt))  // Sort by createdAt
                .map(deadline -> {
                    LocalDate dueDate = deadline.getDueDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("fr", "FR"));
                    String formattedDate = dueDate.format(formatter);

                    boolean isPaymentEligible = subscription.getPartner().type().equals(PartnerType.NORMAL_PARTNER)
                            || (subscription.getPartner().type().equals(PartnerType.SUB_ENTITY) && ((PartnerI)subscription.getPartnerI()).subscriptionPayedBySubEntities())
                            || (subscription.getPartner().type().equals(PartnerType.PARTNER_SB) && ((PartnerI)subscription.getPartnerI()).subscriptionPayedBySubEntities() == false);

                    return new DeadlinesDto(
                            deadline.getId(),
                            formattedDate,
                            deadline.getStatus(),
                            !(subscription.getPartnerI().getPartnerType().equals(PartnerType.PARTNER_SB))
                                    ? deadline.getAmount()
                                    : deadline.getSubEntityDeadlines().stream()
                                    .map(d -> d.getAmount())
                                    .reduce(Price.ZERO(Currency.getInstance("MAD")), Price::add),
                            isPaymentEligible
                    );
                })
                .collect(Collectors.toList());

        return new SubscriptionsDto(
                subscription.getId(),
                !(subscription.getPartnerI().getPartnerType().equals(PartnerType.PARTNER_SB))
                        ? subscription.getAmount()
                        : subscription.getSubEntities().stream()
                        .map(d -> d.getAmount())
                        .reduce(Price.ZERO(Currency.getInstance("MAD")), Price::add),
                subscription.getSolutions().stream().map(s -> s.getName()).toList(),
                deadlinesDto
        );
    }

}
