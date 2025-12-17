package net.foodeals.support.domain.repositories;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import net.foodeals.support.domain.entities.SupportTicket;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID>, 
JpaSpecificationExecutor<SupportTicket> {
	
	@Query("select avg(t.satisfactionRating) from SupportTicket t where t.satisfactionRating is not null")
    BigDecimal averageSatisfaction();
}