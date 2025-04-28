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

CREATE INDEX idx_program_start_date ON program (program_start_date);

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
    id                BIGINT                                       NOT NULL AUTO_INCREMENT,
    email             VARCHAR(128)                                 NOT NULL,
    name              VARCHAR(12)                                  NOT NULL,
    contact           VARCHAR(12),
    status            ENUM ('PENDING', 'ACTIVE') DEFAULT 'PENDING' NOT NULL,
    push_token        VARCHAR(1024),
    push_notification BOOLEAN                    DEFAULT FALSE     NOT NULL,
    sms_notification  BOOLEAN                    DEFAULT FALSE     NOT NULL,
    created_at        DATETIME(3)                DEFAULT NOW(3),
    updated_at        DATETIME(3)                DEFAULT NOW(3),
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

CREATE TABLE health_model
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    model_name VARCHAR(128) NOT NULL,
    user_id    BIGINT       NOT NULL,
    created_at DATETIME(3) DEFAULT NOW(3),
    updated_at DATETIME(3) DEFAULT NOW(3),
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uidx_user_id ON health_model (user_id);

CREATE TABLE activity
(
    id            BIGINT  NOT NULL AUTO_INCREMENT,
    steps_count   INT     NOT NULL,
    activity_date DATE    NOT NULL,
    holiday       BOOLEAN NOT NULL,
    day_of_week   INT     NOT NULL,
    user_id       BIGINT  NOT NULL,
    created_at    DATETIME(3) DEFAULT NOW(3),
    updated_at    DATETIME(3) DEFAULT NOW(3),
    PRIMARY KEY (id)
);

CREATE INDEX idx_user_id ON activity (user_id);

CREATE INDEX idx_activity_date ON activity (activity_date);
