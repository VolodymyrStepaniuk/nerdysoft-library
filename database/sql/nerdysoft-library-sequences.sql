-- SEQUENCE: public.members_id_seq
DROP SEQUENCE IF EXISTS public.members_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.members_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

-- SEQUENCE: public.books_id_seq
DROP SEQUENCE IF EXISTS public.books_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.books_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

-- SEQUENCE: public.borrowed_books_id_seq
DROP SEQUENCE IF EXISTS public.borrowed_books_id_seq;

CREATE SEQUENCE IF NOT EXISTS public.borrowed_books_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;