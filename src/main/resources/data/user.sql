CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255),
                       phone VARCHAR(255),
                       email_enabled BOOLEAN,
                       sms_enabled BOOLEAN
);

INSERT INTO users(email, phone, email_enabled, sms_enabled)
VALUES ('user1@example.com', '+123456789', true, false),
       ('user2@example.com', '+987654321', false, true);