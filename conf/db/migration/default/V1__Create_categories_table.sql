CREATE TABLE categories (
  id        INTEGER PRIMARY KEY NOT NULL,
  name      VARCHAR(50)         NOT NULL,
  parent_id INTEGER,
  CONSTRAINT parent_id_fk FOREIGN KEY (parent_id) REFERENCES categories (id)
);