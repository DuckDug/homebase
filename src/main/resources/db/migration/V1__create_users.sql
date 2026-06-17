CREATE TABLE users (
   id          BIGSERIAL PRIMARY KEY,
   username    VARCHAR(50)  NOT NULL UNIQUE,
   email       VARCHAR(255) NOT NULL UNIQUE,
   password    VARCHAR(255) NOT NULL,
   created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
   updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE users IS 'Stores registered user accounts for HomeBase';
COMMENT ON COLUMN users.id IS 'Auto-incrementing primary key';
COMMENT ON COLUMN users.username IS 'Unique login handle, max 50 chars';
COMMENT ON COLUMN users.email IS 'Unique email used for login and notifications';
COMMENT ON COLUMN users.password IS 'Bcrypt hashed password, never stored plain text';
COMMENT ON COLUMN users.created_at IS 'Timestamp when the record was created';
COMMENT ON COLUMN users.updated_at IS 'Timestamp when the record was last updated';