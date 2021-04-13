CREATE TABLE office (
    id integer NOT NULL,
    name character varying(255),
    CONSTRAINT office_pkey PRIMARY KEY (id)
);

ALTER TABLE visit
    ADD COLUMN office_id integer;

ALTER TABLE visit
    ADD CONSTRAINT fk_office_place_of_visit
        FOREIGN KEY (office_id)
            REFERENCES office(id);