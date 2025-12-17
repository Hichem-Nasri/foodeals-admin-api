package net.foodeals.order.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.order.application.dtos.requests.CouponRequest;
import net.foodeals.order.application.dtos.responses.CouponSummaryDto;
import net.foodeals.order.domain.entities.Coupon;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CouponService extends CrudService<Coupon, UUID, CouponRequest> {

    Coupon toggleIsEnabled(UUID id);
    
    List<CouponSummaryDto> searchCoupons(String name, String code, Boolean isEnabled, Float minDiscount, Float maxDiscount, Date start, Date end);
}
