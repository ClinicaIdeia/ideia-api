CREATE TABLE IF NOT EXISTS motivo (

  codigo              BIGINT PRIMARY KEY  NOT NULL,
  descricao           VARCHAR(100)        NOT NULL,
  observacao          VARCHAR(255)

  );

CREATE SEQUENCE IF NOT EXISTS motivo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

INSERT INTO motivo(codigo, descricao, observacao)
VALUES
(nextval('motivo_seq'), 'Admissão',''),
(nextval('motivo_seq'), 'Periódico',''),
(nextval('motivo_seq'), 'Demissão',''),
(nextval('motivo_seq'), 'Mudança de Função',''),
(nextval('motivo_seq'), 'Retorno ao Trabalho',''),
(nextval('motivo_seq'), 'Porte de Arma de Fogo',''),
(nextval('motivo_seq'), 'Registro de Arma de Fogo',''),
(nextval('motivo_seq'), 'Certificado de Registro de Arma de Fogo no Exército ',''),
(nextval('motivo_seq'), 'Renovação de Certificado de Registro de Arma de Fogo.',''),
(nextval('motivo_seq'), 'Curso de Vigilante',''),
(nextval('motivo_seq'), 'Reciclagem de Curso de Vigilante.','');



