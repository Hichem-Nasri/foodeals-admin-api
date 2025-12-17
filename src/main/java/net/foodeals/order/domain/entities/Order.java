package net.foodeals.order.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter; // Ajouté pour Lombok setters
import lombok.NoArgsConstructor; // Ajouté pour le constructeur par défaut
import lombok.EqualsAndHashCode; // Ajouté pour éviter les boucles dans equals/hashCode
import lombok.ToString; // Ajouté pour éviter les boucles dans toString
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.delivery.domain.entities.Delivery;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.offer.domain.entities.Offer;
import net.foodeals.order.domain.enums.OrderStatus;
import net.foodeals.order.domain.enums.OrderSource; // Nouveau
import net.foodeals.order.domain.enums.OrderType;
import net.foodeals.user.domain.entities.User;
import net.foodeals.organizationEntity.domain.entities.SubEntity; // Nouveau
import net.foodeals.donate.domain.entities.Donate; // Nouveau

import java.time.LocalDateTime; // Changement de Date à LocalDateTime pour les dates
import java.util.ArrayList;
import java.util.Date; // Ancienne date, à retirer si LocalDateTime est adopté partout
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "orders")
@Getter
@Setter // Ajouté
@NoArgsConstructor // Ajouté
public class Order extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    // Champs de la version admin, conservés
    @Embedded
    private Price price;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.EAGER) // CHANGEMENT: FetchType.LAZY et pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User client;

    // Champ de la version admin, conservé
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY) // CHANGEMENT: FetchType.LAZY et pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Offer offer;

    @ManyToOne(fetch = FetchType.LAZY) // CHANGEMENT: FetchType.LAZY et pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Address shippingAddress;

    @OneToOne(fetch = FetchType.LAZY) // CHANGEMENT: FetchType.LAZY et pas de CascadeType.ALL
    // MappedBy doit être sur le côté non-propriétaire de la relation pour OneToOne bidirectionnel.
    // Si Order est propriétaire, Delivery ne doit pas avoir mappedBy="order".
    // Si Delivery est propriétaire, Order doit avoir mappedBy="order".
    // Basé sur l'exemple pro, il n'y a pas de mappedBy, donc Order est probablement propriétaire.
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY) // CHANGEMENT: FetchType.LAZY et pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Coupon coupon;

    @OneToOne(fetch = FetchType.LAZY) // CHANGEMENT: FetchType.LAZY et pas de CascadeType.ALL
    // Pas de mappedBy ici si Order est propriétaire de la transaction (si la transaction n'a pas de Order field).
    // Si Transaction a un champ 'order' et est propriétaire, alors Order.transaction doit être mappedBy="order".
    // La version admin avait mappedBy="order", je le conserve s'il est cohérent avec Transaction.java
    // Si Transaction.java n'a pas de mappedBy="order", alors c'est le propriétaire.
    // Pour simplifier et éviter les boucles:
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Transaction transaction;

    @Column(nullable = true)
    private boolean seen;

    // NOUVEAUX CHAMPS DE foodeals-pro-api

    @ManyToOne(fetch = FetchType.LAZY) // NOUVEAU: clientPro, pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SubEntity clientPro;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "cancellation_subject")
    private String cancellationSubject;

    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate; // CHANGEMENT: Utiliser LocalDateTime

    @Column(name = "attachment_path")
    private String attachmentPath;

    @Enumerated(EnumType.STRING)
    private OrderSource orderSource;

    @ManyToOne(fetch = FetchType.LAZY) // NOUVEAU: donate, pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Donate donate;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrackingStep> trackingSteps = new ArrayList();

    // CONSTRUCTEURS (Lombok @NoArgsConstructor et @AllArgsConstructor peuvent simplifier)
    // Garde le constructeur existant et ajoute un nouveau pour les champs de la version Pro

    public Order(Price price, OrderType type, OrderStatus status, User client, Offer offer, Integer quantity) {
        this.price = price;
        this.type = type;
        this.status = status;
        this.client = client;
        this.offer = offer;
        this.quantity = quantity;
    }

    // Constructeur étendu pour inclure tous les nouveaux champs de la version Pro
    public Order(Price price, OrderType type, OrderStatus status, User client, Offer offer, Integer quantity,
                 Address shippingAddress, Delivery delivery, Coupon coupon, Transaction transaction, boolean seen,
                 SubEntity clientPro, String cancellationReason, String cancellationSubject,
                 LocalDateTime cancellationDate, String attachmentPath, OrderSource orderSource, Donate donate) {
        this(price, type, status, client, offer, quantity); // Appelle le constructeur de base
        this.shippingAddress = shippingAddress;
        this.delivery = delivery;
        this.coupon = coupon;
        this.transaction = transaction;
        this.seen = seen;
        this.clientPro = clientPro;
        this.cancellationReason = cancellationReason;
        this.cancellationSubject = cancellationSubject;
        this.cancellationDate = cancellationDate;
        this.attachmentPath = attachmentPath;
        this.orderSource = orderSource;
        this.donate = donate;
    }


  

    // Les getters et setters sont générés par Lombok @Getter et @Setter.
    // Les méthodes "fluides" existantes peuvent être conservées si elles sont utilisées pour le chaînage.
    public Order setPrice(Price price) {
        this.price = price;
        return this;
    }

    public Order setOrderType(OrderType orderType) {
        this.type = orderType;
        return this;
    }

    public Order setStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
        return this;
    }

    public Order setShippingAddress(Address address) {
        this.shippingAddress = address;
        return this;
    }

    public Order setClient(User client) {
        this.client = client;
        return this;
    }

    public Order setOffer(Offer offer) {
        this.offer = offer;
        return this;
    }

    public Order setDelivery(Delivery delivery) {
        this.delivery = delivery;
        return this;
    }

    public Order setCoupon(Coupon coupon) {
        this.coupon = coupon;
        return this;
    }

    public Order setTransaction(Transaction transactions) {
        this.transaction = transactions;
        return this;
    }

    public Order setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    // NOUVEAUX SETTERS POUR LES CHAMPS DE foodeals-pro-api
    public Order setClientPro(SubEntity clientPro) {
        this.clientPro = clientPro;
        return this;
    }

    public Order setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
        return this;
    }

    public Order setCancellationSubject(String cancellationSubject) {
        this.cancellationSubject = cancellationSubject;
        return this;
    }

    public Order setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
        return this;
    }

    public Order setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
        return this;
    }

    public Order setOrderSource(OrderSource orderSource) {
        this.orderSource = orderSource;
        return this;
    }

    public Order setDonate(Donate donate) {
        this.donate = donate;
        return this;
    }
    
    
    
   
}