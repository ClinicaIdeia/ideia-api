CREATE TABLE IF NOT EXISTS contato (

  codigo        BIGINT PRIMARY KEY  NOT NULL,
  nome          VARCHAR(50)         NOT NULL,
  telefone      VARCHAR(20)         NOT NULL,
  email         VARCHAR(50)

);

CREATE SEQUENCE IF NOT EXISTS contato_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE IF NOT EXISTS empresa (

  codigo        BIGINT PRIMARY KEY  NOT NULL,
  nome          VARCHAR(100)        NOT NULL,
  cnpj          VARCHAR(20) UNIQUE  NOT NULL,
  logradouro    VARCHAR(100),
  numero        VARCHAR(10),
  complemento   VARCHAR(100),
  bairro        VARCHAR(50),
  cep           VARCHAR(20),
  cidade        VARCHAR(50),
  estado        VARCHAR(20),
  ativa         BOOLEAN             NOT NULL DEFAULT FALSE
  );

CREATE SEQUENCE IF NOT EXISTS empresa_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE IF NOT EXISTS  empresa_contato (
	codigo_empresa BIGINT NOT NULL,
	codigo_contato BIGINT NOT NULL,
	PRIMARY KEY (codigo_empresa, codigo_contato),
	FOREIGN KEY (codigo_empresa) REFERENCES empresa(codigo),
	FOREIGN KEY (codigo_contato) REFERENCES contato(codigo)
);

INSERT INTO contato(codigo, nome, telefone, email)
VALUES(1, 'Reinaldo','(34)-7777-7777','reinaldo@ideia.com');

INSERT INTO empresa(codigo, nome, cnpj, logradouro, numero,
complemento, bairro, cep, cidade,  estado, ativa)
 VALUES
  (1, 'IDEIA EXAMES', '23961535000124', 'Av. Floriano Peixoto',
  '3550', '', 'Brasil', '38408177', 'Uberlândia', 'MG', true);

INSERT INTO empresa_contato(codigo_empresa, codigo_contato)
VALUES(1,1);



