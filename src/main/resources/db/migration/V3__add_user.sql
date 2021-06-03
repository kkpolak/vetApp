CREATE TABLE IF NOT EXISTS USERS
(
    ID           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    USERNAME         varchar(256),
    PASSWORD         varchar(256),
    ROLE varchar(256)
    );

-- INSERT INTO users (username, password, role)
-- VALUES ('user', '$2y$10$e.6JlXXjaBlURtvgRfa8g.zx1vkxmEsTfA0Qz46Oi0kciOYSOTkqW', 'ROLE_USER'),
--        ('admin', '$2y$10$sdNg8H1KMPIjNpsgvqiby.XK3ApPXSf/MH6XZiqbEQHcY5qWPRYJe', 'ROLE_ADMIN'),
--        ('vet', '$2y$10$KFiObu.iJoQ5.v0Ut2zZyOfg.kvWVY.yzwpjVCnGiWM08.l7wuO1q', 'ROLE_VET');