package net.foodeals.statistics.domain.repositories;



import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.ClientQueryParams;
import net.foodeals.statistics.application.responses.ClientResponse;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ClientStatisticsRepositoryImpl implements ClientStatisticsRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public ClientResponse getClientStatistics(ClientQueryParams filter) {
        LocalDate startDate = filter.getStartDate() != null ? LocalDate.parse(filter.getStartDate()) : LocalDate.now().minusMonths(6);
        LocalDate endDate = filter.getEndDate() != null ? LocalDate.parse(filter.getEndDate()) : LocalDate.now();

        var start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        var end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        long totalUsers = em.createQuery("""
            SELECT COUNT(u.id)
            FROM User u
            WHERE u.role.name = 'CLIENT'
              AND u.createdAt BETWEEN :start AND :end
        """, Long.class)
        .setParameter("start", start)
        .setParameter("end", end)
        .getSingleResult();

        long activeUsers = em.createQuery("""
            SELECT COUNT(DISTINCT o.client)
            FROM Order o
            WHERE o.createdAt BETWEEN :start AND :end
        """, Long.class)
        .setParameter("start", start)
        .setParameter("end", end)
        .getSingleResult();

        long dailyActive = em.createQuery("""
            SELECT COUNT(DISTINCT o.client)
            FROM Order o
            WHERE o.createdAt >= :todayStart
        """, Long.class)
        .setParameter("todayStart", LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        .getSingleResult();

        Double retentionRate = em.createQuery("""
            SELECT CASE WHEN prev.count > 0
              THEN ((act.count * 1.0) / prev.count) * 100
              ELSE 0 END
            FROM
              (SELECT COUNT(u.id) AS count
               FROM User u WHERE u.createdAt < :start) prev,
              (SELECT COUNT(DISTINCT o.client.id) AS count
               FROM Order o WHERE o.createdAt BETWEEN :start AND :end) act
        """, Double.class)
        .setParameter("start", start)
        .setParameter("end", end)
        .getSingleResult();

        /*Double avgSession = em.createQuery("""
            SELECT COALESCE(AVG(s.duration),0)
            FROM Session s
            WHERE s.startTime BETWEEN :start AND :end
        """, Double.class)
        .setParameter("start", start)
        .setParameter("end", end)
        .getSingleResult();*/
        
        Double avgSession=0.30;
        
       

        BigDecimal clvResult = em.createQuery("""
            SELECT COALESCE(SUM(o.price.amount), 0)
            FROM Order o
            WHERE o.createdAt BETWEEN :start AND :end
        """, BigDecimal.class)
        .setParameter("start", start)
        .setParameter("end", end)
        .getSingleResult();

        Double clv = clvResult.doubleValue();


        // Exemple de timeSeries simplifiée : acquisition mensuelle
        List<ClientResponse.TimeSeriesDataPoint> userGrowth = em.createQuery("""
            SELECT FUNCTION('to_char', u.createdAt, 'YYYY-MM'), COUNT(u.id)
            FROM User u
            WHERE u.createdAt BETWEEN :start AND :end
              AND u.role.name = 'CLIENT'
            GROUP BY FUNCTION('to_char', u.createdAt, 'YYYY-MM')
            ORDER BY 1
        """, Object[].class)
        .setParameter("start", start)
        .setParameter("end", end)
        .getResultList()
        .stream()
        .map(row -> new ClientResponse.TimeSeriesDataPoint(
            (String) row[0],
            ((Number) row[1]).longValue(),
            ((Number) row[1]).longValue()
        ))
        .collect(Collectors.toList());

        return ClientResponse.builder()
            .summary(ClientResponse.Summary.builder()
                .totalUsers(buildCard("Total Utilisateurs", totalUsers, "users", "bg-blue-100", false, null, null))
                .activeUsers(buildCard("Utilisateurs Actifs", activeUsers, "check-circle", "bg-green-100", false, null, null))
                .dailyActiveUsers(buildCard("Actifs Aujourd’hui", dailyActive, "calendar-check", "bg-yellow-100", false, null, null))
                .userRetentionRate(buildCard("Taux de Rétention", retentionRate.longValue(), "refresh", "bg-teal-100", false, null, null))
                .averageSessionDuration(buildCard("Durée Session Moyenne", avgSession.longValue(), "clock", "bg-orange-100", false, null, null))
                .customerLifetimeValue(buildCard("CLV", clv.longValue(), "euro", "bg-indigo-100", true, null, null))
                .build()
            )
            .timeSeries(ClientResponse.TimeSeries.builder()
                .userGrowth(userGrowth)
                // tu peux ajouter engagementTrends, orderBehavior, retentionMetrics de la même manière
                .build()
            )
            .build();
    }

    private ClientResponse.Card buildCard(String title, long value, String icon, String className, boolean currency, Double change, String trend) {
        return ClientResponse.Card.builder()
            .title(title)
            .value(value)
            .iconType(icon)
            .className(className)
            .currency(currency)
            .change(change)
            .trend(trend != null ? trend : "stable")
            .build();
    }
}

