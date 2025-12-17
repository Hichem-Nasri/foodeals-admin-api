package net.foodeals.organizationEntity.domain.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*; // Assurez-vous d'avoir toutes les importations Lombok
import net.foodeals.common.entities.DeletionReason;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.contract.domain.entities.Contract;
import net.foodeals.contract.domain.entities.Subscription;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.offer.domain.entities.DonorInfo;
import net.foodeals.offer.domain.entities.Offer; // Ajouté pour la relation offers
import net.foodeals.offer.domain.entities.PublisherI;
import net.foodeals.offer.domain.entities.ReceiverInfo;
import net.foodeals.offer.domain.enums.DonationReceiverType;
import net.foodeals.offer.domain.enums.DonorType;
import net.foodeals.offer.domain.enums.PublisherType;
import net.foodeals.order.domain.entities.Coupon;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType; // Non utilisé dans le code fourni, peut-être pour OrganizationEntity
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityStatus; // Ajouté
import net.foodeals.payment.domain.entities.Enum.PartnerType;
import net.foodeals.payment.domain.entities.PartnerCommissions;
import net.foodeals.payment.domain.entities.PartnerI;
import net.foodeals.payment.domain.entities.Payment; // Non utilisé dans le code fourni
import net.foodeals.user.domain.entities.User;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Table(name = "sub_entities")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // Ajouté pour JPA
public class SubEntity extends AbstractEntity<UUID> implements DonorInfo, ReceiverInfo, PublisherI, PartnerI {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SubEntityType type;

    @Column(name = "avatar_path")
    private String avatarPath;

    @Column(name = "cover_path")
    private String coverPath;

    @Column(name = "iframe", length = 800) // Ajouté depuis l'API Pro
    private String iFrame;

    @Embedded
    private Coordinates coordinates;

    @OneToOne(fetch = FetchType.LAZY) // CHANGEMENT: Pas de CascadeType.ALL, LAZY
    @JoinColumn(name = "manager_id") // Clé étrangère pour le manager
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User manager; // Ajouté depuis l'API Pro

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // CHANGEMENT: LAZY, optional=false, Pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrganizationEntity organizationEntity;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // Ajouté orphanRemoval = true
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Contact> contacts = new ArrayList<>();

    @Builder.Default // Ajouté
    @ManyToMany(fetch = FetchType.LAZY) // CHANGEMENT: LAZY, Pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Activity> activities = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "subEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // CHANGEMENT: Assuré FetchType.LAZY
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY) // CHANGEMENT: LAZY
    @JoinColumn(name = "address_id", nullable = true, unique = true) // CHANGEMENT: nullable=true, unique=true
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Address address;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DeletionReason> deletionReasons = new ArrayList<>();

   

    @OneToMany(mappedBy = "subEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // CHANGEMENT: Assuré FetchType.LAZY
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Coupon> coupons;

    @Builder.Default // Ajouté
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PartnerCommissions> commissions = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Subscription> subscriptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY) // CHANGEMENT: LAZY, Pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Contract contract;

    @Builder.Default // Ajouté
    @ManyToMany(fetch = FetchType.LAZY) // CHANGEMENT: LAZY, Pas de CascadeType.ALL
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Solution> solutions = new HashSet<>(); // CHANGEMENT: Set pour consistance avec activities

    @Builder.Default // Ajouté
    @OneToMany(mappedBy = "subEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // CHANGEMENT: Ajouté mappedBy
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Offer> offers = new ArrayList<>(); // Ajouté depuis l'API Pro

    private String email; // Ajouté depuis l'API Pro
    private String phone; // Ajouté depuis l'API Pro
    private String reason; // Ajouté depuis l'API Pro
    private String motif; // Ajouté depuis l'API Pro

    @Enumerated(EnumType.STRING)
    private SubEntityStatus subEntityStatus; // Ajouté depuis l'API Pro

    // Implémentations des interfaces
    @Override
    public PartnerType getPartnerType() {
        return PartnerType.SUB_ENTITY;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public PublisherType getPublisherType() {
        return switch (this.type) {
            case PARTNER_SB -> PublisherType.PARTNER_SB;
            case FOOD_BANK_SB -> PublisherType.FOOD_BANK_SB;
            default -> null;
        };
    }

    @Override
    @Transactional // Gardé pour la logique métier
    public boolean subscriptionPayedBySubEntities() {
        return this.contract != null && this.contract.isSubscriptionPayedBySubEntities(); // Ajouté null check
    }

    @Override
    public boolean singleSubscription() {
        return this.contract != null && this.contract.isSingleSubscription(); // Ajouté null check
    }

    @Override
    public DonationReceiverType getReceiverType() {
        return switch (this.type) {
            case FOOD_BANK_ASSOCIATION -> DonationReceiverType.FOOD_BANK_ASSOCIATION;
            case FOOD_BANK_SB -> DonationReceiverType.FOOD_BANK_SB;
            default -> null;
        };
    }

    @Override
    public DonorType getDonorType() {
        return switch (type) {
            case PARTNER_SB -> DonorType.PARTNER_SB;
            case FOOD_BANK_SB -> DonorType.FOOD_BANK_SB;
            default -> null;
        };
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getAvatarPath() {
        return this.avatarPath;
    }

    @Override
    public boolean commissionPayedBySubEntities() {
        // Ajouté null checks pour éviter NullPointerException si organizationEntity ou son contrat sont nuls
        return this.organizationEntity != null && this.organizationEntity.getContract() != null &&
               this.organizationEntity.getContract().isCommissionPayedBySubEntities();
    }

    @Override
    @Transactional // Gardé pour la logique métier
    public String getCity() {
        // Ajouté null check pour l'adresse
        return (this.address != null && this.address.getRegion() != null && this.address.getRegion().getCity() != null) ?
                this.address.getRegion().getCity().getName() : null; // Retourne null si l'adresse ou la ville est manquante
    }
}