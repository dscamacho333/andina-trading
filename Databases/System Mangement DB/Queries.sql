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