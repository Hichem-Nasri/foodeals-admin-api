package net.foodeals.homepage.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "homepage_content")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageContentEntity extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Lob
    @Column(name = "hero_json")
    private String heroJson;

    @Lob
    @Column(name = "testimonials_json")
    private String testimonialsJson;

    @Lob
    @Column(name = "announcements_json")
    private String announcementsJson;

    @Lob
    @Column(name = "categories_json")
    private String categoriesJson;

    @Lob
    @Column(name = "featured_deals_json")
    private String featuredDealsJson;

    @Lob
    @Column(name = "content_sorting_json")
    private String contentSortingJson;

    @Override
    public UUID getId() {
        return id;
    }
}
