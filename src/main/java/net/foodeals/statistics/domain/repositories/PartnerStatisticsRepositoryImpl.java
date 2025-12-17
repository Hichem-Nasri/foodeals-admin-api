package net.foodeals.statistics.domain.repositories;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.PartnerQueryParams;
import net.foodeals.statistics.application.responses.PartnerResponse;

@Repository
@RequiredArgsConstructor
public class PartnerStatisticsRepositoryImpl implements PartnerStatisticsRepository {

	@PersistenceContext
    private final EntityManager em;
	
	@Override
	public PartnerResponse getPartnerStatistics(PartnerQueryParams filter) {
	    LocalDate start = filter.getStartDate() != null ? LocalDate.parse(filter.getStartDate()) : LocalDate.now().minusMonths(6);
	    LocalDate end = filter.getEndDate() != null ? LocalDate.parse(filter.getEndDate()) : LocalDate.now();

	    Instant startInstant = start.atStartOfDay(ZoneId.systemDefault()).toInstant();
	    Instant endInstant = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

	    long totalPartners = em.createQuery("""
	        SELECT COUNT(DISTINCT o.organizationEntity)
	        FROM Offer o
	        WHERE o.createdAt BETWEEN :start AND :end
	    """, Long.class)
	    .setParameter("start", startInstant)
	    .setParameter("end", endInstant)
	    .getSingleResult();

	    long activePartners = em.createQuery("""
	        SELECT COUNT(DISTINCT o.organizationEntity)
	        FROM Offer o
	        WHERE o.createdAt BETWEEN :start AND :end
	        AND o.deal.dealStatus = 'AVAILABLE'
	    """, Long.class)
	    .setParameter("start", startInstant)
	    .setParameter("end", endInstant)
	    .getSingleResult();

	    long totalStores = em.createQuery("""
	        SELECT COUNT(DISTINCT o)
	        FROM OrganizationEntity o
	        WHERE o.createdAt BETWEEN :start AND :end
	    """, Long.class)
	    .setParameter("start", startInstant)
	    .setParameter("end", endInstant)
	    .getSingleResult();

	    Double avgRevenue = 0.0;
	    		
	    		/*em.createQuery("""
	        SELECT COALESCE(AVG(t.price.amount), 0)
	        FROM Transaction t
	        WHERE t.createdAt BETWEEN :start AND :end
	    """, Double.class)
	    .setParameter("start", startInstant)
	    .setParameter("end", endInstant)
	    .getSingleResult();*/

	    Double satisfaction = 0.0;
	    		/*em.createQuery("""
	        SELECT COALESCE(AVG(f.partnerScore), 0)
	        FROM Feedback f
	        WHERE f.createdAt BETWEEN :start AND :end
	    """, Double.class)
	    .setParameter("start", startInstant)
	    .setParameter("end", endInstant)
	    .getSingleResult();*/

	    long adopted =0L;
	    		
	    		/*em.createQuery("""
	        SELECT COUNT(DISTINCT o.organizationEntity.id)
	        FROM Offer o
	        WHERE o.solutionType IS NOT NULL
	    """, Long.class).getSingleResult();*/

	    double adoptionRate = totalPartners == 0 ? 0 : (double) adopted * 100 / totalPartners;

	    return PartnerResponse.builder()
	        .summary(PartnerResponse.Summary.builder()
	            .totalPartners(buildCard("Partenaires", totalPartners, "icon-users", "bg-blue-100", false, null, null))
	            .activePartners(buildCard("Actifs", activePartners, "icon-active", "bg-green-100", false, null, null))
	            .totalStores(buildCard("Magasins", totalStores, "icon-store", "bg-yellow-100", false, null, null))
	            .averageRevenuePerPartner(buildCard("Revenu moyen", avgRevenue.longValue(), "icon-revenue", "bg-indigo-100", true, null, null))
	            .partnerSatisfactionScore(buildCard("Satisfaction", satisfaction.longValue(), "icon-star", "bg-orange-100", false, null, null))
	            .solutionAdoptionRate(buildCard("Adoption", (long) adoptionRate, "icon-growth", "bg-teal-100", false, null, null))
	            .build())
	        .timeSeries(PartnerResponse.TimeSeries.builder().build())
	        .performance(PartnerResponse.Performance.builder().build())
	        .stores(PartnerResponse.Stores.builder().build())
	        .solutions(PartnerResponse.Solutions.builder().build())
	        .geographic(PartnerResponse.Geographic.builder().build())
	        .onboarding(PartnerResponse.Onboarding.builder().build())
	        .revenue(PartnerResponse.Revenue.builder().build())
	        .filterOptions(PartnerResponse.FilterOptions.builder().build())
	        .build();
	}

	private PartnerResponse.Card buildCard(String title, long value, String iconType, String className, boolean currency, Double change, String trend) {
	    return PartnerResponse.Card.builder()
	            .title(title)
	            .value(value)
	            .iconType(iconType)
	            .className(className)
	            .currency(currency)
	            .change(change)
	            .trend(trend != null ? trend : "stable")
	            .build();
	}

	

}
