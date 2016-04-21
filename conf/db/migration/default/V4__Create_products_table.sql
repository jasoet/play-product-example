CREATE TABLE products (
  code        VARCHAR(150) PRIMARY KEY   NOT NULL,
  name        VARCHAR(150) UNIQUE        NOT NULL,
  description VARCHAR(500)               NOT NULL,
  color       VARCHAR(150)               NOT NULL,
  size        VARCHAR(5)                 NOT NULL,
  picture_url VARCHAR(150)               NOT NULL,
  price       INTEGER                    NOT NULL,
  category_id INTEGER                    NOT NULL,
  CONSTRAINT category_id_fk FOREIGN KEY (category_id) REFERENCES categories (id)
);