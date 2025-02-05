package net.foodeals.payment.application.services.utils.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.payment.application.dto.request.PaymentRequest;
import net.foodeals.payment.application.dto.request.PaymentType;
import net.foodeals.payment.application.dto.request.paymentDetails.CashDetails;
import net.foodeals.payment.application.dto.response.PaymentResponse;
import net.foodeals.payment.application.services.utils.PaymentProcessor;
import net.foodeals.payment.domain.entities.Enum.PartnerType;
import net.foodeals.payment.domain.entities.Enum.PaymentDirection;
import net.foodeals.payment.domain.entities.Enum.PaymentStatus;
import net.foodeals.payment.domain.entities.PartnerCommissions;
import net.foodeals.payment.domain.entities.paymentMethods.CashPaymentMethod;
import net.foodeals.payment.domain.repository.PartnerCommissionsRepository;
import net.foodeals.user.application.services.UserService;
import net.foodeals.user.domain.entities.User;
import org.apache.coyote.BadRequestException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CashProcessor implements PaymentProcessor {

    private final PartnerCommissionsRepository repository;
    private final UserService userService;

    @Override
    @Transactional
    public PaymentResponse process(PaymentRequest request, MultipartFile document, PaymentType type) {

        try {
            // upload document
            if (type.equals(PaymentType.COMMISSION)) {
                this.processCommission(request);
            } else {
                this.processSubscription(request);
            }
        } catch(Exception e) {
            throw new RuntimeException("error : " + e.getMessage());
        }
        return new PaymentResponse("payment validated successfully");
    }

    @Override
    @Transactional
    public void processCommission(PaymentRequest request) {
        try {
            PartnerCommissions partnerCommission = this.repository.findById(request.id()).orElseThrow(() -> new ResourceNotFoundException("commission not found with id " + request.id()));
            CashPaymentMethod paymentMethod = new CashPaymentMethod();
            CashDetails cashDetails = (CashDetails) request.paymentDetails();
            paymentMethod.setAmount(new Price(request.amount().amount(), Currency.getInstance(request.amount().currency())));
            paymentMethod.setRecuperationDate(cashDetails.date());
            partnerCommission.setPaymentMethod(paymentMethod);
            partnerCommission.setPaymentDirection(PaymentDirection.FOODEALS_TO_PARTENER);
            partnerCommission.setPaymentStatus(PaymentStatus.VALIDATED_BY_FOODEALS);
            final String email = SecurityContextHolder.getContext().getAuthentication().getName();
            final User emitter = this.userService.findByEmail(email);
            partnerCommission.setEmitter(emitter);
            if (partnerCommission.getPartnerInfo().type().equals(PartnerType.PARTNER_SB)) {
                Set<PartnerCommissions> childs = partnerCommission.getSubEntityCommissions();
                childs = childs.stream().map(c -> {
                    c.setPaymentStatus(PaymentStatus.VALIDATED_BY_FOODEALS);
                    return c;
                }).collect(Collectors.toSet());
                this.repository.saveAll(childs);
            }
            this.repository.save(partnerCommission);
        } catch (Exception e) {
            throw new RuntimeException("error : " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void processSubscription(PaymentRequest request) {

    }

}

// validate by subentities
