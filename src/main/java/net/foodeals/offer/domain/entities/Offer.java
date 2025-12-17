package net.foodeals.offer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.offer.domain.enums.ModalityPaiement; // Nouveau
import net.foodeals.offer.domain.enums.ModalityType;     // Nouveau
import net.foodeals.offer.domain.valueObject.Offerable;
import net.foodeals.order.domain.entities.Order;
import net.foodeals.organizationEntity.domain.entities.Activity;
import net.foodeals.organizationEntity.domain.entities.OrganizationEntity;
import net.foodeals.organizationEntity.domain.entities.SubEntity;

import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "offers")
@Getter
@Setter
public class Offer extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String imagePath; // Présent dans l'admin original, conservé

    private String title;     // Présent dans l'admin original, conservé
    
    @ManyToOne(fetch = FetchType.LAZY) // LAZY est généralement préférable pour ManyToOne
    @JoinColumn(name = "sub_entity_id") // Ceci créera une colonne 'sub_entity_id' dans la table 'offers'
    private SubEntity subEntity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Price price;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "sale_price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "sale_price_currency"))
    })
    private Price salePrice;

    private Integer reduction;

    private String barcode;

    // CHANGEMENT: FetchType.LAZY pour la performance, même en admin
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpenTime> openTime = new ArrayList<>(); // Initialiser la liste

    // CHANGEMENT: CascadeType.ALL retiré pour ManyToOne, sauf si vous gérez le cycle de vie de Activity directement ici.
    // Il est plus sûr de ne pas cascader la suppression ou la persistance d'une Activity via Offer.
    @ManyToOne(fetch = FetchType.LAZY) // CHANGEMENT: LAZY
    private Activity activity;

    @Embedded
    private Offerable offerable;

    // CHANGEMENT: FetchType.LAZY pour la performance
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>(); // Initialiser la liste

    @Transient
    private IOfferChoice offerChoice;

    @Embedded
    private PublisherInfo publisherInfo;

    @Transient
    private PublisherI publisherI;

    // NOUVEAUX CHAMPS DE foodeals-pro-api
    @Enumerated(EnumType.STRING) // Ajout de l'annotation
    private ModalityType modalityType;

    private Long deliveryFee;

    @Enumerated(EnumType.STRING) // Ajout de l'annotation
    private ModalityPaiement modalityPaiement;
    
    // MAINTENANT AVEC LES JOINS EXPLICITES ET FETCHTYPE.LAZY POUR LES NOUVELLES COLONNES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id") // Mappe à la nouvelle colonne 'box_id' dans la base de données
    private Box box ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id") // Mappe à la nouvelle colonne 'deal_id' dans la base de données
    private Deal deal ;

    @ManyToOne(fetch = FetchType.LAZY) // Ajout de FetchType.LAZY pour la performance
    @JoinColumn(name = "organization_entity_id") // S'assure que le nom de colonne est 'organization_entity_id'
    private OrganizationEntity organizationEntity;

    public Offer() {
        // Initialisation par défaut si nécessaire
    }

    // Constructeur complet (pour la méthode create statique)
    public Offer(String imagePath, String title, Price price, Price salePrice, Integer reduction, String barcode,
                 List<OpenTime> openTime, Activity activity, Offerable offerable,
                 ModalityType modalityType, ModalityPaiement modalityPaiement, Long deliveryFee) {
        this.imagePath = imagePath;
        this.title = title;
        this.price = price;
        this.salePrice = salePrice;
        this.reduction = reduction;
        this.barcode = barcode;
        this.openTime = openTime;
        this.activity = activity;
        this.offerable = offerable;
        this.modalityType = modalityType;
        this.modalityPaiement = modalityPaiement;
        this.deliveryFee = deliveryFee;
        // orders, offerChoice, publisherInfo, publisherI sont gérés séparément ou par défaut
    }

    public static Offer create(String imagePath, String title, Price price, Price salePrice, Integer reduction, String barcode,
                               List<OpenTime> openTime, Activity activity, Offerable offerable,
                               ModalityType modalityType, ModalityPaiement modalityPaiement, Long deliveryFee) {
        return new Offer(imagePath, title, price, salePrice, reduction, barcode,
                         openTime, activity, offerable, modalityType, modalityPaiement, deliveryFee);
    }

    // Setters (Lombok @Setter gère la plupart, mais les fluides sont spécifiques)
    public Offer setPrice(Price price) {
        this.price = price;
        return this;
    }

    public Offer setSalePrice(Price salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public Offer setReduction(Integer reduction) {
        this.reduction = reduction;
        return this;
    }

    public Offer setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public Offer setOpenTime(List<OpenTime> openTime) {
        this.openTime = openTime;
        return this;
    }

    public Offer setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public Offer setOfferable(Offerable offerable) {
        this.offerable = offerable;
        return this;
    }

    public Offer setOfferChoice(IOfferChoice offerChoice) {
        this.offerChoice = offerChoice;
        return this;
    }

    // NOUVEAUX SETTERS POUR LES NOUVEAUX CHAMPS
    public Offer setModalityType(ModalityType modalityType) {
        this.modalityType = modalityType;
        return this;
    }

    public Offer setDeliveryFee(Long deliveryFee) {
        this.deliveryFee = deliveryFee;
        return this;
    }

    public Offer setModalityPaiement(ModalityPaiement modalityPaiement) {
        this.modalityPaiement = modalityPaiement;
        return this;
    }
}