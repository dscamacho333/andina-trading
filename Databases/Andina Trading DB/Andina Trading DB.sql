CREATE DATABASE andina_trading;

USE andina_trading;

CREATE TABLE ECONOMY_SITUATION(
	
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    PRIMARY KEY (id)

);

CREATE TABLE COUNTRY(

	id INTEGER NOT NULL AUTO_INCREMENT,
    code VARCHAR(2) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    PRIMARY KEY (id)
);


CREATE TABLE CITY(

	id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(25) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    country_id INTEGER NOT NULL,
    economy_situation_id INTEGER NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (country_id) REFERENCES COUNTRY(id),
    FOREIGN KEY (economy_situation_id) REFERENCES ECONOMY_SITUATION(id)
    
);

CREATE TABLE SECTOR(

	id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    PRIMARY KEY (id)
    
);

CREATE TABLE INDUSTRY(

	id INTEGER NOT NULL AUTO_INCREMENT,
    sector_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    PRIMARY KEY (id),
    FOREIGN KEY (sector_id) REFERENCES SECTOR(id)
    
);


CREATE TABLE ISSUER (

  id  INTEGER AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL UNIQUE,
  ticker VARCHAR(10) NOT NULL UNIQUE,                
  country_id INTEGER NOT NULL,
  industry_id INTEGER NOT NULL,                
  website VARCHAR(200),
  notes VARCHAR(500) NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  
  PRIMARY KEY (id),
  FOREIGN KEY (country_id) REFERENCES COUNTRY(id),
  FOREIGN KEY (industry_id) REFERENCES INDUSTRY(id)
  
);

CREATE TABLE DOCUMENT_TYPE(
	
    id INTEGER NOT NULL AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
	country_id INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    PRIMARY KEY (id),
    FOREIGN KEY (country_id) REFERENCES COUNTRY(id)

);


CREATE TABLE BROKER (
  
	id INTEGER NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    document_type_id INTEGER NOT NULL,
    document_number VARCHAR(30) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
	is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
	PRIMARY KEY (id),
    FOREIGN KEY (document_type_id) REFERENCES DOCUMENT_TYPE(id),
	CONSTRAINT uk_broker_doctype_docnumber UNIQUE (document_type_id, document_number)
);


-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: forest_trade_db
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


DROP TABLE IF EXISTS `markets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `markets` (
  `market_id` int NOT NULL AUTO_INCREMENT,
  `market_code` varchar(10) NOT NULL,
  `open_time` time DEFAULT NULL,
  `close_time` time DEFAULT NULL,
  `business_days` json DEFAULT NULL,
  `edited_by` int DEFAULT NULL,
  PRIMARY KEY (`market_id`),
  UNIQUE KEY `market_code` (`market_code`),
  KEY `edited_by` (`edited_by`),
  CONSTRAINT `markets_ibfk_1` FOREIGN KEY (`edited_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `markets`
--

