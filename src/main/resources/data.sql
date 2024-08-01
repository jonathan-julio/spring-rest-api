-- Estrutura para tabela `usuario`
CREATE TABLE IF NOT EXISTS  usuario (
  acesso TINYINT,
  role TINYINT,
  id INT PRIMARY KEY AUTO_INCREMENT,
  login VARCHAR(255) UNIQUE,
  senha VARCHAR(255)
);

-- Estrutura para tabela `person`
CREATE TABLE IF NOT EXISTS  person (
  id INT PRIMARY KEY AUTO_INCREMENT,
  usuario_id INT UNIQUE,
  data VARCHAR(255),
  nome VARCHAR(255),
  sexo VARCHAR(255)
);

-- Estrutura para tabela `profile`
CREATE TABLE IF NOT EXISTS  profile (
  id INT PRIMARY KEY AUTO_INCREMENT,
  person_id INT UNIQUE,
  about VARCHAR(4000),
  background VARCHAR(255),
  color VARCHAR(255),
  texto VARCHAR(255),
  texto_secundario VARCHAR(255)
);

-- Estrutura para tabela `log`
/* CREATE TABLE IF NOT EXISTS  image (
  id INT PRIMARY KEY AUTO_INCREMENT,
  image tinyblob DEFAULT NULL
); */

CREATE TABLE IF NOT EXISTS image (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image LONGBLOB
);

-- Estrutura para tabela `post`
CREATE TABLE IF NOT EXISTS  post (
  admin_id INT,
  id INT PRIMARY KEY AUTO_INCREMENT,
  status TINYINT,
  descricao VARCHAR(255),
  github VARCHAR(255),
  img VARCHAR(255),
  titulo VARCHAR(255)
);


-- Estrutura para tabela `log`
CREATE TABLE IF NOT EXISTS  log (
  id INT PRIMARY KEY AUTO_INCREMENT,
  usuario_id INT,
  data TIMESTAMP,
  changer VARCHAR(255),
  function VARCHAR(255)
);


-- Despejando dados para a tabela `usuario`
INSERT INTO usuario (acesso, role, login, senha ,email ) VALUES
( 0, 0, 'johndoe', '$2a$10$3ijxAnWdsvBZRG54.jP/2uKL2YNwRE6hVkhKb/gNcYp/OMQiyeE7u', 'okaynatan@gmail.com');
INSERT INTO usuario (acesso, role, login, senha ,email ) VALUES
( 1, 1, 'janedoe', '$2a$10$Eh/oBP6O85lQq8vPQfaoJeL4.nl2d/qp/FIYAMU61gExeFxdsZdeC', 'email@mail.com');
INSERT INTO usuario (acesso, role, login, senha ,email ) VALUES
( 0, 2, 'samsmith', '$2a$10$3ijxAnWdsvBZRG54.jP/2uKL2YNwRE6hVkhKb/gNcYp/OMQiyeE7u', 'email@mail.com');
INSERT INTO usuario (acesso, role, login, senha ,email ) VALUES
( 1, 3, 'emilyjones', '$2a$10$3ijxAnWdsvBZRG54.jP/2uKL2YNwRE6hVkhKb/gNcYp/OMQiyeE7u', 'email@mail.com');




-- Despejando dados para a tabela `person`
INSERT INTO person (usuario_id, data, nome, sexo) VALUES
( 1, '1985-07-23', 'John Doe', 'M');
INSERT INTO person (usuario_id, data, nome, sexo) VALUES
( 2, '1990-03-15', 'Jane Doe', 'F');
INSERT INTO person (usuario_id, data, nome, sexo) VALUES
( 3, '1988-11-30', 'Sam Smith', 'M');
INSERT INTO person (usuario_id, data, nome, sexo) VALUES
( 4, '1992-06-10', 'Emily Jones', 'F');


-- Despejando dados para a tabela `profile`
INSERT INTO profile (id, person_id, about, background, color, texto, texto_secundario) 
VALUES (1, 1, 'Passionate about coding and technology.', '#FFFFFF', '#0000FF', 'Hello, I m John!', 'Software Developer at XYZ Corp.');

INSERT INTO profile (id, person_id, about, background, color, texto, texto_secundario) 
VALUES (2, 2, 'Lover of art, design, and innovation.', '#D3D3D3', '#800080', 'Hi, I m Jane!', 'Graphic Designer at Creative Studio.');

INSERT INTO profile (id, person_id, about, background, color, texto, texto_secundario) 
VALUES (3, 3, 'Enthusiastic about data science and AI.', '#000000', '#008000', 'Hey, I m Sam!', 'Data Scientist at Tech Analytics.');

INSERT INTO profile (id, person_id, about, background, color, texto, texto_secundario) 
VALUES (4, 4, 'Passionate about marketing and brand strategy.', '#F5F5DC', '#FF0000', 'Hello, I m Emily!', 'Marketing Specialist at BrandCorp.');





-- Despejando dados para a tabela `post`
INSERT INTO post (admin_id, status, descricao, github, img, titulo) VALUES
( 1, 0, 'Este eh um projeto exemplo.', 'www.github.com', ' https://80bc-177-89-225-229.ngrok-free.app/image/monochrome-image-8598798_640.jpg', 'Post 01');
INSERT INTO post (admin_id, status, descricao, github, img, titulo) VALUES
( 1, 0, 'Este eh outro projeto exemplo.', 'www.github.com', ' https://80bc-177-89-225-229.ngrok-free.app/image/monochrome-image-8598798_640.jpg', 'Post 02');
INSERT INTO post (admin_id, status, descricao, github, img, titulo) VALUES
( 1, 0, 'Este eh outro projeto exemplo.', 'www.github.com', ' https://80bc-177-89-225-229.ngrok-free.app/image/monochrome-image-8598798_640.jpg', 'Post 03');



-- Despejando dados para a tabela `post`
INSERT INTO PROFILE_POST  (POST_ID, PROFILE_ID) VALUES
( 1, 1);
INSERT INTO PROFILE_POST  (POST_ID, PROFILE_ID) VALUES
( 2, 1);
INSERT INTO PROFILE_POST  (POST_ID, PROFILE_ID) VALUES
( 2, 2);
INSERT INTO PROFILE_POST  (POST_ID, PROFILE_ID) VALUES
( 3, 1);

-- Despejando dados para a tabela `log`
INSERT INTO log (usuario_id, data, changer, function) VALUES
(1, '2024-05-24 22:18:53.000000', 'Usuario salvo no BD', 'UsuarioServiceImpl.salvarUsuario');
INSERT INTO log (usuario_id, data, changer, function) VALUES
(2, '2024-05-24 22:19:54.000000', 'Usuario salvo no BD', 'UsuarioServiceImpl.salvarUsuario');
INSERT INTO log (usuario_id, data, changer, function) VALUES
(3, '2024-05-24 22:20:02.000000', 'Usuario salvo no BD', 'UsuarioServiceImpl.salvarUsuario');
INSERT INTO log (usuario_id, data, changer, function) VALUES
(4, '2024-05-24 22:20:10.000000', 'Usuario salvo no BD', 'UsuarioServiceImpl.salvarUsuario');
INSERT INTO log (usuario_id, data, changer, function) VALUES
( 3, '2024-05-26 18:57:20.000000', 'Usuario autenticado', 'UsuarioServiceImpl.autenticar');


