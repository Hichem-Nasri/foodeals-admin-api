package net.foodeals.user.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "user_personal_documents")
@Getter
@Setter
@NoArgsConstructor
public class UserPersonalDocument extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String path;
    private String name;
    
    private LocalDateTime uploaded_at;

}

