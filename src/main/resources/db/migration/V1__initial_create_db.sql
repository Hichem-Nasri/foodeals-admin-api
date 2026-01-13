--
-- PostgreSQL database dump
--

-- Dumped from database version 13.12
-- Dumped by pg_dump version 13.12

-- Started on 2025-06-03 14:46:58

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 202 (class 1259 OID 1597259)
-- Name: accounts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.accounts (
    id uuid NOT NULL,
    access_token character varying(255),
    expires_at character varying(255),
    id_token character varying(255),
    provider character varying(255),
    provider_account_id character varying(255),
    refresh_token character varying(255),
    scope character varying(255),
    session_state character varying(255),
    token_type character varying(255),
    type character varying(255)
);

--
-- TOC entry 203 (class 1259 OID 1597267)
-- Name: activities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.activities (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    name character varying(255),
    type character varying(255),
    CONSTRAINT activities_type_check CHECK (((type)::text = ANY ((ARRAY['PARTNER'::character varying, 'ASSOCIATION'::character varying, 'DELIVERY_PARTNER'::character varying])::text[])))
);



--
-- TOC entry 204 (class 1259 OID 1597276)
-- Name: address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.address (
    latitude real,
    longitude real,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    region_id uuid,
    iframe character varying(2000),
    address character varying(255)
);


--
-- TOC entry 205 (class 1259 OID 1597284)
-- Name: article; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.article (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    article_category_id uuid,
    id uuid NOT NULL,
    content character varying(255),
    slug character varying(255),
    thumbnail_image character varying(255),
    title character varying(255)
);



--
-- TOC entry 206 (class 1259 OID 1597294)
-- Name: authorities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authorities (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    name character varying(255),
    value character varying(255)
);


--
-- TOC entry 207 (class 1259 OID 1597302)
-- Name: authorities_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authorities_roles (
    authorities_id uuid NOT NULL,
    roles_id uuid NOT NULL
);


--
-- TOC entry 208 (class 1259 OID 1597305)
-- Name: bank_information; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bank_information (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    bank_name character varying(255),
    beneficiary_name character varying(255),
    rib character varying(255)
);


--
-- TOC entry 209 (class 1259 OID 1597313)
-- Name: blog_categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.blog_categories (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    name character varying(255),
    slug character varying(255)
);


--
-- TOC entry 210 (class 1259 OID 1597321)
-- Name: box_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.box_items (
    amount numeric(38,2),
    currency character varying(3),
    quantity integer,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    box_id uuid,
    id uuid NOT NULL,
    product_id uuid
);


--
-- TOC entry 211 (class 1259 OID 1597326)
-- Name: boxes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.boxes (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    type character varying(255),
    CONSTRAINT boxes_type_check CHECK (((type)::text = ANY ((ARRAY['NORMAL_BOX'::character varying, 'MYSTERY_BOX'::character varying])::text[])))
);


--
-- TOC entry 212 (class 1259 OID 1597332)
-- Name: cities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cities (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    state_id uuid,
    name character varying(255)
);


--
-- TOC entry 213 (class 1259 OID 1597337)
-- Name: commissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commissions (
    card real,
    cash real,
    delivery_amount real,
    delivery_commission real,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL
);

--
-- TOC entry 214 (class 1259 OID 1597342)
-- Name: contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contact (
    is_responsible boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    organization_id uuid,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    phone character varying(255)
);

--
-- TOC entry 215 (class 1259 OID 1597350)
-- Name: contracts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contracts (
    commission_payed_by_sub_entities boolean NOT NULL,
    max_number_of_accounts integer,
    max_number_of_sub_entities integer,
    minimum_reduction real,
    single_subscription boolean NOT NULL,
    subscription_payed_by_sub_entities boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    contract_status character varying(255),
    name character varying(255),
    document oid,
    CONSTRAINT contracts_contract_status_check CHECK (((contract_status)::text = ANY ((ARRAY['IN_PROGRESS'::character varying, 'VALIDATED'::character varying, 'REJECTED'::character varying])::text[])))
);

--
-- TOC entry 216 (class 1259 OID 1597359)
-- Name: countries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.countries (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    name character varying(255)
);

--
-- TOC entry 217 (class 1259 OID 1597364)
-- Name: coupons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coupons (
    discount real,
    is_enabled boolean,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    ends_at timestamp(6) without time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    sub_entity_id uuid,
    code character varying(255)
);


--
-- TOC entry 218 (class 1259 OID 1597369)
-- Name: covered_zones; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.covered_zones (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    organization_entity_id uuid,
    region_id uuid
);

--
-- TOC entry 219 (class 1259 OID 1597374)
-- Name: deadlines; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.deadlines (
    amount numeric(38,2),
    currency character varying(3),
    due_date date,
    emitter_id integer,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    parent_partner_id uuid,
    payment_method_id uuid,
    subscription_id uuid,
    payment_responsibility character varying(255),
    status character varying(255),
    CONSTRAINT deadlines_payment_responsibility_check CHECK (((payment_responsibility)::text = ANY ((ARRAY['PAYED_BY_SUB_ENTITIES'::character varying, 'PAYED_BY_PARTNER'::character varying])::text[]))),
    CONSTRAINT deadlines_status_check CHECK (((status)::text = ANY ((ARRAY['PAYED_BY_PARTNER'::character varying, 'CONFIRMED_BY_FOODEALS'::character varying, 'IN_VALID'::character varying])::text[])))
);

--
-- TOC entry 220 (class 1259 OID 1597386)
-- Name: deals; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.deals (
    amount numeric(38,2),
    currency character varying(3),
    quantity integer,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    product_id uuid
);

--
-- TOC entry 221 (class 1259 OID 1597391)
-- Name: deletion_reason; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.deletion_reason (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    details character varying(2000),
    action_type character varying(255) NOT NULL,
    reason_type character varying(255) NOT NULL,
    CONSTRAINT deletion_reason_action_type_check CHECK (((action_type)::text = ANY ((ARRAY['ARCHIVE'::character varying, 'DE_ARCHIVE'::character varying])::text[]))),
    CONSTRAINT deletion_reason_reason_type_check CHECK (((reason_type)::text = 'OTHER'::text))
);


--
-- TOC entry 222 (class 1259 OID 1597401)
-- Name: deliveries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.deliveries (
    delivery_boy_id integer,
    rating integer,
    status character varying(20),
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    delivery_position_id uuid,
    id uuid NOT NULL
);

--
-- TOC entry 223 (class 1259 OID 1597409)
-- Name: delivery_positions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.delivery_positions (
    latitude real,
    longitude real,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL
);

--
-- TOC entry 224 (class 1259 OID 1597414)
-- Name: donation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.donation (
    status smallint,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    activity_id uuid,
    donor_id uuid,
    id uuid NOT NULL,
    offerable_id uuid,
    receiver_id uuid,
    description character varying(20000),
    donor_type character varying(255),
    offerable_type character varying(255),
    receiver_type character varying(255),
    CONSTRAINT donation_donor_type_check CHECK (((donor_type)::text = ANY ((ARRAY['PARTNER_WITH_SB'::character varying, 'NORMAL_PARTNER'::character varying, 'FOOD_BANK'::character varying, 'PARTNER_SB'::character varying, 'FOOD_BANK_SB'::character varying, 'FOOD_BANK_ASSOCIATION'::character varying])::text[]))),
    CONSTRAINT donation_offerable_type_check CHECK (((offerable_type)::text = ANY ((ARRAY['DEAL'::character varying, 'BOX'::character varying])::text[]))),
    CONSTRAINT donation_receiver_type_check CHECK (((receiver_type)::text = ANY ((ARRAY['FOOD_BANK_SB'::character varying, 'FOOD_BANK_ASSOCIATION'::character varying, 'ASSOCIATION'::character varying, 'FOOD_BANK'::character varying])::text[]))),
    CONSTRAINT donation_status_check CHECK (((status >= 0) AND (status <= 1)))
);

--
-- TOC entry 225 (class 1259 OID 1597426)
-- Name: event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.event (
    lead_id integer,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    message character varying(200000),
    date_and_hour character varying(255),
    object character varying(255)
);

--
-- TOC entry 226 (class 1259 OID 1597434)
-- Name: event_publication; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.event_publication (
    completion_date timestamp(6) with time zone,
    publication_date timestamp(6) with time zone,
    id uuid NOT NULL,
    event_type character varying(255),
    listener_id character varying(255),
    serialized_event character varying(255)
);


--
-- TOC entry 227 (class 1259 OID 1597442)
-- Name: features; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.features (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    name character varying(255)
);


--
-- TOC entry 228 (class 1259 OID 1597447)
-- Name: notifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notifications (
    user_id integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    entity_id uuid NOT NULL,
    id uuid NOT NULL,
    sub_entity_id uuid NOT NULL,
    content character varying(255),
    title character varying(255)
);


--
-- TOC entry 229 (class 1259 OID 1597455)
-- Name: offers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offers (
    price_amount numeric(38,2),
    price_currency character varying(3),
    reduction integer,
    sale_price_amount numeric(38,2),
    sale_price_currency character varying(3),
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    activity_id uuid,
    id uuid NOT NULL,
    offerable_id uuid,
    publisher_id uuid,
    barcode character varying(255),
    image_path character varying(255),
    offerable_type character varying(255),
    publisher_type character varying(255),
    title character varying(255),
    CONSTRAINT offers_offerable_type_check CHECK (((offerable_type)::text = ANY ((ARRAY['DEAL'::character varying, 'BOX'::character varying])::text[]))),
    CONSTRAINT offers_publisher_type_check CHECK (((publisher_type)::text = ANY ((ARRAY['PARTNER_WITH_SB'::character varying, 'NORMAL_PARTNER'::character varying, 'FOOD_BANK'::character varying, 'PARTNER_SB'::character varying, 'FOOD_BANK_SB'::character varying, 'FOOD_BANK_ASSOCIATION'::character varying])::text[])))
);


--
-- TOC entry 230 (class 1259 OID 1597465)
-- Name: open_time; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.open_time (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    donation_id uuid,
    id uuid NOT NULL,
    offer_id uuid,
    day TIMESTAMP WITHOUT TIME ZONE,
    end_time character varying(255),
    start_time character varying(255)
);


