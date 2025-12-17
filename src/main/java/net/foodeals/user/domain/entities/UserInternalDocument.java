package net.foodeals.user.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "user_internal_documents")
@Getter
@Setter
@NoArgsConstructor
public class UserInternalDocument extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String path;
    private String name;

}
