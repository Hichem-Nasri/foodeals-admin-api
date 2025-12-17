package net.foodeals.support.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "support_attachments")
@Setter
@Getter
public class SupportAttachment extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id")
	private SupportTicket ticket;

	private String filename;
	private String url;
	private Long size;
	@Column(name = "mime_type")
	private String mimeType;
}