--
-- TOC entry 231 (class 1259 OID 1597473)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    amount numeric(38,2),
    client_id integer,
    currency character varying(3),
    quantity integer,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    coupon_id uuid,
    delivery_id uuid,
    id uuid NOT NULL,
    offer_id uuid,
    shipping_address_id uuid,
    order_type character varying(255),
    status character varying(255),
    CONSTRAINT orders_order_type_check CHECK (((order_type)::text = ANY ((ARRAY['DELIVERY'::character varying, 'AT_PLACE'::character varying, 'PICKUP'::character varying])::text[]))),
    CONSTRAINT orders_status_check CHECK (((status)::text = ANY ((ARRAY['IN_CART'::character varying, 'OPEN'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CANCELED'::character varying, 'RETURNED'::character varying])::text[])))
);


--
-- TOC entry 232 (class 1259 OID 1597483)
-- Name: organization_entities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization_entities (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    address_id uuid,
    bank_information uuid,
    contract_id uuid,
    id uuid NOT NULL,
    avatar_path character varying(255),
    commercial_number character varying(255),
    cover_path character varying(255),
    name character varying(255),
    type character varying(255),
    CONSTRAINT organization_entities_type_check CHECK (((type)::text = ANY ((ARRAY['PARTNER_WITH_SB'::character varying, 'NORMAL_PARTNER'::character varying, 'ASSOCIATION'::character varying, 'DELIVERY_PARTNER'::character varying, 'FOOD_BANK'::character varying, 'FOOD_BANK_ASSO'::character varying])::text[])))
);


--
-- TOC entry 233 (class 1259 OID 1597498)
-- Name: organization_entities_activities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization_entities_activities (
    activities_id uuid NOT NULL,
    organization_entities_id uuid NOT NULL
);


--
-- TOC entry 234 (class 1259 OID 1597503)
-- Name: organization_entities_commissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization_entities_commissions (
    commissions_id uuid NOT NULL,
    organization_entity_id uuid NOT NULL
);



--
-- TOC entry 235 (class 1259 OID 1597508)
-- Name: organization_entities_deletion_reasons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization_entities_deletion_reasons (
    deletion_reasons_id uuid NOT NULL,
    organization_entity_id uuid NOT NULL
);



--
-- TOC entry 236 (class 1259 OID 1597513)
-- Name: organization_entities_features; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization_entities_features (
    features_id uuid NOT NULL,
    organization_entities_id uuid NOT NULL
);



--
-- TOC entry 237 (class 1259 OID 1597518)
-- Name: organization_entities_solutions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization_entities_solutions (
    organization_entities_id uuid NOT NULL,
    solutions_id uuid NOT NULL
);



--
-- TOC entry 238 (class 1259 OID 1597523)
-- Name: organization_entities_subscriptions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.organization_entities_subscriptions (
    organization_entity_id uuid NOT NULL,
    subscriptions_id uuid NOT NULL
);



--
-- TOC entry 239 (class 1259 OID 1597528)
-- Name: partner_commissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.partner_commissions (
    emitter_id integer,
    created_at timestamp(6) with time zone NOT NULL,
    date timestamp(6) without time zone,
    deleted_at timestamp(6) with time zone,
    recuperation_date timestamp(6) without time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    organization_id uuid,
    parent_partner_id uuid,
    partner_id uuid,
    payment_method_id uuid,
    partner_name character varying(255),
    partner_type character varying(255),
    payment_direction character varying(255),
    payment_responsibility character varying(255),
    payment_status character varying(255),
    CONSTRAINT partner_commissions_partner_type_check CHECK (((partner_type)::text = ANY ((ARRAY['PARTNER_SB'::character varying, 'NORMAL_PARTNER'::character varying, 'SUB_ENTITY'::character varying, 'DELIVERY_PARTNER'::character varying])::text[]))),
    CONSTRAINT partner_commissions_payment_direction_check CHECK (((payment_direction)::text = ANY ((ARRAY['FOODEALS_TO_PARTENER'::character varying, 'PARTNER_TO_FOODEALS'::character varying])::text[]))),
    CONSTRAINT partner_commissions_payment_responsibility_check CHECK (((payment_responsibility)::text = ANY ((ARRAY['PAYED_BY_SUB_ENTITIES'::character varying, 'PAYED_BY_PARTNER'::character varying])::text[]))),
    CONSTRAINT partner_commissions_payment_status_check CHECK (((payment_status)::text = ANY ((ARRAY['IN_VALID'::character varying, 'VALIDATED_BY_PARTNER'::character varying, 'VALIDATED_BY_FOODEALS'::character varying, 'VALIDATED_BY_BOTH'::character varying])::text[])))
);


--
-- TOC entry 240 (class 1259 OID 1597542)
-- Name: payment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment (
    payments_with_card double precision,
    payments_with_cash double precision,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    number_of_orders bigint,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    organization_entity_id uuid,
    sub_entity_id uuid,
    date character varying(255),
    partner_type character varying(255),
    payment_status character varying(255),
    CONSTRAINT payment_partner_type_check CHECK (((partner_type)::text = ANY ((ARRAY['PARTNER_SB'::character varying, 'NORMAL_PARTNER'::character varying, 'SUB_ENTITY'::character varying, 'DELIVERY_PARTNER'::character varying])::text[]))),
    CONSTRAINT payment_payment_status_check CHECK (((payment_status)::text = ANY ((ARRAY['IN_VALID'::character varying, 'VALIDATED_BY_PARTNER'::character varying, 'VALIDATED_BY_FOODEALS'::character varying, 'VALIDATED_BY_BOTH'::character varying])::text[])))
);


--
-- TOC entry 241 (class 1259 OID 1597552)
-- Name: payment_method; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_method (
    amount numeric(38,2),
    currency character varying(3),
    created_at timestamp(6) with time zone NOT NULL,
    deadline_date timestamp(6) without time zone,
    deleted_at timestamp(6) with time zone,
    payed_at timestamp(6) without time zone,
    recuperation_date timestamp(6) without time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    method_type character varying(31) NOT NULL,
    bank character varying(255),
    card_holder_name character varying(255),
    card_number character varying(255),
    cheque_document character varying(255),
    cheque_number character varying(255),
    document_path character varying(255),
    issuer character varying(255),
    payment_id character varying(255)
);


--
-- TOC entry 242 (class 1259 OID 1597560)
-- Name: product_categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_categories (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    activity_id uuid,
    id uuid NOT NULL,
    name character varying(255),
    slug character varying(255)
);


--
-- TOC entry 243 (class 1259 OID 1597568)
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    amount numeric(38,2),
    currency character varying(3),
    product_type smallint,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    end_date timestamp(6) without time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    category_id uuid,
    id uuid NOT NULL,
    barcode character varying(255),
    description character varying(255),
    name character varying(255),
    product_image_type character varying(255),
    slug character varying(255),
    title character varying(255),
    CONSTRAINT products_product_type_check CHECK (((product_type >= 0) AND (product_type <= 1)))
);

--
-- TOC entry 244 (class 1259 OID 1597577)
-- Name: prospect; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect (
    owner_id integer NULL,
    lead_id integer,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    address_id uuid,
    id uuid NOT NULL,
    name character varying(255),
    status character varying(255),
    type character varying(255),
    CONSTRAINT prospect_status_check CHECK (((status)::text = ANY ((ARRAY['VALID'::character varying, 'CANCELED'::character varying, 'IN_PROGRESS'::character varying])::text[]))),
    CONSTRAINT prospect_type_check CHECK (((type)::text = ANY ((ARRAY['ASSOCIATION'::character varying, 'PARTNER'::character varying, 'FOOD_BANK'::character varying])::text[])))
);



--
-- TOC entry 245 (class 1259 OID 1597589)
-- Name: prospect_activities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_activities (
    activities_id uuid NOT NULL,
    prospects_id uuid NOT NULL
);



--
-- TOC entry 246 (class 1259 OID 1597594)
-- Name: prospect_contacts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_contacts (
    contacts_id uuid NOT NULL,
    prospect_id uuid NOT NULL
);


--
-- TOC entry 247 (class 1259 OID 1597599)
-- Name: prospect_deletion_reasons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_deletion_reasons (
    deletion_reasons_id uuid NOT NULL,
    prospect_id uuid NOT NULL
);


--
-- TOC entry 248 (class 1259 OID 1597604)
-- Name: prospect_events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_events (
    events_id uuid NOT NULL,
    prospect_id uuid NOT NULL
);



--
-- TOC entry 249 (class 1259 OID 1597609)
-- Name: prospect_solutions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prospect_solutions (
    prospects_id uuid NOT NULL,
    solutions_id uuid NOT NULL
);


--
-- TOC entry 250 (class 1259 OID 1597614)
-- Name: rayon; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rayon (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    name character varying(255)
);


--
-- TOC entry 251 (class 1259 OID 1597619)
-- Name: region; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.region (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    city_id uuid,
    id uuid NOT NULL,
    name character varying(255)
);

--
-- TOC entry 252 (class 1259 OID 1597624)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    name character varying(255)
);



--
-- TOC entry 253 (class 1259 OID 1597629)
-- Name: solution_contracts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.solution_contracts (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    comission_id uuid,
    contract_id uuid,
    id uuid NOT NULL,
    solution_id uuid,
    subscription_id uuid
);



--
-- TOC entry 254 (class 1259 OID 1597636)
-- Name: solutions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.solutions (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    name character varying(255)
);


--
-- TOC entry 255 (class 1259 OID 1597641)
-- Name: states; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.states (
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    country_id uuid,
    id uuid NOT NULL,
    name character varying(255)
);



--
-- TOC entry 256 (class 1259 OID 1597646)
-- Name: sub_entities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sub_entities (
    latitude real,
    longitude real,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    address_id uuid,
    contract_id uuid,
    id uuid NOT NULL,
    organization_entity_id uuid  NULL,
    avatar_path character varying(255),
    cover_path character varying(255),
    name character varying(255),
    type character varying(255),
    CONSTRAINT sub_entities_type_check CHECK (((type)::text = ANY ((ARRAY['PARTNER_SB'::character varying, 'DELIVERY_PARTNER_SB'::character varying, 'FOOD_BANK_SB'::character varying, 'FOOD_BANK_ASSOCIATION'::character varying])::text[])))
);



--
-- TOC entry 257 (class 1259 OID 1597657)
-- Name: sub_entities_activities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sub_entities_activities (
    activities_id uuid NOT NULL,
    sub_entities_id uuid NOT NULL
);


--
-- TOC entry 258 (class 1259 OID 1597662)
-- Name: sub_entities_commissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sub_entities_commissions (
    commissions_id uuid NOT NULL,
    sub_entity_id uuid NOT NULL
);



--
-- TOC entry 259 (class 1259 OID 1597667)
-- Name: sub_entities_contacts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sub_entities_contacts (
    contacts_id uuid NOT NULL,
    sub_entity_id uuid NOT NULL
);



--
-- TOC entry 260 (class 1259 OID 1597672)
-- Name: sub_entities_deletion_reasons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sub_entities_deletion_reasons (
    deletion_reasons_id uuid NOT NULL,
    sub_entity_id uuid NOT NULL
);



--
-- TOC entry 261 (class 1259 OID 1597677)
-- Name: sub_entities_solutions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sub_entities_solutions (
    solutions_id uuid NOT NULL,
    sub_entities_id uuid NOT NULL
);



--
-- TOC entry 262 (class 1259 OID 1597682)
-- Name: sub_entities_subscriptions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sub_entities_subscriptions (
    sub_entity_id uuid NOT NULL,
    subscriptions_id uuid NOT NULL
);



--
-- TOC entry 263 (class 1259 OID 1597687)
-- Name: subscription; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subscription (
    amount numeric(38,2),
    amount_currency character varying(3),
    duration integer,
    end_date date,
    number_of_due_dates integer,
    start_date date,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    organization_id uuid,
    parent_partner_id uuid,
    partner_id uuid,
    partner_name character varying(255),
    partner_type character varying(255),
    subscription_status character varying(255),
    CONSTRAINT subscription_partner_type_check CHECK (((partner_type)::text = ANY ((ARRAY['PARTNER_SB'::character varying, 'NORMAL_PARTNER'::character varying, 'SUB_ENTITY'::character varying, 'DELIVERY_PARTNER'::character varying])::text[]))),
    CONSTRAINT subscription_subscription_status_check CHECK (((subscription_status)::text = ANY ((ARRAY['NOT_STARTED'::character varying, 'IN_PROGRESS'::character varying, 'VALID'::character varying, 'CANCELED'::character varying])::text[])))
);


--
-- TOC entry 264 (class 1259 OID 1597697)
-- Name: subscription_solutions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subscription_solutions (
    solutions_id uuid NOT NULL,
    subscription_id uuid NOT NULL
);



--
-- TOC entry 265 (class 1259 OID 1597702)
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transactions (
    amount numeric(38,2),
    currency character varying(3),
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    order_id uuid,
    context character varying(255),
    payment_id character varying(255),
    reference character varying(255),
    status character varying(255),
    type character varying(255),
    CONSTRAINT transactions_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying, 'REFUNDED'::character varying])::text[]))),
    CONSTRAINT transactions_type_check CHECK (((type)::text = ANY ((ARRAY['CASH'::character varying, 'CARD'::character varying])::text[])))
);



--
-- TOC entry 266 (class 1259 OID 1597714)
-- Name: user_activities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_activities (
    user_id integer,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    activity_id uuid,
    id uuid NOT NULL
);



--
-- TOC entry 267 (class 1259 OID 1597719)
-- Name: user_contract; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_contract (
    user_id integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    contract_id uuid NOT NULL,
    id uuid NOT NULL
);


--
-- TOC entry 268 (class 1259 OID 1597726)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    date_of_birth date,
    id integer NOT NULL,
    reason  character varying(255) null,
   motif character varying(255) null,
    is_email_verified boolean,
    responsible_id integer,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    account_id uuid,
    address_id uuid,
    organization_entity_id uuid,
    rayon_id uuid,
    role_id uuid,
    sub_entity_id uuid,
    avatar_path character varying(255),
    email character varying(255),
    first_name character varying(255),
    gender character varying(255),
    source character varying (255),
    last_name character varying(255),
    national_id character varying(255),
    nationality character varying(255),
    password character varying(255),
    phone character varying(255),
    status character varying(50),
    CONSTRAINT users_gender_check CHECK (((gender)::text = ANY ((ARRAY['MALE'::character varying, 'FEMALE'::character varying, 'OTHER'::character varying])::text[])))
   
);



CREATE TABLE public.user_personal_documents (
    id UUID PRIMARY KEY,
    user_id INTEGER ,
    name VARCHAR(255),
    path TEXT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL
);

CREATE TABLE public.user_internal_documents (
    id UUID PRIMARY KEY,
    user_id INTEGER ,
    name VARCHAR(255),
    path TEXT,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL
);


CREATE TABLE public.absences (
    id UUID PRIMARY KEY,
    user_id INTEGER,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    justification_path TEXT,
    validate_by_id INTEGER,
   

    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL
);

--
-- TOC entry 269 (class 1259 OID 1597740)
-- Name: users_deletion_reasons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_deletion_reasons (
    user_id integer NOT NULL,
    deletion_reasons_id uuid NOT NULL
);


--
-- TOC entry 201 (class 1259 OID 1597257)
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 270 (class 1259 OID 1597745)
-- Name: users_solutions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_solutions (
    users_id integer NOT NULL,
    solutions_id uuid NOT NULL
);



--
-- TOC entry 271 (class 1259 OID 1597750)
-- Name: working_hours; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.working_hours (
    user_id integer,
    created_at timestamp(6) with time zone NOT NULL,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    afternoon_end character varying(255),
    afternoon_start character varying(255),
    day_of_week character varying(255),
    morning_end character varying(255),
    morning_start character varying(255),
    CONSTRAINT working_hours_day_of_week_check CHECK (((day_of_week)::text = ANY ((ARRAY['MONDAY'::character varying, 'TUESDAY'::character varying, 'WEDNESDAY'::character varying, 'THURSDAY'::character varying, 'FRIDAY'::character varying, 'SATURDAY'::character varying, 'SUNDAY'::character varying])::text[])))
);



--
-- TOC entry 3604 (class 0 OID 1597259)
-- Dependencies: 202
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.accounts (id, access_token, expires_at, id_token, provider, provider_account_id, refresh_token, scope, session_state, token_type, type) FROM stdin;
\.


--
-- TOC entry 3605 (class 0 OID 1597267)
-- Dependencies: 203
-- Data for Name: activities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.activities (created_at, deleted_at, updated_at, id, name, type) FROM stdin;
2025-06-03 14:46:41.690509+02	\N	2025-06-03 14:46:41.690509+02	807de42a-e581-4091-8be7-f40bd6e21fcc	supermarché	PARTNER
2025-06-03 14:46:41.691771+02	\N	2025-06-03 14:46:41.691771+02	5de91dee-cf28-4be0-a081-e880b0ef509e	pâtisserie	PARTNER
2025-06-03 14:46:41.693781+02	\N	2025-06-03 14:46:41.693781+02	3e12c1b7-0e36-4e29-938c-3b3d0f02410d	boulangerie	PARTNER
2025-06-03 14:46:41.694785+02	\N	2025-06-03 14:46:41.695973+02	d723208f-aa66-4751-b6ea-174350e3ae20	épicerie	PARTNER
2025-06-03 14:46:41.697242+02	\N	2025-06-03 14:46:41.697242+02	400cb742-e304-4149-aaf8-629f47fcd30d	confiserie	PARTNER
2025-06-03 14:46:41.712249+02	\N	2025-06-03 14:46:41.712249+02	370508ff-676b-4a1b-bd4c-53c15bbdce69	dar diafa	ASSOCIATION
2025-06-03 14:46:41.715252+02	\N	2025-06-03 14:46:41.715252+02	81a9a981-b0f0-4c68-9a3a-0ef20d2014a5	banque alimentaire	ASSOCIATION
2025-06-03 14:46:41.719566+02	\N	2025-06-03 14:46:41.719566+02	cc985afd-f6d1-4345-9540-ba25432fecc7	livraison	DELIVERY_PARTNER
\.


