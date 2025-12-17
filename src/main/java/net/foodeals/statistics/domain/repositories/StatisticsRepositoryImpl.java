package net.foodeals.statistics.domain.repositories;

import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import net.foodeals.offer.domain.enums.Category;
import net.foodeals.order.domain.enums.OrderSource;
import net.foodeals.statistics.application.responses.GlobalStatisticsFilterDto;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.Card;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.EngagementDataPoint;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.FilterOptions;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.GeographicData;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.ParticipationRateData;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.PartnerOption;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.PartnerPerformance;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.RegionOption;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.StorePerformanceData;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.Summary;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.TimeSeriesDataPoint;
import net.foodeals.statistics.application.responses.GlobalStatisticsResponseDto.UserEngagement;

@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryImpl implements StatisticsRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Summary fetchSummaryData(GlobalStatisticsFilterDto filter) {
        Instant start = filter.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        Long totalFoodWaste = (Long) entityManager.createQuery("SELECT SUM(d.quantity) FROM Deal d WHERE d.createdAt BETWEEN :start AND :end")
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        Long totalTransactions = (Long) entityManager.createQuery("SELECT SUM(t.price.amount) FROM Transaction t WHERE t.createdAt BETWEEN :start AND :end")
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        Long processedOrders = (Long) entityManager.createQuery("SELECT COUNT(o.id) FROM Order o WHERE o.status = 'COMPLETED' AND o.createdAt BETWEEN :start AND :end")
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        Long clientUsers = (Long) entityManager.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.role.name = 'CLIENT'").getSingleResult();
        Long proUsers = (Long) entityManager.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.role.name = 'MANAGER'").getSingleResult();
        Long partners = (Long) entityManager.createQuery("SELECT COUNT(DISTINCT o.organizationEntity.id) FROM Offer o").getSingleResult();

        return Summary.builder()
                .totalFoodWastePrevented(new Card("Total Food Waste Prevented", Optional.ofNullable(totalFoodWaste).orElse(0L), "icon-food", "highlight-card", false))
                .totalTransactions(new Card("Total Transactions", Optional.ofNullable(totalTransactions).orElse(0L), "icon-money", "money-card", true))
                .commandsProcessed(new Card("Commands Processed", Optional.ofNullable(processedOrders).orElse(0L), "icon-process", "process-card", false))
                .clientAdoption(new Card("Client Adoption", Optional.ofNullable(clientUsers).orElse(0L), "icon-client", "client-card", false))
                .proAdoption(new Card("Pro Adoption", Optional.ofNullable(proUsers).orElse(0L), "icon-pro", "pro-card", false))
                .totalPartners(new Card("Total Partners", Optional.ofNullable(partners).orElse(0L), "icon-partner", "partner-card", false))
                .build();
    }

    @Override
    public List<TimeSeriesDataPoint> fetchWasteReductionData(GlobalStatisticsFilterDto filter) {
        Instant start = filter.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Object[]> rows = entityManager.createQuery("""
        	    SELECT EXTRACT(MONTH FROM o.createdAt), o.orderSource, SUM(d.quantity)
        	    FROM Order o
        	    JOIN o.offer off
        	    JOIN Deal d ON d.offer = off
        	    WHERE o.createdAt BETWEEN :start AND :end
        	    GROUP BY EXTRACT(MONTH FROM o.createdAt), o.orderSource
        	    ORDER BY EXTRACT(MONTH FROM o.createdAt)
        	""", Object[].class)
        	.setParameter("start", start)
        	.setParameter("end", end)
        	.getResultList();

        Map<Integer, TimeSeriesDataPoint> result = new TreeMap<>();

        for (Object[] row : rows) {
            int month = ((Number) row[0]).intValue();
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.FRENCH);
            OrderSource source = (OrderSource) row[1];
            Long quantity = (Long) row[2];

            result.putIfAbsent(month, new TimeSeriesDataPoint(monthName, 0L, 0L, 0L,0L, null));

            TimeSeriesDataPoint point = result.get(month);

            switch (source) {
                case DEAL_PRO -> point.setDLC(quantity);
                case PRO_MARKET -> point.setMARKET(quantity);
                case DONATION -> point.setDONATE(quantity);
            }

            // Total
            point.setValue(
                Optional.ofNullable(point.getDLC()).orElse(0L)
                + Optional.ofNullable(point.getMARKET()).orElse(0L)
                + Optional.ofNullable(point.getDONATE()).orElse(0L)
            );
        }

        return new ArrayList<>( result .values());
    }

    @Override
    public List<TimeSeriesDataPoint> fetchPartnerEngagementData(GlobalStatisticsFilterDto filter) {
        Instant start = filter.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Object[]> rows = entityManager.createQuery("""
            SELECT 
                EXTRACT(MONTH FROM o.createdAt), 
                ord.orderSource, 
                COUNT(DISTINCT o.organizationEntity.id)
            FROM Offer o
            JOIN Order ord ON ord.offer = o
            WHERE o.createdAt BETWEEN :start AND :end
            GROUP BY EXTRACT(MONTH FROM o.createdAt), ord.orderSource
            ORDER BY EXTRACT(MONTH FROM o.createdAt)
        """, Object[].class)
        .setParameter("start", start)
        .setParameter("end", end)
        .getResultList();

        Map<Integer, TimeSeriesDataPoint> map = new TreeMap<>();

        for (Object[] row : rows) {
            int month = ((Number) row[0]).intValue();
            String source = row[1] != null ? row[1].toString() : "UNKNOWN";
            int partnerCount = ((Number) row[2]).intValue();

            if (month < 1 || month > 12) {
                continue; // ou lève exception si tu préfères
            }

            map.putIfAbsent(month, new TimeSeriesDataPoint(
                    Month.of(month).getDisplayName(TextStyle.SHORT, Locale.FRENCH),
                    0L, 0L, 0L, 0L, 0
            ));

            TimeSeriesDataPoint data = map.get(month);

            // Remplir DLC, MARKET, DONATE
            switch (source) {
                case "DEAL_PRO" -> data.setDLC(Optional.ofNullable(data.getDLC()).orElse(0L) + partnerCount);
                case "PRO_MARKET" -> data.setMARKET(Optional.ofNullable(data.getMARKET()).orElse(0L) + partnerCount);
                case "DONATION" -> data.setDONATE(Optional.ofNullable(data.getDONATE()).orElse(0L) + partnerCount);
            }

            // Maj total partenaires si nécessaire
            data.setPartners(Optional.ofNullable(data.getPartners()).orElse(0) + partnerCount);

            // Maj total général
            data.setValue(
                Optional.ofNullable(data.getDLC()).orElse(0L) +
                Optional.ofNullable(data.getMARKET()).orElse(0L) +
                Optional.ofNullable(data.getDONATE()).orElse(0L)
            );
        }

        return new ArrayList<>(map.values());
    }



    @Override
    public List<TimeSeriesDataPoint> fetchSolutionComparison(GlobalStatisticsFilterDto filter) {
        // Example same as waste reduction, grouped by solutionType only
        Instant start = filter.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Object[]> rows = entityManager.createQuery("""
        	    SELECT EXTRACT(MONTH FROM o.createdAt), o.orderSource, SUM(d.quantity)
        	    FROM Order o
        	    JOIN o.offer of
        	    JOIN Deal d ON d.offer = of
        	    WHERE o.createdAt BETWEEN :start AND :end
        	    GROUP BY EXTRACT(MONTH FROM o.createdAt), o.orderSource
        	    ORDER BY EXTRACT(MONTH FROM o.createdAt)
        	""", Object[].class)
        	.setParameter("start", start)
        	.setParameter("end", end)
        	.getResultList();

        Map<Integer, TimeSeriesDataPoint> result = new TreeMap<>();

        for (Object[] row : rows) {
            int month = ((Number) row[0]).intValue();
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.FRENCH);
            OrderSource source = (OrderSource) row[1];
            Long quantity = (Long) row[2];

            result.putIfAbsent(month, new TimeSeriesDataPoint(monthName, 0L, 0L, 0L,0L, null));

            TimeSeriesDataPoint point = result.get(month);

            switch (source) {
                case DEAL_PRO -> point.setDLC(quantity);
                case PRO_MARKET -> point.setMARKET(quantity);
                case DONATION -> point.setDONATE(quantity);
            }

            point.setValue(
                Optional.ofNullable(point.getDLC()).orElse(0L)
                + Optional.ofNullable(point.getMARKET()).orElse(0L)
                + Optional.ofNullable(point.getDONATE()).orElse(0L)
            );
        }

        return new ArrayList<>(result.values());

       
		
    }

    @Override
    public PartnerPerformance fetchPartnerPerformance(GlobalStatisticsFilterDto filter) {
        Instant start = filter.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // --- Top Performing Stores ---
        List<Object[]> topStoresRows = entityManager.createQuery("""
            SELECT  e.name, a.address, COUNT(d.id), SUM(d.quantity)
            FROM Deal d
            JOIN d.offer o
            JOIN o.organizationEntity e
            JOIN e.address a
            WHERE d.createdAt BETWEEN :start AND :end
            GROUP BY e.id, e.name, a.address
            ORDER BY SUM(d.quantity) DESC
        """, Object[].class)
        .setParameter("start", start)
        .setParameter("end", end)
        .setMaxResults(5)
        .getResultList();

        List<StorePerformanceData> topStores = topStoresRows.stream()
            .map(row -> new StorePerformanceData(
              
                (String) row[0],
                (String) row[1],
                ((Number) row[2]).intValue(),
                ((Number) row[3]).doubleValue()
            ))
            .collect(Collectors.toList());

        // --- Geographic Distribution ---
        String geoQuery = """
            SELECT r.name, SUM(d.quantity)
            FROM Deal d
            JOIN d.offer o
            JOIN o.organizationEntity e
            JOIN e.address a
            JOIN a.region r
            WHERE d.createdAt BETWEEN :start AND :end
            """ + (filter.getRegion() != null && !"all".equalsIgnoreCase(filter.getRegion()) ? " AND r.name = :region" : "") + """
            GROUP BY r.name
        """;

        var geoQueryObj = entityManager.createQuery(geoQuery, Object[].class)
            .setParameter("start", start)
            .setParameter("end", end);

        if (filter.getRegion() != null && !"all".equalsIgnoreCase(filter.getRegion())) {
            geoQueryObj.setParameter("region", filter.getRegion());
        }

        List<Object[]> geoRows = geoQueryObj.getResultList();

        List<GeographicData> geoData = geoRows.stream()
            .map(row -> new GeographicData((String) row[0], ((Number) row[1]).longValue()))
            .collect(Collectors.toList());

        // --- Participation Rate ---
        List<Object[]> participationRows = entityManager.createQuery("""
            SELECT d.publishAs, COUNT(d.id)
            FROM Deal d
            WHERE d.createdAt BETWEEN :start AND :end
            GROUP BY d.publishAs
        """, Object[].class)
        .setParameter("start", start)
        .setParameter("end", end)
        .getResultList();

        long totalDeals = participationRows.stream()
            .mapToLong(row -> ((Number) row[1]).longValue())
            .sum();

        List<ParticipationRateData> participationRates = participationRows.stream()
            .map(row -> {
                String name = ((Enum<?>) row[0]).name(); // assuming it's an Enum like PublishAs
                long count = ((Number) row[1]).longValue();
                double rate = totalDeals > 0 ? (double) count / totalDeals * 100 : 0;
                return new ParticipationRateData(name, rate, name); // use name for solutionType as well
            })
            .collect(Collectors.toList());

        return PartnerPerformance.builder()
            .topPerformingStores(topStores)
            .geographicDistribution(geoData)
            .participationRate(participationRates)
            .build();
    }


    @Override
    public UserEngagement fetchUserEngagement(GlobalStatisticsFilterDto filter) {
        Instant start = filter.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = filter.getEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // --- 1. Client App Usage ---
        List<Object[]> clientUsageRows = entityManager.createQuery("""
            SELECT EXTRACT(MONTH FROM o.createdAt), COUNT(o.id)
            FROM Order o
            WHERE o.client.role.name = 'CLIENT' AND o.createdAt BETWEEN :start AND :end
            GROUP BY EXTRACT(MONTH FROM o.createdAt)
            ORDER BY EXTRACT(MONTH FROM o.createdAt)
        """, Object[].class)
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();

        List<EngagementDataPoint> clientAppUsage = clientUsageRows.stream()
            .map(row -> new EngagementDataPoint(getMonthName(((Number) row[0]).intValue()), ((Number) row[1]).longValue()))
            .collect(Collectors.toList());

        // --- 2. Pro App Adoption ---
        List<Object[]> proUsageRows = entityManager.createQuery("""
            SELECT EXTRACT(MONTH FROM o.createdAt), COUNT(o.id)
            FROM Offer o
            WHERE o.createdAt BETWEEN :start AND :end
            GROUP BY EXTRACT(MONTH FROM o.createdAt)
            ORDER BY EXTRACT(MONTH FROM o.createdAt)
        """, Object[].class)
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();

        List<EngagementDataPoint> proAppAdoption = proUsageRows.stream()
            .map(row -> new EngagementDataPoint(getMonthName(((Number) row[0]).intValue()), ((Number) row[1]).longValue()))
            .collect(Collectors.toList());

        // --- 3. Popular Categories ---
        List<Object[]> categoryRows = entityManager.createQuery("""
            SELECT d.category, COUNT(d.id)
            FROM Deal d
            WHERE d.createdAt BETWEEN :start AND :end
            GROUP BY d.category
            ORDER BY COUNT(d.id) DESC
        """, Object[].class)
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();

        List<EngagementDataPoint> popularCategories = categoryRows.stream()
            .map(row -> new EngagementDataPoint(((Category) row[0]).name(), ((Number) row[1]).longValue()))
            .collect(Collectors.toList());

        return UserEngagement.builder()
            .clientAppUsage(clientAppUsage)
            .proAppAdoption(proAppAdoption)
            .popularCategories(popularCategories)
            .build();
    }


    @Override
    public FilterOptions fetchFilterOptions(GlobalStatisticsFilterDto filter) {
        // Requête pour charger les partenaires
        List<Object[]> partnerRows = entityManager.createQuery("""
            SELECT o.id, o.name 
            FROM OrganizationEntity o
        """, Object[].class).getResultList();

        List<PartnerOption> partners = partnerRows.stream()
            .map(row -> new PartnerOption(
                String.valueOf(row[0]),
                (String) row[1]
            ))
            .collect(Collectors.toList());

        // Requête pour charger les régions
        List<Object[]> regionRows = entityManager.createQuery("""
            SELECT r.id, r.name 
            FROM Region r
        """, Object[].class).getResultList();

        List<RegionOption> regions = regionRows.stream()
            .map(row -> new RegionOption(
                String.valueOf(row[0]),
                (String) row[1]
            ))
            .collect(Collectors.toList());

        return FilterOptions.builder()
            .partners(partners)
            .regions(regions)
            .build();
    }


    private String getMonthName(int month) {
        return Month.of(month).getDisplayName(TextStyle.SHORT, Locale.FRENCH);
    }
}
