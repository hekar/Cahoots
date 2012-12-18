-- --------------------------------
-- Create the test administrators
-- --------------------------------
INSERT INTO users (username, password, name, role) 
VALUES ('test_1', md5('test'), 'Test Admin 1', (SELECT id FROM roles WHERE name='admin'));

INSERT INTO users (username, password, name, role) 
VALUES ('test_2', md5('test'), 'Test Admin 2', (SELECT id FROM roles WHERE name='admin'));

INSERT INTO users (username, password, name, role) 
VALUES ('test_3', md5('test'), 'Test Admin 3', (SELECT id FROM roles WHERE name='admin'));

INSERT INTO users (username, password, name, role) 
VALUES ('test_4', md5('test'), 'Test Admin 4', (SELECT id FROM roles WHERE name='admin'));

INSERT INTO users (username, password, name, role) 
VALUES ('test_5', md5('test'), 'Test Admin 5', (SELECT id FROM roles WHERE name='admin'));

-- --------------------------------
-- Create the test users
-- --------------------------------
INSERT INTO users (username, password, name, role) 
VALUES ('test_user1', md5('test'), 'Test User 1', (SELECT id FROM roles WHERE name='user'));

INSERT INTO users (username, password, name, role) 
VALUES ('test_user2', md5('test'), 'Test User 2', (SELECT id FROM roles WHERE name='user'));

INSERT INTO users (username, password, name, role) 
VALUES ('test_user3', md5('test'), 'Test User 3', (SELECT id FROM roles WHERE name='user'));

INSERT INTO users (username, password, name, role) 
VALUES ('test_user4', md5('test'), 'Test User 4', (SELECT id FROM roles WHERE name='user'));

INSERT INTO users (username, password, name, role) 
VALUES ('test_user5', md5('test'), 'Test User 5', (SELECT id FROM roles WHERE name='user'));

