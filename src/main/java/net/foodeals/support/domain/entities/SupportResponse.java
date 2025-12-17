package net.foodeals.support.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "support_responses")
@Setter
@Getter
public class SupportResponse extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id")
	private SupportTicket ticket;

	@Column(columnDefinition = "text")
	private String message;

	private String type; // admin_response | internal_note | user_message
	private Integer authorId;
	private String authorName;
	private String authorEmail;
	private String authorRole;
	private Boolean isInternal;
}
