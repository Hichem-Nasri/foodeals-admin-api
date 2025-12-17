package net.foodeals.home.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "homepage_hero")
public class HomepageHero extends AbstractEntity<UUID>{
	@Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String title;
    private String subtitle;
    private String backgroundImage;
    private String ctaText;
    private String ctaLink;
    private boolean isActive;
 
}