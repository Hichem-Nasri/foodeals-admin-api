package net.foodeals.support.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.foodeals.support.domain.entities.SupportTicketTag;

public interface SupportTicketTagRepository extends JpaRepository<SupportTicketTag, SupportTicketTag.Key> {
    @Query("select t.tag from SupportTicketTag t where t.ticket.id = :ticketId")
    List<String> findTags(UUID ticketId);
}