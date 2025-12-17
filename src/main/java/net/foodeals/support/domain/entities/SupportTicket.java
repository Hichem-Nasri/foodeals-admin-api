package net.foodeals.support.domain.entities;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Setter
@Getter
@Entity @Table(name = "support_tickets")
public class SupportTicket extends AbstractEntity<UUID> {

	@Id
    @GeneratedValue
    @UuidGenerator
	private UUID id;

	private String subject;
	@Column(columnDefinition = "text")
	private String message;

	private String status; // pending, in_progress, resolved, closed
	private String priority; // low, medium, high
	private String category; // payment, account, ...

	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "user_email")
	private String userEmail;
	@Column(name = "user_phone")
	private String userPhone;
	@Column(name = "user_avatar")
	private String userAvatar;
	@Column(name = "user_account_created")
	private OffsetDateTime userAccountCreated;

	@Column(name = "assigned_to_id")
	private Integer assignedToId;
	@Column(name = "assigned_to_name")
	private String assignedToName;
	@Column(name = "assigned_to_email")
	private String assignedToEmail;

	private Integer responseCount;
	private OffsetDateTime lastResponseAt;
	private BigDecimal satisfactionRating;

}
