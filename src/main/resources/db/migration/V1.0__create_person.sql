CREATE SEQUENCE person_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE person (
    id bigint NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    enabled boolean NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    country_iso_code VARCHAR(255),
    city_name VARCHAR(255),
    CONSTRAINT PK_PERSON PRIMARY KEY (id)
);