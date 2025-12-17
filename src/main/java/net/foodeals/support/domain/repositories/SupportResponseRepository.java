package net.foodeals.support.domain.repositories;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.foodeals.support.domain.entities.SupportResponse;

public interface SupportResponseRepository extends JpaRepository<SupportResponse, UUID> {
  
	  @Query("""
		        select r from SupportResponse r
		        where r.ticket.id = :ticketId
		        order by r.createdAt asc
		    """)
		    List<SupportResponse> findAllByTicketIdOrderByCreated(UUID ticketId);

		    @Query("""
		        select r from SupportResponse r
		        where r.ticket.id = :ticketId and r.type = 'admin_response'
		        order by r.createdAt asc
		    """)
		    List<SupportResponse> findAdminResponses(UUID ticketId);

		    @Query("""
		        select max(r.createdAt) from SupportResponse r
		        where r.ticket.id = :ticketId
		    """)
		    OffsetDateTime findLastResponseAt(UUID ticketId);

		    long countByTicketId(UUID ticketId);
}