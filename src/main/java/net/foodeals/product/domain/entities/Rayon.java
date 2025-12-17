package net.foodeals.product.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.foodeals.common.contracts.BaseRepository;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.user.domain.entities.User;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rayon")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rayon extends AbstractEntity<UUID> {

    @Id
    @UuidGenerator
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "rayon")
    @JsonIgnore
    private List<User> users;

}
