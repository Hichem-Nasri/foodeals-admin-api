package net.foodeals.user.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

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

@Getter
@Setter
@Entity
@Table(name = "user_permissions")
public class UserPermission extends AbstractEntity<UUID> {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	private String permission;

	private boolean granted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	

	

}
