-- V6__update_sub_entities_table.sql

-- Ajout des nouvelles colonnes à la table 'sub_entities'
ALTER TABLE sub_entities
ADD COLUMN IF NOT EXISTS iframe VARCHAR(800),
ADD COLUMN IF NOT EXISTS manager_id integer,
ADD COLUMN IF NOT EXISTS email VARCHAR(255),
ADD COLUMN IF NOT EXISTS phone VARCHAR(255),
ADD COLUMN IF NOT EXISTS reason TEXT,
ADD COLUMN IF NOT EXISTS motif TEXT,
ADD COLUMN IF NOT EXISTS sub_entity_status VARCHAR(255);

-- Ajout de la clé étrangère pour 'manager_id'
-- Assurez-vous que la table 'users' existe
ALTER TABLE sub_entities
ADD CONSTRAINT fk_sub_entity_manager FOREIGN KEY (manager_id) REFERENCES users(id);

-- Mise à jour de la contrainte unique pour 'address_id' si elle n'est pas déjà unique
-- D'abord, supprimez l'ancienne contrainte si elle existe et n'est pas unique
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_address_sub_entity' AND confrelid = (SELECT oid FROM pg_class WHERE relname = 'sub_entities')) THEN
        -- Trouvez le nom de la FK existante et supprimez-la si elle est sur address_id et non unique
        -- Attention: La FK peut avoir un nom généré par Hibernate (ex: fkaos1y7b2253c3v8l97s1h2w9b)
        -- Si vous n'êtes pas sûr, vous pouvez chercher dans votre schéma ou l'exécuter manuellement après l'avoir identifié.
        -- Pour l'exemple, nous allons directement ajouter la contrainte unique sur la colonne.
        -- Si une FK existe et n'est pas unique, cela peut nécessiter une étape de suppression/recréation de la FK.
    END IF;
END $$;

-- Ajouter une contrainte UNIQUE sur address_id si nécessaire
-- Note: Si l'adresse est NULLABLE, la contrainte UNIQUE autorise plusieurs NULLs.
ALTER TABLE sub_entities
ADD CONSTRAINT uc_sub_entity_address_id UNIQUE (address_id);

-- Ajout de la clé étrangère 'sub_entity_id' à la table 'offers'
-- Ceci suppose que vous avez déjà modifié votre entité Offer pour inclure le champ ManyToOne subEntity.
ALTER TABLE offers
ADD COLUMN IF NOT EXISTS sub_entity_id UUID;

ALTER TABLE offers
ADD CONSTRAINT fk_offer_sub_entity FOREIGN KEY (sub_entity_id) REFERENCES sub_entities(id);

-- Si vous avez une table de jointure pour ManyToMany entre sub_entities et solutions (sub_entities_solutions),
-- assurez-vous qu'elle est correctement définie et que les colonnes ont été créées.
-- Si cette table n'existe pas, JPA la créera automatiquement avec @ManyToMany, mais les cascades auront un comportement par défaut.
-- Si vous avez retiré CascadeType.ALL sur @ManyToMany, vous devrez gérer la persistance/suppression manuellement pour les solutions.