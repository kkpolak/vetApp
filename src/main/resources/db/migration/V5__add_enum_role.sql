ALTER TABLE users
DROP COLUMN role;

CREATE TYPE role_type AS ENUM ('ROLE_ADMIN', 'ROLE_USER', 'ROLE_VET');

ALTER TABLE users
    ADD COLUMN role role_type;
