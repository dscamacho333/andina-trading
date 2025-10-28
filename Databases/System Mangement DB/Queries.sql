USE system_management_microservice;

# Delete Country By Id
UPDATE 
	COUNTRY c
SET
	c.is_active = False
WHERE
	c.id = 1;


# Find All Countries Active
SELECT
	c.id,
    c.code,
    c.name,
    c.is_active
FROM
	COUNTRY c
WHERE
	c.is_active = True;
    
# Delete City By Id
UPDATE 
	CITY c
SET
	c.is_active = False
WHERE
	c.id = 1;
    
# Find All Cities Active
SELECT
	c.id,
    c.name,
    c.is_active,
    c.country_id,
    c.economy_situation_id
FROM
	CITY c
WHERE
	c.is_active = True;
    
# Find All Cities Active By Economy Status Name
SELECT
	c.id,
    c.name,
    c.is_active,
    c.country_id,
    c.economy_situation_id
FROM
	CITY c
INNER JOIN
	ECONOMY_SITUATION ec ON c.economy_situation_id = ec.id
WHERE
	c.is_active AND ec.name = 'RECESION';
    
# Find All Active Economy Situations
SELECT
	es.id,
    es.name,
    es.description,
    es.is_active
FROM
	ECONOMY_SITUATION es
WHERE
	es.is_active = True;
    
# Find All Active Sectors

SELECT
	s.id,
    s.name,
    s.is_active
FROM
	SECTOR s
WHERE
	s.is_active = True;

# Find All Active Industries

SELECT
	i.id,
    i.sector_id,
    i.name,
    i.is_active
FROM
	INDUSTRY i
WHERE
	i.is_active = True;
    
# Delete Issuer By ID

UPDATE 
	ISSUER i
SET
	i.is_active = False
WHERE
	i.id = 8;

# Find All Active Issuers

SELECT
	i.id,
	i.name,
	i.ticker,                
	i.country_id,
	i.industry_id,                
	i.website,
	i.notes,
	i.is_active
FROM
	ISSUER i
WHERE
	i.is_active = True;
    
# Find All Issuers By Country

SELECT
	i.id,
	i.name,
	i.ticker,                
	i.country_id,
	i.industry_id,                
	i.website,
	i.notes,
	i.is_active
FROM
	ISSUER i
WHERE
	i.is_active = True AND i.country_id = 1;

# Find All Issuers By Industry

SELECT
	i.id,
	i.name,
	i.ticker,                
	i.country_id,
	i.industry_id,                
	i.website,
	i.notes,
	i.is_active
FROM
	ISSUER i
WHERE
	i.is_active = True AND i.industry_id = 1;

# Find All Issuers By Sector

SELECT
	i.id,
	i.name,
	i.ticker,                
	i.country_id,
	i.industry_id,                
	i.website,
	i.notes,
	i.is_active
FROM
	ISSUER i
INNER JOIN
	INDUSTRY ind ON i.industry_id = ind.id
WHERE
	i.is_active = True AND ind.sector_id = 1;
    
# Find all Active Document Types
SELECT
	d.id,
    d.code,
    d.name,
    d.country_id,
    d.is_active
FROM
	DOCUMENT_TYPE d
WHERE
	d.is_active = True;
    
    
# Delete Broker By ID
UPDATE 
	BROKER b
SET
	b.is_active = FALSE
WHERE
	b.id = 8;
    
# Find All Active Brokers
SELECT
	b.id,
    b.first_name,
    b.middle_name,
    b.last_name,
    b.document_type_id,
    b.document_number,
    b.email,
    b.phone,
    b.is_active,
    b.created_at,
    b.updated_at
FROM
	BROKER b
WHERE
	b.is_active = True;
    
# Find All Active Brokers By Country ID
SELECT
	b.id,
    b.first_name,
    b.middle_name,
    b.last_name,
    b.document_type_id,
    b.document_number,
    b.email,
    b.phone,
    b.is_active,
    b.created_at,
    b.updated_at
FROM
	BROKER b
INNER JOIN 
	DOCUMENT_TYPE dt ON b.document_type_id = dt.id
INNER JOIN 
	COUNTRY c ON dt.country_id = c.id
WHERE
	(b.is_active = True) AND (c.id = 1);
