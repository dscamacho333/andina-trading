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
  
-- Catálogo de sectores
INSERT INTO SECTOR (name, is_active) VALUES
('Energía',        TRUE),
('Finanzas',       TRUE),
('Industrial',     TRUE),
('Tecnología',     TRUE);

-- INDUSTRY INSERTS

-- Energía
INSERT INTO INDUSTRY (sector_id, name, is_active) VALUES
((SELECT id FROM SECTOR WHERE name='Energía'), 'Petróleo y Gas',     TRUE),
((SELECT id FROM SECTOR WHERE name='Energía'), 'Energía Eléctrica',  TRUE);

-- Finanzas
INSERT INTO INDUSTRY (sector_id, name, is_active) VALUES
((SELECT id FROM SECTOR WHERE name='Finanzas'), 'Banca',   TRUE),
((SELECT id FROM SECTOR WHERE name='Finanzas'), 'Seguros', TRUE);

-- Industrial
INSERT INTO INDUSTRY (sector_id, name, is_active) VALUES
((SELECT id FROM SECTOR WHERE name='Industrial'), 'Manufactura',  TRUE),
((SELECT id FROM SECTOR WHERE name='Industrial'), 'Construcción', TRUE);

-- Tecnología
INSERT INTO INDUSTRY (sector_id, name, is_active) VALUES
((SELECT id FROM SECTOR WHERE name='Tecnología'), 'Software',            TRUE),
((SELECT id FROM SECTOR WHERE name='Tecnología'), 'Telecomunicaciones',  TRUE);

-- ISSUERS INSERTS

-- Colombia (CO)
INSERT INTO ISSUER (name, ticker, country_id, industry_id, website, notes, is_active) VALUES
('Ecopetrol S.A.',      'ECO',
 (SELECT id FROM COUNTRY WHERE code='CO'),
 (SELECT i.id FROM INDUSTRY i JOIN SECTOR s ON s.id=i.sector_id
   WHERE s.name='Energía' AND i.name='Petróleo y Gas'),
 'https://www.ecopetrol.com.co', '', TRUE),

('Bancolombia S.A.',    'BCO',
 (SELECT id FROM COUNTRY WHERE code='CO'),
 (SELECT i.id FROM INDUSTRY i JOIN SECTOR s ON s.id=i.sector_id
   WHERE s.name='Finanzas' AND i.name='Banca'),
 'https://www.grupobancolombia.com', '', TRUE);

-- Ecuador (EC)
INSERT INTO ISSUER (name, ticker, country_id, industry_id, website, notes, is_active) VALUES
('Banco Pichincha C.A.', 'BPI',
 (SELECT id FROM COUNTRY WHERE code='EC'),
 (SELECT i.id FROM INDUSTRY i JOIN SECTOR s ON s.id=i.sector_id
   WHERE s.name='Finanzas' AND i.name='Banca'),
 'https://www.pichincha.com', '', TRUE),

('Corporación Favorita C.A.', 'CFV',
 (SELECT id FROM COUNTRY WHERE code='EC'),
 (SELECT i.id FROM INDUSTRY i JOIN SECTOR s ON s.id=i.sector_id
   WHERE s.name='Industrial' AND i.name='Manufactura'),
 'https://www.corporacionfavorita.com', '', TRUE);

-- Perú (PE)
INSERT INTO ISSUER (name, ticker, country_id, industry_id, website, notes, is_active) VALUES
('Petroperú S.A.', 'PET',
 (SELECT id FROM COUNTRY WHERE code='PE'),
 (SELECT i.id FROM INDUSTRY i JOIN SECTOR s ON s.id=i.sector_id
   WHERE s.name='Energía' AND i.name='Petróleo y Gas'),
 'https://www.petroperu.com.pe', '', TRUE),

('Telefónica del Perú S.A.A.', 'TPR',
 (SELECT id FROM COUNTRY WHERE code='PE'),
 (SELECT i.id FROM INDUSTRY i JOIN SECTOR s ON s.id=i.sector_id
   WHERE s.name='Tecnología' AND i.name='Telecomunicaciones'),
 'https://www.telefonica.com.pe', '', TRUE);

-- Venezuela (VE)
INSERT INTO ISSUER (name, ticker, country_id, industry_id, website, notes, is_active) VALUES
('Petróleos de Venezuela S.A.', 'PDV',
 (SELECT id FROM COUNTRY WHERE code='VE'),
 (SELECT i.id FROM INDUSTRY i JOIN SECTOR s ON s.id=i.sector_id
   WHERE s.name='Energía' AND i.name='Petróleo y Gas'),
 'https://www.pdvsa.com', '', TRUE);