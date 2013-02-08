-- MySQL dump 10.13  Distrib 5.5.29, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: openmrs
-- ------------------------------------------------------
-- Server version	5.5.29-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `openmrs`
--

/*!40000 DROP DATABASE IF EXISTS `openmrs`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `openmrs` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `openmrs`;

--
-- Table structure for table `active_list`
--

DROP TABLE IF EXISTS `active_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `active_list` (
  `active_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `active_list_type_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL,
  `concept_id` int(11) NOT NULL,
  `start_obs_id` int(11) DEFAULT NULL,
  `stop_obs_id` int(11) DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`active_list_id`),
  KEY `user_who_voided_active_list` (`voided_by`),
  KEY `user_who_created_active_list` (`creator`),
  KEY `active_list_type_of_active_list` (`active_list_type_id`),
  KEY `person_of_active_list` (`person_id`),
  KEY `concept_active_list` (`concept_id`),
  KEY `start_obs_active_list` (`start_obs_id`),
  KEY `stop_obs_active_list` (`stop_obs_id`),
  CONSTRAINT `stop_obs_active_list` FOREIGN KEY (`stop_obs_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `active_list_type_of_active_list` FOREIGN KEY (`active_list_type_id`) REFERENCES `active_list_type` (`active_list_type_id`),
  CONSTRAINT `concept_active_list` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `person_of_active_list` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `start_obs_active_list` FOREIGN KEY (`start_obs_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `user_who_created_active_list` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_active_list` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_list`
--

LOCK TABLES `active_list` WRITE;
/*!40000 ALTER TABLE `active_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `active_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `active_list_allergy`
--

DROP TABLE IF EXISTS `active_list_allergy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `active_list_allergy` (
  `active_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `allergy_type` varchar(50) DEFAULT NULL,
  `reaction_concept_id` int(11) DEFAULT NULL,
  `severity` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`active_list_id`),
  KEY `reaction_allergy` (`reaction_concept_id`),
  CONSTRAINT `reaction_allergy` FOREIGN KEY (`reaction_concept_id`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_list_allergy`
--

LOCK TABLES `active_list_allergy` WRITE;
/*!40000 ALTER TABLE `active_list_allergy` DISABLE KEYS */;
/*!40000 ALTER TABLE `active_list_allergy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `active_list_problem`
--

DROP TABLE IF EXISTS `active_list_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `active_list_problem` (
  `active_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(50) DEFAULT NULL,
  `sort_weight` double DEFAULT NULL,
  PRIMARY KEY (`active_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_list_problem`
--

LOCK TABLES `active_list_problem` WRITE;
/*!40000 ALTER TABLE `active_list_problem` DISABLE KEYS */;
/*!40000 ALTER TABLE `active_list_problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `active_list_type`
--

DROP TABLE IF EXISTS `active_list_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `active_list_type` (
  `active_list_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`active_list_type_id`),
  KEY `user_who_retired_active_list_type` (`retired_by`),
  KEY `user_who_created_active_list_type` (`creator`),
  CONSTRAINT `user_who_created_active_list_type` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_active_list_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_list_type`
--

LOCK TABLES `active_list_type` WRITE;
/*!40000 ALTER TABLE `active_list_type` DISABLE KEYS */;
INSERT INTO `active_list_type` VALUES (1,'Allergy','An Allergy the Patient may have',1,'2010-05-28 00:00:00',0,NULL,NULL,NULL,'96f4f603-6a99-11df-a648-37a07f9c90fb'),(2,'Problem','A Problem the Patient may have',1,'2010-05-28 00:00:00',0,NULL,NULL,NULL,'a0c7422b-6a99-11df-a648-37a07f9c90fb');
/*!40000 ALTER TABLE `active_list_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cohort`
--

DROP TABLE IF EXISTS `cohort`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cohort` (
  `cohort_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`cohort_id`),
  UNIQUE KEY `cohort_uuid_index` (`uuid`),
  KEY `user_who_changed_cohort` (`changed_by`),
  KEY `cohort_creator` (`creator`),
  KEY `user_who_voided_cohort` (`voided_by`),
  CONSTRAINT `cohort_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_cohort` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_cohort` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cohort`
--

LOCK TABLES `cohort` WRITE;
/*!40000 ALTER TABLE `cohort` DISABLE KEYS */;
/*!40000 ALTER TABLE `cohort` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cohort_member`
--

DROP TABLE IF EXISTS `cohort_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cohort_member` (
  `cohort_id` int(11) NOT NULL DEFAULT '0',
  `patient_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cohort_id`,`patient_id`),
  KEY `member_patient` (`patient_id`),
  CONSTRAINT `member_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `parent_cohort` FOREIGN KEY (`cohort_id`) REFERENCES `cohort` (`cohort_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cohort_member`
--

LOCK TABLES `cohort_member` WRITE;
/*!40000 ALTER TABLE `cohort_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `cohort_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept`
--

DROP TABLE IF EXISTS `concept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept` (
  `concept_id` int(11) NOT NULL AUTO_INCREMENT,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `short_name` varchar(255) DEFAULT NULL,
  `description` text,
  `form_text` text,
  `datatype_id` int(11) NOT NULL DEFAULT '0',
  `class_id` int(11) NOT NULL DEFAULT '0',
  `is_set` smallint(6) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_id`),
  UNIQUE KEY `concept_uuid_index` (`uuid`),
  KEY `user_who_changed_concept` (`changed_by`),
  KEY `concept_classes` (`class_id`),
  KEY `concept_creator` (`creator`),
  KEY `concept_datatypes` (`datatype_id`),
  KEY `user_who_retired_concept` (`retired_by`),
  CONSTRAINT `concept_classes` FOREIGN KEY (`class_id`) REFERENCES `concept_class` (`concept_class_id`),
  CONSTRAINT `concept_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `concept_datatypes` FOREIGN KEY (`datatype_id`) REFERENCES `concept_datatype` (`concept_datatype_id`),
  CONSTRAINT `user_who_changed_concept` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_concept` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept`
--

LOCK TABLES `concept` WRITE;
/*!40000 ALTER TABLE `concept` DISABLE KEYS */;
INSERT INTO `concept` VALUES (1,0,'','',NULL,4,11,0,1,'2013-02-06 12:17:44',NULL,NULL,NULL,NULL,NULL,NULL,'51089f47-f2b6-44ea-b603-2c2c47bdb00e'),(2,0,'','',NULL,4,11,0,1,'2013-02-06 12:17:44',NULL,NULL,NULL,NULL,NULL,NULL,'1ea33ece-04fd-4825-ba50-43c5c688a89b');
/*!40000 ALTER TABLE `concept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_answer`
--

DROP TABLE IF EXISTS `concept_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_answer` (
  `concept_answer_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `answer_concept` int(11) DEFAULT NULL,
  `answer_drug` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  `sort_weight` double DEFAULT NULL,
  PRIMARY KEY (`concept_answer_id`),
  UNIQUE KEY `concept_answer_uuid_index` (`uuid`),
  KEY `answer` (`answer_concept`),
  KEY `answers_for_concept` (`concept_id`),
  KEY `answer_creator` (`creator`),
  CONSTRAINT `answer` FOREIGN KEY (`answer_concept`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `answers_for_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `answer_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_answer`
--

LOCK TABLES `concept_answer` WRITE;
/*!40000 ALTER TABLE `concept_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_class`
--

DROP TABLE IF EXISTS `concept_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_class` (
  `concept_class_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_class_id`),
  UNIQUE KEY `concept_class_uuid_index` (`uuid`),
  KEY `concept_class_retired_status` (`retired`),
  KEY `concept_class_creator` (`creator`),
  KEY `user_who_retired_concept_class` (`retired_by`),
  CONSTRAINT `concept_class_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_concept_class` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_class`
--

LOCK TABLES `concept_class` WRITE;
/*!40000 ALTER TABLE `concept_class` DISABLE KEYS */;
INSERT INTO `concept_class` VALUES (1,'Test','Acq. during patient encounter (vitals, labs, etc.)',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4907b2-c2cc-11de-8d13-0010c6dffd0f'),(2,'Procedure','Describes a clinical procedure',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d490bf4-c2cc-11de-8d13-0010c6dffd0f'),(3,'Drug','Drug',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d490dfc-c2cc-11de-8d13-0010c6dffd0f'),(4,'Diagnosis','Conclusion drawn through findings',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4918b0-c2cc-11de-8d13-0010c6dffd0f'),(5,'Finding','Practitioner observation/finding',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d491a9a-c2cc-11de-8d13-0010c6dffd0f'),(6,'Anatomy','Anatomic sites / descriptors',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d491c7a-c2cc-11de-8d13-0010c6dffd0f'),(7,'Question','Question (eg, patient history, SF36 items)',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d491e50-c2cc-11de-8d13-0010c6dffd0f'),(8,'LabSet','Term to describe laboratory sets',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d492026-c2cc-11de-8d13-0010c6dffd0f'),(9,'MedSet','Term to describe medication sets',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4923b4-c2cc-11de-8d13-0010c6dffd0f'),(10,'ConvSet','Term to describe convenience sets',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d492594-c2cc-11de-8d13-0010c6dffd0f'),(11,'Misc','Terms which don\'t fit other categories',1,'2004-03-02 00:00:00',0,NULL,NULL,NULL,'8d492774-c2cc-11de-8d13-0010c6dffd0f'),(12,'Symptom','Patient-reported observation',1,'2004-10-04 00:00:00',0,NULL,NULL,NULL,'8d492954-c2cc-11de-8d13-0010c6dffd0f'),(13,'Symptom/Finding','Observation that can be reported from patient or found on exam',1,'2004-10-04 00:00:00',0,NULL,NULL,NULL,'8d492b2a-c2cc-11de-8d13-0010c6dffd0f'),(14,'Specimen','Body or fluid specimen',1,'2004-12-02 00:00:00',0,NULL,NULL,NULL,'8d492d0a-c2cc-11de-8d13-0010c6dffd0f'),(15,'Misc Order','Orderable items which aren\'t tests or drugs',1,'2005-02-17 00:00:00',0,NULL,NULL,NULL,'8d492ee0-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `concept_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_complex`
--

DROP TABLE IF EXISTS `concept_complex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_complex` (
  `concept_id` int(11) NOT NULL,
  `handler` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`concept_id`),
  CONSTRAINT `concept_attributes` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_complex`
--

LOCK TABLES `concept_complex` WRITE;
/*!40000 ALTER TABLE `concept_complex` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_complex` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_datatype`
--

DROP TABLE IF EXISTS `concept_datatype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_datatype` (
  `concept_datatype_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `hl7_abbreviation` varchar(3) DEFAULT NULL,
  `description` varchar(255) NOT NULL DEFAULT '',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_datatype_id`),
  UNIQUE KEY `concept_datatype_uuid_index` (`uuid`),
  KEY `concept_datatype_retired_status` (`retired`),
  KEY `concept_datatype_creator` (`creator`),
  KEY `user_who_retired_concept_datatype` (`retired_by`),
  CONSTRAINT `concept_datatype_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_concept_datatype` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_datatype`
--

LOCK TABLES `concept_datatype` WRITE;
/*!40000 ALTER TABLE `concept_datatype` DISABLE KEYS */;
INSERT INTO `concept_datatype` VALUES (1,'Numeric','NM','Numeric value, including integer or float (e.g., creatinine, weight)',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4a4488-c2cc-11de-8d13-0010c6dffd0f'),(2,'Coded','CWE','Value determined by term dictionary lookup (i.e., term identifier)',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4a48b6-c2cc-11de-8d13-0010c6dffd0f'),(3,'Text','ST','Free text',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4a4ab4-c2cc-11de-8d13-0010c6dffd0f'),(4,'N/A','ZZ','Not associated with a datatype (e.g., term answers, sets)',1,'2004-02-02 00:00:00',0,NULL,NULL,NULL,'8d4a4c94-c2cc-11de-8d13-0010c6dffd0f'),(5,'Document','RP','Pointer to a binary or text-based document (e.g., clinical document, RTF, XML, EKG, image, etc.) stored in complex_obs table',1,'2004-04-15 00:00:00',0,NULL,NULL,NULL,'8d4a4e74-c2cc-11de-8d13-0010c6dffd0f'),(6,'Date','DT','Absolute date',1,'2004-07-22 00:00:00',0,NULL,NULL,NULL,'8d4a505e-c2cc-11de-8d13-0010c6dffd0f'),(7,'Time','TM','Absolute time of day',1,'2004-07-22 00:00:00',0,NULL,NULL,NULL,'8d4a591e-c2cc-11de-8d13-0010c6dffd0f'),(8,'Datetime','TS','Absolute date and time',1,'2004-07-22 00:00:00',0,NULL,NULL,NULL,'8d4a5af4-c2cc-11de-8d13-0010c6dffd0f'),(10,'Boolean','BIT','Boolean value (yes/no, true/false)',1,'2004-08-26 00:00:00',0,NULL,NULL,NULL,'8d4a5cca-c2cc-11de-8d13-0010c6dffd0f'),(11,'Rule','ZZ','Value derived from other data',1,'2006-09-11 00:00:00',0,NULL,NULL,NULL,'8d4a5e96-c2cc-11de-8d13-0010c6dffd0f'),(12,'Structured Numeric','SN','Complex numeric values possible (ie, <5, 1-10, etc.)',1,'2005-08-06 00:00:00',0,NULL,NULL,NULL,'8d4a606c-c2cc-11de-8d13-0010c6dffd0f'),(13,'Complex','ED','Complex value.  Analogous to HL7 Embedded Datatype',1,'2008-05-28 12:25:34',0,NULL,NULL,NULL,'8d4a6242-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `concept_datatype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_description`
--

DROP TABLE IF EXISTS `concept_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_description` (
  `concept_description_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `description` text NOT NULL,
  `locale` varchar(50) NOT NULL DEFAULT '',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_description_id`),
  UNIQUE KEY `concept_description_uuid_index` (`uuid`),
  KEY `user_who_changed_description` (`changed_by`),
  KEY `description_for_concept` (`concept_id`),
  KEY `user_who_created_description` (`creator`),
  CONSTRAINT `description_for_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_changed_description` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_description` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_description`
--

LOCK TABLES `concept_description` WRITE;
/*!40000 ALTER TABLE `concept_description` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_description` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_map`
--

DROP TABLE IF EXISTS `concept_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_map` (
  `concept_map_id` int(11) NOT NULL AUTO_INCREMENT,
  `source` int(11) DEFAULT NULL,
  `source_code` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_map_id`),
  UNIQUE KEY `concept_map_uuid_index` (`uuid`),
  KEY `map_for_concept` (`concept_id`),
  KEY `map_creator` (`creator`),
  KEY `map_source` (`source`),
  CONSTRAINT `map_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `map_for_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `map_source` FOREIGN KEY (`source`) REFERENCES `concept_source` (`concept_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_map`
--

LOCK TABLES `concept_map` WRITE;
/*!40000 ALTER TABLE `concept_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_name`
--

DROP TABLE IF EXISTS `concept_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_name` (
  `concept_id` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `locale` varchar(50) NOT NULL DEFAULT '',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `concept_name_id` int(11) NOT NULL AUTO_INCREMENT,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `concept_name_type` varchar(50) DEFAULT NULL,
  `locale_preferred` smallint(6) DEFAULT '0',
  PRIMARY KEY (`concept_name_id`),
  UNIQUE KEY `concept_name_uuid_index` (`uuid`),
  KEY `name_of_concept` (`name`),
  KEY `name_for_concept` (`concept_id`),
  KEY `user_who_created_name` (`creator`),
  KEY `user_who_voided_this_name` (`voided_by`),
  CONSTRAINT `name_for_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_created_name` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_this_name` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_name`
--

LOCK TABLES `concept_name` WRITE;
/*!40000 ALTER TABLE `concept_name` DISABLE KEYS */;
INSERT INTO `concept_name` VALUES (1,'Vero','it',1,'2013-02-06 12:17:44',1,0,NULL,NULL,NULL,'35bc9aab-0589-4b17-b782-4f1f57791399',NULL,0),(1,'S','it',1,'2013-02-06 12:17:44',2,0,NULL,NULL,NULL,'336ac687-84f9-48c2-ba6e-2ee47fb048f2',NULL,0),(1,'Verdadeiro','pt',1,'2013-02-06 12:17:44',3,0,NULL,NULL,NULL,'7700dd5c-43ca-44df-abfe-94ab3097b280',NULL,0),(1,'Sim','pt',1,'2013-02-06 12:17:44',4,0,NULL,NULL,NULL,'c14aaf7b-9b17-418b-b675-dc4f95613246',NULL,0),(1,'Vrai','fr',1,'2013-02-06 12:17:44',5,0,NULL,NULL,NULL,'9ba2755e-ad23-473d-bc70-e99e462c0545',NULL,0),(1,'Oui','fr',1,'2013-02-06 12:17:44',6,0,NULL,NULL,NULL,'d5ba747b-bbac-4707-b127-9f85cb6e0c49',NULL,0),(1,'True','en',1,'2013-02-06 12:17:44',7,0,NULL,NULL,NULL,'3e10e8df-ff3a-4e62-9a3c-271fc45e5581','FULLY_SPECIFIED',0),(1,'Yes','en',1,'2013-02-06 12:17:44',8,0,NULL,NULL,NULL,'ff6a3dc0-95c9-415d-be9a-7cae8f018b1b',NULL,0),(1,'Verdadero','es',1,'2013-02-06 12:17:44',9,0,NULL,NULL,NULL,'738b84f3-f144-4fd1-9c18-13f955dbc9e5',NULL,0),(1,'S','es',1,'2013-02-06 12:17:44',10,0,NULL,NULL,NULL,'246ab882-3875-4f1a-8353-009caaa0ee7e',NULL,0),(2,'Falso','it',1,'2013-02-06 12:17:44',11,0,NULL,NULL,NULL,'4c67ffe8-85bc-46eb-bfed-78f29dacfa2e',NULL,0),(2,'No','it',1,'2013-02-06 12:17:44',12,0,NULL,NULL,NULL,'b0362623-0b81-4637-8b86-c6b0dba697ca',NULL,0),(2,'Falso','pt',1,'2013-02-06 12:17:44',13,0,NULL,NULL,NULL,'c962551f-5fa0-4324-8fe8-6d028ee01715',NULL,0),(2,'N','pt',1,'2013-02-06 12:17:44',14,0,NULL,NULL,NULL,'0e7d4be1-8b3c-4f35-b615-e4cce6d87963',NULL,0),(2,'Faux','fr',1,'2013-02-06 12:17:44',15,0,NULL,NULL,NULL,'783ffbad-e670-4979-83de-54af5bd236b8',NULL,0),(2,'Non','fr',1,'2013-02-06 12:17:44',16,0,NULL,NULL,NULL,'974357e2-c319-4185-aed5-db995846d07e',NULL,0),(2,'False','en',1,'2013-02-06 12:17:44',17,0,NULL,NULL,NULL,'14c202e8-a1ad-40b1-863f-7416d220c569','FULLY_SPECIFIED',0),(2,'No','en',1,'2013-02-06 12:17:44',18,0,NULL,NULL,NULL,'41f5479b-b605-4f89-bf2b-1dfc40552cf8',NULL,0),(2,'Falso','es',1,'2013-02-06 12:17:44',19,0,NULL,NULL,NULL,'f558bf8e-60a3-4768-ad50-a333fae07856',NULL,0),(2,'No','es',1,'2013-02-06 12:17:44',20,0,NULL,NULL,NULL,'99fd7282-d70f-47a0-8fda-a861a2432e2f',NULL,0);
/*!40000 ALTER TABLE `concept_name` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_name_tag`
--

DROP TABLE IF EXISTS `concept_name_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_name_tag` (
  `concept_name_tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(50) NOT NULL,
  `description` text NOT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_name_tag_id`),
  UNIQUE KEY `concept_name_tag_unique_tags` (`tag`),
  UNIQUE KEY `concept_name_tag_uuid_index` (`uuid`),
  KEY `user_who_created_name_tag` (`creator`),
  KEY `user_who_voided_name_tag` (`voided_by`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_name_tag`
--

LOCK TABLES `concept_name_tag` WRITE;
/*!40000 ALTER TABLE `concept_name_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_name_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_name_tag_map`
--

DROP TABLE IF EXISTS `concept_name_tag_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_name_tag_map` (
  `concept_name_id` int(11) NOT NULL,
  `concept_name_tag_id` int(11) NOT NULL,
  KEY `mapped_concept_name` (`concept_name_id`),
  KEY `mapped_concept_name_tag` (`concept_name_tag_id`),
  CONSTRAINT `mapped_concept_name_tag` FOREIGN KEY (`concept_name_tag_id`) REFERENCES `concept_name_tag` (`concept_name_tag_id`),
  CONSTRAINT `mapped_concept_name` FOREIGN KEY (`concept_name_id`) REFERENCES `concept_name` (`concept_name_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_name_tag_map`
--

LOCK TABLES `concept_name_tag_map` WRITE;
/*!40000 ALTER TABLE `concept_name_tag_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_name_tag_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_numeric`
--

DROP TABLE IF EXISTS `concept_numeric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_numeric` (
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `hi_absolute` double DEFAULT NULL,
  `hi_critical` double DEFAULT NULL,
  `hi_normal` double DEFAULT NULL,
  `low_absolute` double DEFAULT NULL,
  `low_critical` double DEFAULT NULL,
  `low_normal` double DEFAULT NULL,
  `units` varchar(50) DEFAULT NULL,
  `precise` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`concept_id`),
  CONSTRAINT `numeric_attributes` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_numeric`
--

LOCK TABLES `concept_numeric` WRITE;
/*!40000 ALTER TABLE `concept_numeric` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_numeric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_proposal`
--

DROP TABLE IF EXISTS `concept_proposal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_proposal` (
  `concept_proposal_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) DEFAULT NULL,
  `encounter_id` int(11) DEFAULT NULL,
  `original_text` varchar(255) NOT NULL DEFAULT '',
  `final_text` varchar(255) DEFAULT NULL,
  `obs_id` int(11) DEFAULT NULL,
  `obs_concept_id` int(11) DEFAULT NULL,
  `state` varchar(32) NOT NULL DEFAULT 'UNMAPPED',
  `comments` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `locale` varchar(50) NOT NULL DEFAULT '',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_proposal_id`),
  UNIQUE KEY `concept_proposal_uuid_index` (`uuid`),
  KEY `user_who_changed_proposal` (`changed_by`),
  KEY `concept_for_proposal` (`concept_id`),
  KEY `user_who_created_proposal` (`creator`),
  KEY `encounter_for_proposal` (`encounter_id`),
  KEY `proposal_obs_concept_id` (`obs_concept_id`),
  KEY `proposal_obs_id` (`obs_id`),
  CONSTRAINT `concept_for_proposal` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `encounter_for_proposal` FOREIGN KEY (`encounter_id`) REFERENCES `encounter` (`encounter_id`),
  CONSTRAINT `proposal_obs_concept_id` FOREIGN KEY (`obs_concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `proposal_obs_id` FOREIGN KEY (`obs_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `user_who_changed_proposal` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_proposal` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_proposal`
--

LOCK TABLES `concept_proposal` WRITE;
/*!40000 ALTER TABLE `concept_proposal` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_proposal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_proposal_tag_map`
--

DROP TABLE IF EXISTS `concept_proposal_tag_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_proposal_tag_map` (
  `concept_proposal_id` int(11) NOT NULL,
  `concept_name_tag_id` int(11) NOT NULL,
  KEY `mapped_concept_proposal_tag` (`concept_name_tag_id`),
  KEY `mapped_concept_proposal` (`concept_proposal_id`),
  CONSTRAINT `mapped_concept_proposal` FOREIGN KEY (`concept_proposal_id`) REFERENCES `concept_proposal` (`concept_proposal_id`),
  CONSTRAINT `mapped_concept_proposal_tag` FOREIGN KEY (`concept_name_tag_id`) REFERENCES `concept_name_tag` (`concept_name_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_proposal_tag_map`
--

LOCK TABLES `concept_proposal_tag_map` WRITE;
/*!40000 ALTER TABLE `concept_proposal_tag_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_proposal_tag_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_set`
--

DROP TABLE IF EXISTS `concept_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_set` (
  `concept_set_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `concept_set` int(11) NOT NULL DEFAULT '0',
  `sort_weight` double DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_set_id`),
  UNIQUE KEY `concept_set_uuid_index` (`uuid`),
  KEY `has_a` (`concept_set`),
  KEY `user_who_created` (`creator`),
  KEY `idx_concept_set_concept` (`concept_id`),
  CONSTRAINT `is_a` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `has_a` FOREIGN KEY (`concept_set`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_created` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_set`
--

LOCK TABLES `concept_set` WRITE;
/*!40000 ALTER TABLE `concept_set` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_set_derived`
--

DROP TABLE IF EXISTS `concept_set_derived`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_set_derived` (
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `concept_set` int(11) NOT NULL DEFAULT '0',
  `sort_weight` double DEFAULT NULL,
  PRIMARY KEY (`concept_id`,`concept_set`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_set_derived`
--

LOCK TABLES `concept_set_derived` WRITE;
/*!40000 ALTER TABLE `concept_set_derived` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_set_derived` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_source`
--

DROP TABLE IF EXISTS `concept_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_source` (
  `concept_source_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  `hl7_code` varchar(50) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` tinyint(1) NOT NULL,
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_source_id`),
  UNIQUE KEY `concept_source_uuid_index` (`uuid`),
  KEY `unique_hl7_code` (`hl7_code`,`retired`),
  KEY `concept_source_creator` (`creator`),
  KEY `user_who_retired_concept_source` (`retired_by`),
  CONSTRAINT `concept_source_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_concept_source` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_source`
--

LOCK TABLES `concept_source` WRITE;
/*!40000 ALTER TABLE `concept_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_state_conversion`
--

DROP TABLE IF EXISTS `concept_state_conversion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_state_conversion` (
  `concept_state_conversion_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) DEFAULT '0',
  `program_workflow_id` int(11) DEFAULT '0',
  `program_workflow_state_id` int(11) DEFAULT '0',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`concept_state_conversion_id`),
  UNIQUE KEY `concept_state_conversion_uuid_index` (`uuid`),
  UNIQUE KEY `unique_workflow_concept_in_conversion` (`program_workflow_id`,`concept_id`),
  KEY `concept_triggers_conversion` (`concept_id`),
  KEY `conversion_to_state` (`program_workflow_state_id`),
  CONSTRAINT `concept_triggers_conversion` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `conversion_involves_workflow` FOREIGN KEY (`program_workflow_id`) REFERENCES `program_workflow` (`program_workflow_id`),
  CONSTRAINT `conversion_to_state` FOREIGN KEY (`program_workflow_state_id`) REFERENCES `program_workflow_state` (`program_workflow_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_state_conversion`
--

LOCK TABLES `concept_state_conversion` WRITE;
/*!40000 ALTER TABLE `concept_state_conversion` DISABLE KEYS */;
/*!40000 ALTER TABLE `concept_state_conversion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `concept_word`
--

DROP TABLE IF EXISTS `concept_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concept_word` (
  `concept_word_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `word` varchar(50) NOT NULL DEFAULT '',
  `locale` varchar(20) NOT NULL DEFAULT '',
  `concept_name_id` int(11) NOT NULL,
  `weight` double DEFAULT '1',
  PRIMARY KEY (`concept_word_id`),
  KEY `word_in_concept_name` (`word`),
  KEY `concept_word_concept_idx` (`concept_id`),
  KEY `word_for_name` (`concept_name_id`),
  KEY `concept_word_weight_index` (`weight`),
  CONSTRAINT `word_for` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `word_for_name` FOREIGN KEY (`concept_name_id`) REFERENCES `concept_name` (`concept_name_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `concept_word`
--

LOCK TABLES `concept_word` WRITE;
/*!40000 ALTER TABLE `concept_word` DISABLE KEYS */;
INSERT INTO `concept_word` VALUES (21,1,'VERDADERO','es',9,8.45679012345679),(22,1,'VRAI','fr',5,10.5),(23,1,'VERDADEIRO','pt',3,8.31),(24,1,'S','it',2,15.15),(25,1,'OUI','fr',6,11.911111111111111),(26,1,'S','es',10,15.15),(27,1,'SIM','pt',4,11.911111111111111),(28,1,'YES','en',8,11.911111111111111),(29,1,'VERO','it',1,10.5),(30,1,'TRUE','en',7,10.5625),(31,2,'FAUX','fr',15,10.5),(32,2,'FALSE','en',17,9.780000000000001),(33,2,'FALSO','it',11,9.72),(34,2,'NO','it',12,15.15),(35,2,'NON','fr',16,11.911111111111111),(36,2,'NO','es',20,15.15),(37,2,'N?O','pt',14,11.911111111111111),(38,2,'FALSO','pt',13,9.72),(39,2,'NO','en',18,15.15),(40,2,'FALSO','es',19,9.72);
/*!40000 ALTER TABLE `concept_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug`
--

DROP TABLE IF EXISTS `drug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug` (
  `drug_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `combination` smallint(6) NOT NULL DEFAULT '0',
  `dosage_form` int(11) DEFAULT NULL,
  `dose_strength` double DEFAULT NULL,
  `maximum_daily_dose` double DEFAULT NULL,
  `minimum_daily_dose` double DEFAULT NULL,
  `route` int(11) DEFAULT NULL,
  `units` varchar(50) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`drug_id`),
  UNIQUE KEY `drug_uuid_index` (`uuid`),
  KEY `primary_drug_concept` (`concept_id`),
  KEY `drug_creator` (`creator`),
  KEY `drug_changed_by` (`changed_by`),
  KEY `dosage_form_concept` (`dosage_form`),
  KEY `drug_retired_by` (`retired_by`),
  KEY `route_concept` (`route`),
  CONSTRAINT `dosage_form_concept` FOREIGN KEY (`dosage_form`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `drug_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `drug_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `drug_retired_by` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `primary_drug_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `route_concept` FOREIGN KEY (`route`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug`
--

LOCK TABLES `drug` WRITE;
/*!40000 ALTER TABLE `drug` DISABLE KEYS */;
/*!40000 ALTER TABLE `drug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug_ingredient`
--

DROP TABLE IF EXISTS `drug_ingredient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_ingredient` (
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `ingredient_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ingredient_id`,`concept_id`),
  KEY `combination_drug` (`concept_id`),
  CONSTRAINT `ingredient` FOREIGN KEY (`ingredient_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `combination_drug` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug_ingredient`
--

LOCK TABLES `drug_ingredient` WRITE;
/*!40000 ALTER TABLE `drug_ingredient` DISABLE KEYS */;
/*!40000 ALTER TABLE `drug_ingredient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug_order`
--

DROP TABLE IF EXISTS `drug_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drug_order` (
  `order_id` int(11) NOT NULL DEFAULT '0',
  `drug_inventory_id` int(11) DEFAULT '0',
  `dose` double DEFAULT NULL,
  `equivalent_daily_dose` double DEFAULT NULL,
  `units` varchar(255) DEFAULT NULL,
  `frequency` varchar(255) DEFAULT NULL,
  `prn` smallint(6) NOT NULL DEFAULT '0',
  `complex` smallint(6) NOT NULL DEFAULT '0',
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `inventory_item` (`drug_inventory_id`),
  CONSTRAINT `extends_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `inventory_item` FOREIGN KEY (`drug_inventory_id`) REFERENCES `drug` (`drug_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug_order`
--

LOCK TABLES `drug_order` WRITE;
/*!40000 ALTER TABLE `drug_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `drug_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encounter`
--

DROP TABLE IF EXISTS `encounter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `encounter` (
  `encounter_id` int(11) NOT NULL AUTO_INCREMENT,
  `encounter_type` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `provider_id` int(11) NOT NULL DEFAULT '0',
  `location_id` int(11) DEFAULT NULL,
  `form_id` int(11) DEFAULT NULL,
  `encounter_datetime` datetime NOT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  PRIMARY KEY (`encounter_id`),
  UNIQUE KEY `encounter_uuid_index` (`uuid`),
  KEY `encounter_ibfk_1` (`creator`),
  KEY `encounter_type_id` (`encounter_type`),
  KEY `encounter_form` (`form_id`),
  KEY `encounter_location` (`location_id`),
  KEY `encounter_patient` (`patient_id`),
  KEY `user_who_voided_encounter` (`voided_by`),
  KEY `encounter_changed_by` (`changed_by`),
  KEY `encounter_provider` (`provider_id`),
  KEY `encounter_datetime_idx` (`encounter_datetime`),
  CONSTRAINT `encounter_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `encounter_form` FOREIGN KEY (`form_id`) REFERENCES `form` (`form_id`),
  CONSTRAINT `encounter_ibfk_1` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `encounter_location` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `encounter_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `encounter_provider` FOREIGN KEY (`provider_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `encounter_type_id` FOREIGN KEY (`encounter_type`) REFERENCES `encounter_type` (`encounter_type_id`),
  CONSTRAINT `user_who_voided_encounter` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encounter`
--

LOCK TABLES `encounter` WRITE;
/*!40000 ALTER TABLE `encounter` DISABLE KEYS */;
/*!40000 ALTER TABLE `encounter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encounter_type`
--

DROP TABLE IF EXISTS `encounter_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `encounter_type` (
  `encounter_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `description` text,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`encounter_type_id`),
  UNIQUE KEY `encounter_type_uuid_index` (`uuid`),
  KEY `retired_status` (`retired`),
  KEY `user_who_created_type` (`creator`),
  KEY `user_who_retired_encounter_type` (`retired_by`),
  CONSTRAINT `user_who_created_type` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_encounter_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encounter_type`
--

LOCK TABLES `encounter_type` WRITE;
/*!40000 ALTER TABLE `encounter_type` DISABLE KEYS */;
INSERT INTO `encounter_type` VALUES (1,'ADULTINITIAL','Outpatient Adult Initial Visit',1,'2005-02-24 00:00:00',0,NULL,NULL,NULL,'8d5b27bc-c2cc-11de-8d13-0010c6dffd0f'),(2,'ADULTRETURN','Outpatient Adult Return Visit',1,'2005-02-24 00:00:00',0,NULL,NULL,NULL,'8d5b2be0-c2cc-11de-8d13-0010c6dffd0f'),(3,'PEDSINITIAL','Outpatient Pediatric Initial Visit',1,'2005-02-24 00:00:00',0,NULL,NULL,NULL,'8d5b2dde-c2cc-11de-8d13-0010c6dffd0f'),(4,'PEDSRETURN','Outpatient Pediatric Return Visit',1,'2005-02-24 00:00:00',0,NULL,NULL,NULL,'8d5b3108-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `encounter_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field`
--

DROP TABLE IF EXISTS `field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field` (
  `field_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` text,
  `field_type` int(11) DEFAULT NULL,
  `concept_id` int(11) DEFAULT NULL,
  `table_name` varchar(50) DEFAULT NULL,
  `attribute_name` varchar(50) DEFAULT NULL,
  `default_value` text,
  `select_multiple` smallint(6) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`field_id`),
  UNIQUE KEY `field_uuid_index` (`uuid`),
  KEY `field_retired_status` (`retired`),
  KEY `user_who_changed_field` (`changed_by`),
  KEY `concept_for_field` (`concept_id`),
  KEY `user_who_created_field` (`creator`),
  KEY `type_of_field` (`field_type`),
  KEY `user_who_retired_field` (`retired_by`),
  CONSTRAINT `concept_for_field` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `type_of_field` FOREIGN KEY (`field_type`) REFERENCES `field_type` (`field_type_id`),
  CONSTRAINT `user_who_changed_field` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_field` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_field` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field`
--

LOCK TABLES `field` WRITE;
/*!40000 ALTER TABLE `field` DISABLE KEYS */;
/*!40000 ALTER TABLE `field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_answer`
--

DROP TABLE IF EXISTS `field_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_answer` (
  `field_id` int(11) NOT NULL DEFAULT '0',
  `answer_id` int(11) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`field_id`,`answer_id`),
  UNIQUE KEY `field_answer_uuid_index` (`uuid`),
  KEY `field_answer_concept` (`answer_id`),
  KEY `user_who_created_field_answer` (`creator`),
  CONSTRAINT `answers_for_field` FOREIGN KEY (`field_id`) REFERENCES `field` (`field_id`),
  CONSTRAINT `field_answer_concept` FOREIGN KEY (`answer_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_created_field_answer` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_answer`
--

LOCK TABLES `field_answer` WRITE;
/*!40000 ALTER TABLE `field_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `field_type`
--

DROP TABLE IF EXISTS `field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `field_type` (
  `field_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` longtext,
  `is_set` smallint(6) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`field_type_id`),
  UNIQUE KEY `field_type_uuid_index` (`uuid`),
  KEY `user_who_created_field_type` (`creator`),
  CONSTRAINT `user_who_created_field_type` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_type`
--

LOCK TABLES `field_type` WRITE;
/*!40000 ALTER TABLE `field_type` DISABLE KEYS */;
INSERT INTO `field_type` VALUES (1,'Concept','',0,1,'2005-02-22 00:00:00','8d5e7d7c-c2cc-11de-8d13-0010c6dffd0f'),(2,'Database element','',0,1,'2005-02-22 00:00:00','8d5e8196-c2cc-11de-8d13-0010c6dffd0f'),(3,'Set of Concepts','',1,1,'2005-02-22 00:00:00','8d5e836c-c2cc-11de-8d13-0010c6dffd0f'),(4,'Miscellaneous Set','',1,1,'2005-02-22 00:00:00','8d5e852e-c2cc-11de-8d13-0010c6dffd0f'),(5,'Section','',1,1,'2005-02-22 00:00:00','8d5e86fa-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `field_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form`
--

DROP TABLE IF EXISTS `form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form` (
  `form_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `version` varchar(50) NOT NULL DEFAULT '',
  `build` int(11) DEFAULT NULL,
  `published` smallint(6) NOT NULL DEFAULT '0',
  `description` text,
  `encounter_type` int(11) DEFAULT NULL,
  `template` mediumtext,
  `xslt` mediumtext,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retired_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`form_id`),
  UNIQUE KEY `form_uuid_index` (`uuid`),
  KEY `user_who_last_changed_form` (`changed_by`),
  KEY `user_who_created_form` (`creator`),
  KEY `form_encounter_type` (`encounter_type`),
  KEY `user_who_retired_form` (`retired_by`),
  KEY `form_published_index` (`published`),
  KEY `form_retired_index` (`retired`),
  KEY `form_published_and_retired_index` (`published`,`retired`),
  CONSTRAINT `form_encounter_type` FOREIGN KEY (`encounter_type`) REFERENCES `encounter_type` (`encounter_type_id`),
  CONSTRAINT `user_who_created_form` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_last_changed_form` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_form` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form`
--

LOCK TABLES `form` WRITE;
/*!40000 ALTER TABLE `form` DISABLE KEYS */;
/*!40000 ALTER TABLE `form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `form_field`
--

DROP TABLE IF EXISTS `form_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `form_field` (
  `form_field_id` int(11) NOT NULL AUTO_INCREMENT,
  `form_id` int(11) NOT NULL DEFAULT '0',
  `field_id` int(11) NOT NULL DEFAULT '0',
  `field_number` int(11) DEFAULT NULL,
  `field_part` varchar(5) DEFAULT NULL,
  `page_number` int(11) DEFAULT NULL,
  `parent_form_field` int(11) DEFAULT NULL,
  `min_occurs` int(11) DEFAULT NULL,
  `max_occurs` int(11) DEFAULT NULL,
  `required` smallint(6) NOT NULL DEFAULT '0',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `sort_weight` float(11,5) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`form_field_id`),
  UNIQUE KEY `form_field_uuid_index` (`uuid`),
  KEY `user_who_last_changed_form_field` (`changed_by`),
  KEY `user_who_created_form_field` (`creator`),
  KEY `field_within_form` (`field_id`),
  KEY `form_containing_field` (`form_id`),
  KEY `form_field_hierarchy` (`parent_form_field`),
  CONSTRAINT `field_within_form` FOREIGN KEY (`field_id`) REFERENCES `field` (`field_id`),
  CONSTRAINT `form_containing_field` FOREIGN KEY (`form_id`) REFERENCES `form` (`form_id`),
  CONSTRAINT `form_field_hierarchy` FOREIGN KEY (`parent_form_field`) REFERENCES `form_field` (`form_field_id`),
  CONSTRAINT `user_who_created_form_field` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_last_changed_form_field` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `form_field`
--

LOCK TABLES `form_field` WRITE;
/*!40000 ALTER TABLE `form_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `global_property`
--

DROP TABLE IF EXISTS `global_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_property` (
  `property` varbinary(255) NOT NULL DEFAULT '',
  `property_value` mediumtext,
  `description` text,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`property`),
  UNIQUE KEY `global_property_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_property`
--

LOCK TABLES `global_property` WRITE;
/*!40000 ALTER TABLE `global_property` DISABLE KEYS */;
INSERT INTO `global_property` VALUES ('FormEntry.enableDashboardTab','true','true/false whether or not to show a Form Entry tab on the patient dashboard','1449d349-f8bd-4e1f-945d-e088f0e4600f'),('FormEntry.enableOnEncounterTab','false','true/false whether or not to show a Enter Form button on the encounters tab of the patient dashboard','ec4aff0f-5591-4cad-983f-66d44f1ceb1f'),('concept.caseSensitiveNamesInConceptNameTable','true','Indicates whether names in the concept_name table are case sensitive or not. Setting this to false for MySQL with a case insensitive collation improves search performance.','d9f93f0f-01d9-49b8-af16-b7534a6eb06c'),('concept.causeOfDeath','5002','Concept id of the concept defining the CAUSE OF DEATH concept','c4e2bf59-bbf1-413c-a94b-b181bf0d1ee0'),('concept.cd4_count','5497','Concept id of the concept defining the CD4 count concept','a3f0c460-70d7-4263-9e8e-6d832dd1fc96'),('concept.false','2','Concept id of the concept defining the FALSE boolean concept','476a3ad9-55b1-40f6-9d3a-4d6479e72f06'),('concept.height','5090','Concept id of the concept defining the HEIGHT concept','8a4f3721-083a-4f8e-8e14-bab1c5b466cb'),('concept.medicalRecordObservations','1238','The concept id of the MEDICAL_RECORD_OBSERVATIONS concept.  This concept_id is presumed to be the generic grouping (obr) concept in hl7 messages.  An obs_group row is not created for this concept.','9209f67c-e4e2-48d4-a38c-1f827e5d4bb6'),('concept.none','1107','Concept id of the concept defining the NONE concept','9dce8a8b-89d5-4150-908e-296d0bb11753'),('concept.otherNonCoded','5622','Concept id of the concept defining the OTHER NON-CODED concept','c3ca25ca-7bb5-4353-b386-957778eb111b'),('concept.patientDied','1742','Concept id of the concept defining the PATIENT DIED concept','4fff6786-1275-43c8-bc3d-58084763890a'),('concept.problemList','1284','The concept id of the PROBLEM LIST concept.  This concept_id is presumed to be the generic grouping (obr) concept in hl7 messages.  An obs_group row is not created for this concept.','a36d0a39-44f0-4016-8b43-066412172bbd'),('concept.reasonExitedCare','','Concept id of the concept defining the REASON EXITED CARE concept','fa507d4b-77e3-44e0-9353-90f38cfdad7c'),('concept.reasonOrderStopped','1812','Concept id of the concept defining the REASON ORDER STOPPED concept','cf09c431-cb26-4f9f-8586-8496068e168f'),('concept.true','1','Concept id of the concept defining the TRUE boolean concept','5dc59829-b2a5-463d-8995-c8fa999d5fda'),('concept.weight','5089','Concept id of the concept defining the WEIGHT concept','705bacc5-04ef-45cd-82c7-98a68af1384b'),('conceptDrug.dosageForm.conceptClasses','','A comma-separated list of the allowed concept classes for the dosage form field of the concept drug management form.','c1436a60-9474-46bd-92ae-330d4794425c'),('conceptDrug.route.conceptClasses','','A comma-separated list of the allowed concept classes for the route field of the concept drug management form.','6aac5b96-f658-4ecf-b5b6-eee6cc2d8870'),('concepts.locked','false','true/false whether or not concepts can be edited in this database.','1b9caf04-7346-4452-ad68-526ffcc89373'),('dashboard.encounters.showEditLink','true','true/false whether or not to show the \'Edit Encounter\' link on the patient dashboard','045d4928-a606-4caa-bde8-5f491abe7b0b'),('dashboard.encounters.showEmptyFields','true','true/false whether or not to show empty fields on the \'View Encounter\' window','80c056d2-a3cf-4a97-94d5-ebd9d6b82106'),('dashboard.encounters.showViewLink','true','true/false whether or not to show the \'View Encounter\' link on the patient dashboard','502e0213-c7fb-4299-bc7d-2c71e99610e4'),('dashboard.encounters.usePages','smart','true/false/smart on how to show the pages on the \'View Encounter\' window.  \'smart\' means that if > 50% of the fields have page numbers defined, show data in pages','05046d1f-a9df-4fc3-8d1d-bf6eaab71b70'),('dashboard.header.programs_to_show','','List of programs to show Enrollment details of in the patient header. (Should be an ordered comma-separated list of program_ids or names.)','9a386923-e766-4898-b907-c69cef2f01cd'),('dashboard.header.workflows_to_show','','List of programs to show Enrollment details of in the patient header. List of workflows to show current status of in the patient header. These will only be displayed if they belong to a program listed above. (Should be a comma-separated list of program_workflow_ids.)','c376b595-28fb-4245-9d7f-cbbaacd8babf'),('dashboard.overview.showConcepts','','Comma delimited list of concepts ids to show on the patient dashboard overview tab','5ec5f431-d723-485b-8848-ce9d85fe8799'),('dashboard.regimen.displayDrugSetIds','ANTIRETROVIRAL DRUGS,TUBERCULOSIS TREATMENT DRUGS','Drug sets that appear on the Patient Dashboard Regimen tab. Comma separated list of name of concepts that are defined as drug sets.','fd69cf94-592d-4eb2-a0ee-cd672d19f3e9'),('dashboard.regimen.displayFrequencies','7 days/week,6 days/week,5 days/week,4 days/week,3 days/week,2 days/week,1 days/week','Frequency of a drug order that appear on the Patient Dashboard. Comma separated list of name of concepts that are defined as drug frequencies.','653ef257-670d-44b6-b742-2d6828a4ccad'),('dashboard.regimen.standardRegimens','<list>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>2</drugId>        <dose>1</dose>        <units>tab(s)</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>    </drugComponents>    <displayName>3TC + d4T(30) + NVP (Triomune-30)</displayName>    <codeName>standardTri30</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>3</drugId>        <dose>1</dose>        <units>tab(s)</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>    </drugComponents>    <displayName>3TC + d4T(40) + NVP (Triomune-40)</displayName>    <codeName>standardTri40</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>39</drugId>        <dose>1</dose>        <units>tab(s)</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>      <drugSuggestion>        <drugId>22</drugId>        <dose>200</dose>        <units>mg</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>    </drugComponents>    <displayName>AZT + 3TC + NVP</displayName>    <codeName>standardAztNvp</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion reference=\"../../../regimenSuggestion[3]/drugComponents/drugSuggestion\"/>      <drugSuggestion>        <drugId>11</drugId>        <dose>600</dose>        <units>mg</units>        <frequency>1/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>    </drugComponents>    <displayName>AZT + 3TC + EFV(600)</displayName>    <codeName>standardAztEfv</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>5</drugId>        <dose>30</dose>        <units>mg</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>      <drugSuggestion>        <drugId>42</drugId>        <dose>150</dose>        		<units>mg</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>      <drugSuggestion reference=\"../../../regimenSuggestion[4]/drugComponents/drugSuggestion[2]\"/>    </drugComponents>    <displayName>d4T(30) + 3TC + EFV(600)</displayName>    <codeName>standardD4t30Efv</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion>  <regimenSuggestion>    <drugComponents>      <drugSuggestion>        <drugId>6</drugId>        <dose>40</dose>        <units>mg</units>        <frequency>2/day x 7 days/week</frequency>        <instructions></instructions>      </drugSuggestion>      <drugSuggestion reference=\"../../../regimenSuggestion[5]/drugComponents/drugSuggestion[2]\"/>      <drugSuggestion reference=\"../../../regimenSuggestion[4]/drugComponents/drugSuggestion[2]\"/>    </drugComponents>    <displayName>d4T(40) + 3TC + EFV(600)</displayName>    <codeName>standardD4t40Efv</codeName>    <canReplace>ANTIRETROVIRAL DRUGS</canReplace>  </regimenSuggestion></list>','XML description of standard drug regimens, to be shown as shortcuts on the dashboard regimen entry tab','8c836918-c569-49d8-a60f-9c614408bb78'),('dashboard.relationships.show_types','','Types of relationships separated by commas.  Doctor/Patient,Parent/Child','81bb71cf-acdb-4b26-9861-f77f7ef8c687'),('dashboard.showPatientName','false','Whether or not to display the patient name in the patient dashboard title. Note that enabling this could be security risk if multiple users operate on the same computer.','a1d81cb8-288c-49b1-862e-72fc8fb52abb'),('default_locale','en_GB','Specifies the default locale. You can specify both the language code(ISO-639) and the country code(ISO-3166), e.g. \'en_GB\' or just country: e.g. \'en\'','6f85283c-6661-46af-97c8-12e503a8813e'),('default_location','Unknown Location','The name of the location to use as a system default','8e5e2f84-b79c-4086-abab-eecad0d2e370'),('default_theme','','Default theme for users.  OpenMRS ships with themes of \'green\', \'orange\', \'purple\', and \'legacy\'','a8b09019-e6e0-4a00-8c01-8a319d75879b'),('encounterForm.obsSortOrder','number','The sort order for the obs listed on the encounter edit form.  \'number\' sorts on the associated numbering from the form schema.  \'weight\' sorts on the order displayed in the form schema.','49379a0d-0dfe-471c-89d9-d490eb19a227'),('graph.color.absolute','rgb(20,20,20)','Color of the \'invalid\' section of numeric graphs on the patient dashboard.','3bc0315a-5ce6-4e7e-bd4b-089d5ec93118'),('graph.color.critical','rgb(200,0,0)','Color of the \'critical\' section of numeric graphs on the patient dashboard.','9a598961-2cea-4f5f-8c2a-5e6a5a873925'),('graph.color.normal','rgb(255,126,0)','Color of the \'normal\' section of numeric graphs on the patient dashboard.','ebc40f95-0ed9-4da8-977f-f11257b3f29c'),('gzip.enabled','false','Set to \'true\' to turn on OpenMRS\'s gzip filter, and have the webapp compress data before sending it to any client that supports it. Generally use this if you are running Tomcat standalone. If you are running Tomcat behind Apache, then you\'d want to use Apache to do gzip compression.','49468f25-0808-47ec-8bdb-995ee36d269e'),('hl7_archive.dir','hl7_archives','The default name or absolute path for the folder where to write the hl7_in_archives.','26e5f994-ed89-443c-96a7-b43bb19deb8b'),('hl7_processor.ignore_missing_patient_non_local','false','If true, hl7 messages for patients that are not found and are non-local will silently be dropped/ignored','ccfc429a-8d55-4506-96c5-d9bb8575f54f'),('layout.address.format','general','Format in which to display the person addresses.  Valid values are general, kenya, rwanda, usa, and lesotho','f0acb653-25fc-472d-a558-5f767b148927'),('layout.name.format','short','Format in which to display the person names.  Valid values are short, long','0c867dd4-67ad-41d9-8c7e-5b8471340c8d'),('locale.allowed.list','en, es, fr, it, pt','Comma delimited list of locales allowed for use on system','d89561db-8468-43bd-a664-977c046fb7bf'),('location.field.style','default','Type of widget to use for location fields','2cb70cd7-2212-444b-ae68-a00af40b807e'),('log.level.openmrs','info','log level used by the logger \'org.openmrs\'. This value will override the log4j.xml value. Valid values are trace, debug, info, warn, error or fatal','d728bc7e-0e9e-4023-bb64-dbfd5dd2bc13'),('logic.database_version','1.4','DO NOT MODIFY.  Current database version number for the logic module.','7f8ff6a2-2038-415d-88ab-87b83f11a92c'),('logic.default.ruleClassDirectory','logic/class','Default folder where compiled rule will be stored','6ad4e9ae-3127-4967-90b5-202dee7d7cf5'),('logic.default.ruleJavaDirectory','logic/sources','Default folder where rule\'s java file will be stored','3c1e3bf4-3c06-4228-bb5d-59ce759579ea'),('logic.defaultTokens.conceptClasses','','When registering default tokens for logic, if you specify a comma-separated list of concept class names here, only concepts of those classes will have tokens registered. If you leave this blank, all classes will have tokens registered for their concepts.','8bded147-04a6-496f-9537-a2bc7fe39db7'),('logic.mandatory','false','true/false whether or not the logic module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','33ca756a-2f8f-424a-81bf-643b28db6152'),('logic.started','true','DO NOT MODIFY. true/false whether or not the logic module has been started.  This is used to make sure modules that were running  prior to a restart are started again','1f1dab25-4e61-4bb3-bcf9-7c1fbae2aab0'),('mail.debug','false','true/false whether to print debugging information during mailing','406d7d28-978a-417a-8c79-de808c069d30'),('mail.default_content_type','text/plain','Content type to append to the mail messages','88d82d41-cb97-4b6d-8aa6-7e1f9b2b35a1'),('mail.from','info@openmrs.org','Email address to use as the default from address','48d37b42-0b3d-4735-ac3c-a402dab82e04'),('mail.password','test','Password for the SMTP user (if smtp_auth is enabled)','4f7e6a19-a582-45f6-8257-59ab031bec42'),('mail.smtp_auth','false','true/false whether the smtp host requires authentication','baf48ce8-57da-434d-a1b7-4205b0a09960'),('mail.smtp_host','localhost','SMTP host name','cbbcda65-031c-43f6-991b-6620c22d4478'),('mail.smtp_port','25','SMTP port','9c2bcae1-f7cd-456c-8f80-af103c3747b6'),('mail.transport_protocol','smtp','Transport protocol for the messaging engine. Valid values: smtp','f8b1f3ff-dd46-4ed0-9370-95672d9cf371'),('mail.user','test','Username of the SMTP user (if smtp_auth is enabled)','4a157f90-d5d7-48ee-b479-0d94e796d722'),('minSearchCharacters','3','Number of characters user must input before searching is started.','9111bd8d-57b7-44e0-8d96-f6c120b1d0f4'),('module_repository_folder','modules','Name of the folder in which to store the modules','dc366192-a2ac-4106-abee-ab3150ef221b'),('newPatientForm.relationships','','Comma separated list of the RelationshipTypes to show on the new/short patient form.  The list is defined like \'3a, 4b, 7a\'.  The number is the RelationshipTypeId and the \'a\' vs \'b\' part is which side of the relationship is filled in by the user.','863cb198-2970-40c3-98fa-c700f39b85a8'),('new_patient_form.showRelationships','false','true/false whether or not to show the relationship editor on the addPatient.htm screen','c94c55b0-3af6-496b-b5f7-3ebb1ba3d28b'),('obs.complex_obs_dir','complex_obs','Default directory for storing complex obs.','ad851f34-5fc2-4c5a-8c47-fc0d24793d22'),('patient.defaultPatientIdentifierValidator','org.openmrs.patient.impl.LuhnIdentifierValidator','This property sets the default patient identifier validator.  The default validator is only used in a handful of (mostly legacy) instances.  For example, it\'s used to generate the isValidCheckDigit calculated column and to append the string \"(default)\" to the name of the default validator on the editPatientIdentifierType form.','9a336c38-d785-4a07-bad0-e88334adb1dd'),('patient.headerAttributeTypes','','A comma delimited list of PersonAttributeType names that will be shown on the patient dashboard','44d8f4fb-9b3d-423f-b71a-4cd7624e7e5a'),('patient.identifierPrefix','','This property is only used if patient.identifierRegex is empty.  The string here is prepended to the sql indentifier search string.  The sql becomes \"... where identifier like \'<PREFIX><QUERY STRING><SUFFIX>\';\".  Typically this value is either a percent sign (%) or empty.','d52cde8a-da36-4127-8e68-3132c44a4e33'),('patient.identifierRegex','','WARNING: Using this search property can cause a drop in mysql performance with large patient sets.  A MySQL regular expression for the patient identifier search strings.  The @SEARCH@ string is replaced at runtime with the user\'s search string.  An empty regex will cause a simply \'like\' sql search to be used. Example: ^0*@SEARCH@([A-Z]+-[0-9])?$','deec6903-43ef-43f7-bb90-8a26ffd2f9b7'),('patient.identifierSearchPattern','','If this is empty, the regex or suffix/prefix search is used.  Comma separated list of identifiers to check.  Allows for faster searching of multiple options rather than the slow regex. e.g. @SEARCH@,0@SEARCH@,@SEARCH-1@-@CHECKDIGIT@,0@SEARCH-1@-@CHECKDIGIT@ would turn a request for \"4127\" into a search for \"in (\'4127\',\'04127\',\'412-7\',\'0412-7\')\"','5a96e044-08fe-44d5-ae46-f47a212d0156'),('patient.identifierSuffix','','This property is only used if patient.identifierRegex is empty.  The string here is prepended to the sql indentifier search string.  The sql becomes \"... where identifier like \'<PREFIX><QUERY STRING><SUFFIX>\';\".  Typically this value is either a percent sign (%) or empty.','4ca00b30-6a27-40ef-9f28-93f8350cc617'),('patient.listingAttributeTypes','','A comma delimited list of PersonAttributeType names that should be displayed for patients in _lists_','fa760dd5-4743-4b23-997c-bd6575888bf2'),('patient.viewingAttributeTypes','','A comma delimited list of PersonAttributeType names that should be displayed for patients when _viewing individually_','2bff9707-da93-4da1-8f67-497cadb5329a'),('patient_identifier.importantTypes','','A comma delimited list of PatientIdentifier names : PatientIdentifier locations that will be displayed on the patient dashboard.  E.g.: TRACnet ID:Rwanda,ELDID:Kenya','c9fef9f7-c09c-40b8-9e00-fd76576aec70'),('person.searchMaxResults','1000','The maximum number of results returned by patient searches','557b3cbe-42a9-40fc-8708-1ff086f8b26b'),('report.xmlMacros','','Macros that will be applied to Report Schema XMLs when they are interpreted. This should be java.util.properties format.','fc131165-5576-4c36-ba30-e6408bddea2e'),('reportProblem.url','http://errors.openmrs.org/scrap','The openmrs url where to submit bug reports','a0ae90ab-8ee0-4fe7-8d06-5a400af5fbd6'),('scheduler.password','test','Password for the OpenMRS user that will perform the scheduler activities','48ace22e-e105-40dc-8218-53dd5add8405'),('scheduler.username','admin','Username for the OpenMRS user that will perform the scheduler activities','f9c0663d-7121-4efe-9fcf-a88c7a222c49'),('searchWidget.batchSize','200','The maximum number of search results that are returned by an ajax call','2874b4ee-6d7e-44bd-bb21-e42a4c5edf64'),('searchWidget.runInSerialMode','true','Specifies whether the search widgets should make ajax requests in serial or parallel order, a value of true is appropriate for implementations running on a slow network connection and vice versa','8432c0a5-d570-454a-916d-17cebd84bffb'),('searchWidget.searchDelayInterval','400','Specifies time interval in milliseconds when searching, between keyboard keyup event and triggering the search off, should be higher if most users are slow when typing so as to minimise the load on the server','a9fad323-cdf8-4d02-984f-22bebea45644'),('security.passwordCannotMatchUsername','true','Configure whether passwords must not match user\'s username or system id','a6d76ac3-dc2f-4b13-ac51-d4e2415fdb11'),('security.passwordCustomRegex','','Configure a custom regular expression that a password must match','1dc9c3aa-094b-48e1-a8de-4872e3030f73'),('security.passwordMinimumLength','8','Configure the minimum length required of all passwords','f68295cf-5fcd-4b00-ba32-6351f256cfdc'),('security.passwordRequiresDigit','true','Configure whether passwords must contain at least one digit','40adecaa-f664-4bf1-a9cc-03990ed4c6c6'),('security.passwordRequiresNonDigit','true','Configure whether passwords must contain at least one non-digit','cffa151e-75d5-409e-9c21-52289c7355ac'),('security.passwordRequiresUpperAndLowerCase','true','Configure whether passwords must contain both upper and lower case characters','00984818-c404-4075-8c7a-c32b24cab9e6'),('use_patient_attribute.healthCenter','false','Indicates whether or not the \'health center\' attribute is shown when viewing/searching for patients','c2819bcc-6622-49bb-8910-6e79147e201a'),('use_patient_attribute.mothersName','false','Indicates whether or not mother\'s name is able to be added/viewed for a patient','c3b295a1-adc3-441c-a796-431102350b4c'),('user.headerAttributeTypes','','A comma delimited list of PersonAttributeType names that will be shown on the user dashboard. (not used in v1.5)','efc98353-6645-47d7-8b7c-874dd16c5d9c'),('user.listingAttributeTypes','','A comma delimited list of PersonAttributeType names that should be displayed for users in _lists_','661f2ff1-f781-47a3-8c0b-0b9114b395cc'),('user.viewingAttributeTypes','','A comma delimited list of PersonAttributeType names that should be displayed for users when _viewing individually_','c3bf3501-be9e-4d15-a8a8-32ad5f04c0c1'),('webservices.rest.allowedips','','A comma-separate list of IP addresses that are allowed to access the web services. An empty string allows everyone to access all ws. \n        IPs can be declared with bit masks e.g. 10.0.0.0/30 matches 10.0.0.0 - 10.0.0.3 and 10.0.0.0/24 matches 10.0.0.0 - 10.0.0.255.','f7f086c8-ee66-42f1-95ec-bb3d2aadf192'),('webservices.rest.mandatory','false','true/false whether or not the webservices.rest module MUST start when openmrs starts.  This is used to make sure that mission critical modules are always running if openmrs is running.','6078f825-a065-45cb-8b98-476b71560e53'),('webservices.rest.maxResultsAbsolute','100','The absolute max results limit. If the client requests a larger number of results, then will get an error','d624b9f7-6029-405f-b102-1a194745f746'),('webservices.rest.maxResultsDefault','50','The default max results limit if the user does not provide a maximum when making the web service call.','a908feee-447d-4213-9c13-f0beb8746be3'),('webservices.rest.started','true','DO NOT MODIFY. true/false whether or not the webservices.rest module has been started.  This is used to make sure modules that were running  prior to a restart are started again','fdcb8f56-53dd-4fd0-83a0-2d2198ce9ddd'),('webservices.rest.uriPrefix','http://localhost:8081/openmrs','The URI prefix through which clients consuming web services will connect to the web application, should be of the form http://{ipAddress}:{port}/{contextPath}','56100459-224e-4b8a-9f4a-f50d750c4437');
/*!40000 ALTER TABLE `global_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hl7_in_archive`
--

DROP TABLE IF EXISTS `hl7_in_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hl7_in_archive` (
  `hl7_in_archive_id` int(11) NOT NULL AUTO_INCREMENT,
  `hl7_source` int(11) NOT NULL DEFAULT '0',
  `hl7_source_key` varchar(255) DEFAULT NULL,
  `hl7_data` mediumtext NOT NULL,
  `date_created` datetime NOT NULL,
  `message_state` int(1) DEFAULT '2',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`hl7_in_archive_id`),
  UNIQUE KEY `hl7_in_archive_uuid_index` (`uuid`),
  KEY `hl7_in_archive_message_state_idx` (`message_state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hl7_in_archive`
--

LOCK TABLES `hl7_in_archive` WRITE;
/*!40000 ALTER TABLE `hl7_in_archive` DISABLE KEYS */;
/*!40000 ALTER TABLE `hl7_in_archive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hl7_in_error`
--

DROP TABLE IF EXISTS `hl7_in_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hl7_in_error` (
  `hl7_in_error_id` int(11) NOT NULL AUTO_INCREMENT,
  `hl7_source` int(11) NOT NULL DEFAULT '0',
  `hl7_source_key` text,
  `hl7_data` mediumtext NOT NULL,
  `error` varchar(255) NOT NULL DEFAULT '',
  `error_details` mediumtext,
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`hl7_in_error_id`),
  UNIQUE KEY `hl7_in_error_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hl7_in_error`
--

LOCK TABLES `hl7_in_error` WRITE;
/*!40000 ALTER TABLE `hl7_in_error` DISABLE KEYS */;
/*!40000 ALTER TABLE `hl7_in_error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hl7_in_queue`
--

DROP TABLE IF EXISTS `hl7_in_queue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hl7_in_queue` (
  `hl7_in_queue_id` int(11) NOT NULL AUTO_INCREMENT,
  `hl7_source` int(11) NOT NULL DEFAULT '0',
  `hl7_source_key` text,
  `hl7_data` mediumtext NOT NULL,
  `message_state` int(1) NOT NULL DEFAULT '0',
  `date_processed` datetime DEFAULT NULL,
  `error_msg` text,
  `date_created` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`hl7_in_queue_id`),
  UNIQUE KEY `hl7_in_queue_uuid_index` (`uuid`),
  KEY `hl7_source` (`hl7_source`),
  CONSTRAINT `hl7_source` FOREIGN KEY (`hl7_source`) REFERENCES `hl7_source` (`hl7_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hl7_in_queue`
--

LOCK TABLES `hl7_in_queue` WRITE;
/*!40000 ALTER TABLE `hl7_in_queue` DISABLE KEYS */;
/*!40000 ALTER TABLE `hl7_in_queue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hl7_source`
--

DROP TABLE IF EXISTS `hl7_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hl7_source` (
  `hl7_source_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` text,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`hl7_source_id`),
  UNIQUE KEY `hl7_source_uuid_index` (`uuid`),
  KEY `creator` (`creator`),
  CONSTRAINT `creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hl7_source`
--

LOCK TABLES `hl7_source` WRITE;
/*!40000 ALTER TABLE `hl7_source` DISABLE KEYS */;
INSERT INTO `hl7_source` VALUES (1,'LOCAL','',1,'2006-09-01 00:00:00','8d6b8bb6-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `hl7_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liquibasechangelog`
--

DROP TABLE IF EXISTS `liquibasechangelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `liquibasechangelog` (
  `ID` varchar(63) NOT NULL,
  `AUTHOR` varchar(63) NOT NULL,
  `FILENAME` varchar(200) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `MD5SUM` varchar(32) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ID`,`AUTHOR`,`FILENAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liquibasechangelog`
--

LOCK TABLES `liquibasechangelog` WRITE;
/*!40000 ALTER TABLE `liquibasechangelog` DISABLE KEYS */;
INSERT INTO `liquibasechangelog` VALUES ('0','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:47','112fa925e7ce223f9dfc1c841176b4c','Custom Change','Run the old sqldiff file to get database up to the 1.4.0.20 version if needed. (Requires \'mysql\' to be on the PATH)',NULL,'1.9.4'),('02232009-1141','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:16:58','aa7af8d8a609c61fd504e6121b8feda','Modify Column','Modify the password column to fit the output of SHA-512 function',NULL,'1.9.4'),('1','upul','liquibase-update-to-latest.xml','2013-02-06 12:16:47','aeb2c081fe662e3375a02ea34696492','Add Column','Add the column to person_attribute type to connect each type to a privilege',NULL,'1.9.4'),('1226348923233-12','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','1ef82a3a056d8fe77c51789ee3e270','Insert Row (x11)','',NULL,'1.9.4'),('1226348923233-13','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','1fbe86152e13998b4e9dfa64f8f99e1','Insert Row (x2)','',NULL,'1.9.4'),('1226348923233-14','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','37d7a09a3680241c2cd870bf875f7679','Insert Row (x4)','',NULL,'1.9.4'),('1226348923233-15','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','6f5fdf6fe4573d335b764e8c3f6dec9','Insert Row (x15)','',NULL,'1.9.4'),('1226348923233-16','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','e3fb7531421d36297f9b551aa14eed3','Insert Row','',NULL,'1.9.4'),('1226348923233-17','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','2c3a40aea75302fa5eda34e68f0b5a8','Insert Row (x2)','',NULL,'1.9.4'),('1226348923233-18','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','a077f3e88c77b1bcfd024568ce97a57','Insert Row (x2)','',NULL,'1.9.4'),('1226348923233-2','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','17915c6b808f125502a8832f191896a','Insert Row (x5)','',NULL,'1.9.4'),('1226348923233-20','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','74d4c95919a87ad385f4678c05befeb','Insert Row','',NULL,'1.9.4'),('1226348923233-21','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','cad8f9efce142229ad5030139ae2ee62','Insert Row','',NULL,'1.9.4'),('1226348923233-22','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','98e8afcf7f83f1f2fe6ae566ae71b','Insert Row','',NULL,'1.9.4'),('1226348923233-23','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','877ae775e48051291b94467caebdbf9','Insert Row','',NULL,'1.9.4'),('1226348923233-5','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','e69c9cbc8e906c10b7b19ce95c6fbb','Insert Row','',NULL,'1.9.4'),('1226348923233-6','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','afbc17f0c1c778754be371db868719f','Insert Row (x14)','',NULL,'1.9.4'),('1226348923233-8','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','52afbf2cc39cde1fed9c97b8886ef','Insert Row (x7)','',NULL,'1.9.4'),('1226348923233-9','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','f8de838176dd923fe06ff3346fd2e9c7','Insert Row (x4)','',NULL,'1.9.4'),('1226412230538-9a','ben (generated)','liquibase-core-data.xml','2013-02-06 12:16:47','d19735782014e65d28267d83a681fac','Insert Row (x4)','',NULL,'1.9.4'),('1227123456789-100','dkayiwa','liquibase-schema-only.xml','2013-02-06 12:16:44','a831ffc27afdc2192e9417c863988461','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-1','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','4b1d6f6458503aad8cf53c8583648d1','Create Table','',NULL,'1.9.4'),('1227303685425-10','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','9021e2e5ee4767d742f622d84ace8bf','Create Table','',NULL,'1.9.4'),('1227303685425-100','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','c4f0efe639c65a0ca719a7672c9ae99','Create Index','',NULL,'1.9.4'),('1227303685425-101','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','ec489290e24b9bc8e7cefa4cb193a5','Create Index','',NULL,'1.9.4'),('1227303685425-102','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','3fffc270441f9846be7f6f15b9186fe','Create Index','',NULL,'1.9.4'),('1227303685425-103','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','5bca419b6ce805277a7f1e853492965','Create Index','',NULL,'1.9.4'),('1227303685425-104','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','f5921bfce0567fad9dbebf4a464b7b8','Create Index','',NULL,'1.9.4'),('1227303685425-105','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','a8e830de2d9b5b30b5cee4f96e3d4f','Create Index','',NULL,'1.9.4'),('1227303685425-106','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','7e434b23ab45109451cc494fc7b66831','Create Index','',NULL,'1.9.4'),('1227303685425-107','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','347132fbd16c8712058e1f95765ce26','Create Index','',NULL,'1.9.4'),('1227303685425-108','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','5b4184878459e4fc9341a43b4be64ef','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-109','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','c2bfb4e612aa1973f1c365ad7118f342','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-11','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','b8eedfaa55dc9af171902334f4902e','Create Table','',NULL,'1.9.4'),('1227303685425-110','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','d6659d52c5bcac8b59fb8ba1a37f86','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-111','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','f995cd7d34245c41d575977cf271ccc6','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-112','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','83c9df4fa7c9a7d84cdd930ff59776','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-113','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','581e495dd2e0605b389c1d497388786f','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-114','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','833048d0b4aae48cec412ca39b7b742','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-115','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','a08b4c2b2cced32d89b1d4b31b97458','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-116','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','eeec2cd3e6dddfebf775011fb81bd94','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-117','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','9f80b1cfa564da653099bec7178a435','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-118','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','1e5f598cefabaaa8ef2b1bec0b95597','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-119','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','698c2d5e06fb51de417d1ed586129','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-12','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','2e6af6d49e94eb82e6597cafcc4f9e5','Create Table','',NULL,'1.9.4'),('1227303685425-120','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','d05ee670738deebde0131d30112ec4ae','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-121','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','b248338c8498759a3ea4de4e3b667793','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-122','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','f2636dd6c2704c55fb9747b3b8a2a','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-123','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','307598cfdfdae399ad1ce3e7c4a8467','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-124','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','ce9674c37a991460aed032ad5cb7d89','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-125','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','dface46a9bf5a1250d143e868208865','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-126','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','de1299f5403e3360e1c2c53390a8b29c','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-127','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','5e7f8e3b6bf0fc792f3f302d7cb2b3','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-128','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','2d81a031cf75efab818d6e71fd344c24','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-129','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','ad7b4d0e76518df21fff420664ea52','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-13','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','8a81103ce3bf6f96ae9a3cd5ea11da4','Create Table','',NULL,'1.9.4'),('1227303685425-130','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','ca1a1a1be5462940b223815c43d43ee','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-131','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','1e994b0469d1759c69824fa79bb47e','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-132','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','71079c961b9b64fc0642ed69b944bef','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-133','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','b7f75569bbf9932631edb2ded9a412','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-134','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','bfdf173f27999984c8fef82bb496c880','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-135','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','fceb5cc5466c89f54fab47951181ead','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-136','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','05f6a40bc30fe272b4bd740ad7709f','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-137','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','e4d42fbfe1248f205c9221b884c8e71','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-138','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','9ebf8d9bb9541ceba5a7ed25d55978','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-139','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','4ab8e4364cdb9f61f512845b537a5e33','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-14','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','2b11cd2e016b3967d4806872727ec7','Create Table','',NULL,'1.9.4'),('1227303685425-140','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','48d01547623e48d3b4d11d547c28c28','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-141','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','1f2917f6f1523ca240c9dfd44ccdfa66','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-142','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','d826371493865456e85a06649e83a9d','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-143','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','669e47429cecc0ebe20c9e4724ad1b','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-144','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','fc622179fac4171f122cdbb217a5332','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-145','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','3ab7a04513262a248a1584ea9eb6342','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-146','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','9c9b88b77c842272ab95b913d2c45f1','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-147','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','6d2f3c8fd1a6fd222cd9e64626ec2414','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-148','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','7f99bbffa5152188ccd74e889cc69344','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-149','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','2971495b795edfbc771618d9d178179','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-15','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','d349b037a5614555956d6c25dfab11','Create Table','',NULL,'1.9.4'),('1227303685425-150','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','7453c6b2dc234075444921641f038cf','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-151','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','62bfd65133eb8649823b76e28f749ae4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-152','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','41f97398c21bcc4f3bbfa57ac36167','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-153','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','34a3388e508254798882f090113b91e9','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-154','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','cd1c528d9bbd8cd521f55f51507f2448','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-155','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','59169212323db3843f2d240afaab688','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-156','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','cb904d9071798e490aaf1a894777b','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-157','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','ec5e335c2d6ef840ba839d2d7cab26bf','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-158','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','edba928f8f3af2b720d1ae52c3da29','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-159','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','1d631d1b5585f307b5bba3e94104897','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-16','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','4a97582b5459ea8f1d718cc7044517b','Create Table','',NULL,'1.9.4'),('1227303685425-160','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','5af7bfc7acbef7db5684ff3e662d7b1','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-161','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','d65716f46e431b417677fb5754f631cf','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-162','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','a823cf39649373bf762fc2ddc9a3b4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-163','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','8efed95ebdac55227fd711cb18c652a','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-164','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','f7cb86e864d7b259bb63c73cb4cf1e8','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-165','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','5da4fd35f568ae85de6274f6fb179dd','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-166','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','d6a910cfe97bc6aed5e54ec1cadd89b','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-167','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','adf31a2b3145f38cfc94632966dc0cf','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-168','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','d3984f9f372ff0df21db96aeae982cab','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-169','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','296119308b945663ddbd5b4b719e53','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-17','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','66944d443ff50a89d5d7e3d3807ee4','Create Table','',NULL,'1.9.4'),('1227303685425-170','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','be7a3bc89baac1b0b2e0fe51991bd015','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-171','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','ca59f633dbae6e045d58b4cca6cc7c5','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-172','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','6325d6535f7cf928fa84a37a72605dfb','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-173','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','351518f028727083f95bc8d54c295c2a','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-174','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','3e888947a28e23f88510af9514767590','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-175','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','5fef7a2498f6d631e1228efc2bdef6c','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-176','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','2bd625c243b7583c86a9bffd3215a741','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-177','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','426c4bccb537fb3f4c3d3e26fff73018','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-178','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','fc3d34e4c7f55660abad9d79fa9f19','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-179','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','41071cc7cfeec957e73ede6cec85063','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-18','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','44be2c894ba65ba6dff11859325ee1e7','Create Table','',NULL,'1.9.4'),('1227303685425-180','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','17f13d77392855ff797b2ee5938150b','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-181','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','1e87e405f78834da8449c3dd538ea69','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-182','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','7e5895b2826c9b539643c2a52db136bd','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-183','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','1ed87f73bb22c85a678e7486ce316e4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-184','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','dd523eb3c0d47022fd3ccde778a7b21','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-185','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','bf22dffeb46511b2c54caa2edde81cf','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-186','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','bcd25ca2c2592b19d299b434d4e2b855','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-187','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','c552885fe12d78ad9d9e902e85c2e','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-188','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','1be8a7c821c27ac5f24f37ea72dde4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-189','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','5ebe66c9ad8928b9dd571fbf2e684342','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-19','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','dcf0c2ad93178afc912bbb599424bb7a','Create Table','',NULL,'1.9.4'),('1227303685425-190','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','765abd819360e17b449b349e3c6fb4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-191','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','eefbf72ce449dfa6d87903b9d48fc14','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-192','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','c49e3e3ef4748a6d227822ba9d9db75','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-193','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','8734c3bf33d23c5167926fde1c2497b1','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-194','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','acf63f4166b9efbeec94a33b9873b6','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-195','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','a6893ed94b1c673064e64f31e76922e3','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-196','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','4d10fc7df1588142908dfe1ecab77983','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-197','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','77df8c771bcafcdc69e0f036fa297e56','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-198','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','37bb62897883da1e2833a1ab2088edb','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-199','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','4dc522cef2d9e8f6679f1ad05d5e971e','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-2','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','484d95d2ffed12fa0ee8aee8974b567','Create Table','',NULL,'1.9.4'),('1227303685425-20','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','7faf5302b18d5ce205b723bca1542c','Create Table','',NULL,'1.9.4'),('1227303685425-200','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:44','939ac86f57297fc01f80b129247630f5','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-201','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','9bf8c421b14fa2c8e5784f89c3d8d65','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-202','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','309ecd6871c152c8c2f87a80f520a68a','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-203','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','c8ac825ea236dd980f088849eb6e974','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-204','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','3094b038b0f828dbe7d0296b28eb3f73','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-205','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','5a12d2856974981835ba499d73ac1','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-206','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','c1abccf6ee7c67d4471cc292fa878aa','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-207','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','ef6b7d9cb53938c0e8682daeb5af97e','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-208','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','a8a7f2b6ac8fa17b814ab859b4532fd','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-209','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','137480e6ed47c390e98547b0551d39f4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-21','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','ea3ef386b2dfb64cb9df5c1fbc6378','Create Table','',NULL,'1.9.4'),('1227303685425-210','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','fa37aa2bf95f6b24806ad4fab3b16e26','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-211','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','e3cfdbbc639a13bb42471dbecdc2d88','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-212','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','6eee35721da53ff117ababbae33bd5','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-213','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','c591a16e2a8fcc25ef4aa858692dfef','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-214','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','56c3ba4cfa55218d3aa0238ec2275f0','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-215','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','48563ec0b8762dce6bf94e068b27cb5','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-216','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','396121d768871a53120935cf19420d7','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-217','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','ebfbd07cd866453255b3532893163562','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-218','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','1530c85467d1b6d6980c794dbfd33f','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-219','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','5061bae5afea383d93b6f8b118253d','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-22','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','4dba51434d6621d026237deaa89fb','Create Table','',NULL,'1.9.4'),('1227303685425-220','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','e4ccb7132533b42c47f77abf5ef26','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-221','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','ff49796922743ca6aec19ec3a4ee2d8','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-222','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','b6a54d3e4d64fd39ef72efb98dfc936','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-223','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','a4de4db8a76fd815c83b53fd5f67fee1','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-224','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','22ea56d2224297e8252864e90466c','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-225','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','dfccc3b975a9f5b8ec5b27164c2d6d','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-226','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','e34f2fe84f1222d6aa5eb652f8d17f44','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-227','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','6bf6811bac713b4761afbba7756e1a61','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-228','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','4732be71b8c8fbf39bc1df446af78751','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-229','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','73d11a5ae7bf33aa516119311cbc16f6','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-23','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','88e233ad3125c41ed528a721cfe2345f','Create Table','',NULL,'1.9.4'),('1227303685425-230','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','ee2e35bc91b5e1b01f2048c987caf6c4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-231','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','63bbb76e789fcad6b69bd4c64f5950','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-232','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','76be79df3e103cc45f4ee148555e4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-233','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','e7b3a215c88f4d892fa76882e5159f64','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-234','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','8c28fb289889fdd53acda37d11112ce2','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-235','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','6cdeded260bdceb9e1826d4ffbf5a375','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-236','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','28e4b2fa85d59877ad9fa54c6d2b031','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-237','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','a1ffd0638df4322f16f8726f98b4f2','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-238','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','41aa312b273ec9ebf460faaa6e6b299c','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-239','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','20d4b6e95fcfb79a613a24cf75e86c6','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-24','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','b2efc086868b3c3274b7d68014b6ea93','Create Table','',NULL,'1.9.4'),('1227303685425-240','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','589dcc4157bfcf62a427f2ce7e4e1c29','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-241','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','52bde811efcc8b60f64bc0317ee6045','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-242','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','c58692cfa01f856a60b98215ba62e5a7','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-243','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','6d44d317d139438e24ff60424ea7df8d','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-244','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','e6e61db0f6b2f486a52797219e8545f','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-245','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','f5403a8a515082dd136af948e6bf4c97','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-246','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','fa4d4b30d92fa994ee2977a353a726f1','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-247','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','67b39f2b2132f427ff2a6fcf4a79bdeb','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-248','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','e67632f4fa63536017f3bcecb073de71','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-249','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','4c81272b8d3d7e4bd95a8cf8e9a91e6a','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-25','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','ec16b9415f779c72364dc4a1af98fb','Create Table','',NULL,'1.9.4'),('1227303685425-250','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','54fb2e3a25332da06e38a9d2b43badfd','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-251','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','fb1a29f3188a43c1501e142ba09fd778','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-252','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','e5c417adb79757dd942b194ad59cd2e3','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-253','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','412115fd6d7bedbba0d3439617328693','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-254','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:45','e76b81d6b51f58983432c296f319e48','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-255','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','80703c6fd0656cf832f32f75ad64f3','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-256','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','88b295bfc4d05f928c8d7cbf36c84d','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-257','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','853fb4af8c20137ecf9d858713cff6','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-258','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','c33b2874e3d38ddef784a0ccb03e3f74','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-259','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','bb18dfa78fe1322413ff4f0871712a','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-26','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','965f979f6fca5553376e5206ef6b2f','Create Table','',NULL,'1.9.4'),('1227303685425-260','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','f0953d50abcef615f855b6d6ef0bbd3','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-261','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','2e24f8fdf2b3dfb9abb02ccaeee87f4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-262','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','28d3165b5789d4ccb62ac271c8d86bdd','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-263','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','7f93b659cc1a2a66c8666f7ab5793b','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-264','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','c51f76c76ca7945eeb2423b56fd754ff','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-265','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','46b310f19ea8c0da3afd50c7f2717f2','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-266','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','aa7981765d72e6c22281f91c71602ce2','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-267','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','b2bc8373ebb491233bea3b6403e2fea','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-268','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','f4882dfee1f2ffac3be37258a8ec4c43','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-269','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','4c9212f9fa9f24ef868c7fd5851b7e8f','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-27','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','7bbca39a5f958ea7e481e566056e94e','Create Table','',NULL,'1.9.4'),('1227303685425-270','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','aaf31e19b19f9e4b840d08ea730d1','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-271','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','47c543135499fdcd7f1ea1a4a71e495d','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-272','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','7df76774c3f25483e4b9612eee98d8','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-273','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','6cbef8ca7253e9b6941625756e3ead12','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-274','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','99c026624d5987f4a39b124c473da2','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-275','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','bab45b77299235943c469ca9e0396cff','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-276','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','c50376c85bff6bdf26a4ca97e2bfe','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-277','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','e7e8229acc4533fd3fca7534e531063','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-278','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','aed02dbce85e293159b787d270e57d63','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-279','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','93ce9101c3bc7214721f3163b11c33','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-28','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','5a62bb3df8ebcc7ff768b3bcebb033','Create Table','',NULL,'1.9.4'),('1227303685425-280','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','cef4a9e68e71659662adddd67c976','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-281','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','929d293722d1d652834da0ceefa9c841','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-282','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','7a2834f44349838fcfbcb61648bc8a0','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-283','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','dd19eeb1b52497428e644a84f0653c8e','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-284','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','8bc32e874e45a6cdd60184e9c92d6c4','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-285','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','66af85da9d747cf7b13b19c358fd93c','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-286','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','14ac9a98a545c5b172c021486d1d36','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-287','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','7c4e227cf011839ef78fb4a83ed59d6','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-288','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','ab73c07b88fcfd7fffcd2bd41e05ec8','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-289','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','ffb7495ff05d83cf97d9986f895efe0','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-29','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','16b6b77c88815c89afc5edb5a756031','Create Table','',NULL,'1.9.4'),('1227303685425-290','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','fd25f4e5377a68bc11f6927ec4ba1dc','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-291','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','fbdd163ca23861baa2f84dce077b6c','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-292','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','d0eebd47c26dc07764741ee4b1ac72','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-293','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','28b8cd9a32fb115c9c746712afa223','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-294','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','ada81a1d3e575780a54bbe79643db8fc','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-295','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','93e944816ec2dc7b31984a9e4932f3bb','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-296','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','54d71bd27cb66b9eb12716bdce3c7c','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-297','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','a9429cc74fb131d5d50899a402b21f','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-298','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','e38bd6437d3ce8274e16d02e73e976a8','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-299','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','2dcda62270603cbfa1581d1339b0a18e','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-3','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','1eeb9f4467c832c61b2cadf8da592ba9','Create Table','',NULL,'1.9.4'),('1227303685425-30','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','77a2e7fda5d89a99cfee38479b0165a','Create Table','',NULL,'1.9.4'),('1227303685425-300','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','c861c2e3e1fcbae170b5fe8c161ba7c9','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-301','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','0444230b26f283b1b4b4c3583ca6a2','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-302','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','3ae36b982c1a4dc32b623ed7eab649','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-303','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:46','dabab8dcefdde7eefee9d8cc177725','Add Foreign Key Constraint','',NULL,'1.9.4'),('1227303685425-31','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','5ce43fef913d6bac45272c9ad52121','Create Table','',NULL,'1.9.4'),('1227303685425-32','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','8149ef2df786f5e2f7965547ac66d1','Create Table','',NULL,'1.9.4'),('1227303685425-33','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','fa102c6455b4f47c2fb0605ccf48f1','Create Table','',NULL,'1.9.4'),('1227303685425-34','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','daafc2af20bfde50d48a6b63706380e2','Create Table','',NULL,'1.9.4'),('1227303685425-35','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','88c2f1f647e779f89929826a776fddc6','Create Table','',NULL,'1.9.4'),('1227303685425-36','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','167c47702631558d6de2568739f1e6','Create Table','',NULL,'1.9.4'),('1227303685425-37','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','a069daa036fbded8e54b73975dac1fe1','Create Table','',NULL,'1.9.4'),('1227303685425-38','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','25c5c2d1727636891151ad31d1e70cd','Create Table','',NULL,'1.9.4'),('1227303685425-39','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','d38de3f1828ef0b32140f9cb576b9c7e','Create Table','',NULL,'1.9.4'),('1227303685425-4','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','d69b4d32237225f348338d6db03ac37c','Create Table','',NULL,'1.9.4'),('1227303685425-40','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','ac8ba1f43f50462696516b562bb4aa3f','Create Table','',NULL,'1.9.4'),('1227303685425-41','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','f0f7a7a4bf316add1449b49698647ef1','Create Table','',NULL,'1.9.4'),('1227303685425-42','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','9678d659e5e8b86b3645dfb5e1cddea0','Create Table','',NULL,'1.9.4'),('1227303685425-43','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','fca0bb4ee259f5aa759c35542beb36fe','Create Table','',NULL,'1.9.4'),('1227303685425-44','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','30397b2c2721aeec98d1a20ea8ed61b','Create Table','',NULL,'1.9.4'),('1227303685425-45','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','a0fae4a0da1abd4a1b145e20341c2c4','Create Table','',NULL,'1.9.4'),('1227303685425-46','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','e4c283dd21fd7ab9edece7a28b6f474','Create Table','',NULL,'1.9.4'),('1227303685425-47','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','2e726fb82e6ea67196cbd74ae989f','Create Table','',NULL,'1.9.4'),('1227303685425-48','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','e811761440ee1fc96f56ccfd79bdbd7e','Create Table','',NULL,'1.9.4'),('1227303685425-49','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','693e9f85a15699482484b33a784228','Create Table','',NULL,'1.9.4'),('1227303685425-5','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','7fcc65a98f7168cbe24bb62e8aba75e','Create Table','',NULL,'1.9.4'),('1227303685425-50','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','211a6e546141eb7d7debc431966e1a4d','Create Table','',NULL,'1.9.4'),('1227303685425-51','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','3ae4506eb6656cc1f4a428f607bd8e6','Create Table','',NULL,'1.9.4'),('1227303685425-52','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','a874dfc4c79e8b81f778bf4d2a826a1','Create Table','',NULL,'1.9.4'),('1227303685425-53','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','13078163a91cacdc43eb0da2153cf11','Create Table','',NULL,'1.9.4'),('1227303685425-54','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','977575cb16f52ca71619fed9e792cd57','Create Table','',NULL,'1.9.4'),('1227303685425-55','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','6d484b3d964b60a65a92388243722cc8','Create Table','',NULL,'1.9.4'),('1227303685425-56','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','aaaef34153e53d2adcc71d90a0955','Create Table','',NULL,'1.9.4'),('1227303685425-57','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','ee5ecb79709fcd91973a5725c444d68e','Create Table','',NULL,'1.9.4'),('1227303685425-58','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','43a3932a5b7c6347ffaac3e3309ff1','Create Table','',NULL,'1.9.4'),('1227303685425-59','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','7de9e546cf9250931641fba5c3aa95f','Create Table','',NULL,'1.9.4'),('1227303685425-6','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','d4a329e7f87f469e80b454a861526','Create Table','',NULL,'1.9.4'),('1227303685425-60','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','df9be9977bc1a13b2da39b5cd7c9d4b','Create Table','',NULL,'1.9.4'),('1227303685425-61','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','3a5216b5741d44cdf367f2a9415ec','Create Table','',NULL,'1.9.4'),('1227303685425-62','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','c4b0da30561adf7d40fcd7da77c574','Create Table','',NULL,'1.9.4'),('1227303685425-63','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','45fd4df9a91aaaea2f515dbb8bbaae0','Create Table','',NULL,'1.9.4'),('1227303685425-64','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','99138560e6e73c3bf6f54fb792552cf','Create Table','',NULL,'1.9.4'),('1227303685425-65','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','4e33e6a7cf3bd773f126612bf0b9','Create Table','',NULL,'1.9.4'),('1227303685425-66','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','c223439be520db74976e1c6399b591b0','Create Table','',NULL,'1.9.4'),('1227303685425-67','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','c06e7aab7120c7f5b2ce3c5498134a7','Create Table','',NULL,'1.9.4'),('1227303685425-68','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','6744cea65ee820c9addf59d78b446ce8','Create Table','',NULL,'1.9.4'),('1227303685425-69','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','c1ee0d08f2c8238f17125d4a58263','Create Table','',NULL,'1.9.4'),('1227303685425-7','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','4fa3245365c5997cbdf491c13c7970cd','Create Table','',NULL,'1.9.4'),('1227303685425-70','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','88c072335290bda8394acf5344b5be','Create Table','',NULL,'1.9.4'),('1227303685425-71','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','5887deaf859b231a28fca6cd8f53985f','Create Table','',NULL,'1.9.4'),('1227303685425-72','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','a94bc5c11995258d80cc35f45042b0c8','Create Table','',NULL,'1.9.4'),('1227303685425-73','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','db75ceff7fabd137f5ffde95c02ab814','Add Primary Key','',NULL,'1.9.4'),('1227303685425-74','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','d7d5d089c54468ac2cfd638f66fcb4','Add Primary Key','',NULL,'1.9.4'),('1227303685425-75','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','39dd463941e952d670a5c44194bfe578','Add Primary Key','',NULL,'1.9.4'),('1227303685425-76','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','dc6155351d7db68e99ff6e95bbc87be','Add Primary Key','',NULL,'1.9.4'),('1227303685425-77','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','4fee106cf921289c298f9c67fc87c7c5','Add Primary Key','',NULL,'1.9.4'),('1227303685425-78','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','3a3de23a22ae9c344b47a4d939e6ff70','Add Primary Key','',NULL,'1.9.4'),('1227303685425-79','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','88ab4066e17d3a326dc54e5837c09f41','Add Primary Key','',NULL,'1.9.4'),('1227303685425-8','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','e55f3abe82ce4b674bcdc1b4996b8b9','Create Table','',NULL,'1.9.4'),('1227303685425-80','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','940c1a878d7788f304e14cf4c3394c7','Add Primary Key','',NULL,'1.9.4'),('1227303685425-81','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','219f4f94772b86a6785a1168655d630','Add Primary Key','',NULL,'1.9.4'),('1227303685425-82','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','4e2676797833eba977cbcae8806c05f','Add Primary Key','',NULL,'1.9.4'),('1227303685425-83','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:41','587abb7e972b9dbae4711c9da4e55b','Add Primary Key','',NULL,'1.9.4'),('1227303685425-84','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','9435c219edb4e839af13de48a94f765c','Add Primary Key','',NULL,'1.9.4'),('1227303685425-85','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','1bf11ad88f6aeb16115e53aec0aa1fc9','Create Index','',NULL,'1.9.4'),('1227303685425-86','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','1e8c4a47a221a275929253c81814e3','Create Index','',NULL,'1.9.4'),('1227303685425-87','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','d7aa9fb9b259691ef965935112690c6','Create Index','',NULL,'1.9.4'),('1227303685425-88','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','8637a8777e63c352c513c78c60a435fb','Create Index','',NULL,'1.9.4'),('1227303685425-89','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','8124613e1c06fe7dbdd82ec7d77b513','Create Index','',NULL,'1.9.4'),('1227303685425-9','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:40','cdba1dcd46bf7733f3d2225245947db','Create Table','',NULL,'1.9.4'),('1227303685425-90','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','ce32a6b73bddd39523fd702837d5e0ac','Create Index','',NULL,'1.9.4'),('1227303685425-91','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','8b88d2f395eb2b5c7a48a2691527335a','Create Index','',NULL,'1.9.4'),('1227303685425-92','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','9935e165ac0c994c840f0b6ad7e6ea9','Create Index','',NULL,'1.9.4'),('1227303685425-93','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','1d54748d2664161ec2a4ec4761dfcbe','Create Index','',NULL,'1.9.4'),('1227303685425-94','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','e4cbe444f931f7892fe1ff2cc8ef9','Create Index','',NULL,'1.9.4'),('1227303685425-95','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','8e83cb5c20116c3bafc2afcec67fbbcd','Create Index','',NULL,'1.9.4'),('1227303685425-96','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','844a9c8a5e4772bb4383d22c1b7c4f','Create Index','',NULL,'1.9.4'),('1227303685425-97','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','50c460b4b15dbe1c357c9637d29fba2e','Create Index','',NULL,'1.9.4'),('1227303685425-98','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:42','ebc8c01af6a50d966426a572a317d1','Create Index','',NULL,'1.9.4'),('1227303685425-99','ben (generated)','liquibase-schema-only.xml','2013-02-06 12:16:43','82c523688fbc57714736dfa7aafb48','Create Index','',NULL,'1.9.4'),('2','upul','liquibase-update-to-latest.xml','2013-02-06 12:16:50','271913afcc5e1fc2a96a6b12705e60a4','Add Foreign Key Constraint','Create the foreign key from the privilege required for to edit\n			a person attribute type and the privilege.privilege column',NULL,'1.9.4'),('200805281223','bmckown','liquibase-update-to-latest.xml','2013-02-06 12:16:52','ca38d15c388fd3987ee8e997f48bcb43','Create Table, Add Foreign Key Constraint','Create the concept_complex table',NULL,'1.9.4'),('200805281224','bmckown','liquibase-update-to-latest.xml','2013-02-06 12:16:52','ca898c6096d952b57e28c5edc2a957','Add Column','Adding the value_complex column to obs.  This may take a long time if you have a large number of observations.',NULL,'1.9.4'),('200805281225','bmckown','liquibase-update-to-latest.xml','2013-02-06 12:16:52','c951352b0c976e84b83acf3cfcbb9e','Insert Row','Adding a \'complex\' Concept Datatype',NULL,'1.9.4'),('200805281226','bmckown','liquibase-update-to-latest.xml','2013-02-06 12:16:54','b8b85f33a0d9fde9f478d77b1b868c80','Drop Table (x2)','Dropping the mimetype and complex_obs tables as they aren\'t needed in the new complex obs setup',NULL,'1.9.4'),('200809191226','smbugua','liquibase-update-to-latest.xml','2013-02-06 12:16:54','99ca2b5ae282ba5ff1df9c48f5f2e97','Add Column','Adding the hl7 archive message_state column so that archives can be tracked\n			\n			(preCondition database_version check in place because this change was in the old format in trunk for a while)',NULL,'1.9.4'),('200809191927','smbugua','liquibase-update-to-latest.xml','2013-02-06 12:16:54','8acc3bbd9bb21da496f4dbcaacfe16','Rename Column, Modify Column','Adding the hl7 archive message_state column so that archives can be tracked',NULL,'1.9.4'),('200811261102','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:50','7ce199737ad5ea72959eb52ece15ab8','Update Data','Fix field property for new Tribe person attribute',NULL,'1.9.4'),('200901101524','Knoll_Frank','liquibase-update-to-latest.xml','2013-02-06 12:16:54','8b599959a0ae994041db885337fd94e','Modify Column','Changing datatype of drug.retire_reason from DATETIME to varchar(255)',NULL,'1.9.4'),('200901130950','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:54','add260b86fbdfe8ddfd15480f7c12530','Delete Data (x2)','Remove Manage Tribes and View Tribes privileges from all roles',NULL,'1.9.4'),('200901130951','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:54','51eaa74d22ee244cc0cbde5ef4758e1c','Delete Data (x2)','Remove Manage Mime Types, View Mime Types, and Purge Mime Types privilege',NULL,'1.9.4'),('200901161126','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:54','478dceabf15112ad9eccee255df8b','Delete Data','Removed the database_version global property',NULL,'1.9.4'),('20090121-0949','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:54','a38a30d48634ab7268204167761db86c','Custom SQL','Switched the default xslt to use PV1-19 instead of PV1-1',NULL,'1.9.4'),('20090122-0853','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:54','5ef0554257579cb649fe4414efd233e0','Custom SQL, Add Lookup Table, Drop Foreign Key Constraint, Delete Data (x2), Drop Table','Remove duplicate concept name tags',NULL,'1.9.4'),('20090123-0305','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:54','fc5153a938b4ce0aa455ca9fea7ad9','Add Unique Constraint','Add unique constraint to the tags table',NULL,'1.9.4'),('20090214-2246','isherman','liquibase-update-to-latest.xml','2013-02-06 12:16:58','6c77a79fc9913830755829adaa983227','Custom SQL','Add weight and cd4 to patientGraphConcepts user property (mysql specific)',NULL,'1.9.4'),('20090214-2247','isherman','liquibase-update-to-latest.xml','2013-02-06 12:16:58','664dee9bed7b178ea62e8de7a8824045','Custom SQL','Add weight and cd4 to patientGraphConcepts user property (using standard sql)',NULL,'1.9.4'),('20090214-2248','isherman','liquibase-update-to-latest.xml','2013-02-06 12:16:58','489fc62a7b3196ba3887b8a2ddc8d93c','Custom SQL','Add weight and cd4 to patientGraphConcepts user property (mssql specific)',NULL,'1.9.4'),('200902142212','ewolodzko','liquibase-update-to-latest.xml','2013-02-06 12:17:16','ca3341c589781757f7b5f3e73a2b8f5','Add Column','Add a sortWeight field to PersonAttributeType',NULL,'1.9.4'),('200902142213','ewolodzko','liquibase-update-to-latest.xml','2013-02-06 12:17:16','918476ca54d9c1e77076be12012841','Update Data','Add default sortWeights to all current PersonAttributeTypes',NULL,'1.9.4'),('20090224-1229','Keelhaul+bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:56','2112b4bd53e438fc2ac4dc2e60213440','Create Table, Add Foreign Key Constraint (x2)','Add location tags table',NULL,'1.9.4'),('20090224-1250','Keelhaul+bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:58','ae593490311f38ee69962330e3788938','Create Table, Add Foreign Key Constraint (x2), Add Primary Key','Add location tag map table',NULL,'1.9.4'),('20090224-1256','Keelhaul+bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:58','27add85e296c1f22deb2fa17d77a542f','Add Column, Add Foreign Key Constraint','Add parent_location column to location table',NULL,'1.9.4'),('20090225-1551','dthomas','liquibase-update-to-latest.xml','2013-02-06 12:16:58','c490c377e78a9740a765e19aacf076','Rename Column (x2)','Change location_tag.name to location_tag.tag and location_tag.retired_reason to location_tag.retire_reason',NULL,'1.9.4'),('20090301-1259','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:16:58','88d88f5cc99c93c264e6976feef8eecc','Update Data (x2)','Fixes the description for name layout global property',NULL,'1.9.4'),('20090316-1008','vanand','liquibase-update-to-latest.xml','2013-02-06 12:16:59','fcdde237e04473db1e6fd16f56bf80e9','Modify Column (x7), Update Data, Modify Column (x18), Update Data, Modify Column (x11)','Change column types of all boolean columns to smallint. The columns used to be either tinyint(4) or MySQL\'s BIT.\n			(This may take a while on large patient or obs tables)',NULL,'1.9.4'),('200903210905','mseaton','liquibase-update-to-latest.xml','2013-02-06 12:17:01','a13f5939f4ce2a6a9a21c1847d3ab6d','Create Table, Add Foreign Key Constraint (x3)','Add a table to enable generic storage of serialized objects',NULL,'1.9.4'),('20090402-1515-38-cohort','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','e5d01ebcda1a2b4ef993a3c859933947','Add Column','Adding \"uuid\" column to cohort table',NULL,'1.9.4'),('20090402-1515-38-concept','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','575aaf2321f320331ba877f49a44b8','Add Column','Adding \"uuid\" column to concept table',NULL,'1.9.4'),('20090402-1515-38-concept_answer','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','57377447c4be5152c5bb18fdceec2312','Add Column','Adding \"uuid\" column to concept_answer table',NULL,'1.9.4'),('20090402-1515-38-concept_class','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','4510924e4fc34a5b4f4786af54a6e9d6','Add Column','Adding \"uuid\" column to concept_class table',NULL,'1.9.4'),('20090402-1515-38-concept_datatype','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','ffa6230eb5d1021556b2c106176b081','Add Column','Adding \"uuid\" column to concept_datatype table',NULL,'1.9.4'),('20090402-1515-38-concept_description','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','a864b52fe66664a95bb25d08ca0fae4','Add Column','Adding \"uuid\" column to concept_description table',NULL,'1.9.4'),('20090402-1515-38-concept_map','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','70db4397c48f719f6b6db9b228a5940','Add Column','Adding \"uuid\" column to concept_map table',NULL,'1.9.4'),('20090402-1515-38-concept_name','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','6a5bf2fb43b89825adbab42e32f8c95','Add Column','Adding \"uuid\" column to concept_name table',NULL,'1.9.4'),('20090402-1515-38-concept_name_tag','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','e5175d7effbbe1f327f6765c8563de','Add Column','Adding \"uuid\" column to concept_name_tag table',NULL,'1.9.4'),('20090402-1515-38-concept_proposal','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','d812c01d4487ee3afd8408d322ae6e9','Add Column','Adding \"uuid\" column to concept_proposal table',NULL,'1.9.4'),('20090402-1515-38-concept_set','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','a76a7ad772591c707d1470bfb8e96ebe','Add Column','Adding \"uuid\" column to concept_set table',NULL,'1.9.4'),('20090402-1515-38-concept_source','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','82f9e9817631b23dd768d92c451a4b','Add Column','Adding \"uuid\" column to concept_source table',NULL,'1.9.4'),('20090402-1515-38-concept_state_conversion','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','da7523634d5355b5bb2dc7ba3b544682','Add Column','Adding \"uuid\" column to concept_state_conversion table',NULL,'1.9.4'),('20090402-1515-38-drug','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','867c42ea0b5a864beccff584fa963ff','Add Column','Adding \"uuid\" column to drug table',NULL,'1.9.4'),('20090402-1515-38-encounter','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','53d8465949124bc6247b28a4a58121','Add Column','Adding \"uuid\" column to encounter table',NULL,'1.9.4'),('20090402-1515-38-encounter_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','ee9b948fa08ad55c3ecd75c32df82d5','Add Column','Adding \"uuid\" column to encounter_type table',NULL,'1.9.4'),('20090402-1515-38-field','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','e24dae16c5a010a575c9c1ac84d8eb','Add Column','Adding \"uuid\" column to field table',NULL,'1.9.4'),('20090402-1515-38-field_answer','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','7bbeeb1abdc5389f5a7488ee35498b','Add Column','Adding \"uuid\" column to field_answer table',NULL,'1.9.4'),('20090402-1515-38-field_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','6271df216ece46cecde2c5f642e84a','Add Column','Adding \"uuid\" column to field_type table',NULL,'1.9.4'),('20090402-1515-38-form','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','2569936cbd22e8029253d4e6034f738','Add Column','Adding \"uuid\" column to form table',NULL,'1.9.4'),('20090402-1515-38-form_field','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','e237246d492a5d9046edba679c2ef7b4','Add Column','Adding \"uuid\" column to form_field table',NULL,'1.9.4'),('20090402-1515-38-global_property','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','77d9d563a6b488dde8856cd8e44e74','Add Column','Adding \"uuid\" column to global_property table',NULL,'1.9.4'),('20090402-1515-38-hl7_in_archive','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','cc8aa360f98aedcc8399a46edb48ca7e','Add Column','Adding \"uuid\" column to hl7_in_archive table',NULL,'1.9.4'),('20090402-1515-38-hl7_in_error','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','4857a237f2d29901dc2721861b13aa1','Add Column','Adding \"uuid\" column to hl7_in_error table',NULL,'1.9.4'),('20090402-1515-38-hl7_in_queue','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','3e0bedc6f28626d1ed8feba9b12744','Add Column','Adding \"uuid\" column to hl7_in_queue table',NULL,'1.9.4'),('20090402-1515-38-hl7_source','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','0c1637fe4ec912bdf75f4ec338f42','Add Column','Adding \"uuid\" column to hl7_source table',NULL,'1.9.4'),('20090402-1515-38-location','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','2fc3aaf750476ed2df71b6689d941546','Add Column','Adding \"uuid\" column to location table',NULL,'1.9.4'),('20090402-1515-38-location_tag','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','b8c3ac793659918dfb140d94207d5d','Add Column','Adding \"uuid\" column to location_tag table',NULL,'1.9.4'),('20090402-1515-38-note','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','c4722e59a9be1c47d0569216e92ad63','Add Column','Adding \"uuid\" column to note table',NULL,'1.9.4'),('20090402-1515-38-notification_alert','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','7a66a648529a78d215e17023cdf5d5','Add Column','Adding \"uuid\" column to notification_alert table',NULL,'1.9.4'),('20090402-1515-38-notification_template','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','739a3826ba3226875d8f944140e1c188','Add Column','Adding \"uuid\" column to notification_template table',NULL,'1.9.4'),('20090402-1515-38-obs','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','75c1752f21de4b95569c7ac66e826e2f','Add Column','Adding \"uuid\" column to obs table',NULL,'1.9.4'),('20090402-1515-38-orders','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','f19f5e991569bc1341e9dd31a412da43','Add Column','Adding \"uuid\" column to orders table',NULL,'1.9.4'),('20090402-1515-38-order_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:02','f2c93b646d83d969bf8ba09d11d36edd','Add Column','Adding \"uuid\" column to order_type table',NULL,'1.9.4'),('20090402-1515-38-patient_identifier','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','dd97565c4ff6c410838a5feb41975d0','Add Column','Adding \"uuid\" column to patient_identifier table',NULL,'1.9.4'),('20090402-1515-38-patient_identifier_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','613f3c8d0226c305678d461a89db238','Add Column','Adding \"uuid\" column to patient_identifier_type table',NULL,'1.9.4'),('20090402-1515-38-patient_program','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','a2c23c3ef77d8ea9a2406c428e207244','Add Column','Adding \"uuid\" column to patient_program table',NULL,'1.9.4'),('20090402-1515-38-patient_state','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','57563194b5fa551f129a9cce1b38e8e7','Add Column','Adding \"uuid\" column to patient_state table',NULL,'1.9.4'),('20090402-1515-38-person','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','e8a138182c0ba1053131888f8cf8b4','Add Column','Adding \"uuid\" column to person table',NULL,'1.9.4'),('20090402-1515-38-person_address','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','2617c04da91ecda5fd2b45237f5ddd','Add Column','Adding \"uuid\" column to person_address table',NULL,'1.9.4'),('20090402-1515-38-person_attribute','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','ae559b7cc39a175062126e4815d762','Add Column','Adding \"uuid\" column to person_attribute table',NULL,'1.9.4'),('20090402-1515-38-person_attribute_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','68f914b55c3b3fb7dc2a77fd8d933d2','Add Column','Adding \"uuid\" column to person_attribute_type table',NULL,'1.9.4'),('20090402-1515-38-person_name','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','1b8be5c78ca558b45a25d5eaefbbed7','Add Column','Adding \"uuid\" column to person_name table',NULL,'1.9.4'),('20090402-1515-38-privilege','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','13d0d85bf5609b955b128fd6f6bc5c6d','Add Column','Adding \"uuid\" column to privilege table',NULL,'1.9.4'),('20090402-1515-38-program','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','39a4f7c8c86791b166d280d9b22ae','Add Column','Adding \"uuid\" column to program table',NULL,'1.9.4'),('20090402-1515-38-program_workflow','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','70877f3fdcfd50bea8d8d0c19fb115ac','Add Column','Adding \"uuid\" column to program_workflow table',NULL,'1.9.4'),('20090402-1515-38-program_workflow_state','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','116efa4fd75f61e034c33a2d4e4c75e2','Add Column','Adding \"uuid\" column to program_workflow_state table',NULL,'1.9.4'),('20090402-1515-38-relationship','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','945c9c8a51c2ddfd75195df74b01c37','Add Column','Adding \"uuid\" column to relationship table',NULL,'1.9.4'),('20090402-1515-38-relationship_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','09c5c28357a6999636569c43bf1d4a','Add Column','Adding \"uuid\" column to relationship_type table',NULL,'1.9.4'),('20090402-1515-38-report_object','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','4bf73bf794f3b6ec1b5dff44e23679','Add Column','Adding \"uuid\" column to report_object table',NULL,'1.9.4'),('20090402-1515-38-report_schema_xml','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','2b7354fdbf3a45a725ad7392f73c1d2c','Add Column','Adding \"uuid\" column to report_schema_xml table',NULL,'1.9.4'),('20090402-1515-38-role','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','2955291013b5657ffa9eeeb526d5563','Add Column','Adding \"uuid\" column to role table',NULL,'1.9.4'),('20090402-1515-38-serialized_object','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','bbb72e88cde2dd2f44f72c175a75f10','Add Column','Adding \"uuid\" column to serialized_object table',NULL,'1.9.4'),('20090402-1516-cohort','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','c916ab788583392ea698441818ca8b','Update Data','Generating UUIDs for all rows in cohort table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','5cd0f78948d656fbe03411c3872b8d3d','Update Data','Generating UUIDs for all rows in concept table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_answer','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','76742a2d89cdf9216b23ff1cc43f7e2b','Update Data','Generating UUIDs for all rows in concept_answer table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_class','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','b036159e7e6a62be355ab2da84dc6d6','Update Data','Generating UUIDs for all rows in concept_class table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_datatype','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','ffd8b9d259bb98779a6a444a3c597','Update Data','Generating UUIDs for all rows in concept_datatype table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_description','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','471858eb414dd416787873d59ce92d','Update Data','Generating UUIDs for all rows in concept_description table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_map','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','fd1e26504b7b5e227de9a6265cdfaf6a','Update Data','Generating UUIDs for all rows in concept_map table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_name','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','af235654349acd661ecadde2b2c9d29','Update Data','Generating UUIDs for all rows in concept_name table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_name_tag','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','bddb56793bee7cbb59a749557e601b','Update Data','Generating UUIDs for all rows in concept_name_tag table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_proposal','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','d35f42aea2acad9d3eb98a0aa68d','Update Data','Generating UUIDs for all rows in concept_proposal table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_set','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','131a6e5d0c39e32443f72d07f14f86','Update Data','Generating UUIDs for all rows in concept_set table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_source','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','3376f58bc68455beaeca7723e8dfdc','Update Data','Generating UUIDs for all rows in concept_source table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-concept_state_conversion','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','2daca61a968dc1d71dd012c2a770a9a5','Update Data','Generating UUIDs for all rows in concept_state_conversion table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-drug','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','60428b7567bee56397d3ad9c3c74da2','Update Data','Generating UUIDs for all rows in drug table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-encounter','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','855a3866a99360f46dcb54965341a8e','Update Data','Generating UUIDs for all rows in encounter table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-encounter_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','2694d23adc4fec35aabf472b737fc36e','Update Data','Generating UUIDs for all rows in encounter_type table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-field','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','5e406b6bd57710696766bc498b9dc0','Update Data','Generating UUIDs for all rows in field table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-field_answer','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','1b547411666d85b4ee383c2feef3ce','Update Data','Generating UUIDs for all rows in field_answer table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-field_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','b5cd4a815ecfcae3e7a0e16539c469b1','Update Data','Generating UUIDs for all rows in field_type table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-form','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','867b94d343b1f0347371ec9fa8e3e','Update Data','Generating UUIDs for all rows in form table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-form_field','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','ea6c2b047b7c59190cd637547dbd3dd','Update Data','Generating UUIDs for all rows in form_field table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-global_property','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','c743637b2a65526c09996236e9967b2','Update Data','Generating UUIDs for all rows in global_property table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-hl7_in_archive','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','b1df4ba911718e7987ad3ff8f36fa2b','Update Data','Generating UUIDs for all rows in hl7_in_archive table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-hl7_in_error','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','a5fbc93dd5decad5f1c254acabd0fa47','Update Data','Generating UUIDs for all rows in hl7_in_error table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-hl7_in_queue','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','9319f535948f73e3fa77872421e21c28','Update Data','Generating UUIDs for all rows in hl7_in_queue table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-hl7_source','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','de74537c46e71afe35c4e3da63da98c','Update Data','Generating UUIDs for all rows in hl7_source table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-location','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','ba24775d1c4e3a5224a88d4f9fd53a1','Update Data','Generating UUIDs for all rows in location table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-location_tag','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','19627ee1543bc0b639f935921a5ef2a','Update Data','Generating UUIDs for all rows in location_tag table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-note','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','dc91bee3824a094b728628b5d74aa7','Update Data','Generating UUIDs for all rows in note table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-notification_alert','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','d9fccf1943822a79503c7f15837316','Update Data','Generating UUIDs for all rows in notification_alert table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-notification_template','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','ff8dd4c09bcb33e9a6b1cba9275646b','Update Data','Generating UUIDs for all rows in notification_template table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-obs','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','77f2862ce7ca7192a4043a35c06673','Update Data','Generating UUIDs for all rows in obs table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-orders','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','967a61314dfd1114c81afbe4dd52ae67','Update Data','Generating UUIDs for all rows in orders table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-order_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:03','f460eb4ea5f07d87cce18e84ba65e12','Update Data','Generating UUIDs for all rows in order_type table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-patient_identifier','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','7539b5d6c2a704d95dc5f56e6e12d73','Update Data','Generating UUIDs for all rows in patient_identifier table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-patient_identifier_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','79f11da93475eb7467b9297dfbd7c79','Update Data','Generating UUIDs for all rows in patient_identifier_type table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-patient_program','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','1e53eb71d25b1d1eda33fe22d66133','Update Data','Generating UUIDs for all rows in patient_program table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-patient_state','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','a0ebd331c38f5296e92c72b96b801d4c','Update Data','Generating UUIDs for all rows in patient_state table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-person','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','cf951b6a27951e66a9a0889eb4fd6f15','Update Data','Generating UUIDs for all rows in person table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-person_address','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','c630f59a4c330969255165c34377d1d','Update Data','Generating UUIDs for all rows in person_address table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-person_attribute','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','608a4c703f42a7269e5d7c39a1d86de','Update Data','Generating UUIDs for all rows in person_attribute table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-person_attribute_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','d04f6cc7bfd235bca17e6d1d10505c3','Update Data','Generating UUIDs for all rows in person_attribute_type table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-person_name','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','e22e8f89ef24732458581cc697d55f','Update Data','Generating UUIDs for all rows in person_name table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-privilege','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','e3eed777bb7727aa1c80e55e5b307c','Update Data','Generating UUIDs for all rows in privilege table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-program','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','7af9569b516c59b71147a7c8bea93d63','Update Data','Generating UUIDs for all rows in program table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-program_workflow','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','56214fa359f3e3bc39104d37fc996538','Update Data','Generating UUIDs for all rows in program_workflow table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-program_workflow_state','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','4fc3046f6f1a9eee5da901fdaacae34','Update Data','Generating UUIDs for all rows in program_workflow_state table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-relationship','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','76db1fe2b15c4e35c0cc6accb385ca','Update Data','Generating UUIDs for all rows in relationship table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-relationship_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','716f58764be9b098e3af891951c','Update Data','Generating UUIDs for all rows in relationship_type table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-report_object','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','acf4b4d7f2f06953fae294bfad74e5b9','Update Data','Generating UUIDs for all rows in report_object table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-report_schema_xml','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','9be2c79cdb24345c90a55ecf9786d1','Update Data','Generating UUIDs for all rows in report_schema_xml table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-role','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','7516c06f5ac23efd21394154e4f157d2','Update Data','Generating UUIDs for all rows in role table via built in uuid function.',NULL,'1.9.4'),('20090402-1516-serialized_object','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','e872c1f23acaea7d2953ab948d3a4b81','Update Data','Generating UUIDs for all rows in serialized_object table via built in uuid function.',NULL,'1.9.4'),('20090402-1517','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','89cc7a14b0582f157ea2dbb9de092fd','Custom Change','Adding UUIDs to all rows in all columns via a java class. (This will take a long time on large databases)',NULL,'1.9.4'),('20090402-1518','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','dfaf705d7776613e619dedf3e06d7640','Add Not-Null Constraint (x52)','Now that UUID generation is done, set the uuid columns to not \"NOT NULL\"',NULL,'1.9.4'),('20090402-1519-cohort','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:04','24d3119128e95cb2e5a1a76bfdc54','Create Index','Creating unique index on cohort.uuid column',NULL,'1.9.4'),('20090402-1519-concept','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','52b46db1629e2ba9581d2ff5569238f','Create Index','Creating unique index on concept.uuid column',NULL,'1.9.4'),('20090402-1519-concept_answer','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','fda1ed7f30fa23da5a9013529bbf1c3b','Create Index','Creating unique index on concept_answer.uuid column',NULL,'1.9.4'),('20090402-1519-concept_class','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','1451a7423139d4a59245f5754cd63dac','Create Index','Creating unique index on concept_class.uuid column',NULL,'1.9.4'),('20090402-1519-concept_datatype','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','7d6afd51a3308f3d7619676eb55e4be6','Create Index','Creating unique index on concept_datatype.uuid column',NULL,'1.9.4'),('20090402-1519-concept_description','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','2546c2e71f8999d46d31c112507ebf87','Create Index','Creating unique index on concept_description.uuid column',NULL,'1.9.4'),('20090402-1519-concept_map','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','61c1fac89a8e525494df82743285432','Create Index','Creating unique index on concept_map.uuid column',NULL,'1.9.4'),('20090402-1519-concept_name','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','3be74640f8f2d5993afb193b3fe3075','Create Index','Creating unique index on concept_name.uuid column',NULL,'1.9.4'),('20090402-1519-concept_name_tag','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','f58b6b51d744b015862e9835369c14f6','Create Index','Creating unique index on concept_name_tag.uuid column',NULL,'1.9.4'),('20090402-1519-concept_proposal','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','b7c251ed66288c2486d39b2e8e5056','Create Index','Creating unique index on concept_proposal.uuid column',NULL,'1.9.4'),('20090402-1519-concept_set','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','c99542d48ce96e6f351cc158a814ed0','Create Index','Creating unique index on concept_set.uuid column',NULL,'1.9.4'),('20090402-1519-concept_source','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','7ca56bc86099e23ed197af71e423b44b','Create Index','Creating unique index on concept_source.uuid column',NULL,'1.9.4'),('20090402-1519-concept_state_conversion','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','311f46b442c5740393c13b924cf4d2','Create Index','Creating unique index on concept_state_conversion.uuid column',NULL,'1.9.4'),('20090402-1519-drug','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','b4867846580078e85629b9554c9845','Create Index','Creating unique index on drug.uuid column',NULL,'1.9.4'),('20090402-1519-encounter','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','b0a5fc73f82532e4f2b392e95a9d1e9d','Create Index','Creating unique index on encounter.uuid column',NULL,'1.9.4'),('20090402-1519-encounter_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','5ed4dbb39ded5667d9897849eb609162','Create Index','Creating unique index on encounter_type.uuid column',NULL,'1.9.4'),('20090402-1519-field','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','f1bb647f7e95324ccdf6a8f644a36d47','Create Index','Creating unique index on field.uuid column',NULL,'1.9.4'),('20090402-1519-field_answer','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:05','d3dde65374ee7222c83b6487f936a4f','Create Index','Creating unique index on field_answer.uuid column',NULL,'1.9.4'),('20090402-1519-field_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','559a6a362f1de933a495f7c281bd28','Create Index','Creating unique index on field_type.uuid column',NULL,'1.9.4'),('20090402-1519-form','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','854ea11fedf3e622cec570e54de222','Create Index','Creating unique index on form.uuid column',NULL,'1.9.4'),('20090402-1519-form_field','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','243c552b3a5c3011b214d261bd32c5e','Create Index','Creating unique index on form_field.uuid column',NULL,'1.9.4'),('20090402-1519-global_property','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','edf3a71ce9d58b7b5c86889cdee84e','Create Index','Creating unique index on global_property.uuid column',NULL,'1.9.4'),('20090402-1519-hl7_in_archive','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','e3e811242f2c8ef055756866aafaed70','Create Index','Creating unique index on hl7_in_archive.uuid column',NULL,'1.9.4'),('20090402-1519-hl7_in_error','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','d4b3b34289fe9a4b7c609f495cdf20','Create Index','Creating unique index on hl7_in_error.uuid column',NULL,'1.9.4'),('20090402-1519-hl7_in_queue','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','21b688a853a9f43c5ad24a53df7fd8c','Create Index','Creating unique index on hl7_in_queue.uuid column',NULL,'1.9.4'),('20090402-1519-hl7_source','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','b5457643d13df39dac482d34fc884','Create Index','Creating unique index on hl7_source.uuid column',NULL,'1.9.4'),('20090402-1519-location','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','a6c5a18e65815abaa42ee07e792f1a','Create Index','Creating unique index on location.uuid column',NULL,'1.9.4'),('20090402-1519-location_tag','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','abf99b851bdc38952843b2635104cfc','Create Index','Creating unique index on location_tag.uuid column',NULL,'1.9.4'),('20090402-1519-note','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','526b32a1d8d77223bd45a722746d246','Create Index','Creating unique index on note.uuid column',NULL,'1.9.4'),('20090402-1519-notification_alert','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','f87f2b6d5f5074c33245321cfbb5878','Create Index','Creating unique index on notification_alert.uuid column',NULL,'1.9.4'),('20090402-1519-notification_template','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','add9db68c554fbc2b4360e69438d3a8','Create Index','Creating unique index on notification_template.uuid column',NULL,'1.9.4'),('20090402-1519-obs','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','7a584580843bfd1a170c061c65413c1','Create Index','Creating unique index on obs.uuid column',NULL,'1.9.4'),('20090402-1519-orders','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','8ffafd6886f4a5fc1515783eabec738','Create Index','Creating unique index on orders.uuid column',NULL,'1.9.4'),('20090402-1519-order_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:06','ae9ea28f147e50c959610dafa5ec25c','Create Index','Creating unique index on order_type.uuid column',NULL,'1.9.4'),('20090402-1519-patient_identifier','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','4b5861f5669e0f29087d617d27513f1','Create Index','Creating unique index on patient_identifier.uuid column',NULL,'1.9.4'),('20090402-1519-patient_identifier_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','73da1d5f87d0397bb396a9b3b2ee4ea','Create Index','Creating unique index on patient_identifier_type.uuid column',NULL,'1.9.4'),('20090402-1519-patient_program','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','34b7b01f4a362b637b239209f2a4820','Create Index','Creating unique index on patient_program.uuid column',NULL,'1.9.4'),('20090402-1519-patient_state','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','efc7e9ba6fc3626ad4afe9c9c41fc0','Create Index','Creating unique index on patient_state.uuid column',NULL,'1.9.4'),('20090402-1519-person','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','2a77bcf4862d901d292c5877dfce4f58','Create Index','Creating unique index on person.uuid column',NULL,'1.9.4'),('20090402-1519-person_address','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','3672c61c62cd26ef3a726613517f6234','Create Index','Creating unique index on person_address.uuid column',NULL,'1.9.4'),('20090402-1519-person_attribute','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','d77b26d4596f149a38d65f2be9fa6b51','Create Index','Creating unique index on person_attribute.uuid column',NULL,'1.9.4'),('20090402-1519-person_attribute_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','7affacc59b57fabf36578609cbc481c','Create Index','Creating unique index on person_attribute_type.uuid column',NULL,'1.9.4'),('20090402-1519-person_name','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','f98a895de49abac9dc5483f4de705adf','Create Index','Creating unique index on person_name.uuid column',NULL,'1.9.4'),('20090402-1519-privilege','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','e9406113927e90f14d8ea3aee7b265e1','Create Index','Creating unique index on privilege.uuid column',NULL,'1.9.4'),('20090402-1519-program','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','f34ba0c98814f03cff165d4b1c391312','Create Index','Creating unique index on program.uuid column',NULL,'1.9.4'),('20090402-1519-program_workflow','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','d537382486f2a3a870983bc2a7e72a4c','Create Index','Creating unique index on program_workflow.uuid column',NULL,'1.9.4'),('20090402-1519-program_workflow_state','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','8020f25b8e995b846d1c3c11e4232399','Create Index','Creating unique index on program_workflow_state.uuid column',NULL,'1.9.4'),('20090402-1519-relationship','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','e56ca578a8411decdfee74a91184d4f','Create Index','Creating unique index on relationship.uuid column',NULL,'1.9.4'),('20090402-1519-relationship_type','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','179c86d0bf953e67eac2c7cd235a11c','Create Index','Creating unique index on relationship_type.uuid column',NULL,'1.9.4'),('20090402-1519-report_object','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','7f15947ec522a8fe38517251fdcb4f27','Create Index','Creating unique index on report_object.uuid column',NULL,'1.9.4'),('20090402-1519-report_schema_xml','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','f1f2922dead3177b121670e26e2be414','Create Index','Creating unique index on report_schema_xml.uuid column',NULL,'1.9.4'),('20090402-1519-role','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:07','48679931cd75ae76824f0a194d011b8','Create Index','Creating unique index on role.uuid column',NULL,'1.9.4'),('20090402-1519-serialized_object','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:08','3457ded29450bcb294aea6848e244a1','Create Index','Creating unique index on serialized_object.uuid column',NULL,'1.9.4'),('20090408-1298','Cory McCarthy','liquibase-update-to-latest.xml','2013-02-06 12:17:01','a675aa3da8651979b233d4c4b8d7f1','Modify Column','Changed the datatype for encounter_type to \'text\' instead of just 50 chars',NULL,'1.9.4'),('200904091023','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:01','a1ee9e6f174e2d8e5ee076126135b7','Delete Data (x4)','Remove Manage Tribes and View Tribes privileges from the privilege table and role_privilege table.\n			The privileges will be recreated by the Tribe module if it is installed.',NULL,'1.9.4'),('20090414-0804','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:10','b5e187a86493a19ff33881d5c5adb61','Drop Foreign Key Constraint','Dropping foreign key on concept_set.concept_id table',NULL,'1.9.4'),('20090414-0805','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:10','afa3399786d9a6695173727b496ba2','Drop Primary Key','Dropping primary key on concept set table',NULL,'1.9.4'),('20090414-0806','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:10','3c192287a93261df2ef571d55d91a5e','Add Column','Adding new integer primary key to concept set table',NULL,'1.9.4'),('20090414-0807','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:10','548ba564f1cf1a5e4353bade7d0a71f','Create Index, Add Foreign Key Constraint','Adding index and foreign key to concept_set.concept_id column',NULL,'1.9.4'),('20090414-0808a','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:12','13cd2c97df1b7163cb18296c328110','Drop Foreign Key Constraint','Dropping foreign key on patient_identifier.patient_id column',NULL,'1.9.4'),('20090414-0808b','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:12','4a69eb4dc87ad51da7497646b55e09','Drop Primary Key','Dropping non-integer primary key on patient identifier table before adding a new integer primary key',NULL,'1.9.4'),('20090414-0809','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:12','912731dafcb26634d6737b05d1f428d','Add Column','Adding new integer primary key to patient identifier table',NULL,'1.9.4'),('20090414-0810','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:12','8ba5e84d1f85707b4a248a21cf7afe16','Create Index, Add Foreign Key Constraint','Adding index and foreign key on patient_identifier.patient_id column',NULL,'1.9.4'),('20090414-0811a','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:14','3b73a595618cc7d4279899c189aeab','Drop Foreign Key Constraint','Dropping foreign key on concept_word.concept_id column',NULL,'1.9.4'),('20090414-0811b','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:14','be823f8a9d48f3df0c83cbafaf803','Drop Primary Key','Dropping non-integer primary key on concept word table before adding new integer one',NULL,'1.9.4'),('20090414-0812','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:14','98a31beac32af7be584e5f35125ebe77','Add Column','Adding integer primary key to concept word table',NULL,'1.9.4'),('20090414-0812b','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:14','fa3df9294b026b6375cde2993bc936c','Add Foreign Key Constraint','Re-adding foreign key for concept_word.concept_name_id',NULL,'1.9.4'),('200904271042','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','1b8214c28c262ca31bb67549c2e29e3','Drop Column','Remove the now unused synonym column',NULL,'1.9.4'),('20090428-0811aa','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:14','8d1cd3bc4fbeb36c55125e68426804d','Drop Foreign Key Constraint','Removing concept_word.concept_name_id foreign key so that primary key can be changed to concept_word_id',NULL,'1.9.4'),('20090428-0854','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','9124601e6dc67a603d950617bd17cea','Add Foreign Key Constraint','Adding foreign key for concept_word.concept_id column',NULL,'1.9.4'),('200905071626','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:01','d2873b168b82edab25ab1080304772c9','Create Index','Add an index to the concept_word.concept_id column (This update may fail if it already exists)',NULL,'1.9.4'),('200905150814','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:01','52a64aaf6ac8af65b8023c0ea96162d','Delete Data','Deleting invalid concept words',NULL,'1.9.4'),('200905150821','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','d6dedef195a69f62fd1c73adcfd93146','Custom SQL','Deleting duplicate concept word keys',NULL,'1.9.4'),('200906301606','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','e0a8f2578ba4e6cbaf9a7f41465bd1b','Modify Column','Change person_attribute_type.sort_weight from an integer to a float',NULL,'1.9.4'),('200907161638-1','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','98edf5727177ea045dff438d7fca27','Modify Column','Change obs.value_numeric from a double(22,0) to a double',NULL,'1.9.4'),('200907161638-2','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','cd8d3abc418a18ff786b14f27798389','Modify Column','Change concept_numeric columns from a double(22,0) type to a double',NULL,'1.9.4'),('200907161638-3','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','8f30dc389272e5f94c34286683ba2c1','Modify Column','Change concept_set.sort_weight from a double(22,0) to a double',NULL,'1.9.4'),('200907161638-4','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','7875dcee562bcd9bb5ad49cddcd9241','Modify Column','Change concept_set_derived.sort_weight from a double(22,0) to a double',NULL,'1.9.4'),('200907161638-5','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','1b4a410e183ef30ba9d7b7ae965e2a1','Modify Column','Change drug table columns from a double(22,0) to a double',NULL,'1.9.4'),('200907161638-6','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','8e5c737f5d65e25f73354f4cd26523','Modify Column','Change drug_order.dose from a double(22,0) to a double',NULL,'1.9.4'),('200908291938-1','dthomas','liquibase-update-to-latest.xml','2013-02-06 12:17:19','5658a0964596d66e70804ddaa48bd77c','Modify Column','set hl7_code in ConceptSource to nullable column',NULL,'1.9.4'),('200908291938-2a','dthomas','liquibase-update-to-latest.xml','2013-02-06 12:17:19','4fff1cee1a3f13ccc2a13c5448f9b1c','Modify Column','set retired in ConceptSource to tinyint(1) not null',NULL,'1.9.4'),('20090831-1039-38-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','612afca3f7f1697d93248951bc8c7','Add Column','Adding \"uuid\" column to scheduler_task_config table',NULL,'1.9.4'),('20090831-1040-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','baf8889ac5cfe2670e6b9c4bb58a47a','Update Data','Generating UUIDs for all rows in scheduler_task_config table via built in uuid function.',NULL,'1.9.4'),('20090831-1041-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','b3e5e26774a54c2cb3397196bedee570','Custom Change','Adding UUIDs to all rows in scheduler_task_config table via a java class for non mysql/oracle/mssql databases.',NULL,'1.9.4'),('20090831-1042-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','11fe3a4afc8e8997caab8e71781e5b10','Add Not-Null Constraint','Now that UUID generation is done for scheduler_task_config, set the uuid column to not \"NOT NULL\"',NULL,'1.9.4'),('20090831-1043-scheduler_task_config','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:16','b8165a4b9f7ea28a6084d74aadbb9432','Create Index','Creating unique index on scheduler_task_config.uuid column',NULL,'1.9.4'),('20090907-1','Knoll_Frank','liquibase-update-to-latest.xml','2013-02-06 12:17:16','542a60e1a743881571dd1c86ed1c4','Rename Column','Rename the concept_source.date_voided column to date_retired',NULL,'1.9.4'),('20090907-2a','Knoll_Frank','liquibase-update-to-latest.xml','2013-02-06 12:17:18','eba5ce8a7d9634e6f528627a82d6b1','Drop Foreign Key Constraint','Remove the concept_source.voided_by foreign key constraint',NULL,'1.9.4'),('20090907-2b','Knoll_Frank','liquibase-update-to-latest.xml','2013-02-06 12:17:18','8412909f334c2221611ce07acfcb5a6','Rename Column, Add Foreign Key Constraint','Rename the concept_source.voided_by column to retired_by',NULL,'1.9.4'),('20090907-3','Knoll_Frank','liquibase-update-to-latest.xml','2013-02-06 12:17:18','627e3e7cc7d9a2aece268fa93bf88','Rename Column','Rename the concept_source.voided column to retired',NULL,'1.9.4'),('20090907-4','Knoll_Frank','liquibase-update-to-latest.xml','2013-02-06 12:17:19','74ad3e52598dfc9af0785c753ba818ac','Rename Column','Rename the concept_source.void_reason column to retire_reason',NULL,'1.9.4'),('200909281005-1','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:17:21','6e728239bd76fa58671bdb4d03ec346','Create Table','Create logic token table to store all registered token',NULL,'1.9.4'),('200909281005-2','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:17:23','c516c1b552f2fdace76762d81bb3bae6','Create Table','Create logic token tag table to store all tag associated with a token',NULL,'1.9.4'),('200909281005-3','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:17:25','5a8f5825e1de3fcbc5ed48f0b8cd3e77','Add Foreign Key Constraint','Adding foreign key for primary key of logic_rule_token_tag',NULL,'1.9.4'),('200909281005-4a','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:17:27','c1bd29bc82b2d227f9eb47f6676f28d','Drop Foreign Key Constraint','Removing bad foreign key for logic_rule_token.creator',NULL,'1.9.4'),('200909281005-4aa','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:17:29','bbc452fd8fa3ff3eaab4c5fbb2ac2fe2','Drop Foreign Key Constraint','Removing bad foreign key for logic_rule_token.creator',NULL,'1.9.4'),('200909281005-4b','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:17:31','ee57b86d35016d4a26116a195908d4d','Add Foreign Key Constraint','Adding correct foreign key for logic_rule_token.creator',NULL,'1.9.4'),('200909281005-5a','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:17:33','9d6119b183383447fe37c9494767927','Add Foreign Key Constraint','Adding foreign key for logic_rule_token.changed_by',NULL,'1.9.4'),('20091001-1023','rcrichton','liquibase-update-to-latest.xml','2013-02-06 12:17:39','6c5b675ed15654c61ad28b7794180c0','Add Column','add retired column to relationship_type table',NULL,'1.9.4'),('20091001-1024','rcrichton','liquibase-update-to-latest.xml','2013-02-06 12:17:39','cdee114b801e2fd29f1f906d3fa553c4','Add Column','add retired_by column to relationship_type table',NULL,'1.9.4'),('20091001-1025','rcrichton','liquibase-update-to-latest.xml','2013-02-06 12:17:41','ded86a7b7ba57a447fdb14ee12804','Add Foreign Key Constraint','Create the foreign key from the relationship.retired_by to users.user_id.',NULL,'1.9.4'),('20091001-1026','rcrichton','liquibase-update-to-latest.xml','2013-02-06 12:17:41','56da622349984de2d9d35dfe4f8c592b','Add Column','add date_retired column to relationship_type table',NULL,'1.9.4'),('20091001-1027','rcrichton','liquibase-update-to-latest.xml','2013-02-06 12:17:41','5c3441c4d4df1305e22a76a58b9e4','Add Column','add retire_reason column to relationship_type table',NULL,'1.9.4'),('200910271049-1','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:33','fe74f276e13dc72ddac24b3d5bfe7b73','Update Data (x5)','Setting core field types to have standard UUIDs',NULL,'1.9.4'),('200910271049-10','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:34','4990b358a988f5eb594e95f205e66','Update Data (x4)','Setting core roles to have standard UUIDs',NULL,'1.9.4'),('200910271049-2','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:33','537a62703f986d8a2db83317a3ac17bb','Update Data (x7)','Setting core person attribute types to have standard UUIDs',NULL,'1.9.4'),('200910271049-3','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:33','c2d6cd47499de2eaaad2bad5c11f9e1','Update Data (x4)','Setting core encounter types to have standard UUIDs',NULL,'1.9.4'),('200910271049-4','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:33','cbf2dfb5f6fec73a9efe9a591803a9c','Update Data (x12)','Setting core concept datatypes to have standard UUIDs',NULL,'1.9.4'),('200910271049-5','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:34','5c2629fbf1258fbb9bcd46e8327ee142','Update Data (x4)','Setting core relationship types to have standard UUIDs',NULL,'1.9.4'),('200910271049-6','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:34','c0dade1ffd2723e8441b6145f83346','Update Data (x15)','Setting core concept classes to have standard UUIDs',NULL,'1.9.4'),('200910271049-7','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:34','e768dab8025d6612bb581c86d95638a','Update Data (x2)','Setting core patient identifier types to have standard UUIDs',NULL,'1.9.4'),('200910271049-8','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:34','7d43ef855a73b2321b4fdb97190f7cb','Update Data','Setting core location to have standard UUIDs',NULL,'1.9.4'),('200910271049-9','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:34','9d61613fa6c3ad43f79f765d5a73e9','Update Data','Setting core hl7 source to have standard UUIDs',NULL,'1.9.4'),('200912031842','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:34','de4dd06c279ad749cb5f1de3ed45586','Drop Foreign Key Constraint, Add Foreign Key Constraint','Changing encounter.provider_id to reference person instead of users',NULL,'1.9.4'),('200912031846-1','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:36','3aff267520eec4d52e81ef7614b2da3','Add Column, Update Data','Adding person_id column to users table (if needed)',NULL,'1.9.4'),('200912031846-2','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:36','334f8c5891f058959155f2ed31da386b','Update Data, Add Not-Null Constraint','Populating users.person_id',NULL,'1.9.4'),('200912031846-3','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:38','f0ebbfbf2d223694de49da385ee203b','Add Foreign Key Constraint, Set Column as Auto-Increment','Restoring foreign key constraint on users.person_id',NULL,'1.9.4'),('200912071501-1','arthurs','liquibase-update-to-latest.xml','2013-02-06 12:17:34','38fc7314599265a8ac635daa063bc32','Update Data','Change name for patient.searchMaxResults global property to person.searchMaxResults',NULL,'1.9.4'),('200912091819','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:38','48846a9c134bd1da5b3234b4e1e041eb','Add Column, Add Foreign Key Constraint','Adding retired metadata columns to users table',NULL,'1.9.4'),('200912091820','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:38','3b8f7d29adb4b8ffdb9b4cd29877af1','Update Data','Migrating voided metadata to retired metadata for users table',NULL,'1.9.4'),('200912091821','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:39','fe9bd963d283aca8d01329a1b3595af1','Drop Foreign Key Constraint, Drop Column (x4)','Dropping voided metadata columns from users table',NULL,'1.9.4'),('200912140038','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:39','b2dc51da6fdb4a6d25763dccbd795dc0','Add Column','Adding \"uuid\" column to users table',NULL,'1.9.4'),('200912140039','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:39','28215abe10dd981bcd46c9d74e4cefe','Update Data','Generating UUIDs for all rows in users table via built in uuid function.',NULL,'1.9.4'),('200912140040','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:39','e45561523e219fe6c21194e37b9a9de6','Custom Change','Adding UUIDs to users table via a java class. (This will take a long time on large databases)',NULL,'1.9.4'),('200912141000-drug-add-date-changed','dkayiwa','liquibase-update-to-latest.xml','2013-02-06 12:18:09','24da58829fe2eaeb8fa9adff24c55831','Add Column','Add date_changed column to drug table',NULL,'1.9.4'),('200912141001-drug-add-changed-by','dkayiwa','liquibase-update-to-latest.xml','2013-02-06 12:18:09','ec28bf078443442b4a5bdb8f5c3839','Add Column, Add Foreign Key Constraint','Add changed_by column to drug table',NULL,'1.9.4'),('200912141552','madanmohan','liquibase-update-to-latest.xml','2013-02-06 12:17:34','8e2978ddf67343ed80a18f7e92799bc','Add Column, Add Foreign Key Constraint','Add changed_by column to encounter table',NULL,'1.9.4'),('200912141553','madanmohan','liquibase-update-to-latest.xml','2013-02-06 12:17:34','c3f6cfc84f19785b1d7ecdd2ccf492','Add Column','Add date_changed column to encounter table',NULL,'1.9.4'),('20091215-0208','sunbiz','liquibase-update-to-latest.xml','2013-02-06 12:17:41','da9945f3554e8dd667c4790276eb5ad','Custom SQL','Prune concepts rows orphaned in concept_numeric tables',NULL,'1.9.4'),('20091215-0209','jmiranda','liquibase-update-to-latest.xml','2013-02-06 12:17:41','2feb74f1ca3fc1f8bb9b7986c321feed','Custom SQL','Prune concepts rows orphaned in concept_complex tables',NULL,'1.9.4'),('20091215-0210','jmiranda','liquibase-update-to-latest.xml','2013-02-06 12:17:41','15cd4ca5b736a89f932456fc17494293','Custom SQL','Prune concepts rows orphaned in concept_derived tables',NULL,'1.9.4'),('200912151032','n.nehete','liquibase-update-to-latest.xml','2013-02-06 12:17:39','fb31da7c25d166f7564bd77926dd286','Add Not-Null Constraint','Encounter Type should not be null when saving an Encounter',NULL,'1.9.4'),('200912211118','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:17:34','2bb03ed55ad1af379452fd18d9b46f74','Add Column','Adding language column to concept_derived table',NULL,'1.9.4'),('201001072007','upul','liquibase-update-to-latest.xml','2013-02-06 12:17:39','afee6c716d0435895c0c98a98099e4','Add Column','Add last execution time column to scheduler_task_config table',NULL,'1.9.4'),('20100128-1','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:16','5bf382cc41fbfa92bb13abdadba3e15','Insert Row','Adding \'System Developer\' role again (see ticket #1499)',NULL,'1.9.4'),('20100128-2','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:16','b351df1ec993c3906d66ebe5db67d120','Update Data','Switching users back from \'Administrator\' to \'System Developer\' (see ticket #1499)',NULL,'1.9.4'),('20100128-3','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:16','6e35b6f3bf99975e6bc5ad988280ef','Delete Data','Deleting \'Administrator\' role (see ticket #1499)',NULL,'1.9.4'),('20100306-095513a','thilini.hg','liquibase-update-to-latest.xml','2013-02-06 12:17:43','2adf32b4543c0bc7c99a9528da09afe','Drop Foreign Key Constraint','Dropping unused foreign key from notification alert table',NULL,'1.9.4'),('20100306-095513b','thilini.hg','liquibase-update-to-latest.xml','2013-02-06 12:17:44','fd3940c7dfe3195055dd6be9381e8863','Drop Column','Dropping unused user_id column from notification alert table',NULL,'1.9.4'),('20100322-0908','syhaas','liquibase-update-to-latest.xml','2013-02-06 12:17:44','1e3b87e49b62a358b5289ce470ea0dc','Add Column, Update Data','Adding sort_weight column to concept_answers table and initially sets the sort_weight to the concept_answer_id',NULL,'1.9.4'),('20100323-192043','ricardosbarbosa','liquibase-update-to-latest.xml','2013-02-06 12:17:44','97bc5f19e765d66665f5478147716aa6','Update Data, Delete Data (x2)','Removing the duplicate privilege \'Add Concept Proposal\' in favor of \'Add Concept Proposals\'',NULL,'1.9.4'),('20100330-190413','ricardosbarbosa','liquibase-update-to-latest.xml','2013-02-06 12:17:44','2b508e1e4546813dcc3425e09e863be','Update Data, Delete Data (x2)','Removing the duplicate privilege \'Edit Concept Proposal\' in favor of \'Edit Concept Proposals\'',NULL,'1.9.4'),('20100412-2217','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:44','6ccd335bb8f1e3dfe5c9b62d018fb27','Add Column','Adding \"uuid\" column to notification_alert_recipient table',NULL,'1.9.4'),('20100412-2218','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:44','b04eb51018a426202057341c4430d851','Update Data','Generating UUIDs for all rows in notification_alert_recipient table via built in uuid function.',NULL,'1.9.4'),('20100412-2219','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:44','13e8f84e2a3ac2eb060e68376dbc19','Custom Change','Adding UUIDs to notification_alert_recipient table via a java class (if needed).',NULL,'1.9.4'),('20100412-2220','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:44','4df07eb79086636f8498cac5a168be2','Add Not-Null Constraint','Now that UUID generation is done, set the notification_alert_recipient.uuid column to not \"NOT NULL\"',NULL,'1.9.4'),('20100413-1509','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:44','f27227d57b9372076542621df1b23','Rename Column','Change location_tag.tag to location_tag.name',NULL,'1.9.4'),('20100415-forgotten-from-before','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:39','48ac37f91a13c53c558923d11c9cee','Add Not-Null Constraint','Adding not null constraint to users.uuid',NULL,'1.9.4'),('20100423-1402','slorenz','liquibase-update-to-latest.xml','2013-02-06 12:17:44','4a1427b91614d7b982182c47b347c834','Create Index','Add an index to the encounter.encounter_datetime column to speed up statistical\n			analysis.',NULL,'1.9.4'),('20100423-1406','slorenz','liquibase-update-to-latest.xml','2013-02-06 12:17:44','1df152ca6d1cace9092a2b04f4d3bd3','Create Index','Add an index to the obs.obs_datetime column to speed up statistical analysis.',NULL,'1.9.4'),('20100426-1947','syhaas','liquibase-update-to-latest.xml','2013-02-06 12:17:44','873ce8452d8fffff16df292347215ce8','Insert Row','Adding daemon user to users table',NULL,'1.9.4'),('20100427-1334','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:44','4a22228b56866b7412bf56bf53d4876','Modify Column','Changing the datatype of global_property.property for mysql databases',NULL,'1.9.4'),('20100512-1400','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:44','c068ace9c7909db24b354c7db191a3b3','Insert Row','Create core order_type for drug orders',NULL,'1.9.4'),('20100513-1947','syhaas','liquibase-update-to-latest.xml','2013-02-06 12:17:44','bd3c85b35c0c189d17a4edfb69479a','Delete Data (x2)','Removing scheduler.username and scheduler.password global properties',NULL,'1.9.4'),('20100517-1545','wyclif and djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:44','a4a006cfd7d2f3777c35a47c729a','Custom Change','Switch boolean concepts/observations to be stored as coded',NULL,'1.9.4'),('20100525-818-1','syhaas','liquibase-update-to-latest.xml','2013-02-06 12:17:47','ae19ea64c1936cc692aab61c895d6cf','Create Table, Add Foreign Key Constraint (x2)','Create active list type table.',NULL,'1.9.4'),('20100525-818-2','syhaas','liquibase-update-to-latest.xml','2013-02-06 12:17:49','681391d216a593f59201dfe225d7288','Create Table, Add Foreign Key Constraint (x7)','Create active list table',NULL,'1.9.4'),('20100525-818-3','syhaas','liquibase-update-to-latest.xml','2013-02-06 12:17:51','85178ccb515c9892c5cf4826c2778f73','Create Table, Add Foreign Key Constraint','Create allergen table',NULL,'1.9.4'),('20100525-818-4','syhaas','liquibase-update-to-latest.xml','2013-02-06 12:17:54','11996601ce66087a69ce6fba3938c63','Create Table','Create problem table',NULL,'1.9.4'),('20100525-818-5','syhaas','liquibase-update-to-latest.xml','2013-02-06 12:17:54','f67090e0c159e3c39418cb5d3ae3e82','Insert Row (x2)','Inserting default active list types',NULL,'1.9.4'),('20100526-1025','Harsha.cse','liquibase-update-to-latest.xml','2013-02-06 12:17:44','16f132945063bedfb4288b9f934f656e','Drop Not-Null Constraint (x2)','Drop Not-Null constraint from location column in Encounter and Obs table',NULL,'1.9.4'),('20100604-0933a','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:44','b61fce30223b9b7cc2766f60b3abfa49','Add Default Value','Changing the default value to 2 for \'message_state\' column in \'hl7_in_archive\' table',NULL,'1.9.4'),('20100604-0933b','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:44','45a4732916ca8fd35d3df9e320b2a333','Update Data','Converting 0 and 1 to 2 for \'message_state\' column in \'hl7_in_archive\' table',NULL,'1.9.4'),('20100607-1550a','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:54','d7635997e5f8fb833c733f7597ed203b','Add Column','Adding \'concept_name_type\' column to concept_name table',NULL,'1.9.4'),('20100607-1550b','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:54','4b3d935771a1703eeaf268dfbbb16da8','Add Column','Adding \'locale_preferred\' column to concept_name table',NULL,'1.9.4'),('20100607-1550c','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:56','f151486ccbf8cec7d2327d4cd674b9','Drop Foreign Key Constraint','Dropping foreign key constraint on concept_name_tag_map.concept_name_tag_id',NULL,'1.9.4'),('20100607-1550d','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:56','f9567368a5999517843d7b3f1fd03bce','Update Data, Delete Data (x2)','Setting the concept name type for short names',NULL,'1.9.4'),('20100607-1550e','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:56','e7ba70b44259f915524358042f5996','Update Data, Delete Data (x2)','Converting preferred names to FULLY_SPECIFIED names',NULL,'1.9.4'),('20100607-1550f','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:56','5ac0213d1b2a2d50d5a0e5e5e45eb10','Update Data, Delete Data (x2)','Converting concept names with country specific concept name tags to preferred names',NULL,'1.9.4'),('20100607-1550g','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:56','69da22ab528eb6dbc68c7eaaafa898','Delete Data (x2)','Deleting \'default\' and \'synonym\' concept name tags',NULL,'1.9.4'),('20100607-1550h','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:56','dadd4e593c3ed4ec37119a933c7e81c','Custom Change','Validating and attempting to fix invalid concepts and ConceptNames',NULL,'1.9.4'),('20100607-1550i','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:59','3133835f91f89b1d1b383ec21dad78fa','Add Foreign Key Constraint','Restoring foreign key constraint on concept_name_tag_map.concept_name_tag_id',NULL,'1.9.4'),('20100621-1443','jkeiper','liquibase-update-to-latest.xml','2013-02-06 12:17:59','5fc85cf41532f6ec8658f25954ee7d2d','Modify Column','Modify the error_details column of hl7_in_error to hold\n			stacktraces',NULL,'1.9.4'),('201008021047','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:59','89abf79d12ba23f8388a78f1f12bf2','Create Index','Add an index to the person_name.family_name2 to speed up patient and person searches',NULL,'1.9.4'),('201008201345','mseaton','liquibase-update-to-latest.xml','2013-02-06 12:17:59','7167c287f456287faab9b382c0caecc6','Custom Change','Validates Program Workflow States for possible configuration problems and reports warnings',NULL,'1.9.4'),('201008242121','misha680','liquibase-update-to-latest.xml','2013-02-06 12:17:59','4a3e4ae561a3e9a1b1131dcdbad2b313','Modify Column','Make person_name.person_id not NULLable',NULL,'1.9.4'),('20100924-1110','mseaton','liquibase-update-to-latest.xml','2013-02-06 12:17:59','deb3d45e5d143178be45776152517d3e','Add Column, Add Foreign Key Constraint','Add location_id column to patient_program table',NULL,'1.9.4'),('201009281047','misha680','liquibase-update-to-latest.xml','2013-02-06 12:17:59','fa4e5cc66ba97ec1d9ad37156591ff','Drop Column','Remove the now unused default_charge column',NULL,'1.9.4'),('201010051745','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:59','c401e7afbba90c8db5a422f994cb8d0','Update Data','Setting the global property \'patient.identifierRegex\' to an empty string',NULL,'1.9.4'),('201010051746','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:17:59','4535b642c1591f7114c3e02d9d7a37','Update Data','Setting the global property \'patient.identifierSuffix\' to an empty string',NULL,'1.9.4'),('201010151054','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:59','eb939ccae537abf05cfdb05c43b675e7','Create Index','Adding index to form.published column',NULL,'1.9.4'),('201010151055','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:59','861d3c98ab90a3c9a197343fce7ee11','Create Index','Adding index to form.retired column',NULL,'1.9.4'),('201010151056','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:59','51346ab9b4b6780c2c8ff8dc193b18f','Create Index','Adding multi column index on form.published and form.retired columns',NULL,'1.9.4'),('201010261143','crecabarren','liquibase-update-to-latest.xml','2013-02-06 12:17:59','b2fcb0991f6dfc4f79ee53e94798c2','Rename Column','Rename neighborhood_cell column to address3 and increase the size to 255 characters',NULL,'1.9.4'),('201010261145','crecabarren','liquibase-update-to-latest.xml','2013-02-06 12:17:59','6519b246e2343453ec336836c65d1','Rename Column','Rename township_division column to address4 and increase the size to 255 characters',NULL,'1.9.4'),('201010261147','crecabarren','liquibase-update-to-latest.xml','2013-02-06 12:17:59','3a39cf4d967d8670bd22c596d34859','Rename Column','Rename subregion column to address5 and increase the size to 255 characters',NULL,'1.9.4'),('201010261149','crecabarren','liquibase-update-to-latest.xml','2013-02-06 12:17:59','1a80befba2d24244ef24a171562ac82','Rename Column','Rename region column to address6 and increase the size to 255 characters',NULL,'1.9.4'),('201010261151','crecabarren','liquibase-update-to-latest.xml','2013-02-06 12:17:59','db73f1b9acdb7af277badc584754df','Rename Column','Rename neighborhood_cell column to address3 and increase the size to 255 characters',NULL,'1.9.4'),('201010261153','crecabarren','liquibase-update-to-latest.xml','2013-02-06 12:17:59','1c22a65ce145618429c7b57d9ee294b','Rename Column','Rename township_division column to address4 and increase the size to 255 characters',NULL,'1.9.4'),('201010261156','crecabarren','liquibase-update-to-latest.xml','2013-02-06 12:17:59','194dac315b5cb6964177d121659dd1d9','Rename Column','Rename subregion column to address5 and increase the size to 255 characters',NULL,'1.9.4'),('201010261159','crecabarren','liquibase-update-to-latest.xml','2013-02-06 12:17:59','db75c1bc82161f47b7831296871cb7','Rename Column','Rename region column to address6 and increase the size to 255 characters',NULL,'1.9.4'),('201011011600','jkeiper','liquibase-update-to-latest.xml','2013-02-06 12:18:00','c7c0354954e8e131a0e2f0aa692725b8','Create Index','Adding index to message_state column in HL7 archive table',NULL,'1.9.4'),('201011011605','jkeiper','liquibase-update-to-latest.xml','2013-02-06 12:18:00','15296bed5f5b94a941bf273ae6ecd3a','Custom Change','Moving \"deleted\" HL7s from HL7 archive table to queue table',NULL,'1.9.4'),('201012081716','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:18:02','eecf7bd9435b6f1b4a08318e22e96','Delete Data','Removing concept that are concept derived and the datatype',NULL,'1.9.4'),('201012081717','nribeka','liquibase-update-to-latest.xml','2013-02-06 12:18:04','842f7086de77f6b31d8f06d56cf56','Drop Table','Removing concept derived tables',NULL,'1.9.4'),('20101209-1721','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:18:00','6519d6c9a8c6f2f31c4a8784ac9c42a','Add Column','Add \'weight\' column to concept_word table',NULL,'1.9.4'),('20101209-1722','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:18:00','f8ea5691d93b71f1e9b0b3b823ac2f9a','Create Index','Adding index to concept_word.weight column',NULL,'1.9.4'),('20101209-1723','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:18:00','4d67c48557f5665a8c2be7e87e22913','Insert Row','Insert a row into the schedule_task_config table for the ConceptIndexUpdateTask',NULL,'1.9.4'),('20101209-1731','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:18:00','40dc15b89fa71ae2559d02d5a6b82f','Update Data','Setting the value of \'start_on_startup\' to trigger off conceptIndexUpdateTask on startup',NULL,'1.9.4'),('201012092009','djazayeri','liquibase-update-to-latest.xml','2013-02-06 12:17:59','435261f5d18756dba65d55f42528a4','Modify Column (x10)','Increasing length of address fields in person_address and location to 255',NULL,'1.9.4'),('201102280948','bwolfe','liquibase-update-to-latest.xml','2013-02-06 12:17:36','7248f6f988e3a1b5fd864a433b61aae','Drop Foreign Key Constraint','Removing the foreign key from users.user_id to person.person_id if it still exists',NULL,'1.9.4'),('20110329-2317','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:18:07','1cf77131c583d513d7513b604b8f3940','Delete Data','Removing \'View Encounters\' privilege from Anonymous user',NULL,'1.9.4'),('20110329-2318','wyclif','liquibase-update-to-latest.xml','2013-02-06 12:18:09','ca9b59cdedd44918453315a89962579e','Delete Data','Removing \'View Observations\' privilege from Anonymous user',NULL,'1.9.4'),('20111209-1400-deleting-non-existing-roles-from-role-role-table','raff','liquibase-update-to-latest.xml','2013-02-06 12:18:09','294cf622d5df41437c3da10bb18685f','Custom SQL','Deleting non-existing roles from the role_role table',NULL,'1.9.4'),('20120330-0954old','jkeiper','liquibase-update-to-latest.xml','2013-02-06 12:18:09','24d44456310727838d3ae7a83e4fe4d','Modify Column','Increase size of drug name column to 255 characters',NULL,'1.9.4'),('disable-foreign-key-checks','ben','liquibase-core-data.xml','2013-02-06 12:16:47','a5f57b3b22b63b75f613458ff51746e2','Custom SQL','',NULL,'1.9.4');
/*!40000 ALTER TABLE `liquibasechangelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liquibasechangeloglock`
--

DROP TABLE IF EXISTS `liquibasechangeloglock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `liquibasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` tinyint(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liquibasechangeloglock`
--

LOCK TABLES `liquibasechangeloglock` WRITE;
/*!40000 ALTER TABLE `liquibasechangeloglock` DISABLE KEYS */;
INSERT INTO `liquibasechangeloglock` VALUES (1,0,NULL,NULL);
/*!40000 ALTER TABLE `liquibasechangeloglock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) DEFAULT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city_village` varchar(255) DEFAULT NULL,
  `state_province` varchar(255) DEFAULT NULL,
  `postal_code` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `latitude` varchar(50) DEFAULT NULL,
  `longitude` varchar(50) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `county_district` varchar(255) DEFAULT NULL,
  `address3` varchar(255) DEFAULT NULL,
  `address6` varchar(255) DEFAULT NULL,
  `address5` varchar(255) DEFAULT NULL,
  `address4` varchar(255) DEFAULT NULL,
  `retired` tinyint(4) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `parent_location` int(11) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`location_id`),
  UNIQUE KEY `location_uuid_index` (`uuid`),
  KEY `name_of_location` (`name`),
  KEY `retired_status` (`retired`),
  KEY `user_who_created_location` (`creator`),
  KEY `user_who_retired_location` (`retired_by`),
  KEY `parent_location` (`parent_location`),
  CONSTRAINT `parent_location` FOREIGN KEY (`parent_location`) REFERENCES `location` (`location_id`),
  CONSTRAINT `user_who_created_location` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_location` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (1,'Unknown Location',NULL,'','','','','','',NULL,NULL,1,'2005-09-22 00:00:00',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'8d6c993e-c2cc-11de-8d13-0010c6dffd0f'),(2,'Clinic A','Clinic A','','','','','','','','',1,'2013-02-08 13:45:44',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'2dcf06f2-6e49-4a94-ad17-4dc2825e19d6'),(3,'Clinic B','Clinic B','','','','','','','','',1,'2013-02-08 13:45:51',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,'3a21ceda-4229-453e-8d83-bac55469b31a');
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_tag`
--

DROP TABLE IF EXISTS `location_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location_tag` (
  `location_tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`location_tag_id`),
  UNIQUE KEY `location_tag_uuid_index` (`uuid`),
  KEY `location_tag_creator` (`creator`),
  KEY `location_tag_retired_by` (`retired_by`),
  CONSTRAINT `location_tag_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `location_tag_retired_by` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_tag`
--

LOCK TABLES `location_tag` WRITE;
/*!40000 ALTER TABLE `location_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `location_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_tag_map`
--

DROP TABLE IF EXISTS `location_tag_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location_tag_map` (
  `location_id` int(11) NOT NULL,
  `location_tag_id` int(11) NOT NULL,
  PRIMARY KEY (`location_id`,`location_tag_id`),
  KEY `location_tag_map_tag` (`location_tag_id`),
  CONSTRAINT `location_tag_map_location` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `location_tag_map_tag` FOREIGN KEY (`location_tag_id`) REFERENCES `location_tag` (`location_tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_tag_map`
--

LOCK TABLES `location_tag_map` WRITE;
/*!40000 ALTER TABLE `location_tag_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `location_tag_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logic_rule_definition`
--

DROP TABLE IF EXISTS `logic_rule_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logic_rule_definition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(38) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `rule_content` varchar(2048) NOT NULL,
  `language` varchar(255) NOT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `creator for rule_definition` (`creator`),
  KEY `changed_by for rule_definition` (`changed_by`),
  KEY `retired_by for rule_definition` (`retired_by`),
  CONSTRAINT `creator for rule_definition` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `changed_by for rule_definition` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `retired_by for rule_definition` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logic_rule_definition`
--

LOCK TABLES `logic_rule_definition` WRITE;
/*!40000 ALTER TABLE `logic_rule_definition` DISABLE KEYS */;
/*!40000 ALTER TABLE `logic_rule_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logic_rule_token`
--

DROP TABLE IF EXISTS `logic_rule_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logic_rule_token` (
  `logic_rule_token_id` int(11) NOT NULL AUTO_INCREMENT,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL DEFAULT '0002-11-30 00:00:00',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `token` varchar(512) NOT NULL,
  `class_name` varchar(512) NOT NULL,
  `state` varchar(512) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`logic_rule_token_id`),
  UNIQUE KEY `logic_rule_token_uuid` (`uuid`),
  KEY `token_creator` (`creator`),
  KEY `token_changed_by` (`changed_by`),
  CONSTRAINT `token_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `person` (`person_id`),
  CONSTRAINT `token_creator` FOREIGN KEY (`creator`) REFERENCES `person` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logic_rule_token`
--

LOCK TABLES `logic_rule_token` WRITE;
/*!40000 ALTER TABLE `logic_rule_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `logic_rule_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logic_rule_token_tag`
--

DROP TABLE IF EXISTS `logic_rule_token_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logic_rule_token_tag` (
  `logic_rule_token_id` int(11) NOT NULL,
  `tag` varchar(512) NOT NULL,
  KEY `token_tag` (`logic_rule_token_id`),
  CONSTRAINT `token_tag` FOREIGN KEY (`logic_rule_token_id`) REFERENCES `logic_rule_token` (`logic_rule_token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logic_rule_token_tag`
--

LOCK TABLES `logic_rule_token_tag` WRITE;
/*!40000 ALTER TABLE `logic_rule_token_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `logic_rule_token_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logic_token_registration`
--

DROP TABLE IF EXISTS `logic_token_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logic_token_registration` (
  `token_registration_id` int(11) NOT NULL AUTO_INCREMENT,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL DEFAULT '0002-11-30 00:00:00',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `token` varchar(512) NOT NULL,
  `provider_class_name` varchar(512) NOT NULL,
  `provider_token` varchar(512) NOT NULL,
  `configuration` varchar(2000) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`token_registration_id`),
  UNIQUE KEY `uuid` (`uuid`),
  KEY `token_registration_creator` (`creator`),
  KEY `token_registration_changed_by` (`changed_by`),
  CONSTRAINT `token_registration_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `token_registration_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logic_token_registration`
--

LOCK TABLES `logic_token_registration` WRITE;
/*!40000 ALTER TABLE `logic_token_registration` DISABLE KEYS */;
INSERT INTO `logic_token_registration` VALUES (1,2,'2013-02-06 12:18:47',NULL,NULL,'encounterLocation','org.openmrs.logic.datasource.EncounterDataSource','encounterLocation','encounterLocation','ca834c32-23e5-4ab1-8380-bd9baa3fbcec'),(2,2,'2013-02-06 12:18:47',NULL,NULL,'encounterProvider','org.openmrs.logic.datasource.EncounterDataSource','encounterProvider','encounterProvider','fce0679a-5b8d-4800-b3b0-9a0c285f42c4'),(3,2,'2013-02-06 12:18:47',NULL,NULL,'encounter','org.openmrs.logic.datasource.EncounterDataSource','encounter','encounter','aafa8146-350b-485f-be4f-43271c58336e'),(4,2,'2013-02-06 12:18:47',NULL,NULL,'identifier','org.openmrs.logic.datasource.PatientDataSource','identifier','identifier','1e5b6b7e-7848-4467-be95-103c357cddf1'),(5,2,'2013-02-06 12:18:47',NULL,NULL,'family name','org.openmrs.logic.datasource.PersonDataSource','family name','family name','c5407128-ea01-4486-ab21-eb8a00d02157'),(6,2,'2013-02-06 12:18:47',NULL,NULL,'middle name','org.openmrs.logic.datasource.PersonDataSource','middle name','middle name','6341dd10-bdf0-4a4c-9a37-a07d07611fea'),(7,2,'2013-02-06 12:18:47',NULL,NULL,'death date','org.openmrs.logic.datasource.PersonDataSource','death date','death date','db1f8b92-edf3-4d99-888b-270e098002a8'),(8,2,'2013-02-06 12:18:47',NULL,NULL,'birthdate','org.openmrs.logic.datasource.PersonDataSource','birthdate','birthdate','cea34a00-cdf4-4de9-bf6f-2e225bb86658'),(9,2,'2013-02-06 12:18:47',NULL,NULL,'cause of death','org.openmrs.logic.datasource.PersonDataSource','cause of death','cause of death','b3d63b96-dff2-4286-9c4c-b3cea32521be'),(10,2,'2013-02-06 12:18:47',NULL,NULL,'birthdate estimated','org.openmrs.logic.datasource.PersonDataSource','birthdate estimated','birthdate estimated','9d06a2cf-b387-4d04-a622-84fb5603c4d5'),(11,2,'2013-02-06 12:18:47',NULL,NULL,'gender','org.openmrs.logic.datasource.PersonDataSource','gender','gender','f0e993e7-cb01-472f-9e29-fac295c6b56f'),(12,2,'2013-02-06 12:18:47',NULL,NULL,'family name2','org.openmrs.logic.datasource.PersonDataSource','family name2','family name2','4c1ff674-cd7b-46e8-bb7c-5182d91f8d25'),(13,2,'2013-02-06 12:18:47',NULL,NULL,'dead','org.openmrs.logic.datasource.PersonDataSource','dead','dead','add0aa98-74e6-4282-bad6-e0819fd21c54'),(14,2,'2013-02-06 12:18:47',NULL,NULL,'given name','org.openmrs.logic.datasource.PersonDataSource','given name','given name','7e8f3f8a-a711-4f65-8245-78a01e5eb987'),(15,2,'2013-02-06 12:18:47',NULL,NULL,'CURRENT STATE','org.openmrs.logic.datasource.ProgramDataSource','CURRENT STATE','CURRENT STATE','d1c746c1-a1dc-4df8-9d83-c63d460f5ad9'),(16,2,'2013-02-06 12:18:47',NULL,NULL,'PROGRAM ENROLLMENT','org.openmrs.logic.datasource.ProgramDataSource','PROGRAM ENROLLMENT','PROGRAM ENROLLMENT','f135ad4f-83c6-4619-b580-6d7106437503'),(17,2,'2013-02-06 12:18:47',NULL,NULL,'PROGRAM COMPLETION','org.openmrs.logic.datasource.ProgramDataSource','PROGRAM COMPLETION','PROGRAM COMPLETION','5b4ac2f6-1f65-4100-9fc7-92d2a4278b36');
/*!40000 ALTER TABLE `logic_token_registration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logic_token_registration_tag`
--

DROP TABLE IF EXISTS `logic_token_registration_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logic_token_registration_tag` (
  `token_registration_id` int(11) NOT NULL,
  `tag` varchar(512) NOT NULL,
  KEY `token_registration_tag` (`token_registration_id`),
  CONSTRAINT `token_registration_tag` FOREIGN KEY (`token_registration_id`) REFERENCES `logic_token_registration` (`token_registration_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logic_token_registration_tag`
--

LOCK TABLES `logic_token_registration_tag` WRITE;
/*!40000 ALTER TABLE `logic_token_registration_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `logic_token_registration_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `note` (
  `note_id` int(11) NOT NULL DEFAULT '0',
  `note_type` varchar(50) DEFAULT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `obs_id` int(11) DEFAULT NULL,
  `encounter_id` int(11) DEFAULT NULL,
  `text` text NOT NULL,
  `priority` int(11) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`note_id`),
  UNIQUE KEY `note_uuid_index` (`uuid`),
  KEY `user_who_changed_note` (`changed_by`),
  KEY `user_who_created_note` (`creator`),
  KEY `encounter_note` (`encounter_id`),
  KEY `obs_note` (`obs_id`),
  KEY `note_hierarchy` (`parent`),
  KEY `patient_note` (`patient_id`),
  CONSTRAINT `encounter_note` FOREIGN KEY (`encounter_id`) REFERENCES `encounter` (`encounter_id`),
  CONSTRAINT `note_hierarchy` FOREIGN KEY (`parent`) REFERENCES `note` (`note_id`),
  CONSTRAINT `obs_note` FOREIGN KEY (`obs_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `patient_note` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `user_who_changed_note` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_note` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
/*!40000 ALTER TABLE `note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_alert`
--

DROP TABLE IF EXISTS `notification_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_alert` (
  `alert_id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(512) NOT NULL,
  `satisfied_by_any` int(11) NOT NULL DEFAULT '0',
  `alert_read` int(11) NOT NULL DEFAULT '0',
  `date_to_expire` datetime DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`alert_id`),
  UNIQUE KEY `notification_alert_uuid_index` (`uuid`),
  KEY `user_who_changed_alert` (`changed_by`),
  KEY `alert_creator` (`creator`),
  CONSTRAINT `alert_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_alert` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_alert`
--

LOCK TABLES `notification_alert` WRITE;
/*!40000 ALTER TABLE `notification_alert` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_alert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_alert_recipient`
--

DROP TABLE IF EXISTS `notification_alert_recipient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_alert_recipient` (
  `alert_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `alert_read` int(11) NOT NULL DEFAULT '0',
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`alert_id`,`user_id`),
  KEY `alert_read_by_user` (`user_id`),
  CONSTRAINT `alert_read_by_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `id_of_alert` FOREIGN KEY (`alert_id`) REFERENCES `notification_alert` (`alert_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_alert_recipient`
--

LOCK TABLES `notification_alert_recipient` WRITE;
/*!40000 ALTER TABLE `notification_alert_recipient` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_alert_recipient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification_template`
--

DROP TABLE IF EXISTS `notification_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_template` (
  `template_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `template` text,
  `subject` varchar(100) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `recipients` varchar(512) DEFAULT NULL,
  `ordinal` int(11) DEFAULT '0',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`template_id`),
  UNIQUE KEY `notification_template_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification_template`
--

LOCK TABLES `notification_template` WRITE;
/*!40000 ALTER TABLE `notification_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `obs`
--

DROP TABLE IF EXISTS `obs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `obs` (
  `obs_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) NOT NULL,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `encounter_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `obs_datetime` datetime NOT NULL,
  `location_id` int(11) DEFAULT NULL,
  `obs_group_id` int(11) DEFAULT NULL,
  `accession_number` varchar(255) DEFAULT NULL,
  `value_group_id` int(11) DEFAULT NULL,
  `value_boolean` tinyint(4) DEFAULT NULL,
  `value_coded` int(11) DEFAULT NULL,
  `value_coded_name_id` int(11) DEFAULT NULL,
  `value_drug` int(11) DEFAULT NULL,
  `value_datetime` datetime DEFAULT NULL,
  `value_numeric` double DEFAULT NULL,
  `value_modifier` varchar(2) DEFAULT NULL,
  `value_text` text,
  `date_started` datetime DEFAULT NULL,
  `date_stopped` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `value_complex` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`obs_id`),
  UNIQUE KEY `obs_uuid_index` (`uuid`),
  KEY `obs_concept` (`concept_id`),
  KEY `obs_enterer` (`creator`),
  KEY `encounter_observations` (`encounter_id`),
  KEY `obs_location` (`location_id`),
  KEY `obs_grouping_id` (`obs_group_id`),
  KEY `obs_order` (`order_id`),
  KEY `person_obs` (`person_id`),
  KEY `answer_concept` (`value_coded`),
  KEY `obs_name_of_coded_value` (`value_coded_name_id`),
  KEY `answer_concept_drug` (`value_drug`),
  KEY `user_who_voided_obs` (`voided_by`),
  KEY `obs_datetime_idx` (`obs_datetime`),
  CONSTRAINT `answer_concept` FOREIGN KEY (`value_coded`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `answer_concept_drug` FOREIGN KEY (`value_drug`) REFERENCES `drug` (`drug_id`),
  CONSTRAINT `encounter_observations` FOREIGN KEY (`encounter_id`) REFERENCES `encounter` (`encounter_id`),
  CONSTRAINT `obs_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `obs_enterer` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `obs_grouping_id` FOREIGN KEY (`obs_group_id`) REFERENCES `obs` (`obs_id`),
  CONSTRAINT `obs_location` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `obs_name_of_coded_value` FOREIGN KEY (`value_coded_name_id`) REFERENCES `concept_name` (`concept_name_id`),
  CONSTRAINT `obs_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `person_obs` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `user_who_voided_obs` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `obs`
--

LOCK TABLES `obs` WRITE;
/*!40000 ALTER TABLE `obs` DISABLE KEYS */;
/*!40000 ALTER TABLE `obs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_type`
--

DROP TABLE IF EXISTS `order_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_type` (
  `order_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`order_type_id`),
  UNIQUE KEY `order_type_uuid_index` (`uuid`),
  KEY `retired_status` (`retired`),
  KEY `type_created_by` (`creator`),
  KEY `user_who_retired_order_type` (`retired_by`),
  CONSTRAINT `type_created_by` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_order_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_type`
--

LOCK TABLES `order_type` WRITE;
/*!40000 ALTER TABLE `order_type` DISABLE KEYS */;
INSERT INTO `order_type` VALUES (2,'Drug Order','An order for a medication to be given to the patient',1,'2010-05-12 00:00:00',0,NULL,NULL,NULL,'131168f4-15f5-102d-96e4-000c29c2a5d7');
/*!40000 ALTER TABLE `order_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_type_id` int(11) NOT NULL DEFAULT '0',
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `orderer` int(11) DEFAULT '0',
  `encounter_id` int(11) DEFAULT NULL,
  `instructions` text,
  `start_date` datetime DEFAULT NULL,
  `auto_expire_date` datetime DEFAULT NULL,
  `discontinued` smallint(6) NOT NULL DEFAULT '0',
  `discontinued_date` datetime DEFAULT NULL,
  `discontinued_by` int(11) DEFAULT NULL,
  `discontinued_reason` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `patient_id` int(11) NOT NULL,
  `accession_number` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `orders_uuid_index` (`uuid`),
  KEY `order_creator` (`creator`),
  KEY `user_who_discontinued_order` (`discontinued_by`),
  KEY `discontinued_because` (`discontinued_reason`),
  KEY `orders_in_encounter` (`encounter_id`),
  KEY `type_of_order` (`order_type_id`),
  KEY `orderer_not_drug` (`orderer`),
  KEY `order_for_patient` (`patient_id`),
  KEY `user_who_voided_order` (`voided_by`),
  CONSTRAINT `discontinued_because` FOREIGN KEY (`discontinued_reason`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `orderer_not_drug` FOREIGN KEY (`orderer`) REFERENCES `users` (`user_id`),
  CONSTRAINT `orders_in_encounter` FOREIGN KEY (`encounter_id`) REFERENCES `encounter` (`encounter_id`),
  CONSTRAINT `order_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `order_for_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `type_of_order` FOREIGN KEY (`order_type_id`) REFERENCES `order_type` (`order_type_id`),
  CONSTRAINT `user_who_discontinued_order` FOREIGN KEY (`discontinued_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_order` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `patient_id` int(11) NOT NULL AUTO_INCREMENT,
  `tribe` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`patient_id`),
  KEY `user_who_changed_pat` (`changed_by`),
  KEY `user_who_created_patient` (`creator`),
  KEY `belongs_to_tribe` (`tribe`),
  KEY `user_who_voided_patient` (`voided_by`),
  CONSTRAINT `belongs_to_tribe` FOREIGN KEY (`tribe`) REFERENCES `tribe` (`tribe_id`),
  CONSTRAINT `person_id_for_patient` FOREIGN KEY (`patient_id`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `user_who_changed_pat` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_patient` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_patient` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_identifier`
--

DROP TABLE IF EXISTS `patient_identifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_identifier` (
  `patient_identifier_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `identifier` varchar(50) NOT NULL DEFAULT '',
  `identifier_type` int(11) NOT NULL DEFAULT '0',
  `preferred` smallint(6) NOT NULL DEFAULT '0',
  `location_id` int(11) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`patient_identifier_id`),
  UNIQUE KEY `patient_identifier_uuid_index` (`uuid`),
  KEY `identifier_name` (`identifier`),
  KEY `identifier_creator` (`creator`),
  KEY `defines_identifier_type` (`identifier_type`),
  KEY `patient_identifier_ibfk_2` (`location_id`),
  KEY `identifier_voider` (`voided_by`),
  KEY `idx_patient_identifier_patient` (`patient_id`),
  CONSTRAINT `identifies_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  CONSTRAINT `defines_identifier_type` FOREIGN KEY (`identifier_type`) REFERENCES `patient_identifier_type` (`patient_identifier_type_id`),
  CONSTRAINT `identifier_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `identifier_voider` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_identifier_ibfk_2` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_identifier`
--

LOCK TABLES `patient_identifier` WRITE;
/*!40000 ALTER TABLE `patient_identifier` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient_identifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_identifier_type`
--

DROP TABLE IF EXISTS `patient_identifier_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_identifier_type` (
  `patient_identifier_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  `format` varchar(50) DEFAULT NULL,
  `check_digit` smallint(6) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `required` smallint(6) NOT NULL DEFAULT '0',
  `format_description` varchar(255) DEFAULT NULL,
  `validator` varchar(200) DEFAULT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`patient_identifier_type_id`),
  UNIQUE KEY `patient_identifier_type_uuid_index` (`uuid`),
  KEY `retired_status` (`retired`),
  KEY `type_creator` (`creator`),
  KEY `user_who_retired_patient_identifier_type` (`retired_by`),
  CONSTRAINT `type_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_patient_identifier_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_identifier_type`
--

LOCK TABLES `patient_identifier_type` WRITE;
/*!40000 ALTER TABLE `patient_identifier_type` DISABLE KEYS */;
INSERT INTO `patient_identifier_type` VALUES (1,'OpenMRS Identification Number','Unique number used in OpenMRS','',1,1,'2005-09-22 00:00:00',0,NULL,'org.openmrs.patient.impl.LuhnIdentifierValidator',0,NULL,NULL,NULL,'8d793bee-c2cc-11de-8d13-0010c6dffd0f'),(2,'Old Identification Number','Number given out prior to the OpenMRS system (No check digit)','',0,1,'2005-09-22 00:00:00',0,NULL,NULL,0,NULL,NULL,NULL,'8d79403a-c2cc-11de-8d13-0010c6dffd0f'),(3,'MoTeCH Id','MoTeCH Id','',0,1,'2013-02-06 12:18:47',0,'','',0,NULL,NULL,NULL,'918c8264-bd04-4a84-9c46-baa4abc8ef6e');
/*!40000 ALTER TABLE `patient_identifier_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_program`
--

DROP TABLE IF EXISTS `patient_program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_program` (
  `patient_program_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) NOT NULL DEFAULT '0',
  `program_id` int(11) NOT NULL DEFAULT '0',
  `date_enrolled` datetime DEFAULT NULL,
  `date_completed` datetime DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `location_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`patient_program_id`),
  UNIQUE KEY `patient_program_uuid_index` (`uuid`),
  KEY `user_who_changed` (`changed_by`),
  KEY `patient_program_creator` (`creator`),
  KEY `patient_in_program` (`patient_id`),
  KEY `program_for_patient` (`program_id`),
  KEY `user_who_voided_patient_program` (`voided_by`),
  KEY `patient_program_location_id` (`location_id`),
  CONSTRAINT `patient_program_location_id` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `patient_in_program` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`) ON UPDATE CASCADE,
  CONSTRAINT `patient_program_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `program_for_patient` FOREIGN KEY (`program_id`) REFERENCES `program` (`program_id`),
  CONSTRAINT `user_who_changed` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_patient_program` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_program`
--

LOCK TABLES `patient_program` WRITE;
/*!40000 ALTER TABLE `patient_program` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient_program` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_state`
--

DROP TABLE IF EXISTS `patient_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient_state` (
  `patient_state_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_program_id` int(11) NOT NULL DEFAULT '0',
  `state` int(11) NOT NULL DEFAULT '0',
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`patient_state_id`),
  UNIQUE KEY `patient_state_uuid_index` (`uuid`),
  KEY `patient_state_changer` (`changed_by`),
  KEY `patient_state_creator` (`creator`),
  KEY `patient_program_for_state` (`patient_program_id`),
  KEY `state_for_patient` (`state`),
  KEY `patient_state_voider` (`voided_by`),
  CONSTRAINT `patient_program_for_state` FOREIGN KEY (`patient_program_id`) REFERENCES `patient_program` (`patient_program_id`),
  CONSTRAINT `patient_state_changer` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_state_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_state_voider` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `state_for_patient` FOREIGN KEY (`state`) REFERENCES `program_workflow_state` (`program_workflow_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_state`
--

LOCK TABLES `patient_state` WRITE;
/*!40000 ALTER TABLE `patient_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `person_id` int(11) NOT NULL AUTO_INCREMENT,
  `gender` varchar(50) DEFAULT '',
  `birthdate` date DEFAULT NULL,
  `birthdate_estimated` smallint(6) NOT NULL DEFAULT '0',
  `dead` smallint(6) NOT NULL DEFAULT '0',
  `death_date` datetime DEFAULT NULL,
  `cause_of_death` int(11) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE KEY `person_uuid_index` (`uuid`),
  KEY `person_birthdate` (`birthdate`),
  KEY `person_death_date` (`death_date`),
  KEY `person_died_because` (`cause_of_death`),
  KEY `user_who_changed_person` (`changed_by`),
  KEY `user_who_created_person` (`creator`),
  KEY `user_who_voided_person` (`voided_by`),
  CONSTRAINT `person_died_because` FOREIGN KEY (`cause_of_death`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `user_who_changed_person` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_created_person` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_person` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'',NULL,0,0,NULL,NULL,1,'2005-01-01 00:00:00',NULL,NULL,0,NULL,NULL,NULL,'04f93310-7081-11e2-b348-00ff26c46bb6');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_address`
--

DROP TABLE IF EXISTS `person_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_address` (
  `person_address_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) DEFAULT NULL,
  `preferred` smallint(6) NOT NULL DEFAULT '0',
  `address1` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `city_village` varchar(255) DEFAULT NULL,
  `state_province` varchar(255) DEFAULT NULL,
  `postal_code` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `latitude` varchar(50) DEFAULT NULL,
  `longitude` varchar(50) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `county_district` varchar(255) DEFAULT NULL,
  `address3` varchar(255) DEFAULT NULL,
  `address6` varchar(255) DEFAULT NULL,
  `address5` varchar(255) DEFAULT NULL,
  `address4` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`person_address_id`),
  UNIQUE KEY `person_address_uuid_index` (`uuid`),
  KEY `patient_address_creator` (`creator`),
  KEY `address_for_person` (`person_id`),
  KEY `patient_address_void` (`voided_by`),
  CONSTRAINT `address_for_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `patient_address_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `patient_address_void` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_address`
--

LOCK TABLES `person_address` WRITE;
/*!40000 ALTER TABLE `person_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `person_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_attribute`
--

DROP TABLE IF EXISTS `person_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_attribute` (
  `person_attribute_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) NOT NULL DEFAULT '0',
  `value` varchar(50) NOT NULL DEFAULT '',
  `person_attribute_type_id` int(11) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`person_attribute_id`),
  UNIQUE KEY `person_attribute_uuid_index` (`uuid`),
  KEY `attribute_changer` (`changed_by`),
  KEY `attribute_creator` (`creator`),
  KEY `defines_attribute_type` (`person_attribute_type_id`),
  KEY `identifies_person` (`person_id`),
  KEY `attribute_voider` (`voided_by`),
  CONSTRAINT `attribute_changer` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `attribute_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `attribute_voider` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `defines_attribute_type` FOREIGN KEY (`person_attribute_type_id`) REFERENCES `person_attribute_type` (`person_attribute_type_id`),
  CONSTRAINT `identifies_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_attribute`
--

LOCK TABLES `person_attribute` WRITE;
/*!40000 ALTER TABLE `person_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `person_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_attribute_type`
--

DROP TABLE IF EXISTS `person_attribute_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_attribute_type` (
  `person_attribute_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  `format` varchar(50) DEFAULT NULL,
  `foreign_key` int(11) DEFAULT NULL,
  `searchable` smallint(6) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `edit_privilege` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `sort_weight` double DEFAULT NULL,
  PRIMARY KEY (`person_attribute_type_id`),
  UNIQUE KEY `person_attribute_type_uuid_index` (`uuid`),
  KEY `attribute_is_searchable` (`searchable`),
  KEY `name_of_attribute` (`name`),
  KEY `person_attribute_type_retired_status` (`retired`),
  KEY `attribute_type_changer` (`changed_by`),
  KEY `attribute_type_creator` (`creator`),
  KEY `user_who_retired_person_attribute_type` (`retired_by`),
  KEY `privilege_which_can_edit` (`edit_privilege`),
  CONSTRAINT `attribute_type_changer` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `attribute_type_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `privilege_which_can_edit` FOREIGN KEY (`edit_privilege`) REFERENCES `privilege` (`privilege`),
  CONSTRAINT `user_who_retired_person_attribute_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_attribute_type`
--

LOCK TABLES `person_attribute_type` WRITE;
/*!40000 ALTER TABLE `person_attribute_type` DISABLE KEYS */;
INSERT INTO `person_attribute_type` VALUES (1,'Race','Group of persons related by common descent or heredity','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,'8d871386-c2cc-11de-8d13-0010c6dffd0f',6),(2,'Birthplace','Location of persons birth','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,'8d8718c2-c2cc-11de-8d13-0010c6dffd0f',0),(3,'Citizenship','Country of which this person is a member','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,'8d871afc-c2cc-11de-8d13-0010c6dffd0f',1),(4,'Mother\'s Name','First or last name of this person\'s mother','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,'8d871d18-c2cc-11de-8d13-0010c6dffd0f',5),(5,'Civil Status','Marriage status of this person','org.openmrs.Concept',1054,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,'8d871f2a-c2cc-11de-8d13-0010c6dffd0f',2),(6,'Health District','District/region in which this patient\' home health center resides','java.lang.String',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,'8d872150-c2cc-11de-8d13-0010c6dffd0f',4),(7,'Health Center','Specific Location of this person\'s home health center.','org.openmrs.Location',0,0,1,'2007-05-04 00:00:00',NULL,NULL,0,NULL,NULL,NULL,NULL,'8d87236c-c2cc-11de-8d13-0010c6dffd0f',3),(8,'Phone Number','','java.lang.String',NULL,0,1,'2013-02-08 13:38:04',NULL,NULL,0,NULL,NULL,NULL,NULL,'6fa5a719-4433-43d2-9eff-99a5f23cc244',7),(9,'Pin','','java.lang.String',NULL,0,1,'2013-02-08 13:38:10',NULL,NULL,0,NULL,NULL,NULL,NULL,'6a874162-1b4c-4e36-bc8f-a1c2bab93889',8),(10,'Clinic','','java.lang.String',NULL,0,1,'2013-02-08 13:38:17',NULL,NULL,0,NULL,NULL,NULL,NULL,'d197ac84-c465-4adf-9a6c-c261519f22d6',9),(11,'Next Campaign','','java.lang.String',NULL,0,1,'2013-02-08 13:38:24',NULL,NULL,0,NULL,NULL,NULL,NULL,'2aaf4f73-c460-4939-8bef-903b3d0917e4',10);
/*!40000 ALTER TABLE `person_attribute_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_name`
--

DROP TABLE IF EXISTS `person_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_name` (
  `person_name_id` int(11) NOT NULL AUTO_INCREMENT,
  `preferred` smallint(6) NOT NULL DEFAULT '0',
  `person_id` int(11) NOT NULL,
  `prefix` varchar(50) DEFAULT NULL,
  `given_name` varchar(50) DEFAULT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `family_name_prefix` varchar(50) DEFAULT NULL,
  `family_name` varchar(50) DEFAULT NULL,
  `family_name2` varchar(50) DEFAULT NULL,
  `family_name_suffix` varchar(50) DEFAULT NULL,
  `degree` varchar(50) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`person_name_id`),
  UNIQUE KEY `person_name_uuid_index` (`uuid`),
  KEY `first_name` (`given_name`),
  KEY `last_name` (`family_name`),
  KEY `middle_name` (`middle_name`),
  KEY `user_who_made_name` (`creator`),
  KEY `name_for_person` (`person_id`),
  KEY `user_who_voided_name` (`voided_by`),
  KEY `family_name2` (`family_name2`),
  CONSTRAINT `name_for_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `user_who_made_name` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_name` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_name`
--

LOCK TABLES `person_name` WRITE;
/*!40000 ALTER TABLE `person_name` DISABLE KEYS */;
INSERT INTO `person_name` VALUES (1,1,1,NULL,'Super','',NULL,'User',NULL,NULL,NULL,1,'2005-01-01 00:00:00',0,NULL,NULL,NULL,NULL,NULL,'050180c8-7081-11e2-b348-00ff26c46bb6');
/*!40000 ALTER TABLE `person_name` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `privilege`
--

DROP TABLE IF EXISTS `privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `privilege` (
  `privilege` varchar(50) NOT NULL DEFAULT '',
  `description` varchar(250) NOT NULL DEFAULT '',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`privilege`),
  UNIQUE KEY `privilege_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `privilege`
--

LOCK TABLES `privilege` WRITE;
/*!40000 ALTER TABLE `privilege` DISABLE KEYS */;
INSERT INTO `privilege` VALUES ('Add Allergies','Add allergies','c9c3bbf9-86de-49b6-abdf-d8a990a40169'),('Add Cohorts','Able to add a cohort to the system','76181bb2-8fce-42af-a4b9-184c5cd6fa1c'),('Add Concept Proposals','Able to add concept proposals to the system','cb6cc9ce-b5f4-4a3e-904e-f7f86207971c'),('Add Encounters','Able to add patient encounters','cae5aa7d-1a78-4210-8a5b-1d1efe9fd6fa'),('Add Observations','Able to add patient observations','6ec53459-5e35-46d1-a0e9-2b89e46216a5'),('Add Orders','Able to add orders','5913c49c-c94a-4211-b0a4-37dc0d5c8e96'),('Add Patient Identifiers','Able to add patient identifiers','6040b14c-2b92-4cd2-a260-53d5971165f3'),('Add Patient Programs','Able to add patients to programs','455e34f6-bf6a-41a1-91e6-301b8579628e'),('Add Patients','Able to add patients','ed9c9d13-0396-43c9-a388-f1bf338134c7'),('Add People','Able to add person objects','d23aac9b-563d-48e7-a5f6-2f0b39bd6d9b'),('Add Problems','Add problems','fecc61e8-843d-4695-9124-9add5e6b7eb6'),('Add Relationships','Able to add relationships','f948f1c9-50d8-411d-b3d5-5467ffd98526'),('Add Users','Able to add users to OpenMRS','ff683f13-3960-448e-b521-c37942fe1abe'),('Delete Cohorts','Able to add a cohort to the system','63d3a8b0-1e73-40e1-94aa-abe73e13ba5f'),('Delete Concept Proposals','Able to delete concept proposals from the system','08206b4d-c052-4fa1-9973-1e5a21f78747'),('Delete Encounters','Able to delete patient encounters','c7c6044c-4b6a-46bd-98ea-88fd12d31191'),('Delete Observations','Able to delete patient observations','85cd8876-a438-4a8e-b9e3-f567483a460f'),('Delete Orders','Able to delete orders','09679d0e-21a5-4e91-880e-8cf2092ea3c0'),('Delete Patient Identifiers','Able to delete patient identifiers','a2b65777-2596-42ef-8bbd-7713289dea6e'),('Delete Patient Programs','Able to delete patients from programs','2753047e-079d-4632-9d55-460f793ce084'),('Delete Patients','Able to delete patients','d20663d2-6ec6-4cf3-8271-b1125f549072'),('Delete People','Able to delete objects','efaff104-0843-403a-ba2f-b1a9dd29b91e'),('Delete Relationships','Able to delete relationships','5cfd8339-b25b-478a-bfec-6d3507336e04'),('Delete Users','Able to delete users in OpenMRS','70f55b3e-c8ed-4cf2-b439-dd633c852172'),('Edit Allergies','Able to edit allergies','185cca87-34a4-4a53-89aa-4600fbb44838'),('Edit Cohorts','Able to add a cohort to the system','c4b6db06-9783-4a5e-83af-8e9172a8d810'),('Edit Concept Proposals','Able to edit concept proposals in the system','6dc676ef-9b42-4388-9e3e-1fb7bc7aee5d'),('Edit Encounters','Able to edit patient encounters','f1451867-7328-4982-bc23-7b9ee82f610d'),('Edit Observations','Able to edit patient observations','ea3229b7-e9c4-43e9-bd06-13bacc750b23'),('Edit Orders','Able to edit orders','642261d2-e084-4acd-8e3b-09dbc353a033'),('Edit Patient Identifiers','Able to edit patient identifiers','c4800582-6531-437f-9ae5-70bb11d087f7'),('Edit Patient Programs','Able to edit patients in programs','d2176f14-7936-4762-bf91-17aae57e7178'),('Edit Patients','Able to edit patients','5fd25148-cfca-4bfd-8563-c110535d356a'),('Edit People','Able to add person objects','f91fa056-2c1b-4394-adf2-f77f930962c4'),('Edit Problems','Able to edit problems','63747ebb-15d6-47fd-ac6c-09b75ddf9a3b'),('Edit Relationships','Able to edit relationships','b8220d81-c8f0-40b1-89c8-b473f18002b5'),('Edit User Passwords','Able to change the passwords of users in OpenMRS','46e4ba17-a7d9-4d29-b193-9725f37c7eee'),('Edit Users','Able to edit users in OpenMRS','f00e4dd4-3078-4d2b-8806-3faef6482cb9'),('Form Entry','Able to fill out forms','b07c474d-9e61-43f3-bb73-8bb548519eb1'),('Manage Alerts','Able to add/edit/delete user alerts','c174b087-e202-4c15-83ee-7b36bb26ab25'),('Manage Concept Classes','Able to add/edit/retire concept classes','62659b49-3d7f-405e-93fd-4a38e4077939'),('Manage Concept Datatypes','Able to add/edit/retire concept datatypes','245d3428-5a7d-4ad6-b1c7-262cdf4ddc9d'),('Manage Concept Name tags','Able to add/edit/delete concept name tags','d56617c5-ac9b-4fad-871d-e75ee4d8128a'),('Manage Concept Sources','Able to add/edit/delete concept sources','7ca57bfa-aecc-4e20-b017-4fe63e1f8776'),('Manage Concepts','Able to add/edit/delete concept entries','5d044744-9183-48da-9411-7553c2d8c70b'),('Manage Encounter Types','Able to add/edit/retire encounter types','ae203d03-e87f-4d20-8c4d-6b30b8c98fd3'),('Manage Field Types','Able to add/edit/retire field types','09f2da57-e10e-4d9c-b6ed-d6cc9c5dce11'),('Manage Forms','Able to add/edit/delete forms','c7b1142e-aca7-4b0a-be84-49a601ba01c2'),('Manage Global Properties','Able to add/edit global properties','36cb6433-9d3b-4786-a6fc-34e2e125e027'),('Manage Identifier Types','Able to add/edit/retire patient identifier types','1ba1a243-fff3-43a5-9962-38b96e3e03ff'),('Manage Implementation Id','Able to view/add/edit the implementation id for the system','deeea3b7-1190-482a-a0ab-4161300fe768'),('Manage Location Tags','Able to add/edit/delete location tags','617328c4-8970-4e3f-bd38-dffd84509088'),('Manage Locations','Able to add/edit/delete locations','31448767-8ff7-48d3-91e0-1e8fe4d8b971'),('Manage Modules','Able to add/remove modules to the system','96f59221-159c-498e-815e-d700d292d25b'),('Manage Order Types','Able to add/edit/retire order types','ffc0ff94-8e67-4398-b448-96cb624bcaf0'),('Manage Person Attribute Types','Able to add/edit/retire person attribute types','a559d135-ed7d-451c-a786-22ed264b0754'),('Manage Privileges','Able to add/edit/delete privileges','70a31a25-768e-4364-bf73-84e8c990a809'),('Manage Programs','Able to add/view/delete patient programs','20eba16d-0c29-4e99-a48b-06a2d02aa55d'),('Manage Relationship Types','Able to add/edit/retire relationship types','b0426bbb-9779-4b7c-8cc1-284d54c34b3b'),('Manage Roles','Able to add/edit/delete user roles','b91d19b0-847c-4297-b022-34255b9d88f7'),('Manage Rule Definitions','Allows creation and editing of user-defined rules','d1ae950d-fbc7-43d5-9b85-d6b51c6f4fee'),('Manage Scheduler','Able to add/edit/remove scheduled tasks','055a6754-131e-47ca-b408-956919f89e4d'),('Manage Tokens','Allows registering and removal of tokens','37d7944b-178e-4352-a33b-0cb52cb07e0e'),('Patient Dashboard - View Demographics Section','Able to view the \'Demographics\' tab on the patient dashboard','51de664a-678d-4bb5-a69a-2fc72d1b8134'),('Patient Dashboard - View Encounters Section','Able to view the \'Encounters\' tab on the patient dashboard','dfdee89e-d3c0-4f1d-960a-0d124e94ff46'),('Patient Dashboard - View Forms Section','Able to view the \'Forms\' tab on the patient dashboard','4d46e7f8-974e-444e-91ed-8a40a18d4b73'),('Patient Dashboard - View Graphs Section','Able to view the \'Graphs\' tab on the patient dashboard','7bae0d41-1512-49af-9f06-a56e66587e2e'),('Patient Dashboard - View Overview Section','Able to view the \'Overview\' tab on the patient dashboard','5a3cfaf6-0716-4d3a-a9b0-f3df31aa6145'),('Patient Dashboard - View Patient Summary','Able to view the \'Summary\' tab on the patient dashboard','6b0fb9f5-d31e-47ca-a5dd-92b8a09fd6be'),('Patient Dashboard - View Regimen Section','Able to view the \'Regimen\' tab on the patient dashboard','683cb349-b032-476d-975f-d424154c734d'),('Remove Allergies','Remove allergies','cd28cff7-ab98-470c-a4a9-a7c80fd5e047'),('Remove Problems','Remove problems','a64aefb4-4a2b-4d0f-be3a-f61cf370b348'),('View Administration Functions','Able to view the \'Administration\' link in the navigation bar','5150ece0-a691-46df-ad04-e9a1916ad0f5'),('View Allergies','Able to view allergies','81000d52-b7e9-4d6b-a916-9b1cd7f72106'),('View Concept Classes','Able to view concept classes','e32adbaa-55bc-451d-8b15-49157cc9f64b'),('View Concept Datatypes','Able to view concept datatypes','79e9de26-2180-4a39-90c2-6e8e061f8406'),('View Concept Proposals','Able to view concept proposals to the system','91fc80c3-cbea-4c28-a53d-4e93b0551f77'),('View Concept Sources','Able to view concept sources','249ab27f-e072-44e6-9a0d-af84048a5d2e'),('View Concepts','Able to view concept entries','a320bf9a-5bce-41e4-b4cc-e588b431730d'),('View Database Changes','Able to view database changes from the admin screen','65f37cf1-c9e0-4053-9108-54325bb937c1'),('View Encounter Types','Able to view encounter types','99aac953-6e84-4906-9fb9-f4ad0f6ed1af'),('View Encounters','Able to view patient encounters','fa88a411-8284-44db-9c96-20b98e1e4402'),('View Field Types','Able to view field types','6030374d-648d-40c3-8cc7-8ead2b97627c'),('View Forms','Able to view forms','0132c724-dd55-463e-b13d-936ab7a47018'),('View Global Properties','Able to view global properties on the administration screen','ad7c017a-06e6-4f87-8c63-049dac6e892c'),('View Identifier Types','Able to view patient identifier types','6e8a7e7f-5dc9-40fe-a4dd-3fc194ab1eac'),('View Locations','Able to view locations','7d16bcb1-1cf2-4138-9718-51fb13f57b79'),('View Navigation Menu','Able to view the navigation menu (Home, View Patients, Dictionary, Administration, My Profile','e4882bd1-ea4f-4974-8edc-bf4900c39c28'),('View Observations','Able to view patient observations','e93cc0f4-890e-4507-829a-cb5484e20a39'),('View Order Types','Able to view order types','c9fe40b6-318f-4eeb-81c2-16a9722a6e2b'),('View Orders','Able to view orders','aa6d7d9b-a195-4dc9-90b1-daa393a24405'),('View Patient Cohorts','Able to view patient cohorts','fddf1bcd-4d10-4c5a-a098-34d18cbdf1b3'),('View Patient Identifiers','Able to view patient identifiers','22cd8439-996b-4b4f-9c45-6e4d0bc6870f'),('View Patient Programs','Able to see which programs that patients are in','b0071f09-dd64-42db-9047-9a5dc204eba6'),('View Patients','Able to view patients','020d5add-357a-477f-8e09-f6da291bd942'),('View People','Able to view person objects','65810381-3dd4-4b26-86a4-51a89a26fd42'),('View Person Attribute Types','Able to view person attribute types','4291c4d0-1710-4094-bd0e-0775dea02270'),('View Privileges','Able to view user privileges','e65767e5-8825-417c-bdfd-058fdb563158'),('View Problems','Able to view problems','4617f534-66b5-4974-b1de-759ba8fc7a98'),('View Programs','Able to view patient programs','70d57e98-aa11-4559-a256-faa08baf4b59'),('View Relationship Types','Able to view relationship types','8546f211-9c51-4c22-8286-48e987922c13'),('View Relationships','Able to view relationships','3568a567-fbcc-4140-bdb8-0bf2ef0f0074'),('View Roles','Able to view user roles','a449151d-41cd-4bb3-8efc-957754b6b1e1'),('View Rule Definitions','Allows viewing of user-defined rules. (This privilege is not necessary to run rules under normal usage.)','ee3ef0fe-bbc6-4dc5-8251-f1fb17f93648'),('View Unpublished Forms','Able to view and fill out unpublished forms','9f49a4d5-e02d-46a4-a297-e693fc58d5f2'),('View Users','Able to view users in OpenMRS','eb6e6746-383d-44d7-8470-c22eeface684');
/*!40000 ALTER TABLE `privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `program`
--

DROP TABLE IF EXISTS `program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program` (
  `program_id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`program_id`),
  UNIQUE KEY `program_uuid_index` (`uuid`),
  KEY `user_who_changed_program` (`changed_by`),
  KEY `program_concept` (`concept_id`),
  KEY `program_creator` (`creator`),
  CONSTRAINT `program_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `program_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_program` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program`
--

LOCK TABLES `program` WRITE;
/*!40000 ALTER TABLE `program` DISABLE KEYS */;
/*!40000 ALTER TABLE `program` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `program_workflow`
--

DROP TABLE IF EXISTS `program_workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_workflow` (
  `program_workflow_id` int(11) NOT NULL AUTO_INCREMENT,
  `program_id` int(11) NOT NULL DEFAULT '0',
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`program_workflow_id`),
  UNIQUE KEY `program_workflow_uuid_index` (`uuid`),
  KEY `workflow_changed_by` (`changed_by`),
  KEY `workflow_concept` (`concept_id`),
  KEY `workflow_creator` (`creator`),
  KEY `program_for_workflow` (`program_id`),
  CONSTRAINT `program_for_workflow` FOREIGN KEY (`program_id`) REFERENCES `program` (`program_id`),
  CONSTRAINT `workflow_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `workflow_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `workflow_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program_workflow`
--

LOCK TABLES `program_workflow` WRITE;
/*!40000 ALTER TABLE `program_workflow` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_workflow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `program_workflow_state`
--

DROP TABLE IF EXISTS `program_workflow_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_workflow_state` (
  `program_workflow_state_id` int(11) NOT NULL AUTO_INCREMENT,
  `program_workflow_id` int(11) NOT NULL DEFAULT '0',
  `concept_id` int(11) NOT NULL DEFAULT '0',
  `initial` smallint(6) NOT NULL DEFAULT '0',
  `terminal` smallint(6) NOT NULL DEFAULT '0',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`program_workflow_state_id`),
  UNIQUE KEY `program_workflow_state_uuid_index` (`uuid`),
  KEY `state_changed_by` (`changed_by`),
  KEY `state_concept` (`concept_id`),
  KEY `state_creator` (`creator`),
  KEY `workflow_for_state` (`program_workflow_id`),
  CONSTRAINT `state_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `state_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`concept_id`),
  CONSTRAINT `state_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `workflow_for_state` FOREIGN KEY (`program_workflow_id`) REFERENCES `program_workflow` (`program_workflow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program_workflow_state`
--

LOCK TABLES `program_workflow_state` WRITE;
/*!40000 ALTER TABLE `program_workflow_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_workflow_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relationship`
--

DROP TABLE IF EXISTS `relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relationship` (
  `relationship_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_a` int(11) NOT NULL,
  `relationship` int(11) NOT NULL DEFAULT '0',
  `person_b` int(11) NOT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) DEFAULT NULL,
  PRIMARY KEY (`relationship_id`),
  UNIQUE KEY `relationship_uuid_index` (`uuid`),
  KEY `relation_creator` (`creator`),
  KEY `person_a` (`person_a`),
  KEY `person_b` (`person_b`),
  KEY `relationship_type_id` (`relationship`),
  KEY `relation_voider` (`voided_by`),
  CONSTRAINT `person_a` FOREIGN KEY (`person_a`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `person_b` FOREIGN KEY (`person_b`) REFERENCES `person` (`person_id`) ON UPDATE CASCADE,
  CONSTRAINT `relationship_type_id` FOREIGN KEY (`relationship`) REFERENCES `relationship_type` (`relationship_type_id`),
  CONSTRAINT `relation_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `relation_voider` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relationship`
--

LOCK TABLES `relationship` WRITE;
/*!40000 ALTER TABLE `relationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relationship_type`
--

DROP TABLE IF EXISTS `relationship_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relationship_type` (
  `relationship_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `a_is_to_b` varchar(50) NOT NULL,
  `b_is_to_a` varchar(50) NOT NULL,
  `preferred` int(11) NOT NULL DEFAULT '0',
  `weight` int(11) NOT NULL DEFAULT '0',
  `description` varchar(255) NOT NULL DEFAULT '',
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `uuid` char(38) NOT NULL,
  `retired` tinyint(1) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`relationship_type_id`),
  UNIQUE KEY `relationship_type_uuid_index` (`uuid`),
  KEY `user_who_created_rel` (`creator`),
  KEY `user_who_retired_relationship_type` (`retired_by`),
  CONSTRAINT `user_who_created_rel` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_relationship_type` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relationship_type`
--

LOCK TABLES `relationship_type` WRITE;
/*!40000 ALTER TABLE `relationship_type` DISABLE KEYS */;
INSERT INTO `relationship_type` VALUES (1,'Doctor','Patient',0,0,'Relationship from a primary care provider to the patient',1,'2007-05-04 00:00:00','8d919b58-c2cc-11de-8d13-0010c6dffd0f',0,NULL,NULL,NULL),(2,'Sibling','Sibling',0,0,'Relationship between brother/sister, brother/brother, and sister/sister',1,'2007-05-04 00:00:00','8d91a01c-c2cc-11de-8d13-0010c6dffd0f',0,NULL,NULL,NULL),(3,'Parent','Child',0,0,'Relationship from a mother/father to the child',1,'2007-05-04 00:00:00','8d91a210-c2cc-11de-8d13-0010c6dffd0f',0,NULL,NULL,NULL),(4,'Aunt/Uncle','Niece/Nephew',0,0,'',1,'2007-05-04 00:00:00','8d91a3dc-c2cc-11de-8d13-0010c6dffd0f',0,NULL,NULL,NULL);
/*!40000 ALTER TABLE `relationship_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_object`
--

DROP TABLE IF EXISTS `report_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_object` (
  `report_object_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `report_object_type` varchar(255) NOT NULL,
  `report_object_sub_type` varchar(255) NOT NULL,
  `xml_data` text,
  `creator` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `voided` smallint(6) NOT NULL DEFAULT '0',
  `voided_by` int(11) DEFAULT NULL,
  `date_voided` datetime DEFAULT NULL,
  `void_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`report_object_id`),
  UNIQUE KEY `report_object_uuid_index` (`uuid`),
  KEY `user_who_changed_report_object` (`changed_by`),
  KEY `report_object_creator` (`creator`),
  KEY `user_who_voided_report_object` (`voided_by`),
  CONSTRAINT `report_object_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_report_object` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_voided_report_object` FOREIGN KEY (`voided_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_object`
--

LOCK TABLES `report_object` WRITE;
/*!40000 ALTER TABLE `report_object` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_schema_xml`
--

DROP TABLE IF EXISTS `report_schema_xml`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_schema_xml` (
  `report_schema_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `xml_data` mediumtext NOT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`report_schema_id`),
  UNIQUE KEY `report_schema_xml_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_schema_xml`
--

LOCK TABLES `report_schema_xml` WRITE;
/*!40000 ALTER TABLE `report_schema_xml` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_schema_xml` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role` varchar(50) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`role`),
  UNIQUE KEY `role_uuid_index` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('Anonymous','Privileges for non-authenticated users.','78e9e043-65a1-4473-8598-4738f7cfe026'),('Authenticated','Privileges gained once authentication has been established.','ae2bb789-1a66-477d-ab8c-5668c23c42c8'),('Provider','All users with the \'Provider\' role will appear as options in the default Infopath ','8d94f280-c2cc-11de-8d13-0010c6dffd0f'),('System Developer','Developers of the OpenMRS .. have additional access to change fundamental structure of the database model.','8d94f852-c2cc-11de-8d13-0010c6dffd0f');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_privilege`
--

DROP TABLE IF EXISTS `role_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_privilege` (
  `role` varchar(50) NOT NULL DEFAULT '',
  `privilege` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`privilege`,`role`),
  KEY `role_privilege` (`role`),
  CONSTRAINT `role_privilege` FOREIGN KEY (`role`) REFERENCES `role` (`role`),
  CONSTRAINT `privilege_definitons` FOREIGN KEY (`privilege`) REFERENCES `privilege` (`privilege`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_privilege`
--

LOCK TABLES `role_privilege` WRITE;
/*!40000 ALTER TABLE `role_privilege` DISABLE KEYS */;
INSERT INTO `role_privilege` VALUES ('Authenticated','View Concept Classes'),('Authenticated','View Concept Datatypes'),('Authenticated','View Encounter Types'),('Authenticated','View Field Types'),('Authenticated','View Global Properties'),('Authenticated','View Identifier Types'),('Authenticated','View Locations'),('Authenticated','View Order Types'),('Authenticated','View Person Attribute Types'),('Authenticated','View Privileges'),('Authenticated','View Relationship Types'),('Authenticated','View Relationships'),('Authenticated','View Roles');
/*!40000 ALTER TABLE `role_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_role`
--

DROP TABLE IF EXISTS `role_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_role` (
  `parent_role` varchar(50) NOT NULL DEFAULT '',
  `child_role` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`parent_role`,`child_role`),
  KEY `inherited_role` (`child_role`),
  CONSTRAINT `parent_role` FOREIGN KEY (`parent_role`) REFERENCES `role` (`role`),
  CONSTRAINT `inherited_role` FOREIGN KEY (`child_role`) REFERENCES `role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_role`
--

LOCK TABLES `role_role` WRITE;
/*!40000 ALTER TABLE `role_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scheduler_task_config`
--

DROP TABLE IF EXISTS `scheduler_task_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_task_config` (
  `task_config_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `schedulable_class` text,
  `start_time` datetime DEFAULT NULL,
  `start_time_pattern` varchar(50) DEFAULT NULL,
  `repeat_interval` int(11) NOT NULL DEFAULT '0',
  `start_on_startup` int(11) NOT NULL DEFAULT '0',
  `started` int(11) NOT NULL DEFAULT '0',
  `created_by` int(11) DEFAULT '0',
  `date_created` datetime DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  `last_execution_time` datetime DEFAULT NULL,
  PRIMARY KEY (`task_config_id`),
  UNIQUE KEY `scheduler_task_config_uuid_index` (`uuid`),
  KEY `scheduler_changer` (`changed_by`),
  KEY `scheduler_creator` (`created_by`),
  CONSTRAINT `scheduler_changer` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `scheduler_creator` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scheduler_task_config`
--

LOCK TABLES `scheduler_task_config` WRITE;
/*!40000 ALTER TABLE `scheduler_task_config` DISABLE KEYS */;
INSERT INTO `scheduler_task_config` VALUES (1,'Update Concept Index','Iterates through the concept dictionary, re-creating the concept index (which are used for searcing). This task is started when using the \'Update Concept Index Storage\' page and no range is given.  This task stops itself when one iteration has completed.','org.openmrs.scheduler.tasks.ConceptIndexUpdateTask',NULL,NULL,0,0,0,1,'2005-01-01 00:00:00',2,'2013-02-06 12:18:20','7c75911e-0310-11e0-8222-18a905e044dc',NULL),(2,'Initialize Logic Rule Providers',NULL,'org.openmrs.logic.task.InitializeLogicRuleProvidersTask','2013-02-08 13:45:36',NULL,1999999999,0,0,NULL,'2013-02-06 12:18:47',NULL,'2013-02-08 13:46:20','652b3480-a99c-479c-bb93-5f1648c33534','2013-02-08 13:45:36');
/*!40000 ALTER TABLE `scheduler_task_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scheduler_task_config_property`
--

DROP TABLE IF EXISTS `scheduler_task_config_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler_task_config_property` (
  `task_config_property_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` text,
  `task_config_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`task_config_property_id`),
  KEY `task_config_for_property` (`task_config_id`),
  CONSTRAINT `task_config_for_property` FOREIGN KEY (`task_config_id`) REFERENCES `scheduler_task_config` (`task_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scheduler_task_config_property`
--

LOCK TABLES `scheduler_task_config_property` WRITE;
/*!40000 ALTER TABLE `scheduler_task_config_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `scheduler_task_config_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serialized_object`
--

DROP TABLE IF EXISTS `serialized_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serialized_object` (
  `serialized_object_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(5000) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `subtype` varchar(255) NOT NULL,
  `serialization_class` varchar(255) NOT NULL,
  `serialized_data` text NOT NULL,
  `date_created` datetime NOT NULL,
  `creator` int(11) NOT NULL,
  `date_changed` datetime DEFAULT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `retired` smallint(6) NOT NULL DEFAULT '0',
  `date_retired` datetime DEFAULT NULL,
  `retired_by` int(11) DEFAULT NULL,
  `retire_reason` varchar(1000) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`serialized_object_id`),
  UNIQUE KEY `serialized_object_uuid_index` (`uuid`),
  KEY `serialized_object_creator` (`creator`),
  KEY `serialized_object_changed_by` (`changed_by`),
  KEY `serialized_object_retired_by` (`retired_by`),
  CONSTRAINT `serialized_object_changed_by` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `serialized_object_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `serialized_object_retired_by` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serialized_object`
--

LOCK TABLES `serialized_object` WRITE;
/*!40000 ALTER TABLE `serialized_object` DISABLE KEYS */;
/*!40000 ALTER TABLE `serialized_object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tribe`
--

DROP TABLE IF EXISTS `tribe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tribe` (
  `tribe_id` int(11) NOT NULL AUTO_INCREMENT,
  `retired` tinyint(4) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`tribe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tribe`
--

LOCK TABLES `tribe` WRITE;
/*!40000 ALTER TABLE `tribe` DISABLE KEYS */;
/*!40000 ALTER TABLE `tribe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_property`
--

DROP TABLE IF EXISTS `user_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_property` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `property` varchar(100) NOT NULL DEFAULT '',
  `property_value` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`user_id`,`property`),
  CONSTRAINT `user_property` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_property`
--

LOCK TABLES `user_property` WRITE;
/*!40000 ALTER TABLE `user_property` DISABLE KEYS */;
INSERT INTO `user_property` VALUES (1,'lockoutTimestamp',''),(1,'loginAttempts','0');
/*!40000 ALTER TABLE `user_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `role` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`role`,`user_id`),
  KEY `user_role` (`user_id`),
  CONSTRAINT `user_role` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `role_definitions` FOREIGN KEY (`role`) REFERENCES `role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,'Provider'),(1,'System Developer');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `system_id` varchar(50) NOT NULL DEFAULT '',
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `salt` varchar(128) DEFAULT NULL,
  `secret_question` varchar(255) DEFAULT NULL,
  `secret_answer` varchar(255) DEFAULT NULL,
  `creator` int(11) NOT NULL DEFAULT '0',
  `date_created` datetime NOT NULL,
  `changed_by` int(11) DEFAULT NULL,
  `date_changed` datetime DEFAULT NULL,
  `person_id` int(11) DEFAULT NULL,
  `retired` tinyint(4) NOT NULL DEFAULT '0',
  `retired_by` int(11) DEFAULT NULL,
  `date_retired` datetime DEFAULT NULL,
  `retire_reason` varchar(255) DEFAULT NULL,
  `uuid` char(38) NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_who_changed_user` (`changed_by`),
  KEY `user_creator` (`creator`),
  KEY `person_id_for_user` (`person_id`),
  KEY `user_who_retired_this_user` (`retired_by`),
  CONSTRAINT `person_id_for_user` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `user_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_changed_user` FOREIGN KEY (`changed_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_who_retired_this_user` FOREIGN KEY (`retired_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','','2b6052d975d8e7560f92ca73d7fcd22789bae8bd8146ee1b67aaeb76cc3e1d8ed6b877873ab8f1d94985a508d33415a7a6c68afb4f3d585c3e0f5f1840e90b05','248cfad7ce32b755550d673145b5dc22aeffed9574e8e562af73b7cfd992dfc13d561f70b47292ec2bb9eff7c7b4fa783fa15bc8b949a2f679e10dcf17b1d6d1',NULL,NULL,1,'2005-01-01 00:00:00',1,'2013-02-06 12:18:17',1,0,NULL,NULL,NULL,'19e89168-7081-11e2-b348-00ff26c46bb6'),(2,'daemon','daemon',NULL,NULL,NULL,NULL,1,'2010-04-26 13:25:00',NULL,NULL,NULL,0,NULL,NULL,NULL,'A4F30A1B-5EB9-11DF-A648-37A07F9C90FB');
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

-- Dump completed on 2013-02-08 13:46:34
