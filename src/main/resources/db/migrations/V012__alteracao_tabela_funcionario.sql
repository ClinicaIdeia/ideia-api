ALTER TABLE funcionario DROP COLUMN IF EXISTS numero_cadastro;
ALTER TABLE funcionario ADD COLUMN numero_cadastro BIGINT;
ALTER TABLE funcionario DROP CONSTRAINT funcionario_cpf_key;
ALTER TABLE funcionario ALTER COLUMN cpf DROP NOT NULL;