package net.foodeals.home.domain.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "homepage_testimonials")
@Getter @Setter
public class HomepageTestimonial extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    private String customerName;
    private int rating;
    private String comment;
    private String avatar;
    private boolean isActive;
    private Integer classement;

}
