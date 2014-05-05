/** iEtueri Database */

--
-- Table structure for table courses 
--
CREATE TABLE `courses` (
	`id` int(10) NOT NULL AUTO_INCREMENT,
	`name` varchar(20) NOT NULL,
	`numberSubjects` int(10) NOT NULL DEFAULT 0,
	`average` float(10) NOT NULL DEFAULT 0,
	`endDate` varchar(8) NOT NULL,
	`subjectesIds` text,
	PRIMARY KEY (`id`);
	);

--
-- Table structure for table subjects
--
CREATE TABLE `subjects` (
	`id` int(10) NOT NULL AUTO_INCREMENT,
	`courseId` int(10) NOT NULL,
	`subjectName` varchar(20) NOT NULL,
	`credits` float(10) NOT NULL DEFAULT 0,
	`average` float(10) NOT NULL DEFAULT 0,
	`teacher` varchar(20) NOT NULL,
	`note` float(10) NOT NULL DEFAULT 0,
	`noteNecessary` float(10) NOT NULL DEFAULT 0,
	`homeworksIds` text,
	`examsIds` text,
	`ponderationsIds` text,
	`schedulesIds` text,
	PRIMARY KEY (`id`);
	);

--
-- Table structure for table homeworks
--
CREATE TABLE `homeworks` (
	`id` int(10) NOT NULL AUTO_INCREMENT,
	`subjectId` int(10) NOT NULL,
	`homeworkName` varchar(20) NOT NULL,
	`description` varchar(40) NOT NULL DEFAULT 0,
	`endDate` varchar(8) NOT NULL DEFAULT 0,
	`priority` int(10) NOT NULL,
	`note` float(10) NOT NULL DEFAULT 0,
	`done` boolean NOT NULL DEFAULT false,
	`ponderationIds` text,
	PRIMARY KEY (`id`);
	);

--
-- Table structure for table exams
--
CREATE TABLE `exams` (
	`id` int(10) NOT NULL AUTO_INCREMENT,
	`subjectId` int(10) NOT NULL,
	`examName` varchar(20) NOT NULL,
	`endDate` varchar(8) NOT NULL DEFAULT 0,
	`note` float(10) NOT NULL DEFAULT 0,
	`noteNecessary` float(10) NOT NULL DEFAULT 0,
	`done` boolean NOT NULL DEFAULT false,
	`ponderationIds` text,
	PRIMARY KEY (`id`);
	);

-- 
-- Table structure for table ponderation
--
CREATE TABLE `ponderations` (
	`id` int(10) NOT NULL AUTO_INCREMENT,
	`ponderationName` varchar(20) NOT NULL,
	`subjectId` int(10) NOT NULL,
	`value` int(10) NOT NULL,
	PRIMARY KEY (`id`);
	);

--
-- Table structure for table schedules
--
CREATE TABLE `schedules` (
	`id` int(10) NOT NULL AUTO_INCREMENT,
	`subjectId` int(10) NOT NULL,
	`hourInit` varchar(5) NOT NULL,
	`hourEnd` varchar(5) NOT NULL DEFAULT 0,
	`dateInit` varchar(8) NOT NULL DEFAULT 0,
	`dateEnd` varchar(8) NOT NULL DEFAULT 0,
	`daysOfCalendar` text NOT NULL,
	PRIMARY KEY (`id`);
	);










