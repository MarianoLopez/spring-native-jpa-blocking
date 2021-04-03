INSERT INTO PERSON (ID, CREATED_DATE, LAST_MODIFIED_DATE, ENABLED, FIRST_NAME, LAST_NAME, COUNTRY_ISO_CODE, CITY_NAME)
VALUES (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Corrientes' ),
       (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Capital Federal' ),
       (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Buenos Aires' ),
       (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Catamarca' ),
       (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Córdoba' ),
       (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Chaco' ),
       (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Chubut' ),
       (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Entre Ríos' ),
       (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Formosa' ),
       (PERSON_SEQUENCE.nextval, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, CONCAT('test', cast(PERSON_SEQUENCE.currval as VARCHAR(5))), 'test', 'AR', 'Jujuy' );