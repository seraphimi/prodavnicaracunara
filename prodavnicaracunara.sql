/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-12.0.2-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: prodavnicaracunara
-- ------------------------------------------------------
-- Server version	12.0.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `kupci`
--

DROP TABLE IF EXISTS `kupci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `kupci` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adresa` varchar(200) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `ime` varchar(50) NOT NULL,
  `prezime` varchar(50) NOT NULL,
  `telefon` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ao579vysqssd3tjjehj6su1jd` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kupci`
--

LOCK TABLES `kupci` WRITE;
/*!40000 ALTER TABLE `kupci` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `kupci` VALUES
(1,'Knez Mihailova 12, Beograd','marko.petrovic@gmail.com','Marko','Petrović','+381641234567'),
(2,'Kralja Petra 5, Novi Sad','ana.jovanovic@yahoo.com','Ana','Jovanović','+381652345678'),
(3,'Svetosavska 8, Niš','stefan.nikolic@hotmail.com','Stefan','Nikolić','+381663456789'),
(4,'Zmaj Jovina 15, Sombor','milica.stojanovic@gmail.com','Milica','Stojanović','+381674567890'),
(5,'Cara Dušana 22, Kragujevac','nemanja.milosavljevic@outlook.com','Nemanja','Milosavljević','+381685678901');
/*!40000 ALTER TABLE `kupci` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `narudzba_proizvod`
--

DROP TABLE IF EXISTS `narudzba_proizvod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `narudzba_proizvod` (
  `narudzba_id` bigint(20) NOT NULL,
  `proizvod_id` bigint(20) NOT NULL,
  KEY `FKopvhrp8neycegjqsuvncjugo7` (`proizvod_id`),
  KEY `FK98i2e5pnp58cg4c9me7rvodgl` (`narudzba_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `narudzba_proizvod`
--

LOCK TABLES `narudzba_proizvod` WRITE;
/*!40000 ALTER TABLE `narudzba_proizvod` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `narudzba_proizvod` VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(4,2),
(5,5);
/*!40000 ALTER TABLE `narudzba_proizvod` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `narudzbe`
--

DROP TABLE IF EXISTS `narudzbe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `narudzbe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `broj_narudzbe` varchar(50) NOT NULL,
  `datum_kreiranja` datetime NOT NULL,
  `status` varchar(20) NOT NULL,
  `ukupna_cena` decimal(10,2) NOT NULL,
  `kupac_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9rg2dyt8f4h3gfqhpdl8rejjm` (`broj_narudzbe`),
  KEY `FKsnxtrxmo2bwa8pgreullq1fq0` (`kupac_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `narudzbe`
--

LOCK TABLES `narudzbe` WRITE;
/*!40000 ALTER TABLE `narudzbe` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `narudzbe` VALUES
(1,'ORD-2024-001','2024-01-15 10:30:00','ISPORUCENA',1250.00,1),
(2,'ORD-2024-002','2024-01-16 14:45:00','ISPORUCENA',650.00,2),
(3,'ORD-2024-003','2024-01-17 09:15:00','ISPORUCENA',2100.00,3),
(4,'ORD-2024-004','2024-01-18 16:20:00','ISPORUCENA',900.00,4),
(5,'ORD-2024-005','2024-01-19 11:10:00','ISPORUCENA',2850.00,5);
/*!40000 ALTER TABLE `narudzbe` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `placanja`
--

DROP TABLE IF EXISTS `placanja`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `placanja` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datum` datetime NOT NULL,
  `nacin_placanja` varchar(20) NOT NULL,
  `status` varchar(20) NOT NULL,
  `narudzba_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk4eko5yewwri404m0hodylw7c` (`narudzba_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `placanja`
--

LOCK TABLES `placanja` WRITE;
/*!40000 ALTER TABLE `placanja` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `placanja` VALUES
(1,'2024-01-15 10:35:00','KARTICA','PLACENO',1),
(2,'2024-01-16 14:50:00','PAYPAL','NEPLACENO',2),
(3,'2024-01-17 09:20:00','GOTOVINA','PLACENO',3),
(4,'2024-01-18 16:25:00','KARTICA','NEPLACENO',4),
(5,'2024-01-19 11:15:00','PAYPAL','PLACENO',5);
/*!40000 ALTER TABLE `placanja` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `proizvodi`
--

DROP TABLE IF EXISTS `proizvodi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `proizvodi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cena` decimal(10,2) NOT NULL,
  `cpu` text DEFAULT NULL,
  `gpu` text DEFAULT NULL,
  `kolicinaustanju` int(11) NOT NULL,
  `naziv` varchar(100) NOT NULL,
  `ram` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proizvodi`
--

LOCK TABLES `proizvodi` WRITE;
/*!40000 ALTER TABLE `proizvodi` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `proizvodi` VALUES
(1,1250.00,'AMD Ryzen 7 5800X','NVIDIA RTX 4070',15,'Gaming PC Pro','32GB DDR4'),
(2,650.00,'Intel Core i5-12400','Intel UHD Graphics',25,'Office Computer','16GB DDR4'),
(3,2100.00,'Intel Core i9-13900K','NVIDIA RTX 4080',8,'Workstation Elite','64GB DDR5'),
(4,450.00,'AMD Ryzen 5 5600G','AMD Radeon Graphics',30,'Budget Build','8GB DDR4'),
(5,2850.00,'AMD Ryzen 9 7900X','NVIDIA RTX 4090',5,'Creator Station','32GB DDR5');
/*!40000 ALTER TABLE `proizvodi` ENABLE KEYS */;
UNLOCK TABLES;
commit;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-09-14 20:21:17
