package net.foodeals.donate.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "relaunch_donate_history")
@Getter
@Setter
public class RelaunchDonateHistory extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "organization_id", nullable = false)
	private OrganizationEntity organization;

	@ManyToOne
	@JoinColumn(name = "donate_id", nullable = false)
	private Donate donate;

	private LocalDateTime relaunchDate;

	public RelaunchDonateHistory(OrganizationEntity organization, Donate donate, LocalDateTime relaunchDate) {
		this.organization = organization;
		this.donate = donate;
		this.relaunchDate = relaunchDate;
	}

	public RelaunchDonateHistory() {
		super();
	}
}