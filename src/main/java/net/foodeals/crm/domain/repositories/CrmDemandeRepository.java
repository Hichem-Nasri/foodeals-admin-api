package net.foodeals.crm.domain.repositories;




import org.springframework.data.jpa.repository.*;

import net.foodeals.crm.domain.entities.CrmDemande;

import java.util.UUID;

public interface CrmDemandeRepository extends JpaRepository<CrmDemande, UUID>, JpaSpecificationExecutor<CrmDemande> {}

