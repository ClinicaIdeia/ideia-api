CREATE TABLE IF NOT EXISTS func_cargo_empresa (

  codigo              BIGINT PRIMARY KEY  NOT NULL,
  cargo               VARCHAR(50),
  data_criacao        TIMESTAMP           NOT NULL,
  data_atualizacao    TIMESTAMP           NOT NULL,
  cod_funcionario  BIGINT              NOT NULL,
  cod_empresa      BIGINT              NOT NULL

);

CREATE SEQUENCE IF NOT EXISTS func_cargo_empresa_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE func_cargo_empresa
  add CONSTRAINT func_emp_unique UNIQUE (cod_funcionario, cod_empresa);