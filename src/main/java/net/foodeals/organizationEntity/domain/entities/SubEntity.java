package net.foodeals.organizationEntity.domain.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import net.foodeals.common.entities.DeletionReason;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Coordinates;
import net.foodeals.contract.domain.entities.Contract;
import net.foodeals.contract.domain.entities.Subscription;
import net.foodeals.location.domain.entities.Address;
import net.foodeals.notification.domain.entity.Notification;
import net.foodeals.offer.domain.entities.DonorInfo;
import net.foodeals.offer.domain.entities.PublisherI;
import net.foodeals.offer.domain.entities.ReceiverInfo;
import net.foodeals.offer.domain.enums.DonationReceiverType;
import net.foodeals.offer.domain.enums.DonorType;
import net.foodeals.offer.domain.enums.PublisherType;
import net.foodeals.order.domain.entities.Coupon;
import net.foodeals.organizationEntity.domain.entities.enums.EntityType;
import net.foodeals.organizationEntity.domain.entities.enums.SubEntityType;
import net.foodeals.payment.domain.entities.Enum.PartnerType;
import net.foodeals.payment.domain.entities.PartnerCommissions;
import net.foodeals.payment.domain.entities.PartnerI;
import net.foodeals.payment.domain.entities.Payment;
import net.foodeals.user.domain.entities.User;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Table(name = "sub_entities")

@Getter
@Setter
@Builder
@AllArgsConstructor
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

    @Embedded
    private Coordinates coordinates;

    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL)
    private OrganizationEntity organizationEntity;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Contact> contacts = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Activity> activities = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "subEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "address_id", unique = false)
    private Address address;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeletionReason> deletionReasons = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "subEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> coupons;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartnerCommissions> commissions = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions = new ArrayList<>();

    @ManyToOne
    private Contract contract;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Solution> solutions = new HashSet<>();

    public SubEntity() {
    }

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
        };    }

    @Override
    @Transactional
    public boolean subscriptionPayedBySubEntities() {
        return this.contract.isSubscriptionPayedBySubEntities();
    }

    @Override
    public boolean singleSubscription() {
        return this.contract.isSingleSubscription();
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
        return switch (this.type) {
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
        return this.organizationEntity.getContract().isCommissionPayedBySubEntities();
    }

    @Override
    @Transactional
    public String getCity() {
        return this.address.getRegion().getCity().getName();
    }
}



// subscription -> sub pay -> deadlines 