CREATE TABLE public.role
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT role_pkey PRIMARY KEY (id)
);
INSERT INTO public.occupation(name)
VALUES ('Chefe'),
('Desenvolverdor Senior'),
('Desenvolvedor Pleno'),
('Desenvolvedor Jr'),
('Estágio');

CREATE TABLE public.owner
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    email character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    phone character varying(255) COLLATE pg_catalog."default",
    role bigint,
    CONSTRAINT owner_pkey PRIMARY KEY (id),
    CONSTRAINT fkbbh48alqwnhysx9thysmxqukq FOREIGN KEY (role)
        REFERENCES public.role (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

INSERT INTO public.owner(email, name, phone, occupation)
VALUES('teste1@email.com', 'André', '111111111', 1),
('teste2@email.com', 'Thiago', '2222222', 2),
('teste3@email.com', 'Michel', '3333333', 4),
('teste4@email.com', 'Gustavo', '444444', 5),
('teste5@email.com', 'Carlos', '555555', 4),
('teste6@email.com', 'Guga', '6666666', 5);

CREATE TABLE public.task
(
    id bigint NOT NULL DEFAULT nextval('task_id_seq'::regclass),
    deadline date,
    description character varying(255) COLLATE pg_catalog."default",
    priority integer,
    status integer,
    title character varying(255) COLLATE pg_catalog."default",
    owner bigint,
    CONSTRAINT task_pkey PRIMARY KEY (id),
    CONSTRAINT fk1095tr7l6fdv7kw6vvqwyt8ja FOREIGN KEY (owner)
        REFERENCES public.owner (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
INSERT INTO public.task(deadline, description, priority, status, title, owner)
VALUES('2021-04-28', 'Teste de descrição.', 0, 1, 'Teste', 1),
('2021-04-29', 'Teste de descrição.', 2, 1, 'Teste', 2),
('2021-04-30', 'Teste de descrição 2.', 1, 0, 'Teste', 3),
('2021-05-01', 'Teste de descrição3.', 1, 0, 'Teste', 4),
('2021-05-06', 'Teste de descrição4.', 1, 1, 'Teste', 2),
('2021-05-07', 'Teste de descrição5.', 0, 0, 'Teste', 1),
('2021-05-20', 'Teste de descrição6.', 0, 0, 'Teste', 3),
('2021-05-25', 'Teste de descrição7.', 0, 1, 'Teste', 5),
('2021-05-28', 'Teste de descrição8.', 0, 1, 'Teste', 6),
('2021-05-30', 'Teste de descrição9.', 2, 1, 'Teste', 6);
