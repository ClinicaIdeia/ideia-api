ALTER TABLE agendamento DROP COLUMN IF EXISTS hora_exame;
ALTER TABLE agendamento ADD COLUMN hora_exame time ;