-- V3__add_box_and_deal_ids_to_offers.sql

-- Ajoute la colonne box_id à la table offers
ALTER TABLE public.offers
ADD COLUMN box_id UUID;

-- Ajoute la colonne deal_id à la table offers
ALTER TABLE public.offers
ADD COLUMN deal_id UUID;

-- AJOUTÉ SELON VOTRE DEMANDE EXPLICITE :
-- ATTENTION : Cette ligne (ADD COLUMN organization_entity_id) existe déjà dans V1.
-- Elle provoquera une erreur "column "organization_entity_id" already exists"
-- si ce script est exécuté sur une base de données où V1 a déjà été appliqué.
ALTER TABLE public.offers
ADD COLUMN organization_entity_id UUID;

-- Ajoute la contrainte de clé étrangère pour box_id
-- Assurez-vous que la table 'boxes' existe et que sa PK 'id' est de type UUID.
ALTER TABLE public.offers
ADD CONSTRAINT fk_offers_box_id
FOREIGN KEY (box_id) REFERENCES public.boxes(id);

-- Ajoute la contrainte de clé étrangère pour deal_id
-- Assurez-vous que la table 'deals' existe et que sa PK 'id' est de type UUID.
ALTER TABLE public.offers
ADD CONSTRAINT fk_offers_deal_id
FOREIGN KEY (deal_id) REFERENCES public.deals(id);

-- AJOUTÉ SELON VOTRE DEMANDE EXPLICITE :
-- ATTENTION : Cette ligne (ADD CONSTRAINT fk_offers_organization_entity_id) peut provoquer une erreur
-- si une contrainte similaire existe déjà (elle est souvent ajoutée avec V1 ou par JPA).
ALTER TABLE public.offers
ADD CONSTRAINT fk_offers_organization_entity_id
FOREIGN KEY (organization_entity_id) REFERENCES public.organization_entities(id);

-- Note importante :
-- Les colonnes 'offerable_id' et 'offerable_type' sont désormais potentiellement redondantes
-- si 'Box' et 'Deal' sont les seuls types d'éléments "offrables" et que vous utilisez
-- uniquement les clés étrangères directes 'box_id' et 'deal_id'.
-- Si vous êtes certain de ne plus en avoir besoin pour Box/Deal, vous pourriez envisager de les supprimer
-- dans une migration ultérieure après avoir migré les données si nécessaire.
-- ALTER TABLE public.offers DROP COLUMN offerable_id;
-- ALTER TABLE public.offers DROP COLUMN offerable_type;