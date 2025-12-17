package net.foodeals.crm.domain.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
@Entity @Table(name = "crm_demande_history")
@Getter @Setter
public class CrmDemandeHistory extends AbstractEntity<UUID>  {
	
	@Id
	@UuidGenerator private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="demande_id")
    private CrmDemande demande;

    private String action;       // created | updated | status_changed | document_added
    private String performedBy;  // user id / system
    private OffsetDateTime timestamp;
    @Column(columnDefinition="text")
    private String details;

}
