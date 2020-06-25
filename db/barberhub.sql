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
INSERT INTO `BOOKING` VALUES (1,NULL,NULL,'2020-06-19','09:00:00',8,1),(2,0,'Prova a venire nel pomeriggio.','2020-06-19','15:30:00',9,1),(3,0,'No','2020-06-17','09:00:00',7,1),(4,0,'No','2020-06-17','17:30:00',10,1),(5,0,'No','2020-06-13','12:00:00',10,1),(6,1,'Non posso venire!','2020-06-18','09:00:00',10,1),(7,1,'Non posso venire!','2020-06-17','09:00:00',10,1),(8,1,'Non posso venire!','2020-06-14','09:00:00',10,1),(9,NULL,NULL,'2020-06-19','09:30:00',10,1),(10,1,'Non posso venire!','2020-06-20','09:00:00',7,1),(11,1,'Non posso venire!','2020-06-18','10:00:00',7,1),(12,1,'Non posso venire!','2020-06-18','16:30:00',7,1),(13,0,'Siamo chiusi','2020-06-18','16:30:00',7,1),(14,1,'Non posso venire!','2020-06-18','16:30:00',7,1),(15,1,'Non posso venire!','2020-06-19','10:00:00',7,1),(16,1,'Non posso venire!','2020-06-20','09:00:00',7,1),(17,1,'Non posso venire!','2020-06-20','12:00:00',7,1);
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
INSERT INTO `COUNTER` VALUES ('bookingId',17),('orderId',42),('productId',16),('structureId',1),('userId',11);
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
INSERT INTO `ITEMS_LIST` VALUES (1,2,3),(2,2,3),(2,4,4),(2,5,1),(3,9,4),(4,1,2),(5,2,5),(5,5,2),(5,9,3),(6,2,2),(6,4,1),(6,9,1),(7,1,3),(8,5,3),(9,1,2),(10,5,1),(11,5,2),(12,5,2),(13,14,4),(14,1,1),(14,4,2),(14,5,2),(14,8,1),(14,9,1),(15,5,2),(15,7,3),(16,5,1),(17,1,1),(17,9,4),(18,15,1),(19,5,1),(20,15,3),(21,15,1),(22,12,1),(23,1,1),(24,1,1),(25,2,1),(26,2,1),(27,2,1),(28,2,1),(29,2,1),(30,2,1),(31,5,1),(32,15,2),(33,6,4),(34,5,1),(35,5,1),(36,5,1),(37,5,1),(38,5,1),(39,5,1),(40,5,1),(41,15,1),(42,15,1);
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
INSERT INTO `ORDERS` VALUES (1,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-08',0,45.00,'Your order has been canceled.',10),(2,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-01-17',0,99.75,'Your order has been shipped...',10),(3,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,58.80,'Your order has been shipped...',10),(4,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,36.00,'Nothing new yet...',10),(5,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,144.60,'Your order is being processed...',10),(6,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-13','2020-06-09',0,55.20,'Your order has been delivered.',10),(7,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,54.00,'Nothing new yet...',10),(8,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,38.25,'Your order has been canceled.',10),(9,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-13','2020-06-09',0,36.00,'Your order has been delivered.',10),(10,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-09',0,12.75,'Nothing new yet...',10),(11,'ITALY|TOSCANA|POTENZA|1111111|21||',NULL,'2020-06-09',0,25.50,'Your order has been canceled.',11),(12,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-10',0,25.50,'Your order has been canceled.',10),(13,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-13','2020-06-12',0,70.84,'Your order has been delivered.',10),(14,'ITALY|EMILIA-ROMAGNA|FERRARA|84965|VIA EMILIA|75|','2020-06-13','2020-06-12',0,134.45,'Your order has been delivered.',8),(15,'ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|',NULL,'2020-06-12',0,64.17,'Nothing new yet...',9),(16,'ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|','2020-06-13','2020-06-12',0,12.75,'Your order has been delivered.',9),(17,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|','2020-06-13','2020-06-12',0,76.80,'Your order has been delivered.',7),(18,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|','2020-06-13','2020-06-13',0,20.65,'Your order has been shipped...',10),(19,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,12.75,'Nothing new yet...',10),(20,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,61.95,'Nothing new yet...',10),(21,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,20.65,'Nothing new yet...',10),(22,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,20.79,'Nothing new yet...',10),(23,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,18.00,'Nothing new yet...',10),(24,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,18.00,'Nothing new yet...',10),(25,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,15.00,'Nothing new yet...',10),(26,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,15.00,'Nothing new yet...',10),(27,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,15.00,'Nothing new yet...',10),(28,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,15.00,'Nothing new yet...',10),(29,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|',NULL,'2020-06-13',0,15.00,'Nothing new yet...',10),(30,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA||',NULL,'2020-06-13',0,15.00,'Nothing new yet...',10),(31,'ITALY|SARDEGNA|CAGLIARI|4555|VIA NON È CASA MIA||',NULL,'2020-06-13',0,12.75,'Nothing new yet...',10),(32,'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA||',NULL,'2020-06-14',0,41.30,'Nothing new yet...',10),(33,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|','2020-06-18','2020-06-18',0,41.76,'Your order is being delivered...',7),(34,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(35,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(36,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(37,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(38,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(39,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(40,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-19',0,12.75,'Your order has been canceled.',7),(41,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|','2020-06-19','2020-06-19',0,20.65,'Your order has been shipped...',7),(42,'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|',NULL,'2020-06-20',0,20.65,'Nothing new yet...',7);
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
INSERT INTO `PRODUCT` VALUES (1,'Garnier',20.00,10,'UltraDolce Estratto di Camomilla e Miele','2020-05-04','product_1.webp','Vera ricetta di lucentezza per i capelli chiari, questa gamma Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietà schiarenti e riflessanti, al Miele, apprezzato per le sue virtù nutritive, in formule delicate e leggere che non appesantiscono i tuoi capelli e li rendono morbidi e setosi. I capelli sono incredibilmente brillanti e morbidi al tatto.  Fine dei biondi spenti, i capelli scoprono riflessi natural\r\n\r\nVera ricetta di lucentezza per i capelli chiari, questa gamma Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietà schiarenti e riflessanti, al Miele, apprezzato per le sue virtù nutritive, in formule delicate e leggere che non appesantiscono i tuoi capelli e li rendono moribidi e setosi.\r\n\r\nI capelli sono incredibilmente brillanti e morbidi al tatto. \r\n\r\nFine dei biondi spenti, i capelli scoprono riflessi naturali e luminosi.\r\n\r\nLa dolcezza dal cuore delle piante Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietà schiarenti e ammorbidenti, al Miele, apprezzato per le sue virtù nutritive.\r\n\r\n\r\n                    ',10,'Shampoo',0,0,1),(2,'Garnier',15.00,0,'Fructis Antiforfora - Shampoo fortificante','2020-05-04','product_2.webp','Shampoo fortificante antiforfora per eliminare la forfora visibile ed ossigenare la cute. \r\nDedicato ai capelli con forfora, lo shampoo antiforfora Fructis Antiforfora Reoxygen Fructis combina l?acido salicilico e il piroctone olamine antibatterici con l?estratto di tea tree, noto per le sue proprietà purificanti. Elimina la forfora visibile* combattendo gli agenti che la causano.\r\n\r\nSUPERFRUTTI. SUPERCAPELLI. \r\nNuova formula senza parabeni, con attivi di frutti fortificanti. Un\'esclusiva combinazione dall\'efficacia provata di estratti derivati da frutti e piante, da vitamine B3 e B6 e con una proteina del limone**. Per capelli forti e dall\'aspetto sano. E\' dimostrato.\r\n\r\nCONSIGLI D\'USO\r\nApplicare sui capelli bagnati, massaggiare delicatamente e risciacquare. In caso di contatto con gli occhi, risciacquare immediatamente.\r\n\r\nRISULTATI\r\nLa cute è purificata e rinfrescata: torna a respirare! I capelli sono forti e brillanti.\r\n\r\nFRUCTIS S\'IMPEGNA PER L\'AMBIENTE\r\nFlaconi con il 25% di plastica riciclata.\r\nFlaconi 100% riciclabili se correttamente smaltiti.\r\nProdotti in uno stabilimento impegnato nello sviluppo sostenibile\r\n\r\n*Test di autovalutazione\r\n**derivato di proteina \r\n\r\nIngredienti chiaveOctopirox: un efficacissimo attivo antiforfora.  Attivo Antibatterico: concorre ad eliminare gli agenti responsabili della forfora, prevenendone la ricomparsa.  Olio Essenziale di Tea Tree: noto per le sue proprietà purificanti, è stato integrato nella formula per aiutare a liberare la cute dalle impurità donando una sensazione di freschezza duratura.\r\n                    ',5,'Shampoo',0,0,1),(3,'Ahava',12.35,80,'Ahava Time to Energize - Schiuma da Barba','2020-05-04','product_3.png','Olio di Argan: sensuale relax.\r\nFormulato con Olio di Argan di origine biologica, ricco di sostanze nutrienti e di ingredienti attivi (Vitamina E, Omega 3 ed Omega 6), per detergere e nutrire intensamente la pelle. Il profumo, caldo ed avvolgente, unisce note di testa agrumate ad un cuore deliziosamente speziato e lascia una persistente e sensuale scia legnosa: la fragranza ideale per un momento di puro relax.\r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    \r\n                    ',5,'Cream',1,0,1),(4,'Vidal',10.50,0,'ARGAN OIL Nutre & Rigenera - Olio di Argan','2020-04-15','product_4.webp','Olio di Argan: sensuale relax.\r\nFormulato con Olio di Argan di origine biologica, ricco di sostanze nutrienti e di ingredienti attivi (Vitamina E, Omega 3 ed Omega 6), per detergere e nutrire intensamente la pelle. Il profumo, caldo ed avvolgente, unisce note di testa agrumate ad un cuore deliziosamente speziato e lascia una persistente e sensuale scia legnosa: la fragranza ideale per un momento di puro relax.\r\n                    ',4,'bagnodoccia',0,0,1),(5,'Dear Barber',15.00,15,'Dear Barber Beard Shampoo','2020-05-08','product_5.png','It improves the condition of beard and hair, increasing its bulk.\r\nEnriched with an invigorating fragrance, this shampoo makes the beard and hair more manageable and shiny.\r\nOur antistatic formula produces an excellent calming effect.\r\n                    ',2,'Shampoo',0,0,1),(6,'Dear Beard',10.99,5,'Shaving Milk','2020-05-04','product_6.png','Fluid shaving cream. Ideal for partial or total shaving of the beard. Ideal for preparing the skin before shaving.\r\n                    ',7,'Lotions',1,0,1),(7,'Suavecito',12.89,0,'Shaving Cream ','2020-05-04','product_7.png','The natural peppermint opens your pores to get your razor in ultra close. It washes off painlessly with water and does not dry out your skin. Finish up with one of our healing and soothing aftershaves and walk out the door refreshed - ready to take on the day.\r\n                    ',6,'Cream',1,0,1),(8,'Rogaine',65.00,15,'Minoxidil','2020-05-04','product_8.png','Minoxidil solution and foam are used to help hair growth in the treatment of male pattern baldness. It is not used for baldness at the front of the scalp or receding hairline in men. The foam and 2 percent minoxidil solution is also used to help hair growth in women with thinning hair.\r\n\r\nMinoxidil belongs to a class of drugs known as vasodilators. It is not known how minoxidil causes hair growth. This medication is not used for sudden/patchy hair loss, unexplained hair loss (for example, if you have no family history of hair loss), or hair loss after giving birth.\r\n\r\nDo not use this product if you are 18 years old or younger.\r\n                    ',4,'Lotions',1,0,1),(9,'Spartan',15.00,2,'Growth oil','2020-05-04','product_9.png','Stimulates hair & beard growth.\r\nSpartan Man\'s natural growth oil is designed to stimulate faster hair growth, and create a fuller, thicker beard.\r\n                    \r\n                    ',6,'Oil',1,0,1),(11,'Kaly',12.10,10,'Reeg','2020-06-10','product_11','Super oil parfume for man and women .                    ',10,'Oil',1,1,1),(12,'Gilly',23.10,10,'Heeg','2020-06-10','product_12.jpg','Super oil.                    ',10,'Oil',1,0,1),(14,'A',23.00,23,'a','2020-06-10','product_14.jpg','A',45,'A',1,1,1),(15,'Mediterranea Cosmetics',22.95,10,'Mediterranea Man - Invisible Cream','2020-06-13','product_15.jpg','Crema Viso Man Invisible\r\nAzione Opacizzante e Antietà\r\nLa crema viso anti aging Man Invisible è un concentrato di minerali per rigenerare, idratare e proteggere la pelle maschile. Il colore caratteristico è determinato dalla presenza di Argilla nera, un minerale dal potere purificante e rigenerante. Non colora la pelle e non macchia. La texture é ultra leggera, si assorbe velocemente, regalando alla pelle un aspetto uniforme. Non contiene parabeni.\r\n\r\nRituale d\'uso\r\nApplicare quotidianamente il prodotto sul viso precedentemente deterso evitando la zona perioculare.',30,'Cream',1,0,1),(16,'a',2.00,0,'a','2020-06-22','product_16.png','a',1,'a',1,0,1);
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
  `ADDRESS` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
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
INSERT INTO `USER` VALUES (1,1,'mario.rossi@gmail.com','MARIO','ROSSI','ITALY|PIEMONTE|TORINO|48652|VIA DEGLI ARANCI|3|','3456719867','1','1973-12-12','RSSMRA73T12L219G','A',0,0),(3,1,'lucia.lusso@gmail.com','LUCIA','LUSSO','ITALY|BASILICATA|POTENZA|45683|VIA PRINCIPALE|10|','3569812456','1','1973-12-12','LCLSOO36721HR39A','E',0,0),(4,1,'RobbyVerdy@gmail.com','ROBERTO','VERDI','ITALY|BASILICATA|POTENZA|45638|VIA LUCANIA|23|','3334455998','1','1973-12-12','VRDRRT82A01G942G','E',0,1),(5,1,'lucianoluci@gmail.com','LUCIANO','LUCI','ITALY|VENETO|VENEZIA|45858|VIA COMACCHIO|9|','3484456489','1','1973-12-12','LGTLCN20E21L736L','E',0,0),(6,1,'lucianoluce@gmail.com','LUCIANO','LUCE','ITALY|VENETO|VENEZIA|45808|VIA LAGUNA|48|','3484456489','1','1973-12-12','LGTLCN20E21L737L','E',0,0),(7,NULL,'mm@gmail.com','Marcello','Marci','ITALY|LAZIO|ROMA|89655|VIA VENETO|79|','354887658','1',NULL,NULL,'C',0,0),(8,NULL,'marcella@marci.com','MARCELLA','MARCI','ITALY|EMILIA-ROMAGNA|FERRARA|84965|VIA EMILIA|75|','1257786548','1',NULL,NULL,'C',0,0),(9,NULL,'marcobello@gmail.com','MARCO','BELLO','ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|','2485568795','1',NULL,NULL,'C',0,0),(10,NULL,'lucaluce@gmail.com','Luca','Luce','ITALY|VENETO|VICENZA|12121|VIA BENEDETTA||','358448658','1',NULL,NULL,'C',0,0),(11,NULL,'mario.bianchi@gmail.com','ALESSIA','Zambro','ITALY|TOSCANA|POTENZA|1111111|21||','111111111','1',NULL,NULL,'C',0,0);
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
INSERT INTO `WISHLIST` VALUES (7,3),(7,6),(7,12),(9,5),(10,2),(10,5),(11,7);
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

-- Dump completed on 2020-06-24 14:43:38
