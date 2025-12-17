package net.foodeals.crm.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.foodeals.crm.domain.entities.CrmDemandeDocument;

import java.util.UUID;

public interface CrmDemandeDocumentRepository extends JpaRepository<CrmDemandeDocument, UUID> {}
