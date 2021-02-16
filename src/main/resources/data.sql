-- populates database on startup
INSERT INTO USER(name, username, password) VALUES('name of the user1', 'aluno@email.com', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq');
--INSERT INTO USER(name, username, password) VALUES('name of the user2', 'aluno@email.com', '$2a$10$sFKmbxbG4ryhwPNx/l3pgOJSt.fW1z6YcUnuE2X8APA/Z3NI/oSpq');


INSERT INTO PROFILE(id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO PROFILE(id, name) VALUES (2, 'ROLE_USER');

INSERT INTO USER_PROFILES_MODEL(user_id, profiles_model_id) VALUES (1, 1);
--INSERT INTO USER_PROFILES_MODEL(user_id, profiles_model_id) VALUES (2, 2);




