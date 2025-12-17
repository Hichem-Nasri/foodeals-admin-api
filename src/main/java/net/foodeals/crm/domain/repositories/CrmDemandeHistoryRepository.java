package net.foodeals.crm.domain.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import net.foodeals.crm.domain.entities.CrmDemandeHistory;

import java.util.UUID;

public interface CrmDemandeHistoryRepository extends JpaRepository<CrmDemandeHistory, UUID> {}
