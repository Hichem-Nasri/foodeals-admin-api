-- V3__add_order_columns_and_fk.sql

-- Ajout des colonnes à la table 'orders'
ALTER TABLE orders
ADD COLUMN client_pro_id UUID, -- Clé étrangère vers SubEntity
ADD COLUMN seen Boolean, 
ADD COLUMN cancellation_reason TEXT,
ADD COLUMN cancellation_subject VARCHAR(255),
ADD COLUMN cancellation_date TIMESTAMP WITHOUT TIME ZONE, -- Pour LocalDateTime
ADD COLUMN attachment_path VARCHAR(255),
ADD COLUMN order_source VARCHAR(255),
ADD COLUMN donate_id UUID,-- Clé étrangère vers Donate
ADD COLUMN transaction_id UUID;

-- Ajout des contraintes de clé étrangère
-- NOTE: Ces FK nécessitent que les tables 'sub_entities' et 'donates' existent.
-- Si elles n'existent pas encore, vous devrez créer des scripts de migration séparés pour elles avant ce script.

-- ALTER TABLE orders
-- ADD CONSTRAINT fk_order_client_pro FOREIGN KEY (client_pro_id) REFERENCES sub_entities(id);

-- ALTER TABLE orders
-- ADD CONSTRAINT fk_order_donate FOREIGN KEY (donate_id) REFERENCES donates(id);

-- J'ai commenté les contraintes FK ci-dessus.
-- Vous devrez les décommenter UNIQUEMENT si les tables 'sub_entities' et 'donates'
-- sont déjà créées dans un script Flyway précédent (V1 ou V2).
-- Sinon, vous devez d'abord créer ces tables (avec leurs propres entités et migrations).
-- Par exemple, si SubEntity et Donate sont des nouvelles entités persistantes,
-- vous devriez avoir des entités Java pour elles et des CREATE TABLE dans des scripts précédents.