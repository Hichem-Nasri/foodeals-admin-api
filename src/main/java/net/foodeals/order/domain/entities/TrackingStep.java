package net.foodeals.order.domain.entities;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name="tracking_step")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TrackingStep extends AbstractEntity<UUID>{
	

	

	@Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String status;

    private String description;

    private Instant timestamp;
    
    private  Float latitude;
    
    private Float longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

	public TrackingStep(Order order,String status, String description, Instant timestamp) {
		super();
		this.status = status;
		this.description = description;
		this.timestamp = timestamp;
		this.order = order;
	}

    
    

}
