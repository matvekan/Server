BEGIN;

INSERT INTO roles (name) VALUES
                             ('ADMIN'),
                             ('MASTER'),
                             ('CLIENT');

INSERT INTO users (login, password_hash, role_id, email) VALUES
                                                             ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYV7lXjL9RgLrQfL5PvRyU3Xv6H2K6', 1, 'admin@tattoo.com'),
                                                             ('master1', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYV7lXjL9RgLrQfL5PvRyU3Xv6H2K6', 2, 'master@tattoo.com'),
                                                             ('client1', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrYV7lXjL9RgLrQfL5PvRyU3Xv6H2K6', 3, 'client@tattoo.com');

COMMIT;