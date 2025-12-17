-- V4__create_donate_related_tables.sql

-- Création de la table 'donates'
CREATE TABLE donates (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    version BIGINT,
    donor_id UUID,
    receiver_id UUID,
    modality_type VARCHAR(255),
    modality_paiement VARCHAR(255),
    donate_status VARCHAR(255),
    donation_type VARCHAR(255),
    donation_unity VARCHAR(255),
    donate_delivry_method VARCHAR(255),
    delivery_fee BIGINT,
    motif TEXT,
    reason TEXT,
    attechement_file_path VARCHAR(255),

    -- Clés étrangères vers organization_entities
    CONSTRAINT fk_donates_donor FOREIGN KEY (donor_id) REFERENCES organization_entities(id),
    CONSTRAINT fk_donates_receiver FOREIGN KEY (receiver_id) REFERENCES organization_entities(id)
);

-- Création de la table 'donate_items' (correction du nom de table 'donates_items' -> 'donate_items')
CREATE TABLE donate_items (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    version BIGINT,
    donate_id UUID UNIQUE, -- OneToOne relation, ensure unique FK
    CONSTRAINT fk_donate_items_donate FOREIGN KEY (donate_id) REFERENCES donates(id)
);

-- Table de jointure pour ManyToMany entre 'donate_items' et 'products'
-- Créée automatiquement par JPA si vous n'avez pas de `@JoinTable`
-- Mais si vous l'avez spécifiée avec @JoinTable, vous devez la créer ici
CREATE TABLE donate_items_products (
    donate_item_id UUID NOT NULL,
    product_id UUID NOT NULL,
    PRIMARY KEY (donate_item_id, product_id),
    CONSTRAINT fk_dip_donate_item FOREIGN KEY (donate_item_id) REFERENCES donate_items(id),
    CONSTRAINT fk_dip_product FOREIGN KEY (product_id) REFERENCES products(id)
);


-- Création de la table 'modify_donate_history'
CREATE TABLE modify_donate_history (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    version BIGINT,
    organization_id UUID NOT NULL,
    donate_id UUID NOT NULL,
    modify_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_mdh_organization FOREIGN KEY (organization_id) REFERENCES organization_entities(id),
    CONSTRAINT fk_mdh_donate FOREIGN KEY (donate_id) REFERENCES donates(id)
);

-- Création de la table 'relaunch_donate_history'
CREATE TABLE relaunch_donate_history (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    version BIGINT,
    organization_id UUID NOT NULL,
    donate_id UUID NOT NULL,
    relaunch_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_rdh_organization FOREIGN KEY (organization_id) REFERENCES organization_entities(id),
    CONSTRAINT fk_rdh_donate FOREIGN KEY (donate_id) REFERENCES donates(id)
);


-- MISE À JOUR DU SCRIPT V3 (OU CRÉATION D'UN NOUVEAU SI V3 EST DÉJÀ APPLIQUÉ SANS LA FK)
-- Si votre script V3 n'a PAS ENCORE été appliqué, vous pouvez ajouter cette ligne à V3.
-- Si V3 a déjà été appliqué, vous devrez créer un V5 ou un script ultérieur juste pour cette FK.
-- Pour la cohérence, si V3 n'est pas encore exécuté, mettez ceci dans V3. Sinon, faites un V5.
-- Puisque vous venez de me donner l'entité Donate, nous allons assumer que V3 n'a pas été appliqué.
-- Veuillez vous référer à votre fichier V3__add_order_columns_and_fk.sql
-- et décommenter la ligne pour ajouter la clé étrangère ou l'ajouter si elle n'existe pas.

-- Exemple de modification pour V3__add_order_columns_and_fk.sql:
-- ALTER TABLE orders
-- ADD CONSTRAINT fk_order_donate FOREIGN KEY (donate_id) REFERENCES donates(id);
-- Cette ligne doit être exécutée APRÈS la création de la table 'donates'.
-- Si V3 ajoute le colonne et V4 crée la table 'donates', alors il faudrait un V5 pour la FK.
-- Pour simplifier, nous mettons cette FK dans V4 ici, en assumant que la colonne 'donate_id' existe déjà dans 'orders'.
ALTER TABLE orders
ADD CONSTRAINT fk_order_donate FOREIGN KEY (donate_id) REFERENCES donates(id);

-- Vous devrez aussi penser à la table 'open_time' si elle n'est pas déjà dans V1.
-- Si elle n'y est pas, vous auriez besoin d'un script pour la créer aussi.
-- Votre snippet de V1 ne l'indique pas directement, mais c'est une entité commune.