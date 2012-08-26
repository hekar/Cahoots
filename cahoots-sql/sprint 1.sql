-- --
--  Sept. 4 -> Sept. 11
-- --

-- --
-- A user in the application
-- --
create table users
(
  id  serial constraint firstkey primary key
);

comment on table users is 'A user in the application';


-- --
-- Table the contains information to map users to an
-- organization's LDAP infrastructure
-- --
create table user_ldap
(
  id serial primary key,
  users  int references users (id),
  dc varchar(255),
  cn varchar(255)
);

comment on table user_ldap is 'Table the contains information to map users to an organizations LDAP infrastructure';
comment on column user_ldap.dc is 'LDAP directory';
comment on column user_ldap.cn is 'LDAP common name';


-- --
-- A user's profile in the application
-- --
create table user_profile
(
  id serial primary key,
  users  int references users (id),
  nick varchar(255) not null
);

comment on table user_profile is 'A users profile in the application';
comment on column user_profile.nick is 'A users nickname';


-- --
-- An organization
-- --
create table organization
(
  id  serial primary key,
  name varchar(364) not null,
  description text not null
);


comment on table organization is 'An organization';

-- --
-- A group of users under an organization
-- Mapping of user to organization
-- --
create table user_organization
(
  id  serial primary key,
  users int references users (id),
  org int references organization (id)
);

comment on table user_organization is 'A group of users under an organization and mapping of user to organization';

