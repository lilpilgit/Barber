-- MySQL dump 10.13  Distrib 8.0.20, for Linux (x86_64)
--
-- Host: localhost    Database: barberhub
-- ------------------------------------------------------
-- Server version	8.0.20-0ubuntu0.20.04.1

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

--
-- Table structure for table `BOOKING`
--

DROP TABLE IF EXISTS `BOOKING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BOOKING` (
  `ID` bigint unsigned NOT NULL,
  `DELETED` tinyint(1) DEFAULT NULL,
  `DELETED_REASON` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `DATE` date NOT NULL,
  `HOUR_START` time NOT NULL,
  `ID_CUSTOMER` bigint unsigned NOT NULL,
  `ID_STRUCTURE` bigint unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
  KEY `ID_STRUCTURE` (`ID_STRUCTURE`),
  CONSTRAINT `BOOKING_ibfk_3` FOREIGN KEY (`ID_STRUCTURE`) REFERENCES `STRUCTURE` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `BOOKING_ibfk_4` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `USER` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BOOKING`
--

LOCK TABLES `BOOKING` WRITE;
/*!40000 ALTER TABLE `BOOKING` DISABLE KEYS */;
INSERT INTO `BOOKING` VALUES (1,NULL,NULL,'2020-06-19','09:00:00',8,1),(2,0,'We are on holiday','2020-06-19','15:30:00',9,1),(3,0,'We are on holiday','2020-06-17','09:00:00',7,1),(4,0,'We are on holiday','2020-06-17','17:30:00',10,1),(5,0,'We are on holiday','2020-06-13','12:00:00',10,1),(6,1,'I\'m busy!','2020-06-18','09:00:00',10,1),(7,1,'I\'m busy!','2020-06-17','09:00:00',10,1),(8,1,'I\'m busy!','2020-06-14','09:00:00',10,1),(9,NULL,NULL,'2020-06-19','09:30:00',10,1),(10,1,'I\'m busy!','2020-06-20','09:00:00',7,1),(11,1,'I\'m busy!','2020-06-18','10:00:00',7,1),(12,1,'I\'m busy!','2020-06-18','16:30:00',7,1),(13,0,'We don\'t work!','2020-06-18','16:30:00',7,1),(14,1,'I\'m busy!','2020-06-18','16:30:00',7,1),(15,1,'I\'m busy!','2020-06-19','10:00:00',7,1),(16,1,'I\'m busy!','2020-06-20','09:00:00',7,1),(17,1,'I\'m busy!','2020-06-20','12:00:00',7,1),(18,1,'We don\'t work','2020-06-26','09:00:00',7,1),(19,1,'We don\'t work','2020-06-26','12:30:00',7,1),(20,0,'We don\'t work','2020-06-27','15:30:00',7,1),(21,1,'We don\'t work','2020-06-26','09:00:00',7,1),(22,NULL,NULL,'2020-06-30','09:00:00',7,1),(23,NULL,NULL,'2020-06-30','16:00:00',10,1),(24,NULL,NULL,'2020-06-26','09:00:00',9,1),(25,NULL,NULL,'2020-06-26','14:30:00',8,1),(26,NULL,NULL,'2020-06-27','16:00:00',19,1),(27,NULL,NULL,'2020-06-30','12:00:00',23,1),(28,NULL,NULL,'2020-06-29','15:30:00',11,1);
/*!40000 ALTER TABLE `BOOKING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CART`
--

DROP TABLE IF EXISTS `CART`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CART` (
  `ID_CUSTOMER` bigint unsigned NOT NULL,
  `ID_PRODUCT` bigint unsigned NOT NULL,
  `DESIRED_QTY` int unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID_CUSTOMER`,`ID_PRODUCT`),
  KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
  KEY `ID_PRODUCT` (`ID_PRODUCT`),
  CONSTRAINT `CART_ibfk_1` FOREIGN KEY (`ID_PRODUCT`) REFERENCES `PRODUCT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `CART_ibfk_2` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `USER` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CART`
--

LOCK TABLES `CART` WRITE;
/*!40000 ALTER TABLE `CART` DISABLE KEYS */;
INSERT INTO `CART` VALUES (7,1,8),(7,2,1),(7,4,1),(10,2,1),(21,3,1),(21,4,1),(21,6,1),(21,15,1),(21,19,2),(22,2,1),(23,1,3),(23,2,5),(23,3,4),(23,4,2),(23,5,2),(23,6,6),(23,7,2),(23,8,2),(23,9,3),(23,12,2),(23,15,3),(23,19,3);
/*!40000 ALTER TABLE `CART` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COUNTER`
--

DROP TABLE IF EXISTS `COUNTER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COUNTER` (
  `ID` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `VALUE` int unsigned NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COUNTER`
--

LOCK TABLES `COUNTER` WRITE;
/*!40000 ALTER TABLE `COUNTER` DISABLE KEYS */;
INSERT INTO `COUNTER` VALUES ('bookingId',28),('orderId',56),('productId',19),('structureId',1),('userId',23);
/*!40000 ALTER TABLE `COUNTER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ITEMS_LIST`
--

DROP TABLE IF EXISTS `ITEMS_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ITEMS_LIST` (
  `ID_ORDER` bigint unsigned NOT NULL,
  `ID_PRODUCT` bigint unsigned NOT NULL,
  `QUANTITY` int unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID_ORDER`,`ID_PRODUCT`),
  KEY `ID_ORDER` (`ID_ORDER`),
  KEY `ID_PRODUCT` (`ID_PRODUCT`),
  CONSTRAINT `ITEMS_LIST_ibfk_1` FOREIGN KEY (`ID_ORDER`) REFERENCES `ORDERS` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ITEMS_LIST_ibfk_2` FOREIGN KEY (`ID_PRODUCT`) REFERENCES `PRODUCT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ITEMS_LIST`
--

LOCK TABLES `ITEMS_LIST` WRITE;
/*!40000 ALTER TABLE `ITEMS_LIST` DISABLE KEYS */;
INSERT INTO `ITEMS_LIST` VALUES (1,2,3),(2,2,3),(2,4,4),(2,5,1),(3,9,4),(4,1,2),(5,2,5),(5,5,2),(5,9,3),(6,2,2),(6,4,1),(6,9,1),(7,1,3),(8,5,3),(9,1,2),(10,5,1),(11,5,2),(12,5,2),(13,14,4),(14,1,1),(14,4,2),(14,5,2),(14,8,1),(14,9,1),(15,5,2),(15,7,3),(16,5,1),(17,1,1),(17,9,4),(18,15,1),(19,5,1),(20,15,3),(21,15,1),(22,12,1),(23,1,1),(24,1,1),(25,2,1),(26,2,1),(27,2,1),(28,2,1),(29,2,1),(30,2,1),(31,5,1),(32,15,2),(33,6,4),(34,5,1),(35,5,1),(36,5,1),(37,5,1),(38,5,1),(39,5,1),(40,5,1),(41,15,1),(42,15,1),(43,15,3),(44,3,5),(44,5,2),(45,3,5),(45,4,4),(46,1,5),(47,19,3),(48,2,4),(49,1,5),(50,3,4),(51,19,1),(52,1,2),(52,4,1),(53,12,1),(54,3,5),(55,3,1),(55,15,1),(56,1,1),(56,2,2),(56,3,2),(56,4,3),(56,5,1),(56,6,5),(56,7,1),(56,9,4),(56,12,5),(56,15,3),(56,19,1);
/*!40000 ALTER TABLE `ITEMS_LIST` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ORDERS`
--

DROP TABLE IF EXISTS `ORDERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ORDERS` (
  `ID` bigint unsigned NOT NULL,
  `SHIPPING_ADDR` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `SELL_DATE` date DEFAULT NULL,
  `ORDER_DATE` date NOT NULL,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `TOT_PRICE` decimal(10,2) NOT NULL,
  `STATUS` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ID_CUSTOMER` bigint unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
  CONSTRAINT `ORDERS_ibfk_1` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `USER` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ORDERS`
--

LOCK TABLES `ORDERS` WRITE;
/*!40000 ALTER TABLE `ORDERS` DISABLE KEYS */;
INSERT INTO `ORDERS` VALUES (1,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-08',0,45.00,'Your order has been canceled.',10),(2,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-01-17',0,99.75,'Your order has been shipped...',10),(3,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,58.80,'Your order has been shipped...',10),(4,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,36.00,'Nothing new yet...',10),(5,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,144.60,'Your order is being processed...',10),(6,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-13','2020-06-09',0,55.20,'Your order has been delivered.',10),(7,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,54.00,'Nothing new yet...',10),(8,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,38.25,'Your order has been canceled.',10),(9,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-13','2020-06-09',0,36.00,'Your order has been delivered.',10),(10,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,12.75,'Your order is being processed...',10),(11,'ITALY|TOSCANA|POTENZA|1111111|21||',NULL,'2020-06-09',0,25.50,'Your order has been canceled.',11),(12,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-10',0,25.50,'Your order has been canceled.',10),(13,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-13','2020-06-12',0,70.84,'Your order has been delivered.',10),(14,'ITALY|EMILIA-ROMAGNA|FERRARA|84965|VIA EMILIA|75|','2020-06-13','2020-06-12',0,134.45,'Your order has been delivered.',8),(15,'ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|','2020-06-25','2020-06-12',0,64.17,'Your order has been shipped...',9),(16,'ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|','2020-06-13','2020-06-12',0,12.75,'Your order has been delivered.',9),(17,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|','2020-06-13','2020-06-12',0,76.80,'Your order has been delivered.',7),(18,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-13','2020-06-13',0,20.65,'Your order has been shipped...',10),(19,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-25','2020-06-13',0,12.75,'Your order has been delivered.',10),(20,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-25','2020-06-13',0,61.95,'Your order is being delivered...',10),(21,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,20.65,'Your order is being processed...',10),(22,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-25','2020-06-13',0,20.79,'Your order has been shipped...',10),(23,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,18.00,'Nothing new yet...',10),(24,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-25','2020-06-13',0,18.00,'Your order is being delivered...',10),(25,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-25','2020-06-13',0,15.00,'Your order has been delivered.',10),(26,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-25','2020-06-13',0,15.00,'Your order has been shipped...',10),(27,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-25','2020-06-13',0,15.00,'Your order has been canceled.',10),(28,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-25','2020-06-13',0,15.00,'Your order is being delivered...',10),(29,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-25','2020-06-13',0,15.00,'Your order has been delivered.',10),(30,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA||','2020-06-25','2020-06-13',0,15.00,'Your order is being delivered...',10),(31,'ITALY|SARDEGNA|CAGLIARI|4555|VIA NON È CASA MIA||','2020-06-25','2020-06-13',0,12.75,'Your order has been canceled.',10),(32,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA||','2020-06-25','2020-06-14',0,41.30,'Your order has been delivered.',10),(33,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|','2020-06-25','2020-06-18',0,41.76,'Your order has been canceled.',7),(34,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(35,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(36,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(37,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(38,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(39,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(40,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(41,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|','2020-06-19','2020-06-19',0,20.65,'Your order has been shipped...',7),(42,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-20',0,20.65,'Nothing new yet...',7),(43,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-25',0,61.95,'Nothing new yet...',7),(44,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA||',NULL,'2020-06-25',0,37.85,'Nothing new yet...',10),(45,'ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|',NULL,'2020-06-25',0,54.35,'Nothing new yet...',9),(46,'ITALY|EMILIA-ROMAGNA|FERRARA|84965|VIA EMILIA|75|',NULL,'2020-06-25',0,90.00,'Nothing new yet...',8),(47,'ITALY|EMILIA-ROMAGNA|FERRARA|84965|VIA EMILIA|75|',NULL,'2020-06-25',0,26.97,'Nothing new yet...',8),(48,'ITALY|VENETO|PADOVA|48645|VIA EUROPA|1|',NULL,'2020-06-25',0,60.00,'Your order is being processed...',19),(49,'ITALY|VENETO|PADOVA|48645|VIA EUROPA|1|','2020-06-25','2020-06-25',0,90.00,'Your order has been shipped...',19),(50,'ITALY|VENETO|PADOVA|48645|VIA EUROPA|1|','2020-06-25','2020-06-25',0,9.88,'Your order is being delivered...',19),(51,'ITALY|BASILICATA|POTENZA|4684|VIA EUROPA|8|','2020-06-25','2020-06-25',0,8.99,'Your order has been delivered.',19),(52,'ITALY|VENETO|PADOVA|648684|VIA EUROPA|46|','2020-06-25','2020-06-25',0,46.50,'Your order has been canceled.',21),(53,'ITALY|VENETO|PADOVA|648684|VIA EUROPA|46|','2020-06-25','2020-06-25',0,20.79,'Your order has been shipped...',21),(54,'ITALY|VENETO|PADOVA|648684|VIA EUROPA|46|','2020-06-25','2020-06-25',0,12.35,'Your order is being delivered...',21),(55,'ITALY|VENETO|PADOVA|648684|VIA EUROPA|46|','2020-06-25','2020-06-25',0,23.12,'Your order has been delivered.',21),(56,'ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|','2020-06-25','2020-06-25',0,395.97,'Your order has been delivered.',9);
/*!40000 ALTER TABLE `ORDERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRODUCT`
--

DROP TABLE IF EXISTS `PRODUCT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRODUCT` (
  `ID` bigint unsigned NOT NULL,
  `PRODUCER` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `PRICE` decimal(10,2) unsigned NOT NULL,
  `DISCOUNT` int unsigned DEFAULT NULL,
  `NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `INSERT_DATE` date NOT NULL,
  `PIC_NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DESCRIPTION` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `MAX_ORDER_QTY` int unsigned NOT NULL,
  `CATEGORY` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `SHOWCASE` tinyint(1) NOT NULL,
  `DELETED` tinyint NOT NULL,
  `ID_STRUCTURE` bigint unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `ID_STRUCTURE` (`ID_STRUCTURE`),
  CONSTRAINT `PRODUCT_ibfk_1` FOREIGN KEY (`ID_STRUCTURE`) REFERENCES `STRUCTURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRODUCT`
--

LOCK TABLES `PRODUCT` WRITE;
/*!40000 ALTER TABLE `PRODUCT` DISABLE KEYS */;
INSERT INTO `PRODUCT` VALUES (1,'Garnier',20.00,10,'UltraDolce Estratto di Camomilla e Miele','2020-05-04','product_1.webp','Vera ricetta di lucentezza per i capelli chiari, questa gamma Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietÃ  schiarenti e riflessanti, al Miele, apprezzato per le sue virtÃ¹ nutritive, in formule delicate e leggere che non appesantiscono i tuoi capelli e li rendono morbidi e setosi. I capelli sono incredibilmente brillanti e morbidi al tatto.  Fine dei biondi spenti, i capelli scoprono riflessi natural\r\n\r\nVera ricetta di lucentezza per i capelli chiari, questa gamma Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietÃ  schiarenti e riflessanti, al Miele, apprezzato per le sue virtÃ¹ nutritive, in formule delicate e leggere che non appesantiscono i tuoi capelli e li rendono moribidi e setosi.\r\n\r\nI capelli sono incredibilmente brillanti e morbidi al tatto. \r\n\r\nFine dei biondi spenti, i capelli scoprono riflessi naturali e luminosi.\r\n\r\nLa dolcezza dal cuore delle piante Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietÃ  schiarenti e ammorbidenti, al Miele, apprezzato per le sue virtÃ¹ nutritive.\r\n\r\n\r\n                    ',10,'Shampoo',1,0,1),(2,'Garnier',15.00,0,'Fructis Antiforfora - Shampoo fortificante','2020-05-04','product_2.webp','Shampoo fortificante antiforfora per eliminare la forfora visibile ed ossigenare la cute. \r\nDedicato ai capelli con forfora, lo shampoo antiforfora Fructis Antiforfora Reoxygen Fructis combina l?acido salicilico e il piroctone olamine antibatterici con l?estratto di tea tree, noto per le sue proprietÃ  purificanti. Elimina la forfora visibile* combattendo gli agenti che la causano.\r\n\r\nSUPERFRUTTI. SUPERCAPELLI. \r\nNuova formula senza parabeni, con attivi di frutti fortificanti. Un\'esclusiva combinazione dall\'efficacia provata di estratti derivati da frutti e piante, da vitamine B3 e B6 e con una proteina del limone**. Per capelli forti e dall\'aspetto sano. E\' dimostrato.\r\n\r\nCONSIGLI D\'USO\r\nApplicare sui capelli bagnati, massaggiare delicatamente e risciacquare. In caso di contatto con gli occhi, risciacquare immediatamente.\r\n\r\nRISULTATI\r\nLa cute Ã¨ purificata e rinfrescata: torna a respirare! I capelli sono forti e brillanti.\r\n\r\nFRUCTIS S\'IMPEGNA PER L\'AMBIENTE\r\nFlaconi con il 25% di plastica riciclata.\r\nFlaconi 100% riciclabili se correttamente smaltiti.\r\nProdotti in uno stabilimento impegnato nello sviluppo sostenibile\r\n\r\n*Test di autovalutazione\r\n**derivato di proteina \r\n\r\nIngredienti chiaveOctopirox: un efficacissimo attivo antiforfora.  Attivo Antibatterico: concorre ad eliminare gli agenti responsabili della forfora, prevenendone la ricomparsa.  Olio Essenziale di Tea Tree: noto per le sue proprietÃ  purificanti, Ã¨ stato integrato nella formula per aiutare a liberare la cute dalle impuritÃ  donando una sensazione di freschezza duratura.\r\n                    ',5,'Shampoo',1,0,1),(3,'Ahava',12.35,80,'Ahava Time to Energize - Schiuma da Barba','2020-05-04','product_3.png','Olio di Argan: sensuale relax.\r\nFormulato con Olio di Argan di origine biologica, ricco di sostanze nutrienti e di ingredienti attivi (Vitamina E, Omega 3 ed Omega 6), per detergere e nutrire intensamente la pelle. Il profumo, caldo ed avvolgente, unisce note di testa agrumate ad un cuore deliziosamente speziato e lascia una persistente e sensuale scia legnosa: la fragranza ideale per un momento di puro relax.\r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    ',5,'Cream',1,0,1),(4,'Vidal',10.50,0,'ARGAN OIL Nutre & Rigenera - Olio di Argan','2020-04-15','product_4.webp','Olio di Argan: sensuale relax.\r\nFormulato con Olio di Argan di origine biologica, ricco di sostanze nutrienti e di ingredienti attivi (Vitamina E, Omega 3 ed Omega 6), per detergere e nutrire intensamente la pelle. Il profumo, caldo ed avvolgente, unisce note di testa agrumate ad un cuore deliziosamente speziato e lascia una persistente e sensuale scia legnosa: la fragranza ideale per un momento di puro relax.\r\n                    ',4,'bagnodoccia',1,0,1),(5,'Dear Barber',15.00,15,'Dear Barber Beard Shampoo','2020-05-08','product_5.png','It improves the condition of beard and hair, increasing its bulk.\r\nEnriched with an invigorating fragrance, this shampoo makes the beard and hair more manageable and shiny.\r\nOur antistatic formula produces an excellent calming effect.\r\n                    ',2,'Shampoo',1,0,1),(6,'Dear Beard',10.99,5,'Shaving Milk','2020-05-04','product_6.png','Fluid shaving cream. Ideal for partial or total shaving of the beard. Ideal for preparing the skin before shaving.\r\n                    ',7,'Lotions',1,0,1),(7,'Suavecito',12.89,0,'Shaving Cream ','2020-05-04','product_7.png','The natural peppermint opens your pores to get your razor in ultra close. It washes off painlessly with water and does not dry out your skin. Finish up with one of our healing and soothing aftershaves and walk out the door refreshed - ready to take on the day.\r\n                    ',6,'Cream',1,0,1),(8,'Rogaine',65.00,15,'Minoxidil','2020-05-04','product_8.png','Minoxidil solution and foam are used to help hair growth in the treatment of male pattern baldness. It is not used for baldness at the front of the scalp or receding hairline in men. The foam and 2 percent minoxidil solution is also used to help hair growth in women with thinning hair.\r\n\r\nMinoxidil belongs to a class of drugs known as vasodilators. It is not known how minoxidil causes hair growth. This medication is not used for sudden/patchy hair loss, unexplained hair loss (for example, if you have no family history of hair loss), or hair loss after giving birth.\r\n\r\nDo not use this product if you are 18 years old or younger.\r\n                    ',4,'Lotions',1,0,1),(9,'Spartan',15.00,2,'Growth oil','2020-05-04','product_9.png','Stimulates hair & beard growth.\r\nSpartan Man\'s natural growth oil is designed to stimulate faster hair growth, and create a fuller, thicker beard.\r\n                    \r\n                    ',6,'Oil',1,0,1),(11,'Kaly',12.10,10,'Reeg','2020-06-10','product_11.jpg','Super oil parfume for man and women .                    ',10,'Oil',1,1,1),(12,'Gilly',23.10,10,'Heeg','2020-06-10','product_12.jpg','Super oil.                    ',10,'Oil',1,0,1),(14,'test',23.00,23,'test','2020-06-10','product_14.jpg','Undefined',45,'A',1,1,1),(15,'Mediterranea',22.95,10,'Mediterranea Man - Invisible Cream','2020-06-13','product_15.jpg','Crema Viso Man Invisible\r\nAzione Opacizzante e Antietà\r\nLa crema viso anti aging Man Invisible è un concentrato di minerali per rigenerare, idratare e proteggere la pelle maschile. Il colore caratteristico è determinato dalla presenza di Argilla nera, un minerale dal potere purificante e rigenerante. Non colora la pelle e non macchia. La texture é ultra leggera, si assorbe velocemente, regalando alla pelle un aspetto uniforme. Non contiene parabeni.\r\n\r\nRituale d\'uso\r\nApplicare quotidianamente il prodotto sul viso precedentemente deterso evitando la zona perioculare.',30,'Cream',1,0,1),(16,'Garnier',2.00,0,'Avahaha','2020-06-22','product_16.png','Description',1,'as',0,1,1),(17,'test',1.00,10,'test','2020-06-25','product_17.png','Undefined',1,'d',0,1,1),(18,'test',1.00,0,'test','2020-06-25','product_18.png','Undefined',1,'a',0,1,1),(19,'Dovo',9.99,10,'Shavette','2020-06-25','product_19.png','Shaving Factory SF103 free hand shaver\r\nThe Shaving Factory free-standing razor SF103 is a free-hand razor with replaceable shaving razor blades.\r\nThe Shaving Factory SF103 free-hand razor has a total weight of 38 grams',3,'Razor',1,0,1);
/*!40000 ALTER TABLE `PRODUCT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STRUCTURE`
--

DROP TABLE IF EXISTS `STRUCTURE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STRUCTURE` (
  `ID` bigint unsigned NOT NULL,
  `ADDRESS` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `NAME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `OPENING_TIME` time NOT NULL,
  `CLOSING_TIME` time NOT NULL,
  `SLOT` time NOT NULL,
  `PHONE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STRUCTURE`
--

LOCK TABLES `STRUCTURE` WRITE;
/*!40000 ALTER TABLE `STRUCTURE` DISABLE KEYS */;
INSERT INTO `STRUCTURE` VALUES (1,'ITALY|LOMBARDIA|TORINO|48659|VIA DEGLI ARANCI|3|','BarberHub','09:00:00','18:00:00','00:30:00','1598468498');
/*!40000 ALTER TABLE `STRUCTURE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER` (
  `ID` bigint unsigned NOT NULL,
  `ID_STRUCTURE` bigint unsigned DEFAULT NULL,
  `EMAIL` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `NAME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `SURNAME` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ADDRESS` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `PHONE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `PASSWORD` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `BIRTH_DATE` date DEFAULT NULL,
  `FISCAL_CODE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TYPE` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `BLOCKED` tinyint(1) NOT NULL DEFAULT '0',
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `ID_STRUCTURE` (`ID_STRUCTURE`),
  CONSTRAINT `USER_ibfk_1` FOREIGN KEY (`ID_STRUCTURE`) REFERENCES `STRUCTURE` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER`
--

LOCK TABLES `USER` WRITE;
/*!40000 ALTER TABLE `USER` DISABLE KEYS */;
INSERT INTO `USER` VALUES (1,1,'admin@gmail.com','Mario','Rossi','ITALY|PIEMONTE|TORINO|48652|VIA DEGLI ARANCI|3|','3456719867','1','1973-12-12','RSSMRA73T12L219G','A',0,0),(3,1,'lucia.lusso@gmail.com','Lucia','Lusso','ITALY|BASILICATA|POTENZA|45683|VIA PRINCIPALE|10|','3569812456','1','1973-12-12','LCLSOO36721HR39A','E',0,0),(4,1,'RobbyVerdy@gmail.com','Roberto','Verdi','ITALY|BASILICATA|POTENZA|45638|VIA LUCANIA|23|','3334455998','1','1973-12-12','VRDRRT82A01G942G','E',0,1),(5,1,'lucianoluci@gmail.com','Luciano','Luci','ITALY|VENETO|VENEZIA|45858|VIA COMACCHIO|9|','3484456489','1','1973-12-12','LGTLCN20E21L736L','E',0,0),(6,1,'lucianoluce@gmail.com','Luciano','Luce','ITALY|VENETO|VENEZIA|45808|VIA LAGUNA|48|','3484456489','1','1973-12-12','LGTLCN20E21L737L','E',0,0),(7,NULL,'mm@gmail.com','Marcello','Marci','ITALY|LAZIO|ROMA|89655|VIA VENETO|79|','354887658','1',NULL,NULL,'C',0,0),(8,NULL,'marcella@marci.com','Marcella','Marci','ITALY|EMILIA-ROMAGNA|FERRARA|84965|VIA EMILIA|75|','1257786548','1',NULL,NULL,'C',0,0),(9,NULL,'marcobello@gmail.com','Marco','Bello','ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|','2485568795','1',NULL,NULL,'C',0,0),(10,NULL,'lucaluce@gmail.com','Luca','Luce','ITALY|VENETO|VICENZA|12121|VIA BENEDETTA||','358448658','1',NULL,NULL,'C',0,0),(11,NULL,'mario.bianchi@gmail.com','Mario','Bianchi','ITALY|TOSCANA|POTENZA|11318|21|9|','318166846','1',NULL,NULL,'C',0,0),(12,1,'albertobianchi@gmail.com','Alberto','Bianchi','ITALY|PIEMONTE|TORINO|45915|VIA LIBERA|5|','333444555','1','1975-06-20','VRDRRT82A01G942G','E',0,0),(13,1,'giorgialeonardi@student.unife.it','Giorgia','Leonardi','ITALY|LAZIO|ROMA|84654|VIA VENETO|88|','5489856365','1','1999-02-02','VRDRRT82A01G942G','E',0,0),(14,1,'valentinarossi@gmail.com','Valentina','Rossi','ITALY|BASILICATA|POTENZA|49846|VIA ALTA|88|','548665899','1','1982-07-15','VRDRRT82A01G942G','E',0,0),(15,1,'alessandroamato@gmail.com','Alessandro','Amato','ITALY|VENETO|VICENZA|546846|VIA EUROPA|35|','3584489658','1','1975-02-02','VRDRRT82A01G942G','E',0,0),(16,1,'cirogrieco@gmail.com','Ciro','Grieco','ITALY|PUGLIA|MANFREDONIA|86518|VIA LUNGOMARE|12|','8965831583','1','1998-05-05','VRDRRT82A01G942G','E',0,0),(17,1,'rossanogiacobbe@gmail.com','Rossano','Giacobbe','ITALY|VENETO|VERONA|48618|VIA FRANCA|863|','786846894','1','1993-03-17','VRDRRT82A01G942G','E',0,0),(18,1,'yuriminella@gmail.com','Yuri','Minella','ITALY|FRIULI-VENEZIA-GIULIA|UDINE|12344|VIA STELLATA|47|','7689654684','1','1984-06-02','VRDRRT82A01G942G','E',0,0),(19,NULL,'martinamerighi@gmail.com','Martina','Merighi','ITALY|VENETO|PADOVA|48645|VIA EUROPA|1|','3584489658','1',NULL,NULL,'C',0,0),(20,NULL,'martinorossi@gmail.com','Martino','Rossi','ITALY|VENETO|VERONA|348384|VIA EUROPA|123|','168469996','1',NULL,NULL,'C',0,0),(21,NULL,'valentinorossi@gmail.com','Valentino','Rossi','ITALY|VENETO|PADOVA|648684|VIA EUROPA|46|','13541868489','1',NULL,NULL,'C',0,0),(22,NULL,'alessiorossi@gmail.com','Alessio','Rossi','ITALY|VENETO|PADOVA|12445|VIA VENETO|51|','464468438','1',NULL,NULL,'C',0,0),(23,NULL,'utentesicuro@gmail.com','Utente','Sicuro','ITALY|EMILIA-ROMAGNA|RIMINI|31833|VIA LUNGOMARE|963|','6464351866','1',NULL,NULL,'C',0,0);
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WISHLIST`
--

DROP TABLE IF EXISTS `WISHLIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WISHLIST` (
  `ID_CUSTOMER` bigint unsigned NOT NULL,
  `ID_PRODUCT` bigint unsigned NOT NULL,
  PRIMARY KEY (`ID_CUSTOMER`,`ID_PRODUCT`),
  KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
  KEY `ID_PRODUCT` (`ID_PRODUCT`),
  CONSTRAINT `WISHLIST_ibfk_1` FOREIGN KEY (`ID_PRODUCT`) REFERENCES `PRODUCT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `WISHLIST_ibfk_2` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `USER` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WISHLIST`
--

LOCK TABLES `WISHLIST` WRITE;
/*!40000 ALTER TABLE `WISHLIST` DISABLE KEYS */;
INSERT INTO `WISHLIST` VALUES (7,4),(7,12),(8,19),(9,1),(9,2),(9,3),(9,4),(9,5),(9,6),(9,7),(9,8),(9,9),(9,12),(9,15),(10,2),(10,3),(10,5),(11,7),(19,2),(19,3),(19,19),(21,3),(22,3),(22,15),(22,19),(23,1),(23,2),(23,3),(23,4),(23,5),(23,6),(23,7),(23,8),(23,9),(23,12),(23,15),(23,19);
/*!40000 ALTER TABLE `WISHLIST` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-26 13:59:34
