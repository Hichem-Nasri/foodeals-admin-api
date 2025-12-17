package net.foodeals.offer.domain.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor; // Ajouté pour le constructeur par défaut
import lombok.Setter;
import lombok.ToString;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.offer.domain.enums.BoxStatus;   // Nouveau
import net.foodeals.offer.domain.enums.BoxType;
import net.foodeals.offer.domain.enums.Category;   // Nouveau
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.enums.PublishAs;  // Nouveau
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "boxes")
@Getter
@Setter
@NoArgsConstructor // Ajouté pour le constructeur par défaut
public class Box extends AbstractEntity<UUID> implements IOfferChoice {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    // NOUVEAUX CHAMPS DE foodeals-pro-api
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private BoxType type;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<BoxItem> boxItems;

    // CHANGEMENT: Relation ManyToOne vers Offer, mais retire CascadeType.ALL
    @ManyToOne(fetch = FetchType.LAZY) // CHANGEMENT: LAZY et pas de cascade ALL
    @ToString.Exclude // Éviter les boucles infinies avec ToString
    @EqualsAndHashCode.Exclude // Éviter les boucles infinies avec EqualsAndHashCode
    private Offer offer;

    // NOUVEAUX CHAMPS DE foodeals-pro-api
    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // Généralement ces champs sont NOT NULL
    private PublishAs publishAs;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // Généralement ces champs sont NOT NULL
    private Category category;

    @Enumerated(EnumType.STRING)
    private BoxStatus boxStatus;

    private String reason;
    private String motif;

    @Override
    public OfferType getOfferType() {
        return OfferType.BOX;
    }

    // Constructeur pour l'admin, peut être plus simple
    // Pour Lombok @Builder, il faudra ajouter `@Builder` à la classe.
    // J'ai gardé les constructeurs présents dans vos exemples
    public Box(BoxType type, List<BoxItem> boxItems) {
        this.type = type;
        this.boxItems = boxItems;
    }

    public static Box create(BoxType type, List<BoxItem> boxItems) {
        return new Box(type, boxItems);
    }

    // Constructeur complet de la version Pro, utile pour des créations riches
    public Box(String title, String description, BoxType type, List<BoxItem> boxItems,
               PublishAs publishAs, Category category, Offer offer) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.boxItems = boxItems;
        this.offer = offer;
        this.publishAs = publishAs;
        this.category = category;
    }

    public static Box create(String title, String description, BoxType type, List<BoxItem> boxItems,
                             PublishAs publishAs, Category category, Offer offer) {
        return new Box(title, description, type, boxItems, publishAs, category, offer);
    }
}