LOCK TABLES `markets` WRITE;
/*!40000 ALTER TABLE `markets` DISABLE KEYS */;
INSERT INTO `markets` VALUES (1,'NASDAQ',NULL,NULL,NULL,NULL),(2,'NYSE',NULL,NULL,NULL,NULL),(3,'AMEX',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `markets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_preferences`
--

DROP TABLE IF EXISTS `notification_preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification_preferences` (
  `preference_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `monthly_report` tinyint(1) DEFAULT NULL,
  `daily_summary` tinyint(1) DEFAULT NULL,
  `notification_channel` enum('EMAIL','SMS','APP') DEFAULT NULL,
  PRIMARY KEY (`preference_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `notification_preferences_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_preferences`
--

LOCK TABLES `notification_preferences` WRITE;
/*!40000 ALTER TABLE `notification_preferences` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_preferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_approval`
--

DROP TABLE IF EXISTS `order_approval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_approval` (
  `approval_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `approval_result` enum('APPROVED','REJECTED') DEFAULT NULL,
  `approval_date` datetime DEFAULT NULL,
  PRIMARY KEY (`approval_id`),
  KEY `order_id` (`order_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `order_approval_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `order_approval_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_approval`
--

LOCK TABLES `order_approval` WRITE;
/*!40000 ALTER TABLE `order_approval` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_approval` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `symbol` varchar(255) NOT NULL,
  `quantity` int DEFAULT NULL,
  `order_type` enum('MARKET','LIMIT','STOP','STOP_LIMIT') DEFAULT NULL,
  `status` enum('SENDED','REJECTED','SIGNED','APPROVED','QUEUED','PENDING_BROKER','PENDING_INVESTOR') DEFAULT NULL,
  `limit_price` float DEFAULT NULL,
  `requires_signature` tinyint(1) DEFAULT NULL,
  `signed_by` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `sent_to_alpaca_at` datetime DEFAULT NULL,
  `sent_to_alpaca` tinyint(1) DEFAULT NULL,
  `alpaca_id` varchar(255) DEFAULT NULL,
  `filled_price` float DEFAULT NULL,
  `time_in_force` enum('DAY','GTC','OPG','CLS','IOC','FOK') DEFAULT NULL,
  `stop_price` float DEFAULT NULL,
  `platform_commission` float DEFAULT NULL,
  `broker_commission` float DEFAULT NULL,
  `total_amount_paid` float DEFAULT NULL,
  `alpaca_sended_status` varchar(255) DEFAULT NULL,
  `initiated_by` enum('INVESTOR','STOCKBROKER') DEFAULT NULL,
  `stockbroker_id` int DEFAULT NULL,
  `order_side` enum('BUY','SELL') DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `signed_by` (`signed_by`),
  KEY `idx_orders_user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`signed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (4,2,'EAD',2,'MARKET','SENDED',NULL,0,NULL,'2025-04-15 17:44:40','2025-04-15 17:44:40',1,'a1852fd8-dc53-4175-82ed-638a7636105b',12.88,'IOC',NULL,2.576,0,15.456,'FILLED',NULL,NULL,'BUY'),(5,2,'CCSI',1,'MARKET','SENDED',0,0,NULL,'2025-04-15 13:33:39','2025-04-15 13:33:39',1,'db0b10a1-04f2-409a-8d2c-d193be8ee7b0',19.21,'DAY',0,3.83,0,23.04,'FILLED',NULL,NULL,'BUY'),(6,2,'CCSI',1,'LIMIT','SENDED',20,0,NULL,'2025-04-15 14:49:12','2025-04-15 14:49:12',1,'97c43815-4a41-4198-9f40-915a9b165861',19.13,'DAY',0,3.83,0,22.96,'FILLED',NULL,NULL,'BUY'),(7,2,'CDE',1,'LIMIT','SENDED',10,0,NULL,'2025-04-15 14:57:48','2025-04-15 14:57:48',1,'28b3b7d5-d1ab-45eb-8f75-357c4dd9f01d',6.08,'DAY',0,1.215,0,7.295,'FILLED',NULL,NULL,'BUY'),(8,2,'CDLX',1,'LIMIT','SENDED',1.1,0,NULL,'2025-04-15 15:04:05','2025-04-15 15:04:05',1,'9b427753-58f9-484e-8882-8996631417b3',1.555,'DAY',0,0.311,0,1.866,'ACCEPTED',NULL,NULL,'BUY'),(9,2,'CDLX',1,'LIMIT','SENDED',2,0,NULL,'2025-04-15 15:04:50','2025-04-15 15:04:50',1,'775c13e2-f11c-4394-a8a4-fc20a83b9f6a',1.555,'DAY',0,0.311,0,1.866,'ACCEPTED',NULL,NULL,'BUY'),(10,2,'VSCO',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-17 03:25:44','2025-04-17 03:25:44',1,'76c68ec1-5773-4238-8624-d5c9f8615474',15.62,'IOC',NULL,0.31,0,15.93,'ACCEPTED','INVESTOR',NULL,'BUY'),(11,2,'F',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-18 20:38:28','2025-04-18 20:38:28',1,'76cf14bc-d0f4-461e-80df-d07235ffa135',9.63,'IOC',NULL,0.19,0,9.82,'ACCEPTED','INVESTOR',NULL,'BUY'),(12,2,'CDE',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-19 16:19:00','2025-04-19 16:19:00',1,'22b88875-ff94-4df0-8b4f-eff929f607b1',6.03,'IOC',NULL,0.12,0,6.15,'ACCEPTED','INVESTOR',NULL,'BUY'),(13,2,'CDE',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-19 16:38:52','2025-04-19 16:38:52',1,'ed6f64e2-1826-4d50-a48f-3d43634eea2e',6.03,'IOC',NULL,0.12,0,6.15,'ACCEPTED','INVESTOR',NULL,'BUY'),(14,2,'RNRG',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 00:41:19','2025-04-20 00:41:19',1,'a9bb61eb-03e3-4226-8add-97298e926048',8.58,'IOC',NULL,0.17,0,8.75,'ACCEPTED','INVESTOR',NULL,'BUY'),(15,2,'CCB',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 00:51:10','2025-04-20 00:51:10',1,'3aa5bebd-7413-4aac-acf0-27e33bc48e3a',83.25,'IOC',NULL,1.67,0,84.92,'ACCEPTED','INVESTOR',NULL,'BUY'),(16,2,'TDF',1,'LIMIT','SENDED',20,0,NULL,'2025-04-20 01:06:32','2025-04-20 01:06:32',1,'a829f95d-eb51-4363-bb72-871562a09109',8.79,'IOC',NULL,0.18,0,8.97,'ACCEPTED','INVESTOR',NULL,'BUY'),(17,2,'RNRG',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 02:15:00','2025-04-20 02:15:00',1,'bde0bddd-04a0-4116-8dfc-c8b9bfabde22',8.58,'IOC',NULL,0.17,0,8.75,'ACCEPTED','INVESTOR',NULL,'BUY'),(18,2,'TDF',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 03:02:10','2025-04-20 03:02:10',1,'492b4b8d-21e5-426f-9b2a-2fa7e5d42461',8.79,'IOC',NULL,0.18,0,8.97,'ACCEPTED','INVESTOR',NULL,'BUY'),(19,2,'TDF',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 03:02:28','2025-04-20 03:02:28',1,'53997adf-3b80-4fb6-8f30-4f404238f1d6',8.79,'IOC',NULL,0.18,0,8.97,'ACCEPTED','INVESTOR',NULL,'BUY'),(20,2,'TDF',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 03:03:22','2025-04-20 03:03:22',1,'47be076c-7762-46c6-bfab-2207fdf197e3',8.79,'IOC',NULL,0.18,0,8.97,'ACCEPTED','INVESTOR',NULL,'BUY'),(21,2,'TDF',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 03:03:57','2025-04-20 03:03:57',1,'f0b0e4e7-3f45-450f-a525-80e20b152b34',8.79,'IOC',NULL,0.18,0,8.97,'ACCEPTED','INVESTOR',NULL,'BUY'),(22,2,'TDF',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 03:31:51','2025-04-20 03:31:51',1,'23358e23-d4fb-4519-b358-fd6e8ff738d8',8.79,'IOC',NULL,0.18,0,8.97,'ACCEPTED','INVESTOR',NULL,'BUY'),(23,2,'TDF',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 15:17:54','2025-04-20 15:17:54',1,'b61c6f6f-9c91-4e5f-bf1e-24d41826884e',8.79,'IOC',NULL,0.18,0,8.97,'ACCEPTED','INVESTOR',NULL,'BUY'),(24,2,'TDF',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 15:46:59','2025-04-20 15:46:59',1,'78c34e65-7a8e-4f46-a43a-17ee0cfe61eb',8.79,'IOC',NULL,0.18,0,8.97,'ACCEPTED','INVESTOR',NULL,'BUY'),(25,2,'TDACW',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 15:49:45','2025-04-20 15:49:45',1,'eebd26ee-f3dd-476a-90e3-181c28d46540',3.6,'IOC',NULL,0.07,0,3.67,'ACCEPTED','INVESTOR',NULL,'BUY'),(26,2,'AERTW',30,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 20:31:48','2025-04-20 20:31:48',1,'c922cb4d-aa9b-410b-ba92-f33535d30cae',0.672,'IOC',NULL,0.01,0,0.682,'ACCEPTED','INVESTOR',NULL,'BUY'),(27,2,'AERT',1,'MARKET','SENDED',NULL,0,NULL,'2025-04-20 21:52:18','2025-04-20 21:52:18',1,'64289db4-9c6d-440e-b4a6-d2446ee0e22b',0.709,'GTC',NULL,0.01,0,0.719,'ACCEPTED','INVESTOR',NULL,'BUY');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `price_alerts`
--

DROP TABLE IF EXISTS `price_alerts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `price_alerts` (
  `alert_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `symbol` varchar(20) NOT NULL,
  `price_threshold` float NOT NULL,
  `condition_alert` enum('LESS','GREATER','EQUAL') DEFAULT NULL,
  `notified` tinyint(1) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `notification_date` datetime DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`alert_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `price_alerts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `price_alerts`
--

LOCK TABLES `price_alerts` WRITE;
/*!40000 ALTER TABLE `price_alerts` DISABLE KEYS */;
/*!40000 ALTER TABLE `price_alerts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stockbroker_contracts`
--

DROP TABLE IF EXISTS `stockbroker_contracts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stockbroker_contracts` (
  `contract_id` int NOT NULL AUTO_INCREMENT,
  `investor_id` int NOT NULL,
  `stockbroker_id` int NOT NULL,
  `start_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `end_date` datetime DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`contract_id`),
  KEY `investor_id` (`investor_id`),
  KEY `stockbroker_id` (`stockbroker_id`),
  CONSTRAINT `stockbroker_contracts_ibfk_1` FOREIGN KEY (`investor_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `stockbroker_contracts_ibfk_2` FOREIGN KEY (`stockbroker_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stockbroker_contracts`
--

LOCK TABLES `stockbroker_contracts` WRITE;
/*!40000 ALTER TABLE `stockbroker_contracts` DISABLE KEYS */;
/*!40000 ALTER TABLE `stockbroker_contracts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscriptions` (
  `subscription_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `start_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `end_date` datetime DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `payment_method` varchar(40) DEFAULT NULL,
  `type` enum('MONTHLY','YEARLY') DEFAULT NULL,
  PRIMARY KEY (`subscription_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `subscriptions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriptions`
--

LOCK TABLES `subscriptions` WRITE;
/*!40000 ALTER TABLE `subscriptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_logs`
--

DROP TABLE IF EXISTS `system_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_logs` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `entity_id` int NOT NULL,
  `user_id` int NOT NULL,
  `modified_entity` varchar(50) DEFAULT NULL,
  `details` varchar(500) NOT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `system_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_logs`
--

LOCK TABLES `system_logs` WRITE;
/*!40000 ALTER TABLE `system_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `type` enum('RECHARGE','BUY','SELL','COMMISSION_FT','COMMISSION_BROKER') NOT NULL,
  `amount` float NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `status` enum('CANCELED','CONFIRMED','PENDING') DEFAULT NULL,
  `alpaca_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_transaction_user` (`user_id`),
  KEY `fk_transaction_order` (`order_id`),
  CONSTRAINT `fk_transaction_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `fk_transaction_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,2,'BUY',-6.03,'2025-04-19 16:38:52',13,'Compra de 1 acciones de CDE','2025-04-19 16:38:52.092466','PENDING',NULL),(2,2,'COMMISSION_FT',-0.12,'2025-04-19 16:38:52',13,'Comisión de la plataforma por compra de acciones','2025-04-19 16:38:52.183499','PENDING',NULL),(3,2,'BUY',-8.58,'2025-04-20 00:41:19',14,'Compra de 1 acciones de RNRG','2025-04-20 00:41:19.411537','PENDING',NULL),(4,2,'COMMISSION_FT',-0.17,'2025-04-20 00:41:19',14,'Comisión de la plataforma por compra de acciones','2025-04-20 00:41:19.454652','PENDING',NULL),(5,2,'BUY',-83.25,'2025-04-20 00:51:09',15,'Compra de 1 acciones de CCB','2025-04-20 00:51:09.676127','PENDING',NULL),(6,2,'COMMISSION_FT',-1.67,'2025-04-20 00:51:09',15,'Comisión de la plataforma por compra de acciones','2025-04-20 00:51:09.700247','PENDING',NULL),(7,2,'BUY',-8.79,'2025-04-20 01:06:32',16,'Compra de 1 acciones de TDF','2025-04-20 01:06:32.451419','PENDING',NULL),(8,2,'COMMISSION_FT',-0.18,'2025-04-20 01:06:32',16,'Comisión de la plataforma por compra de acciones','2025-04-20 01:06:32.455495','PENDING',NULL),(9,2,'BUY',-8.58,'2025-04-20 02:14:59',17,'Compra de 1 acciones de RNRG','2025-04-20 02:14:59.893972','PENDING',NULL),(10,2,'COMMISSION_FT',-0.17,'2025-04-20 02:14:59',17,'Comisión de la plataforma por compra de acciones','2025-04-20 02:14:59.909261','PENDING',NULL),(11,2,'BUY',-8.79,'2025-04-20 03:02:10',18,'Compra de 1 acciones de TDF','2025-04-20 03:02:10.030665','PENDING',NULL),(12,2,'COMMISSION_FT',-0.18,'2025-04-20 03:02:10',18,'Comisión de la plataforma por compra de acciones','2025-04-20 03:02:10.118168','PENDING',NULL),(13,2,'BUY',-8.79,'2025-04-20 03:02:27',19,'Compra de 1 acciones de TDF','2025-04-20 03:02:27.905102','PENDING',NULL),(14,2,'COMMISSION_FT',-0.18,'2025-04-20 03:02:27',19,'Comisión de la plataforma por compra de acciones','2025-04-20 03:02:27.913053','PENDING',NULL),(15,2,'BUY',-8.79,'2025-04-20 03:03:21',20,'Compra de 1 acciones de TDF','2025-04-20 03:03:21.524948','PENDING',NULL),(16,2,'COMMISSION_FT',-0.18,'2025-04-20 03:03:21',20,'Comisión de la plataforma por compra de acciones','2025-04-20 03:03:21.534142','PENDING',NULL),(17,2,'BUY',-8.79,'2025-04-20 03:03:57',21,'Compra de 1 acciones de TDF','2025-04-20 03:03:57.157870','PENDING',NULL),(18,2,'COMMISSION_FT',-0.18,'2025-04-20 03:03:57',21,'Comisión de la plataforma por compra de acciones','2025-04-20 03:03:57.171737','PENDING',NULL),(19,2,'BUY',-8.79,'2025-04-20 03:31:51',22,'Compra de 1 acciones de TDF','2025-04-20 03:31:51.001937','PENDING',NULL),(20,2,'COMMISSION_FT',-0.18,'2025-04-20 03:31:51',22,'Comisión de la plataforma por compra de acciones','2025-04-20 03:31:51.010374','PENDING',NULL),(21,2,'BUY',-8.79,'2025-04-20 15:17:53',23,'Compra de 1 acciones de TDF','2025-04-20 15:17:53.755904','PENDING',NULL),(22,2,'COMMISSION_FT',-0.18,'2025-04-20 15:17:53',23,'Comisión de la plataforma por compra de acciones','2025-04-20 15:17:53.763815','PENDING',NULL),(23,2,'BUY',-8.79,'2025-04-20 15:46:59',24,'Compra de 1 acciones de TDF','2025-04-20 15:46:59.291784','PENDING',NULL),(24,2,'COMMISSION_FT',-0.18,'2025-04-20 15:46:59',24,'Comisión de la plataforma por compra de acciones','2025-04-20 15:46:59.297133','PENDING',NULL),(25,2,'BUY',-3.6,'2025-04-20 15:49:45',25,'Compra de 1 acciones de TDACW','2025-04-20 15:49:45.255014','PENDING',NULL),(26,2,'COMMISSION_FT',-0.07,'2025-04-20 15:49:45',25,'Comisión de la plataforma por compra de acciones','2025-04-20 15:49:45.259015','PENDING',NULL),(27,2,'BUY',-0.672,'2025-04-20 20:31:47',26,'Compra de 30 acciones de AERTW','2025-04-20 20:31:47.950973','PENDING',NULL),(28,2,'COMMISSION_FT',-0.01,'2025-04-20 20:31:47',26,'Comisión de la plataforma por compra de acciones','2025-04-20 20:31:47.976951','PENDING',NULL),(29,2,'BUY',-0.709,'2025-04-20 21:52:18',27,'Compra de 1 acciones de AERT','2025-04-20 21:52:18.034757','PENDING',NULL),(30,2,'COMMISSION_FT',-0.01,'2025-04-20 21:52:18',27,'Comisión de la plataforma por compra de acciones','2025-04-20 21:52:18.041668','PENDING',NULL),(31,4,'RECHARGE',100,'2025-04-21 00:56:27',NULL,'Recarga realizada por ForestTrade','2025-04-21 00:56:27.282809','PENDING','fd5426b3-a5ef-4d40-ad33-5f5b5c5382b3'),(32,6,'RECHARGE',1000,'2025-04-21 01:22:52',NULL,'Recarga realizada por ForestTrade','2025-04-21 01:22:52.346845','PENDING','bfe62420-40ab-4d1f-bb8a-12c9c0b2f52f'),(33,7,'RECHARGE',200,'2025-04-21 01:54:39',NULL,'Recarga realizada por ForestTrade','2025-04-21 01:54:39.502560','PENDING','0e8fe5a7-65c7-403f-8dbc-f70d9d68e134');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `user_role` enum('INVESTOR','BROKER','ADMIN') NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `account_status` enum('ACTIVE','INACTIVE','RESTRICTED') DEFAULT 'ACTIVE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_access` datetime DEFAULT NULL,
  `has_subscription` tinyint(1) DEFAULT '0',
  `alpaca_status` varchar(30) DEFAULT NULL,
  `alpaca_account_id` varchar(100) DEFAULT NULL,
  `daily_order_limit` int DEFAULT '0',
  `default_order_type` enum('MARKET','LIMIT','STOP LOSS','TAKE PROFIT') DEFAULT NULL,
  `balance` float DEFAULT '0',
  `commission_rate` double DEFAULT NULL,
  `role_user` enum('ADMIN','INVESTOR','STOCKBROKER') NOT NULL,
  `bank_relationship_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES 
(2,'Melanny Jutinico','mjutinico@unbosque.edu.co','$2a$10$wLdT.KKZWf0CEEzCEmn.B.Nhr/yv2QYLuXCnTdhEQ7AAoxX2MngLu','INVESTOR','+573259650423','ACTIVE','2025-04-14 00:06:55',NULL,0,'APPROVED','55f117cb-f308-4138-b482-a1e0d452e78c',NULL,NULL,1.589,NULL,'INVESTOR','4b4d1dd2-adb6-4a35-bdd6-4888ec0514d2'),
(4,'Miguel Alvarado','kind_caver_30481491@example.com','$2a$10$wLdT.KKZWf0CEEzCEmn.B.Nhr/yv2QYLuXCnTdhEQ7AAoxX2MngLu','INVESTOR','+571234567890','ACTIVE','2025-04-15 00:06:55',NULL,0,'ACTIVE','648e4e45-8797-4b9c-a3fb-07848db382c8',0,NULL,0,NULL,'INVESTOR','f80220fe-c1f9-436c-9517-ec2e50a3b468'),
(5,'Santiago Camacho','miguel@mail.com','$2a$10$wLdT.KKZWf0CEEzCEmn.B.Nhr/yv2QYLuXCnTdhEQ7AAoxX2MngLu','INVESTOR','+571234567890','ACTIVE','2025-04-15 00:06:55',NULL,0,'ACTIVE','c35c3fd9-9578-48cf-9a7a-e11adbff5845',0,NULL,0,NULL,'INVESTOR','fd635e1c-03d9-4c50-b50d-ca631c1107a2'),
(6,'Pepe Grillo','test@test.com','$2a$10$wLdT.KKZWf0CEEzCEmn.B.Nhr/yv2QYLuXCnTdhEQ7AAoxX2MngLu','INVESTOR','+571234567890','ACTIVE','2025-04-15 00:06:55',NULL,0,'ACTIVE','54adb89f-d78f-48b9-a6a6-30f2bca8e395',0,NULL,0,NULL,'INVESTOR','62497ce2-5cac-4103-9f23-7132be6660b0'),
(7,'Kind Carver','kind_ca_30481491@example.com','$2a$10$wLdT.KKZWf0CEEzCEmn.B.Nhr/yv2QYLuXCnTdhEQ7AAoxX2MngLu','INVESTOR','+571234567890','ACTIVE','2025-04-17 00:06:55',NULL,0,'ACTIVE','5d69edb8-c4e3-4114-9b3e-6b88592c6a6b',0,NULL,0,NULL,'INVESTOR','82c374f5-ddd8-4eed-a5f4-5895b269ba4b');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-24 20:34:37