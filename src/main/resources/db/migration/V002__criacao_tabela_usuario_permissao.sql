CREATE TABLE IF NOT EXISTS usuario (

	codigo BIGINT   PRIMARY KEY   NOT NULL,
	nome            VARCHAR(50)   NOT NULL,
	email           VARCHAR(50)   UNIQUE NOT NULL,
	senha           VARCHAR(150)  NOT NULL,
	codigo_empresa  BIGINT        NOT NULL,
  FOREIGN KEY (codigo_empresa)  REFERENCES empresa(codigo)

);

CREATE SEQUENCE IF NOT EXISTS usuario_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE IF NOT EXISTS permissao (
	codigo BIGINT PRIMARY KEY NOT NULL,
	descricao VARCHAR(50) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS permissao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE IF NOT EXISTS  usuario_permissao (
	codigo_usuario BIGINT NOT NULL,
	codigo_permissao BIGINT NOT NULL,
	PRIMARY KEY (codigo_usuario, codigo_permissao),
	FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
	FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
);

INSERT INTO usuario (codigo, nome, email, senha, codigo_empresa) values (nextval('usuario_seq'), 'Administrador',
'admin@ideia.com', '$2a$10$0SRMSVY60PiIfnDfnN6uGOFSbxPirSmkAwhKVlMEzXf4qCppA/cwi', 1);


INSERT INTO permissao (codigo, descricao) values (1, 'ROLE_PESQUISAR_AGENDAMENTO');
INSERT INTO permissao (codigo, descricao) values (2, 'ROLE_CADASTRAR_AGENDAMENTO');
INSERT INTO permissao (codigo, descricao) values (3, 'ROLE_REMOVER_AGENDAMENTO');

INSERT INTO permissao (codigo, descricao) values (4, 'ROLE_PESQUISAR_EMPRESA');
INSERT INTO permissao (codigo, descricao) values (5, 'ROLE_CADASTRAR_EMPRESA');
INSERT INTO permissao (codigo, descricao) values (6, 'ROLE_REMOVER_EMPRESA');

INSERT INTO permissao (codigo, descricao) values (7, 'ROLE_PESQUISAR_FUNCIONARIO');
INSERT INTO permissao (codigo, descricao) values (8, 'ROLE_CADASTRAR_FUNCIONARIO');
INSERT INTO permissao (codigo, descricao) values (9, 'ROLE_REMOVER_FUNCIONARIO');

INSERT INTO permissao (codigo, descricao) values (10, 'ROLE_PESQUISAR_HORARIO');
INSERT INTO permissao (codigo, descricao) values (11, 'ROLE_CADASTRAR_HORARIO');
INSERT INTO permissao (codigo, descricao) values (12, 'ROLE_REMOVER_HORARIO');

INSERT INTO permissao (codigo, descricao) values (13, 'ROLE_ADMIN');

INSERT INTO permissao (codigo, descricao) values (14, 'ROLE_PESQUISAR_LAUDO');
INSERT INTO permissao (codigo, descricao) values (15, 'ROLE_CADASTRAR_LAUDO');
INSERT INTO permissao (codigo, descricao) values (16, 'ROLE_REMOVER_LAUDO');

INSERT INTO permissao (codigo, descricao) values (17, 'ROLE_GERAR_RELATORIO');
INSERT INTO permissao (codigo, descricao) values (18, 'ROLE_GERAR_LAUDO');

-- admin
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 13);