--
-- TOC entry 3606 (class 0 OID 1597276)
-- Dependencies: 204
-- Data for Name: address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.address (latitude, longitude, created_at, deleted_at, updated_at, id, region_id, iframe, address) FROM stdin;
\.


--
-- TOC entry 3607 (class 0 OID 1597284)
-- Dependencies: 205
-- Data for Name: article; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.article (created_at, deleted_at, updated_at, article_category_id, id, content, slug, thumbnail_image, title) FROM stdin;
\.


--
-- TOC entry 3608 (class 0 OID 1597294)
-- Dependencies: 206
-- Data for Name: authorities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.authorities (created_at, deleted_at, updated_at, id, name, value) FROM stdin;
2025-06-03 14:46:41.2634+02	\N	2025-06-03 14:46:41.2634+02	6a64f61a-40ed-4ea7-93f1-eb18605220bf	READ_USER	read:user
2025-06-03 14:46:41.2634+02	\N	2025-06-03 14:46:41.2634+02	3ac40fac-0f48-4c72-9c75-74446a6a8786	WRITE_USER	write:user
2025-06-03 14:46:41.2634+02	\N	2025-06-03 14:46:41.2634+02	eca5a19e-fcd9-47cc-bef9-3610763d03d7	DELETE_USER	delete:user
2025-06-03 14:46:41.2634+02	\N	2025-06-03 14:46:41.2634+02	cd1fe582-74ad-445e-b28f-6de1852bf516	READ_ROLE	read:role
2025-06-03 14:46:41.2634+02	\N	2025-06-03 14:46:41.2634+02	dc4cea85-9f28-4bf5-9ba5-5860a83c1e2e	WRITE_ROLE	write:role
2025-06-03 14:46:41.2634+02	\N	2025-06-03 14:46:41.2634+02	0abb6317-dccd-4571-b37a-e0d6dcf972ee	DELETE_ROLE	delete:role
2025-06-03 14:46:41.2634+02	\N	2025-06-03 14:46:41.2634+02	cff5e7c7-4cc5-4789-a5ff-d89cf7aaac2d	READ_PERMISSION	read:permission
2025-06-03 14:46:41.2634+02	\N	2025-06-03 14:46:41.2634+02	6b8b0261-e10e-43ff-8ea7-2c72a549174f	WRITE_PERMISSION	write:permission
2025-06-03 14:46:41.2634+02	\N	2025-06-03 14:46:41.2634+02	64efd765-6314-439d-890e-0f744f9755f5	DELETE_PERMISSION	delete:permission
\.


--
-- TOC entry 3609 (class 0 OID 1597302)
-- Dependencies: 207
-- Data for Name: authorities_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.authorities_roles (authorities_id, roles_id) FROM stdin;
\.


--
-- TOC entry 3610 (class 0 OID 1597305)
-- Dependencies: 208
-- Data for Name: bank_information; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bank_information (created_at, deleted_at, updated_at, id, bank_name, beneficiary_name, rib) FROM stdin;
\.


--
-- TOC entry 3611 (class 0 OID 1597313)
-- Dependencies: 209
-- Data for Name: blog_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.blog_categories (created_at, deleted_at, updated_at, id, name, slug) FROM stdin;
\.


--
-- TOC entry 3612 (class 0 OID 1597321)
-- Dependencies: 210
-- Data for Name: box_items; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.box_items (amount, currency, quantity, created_at, deleted_at, updated_at, box_id, id, product_id) FROM stdin;
\.


--
-- TOC entry 3613 (class 0 OID 1597326)
-- Dependencies: 211
-- Data for Name: boxes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.boxes (created_at, deleted_at, updated_at, id, type) FROM stdin;
\.


--
-- TOC entry 3614 (class 0 OID 1597332)
-- Dependencies: 212
-- Data for Name: cities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cities (created_at, deleted_at, updated_at, id, state_id, name) FROM stdin;
2025-06-03 14:46:41.341496+02	\N	2025-06-03 14:46:41.341496+02	f3c8d734-8ba6-4b60-8f77-fcb87bdaace5	bdcaae39-2bd2-4a78-8cfb-bb88cf3aafd3	casablanca
2025-06-03 14:46:41.39201+02	\N	2025-06-03 14:46:41.39201+02	55c5f5fa-297d-4a57-a4aa-40f83cfc2d74	bdcaae39-2bd2-4a78-8cfb-bb88cf3aafd3	settat
2025-06-03 14:46:41.428205+02	\N	2025-06-03 14:46:41.428205+02	912f9392-3d64-4cc8-b339-38da4c4b4097	bdcaae39-2bd2-4a78-8cfb-bb88cf3aafd3	mohammedia
2025-06-03 14:46:41.472237+02	\N	2025-06-03 14:46:41.472237+02	ab1a90ee-5dbc-4859-aa37-4013fd1b2188	04d08167-bdef-4191-9869-b60a4ef748fe	rabat
2025-06-03 14:46:41.498251+02	\N	2025-06-03 14:46:41.498251+02	a43c2890-be4e-4b7f-be2f-9b13acab0171	04d08167-bdef-4191-9869-b60a4ef748fe	salé
2025-06-03 14:46:41.528505+02	\N	2025-06-03 14:46:41.528505+02	ea6b1cd5-67eb-4439-862b-f789f906e681	04d08167-bdef-4191-9869-b60a4ef748fe	kénitra
2025-06-03 14:46:41.576539+02	\N	2025-06-03 14:46:41.576539+02	2d1ec079-107c-46dc-acec-e3a50d971b56	97cef3b8-1f42-4d31-9b25-ebd077017895	marrakesh
2025-06-03 14:46:41.610563+02	\N	2025-06-03 14:46:41.610563+02	5c59dc70-926a-49b0-8ae5-bb1f11466512	97cef3b8-1f42-4d31-9b25-ebd077017895	safi
2025-06-03 14:46:41.644018+02	\N	2025-06-03 14:46:41.644018+02	4444b736-5004-4e5b-8828-3b961d975afb	97cef3b8-1f42-4d31-9b25-ebd077017895	essaouira
\.


--
-- TOC entry 3615 (class 0 OID 1597337)
-- Dependencies: 213
-- Data for Name: commissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.commissions (card, cash, delivery_amount, delivery_commission, created_at, deleted_at, updated_at, id) FROM stdin;
\.


--
-- TOC entry 3616 (class 0 OID 1597342)
-- Dependencies: 214
-- Data for Name: contact; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.contact (is_responsible, created_at, deleted_at, updated_at, id, organization_id, email, first_name, last_name, phone) FROM stdin;
\.


--
-- TOC entry 3617 (class 0 OID 1597350)
-- Dependencies: 215
-- Data for Name: contracts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.contracts (commission_payed_by_sub_entities, max_number_of_accounts, max_number_of_sub_entities, minimum_reduction, single_subscription, subscription_payed_by_sub_entities, created_at, deleted_at, updated_at, id, contract_status, name, document) FROM stdin;
\.


--
-- TOC entry 3618 (class 0 OID 1597359)
-- Dependencies: 216
-- Data for Name: countries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.countries (created_at, deleted_at, updated_at, id, name) FROM stdin;
2025-06-03 14:46:41.285885+02	\N	2025-06-03 14:46:41.285885+02	cb2da684-4cf7-4b77-9729-182ec2c872b6	morocco
\.


--
-- TOC entry 3619 (class 0 OID 1597364)
-- Dependencies: 217
-- Data for Name: coupons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coupons (discount, is_enabled, created_at, deleted_at, ends_at, updated_at, id, sub_entity_id, code) FROM stdin;
\.


--
-- TOC entry 3620 (class 0 OID 1597369)
-- Dependencies: 218
-- Data for Name: covered_zones; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.covered_zones (created_at, deleted_at, updated_at, id, organization_entity_id, region_id) FROM stdin;
\.


--
-- TOC entry 3621 (class 0 OID 1597374)
-- Dependencies: 219
-- Data for Name: deadlines; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.deadlines (amount, currency, due_date, emitter_id, created_at, deleted_at, updated_at, id, parent_partner_id, payment_method_id, subscription_id, payment_responsibility, status) FROM stdin;
\.


--
-- TOC entry 3622 (class 0 OID 1597386)
-- Dependencies: 220
-- Data for Name: deals; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.deals (amount, currency, quantity, created_at, deleted_at, updated_at, id, product_id) FROM stdin;
\.


--
-- TOC entry 3623 (class 0 OID 1597391)
-- Dependencies: 221
-- Data for Name: deletion_reason; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.deletion_reason (created_at, deleted_at, updated_at, id, details, action_type, reason_type) FROM stdin;
\.


--
-- TOC entry 3624 (class 0 OID 1597401)
-- Dependencies: 222
-- Data for Name: deliveries; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.deliveries (delivery_boy_id, rating, status, created_at, deleted_at, updated_at, delivery_position_id, id) FROM stdin;
\.


--
-- TOC entry 3625 (class 0 OID 1597409)
-- Dependencies: 223
-- Data for Name: delivery_positions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.delivery_positions (latitude, longitude, created_at, deleted_at, updated_at, id) FROM stdin;
\.


--
-- TOC entry 3626 (class 0 OID 1597414)
-- Dependencies: 224
-- Data for Name: donation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.donation (status, created_at, deleted_at, updated_at, activity_id, donor_id, id, offerable_id, receiver_id, description, donor_type, offerable_type, receiver_type) FROM stdin;
\.


--
-- TOC entry 3627 (class 0 OID 1597426)
-- Dependencies: 225
-- Data for Name: event; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.event (lead_id, created_at, deleted_at, updated_at, id, message, date_and_hour, object) FROM stdin;
\.


--
-- TOC entry 3628 (class 0 OID 1597434)
-- Dependencies: 226
-- Data for Name: event_publication; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.event_publication (completion_date, publication_date, id, event_type, listener_id, serialized_event) FROM stdin;
\.


--
-- TOC entry 3629 (class 0 OID 1597442)
-- Dependencies: 227
-- Data for Name: features; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.features (created_at, deleted_at, updated_at, id, name) FROM stdin;
2025-06-03 14:46:41.725569+02	\N	2025-06-03 14:46:41.725569+02	2c1c9780-8208-428d-bb78-7f56f8421077	seller_pro
2025-06-03 14:46:41.731566+02	\N	2025-06-03 14:46:41.731566+02	db32b682-9e2f-4e7c-a7b6-2f3792073597	buyer_pro
\.


--
-- TOC entry 3602 (class 0 OID 1597247)
-- Dependencies: 200
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
\.


--
-- TOC entry 3630 (class 0 OID 1597447)
-- Dependencies: 228
-- Data for Name: notifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notifications (user_id, created_at, deleted_at, updated_at, entity_id, id, sub_entity_id, content, title) FROM stdin;
\.


--
-- TOC entry 3631 (class 0 OID 1597455)
-- Dependencies: 229
-- Data for Name: offers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.offers (price_amount, price_currency, reduction, sale_price_amount, sale_price_currency, created_at, deleted_at, updated_at, activity_id, id, offerable_id, publisher_id, barcode, image_path, offerable_type, publisher_type, title) FROM stdin;
\.


--
-- TOC entry 3632 (class 0 OID 1597465)
-- Dependencies: 230
-- Data for Name: open_time; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.open_time (created_at, deleted_at, updated_at, donation_id, id, offer_id, day, end_time, start_time) FROM stdin;
\.


--
-- TOC entry 3633 (class 0 OID 1597473)
-- Dependencies: 231
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (amount, client_id, currency, quantity, created_at, deleted_at, updated_at, coupon_id, delivery_id, id, offer_id, shipping_address_id, order_type, status) FROM stdin;
\.


--
-- TOC entry 3634 (class 0 OID 1597483)
-- Dependencies: 232
-- Data for Name: organization_entities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization_entities (created_at, deleted_at, updated_at, address_id, bank_information, contract_id, id, avatar_path, commercial_number, cover_path, name, type) FROM stdin;
2025-06-03 14:46:41.738576+02	\N	2025-06-03 14:46:41.738576+02	\N	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	\N	\N	manager test	\N
\.


--
-- TOC entry 3635 (class 0 OID 1597498)
-- Dependencies: 233
-- Data for Name: organization_entities_activities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization_entities_activities (activities_id, organization_entities_id) FROM stdin;
\.


--
-- TOC entry 3636 (class 0 OID 1597503)
-- Dependencies: 234
-- Data for Name: organization_entities_commissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization_entities_commissions (commissions_id, organization_entity_id) FROM stdin;
\.


--
-- TOC entry 3637 (class 0 OID 1597508)
-- Dependencies: 235
-- Data for Name: organization_entities_deletion_reasons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization_entities_deletion_reasons (deletion_reasons_id, organization_entity_id) FROM stdin;
\.


--
-- TOC entry 3638 (class 0 OID 1597513)
-- Dependencies: 236
-- Data for Name: organization_entities_features; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization_entities_features (features_id, organization_entities_id) FROM stdin;
\.


--
-- TOC entry 3639 (class 0 OID 1597518)
-- Dependencies: 237
-- Data for Name: organization_entities_solutions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization_entities_solutions (organization_entities_id, solutions_id) FROM stdin;
\.


--
-- TOC entry 3640 (class 0 OID 1597523)
-- Dependencies: 238
-- Data for Name: organization_entities_subscriptions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.organization_entities_subscriptions (organization_entity_id, subscriptions_id) FROM stdin;
\.


--
-- TOC entry 3641 (class 0 OID 1597528)
-- Dependencies: 239
-- Data for Name: partner_commissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.partner_commissions (emitter_id, created_at, date, deleted_at, recuperation_date, updated_at, id, organization_id, parent_partner_id, partner_id, payment_method_id, partner_name, partner_type, payment_direction, payment_responsibility, payment_status) FROM stdin;
\.


--
-- TOC entry 3642 (class 0 OID 1597542)
-- Dependencies: 240
-- Data for Name: payment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.payment (payments_with_card, payments_with_cash, created_at, deleted_at, number_of_orders, updated_at, id, organization_entity_id, sub_entity_id, date, partner_type, payment_status) FROM stdin;
\.


--
-- TOC entry 3643 (class 0 OID 1597552)
-- Dependencies: 241
-- Data for Name: payment_method; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.payment_method (amount, currency, created_at, deadline_date, deleted_at, payed_at, recuperation_date, updated_at, id, method_type, bank, card_holder_name, card_number, cheque_document, cheque_number, document_path, issuer, payment_id) FROM stdin;
\.


