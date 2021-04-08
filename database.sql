-- MySQL dump 10.13  Distrib 8.0.23, for Linux (x86_64)
--
-- Host: 0.0.0.0    Database: app
-- ------------------------------------------------------
-- Server version	5.7.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_created` date NOT NULL,
  `status` varchar(255) NOT NULL,
  `uid` bigint(20) NOT NULL,
  `vendor` varchar(255) NOT NULL,
  `gateway_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bym2ir5cd5feay02tryi5dv1a` (`uid`),
  KEY `FKq1cskqsy9nxmn4syxncvk1s0o` (`gateway_id`),
  CONSTRAINT `FKq1cskqsy9nxmn4syxncvk1s0o` FOREIGN KEY (`gateway_id`) REFERENCES `gateway` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
INSERT INTO `device` VALUES (30,'2021-04-08','ONLINE',40001001,'Dell',10),(31,'2021-04-08','ONLINE',40000001,'Sony',9),(32,'2021-04-08','OFFLINE',40000002,'Dell',9),(33,'2021-04-08','ONLINE',40005001,'HP',13),(34,'2021-04-08','ONLINE',40002001,'Microsoft',12),(35,'2021-04-08','ONLINE',50000001,'HP',11),(36,'2021-04-08','ONLINE',50000002,'HP',11),(37,'2021-04-08','ONLINE',40001002,'HP',10),(38,'2021-04-08','ONLINE',40005011,'Dell',13),(39,'2021-04-08','ONLINE',40000003,'Dell',9),(40,'2021-04-08','ONLINE',40002002,'Dell',12),(41,'2021-04-08','ONLINE',40000004,'Dell',9),(42,'2021-04-08','ONLINE',40000005,'Dell',9),(43,'2021-04-08','ONLINE',40000006,'Dell',9),(44,'2021-04-08','ONLINE',40000007,'Dell',9),(45,'2021-04-08','ONLINE',40000008,'Dell',9),(46,'2021-04-08','ONLINE',40000009,'Dell',9),(47,'2021-04-08','ONLINE',4000010,'Dell',9);
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gateway`
--

DROP TABLE IF EXISTS `gateway`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gateway` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ipv4address` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `serial` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qmicmpxohwyw8ljysdnm9ypox` (`ipv4address`),
  UNIQUE KEY `UK_8o93sxh5kntyslevi720y55x3` (`serial`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gateway`
--

LOCK TABLES `gateway` WRITE;
/*!40000 ALTER TABLE `gateway` DISABLE KEYS */;
INSERT INTO `gateway` VALUES (9,1084752130,'Ottawa','100101'),(10,1084752386,'Edmonton','100102'),(11,1084752642,'Victoria','100103'),(12,1084752898,'Winnipeg','101104'),(13,1084754434,'Fredericton','101016');
/*!40000 ALTER TABLE `gateway` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-08 17:35:11
