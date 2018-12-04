CREATE TABLE IF NOT EXISTS agendamento (

  codigo              BIGINT PRIMARY KEY  NOT NULL,
  observacao          VARCHAR(100),
  codigo_motivo       BIGINT        NOT NULL,
  codigo_agenda       BIGINT        NOT NULL,
  codigo_funcionario  BIGINT        NOT NULL,
  FOREIGN KEY (codigo_motivo)       REFERENCES motivo(codigo),
  FOREIGN KEY (codigo_agenda)       REFERENCES agenda(codigo),
  FOREIGN KEY (codigo_funcionario)  REFERENCES funcionario(codigo)


);

CREATE SEQUENCE IF NOT EXISTS agendamento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- CREATE TABLE IF NOT EXISTS  agendamento_agenda (
--   codigo_agendamento  BIGINT NOT NULL,
--   codigo_agenda      BIGINT NOT NULL,
--   PRIMARY KEY (codigo_agendamento, codigo_agenda),
--   FOREIGN KEY (codigo_agenda) REFERENCES agendamento(codigo),
--   FOREIGN KEY (codigo_agendamento) REFERENCES agendamento(codigo)
-- );
--
-- CREATE TABLE IF NOT EXISTS  agendamento_funcionario (
--   codigo_agendamento      BIGINT NOT NULL,
--   codigo_funcionario  BIGINT NOT NULL,
--   PRIMARY KEY (codigo_agendamento, codigo_funcionario),
--   FOREIGN KEY (codigo_agendamento) REFERENCES agendamento(codigo),
--   FOREIGN KEY (codigo_funcionario) REFERENCES funcionario(codigo)
-- );


