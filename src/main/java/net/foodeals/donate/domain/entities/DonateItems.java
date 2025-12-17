package net.foodeals.donate.domain.entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.product.domain.entities.Product;

@Entity
@Table(name = "donates_items")
@Getter
@Setter
public class DonateItems extends AbstractEntity<UUID>{
	
	
	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Product>products ;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Donate donate ;

}
