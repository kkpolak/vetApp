CREATE TABLE if not exists public.client
(
    id integer NOT NULL,
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT client_pkey PRIMARY KEY (id)
)
CREATE TABLE if not exists public.animal
(
    id integer NOT NULL,
    date_of_birth timestamp without time zone,
    name character varying(255) COLLATE pg_catalog."default",
    type integer,
    owner_id integer,
    CONSTRAINT animal_pkey PRIMARY KEY (id),
    CONSTRAINT fkitahe3ljymr5ulhf8oy8sk6p9 FOREIGN KEY (owner_id)
        REFERENCES public.client (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE public.vet
(
    id integer NOT NULL,
    admission_end timestamp without time zone,
    admission_start timestamp without time zone,
    duration interval,
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    photo character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT vet_pkey PRIMARY KEY (id)
)

CREATE TABLE public.visit
(
    id integer NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    duration interval,
    price numeric(19,2),
    start_time timestamp without time zone,
    status integer,
    animal_id integer,
    client_id integer,
    vet_id integer,
    CONSTRAINT visit_pkey PRIMARY KEY (id),
    CONSTRAINT fk83qviporb5ov1ph00ynyn2bxh FOREIGN KEY (client_id)
        REFERENCES public.client (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk8wn2rh59ouslkysfmd741rxii FOREIGN KEY (vet_id)
        REFERENCES public.vet (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fktnvt1w24k8xb5bs3acqcwlwmr FOREIGN KEY (animal_id)
        REFERENCES public.animal (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

INSERT INTO client(id, first_name, last_name)
VALUES (1, 'Adam', 'Hgw');