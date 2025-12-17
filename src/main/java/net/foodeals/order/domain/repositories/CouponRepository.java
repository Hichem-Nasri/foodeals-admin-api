package net.foodeals.order.domain.repositories;

import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.order.application.dtos.responses.CouponSummaryDto;
import net.foodeals.order.domain.entities.Coupon;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends BaseRepository<Coupon, UUID> {

	Optional<Coupon> findByCode(String code);

	long countByIsEnabled(boolean isEnabled);

	@Query("""
		    SELECT new net.foodeals.order.application.dtos.responses.CouponSummaryDto(
		        c.id, c.name, c.code, c.discount, c.isEnabled, c.endsAt, COUNT(DISTINCT o.client.id)
		    )
		    FROM Coupon c
		    LEFT JOIN c.orders o
		    WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
		      AND (:code IS NULL OR LOWER(c.code) LIKE LOWER(CONCAT('%', :code, '%')))
		      AND (:isEnabled IS NULL OR c.isEnabled = :isEnabled)
		      AND (:minDiscount IS NULL OR c.discount >= :minDiscount)
		      AND (:maxDiscount IS NULL OR c.discount <= :maxDiscount)
		      AND (coalesce(:startDate, current_date) IS NULL OR c.endsAt >= :startDate)
		      AND (coalesce(:endDate, current_date) IS NULL OR c.endsAt <= :endDate)
		    GROUP BY c
		    ORDER BY c.createdAt DESC
		""")
		List<CouponSummaryDto> searchCouponsWithFilters(
		    @Param("name") String name,
		    @Param("code") String code,
		    @Param("isEnabled") Boolean isEnabled,
		    @Param("minDiscount") Float minDiscount,
		    @Param("maxDiscount") Float maxDiscount,
		    @Param("startDate") Date startDate,
		    @Param("endDate") Date endDate
		);

}