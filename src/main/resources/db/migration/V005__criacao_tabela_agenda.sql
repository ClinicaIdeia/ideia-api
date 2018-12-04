CREATE TABLE IF NOT EXISTS agenda (

  codigo              BIGINT PRIMARY KEY  NOT NULL,
  dia_agenda          DATE                NOT NULL,
  observacao          VARCHAR(100)

  );

CREATE SEQUENCE IF NOT EXISTS agenda_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS  agenda_horario (
	codigo_agenda       BIGINT NOT NULL,
	codigo_horario      BIGINT NOT NULL,
	PRIMARY KEY (codigo_agenda, codigo_horario),
	FOREIGN KEY (codigo_agenda)   REFERENCES agenda(codigo),
	FOREIGN KEY (codigo_horario)  REFERENCES horario(codigo)
);




