CREATE TABLE IF NOT EXISTS configuracao (

  codigo              BIGINT PRIMARY KEY  NOT NULL,
  chave_configuracao  VARCHAR(100),
  valor               VARCHAR(255)

);

CREATE SEQUENCE IF NOT EXISTS configuracao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;