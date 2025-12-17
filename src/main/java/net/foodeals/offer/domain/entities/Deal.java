package net.foodeals.offer.domain.entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor; // Ajouté pour le constructeur par défaut
import lombok.Setter; // Ajouté pour les setters
import lombok.ToString;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.domain.enums.Category;       // Nouveau
import net.foodeals.offer.domain.enums.DealStatus;     // Nouveau
import net.foodeals.offer.domain.enums.OfferType;
import net.foodeals.offer.domain.enums.PublishAs;      // Nouveau
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.Supplement;

@Entity
@Table(name = "deals")
@Getter
@Setter // Ajouté pour les setters automatiques
@NoArgsConstructor // Ajouté pour le constructeur par défaut
public class Deal extends AbstractEntity<UUID> implements IOfferChoice {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private Price price;

    private Integer quantity;

    // CHANGEMENT: FetchType.LAZY pour la performance, et pas de cascade ALL
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;

    // NOUVEAU CHAMP DE foodeals-pro-api
    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Supplement> supplements;

    // NOUVEAUX CHAMPS DE foodeals-pro-api
    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // Généralement NOT NULL
    private PublishAs publishAs;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // Généralement NOT NULL
    private Category category;

    @Override
    public final OfferType getOfferType() {
        return OfferType.DEAL;
    }

    // NOUVEAU CHAMP DE foodeals-pro-api
    // CHANGEMENT: Relation ManyToOne vers Offer, mais retire CascadeType.ALL
    @ManyToOne(fetch = FetchType.LAZY) // CHANGEMENT: LAZY et pas de cascade ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Offer offer;

    // NOUVEAUX CHAMPS DE foodeals-pro-api
    @Enumerated(EnumType.STRING)
    private DealStatus dealStatus;

    private String reason;
    private String motif;

    // Constructeur complet de la version Pro
    public Deal(Price price, int quantity, Product product, List<Supplement> supplements, PublishAs publishAs,
                Category category, Offer offer) {
        this.price = price;
        this.quantity = quantity;
        this.product = product;
        this.supplements = supplements;
        this.publishAs = publishAs;
        this.category = category;
        this.offer = offer;
    }

    public static Deal create(Price price, Integer quantity, Product product, List<Supplement> supplements,
                              PublishAs publishAs, Category category, Offer offer) {
        return new Deal(price, quantity, product, supplements, publishAs, category, offer);
    }

    // Setters spécifiques si besoin de retour "this"
    public Deal setPrice(Price price) {
        this.price = price;
        return this;
    }

    public Deal setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Deal setProduct(Product product) {
        this.product = product;
        return this;
    }

    // NOUVEAUX SETTERS
    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setDealStatus(DealStatus dealStatus) {
        this.dealStatus = dealStatus;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}