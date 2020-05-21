-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Mag 20, 2020 alle 12:05
-- Versione del server: 10.4.12-MariaDB
-- Versione PHP: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aesthetic`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `ADMIN`
--

CREATE TABLE `ADMIN`
(
    `ID`          bigint(20) UNSIGNED                    NOT NULL,
    `BIRTH_DATE`  date                                   NOT NULL,
    `FISCAL_CODE` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `ADMIN`
--

INSERT INTO `ADMIN` (`ID`, `BIRTH_DATE`, `FISCAL_CODE`)
VALUES (1, '1973-12-12', 'RSSMRA73T12L219G');

-- --------------------------------------------------------

--
-- Struttura della tabella `BOOKING`
--

CREATE TABLE `BOOKING`
(
    `ID`             bigint(20) UNSIGNED             NOT NULL,
    `DELETED`        tinyint(1)                      NOT NULL,
    `DELETED_REASON` text COLLATE utf8mb4_unicode_ci NOT NULL,
    `DATE`           date DEFAULT NULL,
    `HOUR_START`     time                            NOT NULL,
    `ID_CUSTOMER`    bigint(20) UNSIGNED             NOT NULL,
    `ID_STRUCTURE`   bigint(20) UNSIGNED             NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `CART`
--

CREATE TABLE `CART`
(
    `ID`          bigint(20) UNSIGNED NOT NULL,
    `ID_CUSTOMER` bigint(20) UNSIGNED NOT NULL,
    `ID_PRODUCT`  bigint(20) UNSIGNED NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `COUNTER`
--

CREATE TABLE `COUNTER`
(
    `ID`    varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
    `VALUE` int(10) UNSIGNED                       NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `COUNTER`
--

INSERT INTO `COUNTER` (`ID`, `VALUE`)
VALUES ('bookingId', 0),
       ('cartId', 0),
       ('ordersId', 0),
       ('productId', 9),
       ('structureId', 1),
       ('userId', 4);

-- --------------------------------------------------------

--
-- Struttura della tabella `CUSTOMER`
--

CREATE TABLE `CUSTOMER`
(
    `ID`                      bigint(20) UNSIGNED NOT NULL,
    `NUM_BOOKED_RESERVATIONS` int(10) UNSIGNED    NOT NULL DEFAULT 0,
    `NUM_ORDERED_PRODUCT`     int(10) UNSIGNED    NOT NULL DEFAULT 0,
    `BLOCKED`                 tinyint(1)          NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `EMPLOYEE`
--

CREATE TABLE `EMPLOYEE`
(
    `ID`           bigint(20) UNSIGNED                    NOT NULL,
    `BIRTH_DATE`   date                                   NOT NULL,
    `FISCAL_CODE`  varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
    `HIRE_DATE`    date                                   NOT NULL,
    `ID_STRUCTURE` bigint(20) UNSIGNED DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `EMPLOYEE`
--

INSERT INTO `EMPLOYEE` (`ID`, `BIRTH_DATE`, `FISCAL_CODE`, `HIRE_DATE`, `ID_STRUCTURE`)
VALUES (2, '2020-05-17', 'LUCUTDD9432423PA', '2020-05-17', 1),
       (3, '2020-05-17', 'LCLSOO36721HR39A', '2020-05-17', 1),
       (4, '1982-01-01', 'VRDRRT82A01G942G', '2020-05-10', 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `ORDERS`
--

CREATE TABLE `ORDERS`
(
    `ID`          bigint(20) UNSIGNED                    NOT NULL,
    `ID_PRODUCT`  bigint(20) UNSIGNED                    NOT NULL,
    `SELL_DATE`   date                                   NOT NULL,
    `ORDER_DATE`  date                                   NOT NULL,
    `STATUS`      varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
    `TOT_PRICE`   decimal(10, 2)                         NOT NULL,
    `DELETED`     tinyint(1)                             NOT NULL,
    `ID_CUSTOMER` bigint(20) UNSIGNED                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `PRODUCT`
--

CREATE TABLE `PRODUCT`
(
    `ID`           bigint(20) UNSIGNED                    NOT NULL,
    `PRODUCER`     varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
    `PRICE`        decimal(10, 2) UNSIGNED                NOT NULL,
    `DISCOUNT`     int(10) UNSIGNED                        DEFAULT NULL,
    `NAME`         varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `INSERT_DATE`  date                                   NOT NULL,
    `PIC_NAME`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DESCRIPTION`  text COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `QUANTITY`     int(10) UNSIGNED                       NOT NULL,
    `CATEGORY`     varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `SHOWCASE`     tinyint(1)                             NOT NULL,
    `DELETED`      tinyint(4)                             NOT NULL,
    `ID_STRUCTURE` bigint(20) UNSIGNED                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `PRODUCT`
--

