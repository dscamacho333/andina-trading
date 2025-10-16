USE system_management_microservice;

-- ECONOMY_SITUATION
INSERT INTO ECONOMY_SITUATION (name, description) VALUES
('EXPANSION', 'Crecimiento económico superior a la tendencia.'),
('ESTABLE',   'Actividad económica sin cambios relevantes.'),
('RECESION',  'Contracción de la actividad económica.');

-- COUNTRY
INSERT INTO COUNTRY (code, name) VALUES
('CO', 'Colombia'),
('EC', 'Ecuador'),
('PE', 'Peru'),
('VE', 'Venezuela');

-- CITY (usa subconsultas para FK)
-- Colombia
INSERT INTO CITY (name, is_active, country_id, economy_situation_id) VALUES
('Bogota',   TRUE,
  (SELECT id FROM COUNTRY WHERE code='CO'),
  (SELECT id FROM ECONOMY_SITUATION WHERE name='ESTABLE')),
('Medellin', TRUE,
  (SELECT id FROM COUNTRY WHERE code='CO'),
  (SELECT id FROM ECONOMY_SITUATION WHERE name='EXPANSION')),
('Cali',     TRUE,
  (SELECT id FROM COUNTRY WHERE code='CO'),
  (SELECT id FROM ECONOMY_SITUATION WHERE name='RECESION'));

-- Ecuador
INSERT INTO CITY (name, is_active, country_id, economy_situation_id) VALUES
('Quito',     TRUE,
  (SELECT id FROM COUNTRY WHERE code='EC'),
  (SELECT id FROM ECONOMY_SITUATION WHERE name='ESTABLE')),
('Guayaquil', TRUE,
  (SELECT id FROM COUNTRY WHERE code='EC'),
  (SELECT id FROM ECONOMY_SITUATION WHERE name='EXPANSION'));

-- Peru
INSERT INTO CITY (name, is_active, country_id, economy_situation_id) VALUES
('Lima',     TRUE,
  (SELECT id FROM COUNTRY WHERE code='PE'),
  (SELECT id FROM ECONOMY_SITUATION WHERE name='ESTABLE')),
('Arequipa', TRUE,
  (SELECT id FROM COUNTRY WHERE code='PE'),
  (SELECT id FROM ECONOMY_SITUATION WHERE name='RECESION'));

-- Venezuela
INSERT INTO CITY (name, is_active, country_id, economy_situation_id) VALUES
('Caracas',    TRUE,
  (SELECT id FROM COUNTRY WHERE code='VE'),
  (SELECT id FROM ECONOMY_SITUATION WHERE name='RECESION')),
('Maracaibo',  TRUE,
  (SELECT id FROM COUNTRY WHERE code='VE'),
  (SELECT id FROM ECONOMY_SITUATION WHERE name='ESTABLE'));
