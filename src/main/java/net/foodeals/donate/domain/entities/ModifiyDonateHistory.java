package net.foodeals.donate.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;

@Entity
@Table(name = "modify_donate_history")
@Getter
@Setter
public class ModifiyDonateHistory extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "organization_id", nullable = false)
	private OrganizationEntity organization;

	@ManyToOne
	@JoinColumn(name = "donate_id", nullable = false)
	private Donate donate;

	private LocalDateTime modifyDate;

	public ModifiyDonateHistory(OrganizationEntity organization, Donate donate, LocalDateTime modifyDate) {
		this.organization = organization;
		this.donate = donate;
		this.modifyDate = modifyDate;
	}

	public ModifiyDonateHistory() {
		super();
	}

}
