DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS сomment;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;


CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    description VARCHAR(550) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_user FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_name VARCHAR(50) NOT NULL,
    description VARCHAR(250) NOT NULL,
    available BOOLEAN NOT NULL,
    request_id BIGINT,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT fk_item_user FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE,
    CONSTRAINT fk_item_requests FOREIGN KEY (request_id) REFERENCES requests ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_dt TIMESTAMP WITHOUT TIME ZONE,
    end_dt TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT NOT NULL,
    booker BIGINT NOT NULL,
    status SMALLINT,
    CONSTRAINT pk_booking PRIMARY KEY (id),
    CONSTRAINT fk_item_bookings FOREIGN KEY (item_id) REFERENCES items ON DELETE CASCADE,
    CONSTRAINT fk_user_bookings FOREIGN KEY (booker) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS сomment (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(550) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_сomment PRIMARY KEY (id),
    CONSTRAINT fk_item_сomment FOREIGN KEY (item_id) REFERENCES items ON DELETE CASCADE,
    CONSTRAINT fk_user_сomment FOREIGN KEY (author_id) REFERENCES users ON DELETE CASCADE
);