--
-- TOC entry 3644 (class 0 OID 1597560)
-- Dependencies: 242
-- Data for Name: product_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product_categories (created_at, deleted_at, updated_at, activity_id, id, name, slug) FROM stdin;
\.


--
-- TOC entry 3645 (class 0 OID 1597568)
-- Dependencies: 243
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (amount, currency, product_type, created_at, deleted_at, end_date, updated_at, category_id, id, barcode, description, name, product_image_type, slug, title) FROM stdin;
\.


--
-- TOC entry 3646 (class 0 OID 1597577)
-- Dependencies: 244
-- Data for Name: prospect; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect (owner_id, lead_id, created_at, deleted_at, updated_at, address_id, id, name, status, type) FROM stdin;
\.


--
-- TOC entry 3647 (class 0 OID 1597589)
-- Dependencies: 245
-- Data for Name: prospect_activities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_activities (activities_id, prospects_id) FROM stdin;
\.


--
-- TOC entry 3648 (class 0 OID 1597594)
-- Dependencies: 246
-- Data for Name: prospect_contacts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_contacts (contacts_id, prospect_id) FROM stdin;
\.


--
-- TOC entry 3649 (class 0 OID 1597599)
-- Dependencies: 247
-- Data for Name: prospect_deletion_reasons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_deletion_reasons (deletion_reasons_id, prospect_id) FROM stdin;
\.


--
-- TOC entry 3650 (class 0 OID 1597604)
-- Dependencies: 248
-- Data for Name: prospect_events; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_events (events_id, prospect_id) FROM stdin;
\.


--
-- TOC entry 3651 (class 0 OID 1597609)
-- Dependencies: 249
-- Data for Name: prospect_solutions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prospect_solutions (prospects_id, solutions_id) FROM stdin;
\.


--
-- TOC entry 3652 (class 0 OID 1597614)
-- Dependencies: 250
-- Data for Name: rayon; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rayon (created_at, deleted_at, updated_at, id, name) FROM stdin;
2025-06-03 14:46:41.076745+02	\N	2025-06-03 14:46:41.076745+02	d410cfaa-39b7-47be-b346-2b13a5d00338	fromagerie
2025-06-03 14:46:41.121146+02	\N	2025-06-03 14:46:41.121146+02	f2d16d84-79ae-4dad-9e7a-b0127a26c9b6	boulangerie
2025-06-03 14:46:41.123162+02	\N	2025-06-03 14:46:41.123162+02	af0e681e-ebd9-4222-bb8d-c53351e79626	patisserie
2025-06-03 14:46:41.125076+02	\N	2025-06-03 14:46:41.125076+02	57a73c2a-637b-47cd-a60a-dc570191d72e	charcuterie
\.


--
-- TOC entry 3653 (class 0 OID 1597619)
-- Dependencies: 251
-- Data for Name: region; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.region (created_at, deleted_at, updated_at, city_id, id, name) FROM stdin;
2025-06-03 14:46:41.360007+02	\N	2025-06-03 14:46:41.360007+02	f3c8d734-8ba6-4b60-8f77-fcb87bdaace5	84ddc999-d9b5-4320-a202-a263701eba00	maarif
2025-06-03 14:46:41.373009+02	\N	2025-06-03 14:46:41.373009+02	f3c8d734-8ba6-4b60-8f77-fcb87bdaace5	07aea3ef-1146-4bf9-9cc3-434dabc77609	hay hassani
2025-06-03 14:46:41.383007+02	\N	2025-06-03 14:46:41.383007+02	f3c8d734-8ba6-4b60-8f77-fcb87bdaace5	5d20da1e-beb4-4e98-a5b4-3de9aa36525e	anfa
2025-06-03 14:46:41.40301+02	\N	2025-06-03 14:46:41.40301+02	55c5f5fa-297d-4a57-a4aa-40f83cfc2d74	d9fce055-0726-49e9-8a45-f04034f16e7d	sidi bouzid
2025-06-03 14:46:41.413008+02	\N	2025-06-03 14:46:41.413008+02	55c5f5fa-297d-4a57-a4aa-40f83cfc2d74	3aa44808-7ecf-40b1-8389-4da6827d8844	sidi yahya
2025-06-03 14:46:41.42101+02	\N	2025-06-03 14:46:41.42101+02	55c5f5fa-297d-4a57-a4aa-40f83cfc2d74	d47bb956-e33e-4a66-bbc3-fb84831682c9	bouznika
2025-06-03 14:46:41.441766+02	\N	2025-06-03 14:46:41.441766+02	912f9392-3d64-4cc8-b339-38da4c4b4097	c347b5a2-4be2-4dbe-9cd2-16ac1751d8fc	ain sebaa
2025-06-03 14:46:41.448769+02	\N	2025-06-03 14:46:41.448769+02	912f9392-3d64-4cc8-b339-38da4c4b4097	4359186a-da72-4e89-a339-e9762f29a303	bouskoura
2025-06-03 14:46:41.456061+02	\N	2025-06-03 14:46:41.456061+02	912f9392-3d64-4cc8-b339-38da4c4b4097	1f45ca82-4e6d-4570-8386-8f63989045ed	had soualem
2025-06-03 14:46:41.482072+02	\N	2025-06-03 14:46:41.482072+02	ab1a90ee-5dbc-4859-aa37-4013fd1b2188	db602f7d-fc91-4353-96d7-77cb7e4a4330	agdal
2025-06-03 14:46:41.488252+02	\N	2025-06-03 14:46:41.488252+02	ab1a90ee-5dbc-4859-aa37-4013fd1b2188	eb599342-1e6b-4975-9535-22d74712ae61	hay riad
2025-06-03 14:46:41.493251+02	\N	2025-06-03 14:46:41.493251+02	ab1a90ee-5dbc-4859-aa37-4013fd1b2188	527c9217-cdf9-41f2-b872-96f7d9580728	souissi
2025-06-03 14:46:41.509504+02	\N	2025-06-03 14:46:41.509504+02	a43c2890-be4e-4b7f-be2f-9b13acab0171	6345c634-fecc-45c6-b0d3-d3db4e9bad36	tabriquet
2025-06-03 14:46:41.514508+02	\N	2025-06-03 14:46:41.514508+02	a43c2890-be4e-4b7f-be2f-9b13acab0171	5e621668-94b5-4e64-8f01-2321f1d40f55	bettana
2025-06-03 14:46:41.522506+02	\N	2025-06-03 14:46:41.522506+02	a43c2890-be4e-4b7f-be2f-9b13acab0171	0a74ca13-3e32-4ccc-92e0-bcea3107bff2	hassan
2025-06-03 14:46:41.539516+02	\N	2025-06-03 14:46:41.539516+02	ea6b1cd5-67eb-4439-862b-f789f906e681	6ee88209-2c5d-4b10-b576-286dfeba601d	mehdia
2025-06-03 14:46:41.547515+02	\N	2025-06-03 14:46:41.547515+02	ea6b1cd5-67eb-4439-862b-f789f906e681	e68b9bab-42cb-462b-b137-0d978d53d6b5	gharb
2025-06-03 14:46:41.556052+02	\N	2025-06-03 14:46:41.556052+02	ea6b1cd5-67eb-4439-862b-f789f906e681	95d91ba8-1da4-4557-b0d6-7bce24bc3a2e	larache
2025-06-03 14:46:41.587539+02	\N	2025-06-03 14:46:41.587539+02	2d1ec079-107c-46dc-acec-e3a50d971b56	d5119573-f455-4ee6-b8c1-0e96cc06c507	guéliz
2025-06-03 14:46:41.595558+02	\N	2025-06-03 14:46:41.595558+02	2d1ec079-107c-46dc-acec-e3a50d971b56	2127326a-e96b-4078-88a3-a19381552da7	medina
2025-06-03 14:46:41.60354+02	\N	2025-06-03 14:46:41.60354+02	2d1ec079-107c-46dc-acec-e3a50d971b56	78bd0dc6-1cce-4d3b-9734-5db33e9e0dcd	menara
2025-06-03 14:46:41.626539+02	\N	2025-06-03 14:46:41.626539+02	5c59dc70-926a-49b0-8ae5-bb1f11466512	4ac51169-2c51-4d30-901d-e783543622bb	jamaâ el fna
2025-06-03 14:46:41.634983+02	\N	2025-06-03 14:46:41.634983+02	5c59dc70-926a-49b0-8ae5-bb1f11466512	9bf00b65-6ed2-4734-819b-f7c8c13d4e00	kasbah
2025-06-03 14:46:41.656537+02	\N	2025-06-03 14:46:41.656537+02	4444b736-5004-4e5b-8828-3b961d975afb	651c4082-9102-44b3-a7ce-438f466fce43	zerktouni
2025-06-03 14:46:41.665537+02	\N	2025-06-03 14:46:41.665537+02	4444b736-5004-4e5b-8828-3b961d975afb	38fa151f-95e5-49d3-83de-ce3a02102bc6	sidi youssef ben ali
2025-06-03 14:46:41.67487+02	\N	2025-06-03 14:46:41.67487+02	4444b736-5004-4e5b-8828-3b961d975afb	cc512ccb-3817-4927-b229-b51d21e838d6	chichaoua
\.


--
-- TOC entry 3654 (class 0 OID 1597624)
-- Dependencies: 252
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (created_at, deleted_at, updated_at, id, name) FROM stdin;
2025-06-03 14:46:41.132148+02	\N	2025-06-03 14:46:41.132148+02	cb8889b1-9d5b-4fdf-9aa6-36bc153bd019	ADMIN
2025-06-03 14:46:41.132148+02	\N	2025-06-03 14:46:41.132148+02	2bdcec82-2aab-4f51-9534-a289a202aa55	SUPER_ADMIN
2025-06-03 14:46:41.132148+02	\N	2025-06-03 14:46:41.132148+02	83f11a51-66d7-4229-a5ec-dbb53ab823da	MANAGER
2025-06-03 14:46:41.132148+02	\N	2025-06-03 14:46:41.132148+02	603b1315-06d9-48fe-bfca-6864f98353d9	SALES_MANAGER
2025-06-03 14:46:41.132148+02	\N	2025-06-03 14:46:41.132148+02	1a836b00-b793-4363-b718-b9b1f307ad39	CLIENT
2025-06-03 14:46:41.132148+02	\N	2025-06-03 14:46:41.132148+02	be381b90-b4f6-4447-a813-64d6db184bd7	DELIVERY_MAN
2025-06-03 14:46:41.132148+02	\N	2025-06-03 14:46:41.132148+02	03e591bb-1c98-4766-a020-4d4700e8d9b9	LEAD
\.


--
-- TOC entry 3655 (class 0 OID 1597629)
-- Dependencies: 253
-- Data for Name: solution_contracts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.solution_contracts (created_at, deleted_at, updated_at, comission_id, contract_id, id, solution_id, subscription_id) FROM stdin;
\.


--
-- TOC entry 3656 (class 0 OID 1597636)
-- Dependencies: 254
-- Data for Name: solutions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.solutions (created_at, deleted_at, updated_at, id, name) FROM stdin;
2025-06-03 14:46:43.064907+02	\N	2025-06-03 14:46:43.064907+02	54294820-d298-42dd-a98f-eee1e1db6ca0	pro_market
2025-06-03 14:46:43.069911+02	\N	2025-06-03 14:46:43.069911+02	c2909f60-3797-4e0d-b6b1-66b9d1877992	pro_donate
2025-06-03 14:46:43.072911+02	\N	2025-06-03 14:46:43.072911+02	ca279f28-c3ea-4f97-b6be-256ea94f9a3a	pro_dlc
\.


--
-- TOC entry 3657 (class 0 OID 1597641)
-- Dependencies: 255
-- Data for Name: states; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.states (created_at, deleted_at, updated_at, country_id, id, name) FROM stdin;
2025-06-03 14:46:41.311484+02	\N	2025-06-03 14:46:41.311484+02	cb2da684-4cf7-4b77-9729-182ec2c872b6	bdcaae39-2bd2-4a78-8cfb-bb88cf3aafd3	casablanca-settat
2025-06-03 14:46:41.462059+02	\N	2025-06-03 14:46:41.462059+02	cb2da684-4cf7-4b77-9729-182ec2c872b6	04d08167-bdef-4191-9869-b60a4ef748fe	rabat-salé-kénitra
2025-06-03 14:46:41.563037+02	\N	2025-06-03 14:46:41.563037+02	cb2da684-4cf7-4b77-9729-182ec2c872b6	97cef3b8-1f42-4d31-9b25-ebd077017895	marrakesh-safi
\.


--
-- TOC entry 3658 (class 0 OID 1597646)
-- Dependencies: 256
-- Data for Name: sub_entities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sub_entities (latitude, longitude, created_at, deleted_at, updated_at, address_id, contract_id, id, organization_entity_id, avatar_path, cover_path, name, type) FROM stdin;
\.


--
-- TOC entry 3659 (class 0 OID 1597657)
-- Dependencies: 257
-- Data for Name: sub_entities_activities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sub_entities_activities (activities_id, sub_entities_id) FROM stdin;
\.


--
-- TOC entry 3660 (class 0 OID 1597662)
-- Dependencies: 258
-- Data for Name: sub_entities_commissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sub_entities_commissions (commissions_id, sub_entity_id) FROM stdin;
\.


--
-- TOC entry 3661 (class 0 OID 1597667)
-- Dependencies: 259
-- Data for Name: sub_entities_contacts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sub_entities_contacts (contacts_id, sub_entity_id) FROM stdin;
\.


--
-- TOC entry 3662 (class 0 OID 1597672)
-- Dependencies: 260
-- Data for Name: sub_entities_deletion_reasons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sub_entities_deletion_reasons (deletion_reasons_id, sub_entity_id) FROM stdin;
\.


--
-- TOC entry 3663 (class 0 OID 1597677)
-- Dependencies: 261
-- Data for Name: sub_entities_solutions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sub_entities_solutions (solutions_id, sub_entities_id) FROM stdin;
\.


--
-- TOC entry 3664 (class 0 OID 1597682)
-- Dependencies: 262
-- Data for Name: sub_entities_subscriptions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sub_entities_subscriptions (sub_entity_id, subscriptions_id) FROM stdin;
\.


