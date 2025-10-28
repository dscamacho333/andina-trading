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
 
 -- CO
INSERT INTO DOCUMENT_TYPE (code, name, country_id) VALUES
('CC',   'Cédula de Ciudadanía', (SELECT id FROM COUNTRY WHERE code='CO'));
-- EC
INSERT INTO DOCUMENT_TYPE (code, name, country_id) VALUES
('CI',   'Cédula de Identidad',   (SELECT id FROM COUNTRY WHERE code='EC'));
-- PE
INSERT INTO DOCUMENT_TYPE (code, name, country_id) VALUES
('DNI',  'Documento Nacional de Identidad', (SELECT id FROM COUNTRY WHERE code='PE'));
-- VE
INSERT INTO DOCUMENT_TYPE (code, name, country_id) VALUES
('CV', 'Cédula de Identidad Venezolana', (SELECT id FROM COUNTRY WHERE code='VE'));

-- ===== COLOMBIA (CO) - document_type: CC
INSERT INTO BROKER
  (first_name, middle_name, last_name, document_type_id, document_number, email, phone, is_active)
VALUES
('Ana',   NULL, 'Pérez',
  (SELECT dt.id FROM DOCUMENT_TYPE dt JOIN COUNTRY c ON c.id = dt.country_id
     WHERE c.code = 'CO' AND dt.code = 'CC'),
 '10203040', 'ana.perez@broker.com', '+57-3001234567', TRUE),

('Luis',  NULL, 'García',
  (SELECT dt.id FROM DOCUMENT_TYPE dt JOIN COUNTRY c ON c.id = dt.country_id
     WHERE c.code = 'CO' AND dt.code = 'CC'),
 '20406080', 'luis.garcia@broker.com', '+57-3012345678', TRUE);

-- ===== ECUADOR (EC) - document_type: CI
INSERT INTO BROKER
  (first_name, middle_name, last_name, document_type_id, document_number, email, phone, is_active)
VALUES
('María',  NULL, 'López',
  (SELECT dt.id FROM DOCUMENT_TYPE dt JOIN COUNTRY c ON c.id = dt.country_id
     WHERE c.code = 'EC' AND dt.code = 'CI'),
 '0912345678', 'maria.lopez@broker.com', '+593-991112233', TRUE),

('Diego', 'A.', 'Torres',
  (SELECT dt.id FROM DOCUMENT_TYPE dt JOIN COUNTRY c ON c.id = dt.country_id
     WHERE c.code = 'EC' AND dt.code = 'CI'),
 '0922334455', 'diego.torres@broker.com', '+593-981234567', TRUE);  -- inactivo para pruebas

-- ===== PERÚ (PE) - document_type: DNI
INSERT INTO BROKER
  (first_name, middle_name, last_name, document_type_id, document_number, email, phone, is_active)
VALUES
('Carla', NULL, 'Rojas',
  (SELECT dt.id FROM DOCUMENT_TYPE dt JOIN COUNTRY c ON c.id = dt.country_id
     WHERE c.code = 'PE' AND dt.code = 'DNI'),
 '45678901', 'carla.rojas@broker.com', '+51-981122334', TRUE),

('José',  NULL, 'Vega',
  (SELECT dt.id FROM DOCUMENT_TYPE dt JOIN COUNTRY c ON c.id = dt.country_id
     WHERE c.code = 'PE' AND dt.code = 'DNI'),
 '11223344', 'jose.vega@broker.com', '+51-987654321', TRUE);

-- ===== VENEZUELA (VE) - document_type: CV
INSERT INTO BROKER
  (first_name, middle_name, last_name, document_type_id, document_number, email, phone, is_active)
VALUES
('Sofía',  NULL, 'Martínez',
  (SELECT dt.id FROM DOCUMENT_TYPE dt JOIN COUNTRY c ON c.id = dt.country_id
     WHERE c.code = 'VE' AND dt.code = 'CV'),
 'V-12345678', 'sofia.martinez@broker.com', '+58-2123456789', TRUE),

('Pedro',  NULL, 'Gómez',
  (SELECT dt.id FROM DOCUMENT_TYPE dt JOIN COUNTRY c ON c.id = dt.country_id
     WHERE c.code = 'VE' AND dt.code = 'CV'),
 'V-87654321', 'pedro.gomez@broker.com', '+58-2141112233', TRUE);
