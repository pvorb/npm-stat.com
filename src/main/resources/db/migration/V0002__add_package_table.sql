CREATE TABLE package
(
  id           BIGSERIAL PRIMARY KEY,
  package_name VARCHAR(255) UNIQUE NOT NULL,
  imported     BOOLEAN             NOT NULL DEFAULT FALSE
);
