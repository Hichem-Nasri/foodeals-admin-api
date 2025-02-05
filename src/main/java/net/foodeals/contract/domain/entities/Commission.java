package net.foodeals.contract.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import net.foodeals.common.models.AbstractEntity;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "commissions")

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Commission extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private Float cash;

    private Float card;

    private Float deliveryAmount;

    private Float deliveryCommission;

    @OneToOne(mappedBy = "commission", cascade = CascadeType.ALL)
    private SolutionContract solutionContract;
}
