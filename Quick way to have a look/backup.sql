-- MySQL dump 10.13  Distrib 8.0.22, for Linux (x86_64)
--
-- Host: localhost    Database: patient_db
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `type` varchar(31) NOT NULL,
  `patient_id` binary(16) NOT NULL,
  `birth_date` date DEFAULT NULL,
  `first_name` varchar(25) NOT NULL,
  `gender` int DEFAULT NULL,
  `last_name` varchar(25) NOT NULL,
  `address_line1` varchar(30) DEFAULT NULL,
  `address_line2` varchar(30) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`patient_id`),
  UNIQUE KEY `UK8snd6pabuuk99uckls8ftd2uj` (`first_name`,`last_name`,`birth_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES ('PatientDetails',_binary '\Â(sõ\"A òE]J0','1959-06-28','Piers',0,'Bailey','1202 Bumble Dr',NULL,'JAVA-SPRING-CITY','747-815-0557','2021'),('PatientDetails',_binary 'DõÉ8é@á±7K\ˆ=ç\£','2002-06-28','Test',1,'TestEarlyOnset',NULL,NULL,NULL,NULL,NULL),('PatientDetails',_binary 'cå\Ï¡&Iöëdä0\Ã0i','1952-11-11','Edward',0,'Arnold','2 Warren Street ',NULL,'JAVA-SPRING-CITY','387-866-1399','2021'),('PatientDetails',_binary 'd¥≤ì;B;∞\‹{≤\ÔÆ\„#','1991-11-11','Jaimie',1,'SOMMERS',NULL,NULL,NULL,NULL,NULL),('PatientDetails',_binary 'içsº¿\»C≥]\'ºå{\“\Û','1964-06-18','Natalie',1,'Clark','12 Beechwood Road',NULL,'JAVA-SPRING-CITY','241-467-9197','2021'),('PatientDetails',_binary 'l—∏q2E´®]®í\«6[≤','1949-12-07','Tracey',1,'Ross','40 Sulphur Springs Dr',NULL,'ANGULAR-CITY','131-396-5049','2022'),('PatientDetails',_binary 'p&&\0öGãê\‚°âäi','1968-06-22','Lucas',0,'Ferguson','2 Warren Street ',NULL,'JAVA-SPRING-CITY','387-866-1399','2021'),('PatientDetails',_binary 'w\ÒõdK∫OÖ6µ\ﬁQ6Xò','1966-12-31','test',1,'TestNone','1 Brookside ',NULL,'TEST CITY','100-222-3333','92333'),('PatientDetails',_binary '|D§É\ÔN<∑\Á˚\Ó8∑','1945-06-24','test',0,'TestBorderline','2 High St',NULL,'TEST CITY','200-333-4444','92000'),('PatientDetails',_binary 'Ä\‘\‘˘&FRù\ı\˜\˜\Èsrï','2004-06-18','Test',0,'TestInDanger','3 Club Road',NULL,'TEST CITY','300-444-5555','92000'),('PatientDetails',_binary 'É…™æ1\”B?Ω\'9|%\Óÿ±','1952-09-27','Pippa',1,'Rees','745 West Valley Farms Drive',NULL,'JAVA-SPRING-CITY','628-423-0993','2021'),('PatientDetails',_binary 'à\·€õZæA©íêÄçÜk˙µ','1958-06-29','Wendy',1,'Ince','4 Southampton Road',NULL,'ANGULAR-CITY','802-911-9975','2022'),('PatientDetails',_binary '©ãçõJCÅªë\‡\Ù\È†~','1963-09-16','John',0,'DOE',NULL,NULL,NULL,NULL,NULL),('PatientDetails',_binary '\Œ#Ω∂ÀòLöér\˜æ\÷\ÁV','1966-12-31','Claire',1,'Wilson','12 Cobblestone St ',NULL,'JAVA-SPRING-CITY','300-452-1091','2021'),('PatientDetails',_binary '\Œf\“P#yN”ü§p\˜oj^]','1946-11-26','Anthony',0,'Sharp','894 Hall Street',NULL,'JAVA-SPRING-CITY','451-761-8383','2021'),('PatientDetails',_binary '\ˆ\Ê\Â$NLì¯Ç$\∆)ß','1945-06-24','Max',0,'Buckland','193 Vale St',NULL,'ANGULAR-CITY','833-534-0864','2022');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-01-27  0:39:12
