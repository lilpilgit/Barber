-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Creato il: Giu 09, 2020 alle 12:56
-- Versione del server: 5.7.29-0ubuntu0.18.04.1
-- Versione PHP: 7.2.24-0ubuntu0.18.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `barberhub`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `BOOKING`
--

CREATE TABLE `BOOKING` (
  `ID` bigint(20) UNSIGNED NOT NULL,
  `DELETED` tinyint(1) NOT NULL,
  `DELETED_REASON` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `DATE` date DEFAULT NULL,
  `HOUR_START` time NOT NULL,
  `ID_CUSTOMER` bigint(20) UNSIGNED NOT NULL,
  `ID_STRUCTURE` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `CART`
--

CREATE TABLE `CART` (
  `ID_CUSTOMER` bigint(20) UNSIGNED NOT NULL,
  `ID_PRODUCT` bigint(20) UNSIGNED NOT NULL,
  `DESIRED_QTY` int(10) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `CART`
--

INSERT INTO `CART` (`ID_CUSTOMER`, `ID_PRODUCT`, `DESIRED_QTY`) VALUES
(8, 1, 1),
(8, 4, 2),
(8, 5, 2),
(8, 8, 1),
(8, 9, 1),
(9, 5, 2),
(9, 7, 3),
(11, 5, 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `COUNTER`
--

CREATE TABLE `COUNTER` (
  `ID` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `VALUE` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `COUNTER`
--

INSERT INTO `COUNTER` (`ID`, `VALUE`) VALUES
('bookingId', 0),
('orderId', 10),
('productId', 10),
('structureId', 1),
('userId', 11);

-- --------------------------------------------------------

--
-- Struttura della tabella `ITEMS_LIST`
--

CREATE TABLE `ITEMS_LIST` (
  `ID_PRODUCT` bigint(20) UNSIGNED NOT NULL,
  `ID_ORDER` bigint(20) UNSIGNED NOT NULL,
  `QUANTITY` int(10) UNSIGNED NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `ITEMS_LIST`
--

INSERT INTO `ITEMS_LIST` (`ID_PRODUCT`, `ID_ORDER`, `QUANTITY`) VALUES
(1, 4, 2),
(1, 7, 3),
(1, 9, 2),
(2, 1, 3),
(2, 2, 3),
(2, 5, 5),
(2, 6, 2),
(4, 2, 4),
(4, 6, 1),
(5, 2, 1),
(5, 5, 2),
(5, 8, 3),
(5, 10, 1),
(9, 3, 4),
(9, 5, 3),
(9, 6, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `ORDERS`
--

CREATE TABLE `ORDERS` (
  `ID` bigint(20) UNSIGNED NOT NULL,
  `SELL_DATE` date DEFAULT NULL,
  `ORDER_DATE` date NOT NULL,
  `STATUS` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `TOT_PRICE` decimal(10,2) NOT NULL,
  `SHIPPING_ADDR` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `ID_CUSTOMER` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `ORDERS`
--

INSERT INTO `ORDERS` (`ID`, `SELL_DATE`, `ORDER_DATE`, `STATUS`, `TOT_PRICE`, `SHIPPING_ADDR`, `DELETED`, `ID_CUSTOMER`) VALUES
(1, NULL, '2020-06-08', 'Your order has been canceled.', '45.00', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10),
(2, NULL, '2020-01-17', 'Your order has been shipped...', '99.75', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10),
(3, NULL, '2020-06-09', 'Your order has been shipped...', '58.80', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10),
(4, NULL, '2020-06-09', 'Nothing new yet...', '36.00', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10),
(5, NULL, '2020-06-09', 'Your order is being processed...', '144.60', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10),
(6, NULL, '2020-06-09', 'Your order has been delivered.', '55.20', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10),
(7, NULL, '2020-06-09', 'Nothing new yet...', '54.00', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10),
(8, NULL, '2020-06-09', 'Nothing new yet...', '38.25', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10),
(9, NULL, '2020-06-09', 'Nothing new yet...', '36.00', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10),
(10, NULL, '2020-06-09', 'Nothing new yet...', '12.75', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', 0, 10);

-- --------------------------------------------------------

--
-- Struttura della tabella `PRODUCT`
--

CREATE TABLE `PRODUCT` (
  `ID` bigint(20) UNSIGNED NOT NULL,
  `PRODUCER` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PRICE` decimal(10,2) UNSIGNED NOT NULL,
  `DISCOUNT` int(10) UNSIGNED DEFAULT NULL,
  `NAME` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `INSERT_DATE` date NOT NULL,
  `PIC_NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DESCRIPTION` text COLLATE utf8mb4_unicode_ci,
  `MAX_ORDER_QTY` int(10) UNSIGNED NOT NULL,
  `CATEGORY` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SHOWCASE` tinyint(1) NOT NULL,
  `DELETED` tinyint(4) NOT NULL,
  `ID_STRUCTURE` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `PRODUCT`
--

INSERT INTO `PRODUCT` (`ID`, `PRODUCER`, `PRICE`, `DISCOUNT`, `NAME`, `INSERT_DATE`, `PIC_NAME`, `DESCRIPTION`, `MAX_ORDER_QTY`, `CATEGORY`, `SHOWCASE`, `DELETED`, `ID_STRUCTURE`) VALUES
(1, 'Garnier', '20.00', 10, 'UltraDolce ? Estratto di Camomilla e Miele', '2020-05-04', 'product1.webp', 'Vera ricetta di lucentezza per i capelli chiari, questa gamma Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietà schiarenti e riflessanti, al Miele, apprezzato per le sue virtù nutritive, in formule delicate e leggere che non appesantiscono i tuoi capelli e li rendono morbidi e setosi. I capelli sono incredibilmente brillanti e morbidi al tatto.  Fine dei biondi spenti, i capelli scoprono riflessi natural\r\n\r\nVera ricetta di lucentezza per i capelli chiari, questa gamma Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietà schiarenti e riflessanti, al Miele, apprezzato per le sue virtù nutritive, in formule delicate e leggere che non appesantiscono i tuoi capelli e li rendono moribidi e setosi.\r\n\r\nI capelli sono incredibilmente brillanti e morbidi al tatto. \r\n\r\nFine dei biondi spenti, i capelli scoprono riflessi naturali e luminosi.\r\n\r\nLa dolcezza dal cuore delle piante Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietà schiarenti e ammorbidenti, al Miele, apprezzato per le sue virtù nutritive.\r\n\r\n\r\n                    ', 10, 'Shampoo', 1, 0, 1),
(2, 'Garnier', '15.00', 0, 'Fructis Antiforfora - Shampoo fortificante', '2020-05-04', 'product2.webp', 'Shampoo fortificante antiforfora per eliminare la forfora visibile ed ossigenare la cute. \r\nDedicato ai capelli con forfora, lo shampoo antiforfora Fructis Antiforfora Reoxygen Fructis combina l?acido salicilico e il piroctone olamine antibatterici con l?estratto di tea tree, noto per le sue proprietà purificanti. Elimina la forfora visibile* combattendo gli agenti che la causano.\r\n\r\nSUPERFRUTTI. SUPERCAPELLI. \r\nNuova formula senza parabeni, con attivi di frutti fortificanti. Un\'esclusiva combinazione dall\'efficacia provata di estratti derivati da frutti e piante, da vitamine B3 e B6 e con una proteina del limone**. Per capelli forti e dall\'aspetto sano. E\' dimostrato.\r\n\r\nCONSIGLI D\'USO\r\nApplicare sui capelli bagnati, massaggiare delicatamente e risciacquare. In caso di contatto con gli occhi, risciacquare immediatamente.\r\n\r\nRISULTATI\r\nLa cute è purificata e rinfrescata: torna a respirare! I capelli sono forti e brillanti.\r\n\r\nFRUCTIS S\'IMPEGNA PER L\'AMBIENTE\r\nFlaconi con il 25% di plastica riciclata.\r\nFlaconi 100% riciclabili se correttamente smaltiti.\r\nProdotti in uno stabilimento impegnato nello sviluppo sostenibile\r\n\r\n*Test di autovalutazione\r\n**derivato di proteina \r\n\r\nIngredienti chiaveOctopirox: un efficacissimo attivo antiforfora.  Attivo Antibatterico: concorre ad eliminare gli agenti responsabili della forfora, prevenendone la ricomparsa.  Olio Essenziale di Tea Tree: noto per le sue proprietà purificanti, è stato integrato nella formula per aiutare a liberare la cute dalle impurità donando una sensazione di freschezza duratura.\r\n                    ', 5, 'Shampoo', 1, 0, 1),
(3, 'Ahava', '20.35', 80, 'Ahava Time to Energize - Schiuma da Barba', '2020-05-04', 'product3.png', 'Olio di Argan: sensuale relax.\r\nFormulato con Olio di Argan di origine biologica, ricco di sostanze nutrienti e di ingredienti attivi (Vitamina E, Omega 3 ed Omega 6), per detergere e nutrire intensamente la pelle. Il profumo, caldo ed avvolgente, unisce note di testa agrumate ad un cuore deliziosamente speziato e lascia una persistente e sensuale scia legnosa: la fragranza ideale per un momento di puro relax.\r\n                    \r\n                    \r\n                    ', 5, 'Cream', 1, 1, 1),
(4, 'Vidal', '10.50', 0, 'ARGAN OIL Nutre & Rigenera - Olio di Argan', '2020-04-15', 'product4.webp', 'Olio di Argan: sensuale relax.\r\nFormulato con Olio di Argan di origine biologica, ricco di sostanze nutrienti e di ingredienti attivi (Vitamina E, Omega 3 ed Omega 6), per detergere e nutrire intensamente la pelle. Il profumo, caldo ed avvolgente, unisce note di testa agrumate ad un cuore deliziosamente speziato e lascia una persistente e sensuale scia legnosa: la fragranza ideale per un momento di puro relax.\r\n                    ', 4, 'bagnodoccia', 1, 0, 1),
(5, 'Dear Barber', '15.00', 15, 'Dear Barber Beard Shampoo', '2020-05-08', 'product5.png', 'It improves the condition of beard and hair, increasing its bulk.\r\nEnriched with an invigorating fragrance, this shampoo makes the beard and hair more manageable and shiny.\r\nOur antistatic formula produces an excellent calming effect.\r\n                    ', 2, 'Shampoo', 1, 0, 1),
(6, 'Dear Beard', '10.99', 5, 'Shaving Milk', '2020-05-04', 'product6.png', 'Fluid shaving cream. Ideal for partial or total shaving of the beard. Ideal for preparing the skin before shaving.\r\n                    ', 7, 'Lotions', 1, 0, 1),
(7, 'Suavecito', '12.89', 0, 'Shaving Cream ', '2020-05-04', 'product7.png', 'The natural peppermint opens your pores to get your razor in ultra close. It washes off painlessly with water and does not dry out your skin. Finish up with one of our healing and soothing aftershaves and walk out the door refreshed - ready to take on the day.\r\n                    ', 6, 'Cream', 1, 0, 1),
(8, 'Rogaine', '65.00', 15, 'Minoxidil', '2020-05-04', 'product8.png', 'Minoxidil solution and foam are used to help hair growth in the treatment of male pattern baldness. It is not used for baldness at the front of the scalp or receding hairline in men. The foam and 2 percent minoxidil solution is also used to help hair growth in women with thinning hair.\r\n\r\nMinoxidil belongs to a class of drugs known as vasodilators. It is not known how minoxidil causes hair growth. This medication is not used for sudden/patchy hair loss, unexplained hair loss (for example, if you have no family history of hair loss), or hair loss after giving birth.\r\n\r\nDo not use this product if you are 18 years old or younger.\r\n                    ', 4, 'Lotions', 1, 0, 1),
(9, 'Spartan', '15.00', 2, 'Growth oil', '2020-05-04', 'product9.png', 'Stimulates hair & beard growth.\r\nSpartan Man\'s natural growth oil is designed to stimulate faster hair growth, and create a fuller, thicker beard.\r\n                    \r\n                    ', 6, 'Oil', 1, 0, 1),
(10, 'Gaifa', '10.00', 10, 'SUper crema gaifa', '2020-06-03', 'pictureciao.png', 'SUper crema faiga per occasioni faighe. Daje enjoyla                    \r\n                    ', 3, 'Cream', 1, 0, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `STRUCTURE`
--

CREATE TABLE `STRUCTURE` (
  `ID` bigint(20) UNSIGNED NOT NULL,
  `ADDRESS` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `OPENING_TIME` time NOT NULL,
  `CLOSING_TIME` time NOT NULL,
  `SLOT` time NOT NULL,
  `NAME` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PHONE` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `STRUCTURE`
--

INSERT INTO `STRUCTURE` (`ID`, `ADDRESS`, `OPENING_TIME`, `CLOSING_TIME`, `SLOT`, `NAME`, `PHONE`) VALUES
(1, 'ITALY|LOMBARDIA|TORINO|48659|VIA DEGLI ARANCI|3|', '09:00:00', '18:00:00', '00:30:00', 'BarberHub', '1598468498');

-- --------------------------------------------------------

--
-- Struttura della tabella `USER`
--

CREATE TABLE `USER` (
  `ID` bigint(20) UNSIGNED NOT NULL,
  `ID_STRUCTURE` bigint(20) UNSIGNED DEFAULT NULL,
  `EMAIL` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `NAME` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SURNAME` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ADDRESS` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PHONE` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PASSWORD` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `BIRTH_DATE` date DEFAULT NULL,
  `FISCAL_CODE` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TYPE` char(1) COLLATE utf8mb4_unicode_ci NOT NULL,
  `BLOCKED` tinyint(1) NOT NULL DEFAULT '0',
  `DELETED` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `USER`
--

INSERT INTO `USER` (`ID`, `ID_STRUCTURE`, `EMAIL`, `NAME`, `SURNAME`, `ADDRESS`, `PHONE`, `PASSWORD`, `BIRTH_DATE`, `FISCAL_CODE`, `TYPE`, `BLOCKED`, `DELETED`) VALUES
(1, 1, 'mario.rossi@gmail.com', 'MARIO', 'ROSSI', 'ITALY|PIEMONTE|TORINO|48652|VIA DEGLI ARANCI|3|', '3456719867', '1', '1973-12-12', 'RSSMRA73T12L219G', 'A', 0, 0),
(3, 1, 'lucia.lusso@gmail.com', 'LUCIA', 'LUSSO', 'ITALY|BASILICATA|POTENZA|45683|VIA PRINCIPALE|10|', '3569812456', '1', '1973-12-12', 'LCLSOO36721HR39A', 'E', 0, 0),
(4, 1, 'RobbyVerdy@gmail.com', 'ROBERTO', 'VERDI', 'ITALY|BASILICATA|POTENZA|45638|VIA LUCANIA|23|', '3334455998', 'robertoverdi', '1973-12-12', 'VRDRRT82A01G942G', 'E', 0, 1),
(5, 1, 'lucianoligatoro@gmail.com', 'LUCIANO', 'LIGATORO', 'ITALY|VENETO|VENEZIA|45858|VIA COMACCHIO|9|', '3484456489', 'password', '1973-12-12', 'LGTLCN20E21L736L', 'E', 0, 0),
(6, 1, 'lucianoligatori@gmail.com', 'LUCIANO', 'LIGATORI', 'ITALY|VENETO|VENEZIA|45808|VIA LAGUNA|48|', '3484456489', 'password', '1973-12-12', 'LGTLCN20E21L737L', 'E', 0, 0),
(7, NULL, 'mm@gmail.com', 'Marcello', 'Marci', 'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|', '354887658', '1', NULL, NULL, 'C', 0, 0),
(8, NULL, 'marcella@marci.com', 'MARCELLA', 'MARCI', 'ITALY|EMILIA-ROMAGNA|FERRARA|84965|VIA EMILIA|75|', '1257786548', '1', NULL, NULL, 'C', 0, 0),
(9, NULL, 'marcobello@gmail.com', 'MARCO', 'BELLO', 'ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|', '2485568795', 'password', NULL, NULL, 'C', 0, 0),
(10, NULL, 'lucaluce@gmail.com', 'LUCA', 'LUCE', 'ITALY|VENETO|VICENZA|12121|VIA BENEDETTA|16|', '358448658', '1', NULL, NULL, 'C', 0, 0),
(11, NULL, 'mario.zambrini@gmail.com', 'ALESSIA', 'Zambro', 'ITALY|TOSCANA|POTENZA|1111111|21||', '111111111', '1', NULL, NULL, 'C', 0, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `WISHLIST`
--

CREATE TABLE `WISHLIST` (
  `ID_CUSTOMER` bigint(20) UNSIGNED NOT NULL,
  `ID_PRODUCT` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `WISHLIST`
--

INSERT INTO `WISHLIST` (`ID_CUSTOMER`, `ID_PRODUCT`) VALUES
(10, 1),
(10, 2),
(10, 5),
(11, 7);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `BOOKING`
--
ALTER TABLE `BOOKING`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `ID` (`ID`),
  ADD KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
  ADD KEY `ID_STRUCTURE` (`ID_STRUCTURE`);

--
-- Indici per le tabelle `CART`
--
ALTER TABLE `CART`
  ADD PRIMARY KEY (`ID_CUSTOMER`,`ID_PRODUCT`),
  ADD KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
  ADD KEY `ID_PRODUCT` (`ID_PRODUCT`);

--
-- Indici per le tabelle `COUNTER`
--
ALTER TABLE `COUNTER`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `ITEMS_LIST`
--
ALTER TABLE `ITEMS_LIST`
  ADD PRIMARY KEY (`ID_PRODUCT`,`ID_ORDER`),
  ADD KEY `ID_ORDER` (`ID_ORDER`),
  ADD KEY `ID_PRODUCT` (`ID_PRODUCT`);

--
-- Indici per le tabelle `ORDERS`
--
ALTER TABLE `ORDERS`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `ID` (`ID`),
  ADD KEY `ID_CUSTOMER` (`ID_CUSTOMER`);

--
-- Indici per le tabelle `PRODUCT`
--
ALTER TABLE `PRODUCT`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `ID` (`ID`),
  ADD KEY `ID_STRUCTURE` (`ID_STRUCTURE`);

--
-- Indici per le tabelle `STRUCTURE`
--
ALTER TABLE `STRUCTURE`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `ID` (`ID`) USING BTREE;

--
-- Indici per le tabelle `USER`
--
ALTER TABLE `USER`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `EMAIL` (`EMAIL`),
  ADD UNIQUE KEY `ID` (`ID`),
  ADD UNIQUE KEY `FISCAL_CODE` (`FISCAL_CODE`),
  ADD KEY `ID_STRUCTURE` (`ID_STRUCTURE`);

--
-- Indici per le tabelle `WISHLIST`
--
ALTER TABLE `WISHLIST`
  ADD PRIMARY KEY (`ID_CUSTOMER`,`ID_PRODUCT`),
  ADD KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
  ADD KEY `ID_PRODUCT` (`ID_PRODUCT`);

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `BOOKING`
--
ALTER TABLE `BOOKING`
  ADD CONSTRAINT `BOOKING_ibfk_3` FOREIGN KEY (`ID_STRUCTURE`) REFERENCES `STRUCTURE` (`ID`) ON UPDATE CASCADE,
  ADD CONSTRAINT `BOOKING_ibfk_4` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `USER` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Limiti per la tabella `CART`
--
ALTER TABLE `CART`
  ADD CONSTRAINT `CART_ibfk_1` FOREIGN KEY (`ID_PRODUCT`) REFERENCES `PRODUCT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `CART_ibfk_2` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `USER` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `ITEMS_LIST`
--
ALTER TABLE `ITEMS_LIST`
  ADD CONSTRAINT `ITEMS_LIST_ibfk_1` FOREIGN KEY (`ID_ORDER`) REFERENCES `ORDERS` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ITEMS_LIST_ibfk_2` FOREIGN KEY (`ID_PRODUCT`) REFERENCES `PRODUCT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `ORDERS`
--
ALTER TABLE `ORDERS`
  ADD CONSTRAINT `ORDERS_ibfk_1` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `USER` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Limiti per la tabella `PRODUCT`
--
ALTER TABLE `PRODUCT`
  ADD CONSTRAINT `PRODUCT_ibfk_1` FOREIGN KEY (`ID_STRUCTURE`) REFERENCES `STRUCTURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `USER`
--
ALTER TABLE `USER`
  ADD CONSTRAINT `USER_ibfk_1` FOREIGN KEY (`ID_STRUCTURE`) REFERENCES `STRUCTURE` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Limiti per la tabella `WISHLIST`
--
ALTER TABLE `WISHLIST`
  ADD CONSTRAINT `WISHLIST_ibfk_1` FOREIGN KEY (`ID_PRODUCT`) REFERENCES `PRODUCT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `WISHLIST_ibfk_2` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `USER` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
