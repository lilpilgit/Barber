-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Giu 01, 2020 alle 20:31
-- Versione del server: 10.4.12-MariaDB
-- Versione PHP: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aesthetic`
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
  `DESIRED_QTY` int(10) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
('ordersId', 0),
('productId', 9),
('structureId', 1),
('userId', 10);

-- --------------------------------------------------------

--
-- Struttura della tabella `ITEMS_LIST`
--

CREATE TABLE `ITEMS_LIST` (
  `ID_PRODUCT` bigint(20) UNSIGNED NOT NULL,
  `ID_ORDER` bigint(20) UNSIGNED NOT NULL,
  `QUANTITY` int(10) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `ORDERS`
--

CREATE TABLE `ORDERS` (
  `ID` bigint(20) UNSIGNED NOT NULL,
  `SELL_DATE` date NOT NULL,
  `ORDER_DATE` date NOT NULL,
  `STATUS` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `TOT_PRICE` decimal(10,2) NOT NULL,
  `SHIPPING_ADDR` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DELETED` tinyint(1) NOT NULL DEFAULT 0,
  `ID_CUSTOMER` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `DESCRIPTION` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `QUANTITY` int(10) UNSIGNED NOT NULL,
  `CATEGORY` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SHOWCASE` tinyint(1) NOT NULL,
  `DELETED` tinyint(4) NOT NULL,
  `ID_STRUCTURE` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
(1, 'ITALY|PIEMONTE|TORINO|48652|VIA DEGLI ARANCI|3|', '09:00:00', '18:00:00', '00:30:00', 'BarberHub', '1598468498');

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
  `BLOCKED` tinyint(1) NOT NULL DEFAULT 0,
  `DELETED` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `USER`
--

INSERT INTO `USER` (`ID`, `ID_STRUCTURE`, `EMAIL`, `NAME`, `SURNAME`, `ADDRESS`, `PHONE`, `PASSWORD`, `BIRTH_DATE`, `FISCAL_CODE`, `TYPE`, `BLOCKED`, `DELETED`) VALUES
(1, NULL, 'mario.rossi@gmail.com', 'MARIO', 'ROSSI', 'ITALY|PIEMONTE|TORINO|48652|VIA DEGLI ARANCI|3|', '3456719867', 'mariorossi', '1973-12-12', 'RSSMRA73T12L219G', 'A', 0, 0),
(3, NULL, 'lucia.lusso@gmail.com', 'LUCIA', 'LUSSO', 'ITALY|BASILICATA|POTENZA|45683|VIA PRINCIPALE|8|', '3569812456', 'lucialusso', '1973-12-12', 'LCLSOO36721HR39A', 'E', 0, 0),
(4, NULL, 'RobbyVerdy@gmail.com', 'ROBERTO', 'VERDI', 'ITALY|BASILICATA|POTENZA|45638|VIA LUCANIA|23|', '3334455998', 'robertoverdi', '1973-12-12', 'VRDRRT82A01G942G', 'E', 0, 0),
(5, NULL, 'lucianoligatoro@gmail.com', 'LUCIANO', 'LIGATORO', 'ITALY|VENETO|VENEZIA|45858|VIA COMACCHIO|9|', '3484456489', 'password', '1973-12-12', 'LGTLCN20E21L736L', 'E', 0, 0),
(6, NULL, 'lucianoligatori@gmail.com', 'LUCIANO', 'LIGATORI', 'ITALY|VENETO|VENEZIA|45808|VIA LAGUNA|48|', '3484456489', 'password', '1973-12-12', 'LGTLCN20E21L737L', 'E', 0, 0),
(7, NULL, 'mm@gmail.com', 'Marcello', 'Marci', 'ITALY|LAZIO|ROMA|89655|VIA VENETO|79|', '354887658', '1', '1973-12-12', NULL, 'C', 0, 0),
(8, NULL, 'marcella@marci.com', 'MARCELLA', 'MARCI', 'ITALY|EMILIA-ROMAGNA|FERRARA|84965|VIA EMILIA|75|', '1257786548', '1', '1973-12-12', NULL, 'C', 0, 0),
(9, NULL, 'marcobello@gmail.com', 'MARCO', 'BELLO', 'ITALY|VENETO|VENEZIA|48958|VIA EUROPA|10|', '2485568795', 'password', '1973-12-12', NULL, 'C', 0, 0),
(10, NULL, 'lucaluce@gmail.com', 'LUCA', 'LUCE', 'ITALY|VENETO|VICENZA|45895|VIA BENEDETTA|15|', '358448658', 'password', '1973-12-12', NULL, 'C', 0, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `WISHLIST`
--

CREATE TABLE `WISHLIST` (
  `ID_CUSTOMER` bigint(20) UNSIGNED NOT NULL,
  `ID_PRODUCT` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
