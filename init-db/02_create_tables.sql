\connect vaselek

CREATE TABLE IF NOT EXISTS users (
    id                 BIGSERIAL PRIMARY KEY,
    full_name          VARCHAR(255) NOT NULL,
    login              VARCHAR(100) UNIQUE NOT NULL,
    password           VARCHAR(255) NOT NULL,
    role               VARCHAR(20)  NOT NULL DEFAULT 'SELLER',
    status             VARCHAR(20)  NOT NULL DEFAULT 'WORKING',
    registration_date  DATE         NOT NULL DEFAULT CURRENT_DATE,
    dismissal_date     DATE,
    block_date         DATE
);

CREATE TABLE IF NOT EXISTS flowers (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    quantity      INTEGER      NOT NULL,
    arrival_date  DATE         NOT NULL,
    sale_date      DATE,
    seller_id      BIGINT REFERENCES users(id)
);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO vaselek_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO vaselek_user;
