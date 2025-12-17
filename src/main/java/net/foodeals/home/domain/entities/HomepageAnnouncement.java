package net.foodeals.home.domain.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "homepage_announcements")
@Getter @Setter
public class HomepageAnnouncement extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String message;
    private String type; // e.g. "info", "warning", "success"

    private boolean isActive;
    private Instant expiresAt;

}

