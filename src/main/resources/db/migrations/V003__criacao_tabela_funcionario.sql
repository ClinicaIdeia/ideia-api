CREATE TABLE IF NOT EXISTS funcionario (
  codigo            BIGINT PRIMARY KEY  NOT NULL,
  nome              VARCHAR(50)         NOT NULL,
  rg                VARCHAR(50),
  cpf               VARCHAR(20)         UNIQUE,
  sexo              VARCHAR(20),
  estado_civil      VARCHAR(50),
  escolaridade      VARCHAR(100),
  naturalidade      VARCHAR(100),
  email             VARCHAR(50),
  matricula         VARCHAR(20),
  cargo             VARCHAR(50),
  data_nascimento   DATE                NOT NULL,
  telefone          VARCHAR(20),
  logradouro        VARCHAR(100),
  numero            VARCHAR(10),
  complemento       VARCHAR(100),
  bairro            VARCHAR(50),
  cep               VARCHAR(20),
  cidade            VARCHAR(50),
  estado            VARCHAR(20)
  );

CREATE SEQUENCE IF NOT EXISTS funcionario_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS  funcionario_empresa (
	codigo_funcionario  BIGINT NOT NULL,
	codigo_empresa      BIGINT NOT NULL,
	PRIMARY KEY (codigo_funcionario, codigo_empresa),
	FOREIGN KEY (codigo_empresa) REFERENCES empresa(codigo),
	FOREIGN KEY (codigo_funcionario) REFERENCES funcionario(codigo)
);

