package net.foodeals.support.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.foodeals.support.domain.entities.SupportAttachment;

public interface SupportAttachmentRepository extends JpaRepository<SupportAttachment, UUID> {
	List<SupportAttachment> findByTicketId(UUID ticketId);

    @Query("select count(a) from SupportAttachment a where a.ticket.id = :ticketId")
    long countByTicket(UUID ticketId);
}