--
-- TOC entry 3665 (class 0 OID 1597687)
-- Dependencies: 263
-- Data for Name: subscription; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.subscription (amount, amount_currency, duration, end_date, number_of_due_dates, start_date, created_at, deleted_at, updated_at, id, organization_id, parent_partner_id, partner_id, partner_name, partner_type, subscription_status) FROM stdin;
\.


--
-- TOC entry 3666 (class 0 OID 1597697)
-- Dependencies: 264
-- Data for Name: subscription_solutions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.subscription_solutions (solutions_id, subscription_id) FROM stdin;
\.


--
-- TOC entry 3667 (class 0 OID 1597702)
-- Dependencies: 265
-- Data for Name: transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transactions (amount, currency, created_at, deleted_at, updated_at, id, order_id, context, payment_id, reference, status, type) FROM stdin;
\.


--
-- TOC entry 3668 (class 0 OID 1597714)
-- Dependencies: 266
-- Data for Name: user_activities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_activities (user_id, created_at, deleted_at, updated_at, activity_id, id) FROM stdin;
\.


--
-- TOC entry 3669 (class 0 OID 1597719)
-- Dependencies: 267
-- Data for Name: user_contract; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_contract (user_id, created_at, deleted_at, updated_at, contract_id, id) FROM stdin;
\.


--
-- TOC entry 3670 (class 0 OID 1597726)
-- Dependencies: 268
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (date_of_birth, id, is_email_verified, responsible_id, status, created_at, deleted_at, updated_at, account_id, address_id, organization_entity_id, rayon_id, role_id, sub_entity_id, avatar_path, email, first_name, gender, last_name, national_id, nationality, password, phone) FROM stdin;
\N	1	t	\N	\N	2025-06-03 14:46:41.771823+02	\N	2025-06-03 14:46:41.771823+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	2bdcec82-2aab-4f51-9534-a289a202aa55	\N	https://media.licdn.com/dms/image/v2/C5603AQE4qIqZ7BP72g/profile-displayphoto-shrink_800_800/profile-displayphoto-shrink_800_800/0/1648051607490?e=1738195200&v=beta&t=bBOsxoXBYopItC5__X8qt0k1hKKZv6JQni1JcQms3F4	admin-test1@gmail.com	yassine	\N	ben	\N	\N	$2a$10$ltP2aNZAkUT1KzAmoAbLA.Y9rir96eoNjVTaPA.yHPNbSKWK5WZ/G	2126000000000
\N	2	t	\N	\N	2025-06-03 14:46:41.949404+02	\N	2025-06-03 14:46:41.949404+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	2bdcec82-2aab-4f51-9534-a289a202aa55	\N	https://cdn.vectorstock.com/i/1000v/74/57/green-user-icon-vector-42797457.jpg	admin-test2@gmail.com	Manare	\N	Saidi	\N	\N	$2a$10$CJk1.5qEIx.f.16/imC7n.IJq9LkEt6J50Lo3jeKW3ytr/VO.fCiG	2126000000000
\N	3	t	\N	\N	2025-06-03 14:46:42.048584+02	\N	2025-06-03 14:46:42.048584+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	2bdcec82-2aab-4f51-9534-a289a202aa55	\N	https://media.licdn.com/dms/image/v2/C4D03AQFnXZY6Eoy4ng/profile-displayphoto-shrink_800_800/profile-displayphoto-shrink_800_800/0/1642890536880?e=1738195200&v=beta&t=BtvhNrnNYKdG99-YZ74c0XNL6tsDvvAqM2_APUJI9YQ	admin-test3@gmail.com	driss	\N	machkour	\N	\N	$2a$10$xuuHuDxoRhmelxJZeqpCqe62cmH9YbEMvKwEh.GQOvllzXLKEeTQ6	2126000000000
\N	4	t	\N	\N	2025-06-03 14:46:42.140223+02	\N	2025-06-03 14:46:42.140223+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	2bdcec82-2aab-4f51-9534-a289a202aa55	\N	https://cdn.vectorstock.com/i/1000v/74/57/green-user-icon-vector-42797457.jpg	admin-test4@gmail.com	Zakaria	\N	aabou	\N	\N	$2a$10$3hzZ07uiQZaqK22kvlrR6ebbFBVDpxjqIRdvmw8SIvzinf6/iTMje	2126000000000
\N	5	t	\N	\N	2025-06-03 14:46:42.2293+02	\N	2025-06-03 14:46:42.2293+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	2bdcec82-2aab-4f51-9534-a289a202aa55	\N	https://cdn.vectorstock.com/i/1000v/74/57/green-user-icon-vector-42797457.jpg	admin-test5@gmail.com	Ayman	\N	1337	\N	\N	$2a$10$DtG0pSWOwa13AjOKW3V5be6VEl1JAn3UaAZdM6MlLFmp.mph6APq2	2126000000000
\N	6	t	\N	\N	2025-06-03 14:46:42.310735+02	\N	2025-06-03 14:46:42.310735+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	603b1315-06d9-48fe-bfca-6864f98353d9	\N	https://randomuser.me/api/portraits/men/25.jpg	driis.sabir@example.com	Driis	\N	Sabir	\N	\N	$2a$10$50q3Bwv1NpiaD76gc3BMeOF92Rc/7QKslAYGd5w4zPq/046SE1n26	+2126164971
\N	7	t	\N	\N	2025-06-03 14:46:42.416561+02	\N	2025-06-03 14:46:42.416561+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	603b1315-06d9-48fe-bfca-6864f98353d9	\N	https://randomuser.me/api/portraits/men/22.jpg	amine.el@example.com	Amine	\N	El	\N	\N	$2a$10$eyR9/HDIak5unThYQBz4t.qDCZq4EyGqHFhtmmhGaQSlTTP37szc2	+2126022223
\N	8	t	\N	\N	2025-06-03 14:46:42.502973+02	\N	2025-06-03 14:46:42.502973+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	603b1315-06d9-48fe-bfca-6864f98353d9	\N	https://randomuser.me/api/portraits/men/26.jpg	youssef.bennouna@example.com	Youssef	\N	Bennouna	\N	\N	$2a$10$jhwTGfuvrrQJqSil0R1vleQrNMYWAM8JR1rwb7s4zIIwZCnF/j8wa	+2126281887
\N	9	t	\N	\N	2025-06-03 14:46:42.593412+02	\N	2025-06-03 14:46:42.593412+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	603b1315-06d9-48fe-bfca-6864f98353d9	\N	https://randomuser.me/api/portraits/men/24.jpg	omar.ait@example.com	Omar	\N	Ait	\N	\N	$2a$10$tPSKYSE2UE4tHJrU4h1NCuCaIKCThwpnzkr5ZhVuamJsRzzBezzES	+2126918580
\N	10	t	\N	\N	2025-06-03 14:46:42.701299+02	\N	2025-06-03 14:46:42.701299+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	603b1315-06d9-48fe-bfca-6864f98353d9	\N	https://randomuser.me/api/portraits/men/24.jpg	hamza.lahlou@example.com	Hamza	\N	Lahlou	\N	\N	$2a$10$2FKDqJuSI6FqVulRlAYNm.ClzeRalehuDNiVGVsMQ.uRtVgrTCOEy	+2126176450
\N	11	t	\N	\N	2025-06-03 14:46:42.78448+02	\N	2025-06-03 14:46:42.78448+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	603b1315-06d9-48fe-bfca-6864f98353d9	\N	https://randomuser.me/api/portraits/men/24.jpg	ali.arahou@example.com	Ali	\N	Arahou	\N	\N	$2a$10$YPkYspa37Qq2Gv3LdkGWsefYflKKYjw60WlkI7dUP88A3TvfgLEae	+2126899640
\N	12	t	\N	\N	2025-06-03 14:46:42.868334+02	\N	2025-06-03 14:46:42.868334+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	603b1315-06d9-48fe-bfca-6864f98353d9	\N	https://randomuser.me/api/portraits/men/22.jpg	mohammed.el moussaoui@example.com	Mohammed	\N	El Moussaoui	\N	\N	$2a$10$Z07Qf21H77EGnQQ6UdvLq.N2M1i6CRGS/nIKVLiuOgsxHkzAPpKzq	+2126747997
\N	13	t	\N	\N	2025-06-03 14:46:42.946559+02	\N	2025-06-03 14:46:42.946559+02	\N	\N	7cabf7a5-c655-4bb9-9a09-8d6e03d3359f	\N	603b1315-06d9-48fe-bfca-6864f98353d9	\N	https://randomuser.me/api/portraits/men/26.jpg	abdel.oubrahim@example.com	Abdel	\N	Oubrahim	\N	\N	$2a$10$ZhW1ijJDkkdQ0ymjMCi0CuvKJIDhM5CBiXAzqh95aIWFvIsKfNcOe	+2126266691
\.


--
-- TOC entry 3671 (class 0 OID 1597740)
-- Dependencies: 269
-- Data for Name: users_deletion_reasons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_deletion_reasons (user_id, deletion_reasons_id) FROM stdin;
\.


--
-- TOC entry 3672 (class 0 OID 1597745)
-- Dependencies: 270
-- Data for Name: users_solutions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_solutions (users_id, solutions_id) FROM stdin;
\.


--
-- TOC entry 3673 (class 0 OID 1597750)
-- Dependencies: 271
-- Data for Name: working_hours; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.working_hours (user_id, created_at, deleted_at, updated_at, id, afternoon_end, afternoon_start, day_of_week, morning_end, morning_start) FROM stdin;
\.


--
-- TOC entry 3679 (class 0 OID 0)
-- Dependencies: 201
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_seq', 51, true);


--
-- TOC entry 3200 (class 2606 OID 1597266)
-- Name: accounts accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (id);


--
-- TOC entry 3202 (class 2606 OID 1597275)
-- Name: activities activities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activities
    ADD CONSTRAINT activities_pkey PRIMARY KEY (id);


--
-- TOC entry 3204 (class 2606 OID 1597283)
-- Name: address address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);


--
-- TOC entry 3206 (class 2606 OID 1597291)
-- Name: article article_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.article
    ADD CONSTRAINT article_pkey PRIMARY KEY (id);


--
-- TOC entry 3208 (class 2606 OID 1597293)
-- Name: article article_slug_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.article
    ADD CONSTRAINT article_slug_key UNIQUE (slug);


--
-- TOC entry 3210 (class 2606 OID 1597301)
-- Name: authorities authorities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT authorities_pkey PRIMARY KEY (id);


--
-- TOC entry 3212 (class 2606 OID 1597312)
-- Name: bank_information bank_information_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bank_information
    ADD CONSTRAINT bank_information_pkey PRIMARY KEY (id);


--
-- TOC entry 3214 (class 2606 OID 1597320)
-- Name: blog_categories blog_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.blog_categories
    ADD CONSTRAINT blog_categories_pkey PRIMARY KEY (id);


--
-- TOC entry 3216 (class 2606 OID 1597325)
-- Name: box_items box_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.box_items
    ADD CONSTRAINT box_items_pkey PRIMARY KEY (id);


--
-- TOC entry 3218 (class 2606 OID 1597331)
-- Name: boxes boxes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.boxes
    ADD CONSTRAINT boxes_pkey PRIMARY KEY (id);


--
-- TOC entry 3220 (class 2606 OID 1597336)
-- Name: cities cities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id);


--
-- TOC entry 3222 (class 2606 OID 1597341)
-- Name: commissions commissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commissions
    ADD CONSTRAINT commissions_pkey PRIMARY KEY (id);


--
-- TOC entry 3224 (class 2606 OID 1597349)
-- Name: contact contact_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (id);


--
-- TOC entry 3226 (class 2606 OID 1597358)
-- Name: contracts contracts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contracts
    ADD CONSTRAINT contracts_pkey PRIMARY KEY (id);


--
-- TOC entry 3228 (class 2606 OID 1597363)
-- Name: countries countries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.countries
    ADD CONSTRAINT countries_pkey PRIMARY KEY (id);


--
-- TOC entry 3230 (class 2606 OID 1597368)
-- Name: coupons coupons_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coupons
    ADD CONSTRAINT coupons_pkey PRIMARY KEY (id);


--
-- TOC entry 3232 (class 2606 OID 1597373)
-- Name: covered_zones covered_zones_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.covered_zones
    ADD CONSTRAINT covered_zones_pkey PRIMARY KEY (id);


--
-- TOC entry 3234 (class 2606 OID 1597385)
-- Name: deadlines deadlines_payment_method_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deadlines
    ADD CONSTRAINT deadlines_payment_method_id_key UNIQUE (payment_method_id);


--
-- TOC entry 3236 (class 2606 OID 1597383)
-- Name: deadlines deadlines_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deadlines
    ADD CONSTRAINT deadlines_pkey PRIMARY KEY (id);


--
-- TOC entry 3238 (class 2606 OID 1597390)
-- Name: deals deals_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deals
    ADD CONSTRAINT deals_pkey PRIMARY KEY (id);


--
-- TOC entry 3240 (class 2606 OID 1597400)
-- Name: deletion_reason deletion_reason_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deletion_reason
    ADD CONSTRAINT deletion_reason_pkey PRIMARY KEY (id);


--
-- TOC entry 3242 (class 2606 OID 1597408)
-- Name: deliveries deliveries_delivery_position_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deliveries
    ADD CONSTRAINT deliveries_delivery_position_id_key UNIQUE (delivery_position_id);


--
-- TOC entry 3244 (class 2606 OID 1597406)
-- Name: deliveries deliveries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deliveries
    ADD CONSTRAINT deliveries_pkey PRIMARY KEY (id);


--
-- TOC entry 3246 (class 2606 OID 1597413)
-- Name: delivery_positions delivery_positions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivery_positions
    ADD CONSTRAINT delivery_positions_pkey PRIMARY KEY (id);


--
-- TOC entry 3248 (class 2606 OID 1597425)
-- Name: donation donation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.donation
    ADD CONSTRAINT donation_pkey PRIMARY KEY (id);


--
-- TOC entry 3250 (class 2606 OID 1597433)
-- Name: event event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event
    ADD CONSTRAINT event_pkey PRIMARY KEY (id);


