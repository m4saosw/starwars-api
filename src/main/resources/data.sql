-- populates database on startup
select sysdate from dual;

-- password userAdmin1 123456
INSERT INTO USER(name, username, password) VALUES('This is the full username 1', 'userAdmin1', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq');

-- password user1 123456
INSERT INTO USER(name, username, password) VALUES('This is the full username 2', 'user1', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq');


INSERT INTO PROFILE(id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO PROFILE(id, name) VALUES (2, 'ROLE_USER');

INSERT INTO USER_PROFILES_MODEL(user_id, profiles_model_id) VALUES (1, 1);
INSERT INTO USER_PROFILES_MODEL(user_id, profiles_model_id) VALUES (2, 2);




