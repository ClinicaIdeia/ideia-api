CREATE TABLE IF NOT EXISTS laudo (

  codigo              BIGINT PRIMARY KEY  NOT NULL,
  observacao          VARCHAR(100),
  data_exame          DATE                NOT NULL,
  data_emissao        DATE                NOT NULL,
  data_criacao        DATE                NOT NULL,
  data_atualizacao    DATE                NOT NULL,
  codigo_motivo       BIGINT              NOT NULL,
  codigo_funcionario  BIGINT              NOT NULL,
  codigo_agendamento  BIGINT              NOT NULL,
  FOREIGN KEY (codigo_motivo)       REFERENCES motivo(codigo),
  FOREIGN KEY (codigo_funcionario)  REFERENCES funcionario(codigo),
  FOREIGN KEY (codigo_agendamento)  REFERENCES agendamento(codigo)

);

CREATE SEQUENCE IF NOT EXISTS laudo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE IF NOT EXISTS laudo_aptidao (
	codigo_laudo       BIGINT NOT NULL,
	codigo_aptidao      BIGINT NOT NULL,
	PRIMARY KEY (codigo_laudo, codigo_aptidao),
	FOREIGN KEY (codigo_laudo)   REFERENCES laudo(codigo),
	FOREIGN KEY (codigo_aptidao)  REFERENCES aptidao(codigo)
);

CREATE TABLE IF NOT EXISTS laudo_funcionario (
	codigo_laudo       BIGINT NOT NULL,
	codigo_funcionario      BIGINT NOT NULL,
	PRIMARY KEY (codigo_laudo, codigo_funcionario),
	FOREIGN KEY (codigo_laudo)   REFERENCES laudo(codigo),
	FOREIGN KEY (codigo_funcionario)  REFERENCES funcionario(codigo)
);

CREATE TABLE IF NOT EXISTS laudo_agendamento (
	codigo_laudo       BIGINT NOT NULL,
	codigo_agendamento      BIGINT NOT NULL,
	PRIMARY KEY (codigo_laudo, codigo_agendamento),
	FOREIGN KEY (codigo_laudo)   REFERENCES laudo(codigo),
	FOREIGN KEY (codigo_agendamento)  REFERENCES agendamento(codigo)
);

CREATE TABLE IF NOT EXISTS laudo_motivo (
	codigo_laudo       BIGINT NOT NULL,
	codigo_motivo      BIGINT NOT NULL,
	PRIMARY KEY (codigo_laudo, codigo_motivo),
	FOREIGN KEY (codigo_laudo)   REFERENCES laudo(codigo),
	FOREIGN KEY (codigo_motivo)  REFERENCES motivo(codigo)
);