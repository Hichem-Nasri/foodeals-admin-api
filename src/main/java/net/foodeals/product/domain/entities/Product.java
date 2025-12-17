package net.foodeals.product.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.offer.domain.entities.BoxItem;
import net.foodeals.offer.domain.entities.Deal;
import net.foodeals.organizationEntity.domain.entities.SubEntity;
import net.foodeals.organizationEntity.domain.entities.Solution;
import net.foodeals.product.domain.enums.CreatedBy;
import net.foodeals.product.domain.enums.ProductType;
import net.foodeals.user.domain.entities.User;
import net.foodeals.common.valueOjects.Price;
import org.hibernate.annotations.UuidGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;
    private String slug;
    private String description;
    private String title;
    private String barcode;
    private String brand;

    // Champs communs
    @Column(name = "product_type")
    private ProductType type;

    @Embedded
    private Price price;

    // Prix avec réduction (ajouté depuis foodeals-pro)
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "price_after_discount_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "price_after_discount_currency"))
    })
    private Price priceAfterDiscount;

    @Column(name = "product_image_type")
    private String productImagePath;

    // Relations foodeals-admin
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Deal> deals;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BoxItem> boxItems;

    private Date endDate;              // admin
    private Date expirationDate;       // pro

    private Integer quantity;          // pro

    @Enumerated(EnumType.STRING)
    private CreatedBy createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id")
    private ProductSubCategory subcategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;        // admin

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_sub_entity_id")
    private SubEntity createdBySubEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rayon_id")
    private Rayon rayon;               // admin

    @ManyToOne
    private Solution solution;         // pro

    @ManyToOne(cascade = CascadeType.ALL)
    private PaymentMethodProduct paymentMethodProduct; // pro

    @ManyToOne(cascade = CascadeType.ALL)
    private DeliveryMethod deliveryMethod;             // pro

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<PickupCondition> pickupConditions;    // pro

    private String reason;
    private String motif;
}
