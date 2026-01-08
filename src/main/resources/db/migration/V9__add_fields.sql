-- V9__add_collaborator_fields_to_user.sql

ALTER TABLE users
ADD COLUMN hire_date DATE;

ALTER TABLE users
ADD COLUMN gross_declaration DOUBLE PRECISION;

ALTER TABLE users
ADD COLUMN net_declaration DOUBLE PRECISION;

ALTER TABLE users
ADD COLUMN contract_type VARCHAR(255);


ALTER TABLE coupons
ADD COLUMN name VARCHAR(255);


-- Mise à jour table notifications (ajout de colonnes si nécessaire)
DROP TABLE IF EXISTS notifications CASCADE;

CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL, -- 'info', 'warning', 'success', 'error'
    target_type VARCHAR(50) NOT NULL, -- 'all_users', 'specific_users', 'user_groups'
    target_ids JSONB, -- array of user/group UUIDs
    scheduled_at TIMESTAMP WITH TIME ZONE,
    sent_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) DEFAULT 'draft', -- 'draft', 'scheduled', 'sent', 'failed'
    created_by integer REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Table: user_notification_preferences
CREATE TABLE user_notification_preferences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id integer  REFERENCES users(id),
    push_enabled BOOLEAN DEFAULT true,
    email_enabled BOOLEAN DEFAULT true,
    sms_enabled BOOLEAN DEFAULT false,
    notification_types JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Table: notification_deliveries
CREATE TABLE notification_deliveries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    notification_id UUID REFERENCES notifications(id),
    user_id integer REFERENCES users(id),
    delivery_method VARCHAR(50), -- 'push', 'email', 'sms'
    status VARCHAR(50), -- 'pending', 'delivered', 'failed', 'read'
    delivered_at TIMESTAMP WITH TIME ZONE,
    read_at TIMESTAMP WITH TIME ZONE,
    error_message TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Table: device_tokens
CREATE TABLE device_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id integer REFERENCES users(id),
    token VARCHAR(500) NOT NULL,
    device_type VARCHAR(50), -- 'ios', 'android', 'web'
    device_info JSONB,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

ALTER TABLE supplements
ADD COLUMN IF NOT EXISTS description TEXT;

ALTER TABLE supplements
ADD COLUMN IF NOT EXISTS created_by VARCHAR(50);

ALTER TABLE supplements
ADD COLUMN IF NOT EXISTS reason VARCHAR(250);

ALTER TABLE supplements
ADD COLUMN IF NOT EXISTS motif VARCHAR(250);

ALTER TABLE supplements
ADD COLUMN IF NOT EXISTS type VARCHAR(50);

ALTER TABLE activities
ADD COLUMN IF NOT EXISTS motif VARCHAR(50);

ALTER TABLE activities
ADD COLUMN IF NOT EXISTS reason VARCHAR(250);


ALTER TABLE activities
ADD COLUMN IF NOT EXISTS observation VARCHAR(250);


ALTER TABLE activities
ADD COLUMN IF NOT EXISTS classement integer default 1;


ALTER TABLE product_categories
ADD COLUMN IF NOT EXISTS observation VARCHAR(250);


ALTER TABLE product_categories
ADD COLUMN IF NOT EXISTS classement integer default 1; 

ALTER TABLE product_categories
ADD COLUMN IF NOT EXISTS image_url VARCHAR(250);


ALTER TABLE users
ADD COLUMN latitude FLOAT;

ALTER TABLE users
ADD COLUMN longitude FLOAT;

ALTER TABLE users
ADD COLUMN radius INTEGER default null;

ALTER TABLE users
ADD COLUMN vehicle_type VARCHAR(50) default null;


CREATE TABLE tracking_step (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id),
    status VARCHAR(50) NOT NULL,
    longitude FLOAT ,
    latitude FLOAT,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null

);


CREATE TABLE user_permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    permission VARCHAR(255) NOT NULL,
    granted BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);


