package net.foodeals.offer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.product.domain.entities.Product;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "box_items")
@Getter
@Setter
@NoArgsConstructor
public class BoxItem extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Embedded
    private Price price;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id")
    private Box box;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    public BoxItem(Price price, Integer quantity, Box box, Product product) {
        this.price = price;
        this.quantity = quantity;
        this.box = box;
        this.product = product;
    }

    public static BoxItem create(Price price, Integer quantity, Box box, Product product) {
        return new BoxItem(price, quantity, box, product);
    }
}
