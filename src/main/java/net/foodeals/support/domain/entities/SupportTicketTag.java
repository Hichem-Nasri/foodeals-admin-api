package net.foodeals.support.domain.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "support_ticket_tags")
@IdClass(SupportTicketTag.Key.class)
public class SupportTicketTag {
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id")
	private SupportTicket ticket;
	@Id
	private String tag;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Key implements Serializable {
		private UUID ticket;
		private String tag;
	}
}