DROP TABLE users CASCADE;
DROP TABLE user_profile CASCADE;
DROP TABLE user_organization CASCADE;
DROP TABLE user_ldap CASCADE;
DROP TABLE organization CASCADE;

CREATE TABLE roles
(
  id serial NOT NULL,
  name character varying(255) NOT NULL,
  CONSTRAINT roles_pkey PRIMARY KEY (id),
  CONSTRAINT roles_id_name_key UNIQUE (id, name)
)
WITH (
  OIDS=FALSE
);

INSERT INTO roles (name) VALUES ('admin');
INSERT INTO roles (name) VALUES ('user');

CREATE TABLE users
(
  id serial NOT NULL,
  username character varying(255) NOT NULL,
  password character varying(255) NOT NULL,
  name character varying(255) NOT NULL,
  role integer NOT NULL,
  CONSTRAINT new_user_pkey PRIMARY KEY (id),
  CONSTRAINT users_role_fkey FOREIGN KEY (role)
      REFERENCES roles (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT new_user_id_username_key UNIQUE (id, username)
)
WITH (
  OIDS=FALSE
);

INSERT INTO users (username, password, name, role) VALUES ('admin', md5('admin'), 'Admin McAdmin', (SELECT id FROM roles WHERE name='admin'));