--
-- TOC entry 3252 (class 2606 OID 1597441)
-- Name: event_publication event_publication_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event_publication
    ADD CONSTRAINT event_publication_pkey PRIMARY KEY (id);


--
-- TOC entry 3254 (class 2606 OID 1597446)
-- Name: features features_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.features
    ADD CONSTRAINT features_pkey PRIMARY KEY (id);


--
-- TOC entry 3256 (class 2606 OID 1597454)
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- TOC entry 3258 (class 2606 OID 1597464)
-- Name: offers offers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_pkey PRIMARY KEY (id);


--
-- TOC entry 3260 (class 2606 OID 1597472)
-- Name: open_time open_time_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.open_time
    ADD CONSTRAINT open_time_pkey PRIMARY KEY (id);


--
-- TOC entry 3262 (class 2606 OID 1597482)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 3272 (class 2606 OID 1597502)
-- Name: organization_entities_activities organization_entities_activities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_activities
    ADD CONSTRAINT organization_entities_activities_pkey PRIMARY KEY (activities_id, organization_entities_id);


--
-- TOC entry 3264 (class 2606 OID 1597493)
-- Name: organization_entities organization_entities_address_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities
    ADD CONSTRAINT organization_entities_address_id_key UNIQUE (address_id);


--
-- TOC entry 3266 (class 2606 OID 1597495)
-- Name: organization_entities organization_entities_bank_information_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities
    ADD CONSTRAINT organization_entities_bank_information_key UNIQUE (bank_information);


--
-- TOC entry 3274 (class 2606 OID 1597507)
-- Name: organization_entities_commissions organization_entities_commissions_commissions_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_commissions
    ADD CONSTRAINT organization_entities_commissions_commissions_id_key UNIQUE (commissions_id);


--
-- TOC entry 3268 (class 2606 OID 1597497)
-- Name: organization_entities organization_entities_contract_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities
    ADD CONSTRAINT organization_entities_contract_id_key UNIQUE (contract_id);


--
-- TOC entry 3276 (class 2606 OID 1597512)
-- Name: organization_entities_deletion_reasons organization_entities_deletion_reasons_deletion_reasons_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_deletion_reasons
    ADD CONSTRAINT organization_entities_deletion_reasons_deletion_reasons_id_key UNIQUE (deletion_reasons_id);


--
-- TOC entry 3278 (class 2606 OID 1597517)
-- Name: organization_entities_features organization_entities_features_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_features
    ADD CONSTRAINT organization_entities_features_pkey PRIMARY KEY (features_id, organization_entities_id);


--
-- TOC entry 3270 (class 2606 OID 1597491)
-- Name: organization_entities organization_entities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities
    ADD CONSTRAINT organization_entities_pkey PRIMARY KEY (id);


--
-- TOC entry 3280 (class 2606 OID 1597522)
-- Name: organization_entities_solutions organization_entities_solutions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_solutions
    ADD CONSTRAINT organization_entities_solutions_pkey PRIMARY KEY (organization_entities_id, solutions_id);


--
-- TOC entry 3282 (class 2606 OID 1597527)
-- Name: organization_entities_subscriptions organization_entities_subscriptions_subscriptions_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_subscriptions
    ADD CONSTRAINT organization_entities_subscriptions_subscriptions_id_key UNIQUE (subscriptions_id);


--
-- TOC entry 3284 (class 2606 OID 1597541)
-- Name: partner_commissions partner_commissions_payment_method_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partner_commissions
    ADD CONSTRAINT partner_commissions_payment_method_id_key UNIQUE (payment_method_id);


--
-- TOC entry 3286 (class 2606 OID 1597539)
-- Name: partner_commissions partner_commissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partner_commissions
    ADD CONSTRAINT partner_commissions_pkey PRIMARY KEY (id);


--
-- TOC entry 3290 (class 2606 OID 1597559)
-- Name: payment_method payment_method_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method
    ADD CONSTRAINT payment_method_pkey PRIMARY KEY (id);


--
-- TOC entry 3288 (class 2606 OID 1597551)
-- Name: payment payment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);


--
-- TOC entry 3292 (class 2606 OID 1597567)
-- Name: product_categories product_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_categories
    ADD CONSTRAINT product_categories_pkey PRIMARY KEY (id);


--
-- TOC entry 3294 (class 2606 OID 1597576)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 3300 (class 2606 OID 1597593)
-- Name: prospect_activities prospect_activities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_activities
    ADD CONSTRAINT prospect_activities_pkey PRIMARY KEY (activities_id, prospects_id);


--
-- TOC entry 3296 (class 2606 OID 1597588)
-- Name: prospect prospect_address_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_address_id_key UNIQUE (address_id);


--
-- TOC entry 3302 (class 2606 OID 1597598)
-- Name: prospect_contacts prospect_contacts_contacts_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_contacts
    ADD CONSTRAINT prospect_contacts_contacts_id_key UNIQUE (contacts_id);


--
-- TOC entry 3304 (class 2606 OID 1597603)
-- Name: prospect_deletion_reasons prospect_deletion_reasons_deletion_reasons_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_deletion_reasons
    ADD CONSTRAINT prospect_deletion_reasons_deletion_reasons_id_key UNIQUE (deletion_reasons_id);


--
-- TOC entry 3306 (class 2606 OID 1597608)
-- Name: prospect_events prospect_events_events_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_events
    ADD CONSTRAINT prospect_events_events_id_key UNIQUE (events_id);


--
-- TOC entry 3298 (class 2606 OID 1597586)
-- Name: prospect prospect_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT prospect_pkey PRIMARY KEY (id);


--
-- TOC entry 3308 (class 2606 OID 1597613)
-- Name: prospect_solutions prospect_solutions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_solutions
    ADD CONSTRAINT prospect_solutions_pkey PRIMARY KEY (prospects_id, solutions_id);


--
-- TOC entry 3310 (class 2606 OID 1597618)
-- Name: rayon rayon_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rayon
    ADD CONSTRAINT rayon_pkey PRIMARY KEY (id);


--
-- TOC entry 3312 (class 2606 OID 1597623)
-- Name: region region_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.region
    ADD CONSTRAINT region_pkey PRIMARY KEY (id);


--
-- TOC entry 3314 (class 2606 OID 1597628)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 3316 (class 2606 OID 1597635)
-- Name: solution_contracts solution_contracts_comission_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solution_contracts
    ADD CONSTRAINT solution_contracts_comission_id_key UNIQUE (comission_id);


--
-- TOC entry 3318 (class 2606 OID 1597633)
-- Name: solution_contracts solution_contracts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solution_contracts
    ADD CONSTRAINT solution_contracts_pkey PRIMARY KEY (id);


--
-- TOC entry 3320 (class 2606 OID 1597640)
-- Name: solutions solutions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solutions
    ADD CONSTRAINT solutions_pkey PRIMARY KEY (id);


--
-- TOC entry 3322 (class 2606 OID 1597645)
-- Name: states states_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.states
    ADD CONSTRAINT states_pkey PRIMARY KEY (id);


--
-- TOC entry 3328 (class 2606 OID 1597661)
-- Name: sub_entities_activities sub_entities_activities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_activities
    ADD CONSTRAINT sub_entities_activities_pkey PRIMARY KEY (activities_id, sub_entities_id);


--
-- TOC entry 3324 (class 2606 OID 1597656)
-- Name: sub_entities sub_entities_address_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities
    ADD CONSTRAINT sub_entities_address_id_key UNIQUE (address_id);


--
-- TOC entry 3330 (class 2606 OID 1597666)
-- Name: sub_entities_commissions sub_entities_commissions_commissions_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_commissions
    ADD CONSTRAINT sub_entities_commissions_commissions_id_key UNIQUE (commissions_id);


--
-- TOC entry 3332 (class 2606 OID 1597671)
-- Name: sub_entities_contacts sub_entities_contacts_contacts_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_contacts
    ADD CONSTRAINT sub_entities_contacts_contacts_id_key UNIQUE (contacts_id);


--
-- TOC entry 3334 (class 2606 OID 1597676)
-- Name: sub_entities_deletion_reasons sub_entities_deletion_reasons_deletion_reasons_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_deletion_reasons
    ADD CONSTRAINT sub_entities_deletion_reasons_deletion_reasons_id_key UNIQUE (deletion_reasons_id);


--
-- TOC entry 3326 (class 2606 OID 1597654)
-- Name: sub_entities sub_entities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities
    ADD CONSTRAINT sub_entities_pkey PRIMARY KEY (id);


--
-- TOC entry 3336 (class 2606 OID 1597681)
-- Name: sub_entities_solutions sub_entities_solutions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_solutions
    ADD CONSTRAINT sub_entities_solutions_pkey PRIMARY KEY (solutions_id, sub_entities_id);


--
-- TOC entry 3338 (class 2606 OID 1597686)
-- Name: sub_entities_subscriptions sub_entities_subscriptions_subscriptions_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_subscriptions
    ADD CONSTRAINT sub_entities_subscriptions_subscriptions_id_key UNIQUE (subscriptions_id);


--
-- TOC entry 3340 (class 2606 OID 1597696)
-- Name: subscription subscription_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscription
    ADD CONSTRAINT subscription_pkey PRIMARY KEY (id);


--
-- TOC entry 3342 (class 2606 OID 1597701)
-- Name: subscription_solutions subscription_solutions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscription_solutions
    ADD CONSTRAINT subscription_solutions_pkey PRIMARY KEY (solutions_id, subscription_id);


--
-- TOC entry 3344 (class 2606 OID 1597713)
-- Name: transactions transactions_order_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_order_id_key UNIQUE (order_id);


--
-- TOC entry 3346 (class 2606 OID 1597711)
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (id);


--
-- TOC entry 3348 (class 2606 OID 1597718)
-- Name: user_activities user_activities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT user_activities_pkey PRIMARY KEY (id);


--
-- TOC entry 3350 (class 2606 OID 1597725)
-- Name: user_contract user_contract_contract_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_contract
    ADD CONSTRAINT user_contract_contract_id_key UNIQUE (contract_id);


--
-- TOC entry 3352 (class 2606 OID 1597723)
-- Name: user_contract user_contract_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_contract
    ADD CONSTRAINT user_contract_pkey PRIMARY KEY (id);


--
-- TOC entry 3354 (class 2606 OID 1597737)
-- Name: users users_account_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_account_id_key UNIQUE (account_id);


--
-- TOC entry 3360 (class 2606 OID 1597744)
-- Name: users_deletion_reasons users_deletion_reasons_deletion_reasons_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_deletion_reasons
    ADD CONSTRAINT users_deletion_reasons_deletion_reasons_id_key UNIQUE (deletion_reasons_id);


--
-- TOC entry 3356 (class 2606 OID 1597739)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3358 (class 2606 OID 1597735)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3362 (class 2606 OID 1597749)
-- Name: users_solutions users_solutions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_solutions
    ADD CONSTRAINT users_solutions_pkey PRIMARY KEY (users_id, solutions_id);


--
-- TOC entry 3364 (class 2606 OID 1597758)
-- Name: working_hours working_hours_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.working_hours
    ADD CONSTRAINT working_hours_pkey PRIMARY KEY (id);



--
-- TOC entry 3369 (class 2606 OID 1597779)
-- Name: box_items fk19a29b56ajo5fe1sx3849nw6t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.box_items
    ADD CONSTRAINT fk19a29b56ajo5fe1sx3849nw6t FOREIGN KEY (box_id) REFERENCES public.boxes(id);


--
-- TOC entry 3456 (class 2606 OID 1598214)
-- Name: user_activities fk234dtcelry3pf97oi0hmo1cv6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT fk234dtcelry3pf97oi0hmo1cv6 FOREIGN KEY (activity_id) REFERENCES public.activities(id);


--
-- TOC entry 3452 (class 2606 OID 1598194)
-- Name: subscription fk2jaqa9jn5wenprskghioq1nul; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscription
    ADD CONSTRAINT fk2jaqa9jn5wenprskghioq1nul FOREIGN KEY (parent_partner_id) REFERENCES public.subscription(id);


