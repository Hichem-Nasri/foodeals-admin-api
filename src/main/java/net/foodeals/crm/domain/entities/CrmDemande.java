package net.foodeals.crm.domain.entities;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.crm.domain.entities.enums.DemandeStatus;
import net.foodeals.crm.domain.entities.enums.DemandeType;

import java.time.OffsetDateTime;
import java.util.*;

import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "crm_demandes")
@Getter
@Setter
public class CrmDemande extends AbstractEntity<UUID> {
	@Id
	@UuidGenerator
	private UUID id;

	@Enumerated(EnumType.STRING)
	private DemandeType type;

	@Column(name = "company_name", nullable = false)
	private String companyName;

	@ElementCollection
	@CollectionTable(name = "crm_demande_activities", joinColumns = @JoinColumn(name = "demande_id"))
	@Column(name = "activity")
	private Set<String> activity = new LinkedHashSet<>();

	private String country;
	private String city;

	private OffsetDateTime date; // optionnel

	@Column(name = "responsable")
	private String responsable; // JSON "respansable"

	private String address;
	private String email;
	private String phone;

	@Enumerated(EnumType.STRING)
	private DemandeStatus status = DemandeStatus.PENDING;

	@Column(columnDefinition = "text")
	private String notes;

	

	@OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CrmDemandeDocument> documents = new ArrayList<>();

	@OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CrmDemandeHistory> history = new ArrayList<>();
	
	public void addDocument(CrmDemandeDocument d) {
	    d.setDemande(this);
	    this.documents.add(d);
	}
	public void addHistory(CrmDemandeHistory h) {
	    h.setDemande(this);
	    this.history.add(h);
	}
}
