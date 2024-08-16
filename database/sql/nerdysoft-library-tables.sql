-- TABLE: public.members
DROP TABLE IF EXISTS public.members;

CREATE TABLE IF NOT EXISTS public.members
(
    id               bigint                                              NOT NULL DEFAULT nextval('members_id_seq'::regclass),
    name             character varying(255) COLLATE pg_catalog."default" NOT NULL,
    surname          character varying(255) COLLATE pg_catalog."default" NOT NULL,
    membership_date  timestamp(6) with time zone                         NOT NULL DEFAULT NOW(),
    CONSTRAINT members_pkey PRIMARY KEY (id)
    )
    TABLESPACE pg_default;

ALTER SEQUENCE IF EXISTS public.members_id_seq
    OWNED by public.members.id;

-- TABLE: public.books
DROP TABLE IF EXISTS public.books;

CREATE TABLE IF NOT EXISTS public.books
(
    id               bigint                                              NOT NULL DEFAULT nextval('members_id_seq'::regclass),
    title            character varying(255) COLLATE pg_catalog."default" NOT NULL,
    author           character varying(255) COLLATE pg_catalog."default" NOT NULL,
    amount           integer                                             NOT NULL,
    CONSTRAINT books_pkey PRIMARY KEY (id)
    )
    TABLESPACE pg_default;

ALTER SEQUENCE IF EXISTS public.books_id_seq
    OWNED by public.books.id;

-- TABLE: public.borrowed_books
DROP TABLE IF EXISTS public.borrowed_books;

CREATE TABLE IF NOT EXISTS public.borrowed_books
(
    id               bigint                                              NOT NULL DEFAULT nextval('borrowed_books_id_seq'::regclass),
    book_id          bigint                                              NOT NULL,
    member_id        bigint                                              NOT NULL,
    borrowed_date    timestamp(6) with time zone                         NOT NULL DEFAULT NOW(),
    returned_date    timestamp(6) with time zone                         NOT NULL DEFAULT NOW(),
    CONSTRAINT borrowed_books_pkey PRIMARY KEY (id)
    )
    TABLESPACE pg_default;

ALTER SEQUENCE IF EXISTS public.borrowed_books_id_seq
    OWNED by public.borrowed_books.id;