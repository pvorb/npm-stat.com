CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

CREATE TABLE download_count (
  package_name VARCHAR(255) NOT NULL,
  date         DATE         NOT NULL,
  count        INTEGER      NOT NULL CHECK (count >= 0),
  PRIMARY KEY (package_name, date)
);

SELECT create_hypertable('download_count', 'date');