--
-- TOC entry 3385 (class 2606 OID 1597859)
-- Name: notifications fk31ckcdoxhr9qpjfv89k6ux15j; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT fk31ckcdoxhr9qpjfv89k6ux15j FOREIGN KEY (entity_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3422 (class 2606 OID 1598044)
-- Name: prospect_activities fk39yulx9w1wr26dw0bhaqapobp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_activities
    ADD CONSTRAINT fk39yulx9w1wr26dw0bhaqapobp FOREIGN KEY (prospects_id) REFERENCES public.prospect(id);


--
-- TOC entry 3397 (class 2606 OID 1597919)
-- Name: organization_entities fk3qbputxf07klrhpgcesvoopt8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities
    ADD CONSTRAINT fk3qbputxf07klrhpgcesvoopt8 FOREIGN KEY (bank_information) REFERENCES public.bank_information(id);


--
-- TOC entry 3365 (class 2606 OID 1597759)
-- Name: address fk4ljdc68rnke4txup1jlf1il4l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT fk4ljdc68rnke4txup1jlf1il4l FOREIGN KEY (region_id) REFERENCES public.region(id);


--
-- TOC entry 3459 (class 2606 OID 1598229)
-- Name: user_contract fk50kiyim5t493ba62duk44e64y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_contract
    ADD CONSTRAINT fk50kiyim5t493ba62duk44e64y FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3444 (class 2606 OID 1598154)
-- Name: sub_entities_contacts fk51evp51y4lt1q15hdv0ankxim; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_contacts
    ADD CONSTRAINT fk51evp51y4lt1q15hdv0ankxim FOREIGN KEY (contacts_id) REFERENCES public.contact(id);


--
-- TOC entry 3432 (class 2606 OID 1598094)
-- Name: solution_contracts fk53ote4wsi1pq0mhby5qtx2key; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solution_contracts
    ADD CONSTRAINT fk53ote4wsi1pq0mhby5qtx2key FOREIGN KEY (comission_id) REFERENCES public.commissions(id);


--
-- TOC entry 3407 (class 2606 OID 1597969)
-- Name: organization_entities_solutions fk57xscvldv7ekcvwtk5v9wwjk3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_solutions
    ADD CONSTRAINT fk57xscvldv7ekcvwtk5v9wwjk3 FOREIGN KEY (solutions_id) REFERENCES public.solutions(id);


--
-- TOC entry 3434 (class 2606 OID 1598104)
-- Name: solution_contracts fk5bgve0yd499piici8lu3txivb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solution_contracts
    ADD CONSTRAINT fk5bgve0yd499piici8lu3txivb FOREIGN KEY (solution_id) REFERENCES public.solutions(id);


--
-- TOC entry 3421 (class 2606 OID 1598039)
-- Name: prospect_activities fk5qka2spn56e023djgug0rt0o9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_activities
    ADD CONSTRAINT fk5qka2spn56e023djgug0rt0o9 FOREIGN KEY (activities_id) REFERENCES public.activities(id);


--
-- TOC entry 3370 (class 2606 OID 1597784)
-- Name: box_items fk5qqlegy8w8xhqqnwdvc2o3syo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.box_items
    ADD CONSTRAINT fk5qqlegy8w8xhqqnwdvc2o3syo FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3435 (class 2606 OID 1598109)
-- Name: solution_contracts fk6a90ma8wtnodemkby3c8evmic; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solution_contracts
    ADD CONSTRAINT fk6a90ma8wtnodemkby3c8evmic FOREIGN KEY (subscription_id) REFERENCES public.subscription(id);


--
-- TOC entry 3417 (class 2606 OID 1598019)
-- Name: products fk6t5dtw6tyo83ywljwohuc6g7k; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fk6t5dtw6tyo83ywljwohuc6g7k FOREIGN KEY (category_id) REFERENCES public.product_categories(id);


--
-- TOC entry 3425 (class 2606 OID 1598059)
-- Name: prospect_deletion_reasons fk7b7vjbjutw929o0vokg19ch2l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_deletion_reasons
    ADD CONSTRAINT fk7b7vjbjutw929o0vokg19ch2l FOREIGN KEY (deletion_reasons_id) REFERENCES public.deletion_reason(id);


--
-- TOC entry 3383 (class 2606 OID 1597849)
-- Name: donation fk7h0hbksw3jphaq4nnooicc8dx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.donation
    ADD CONSTRAINT fk7h0hbksw3jphaq4nnooicc8dx FOREIGN KEY (activity_id) REFERENCES public.activities(id);


--
-- TOC entry 3373 (class 2606 OID 1597799)
-- Name: coupons fk83n33vrlaeam56yibmuxi685y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coupons
    ADD CONSTRAINT fk83n33vrlaeam56yibmuxi685y FOREIGN KEY (sub_entity_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3398 (class 2606 OID 1597924)
-- Name: organization_entities fk8bcmsu5dx1ew7qr9l1hwtb9f2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities
    ADD CONSTRAINT fk8bcmsu5dx1ew7qr9l1hwtb9f2 FOREIGN KEY (contract_id) REFERENCES public.contracts(id);


--
-- TOC entry 3430 (class 2606 OID 1598084)
-- Name: prospect_solutions fk8u6davo90yryelftpjxuqr617; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_solutions
    ADD CONSTRAINT fk8u6davo90yryelftpjxuqr617 FOREIGN KEY (prospects_id) REFERENCES public.prospect(id);


--
-- TOC entry 3393 (class 2606 OID 1597899)
-- Name: orders fk8w9m21riko8j8eit0yvog02nr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk8w9m21riko8j8eit0yvog02nr FOREIGN KEY (delivery_id) REFERENCES public.deliveries(id);


--
-- TOC entry 3467 (class 2606 OID 1598269)
-- Name: users_deletion_reasons fk9iyqha9egkn1od9ftdai1ievs; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_deletion_reasons
    ADD CONSTRAINT fk9iyqha9egkn1od9ftdai1ievs FOREIGN KEY (deletion_reasons_id) REFERENCES public.deletion_reason(id);


--
-- TOC entry 3367 (class 2606 OID 1597769)
-- Name: authorities_roles fk9qvds0a45jkj2vk4wb2qs6b6u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities_roles
    ADD CONSTRAINT fk9qvds0a45jkj2vk4wb2qs6b6u FOREIGN KEY (roles_id) REFERENCES public.roles(id);


--
-- TOC entry 3387 (class 2606 OID 1597869)
-- Name: notifications fk9y21adhxn0ayjhfocscqox7bh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT fk9y21adhxn0ayjhfocscqox7bh FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3449 (class 2606 OID 1598179)
-- Name: sub_entities_solutions fka3o6wnugeqyffln8iirlbmr1j; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_solutions
    ADD CONSTRAINT fka3o6wnugeqyffln8iirlbmr1j FOREIGN KEY (sub_entities_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3379 (class 2606 OID 1597829)
-- Name: deadlines fkai3noo74rlvv4p1590xtdwsk2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deadlines
    ADD CONSTRAINT fkai3noo74rlvv4p1590xtdwsk2 FOREIGN KEY (subscription_id) REFERENCES public.subscription(id);


--
-- TOC entry 3415 (class 2606 OID 1598009)
-- Name: payment fkb3j0nkf5183t9sob835cxxkge; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT fkb3j0nkf5183t9sob835cxxkge FOREIGN KEY (sub_entity_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3466 (class 2606 OID 1598264)
-- Name: users fkb3r9esyf04b8oqqx3fbkedb56; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkb3r9esyf04b8oqqx3fbkedb56 FOREIGN KEY (sub_entity_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3404 (class 2606 OID 1597954)
-- Name: organization_entities_deletion_reasons fkbbix2yfg2lm1ti2o6ctubak1u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_deletion_reasons
    ADD CONSTRAINT fkbbix2yfg2lm1ti2o6ctubak1u FOREIGN KEY (organization_entity_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3457 (class 2606 OID 1598219)
-- Name: user_activities fkbe7yq8t74yxeoarmxlxevoped; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_activities
    ADD CONSTRAINT fkbe7yq8t74yxeoarmxlxevoped FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3420 (class 2606 OID 1598034)
-- Name: prospect fkbn5vel1jrlor0s7tap189oxc6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT fkbn5vel1jrlor0s7tap189oxc6 FOREIGN KEY (lead_id) REFERENCES public.users(id);


--
-- TOC entry 3451 (class 2606 OID 1598189)
-- Name: sub_entities_subscriptions fkbrdtx3l9ndng70j19yfadoo1q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_subscriptions
    ADD CONSTRAINT fkbrdtx3l9ndng70j19yfadoo1q FOREIGN KEY (sub_entity_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3390 (class 2606 OID 1597884)
-- Name: open_time fkcbx26thjwpowwf7wgs6it9bpj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.open_time
    ADD CONSTRAINT fkcbx26thjwpowwf7wgs6it9bpj FOREIGN KEY (offer_id) REFERENCES public.offers(id);


--
-- TOC entry 3439 (class 2606 OID 1598129)
-- Name: sub_entities fkdbe5e6ryn6lp9qlkwki1kxod4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities
    ADD CONSTRAINT fkdbe5e6ryn6lp9qlkwki1kxod4 FOREIGN KEY (organization_entity_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3461 (class 2606 OID 1598239)
-- Name: users fkditu6lr4ek16tkxtdsne0gxib; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkditu6lr4ek16tkxtdsne0gxib FOREIGN KEY (address_id) REFERENCES public.address(id);


--
-- TOC entry 3380 (class 2606 OID 1597834)
-- Name: deals fkdiyxm7qwacnjy62mr34jn35p6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deals
    ADD CONSTRAINT fkdiyxm7qwacnjy62mr34jn35p6 FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3402 (class 2606 OID 1597944)
-- Name: organization_entities_commissions fke5ggl6fpohsr3ph4kgljsqo4y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_commissions
    ADD CONSTRAINT fke5ggl6fpohsr3ph4kgljsqo4y FOREIGN KEY (organization_entity_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3381 (class 2606 OID 1597839)
-- Name: deliveries fkeci9rr5xkfprha3aj8tj73f6u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deliveries
    ADD CONSTRAINT fkeci9rr5xkfprha3aj8tj73f6u FOREIGN KEY (delivery_boy_id) REFERENCES public.users(id);


--
-- TOC entry 3384 (class 2606 OID 1597854)
-- Name: event fkehvfs42uvga5s9bymvpkrw1tj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.event
    ADD CONSTRAINT fkehvfs42uvga5s9bymvpkrw1tj FOREIGN KEY (lead_id) REFERENCES public.users(id);


--
-- TOC entry 3453 (class 2606 OID 1598199)
-- Name: subscription_solutions fkel0n10y1q394svchv0twf139b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscription_solutions
    ADD CONSTRAINT fkel0n10y1q394svchv0twf139b FOREIGN KEY (solutions_id) REFERENCES public.solutions(id);


--
-- TOC entry 3410 (class 2606 OID 1597984)
-- Name: organization_entities_subscriptions fkembkp0y562yeyrt69yvjulfvi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_subscriptions
    ADD CONSTRAINT fkembkp0y562yeyrt69yvjulfvi FOREIGN KEY (organization_entity_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3447 (class 2606 OID 1598169)
-- Name: sub_entities_deletion_reasons fkexm67aha7667ivbqrqecarg14; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_deletion_reasons
    ADD CONSTRAINT fkexm67aha7667ivbqrqecarg14 FOREIGN KEY (sub_entity_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3442 (class 2606 OID 1598144)
-- Name: sub_entities_commissions fkf26bocmlsdmpmmy8v65chnm17; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_commissions
    ADD CONSTRAINT fkf26bocmlsdmpmmy8v65chnm17 FOREIGN KEY (commissions_id) REFERENCES public.partner_commissions(id);


--
-- TOC entry 3394 (class 2606 OID 1597904)
-- Name: orders fkfa6ca42wke9qjplj694yo21wc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fkfa6ca42wke9qjplj694yo21wc FOREIGN KEY (offer_id) REFERENCES public.offers(id);


--
-- TOC entry 3414 (class 2606 OID 1598004)
-- Name: payment fkffm25vle0gs68aus11pnv8df2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT fkffm25vle0gs68aus11pnv8df2 FOREIGN KEY (organization_entity_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3460 (class 2606 OID 1598234)
-- Name: users fkfm8rm8ks0kgj4fhlmmljkj17x; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkfm8rm8ks0kgj4fhlmmljkj17x FOREIGN KEY (account_id) REFERENCES public.accounts(id);


--
-- TOC entry 3431 (class 2606 OID 1598089)
-- Name: region fkfs479tu16j7jfj2htgy666mfm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.region
    ADD CONSTRAINT fkfs479tu16j7jfj2htgy666mfm FOREIGN KEY (city_id) REFERENCES public.cities(id);


--
-- TOC entry 3455 (class 2606 OID 1598209)
-- Name: transactions fkfyxndk58yiq2vpn0yd4m09kbt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT fkfyxndk58yiq2vpn0yd4m09kbt FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 3400 (class 2606 OID 1597934)
-- Name: organization_entities_activities fkgfqtgy9ktrng8rep3pqfo3sr9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_activities
    ADD CONSTRAINT fkgfqtgy9ktrng8rep3pqfo3sr9 FOREIGN KEY (organization_entities_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3408 (class 2606 OID 1597974)
-- Name: organization_entities_solutions fkgnfplgmp6f3hbpbuf5t5w4tn5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_solutions
    ADD CONSTRAINT fkgnfplgmp6f3hbpbuf5t5w4tn5 FOREIGN KEY (organization_entities_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3388 (class 2606 OID 1597874)
-- Name: offers fkgq3qhnvpsmp6ycodkchuutx2g; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fkgq3qhnvpsmp6ycodkchuutx2g FOREIGN KEY (activity_id) REFERENCES public.activities(id);


--
-- TOC entry 3426 (class 2606 OID 1598064)
-- Name: prospect_deletion_reasons fkgvjjmdtir65iwqkrbidm1vr0q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_deletion_reasons
    ADD CONSTRAINT fkgvjjmdtir65iwqkrbidm1vr0q FOREIGN KEY (prospect_id) REFERENCES public.prospect(id);


--
-- TOC entry 3395 (class 2606 OID 1597909)
-- Name: orders fkh0uue95ltjysfmkqb5abgk7tj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fkh0uue95ltjysfmkqb5abgk7tj FOREIGN KEY (shipping_address_id) REFERENCES public.address(id);


--
-- TOC entry 3468 (class 2606 OID 1598274)
-- Name: users_deletion_reasons fkh37pb1ee8h0y596naum26adp3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_deletion_reasons
    ADD CONSTRAINT fkh37pb1ee8h0y596naum26adp3 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3376 (class 2606 OID 1597814)
-- Name: deadlines fkh7d550wtixrd8yfjrvafduu0w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deadlines
    ADD CONSTRAINT fkh7d550wtixrd8yfjrvafduu0w FOREIGN KEY (emitter_id) REFERENCES public.users(id);


--
-- TOC entry 3375 (class 2606 OID 1597809)
-- Name: covered_zones fkhb9ef2imsjgjkm1u011vxdsrs; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.covered_zones
    ADD CONSTRAINT fkhb9ef2imsjgjkm1u011vxdsrs FOREIGN KEY (region_id) REFERENCES public.region(id);


--
-- TOC entry 3374 (class 2606 OID 1597804)
-- Name: covered_zones fkhn2udlgponl2ypn7ee4da221c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.covered_zones
    ADD CONSTRAINT fkhn2udlgponl2ypn7ee4da221c FOREIGN KEY (organization_entity_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3438 (class 2606 OID 1598124)
-- Name: sub_entities fki1x07b7aib37a2l9292lja55v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities
    ADD CONSTRAINT fki1x07b7aib37a2l9292lja55v FOREIGN KEY (contract_id) REFERENCES public.contracts(id);


--
-- TOC entry 3403 (class 2606 OID 1597949)
-- Name: organization_entities_deletion_reasons fki85xh1moo8nhvjvoba33jvshx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_deletion_reasons
    ADD CONSTRAINT fki85xh1moo8nhvjvoba33jvshx FOREIGN KEY (deletion_reasons_id) REFERENCES public.deletion_reason(id);


--
-- TOC entry 3464 (class 2606 OID 1598254)
-- Name: users fkicpnwuxuhyjqhu86xpj6yapdi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkicpnwuxuhyjqhu86xpj6yapdi FOREIGN KEY (responsible_id) REFERENCES public.users(id);


--
-- TOC entry 3450 (class 2606 OID 1598184)
-- Name: sub_entities_subscriptions fkir90akvuen03su42av8lb0in1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_subscriptions
    ADD CONSTRAINT fkir90akvuen03su42av8lb0in1 FOREIGN KEY (subscriptions_id) REFERENCES public.subscription(id);


--
-- TOC entry 3372 (class 2606 OID 1597794)
-- Name: contact fkjcvvyn9ru23yoe41fjaf36054; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contact
    ADD CONSTRAINT fkjcvvyn9ru23yoe41fjaf36054 FOREIGN KEY (organization_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3413 (class 2606 OID 1597999)
-- Name: partner_commissions fkjh036ur7c1qr73rgscbkbhab4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partner_commissions
    ADD CONSTRAINT fkjh036ur7c1qr73rgscbkbhab4 FOREIGN KEY (payment_method_id) REFERENCES public.payment_method(id);


--
-- TOC entry 3389 (class 2606 OID 1597879)
-- Name: open_time fkjlfwfrduxwvngbdi48xeqj2f2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.open_time
    ADD CONSTRAINT fkjlfwfrduxwvngbdi48xeqj2f2 FOREIGN KEY (donation_id) REFERENCES public.donation(id);


--
-- TOC entry 3427 (class 2606 OID 1598069)
-- Name: prospect_events fkk0x2qjrygysjf4nmmjgls3oc0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_events
    ADD CONSTRAINT fkk0x2qjrygysjf4nmmjgls3oc0 FOREIGN KEY (events_id) REFERENCES public.event(id);


--
-- TOC entry 3377 (class 2606 OID 1597819)
-- Name: deadlines fkk508tb978rbpv6jkxy1s2to44; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deadlines
    ADD CONSTRAINT fkk508tb978rbpv6jkxy1s2to44 FOREIGN KEY (parent_partner_id) REFERENCES public.deadlines(id);


--
-- TOC entry 3454 (class 2606 OID 1598204)
-- Name: subscription_solutions fkkbbujlxrkkl4bfm2lh9xj52ew; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subscription_solutions
    ADD CONSTRAINT fkkbbujlxrkkl4bfm2lh9xj52ew FOREIGN KEY (subscription_id) REFERENCES public.subscription(id);


--
-- TOC entry 3463 (class 2606 OID 1598249)
-- Name: users fkkqbr1ok9fin7lthwbmh1b8ej1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkkqbr1ok9fin7lthwbmh1b8ej1 FOREIGN KEY (rayon_id) REFERENCES public.rayon(id);


--
-- TOC entry 3440 (class 2606 OID 1598134)
-- Name: sub_entities_activities fkkuyu9n7frx4igychyi79kcpd1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_activities
    ADD CONSTRAINT fkkuyu9n7frx4igychyi79kcpd1 FOREIGN KEY (activities_id) REFERENCES public.activities(id);


--
-- TOC entry 3441 (class 2606 OID 1598139)
-- Name: sub_entities_activities fkkw79x6a002u7rg9rk61w0r7t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_activities
    ADD CONSTRAINT fkkw79x6a002u7rg9rk61w0r7t FOREIGN KEY (sub_entities_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3405 (class 2606 OID 1597959)
-- Name: organization_entities_features fkkwwpqp1f7k71pfkhvh184lj0i; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_features
    ADD CONSTRAINT fkkwwpqp1f7k71pfkhvh184lj0i FOREIGN KEY (features_id) REFERENCES public.features(id);


--
-- TOC entry 3446 (class 2606 OID 1598164)
-- Name: sub_entities_deletion_reasons fkkybdph40kfoudtd5stwpybdl4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_deletion_reasons
    ADD CONSTRAINT fkkybdph40kfoudtd5stwpybdl4 FOREIGN KEY (deletion_reasons_id) REFERENCES public.deletion_reason(id);


--
-- TOC entry 3406 (class 2606 OID 1597964)
-- Name: organization_entities_features fklk2ga0ad08c2ktxbvklws6uwa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_features
    ADD CONSTRAINT fklk2ga0ad08c2ktxbvklws6uwa FOREIGN KEY (organization_entities_id) REFERENCES public.organization_entities(id);


--
-- TOC entry 3458 (class 2606 OID 1598224)
-- Name: user_contract fklmb28qgvcvx0gb7ffrcnobxk2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_contract
    ADD CONSTRAINT fklmb28qgvcvx0gb7ffrcnobxk2 FOREIGN KEY (contract_id) REFERENCES public.contracts(id);


--
-- TOC entry 3399 (class 2606 OID 1597929)
-- Name: organization_entities_activities fklpt1fhg921kkywjfjo13bg54v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_activities
    ADD CONSTRAINT fklpt1fhg921kkywjfjo13bg54v FOREIGN KEY (activities_id) REFERENCES public.activities(id);


--
-- TOC entry 3382 (class 2606 OID 1597844)
-- Name: deliveries fkly14vmwtm1a8vbt08cx46puf5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deliveries
    ADD CONSTRAINT fkly14vmwtm1a8vbt08cx46puf5 FOREIGN KEY (delivery_position_id) REFERENCES public.delivery_positions(id);


--
-- TOC entry 3423 (class 2606 OID 1598049)
-- Name: prospect_contacts fkm3upfqd8bxopnxm1vqhb8w84a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_contacts
    ADD CONSTRAINT fkm3upfqd8bxopnxm1vqhb8w84a FOREIGN KEY (contacts_id) REFERENCES public.contact(id);


--
-- TOC entry 3409 (class 2606 OID 1597979)
-- Name: organization_entities_subscriptions fkm8c3ijtuwioqnl77nrlh3mr6p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_subscriptions
    ADD CONSTRAINT fkm8c3ijtuwioqnl77nrlh3mr6p FOREIGN KEY (subscriptions_id) REFERENCES public.subscription(id);


--
-- TOC entry 3416 (class 2606 OID 1598014)
-- Name: product_categories fkm8npyiami1kf8bk3jssxth99r; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_categories
    ADD CONSTRAINT fkm8npyiami1kf8bk3jssxth99r FOREIGN KEY (activity_id) REFERENCES public.activities(id);


--
-- TOC entry 3366 (class 2606 OID 1597764)
-- Name: article fkmj2a6kyync9cf7b1ld805914q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.article
    ADD CONSTRAINT fkmj2a6kyync9cf7b1ld805914q FOREIGN KEY (article_category_id) REFERENCES public.blog_categories(id);


--
-- TOC entry 3412 (class 2606 OID 1597994)
-- Name: partner_commissions fkmok30i512fjbckd514imoleq8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partner_commissions
    ADD CONSTRAINT fkmok30i512fjbckd514imoleq8 FOREIGN KEY (parent_partner_id) REFERENCES public.partner_commissions(id);


--
-- TOC entry 3433 (class 2606 OID 1598099)
-- Name: solution_contracts fkmwcjednftoq2xkloyvotfotse; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solution_contracts
    ADD CONSTRAINT fkmwcjednftoq2xkloyvotfotse FOREIGN KEY (contract_id) REFERENCES public.contracts(id);


--
-- TOC entry 3392 (class 2606 OID 1597894)
-- Name: orders fkn1d1gkxckw648m2n2d5gx0yx5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fkn1d1gkxckw648m2n2d5gx0yx5 FOREIGN KEY (coupon_id) REFERENCES public.coupons(id);


--
-- TOC entry 3443 (class 2606 OID 1598149)
-- Name: sub_entities_commissions fkn9jxkjkdimthxsnjjeoam1ei6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_commissions
    ADD CONSTRAINT fkn9jxkjkdimthxsnjjeoam1ei6 FOREIGN KEY (sub_entity_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3445 (class 2606 OID 1598159)
-- Name: sub_entities_contacts fkndygmkweojh4275yk0r6q5hi4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_contacts
    ADD CONSTRAINT fkndygmkweojh4275yk0r6q5hi4 FOREIGN KEY (sub_entity_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3424 (class 2606 OID 1598054)
-- Name: prospect_contacts fkoify4ui3pmgum4o1ngpwndb1m; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_contacts
    ADD CONSTRAINT fkoify4ui3pmgum4o1ngpwndb1m FOREIGN KEY (prospect_id) REFERENCES public.prospect(id);


--
-- TOC entry 3391 (class 2606 OID 1597889)
-- Name: orders fkojjigrbyd7qrcwrxvr7e9bdr2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fkojjigrbyd7qrcwrxvr7e9bdr2 FOREIGN KEY (client_id) REFERENCES public.users(id);


--
-- TOC entry 3401 (class 2606 OID 1597939)
-- Name: organization_entities_commissions fkojox9umqv8p6s5ds7si5fae3o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities_commissions
    ADD CONSTRAINT fkojox9umqv8p6s5ds7si5fae3o FOREIGN KEY (commissions_id) REFERENCES public.partner_commissions(id);


--
-- TOC entry 3469 (class 2606 OID 1598279)
-- Name: users_solutions fkosic76vvt4njiaqh5tpd55oys; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_solutions
    ADD CONSTRAINT fkosic76vvt4njiaqh5tpd55oys FOREIGN KEY (solutions_id) REFERENCES public.solutions(id);


--
-- TOC entry 3448 (class 2606 OID 1598174)
-- Name: sub_entities_solutions fkp4kalye2qtorbubk4dh2iyveq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities_solutions
    ADD CONSTRAINT fkp4kalye2qtorbubk4dh2iyveq FOREIGN KEY (solutions_id) REFERENCES public.solutions(id);


--
-- TOC entry 3465 (class 2606 OID 1598259)
-- Name: users fkp56c1712k691lhsyewcssf40f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkp56c1712k691lhsyewcssf40f FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- TOC entry 3437 (class 2606 OID 1598119)
-- Name: sub_entities fkpbr37fckcgcml8833j6obyid2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sub_entities
    ADD CONSTRAINT fkpbr37fckcgcml8833j6obyid2 FOREIGN KEY (address_id) REFERENCES public.address(id);


--
-- TOC entry 3470 (class 2606 OID 1598284)
-- Name: users_solutions fkpbsu60m14akfh4ah0tkhb3tl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_solutions
    ADD CONSTRAINT fkpbsu60m14akfh4ah0tkhb3tl FOREIGN KEY (users_id) REFERENCES public.users(id);


--
-- TOC entry 3429 (class 2606 OID 1598079)
-- Name: prospect_solutions fkpf494qkafvn2duyju57p1gpjs; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_solutions
    ADD CONSTRAINT fkpf494qkafvn2duyju57p1gpjs FOREIGN KEY (solutions_id) REFERENCES public.solutions(id);


--
-- TOC entry 3471 (class 2606 OID 1598289)
-- Name: working_hours fkpy10xmimpg66989h8quu4b5lw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.working_hours
    ADD CONSTRAINT fkpy10xmimpg66989h8quu4b5lw FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3386 (class 2606 OID 1597864)
-- Name: notifications fkrnlsylg455m5km9lo2ox70t98; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT fkrnlsylg455m5km9lo2ox70t98 FOREIGN KEY (sub_entity_id) REFERENCES public.sub_entities(id);


--
-- TOC entry 3428 (class 2606 OID 1598074)
-- Name: prospect_events fkrxuggmq5afhnmgwlyfi82mp73; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect_events
    ADD CONSTRAINT fkrxuggmq5afhnmgwlyfi82mp73 FOREIGN KEY (prospect_id) REFERENCES public.prospect(id);


--
-- TOC entry 3368 (class 2606 OID 1597774)
-- Name: authorities_roles fksfx9h1gswunuhi72vsqtraept; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities_roles
    ADD CONSTRAINT fksfx9h1gswunuhi72vsqtraept FOREIGN KEY (authorities_id) REFERENCES public.authorities(id);


--
-- TOC entry 3436 (class 2606 OID 1598114)
-- Name: states fkskkdphjml9vjlrqn4m5hi251y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.states
    ADD CONSTRAINT fkskkdphjml9vjlrqn4m5hi251y FOREIGN KEY (country_id) REFERENCES public.countries(id);


--
-- TOC entry 3418 (class 2606 OID 1598024)
-- Name: prospect fksnry26w9uqg47lui7cnu0sxfn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT fksnry26w9uqg47lui7cnu0sxfn FOREIGN KEY (address_id) REFERENCES public.address(id);


--
-- TOC entry 3411 (class 2606 OID 1597989)
-- Name: partner_commissions fksqvd2fl98b33wy4cslq07fjnt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partner_commissions
    ADD CONSTRAINT fksqvd2fl98b33wy4cslq07fjnt FOREIGN KEY (emitter_id) REFERENCES public.users(id);


--
-- TOC entry 3396 (class 2606 OID 1597914)
-- Name: organization_entities fkssv6ypontivvvbnsg3aa83ttg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.organization_entities
    ADD CONSTRAINT fkssv6ypontivvvbnsg3aa83ttg FOREIGN KEY (address_id) REFERENCES public.address(id);


--
-- TOC entry 3371 (class 2606 OID 1597789)
-- Name: cities fksu54e1tlhaof4oklvv7uphsli; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT fksu54e1tlhaof4oklvv7uphsli FOREIGN KEY (state_id) REFERENCES public.states(id);


--
-- TOC entry 3378 (class 2606 OID 1597824)
-- Name: deadlines fksxsdvreuyjoh7fx5aveq7am7w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deadlines
    ADD CONSTRAINT fksxsdvreuyjoh7fx5aveq7am7w FOREIGN KEY (payment_method_id) REFERENCES public.payment_method(id);


--
-- TOC entry 3419 (class 2606 OID 1598029)
-- Name: prospect fkt6edwo79nw9abjp2ru19hy3im; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prospect
    ADD CONSTRAINT fkt6edwo79nw9abjp2ru19hy3im FOREIGN KEY (owner_id) REFERENCES public.users(id);


--
-- TOC entry 3462 (class 2606 OID 1598244)
-- Name: users fktfdflb56ryin1sxhgql9dkh2y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fktfdflb56ryin1sxhgql9dkh2y FOREIGN KEY (organization_entity_id) REFERENCES public.organization_entities(id);


-- Completed on 2025-06-03 14:46:58

--
-- PostgreSQL database dump complete
--

update public.users set status='ACTIVE' where status='0';