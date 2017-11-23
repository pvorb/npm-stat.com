CREATE TABLE package (
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE author (
  id            BIGSERIAL PRIMARY KEY,
  name          VARCHAR(255) UNIQUE NOT NULL,
  last_modified TIMESTAMP           NOT NULL
);

CREATE TABLE package_author (
  package_id BIGINT NOT NULL REFERENCES package (id),
  author_id  BIGINT NOT NULL REFERENCES author (id),
  PRIMARY KEY (package_id, author_id)
);

CREATE TABLE download_count (
  package_id BIGINT  NOT NULL REFERENCES package (id),
  date       DATE    NOT NULL,
  count      INTEGER NOT NULL CHECK (count >= 0),
  PRIMARY KEY (package_id, date)
);
