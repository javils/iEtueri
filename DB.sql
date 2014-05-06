/** iEtueri Database */

--
-- Table structure for table courses 
--
DROP TABLE IF EXISTS courses;
CREATE TABLE `courses` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(20) NOT NULL,
	`numberSubjects` INT(10) NOT NULL DEFAULT 0,
	`average` FLOAT(10) NOT NULL DEFAULT 0,
	`endDate` VARCHAR(8) NOT NULL,
	`subjectesIds` TEXT,
	PRIMARY KEY (`id`)
	);

--
-- Table structure for table subjects
--
DROP TABLE IF EXISTS subjects;
CREATE TABLE `subjects` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`courseId` INT(10) NOT NULL,
	`subjectName` VARCHAR(20) NOT NULL,
	`credits` FLOAT(10) NOT NULL DEFAULT 0,
	`average` FLOAT(10) NOT NULL DEFAULT 0,
	`teacher` VARCHAR(20) NOT NULL,
	`note` FLOAT(10) NOT NULL DEFAULT 0,
	`noteNecessary` FLOAT(10) NOT NULL DEFAULT 0,
	`homeworksIds` TEXT,
	`examsIds` TEXT,
	`ponderationsIds` TEXT,
	`schedulesIds` TEXT,
	PRIMARY KEY (`id`)
	);

--
-- Table structure for table homeworks
--
DROP TABLE IF EXISTS homeworks;
CREATE TABLE `homeworks` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`subjectId` INT(10) NOT NULL,
	`homeworkName` VARCHAR(20) NOT NULL,
	`description` VARCHAR(40) NOT NULL DEFAULT 0,
	`endDate` VARCHAR(8) NOT NULL DEFAULT 0,
	`priority` INT(10) NOT NULL,
	`note` FLOAT(10) NOT NULL DEFAULT 0,
	`done` BOOLEAN NOT NULL DEFAULT FALSE,
	`ponderationIds` TEXT,
	PRIMARY KEY (`id`)
	);

--
-- Table structure for table exams
--
DROP TABLE IF EXISTS exams;
CREATE TABLE `exams` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`subjectId` INT(10) NOT NULL,
	`examName` VARCHAR(20) NOT NULL,
	`endDate` VARCHAR(8) NOT NULL DEFAULT 0,
	`note` FLOAT(10) NOT NULL DEFAULT 0,
	`noteNecessary` FLOAT(10) NOT NULL DEFAULT 0,
	`done` BOOLEAN NOT NULL DEFAULT FALSE,
	`ponderationIds` TEXT,
	PRIMARY KEY (`id`)
	);

-- 
-- Table structure for table ponderation
--
DROP TABLE IF EXISTS ponderations;
CREATE TABLE `ponderations` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`ponderationName` VARCHAR(20) NOT NULL,
	`subjectId` INT(10) NOT NULL,
	`value` INT(10) NOT NULL,
	PRIMARY KEY (`id`)
	);

--
-- Table structure for table schedules
--
DROP TABLE IF EXISTS schedules;
CREATE TABLE `schedules` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`subjectId` INT(10) NOT NULL,
	`hourInit` VARCHAR(5) NOT NULL,
	`hourEnd` VARCHAR(5) NOT NULL DEFAULT 0,
	`dateInit` VARCHAR(8) NOT NULL DEFAULT 0,
	`dateEnd` VARCHAR(8) NOT NULL DEFAULT 0,
	`daysOfCalendar` TEXT NOT NULL,
	PRIMARY KEY (`id`)
	);










