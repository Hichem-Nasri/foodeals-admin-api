-- V2__add_offer_box_deal_columns.sql

-- Ajout des colonnes à la table 'offers'
ALTER TABLE offers
ADD COLUMN modality_type VARCHAR(255),
ADD COLUMN delivery_fee BIGINT,
ADD COLUMN modality_paiement VARCHAR(255);

-- Ajout des colonnes à la table 'boxes'
ALTER TABLE boxes
ADD COLUMN title VARCHAR(255),
ADD COLUMN description TEXT,
ADD COLUMN offer_id UUID, -- Foreign key to offers table
ADD COLUMN publish_as VARCHAR(255) NOT NULL DEFAULT 'DRAFT', -- Exemple de NOT NULL avec DEFAULT
ADD COLUMN category VARCHAR(255) NOT NULL DEFAULT 'OTHER', -- Exemple de NOT NULL avec DEFAULT
ADD COLUMN box_status VARCHAR(255),
ADD COLUMN reason TEXT,
ADD COLUMN motif TEXT;

-- Ajout des contraintes de clé étrangère pour 'boxes' (vers 'offers')
-- Assurez-vous que la table 'offers' existe et que la colonne 'id' est bien le PK
ALTER TABLE boxes
ADD CONSTRAINT fk_box_offer FOREIGN KEY (offer_id) REFERENCES offers(id);

-- Ajout des colonnes à la table 'deals'
ALTER TABLE deals
ADD COLUMN offer_id UUID, -- Foreign key to offers table
ADD COLUMN publish_as VARCHAR(255) NOT NULL DEFAULT 'DRAFT', -- Exemple de NOT NULL avec DEFAULT
ADD COLUMN category VARCHAR(255) NOT NULL DEFAULT 'OTHER', -- Exemple de NOT NULL avec DEFAULT
ADD COLUMN deal_status VARCHAR(255),
ADD COLUMN reason TEXT,
ADD COLUMN motif TEXT;

-- Ajout des contraintes de clé étrangère pour 'deals' (vers 'offers')
-- Assurez-vous que la table 'offers' existe et que la colonne 'id' est bien le PK
ALTER TABLE deals
ADD CONSTRAINT fk_deal_offer FOREIGN KEY (offer_id) REFERENCES offers(id);


-- NOUVEAU: Création de la table 'supplements'
CREATE TABLE supplements (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    amount NUMERIC(19,2),
    currency VARCHAR(3),
    supplement_image_type VARCHAR(255), -- Correspond à supplementImagePath dans l'entité
    deal_id UUID, -- Foreign key to deals table
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    CONSTRAINT fk_supplement_deal FOREIGN KEY (deal_id) REFERENCES deals(id)
);

-- Remarques sur DEFAULT 'DRAFT'/'OTHER' pour NOT NULL:
-- Si vous avez des données existantes, ces valeurs par défaut seront appliquées.
-- Ajustez les DEFAULT en fonction de vos besoins métier.
-- Si les champs ne peuvent pas avoir de DEFAULT, ils doivent être NULLABLE ou vous devrez
-- faire une migration en plusieurs étapes (ADD COLUMN NULLABLE -> UPDATE existing rows -> ALTER COLUMN SET NOT NULL).

-- Si 'products' n'est pas déjà dans V1 et que vous avez besoin de 'Deal.product'
-- Exemple:
-- CREATE TABLE products (
--     id UUID PRIMARY KEY,
--     name VARCHAR(255) NOT NULL,
--     -- autres champs du produit
-- );