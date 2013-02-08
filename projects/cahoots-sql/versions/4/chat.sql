-- Table: chat

-- Constraint: username

-- ALTER TABLE users DROP CONSTRAINT username;

ALTER TABLE users
  ADD CONSTRAINT username UNIQUE(username);


-- DROP TABLE chat;

CREATE TABLE chat
(
  "from" character varying(255) NOT NULL,
  "to" character varying(255) NOT NULL,
  message text NOT NULL,
  date time with time zone,
  id serial NOT NULL,
  CONSTRAINT chat_pkey PRIMARY KEY (id),
  CONSTRAINT from_fk FOREIGN KEY ("from")
      REFERENCES users (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT to_fk FOREIGN KEY ("to")
      REFERENCES users (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT u_id UNIQUE (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE chat
  OWNER TO postgres;

-- Index: fki_from_fk

-- DROP INDEX fki_from_fk;

CREATE INDEX fki_from_fk
  ON chat
  USING btree
  ("from" COLLATE pg_catalog."default");

-- Index: fki_to_fk

-- DROP INDEX fki_to_fk;

CREATE INDEX fki_to_fk
  ON chat
  USING btree
  ("to" COLLATE pg_catalog."default");

