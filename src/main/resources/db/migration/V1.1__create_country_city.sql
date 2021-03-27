CREATE TABLE country (
    country_iso_code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT UK_COUNTRY_NAME UNIQUE (name),
    CONSTRAINT PK_COUNTRY PRIMARY KEY (country_iso_code)
);

CREATE TABLE city (
    country_iso_code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT PK_CITY PRIMARY KEY (country_iso_code, name),
    CONSTRAINT FK_CITY_COUNTRY FOREIGN KEY (country_iso_code) REFERENCES country
);

ALTER TABLE person ADD CONSTRAINT FK_PERSON_CITY FOREIGN KEY (country_iso_code, city_name) REFERENCES city;

INSERT INTO country (COUNTRY_ISO_CODE, NAME) VALUES ( 'AR', 'Argentina' );
INSERT INTO city (country_iso_code, name) VALUES
                                                 ( 'AR', 'Capital Federal' ),
                                                 ( 'AR', 'Buenos Aires' ),
                                                 ( 'AR', 'Catamarca' ),
                                                 ( 'AR', 'Córdoba' ),
                                                 ( 'AR', 'Corrientes' ),
                                                 ( 'AR', 'Chaco' ),
                                                 ( 'AR', 'Chubut' ),
                                                 ( 'AR', 'Entre Ríos' ),
                                                 ( 'AR', 'Formosa' ),
                                                 ( 'AR', 'Jujuy' ),
                                                 ( 'AR', 'La Pampa' ),
                                                 ( 'AR', 'La Rioja' ),
                                                 ( 'AR', 'Mendoza' ),
                                                 ( 'AR', 'Misiones' ),
                                                 ( 'AR', 'Neuquén' ),
                                                 ( 'AR', 'Río Negro' ),
                                                 ( 'AR', 'Salta' ),
                                                 ( 'AR', 'San Juan' ),
                                                 ( 'AR', 'San Luis' ),
                                                 ( 'AR', 'Santa Cruz' ),
                                                 ( 'AR', 'Santa Fe' ),
                                                 ( 'AR', 'Santiago del Estero' ),
                                                 ( 'AR', 'Tucumán' ),
                                                 ( 'AR', 'Tierra del Fuego' );