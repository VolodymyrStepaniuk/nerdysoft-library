-- Database: solutions

DROP DATABASE IF EXISTS "nerdysoft-library";

CREATE DATABASE "nerdysoft-library"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;