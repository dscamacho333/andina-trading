CREATE DATABASE system_management_microservice;

USE system_management_microservice;


CREATE TABLE ECONOMY_SITUATION(
	
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    
    PRIMARY KEY (id)

);

CREATE TABLE COUNTRY(

	id INTEGER NOT NULL AUTO_INCREMENT,
    code VARCHAR(2) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL,
    
    PRIMARY KEY (id)
);


CREATE TABLE CITY(

	id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(25) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL,
    country_id INTEGER NOT NULL,
    economy_situation_id INTEGER NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (country_id) REFERENCES COUNTRY(id),
    FOREIGN KEY (economy_situation_id) REFERENCES ECONOMY_SITUATION(id)
    
)