INSERT INTO `PRODUCT` (`ID`, `PRODUCER`, `PRICE`, `DISCOUNT`, `NAME`, `INSERT_DATE`, `PIC_NAME`, `DESCRIPTION`,
                       `QUANTITY`, `CATEGORY`, `SHOWCASE`, `DELETED`, `ID_STRUCTURE`)
VALUES (1, 'Garnier', '20.00', 10, 'UltraDolce – Estratto di Camomilla e Miele', '2020-05-04', 'product1.webp',
        'Vera ricetta di lucentezza per i capelli chiari, questa gamma Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietà schiarenti e riflessanti, al Miele, apprezzato per le sue virtù nutritive, in formule delicate e leggere che non appesantiscono i tuoi capelli e li rendono morbidi e setosi. I capelli sono incredibilmente brillanti e morbidi al tatto.  Fine dei biondi spenti, i capelli scoprono riflessi natural\r\n\r\nVera ricetta di lucentezza per i capelli chiari, questa gamma Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietà schiarenti e riflessanti, al Miele, apprezzato per le sue virtù nutritive, in formule delicate e leggere che non appesantiscono i tuoi capelli e li rendono moribidi e setosi.\r\n\r\nI capelli sono incredibilmente brillanti e morbidi al tatto. \r\n\r\nFine dei biondi spenti, i capelli scoprono riflessi naturali e luminosi.\r\n\r\nLa dolcezza dal cuore delle piante Garnier Ultra Dolce unisce la Camomilla, rinomata per le sue proprietà schiarenti e ammorbidenti, al Miele, apprezzato per le sue virtù nutritive.\r\n\r\n',
        10, 'Shampoo', 1, 0, 1),
       (2, 'Garnier', '15.00', NULL, 'Fructis Antiforfora - Shampoo fortificante', '2020-05-04', 'product2.webp',
        'Shampoo fortificante antiforfora per eliminare la forfora visibile ed ossigenare la cute. \r\nDedicato ai capelli con forfora, lo shampoo antiforfora Fructis Antiforfora Reoxygen Fructis combina l’acido salicilico e il piroctone olamine antibatterici con l’estratto di tea tree, noto per le sue proprietà purificanti. Elimina la forfora visibile* combattendo gli agenti che la causano.\r\n\r\nSUPERFRUTTI. SUPERCAPELLI. \r\nNuova formula senza parabeni, con attivi di frutti fortificanti. Un\'esclusiva combinazione dall\'efficacia provata di estratti derivati da frutti e piante, da vitamine B3 e B6 e con una proteina del limone**. Per capelli forti e dall\'aspetto sano. E\' dimostrato.\r\n\r\nCONSIGLI D\'USO\r\nApplicare sui capelli bagnati, massaggiare delicatamente e risciacquare. In caso di contatto con gli occhi, risciacquare immediatamente.\r\n\r\nRISULTATI\r\nLa cute è purificata e rinfrescata: torna a respirare! I capelli sono forti e brillanti.\r\n\r\nFRUCTIS S\'IMPEGNA PER L\'AMBIENTE\r\nFlaconi con il 25% di plastica riciclata.\r\nFlaconi 100% riciclabili se correttamente smaltiti.\r\nProdotti in uno stabilimento impegnato nello sviluppo sostenibile\r\n\r\n*Test di autovalutazione\r\n**derivato di proteina \r\n\r\nIngredienti chiaveOctopirox: un efficacissimo attivo antiforfora.  Attivo Antibatterico: concorre ad eliminare gli agenti responsabili della forfora, prevenendone la ricomparsa.  Olio Essenziale di Tea Tree: noto per le sue proprietà purificanti, è stato integrato nella formula per aiutare a liberare la cute dalle impurità donando una sensazione di freschezza duratura.',
        20, 'Shampoo', 1, 0, 1),
       (3, 'Ahava', '19.05', 30, 'Ahava Time to Energize - Schiuma da Barba', '2020-05-04', 'product3.png',
        'Crema da Barba setosa e senza schiuma per una perfetta rasatura a fil di pelle. A base di minerali del Mar Morto, estratti vegetali e vitamine che ammorbidiscono la barba ispida, per una pelle idratata durante e dopo la rasatura.\r\n',
        5, 'Cream', 1, 0, 1),
       (4, 'Vidal', '10.50', NULL, 'ARGAN OIL Nutre & Rigenera - Olio di Argan', '2020-04-15', 'product4.webp',
        'Olio di Argan: sensuale relax.\r\nFormulato con Olio di Argan di origine biologica, ricco di sostanze nutrienti e di ingredienti attivi (Vitamina E, Omega 3 ed Omega 6), per detergere e nutrire intensamente la pelle. Il profumo, caldo ed avvolgente, unisce note di testa agrumate ad un cuore deliziosamente speziato e lascia una persistente e sensuale scia legnosa: la fragranza ideale per un momento di puro relax.',
        30, 'bagnodoccia', 0, 0, 1),
       (5, 'Dear Barber', '15.00', 15, 'Dear Barber Beard Shampoo', '2020-05-08', 'product5.png',
        'It improves the condition of beard and hair, increasing its bulk.\r\nEnriched with an invigorating fragrance, this shampoo makes the beard and hair more manageable and shiny.\r\nOur antistatic formula produces an excellent calming effect.',
        20, 'Shampoo', 0, 0, 1),
       (6, 'Dear Beard', '10.99', 5, 'Shaving Milk', '2020-05-04', 'product6.png',
        'Fluid shaving cream. Ideal for partial or total shaving of the beard. Ideal for preparing the skin before shaving.',
        11, 'Lotions', 0, 0, 1),
       (7, 'Suavecito', '12.89', NULL, 'Shaving Cream ', '2020-05-04', 'product7.png',
        'The natural peppermint opens your pores to get your razor in ultra close. It washes off painlessly with water and does not dry out your skin. Finish up with one of our healing and soothing aftershaves and walk out the door refreshed - ready to take on the day.',
        80, 'Cream', 0, 0, 1),
       (8, 'Rogaine', '65.00', 15, 'Minoxidil', '2020-05-04', 'product8.png',
        'Minoxidil solution and foam are used to help hair growth in the treatment of male pattern baldness. It is not used for baldness at the front of the scalp or receding hairline in men. The foam and 2 percent minoxidil solution is also used to help hair growth in women with thinning hair.\r\n\r\nMinoxidil belongs to a class of drugs known as vasodilators. It is not known how minoxidil causes hair growth. This medication is not used for sudden/patchy hair loss, unexplained hair loss (for example, if you have no family history of hair loss), or hair loss after giving birth.\r\n\r\nDo not use this product if you are 18 years old or younger.',
        49, 'Lotions', 0, 0, 1),
       (9, 'Spartan', '15.00', 2, 'Growth oil', '2020-05-04', 'product9.png',
        'Stimulates hair & beard growth.\r\nSpartan Man\'s natural growth oil is designed to stimulate faster hair growth, and create a fuller, thicker beard.',
        101, 'Oil', 0, 0, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `STRUCTURE`
--

CREATE TABLE `STRUCTURE`
(
    `ID`           bigint(20) UNSIGNED                     NOT NULL,
    `ADDRESS`      varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `OPENING_TIME` time                                    NOT NULL,
    `CLOSING_TIME` time                                    NOT NULL,
    `SLOT`         time                DEFAULT NULL,
    `NAME`         varchar(30) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `PHONE`        varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `ID_ADMIN`     bigint(20) UNSIGNED DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `STRUCTURE`
--

INSERT INTO `STRUCTURE` (`ID`, `ADDRESS`, `OPENING_TIME`, `CLOSING_TIME`, `SLOT`, `NAME`, `PHONE`, `ID_ADMIN`)
VALUES (1, 'Lombardia,Milano,Via Corona,19', '09:00:00', '18:00:00', NULL, 'BarberHub', '02101010', 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `USER`
--

CREATE TABLE `USER`
(
    `ID`          bigint(20) UNSIGNED                     NOT NULL,
    `EMAIL`       varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `NAME`        varchar(30) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `SURNAME`     varchar(30) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `ADDRESS`     varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `PHONE`       varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `PASSWORD`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `IS_ADMIN`    tinyint(1)                                       DEFAULT 0,
    `IS_EMPLOYEE` tinyint(1)                                       DEFAULT 0,
    `IS_CUSTOMER` tinyint(1)                                       DEFAULT 1,
    `DELETED`     tinyint(1)                              NOT NULL DEFAULT 0
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `USER`
--

INSERT INTO `USER` (`ID`, `EMAIL`, `NAME`, `SURNAME`, `ADDRESS`, `PHONE`, `PASSWORD`, `IS_ADMIN`, `IS_EMPLOYEE`,
                    `IS_CUSTOMER`, `DELETED`)
VALUES (1, 'mario.rossi@gmail.com', 'Mario', 'Rossi', 'Italy,Piemonte,Torino,Via Degli Aranci,3', '3456719867',
        'mariorossi', 1, 0, 0, 0),
       (2, 'luca.cittadini@gmail.com', 'Luca', 'Cittadini', 'Italy,Basilicata,Matera,Via Dei Sassi', '3487512978',
        'lucacittadini', 0, 1, 0, 0),
       (3, 'lucia.lusso@gmail.com', 'Lucia', 'Lusso', 'Italy,Basilicata,Potenza,Via Principale,56,Palazzo Giallo',
        '3569812456', 'lucialusso', 0, 1, 0, 0),
       (4, 'RobbyVerdy@gmail.com', 'Roberto', 'Verdi', 'Italy,Basilicata,Potenza,Via Lucania,23', '3334455998',
        'robertoverdi', 0, 1, 0, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `WISHLIST`
--

CREATE TABLE `WISHLIST`
(
    `ID_CUSTOMER` bigint(20) UNSIGNED NOT NULL,
    `ID_PRODUCT`  bigint(20) UNSIGNED NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `ADMIN`
--
ALTER TABLE `ADMIN`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `FISCAL_CODE` (`FISCAL_CODE`),
    ADD UNIQUE KEY `ID` (`ID`) USING BTREE;

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
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `ID` (`ID`),
    ADD KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
    ADD KEY `ID_PRODUCT` (`ID_PRODUCT`);

--
-- Indici per le tabelle `COUNTER`
--
ALTER TABLE `COUNTER`
    ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `CUSTOMER`
--
ALTER TABLE `CUSTOMER`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `ID` (`ID`);

--
-- Indici per le tabelle `EMPLOYEE`
--
ALTER TABLE `EMPLOYEE`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `ID` (`ID`) USING BTREE,
    ADD KEY `ID_STRUCTURE` (`ID_STRUCTURE`);

--
-- Indici per le tabelle `ORDERS`
--
ALTER TABLE `ORDERS`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `ID` (`ID`),
    ADD KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
    ADD KEY `ID_PRODUCT` (`ID_PRODUCT`);

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
    ADD UNIQUE KEY `ID` (`ID`) USING BTREE,
    ADD KEY `ID_ADMIN` (`ID_ADMIN`);

--
-- Indici per le tabelle `USER`
--
ALTER TABLE `USER`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `EMAIL` (`EMAIL`),
    ADD UNIQUE KEY `ID` (`ID`);

--
-- Indici per le tabelle `WISHLIST`
--
ALTER TABLE `WISHLIST`
    ADD KEY `ID_CUSTOMER` (`ID_CUSTOMER`),
    ADD KEY `ID_PRODUCT` (`ID_PRODUCT`);

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `ADMIN`
--
ALTER TABLE `ADMIN`
    ADD CONSTRAINT `ADMIN_ibfk_1` FOREIGN KEY (`ID`) REFERENCES `USER` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `BOOKING`
--
ALTER TABLE `BOOKING`
    ADD CONSTRAINT `BOOKING_ibfk_2` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `CUSTOMER` (`ID`) ON UPDATE CASCADE,
    ADD CONSTRAINT `BOOKING_ibfk_3` FOREIGN KEY (`ID_STRUCTURE`) REFERENCES `STRUCTURE` (`ID`) ON UPDATE CASCADE;

--
-- Limiti per la tabella `CART`
--
ALTER TABLE `CART`
    ADD CONSTRAINT `CART_ibfk_1` FOREIGN KEY (`ID_PRODUCT`) REFERENCES `PRODUCT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `CART_ibfk_2` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `CUSTOMER` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `CUSTOMER`
--
ALTER TABLE `CUSTOMER`
    ADD CONSTRAINT `CUSTOMER_ibfk_1` FOREIGN KEY (`ID`) REFERENCES `USER` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `EMPLOYEE`
--
ALTER TABLE `EMPLOYEE`
    ADD CONSTRAINT `EMPLOYEE_ibfk_2` FOREIGN KEY (`ID_STRUCTURE`) REFERENCES `STRUCTURE` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT `EMPLOYEE_ibfk_3` FOREIGN KEY (`ID`) REFERENCES `USER` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `ORDERS`
--
ALTER TABLE `ORDERS`
    ADD CONSTRAINT `ORDERS_ibfk_1` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `CUSTOMER` (`ID`) ON UPDATE CASCADE,
    ADD CONSTRAINT `ORDERS_ibfk_2` FOREIGN KEY (`ID_PRODUCT`) REFERENCES `PRODUCT` (`ID`) ON UPDATE CASCADE;

--
-- Limiti per la tabella `PRODUCT`
--
ALTER TABLE `PRODUCT`
    ADD CONSTRAINT `PRODUCT_ibfk_1` FOREIGN KEY (`ID_STRUCTURE`) REFERENCES `STRUCTURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `STRUCTURE`
--
ALTER TABLE `STRUCTURE`
    ADD CONSTRAINT `STRUCTURE_ibfk_1` FOREIGN KEY (`ID_ADMIN`) REFERENCES `ADMIN` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Limiti per la tabella `WISHLIST`
--
ALTER TABLE `WISHLIST`
    ADD CONSTRAINT `WISHLIST_ibfk_1` FOREIGN KEY (`ID_PRODUCT`) REFERENCES `PRODUCT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `WISHLIST_ibfk_2` FOREIGN KEY (`ID_CUSTOMER`) REFERENCES `CUSTOMER` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
