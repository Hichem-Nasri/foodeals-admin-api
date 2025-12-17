package net.foodeals.statistics.domain.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.foodeals.statistics.application.responses.FinanceQueryParams;


@Repository
@RequiredArgsConstructor
public class FinanceStatisticsRepositoryImpl implements FinanceStatisticsRepository {

    private final EntityManager em;

    @Override
    public double getTotalRevenue(LocalDate start, LocalDate end, FinanceQueryParams filter) {
        return em.createQuery("""
            SELECT COALESCE(SUM(p.paymentsWithCard + p.paymentsWithCash), 0)
            FROM Payment p
            WHERE p.createdAt BETWEEN :start AND :end
        """, Double.class)
        .setParameter("start", start.atStartOfDay().toInstant(ZoneOffset.UTC))
        .setParameter("end", end.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC))
        .getSingleResult();
    }

    @Override
    public long getMonthlyRecurringRevenue(LocalDate start, LocalDate end, FinanceQueryParams filter) {
    	 BigDecimal result = em.createQuery("""
    		        SELECT COALESCE(SUM(s.amount.amount), 0)
    		        FROM Subscription s
    		        WHERE s.createdAt BETWEEN :start AND :end
    		    """, BigDecimal.class)
    		    .setParameter("start", start.atStartOfDay().toInstant(ZoneOffset.UTC))
    		    .setParameter("end", end.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC))
    		    .getSingleResult();

    		    return result.longValue();
    }

    @Override
    public long getTransactionFees(LocalDate start, LocalDate end, FinanceQueryParams filter) {
        BigDecimal result = em.createQuery("""
            SELECT COALESCE(SUM(t.price.amount), 0)
            FROM Transaction t
            WHERE t.createdAt BETWEEN :start AND :end
        """, BigDecimal.class)
        .setParameter("start", start.atStartOfDay().toInstant(ZoneOffset.UTC))
        .setParameter("end", end.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC))
        .getSingleResult();

        return result.longValue();
    }

    @Override
    public long getPartnerCommissions(LocalDate start, LocalDate end, FinanceQueryParams filter) {
        Double result = em.createQuery("""
            SELECT COALESCE(SUM(
                c.cash + c.card + c.deliveryAmount + c.deliveryCommission
            ), 0)
            FROM OrganizationEntity o
            JOIN o.contract con
            JOIN con.solutionContracts sc
            JOIN sc.commission c
            WHERE o.createdAt BETWEEN :start AND :end
        """, Double.class)
        .setParameter("start", start.atStartOfDay().toInstant(ZoneOffset.UTC))
        .setParameter("end", end.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC))
        .getSingleResult();

        return result.longValue();
    }


    @Override
    public long getOperationalCosts(LocalDate start, LocalDate end, FinanceQueryParams filter) {
    	return 0L;
    }
}
