package net.foodeals.payment.application.services;

import net.foodeals.contract.domain.entities.Subscription;
import net.foodeals.payment.application.dto.request.PaymentRequest;
import net.foodeals.payment.application.dto.request.PaymentType;
import net.foodeals.payment.application.dto.request.ReceiveDto;
import net.foodeals.payment.application.dto.response.*;
import net.foodeals.payment.domain.entities.Enum.PartnerType;
import net.foodeals.payment.domain.entities.PartnerCommissions;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    List<String> getAvailableMonths();
    List<Integer> getAvailableYears();

    List<Integer> getAvailableYearsByPartner(UUID partnerId);


    Page<PartnerInfoDto> searchPartnersByNameCommission(String name, List<PartnerType> types, Pageable pageable, UUID id);

    Page<PartnerInfoDto> searchPartnersByNameSubscription(String name, List<PartnerType> types, Pageable pageable, UUID id);

    List<String> getAvailableMonthsByPartner(UUID partnerId);

    List<String> getAvailableMonthsByOrganization(UUID organizationId);

    List<PartnerCommissions> getCommissionPayments(int year, int month);

    SubscriptionPaymentDto toSubscriptionPaymentDto(Subscription subscription);

    PaymentResponse processPayment(PaymentRequest paymentRequest, MultipartFile document, PaymentType type) throws BadRequestException;

    Page<CommissionPaymentDto> convertCommissionToDto(Page<PartnerCommissions> payments);

    MonthlyOperationsDto monthlyOperations(UUID id, int year, int month, Pageable page, PartnerType type);

    List<PartnerCommissions> getCommissionPaymentsByOrganizationId(UUID id, int year, int month);

    CommissionDto getCommissionResponse(List<PartnerCommissions> payments, Pageable page);

    PaymentStatistics getPaymentStatistics(List<PartnerCommissions> commissions);

    PaymentResponse receive(ReceiveDto receiveDto, PaymentType type) throws Exception;

    void receiveCommission(ReceiveDto receiveDto) throws BadRequestException;

    void receiveSubscription(ReceiveDto receiveDto);

    PaymentFormData getFormData(UUID id, PaymentType type);

    PaymentFormData getCommissionFormData(UUID id);

    PaymentFormData getSubscriptionFormData(UUID id);

    SubscriptionPaymentDto getSubscriptionResponse(int year, Pageable pageable, UUID id);

    SubscriptionDetails getSubscriptionDetails(int year, UUID id);

    DeliveryPaymentResponse getDeliveryPayments(int year, Pageable page, UUID id);

    DeliveryPaymentDto convertToDeliveryCommission(PartnerCommissions commissionsPage);
}
