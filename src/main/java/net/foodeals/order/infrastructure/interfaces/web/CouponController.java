package net.foodeals.order.infrastructure.interfaces.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.order.application.dtos.requests.CouponRequest;
import net.foodeals.order.application.dtos.responses.CouponResponse;
import net.foodeals.order.application.dtos.responses.CouponStatsDto;
import net.foodeals.order.application.services.CouponService;
import net.foodeals.order.domain.entities.Coupon;
import net.foodeals.order.domain.repositories.CouponRepository;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService service;
    private final CouponRepository couponRepo;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Page<CouponResponse>> getAll(@RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        final Page<CouponResponse> response = service.findAll(pageNum, pageSize)
                .map(coupon -> mapper.map(coupon, CouponResponse.class));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> getById(@PathVariable UUID id) {
        final CouponResponse response = mapper.map(
                service.findById(id),
                CouponResponse.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CouponResponse> create(@RequestBody @Valid CouponRequest request) {
        final CouponResponse response = mapper.map(
                service.create(request),
                CouponResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CouponResponse> update(@PathVariable UUID id, @RequestBody @Valid CouponRequest request) {
        final CouponResponse response = mapper.map(
                service.update(id, request),
                CouponResponse.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<CouponResponse> toggleIsEnabled(@PathVariable UUID id) {
        final CouponResponse response = mapper.map(
                service.toggleIsEnabled(id),
                CouponResponse.class);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/enable")
    public ResponseEntity<String> enableCoupon(@PathVariable UUID id) {
        Coupon coupon = couponRepo.findById(id).orElse(null);
        if (coupon == null) {
            return ResponseEntity.notFound().build();
        }
        coupon.setIsEnabled(true);
        couponRepo.save(coupon);
        return ResponseEntity.ok("Coupon activé avec succès.");
    }
    
    @PostMapping("/{id}/disable")
    public ResponseEntity<String> disableCoupon(@PathVariable UUID id) {
        Coupon coupon = couponRepo.findById(id).orElse(null);
        if (coupon == null) {
            return ResponseEntity.notFound().build();
        }
        coupon.setIsEnabled(false);
        couponRepo.save(coupon);
        return ResponseEntity.ok("Coupon désactivé avec succès.");
    }
    
    @GetMapping("/stats")
    public ResponseEntity<CouponStatsDto> getCouponStats() {
        long total = couponRepo.count();
        long enabled = couponRepo.countByIsEnabled(true);
        long disabled = couponRepo.countByIsEnabled(false);
        
        CouponStatsDto stats = new CouponStatsDto(total, enabled, disabled);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchCoupons(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Boolean isEnabled,
            @RequestParam(required = false) Float minDiscount,
            @RequestParam(required = false) Float maxDiscount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        var result = service.searchCoupons(name, code, isEnabled, minDiscount, maxDiscount, startDate, endDate);
        return ResponseEntity.ok(Map.of("success", true, "data", result));
    }
}