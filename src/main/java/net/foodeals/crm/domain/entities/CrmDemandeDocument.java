package net.foodeals.crm.domain.entities;



import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

@Entity @Table(name = "crm_demande_documents")
@Getter @Setter
public class CrmDemandeDocument extends AbstractEntity<UUID>  {
	@Id
	@UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="demande_id")
    private CrmDemande demande;

    private String name;
    @Column(columnDefinition="text") private String url;
    private String type;
}
