CREATE TABLE IF NOT EXISTS horario (
  codigo            BIGINT PRIMARY KEY        NOT NULL,
  hora_exame        VARCHAR(10)               NOT NULL,
  maximo_permitido  INTEGER                   NOT NULL DEFAULT 15,
  restante          INTEGER                   NOT NULL DEFAULT 0,
  disponivel        BOOLEAN NOT NULL DEFAULT  FALSE,
  avulso            BOOLEAN NOT NULL DEFAULT  FALSE
  );

CREATE SEQUENCE IF NOT EXISTS horarioseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;