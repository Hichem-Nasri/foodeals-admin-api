package net.foodeals.user.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "absences")
@Getter
@Setter
@NoArgsConstructor
public class Absence extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;

    private String justificationPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validate_by_id")
    private User validatedBy;

}
