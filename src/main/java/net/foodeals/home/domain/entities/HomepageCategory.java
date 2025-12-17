package net.foodeals.home.domain.entities;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "homepage_categories")
@Getter @Setter
public class HomepageCategory extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String icon;
    private boolean isActive;
    private Integer classement;
    private Integer dealCount = 0;
}