-- Hero section
CREATE TABLE homepage_hero (
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    subtitle TEXT,
    background_image TEXT,
    cta_text VARCHAR(255),
    cta_link VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Category section
CREATE TABLE homepage_categories (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    icon TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    classement INTEGER DEFAULT 0,
    deal_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Featured deals section
CREATE TABLE homepage_featured_deal (
    id UUID PRIMARY KEY,
    deal_id UUID DEFAULT NULL,
    title VARCHAR(255),
    description TEXT,
    image TEXT,
    original_price DOUBLE PRECISION,
    discounted_price DOUBLE PRECISION,
    restaurant VARCHAR(255),
    restaurant_id UUID,
    is_active BOOLEAN DEFAULT TRUE,
    classement INTEGER DEFAULT 0,
     created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Testimonial section
CREATE TABLE homepage_testimonials (
    id UUID PRIMARY KEY,
    customer_name VARCHAR(255),
    rating INTEGER CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    avatar TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    classement INTEGER DEFAULT 0,
     created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Announcement section
CREATE TABLE homepage_announcements (
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    message TEXT,
    type VARCHAR(50), -- e.g., 'info', 'warning', 'success'
    is_active BOOLEAN DEFAULT TRUE,
    expires_at TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

ALTER TABLE coupons
  ALTER COLUMN name TYPE text USING name::text,
  ALTER COLUMN code TYPE text USING code::text;
  
  
CREATE TABLE personalized_best_sellers (
    id UUID PRIMARY KEY,
    name TEXT,
    image TEXT,
    total_sales INT,
    completed_orders INT,
    rating FLOAT,
    country_id UUID,
    state_id UUID,
    city_id UUID,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

CREATE TABLE home_sorting (
    id UUID PRIMARY KEY ,
    name VARCHAR(100) NOT NULL,
    order_class INTEGER NOT NULL,
    rank INTEGER NOT NULL,
    country_id UUID,
    state_id UUID,
    city_id UUID,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null

);

ALTER TABLE notifications
    ADD COLUMN IF NOT EXISTS image_url VARCHAR(2048);
-- Tickets
CREATE TABLE IF NOT EXISTS support_tickets (
    id UUID PRIMARY KEY,
    subject TEXT NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(32) NOT NULL,         -- pending | in_progress | resolved | closed
    priority VARCHAR(16) NOT NULL,       -- low | medium | high
    category VARCHAR(64) NOT NULL,       -- payment | account | ...
    user_id INTEGER NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    user_phone VARCHAR(64),
    user_avatar TEXT,
    user_account_created TIMESTAMPTZ,
    assigned_to_id INTEGER,
    assigned_to_name VARCHAR(255),
    assigned_to_email VARCHAR(255),
    response_count INT NOT NULL DEFAULT 0,
    last_response_at  TIMESTAMPTZ NULL,
    satisfaction_rating NUMERIC(2,1),    -- nullable, moyenne ou note finale
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);


-- Responses
CREATE TABLE IF NOT EXISTS support_responses (
    id UUID PRIMARY KEY,
    ticket_id UUID NOT NULL REFERENCES support_tickets(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    type VARCHAR(32) NOT NULL,           -- admin_response | internal_note | user_message
    author_id INTEGER NOT NULL,
    author_name VARCHAR(255) NOT NULL,
    author_email VARCHAR(255),
    author_role VARCHAR(64),
    is_internal BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);


-- Attachments
CREATE TABLE IF NOT EXISTS support_attachments (
    id UUID PRIMARY KEY,
    ticket_id UUID NOT NULL REFERENCES support_tickets(id) ON DELETE CASCADE,
    filename VARCHAR(512) NOT NULL,
    url TEXT NOT NULL,
    size BIGINT,
    mime_type VARCHAR(128),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Tags (simples)
CREATE TABLE IF NOT EXISTS support_ticket_tags (
    ticket_id UUID NOT NULL REFERENCES support_tickets(id) ON DELETE CASCADE,
    tag VARCHAR(64) NOT NULL,
    PRIMARY KEY (ticket_id, tag)
);

CREATE TABLE IF NOT EXISTS crm_demandes (
    id UUID PRIMARY KEY,
    type VARCHAR(32) NOT NULL,                      -- dlc | marketpro | donate | association
    company_name VARCHAR(255) NOT NULL,
    country VARCHAR(128) NOT NULL,
    city VARCHAR(128) NOT NULL,
    date TIMESTAMPTZ,
    responsable VARCHAR(255),
    address VARCHAR(512),
    email VARCHAR(255),
    phone VARCHAR(64),
    status VARCHAR(32) NOT NULL DEFAULT 'pending',  -- pending | approved | rejected
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);



CREATE TABLE IF NOT EXISTS crm_demande_activities (
    demande_id UUID NOT NULL REFERENCES crm_demandes(id) ON DELETE CASCADE,
    activity VARCHAR(128) NOT NULL,
    PRIMARY KEY (demande_id, activity)
);

CREATE TABLE IF NOT EXISTS crm_demande_documents (
    id UUID PRIMARY KEY,
    demande_id UUID NOT NULL REFERENCES crm_demandes(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    type VARCHAR(64) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

CREATE TABLE IF NOT EXISTS crm_demande_history (
    id UUID PRIMARY KEY,
    demande_id UUID NOT NULL REFERENCES crm_demandes(id) ON DELETE CASCADE,
    action VARCHAR(64) NOT NULL,                    -- created | updated | status_changed | document_added ...
    performed_by VARCHAR(128) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    details TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

  
CREATE TABLE product_subcategories (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    slug        VARCHAR(255) NOT NULL,
    category_id UUID,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);


ALTER TABLE products ADD COLUMN brand VARCHAR(255);
ALTER TABLE products ADD COLUMN created_by VARCHAR(50);
ALTER TABLE products ADD COLUMN created_by_user_id INTEGER;
ALTER TABLE products ADD COLUMN created_by_sub_entity_id UUID;
ALTER TABLE products ADD COLUMN rayon_id UUID;
ALTER TABLE products ADD COLUMN subcategory_id UUID;
ALTER TABLE products ADD COLUMN reason VARCHAR(50);
ALTER TABLE products ADD COLUMN motif VARCHAR(50);

ALTER TABLE prospect
ADD COLUMN creator_id integer;


-- Ajout des colonnes manquantes depuis foodeals-pro
ALTER TABLE products ADD COLUMN IF NOT EXISTS quantity INT;
ALTER TABLE products ADD COLUMN IF NOT EXISTS expiration_date TIMESTAMP;
ALTER TABLE products ADD COLUMN IF NOT EXISTS price_after_discount_amount DECIMAL(10,2);
ALTER TABLE products ADD COLUMN IF NOT EXISTS price_after_discount_currency VARCHAR(3);

-- Ajout des colonnes pour relations supplémentaires
ALTER TABLE products ADD COLUMN IF NOT EXISTS solution_id UUID;
ALTER TABLE products ADD COLUMN IF NOT EXISTS payment_method_product_id UUID;
ALTER TABLE products ADD COLUMN IF NOT EXISTS delivery_method_id UUID;


-- Création de la table delivery_methods
CREATE TABLE IF NOT EXISTS delivery_methods (
    id UUID PRIMARY KEY,
    delivery_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Création de la table payment_methods_product
CREATE TABLE IF NOT EXISTS payment_methods_product (
    id UUID PRIMARY KEY,
    method_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

-- Création de la table pickup_conditions
CREATE TABLE IF NOT EXISTS pickup_conditions (
    id UUID PRIMARY KEY,
    condition_name VARCHAR(255) NOT NULL,
    days_before_pickup INT NOT NULL,
    discount_percentage INT NOT NULL,
    product_id UUID,
    payment_method_product_id UUID ,
    delivery_method_id UUID ,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);
CREATE TABLE IF NOT EXISTS admin_notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    type VARCHAR(50) NOT NULL,            -- ORDER | SUPPORT | REPORT | SYSTEM | PARTNER | DELIVERY
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,

    icon VARCHAR(255),
    action_url VARCHAR(500),

    metadata_json JSONB,                  -- payload dynamique (orderId, userName, etc.)

    source_app VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',   -- CLIENT_APP | PRO_APP | ADMIN_APP | SYSTEM
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',     -- LOW | MEDIUM | HIGH | URGENT

    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

CREATE TABLE IF NOT EXISTS admin_notification_states (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    notification_id UUID NOT NULL REFERENCES admin_notifications(id) ON DELETE CASCADE,
    admin_id INTEGER NOT NULL REFERENCES users(id),

    is_read BOOLEAN NOT NULL DEFAULT false,
    read_at TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null,

    CONSTRAINT uq_admin_notification UNIQUE (notification_id, admin_id)
);
CREATE TABLE IF NOT EXISTS admin_notification_preferences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    admin_id INTEGER NOT NULL UNIQUE REFERENCES users(id),

    order_notifications BOOLEAN DEFAULT true,
    support_notifications BOOLEAN DEFAULT true,
    report_notifications BOOLEAN DEFAULT true,
    system_notifications BOOLEAN DEFAULT true,
    partner_notifications BOOLEAN DEFAULT true,
    delivery_notifications BOOLEAN DEFAULT true,

    email_digest BOOLEAN DEFAULT false,
    sound_enabled BOOLEAN DEFAULT true,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);

CREATE TABLE IF NOT EXISTS blog_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(150) NOT NULL UNIQUE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null
);
CREATE TABLE IF NOT EXISTS blogs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(150),

    category_id UUID NOT NULL,
    published BOOLEAN NOT NULL DEFAULT false,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    deleted_at TIMESTAMP WITH TIME ZONE DEFAULT null,

    CONSTRAINT fk_blog_category
        FOREIGN KEY (category_id)
        REFERENCES blog_categories (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_admin_notification_states_admin
    ON admin_notification_states(admin_id);

CREATE INDEX IF NOT EXISTS idx_admin_notification_states_unread
    ON admin_notification_states(admin_id, is_read);

CREATE INDEX IF NOT EXISTS idx_admin_notifications_type
    ON admin_notifications(type);

CREATE INDEX IF NOT EXISTS idx_admin_notifications_priority
    ON admin_notifications(priority);

CREATE INDEX IF NOT EXISTS idx_admin_notifications_created_at
    ON admin_notifications(created_at);

CREATE INDEX IF NOT EXISTS idx_blogs_category_id ON blogs(category_id);
CREATE INDEX IF NOT EXISTS idx_blogs_deleted_at ON blogs(deleted_at);
CREATE INDEX IF NOT EXISTS idx_blog_categories_deleted_at ON blog_categories(deleted_at);




