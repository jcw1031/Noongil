CREATE TABLE program
(
    id                   BIGINT                NOT NULL AUTO_INCREMENT,
    name                 VARCHAR(1024)         NOT NULL,
    reception_start_date DATE,
    reception_end_date   DATE,
    program_start_date   DATE,
    program_end_date     DATE,
    address              VARCHAR(1024)         NOT NULL,
    borough              VARCHAR(8)            NOT NULL,
    location             POINT SRID 4326       NOT NULL,
    institution          VARCHAR(1024)         NOT NULL,
    contact              VARCHAR(128),
    age_range            VARCHAR(32),
    gender               VARCHAR(6)            NOT NULL,
    fee_type             ENUM ('FREE', 'PAID') NOT NULL,
    fee_amount           VARCHAR(1024),
    reception_method     VARCHAR(1024),
    reception_url        VARCHAR(1024),
    unique_id            CHAR(32)              NOT NULL,
    created_at           DATETIME(3) DEFAULT NOW(3),
    updated_at           DATETIME(3) DEFAULT NOW(3),
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uidx_unique_id ON program (unique_id);

CREATE INDEX idx_borough ON program (borough);

CREATE SPATIAL INDEX sidx_location ON program (location);

CREATE TABLE emergency_contact
(
    id              BIGINT                                         NOT NULL AUTO_INCREMENT,
    name            VARCHAR(12)                                    NOT NULL,
    contact         VARCHAR(12)                                    NOT NULL,
    notification    BOOLEAN                      DEFAULT FALSE     NOT NULL,
    status          ENUM ('PENDING', 'ACCEPTED') DEFAULT 'PENDING' NOT NULL,
    user_id         BIGINT                                         NOT NULL,
    contact_user_id BIGINT,
    created_at      DATETIME(3)                  DEFAULT NOW(3),
    updated_at      DATETIME(3)                  DEFAULT NOW(3),
    PRIMARY KEY (id)
);

CREATE INDEX idx_user_id ON emergency_contact (user_id);

CREATE INDEX idx_contact_user_id ON emergency_contact (contact_user_id);

CREATE INDEX idx_contact ON emergency_contact (contact);

CREATE TABLE user
(
    id         BIGINT                                       NOT NULL AUTO_INCREMENT,
    email      VARCHAR(128)                                 NOT NULL,
    name       VARCHAR(12)                                  NOT NULL,
    contact    VARCHAR(12)                                  NOT NULL,
    status     ENUM ('PENDING', 'ACTIVE') DEFAULT 'PENDING' NOT NULL,
    created_at DATETIME(3)                DEFAULT NOW(3),
    updated_at DATETIME(3)                DEFAULT NOW(3),
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uidx_email ON user (email);

CREATE UNIQUE INDEX uidx_contact ON user (contact);

CREATE TABLE emergency_notification
(
    id          BIGINT                                   NOT NULL AUTO_INCREMENT,
    status      ENUM ('CREATED', 'WAITING', 'COMPLETED') NOT NULL,
    sender_id   BIGINT                                   NOT NULL,
    receiver_id BIGINT                                   NOT NULL,
    created_at  DATETIME(3) DEFAULT NOW(3),
    updated_at  DATETIME(3) DEFAULT NOW(3),
    PRIMARY KEY (id)
);

CREATE INDEX idx_updated_at ON emergency_notification (updated_at);
