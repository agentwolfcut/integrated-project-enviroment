drop database taskBoard;
create database taskBoard ;
use taskBoard;

-- CREATE TABLE Tasks (
--     taskID INT AUTO_INCREMENT PRIMARY KEY,
--     taskTitle VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
--     taskDescription VARCHAR(500) CHARACTER SET utf8mb4,
--     taskAssignees VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
--     taskStatus ENUM('NO_STATUS', 'TO_DO', 'DOING', 'DONE') NOT NULL DEFAULT 'NO_STATUS',
--     createdOn DATETIME default current_timestamp NOT NULL,
--     updatedOn DATETIME default now() on update now() NOT NULL,
--     CONSTRAINT check_taskTitle_not_empty CHECK (LENGTH(TRIM(taskTitle)) > 0)
-- );

-- INSERT INTO Tasks (taskTitle, taskDescription, taskAssignees, taskStatus, createdOn, updatedOn) VALUES 
-- ('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0', 'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5', 'Assignees1Assignees2Assignees3', 'NO_STATUS', '2024-04-22 09:00:00', '2024-04-22 09:00:00'),
-- ('Repository', NULL, NULL, 'TO_DO', '2024-04-22 09:05:00', '2024-04-22 14:00:00'),
-- ('ดาต้าเบส', 'ສ້າງຖານຂໍ້ມູນ', 'あなた、彼、彼女 (私ではありません)', 'DOING', '2024-04-22 09:10:00', '2024-04-25 00:00:00'),
-- ('_Infrastructure_', '_Setup containers_', 'ไก่งวง กับ เพนกวิน', 'DONE', '2024-04-22 09:15:00', '2024-04-22 10:00:00');

CREATE TABLE `status` (
  `statusID` int NOT NULL AUTO_INCREMENT,
  `statusName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `statusDescription` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '',
  PRIMARY KEY (`statusID`),
  UNIQUE KEY `statusName` (`statusName`),
  CONSTRAINT `check_statusDescription_not_empty` CHECK ((length(trim(`statusDescription`)) > 0)),
  CONSTRAINT `check_statusName_not_empty` CHECK ((length(trim(`statusName`)) > 0))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `status` VALUES (1,'No Status','A status has not been assigned'),(2,'To Do','The task is included in the project'),(3,'In Progress','The task is being worked on'),(4,'Reviewing','The task is being reviewed'),(5,'Testing','The task is being tested'),(6,'Waiting','The task is waiting for a resource'),(7,'​Done','The task has been completed');


CREATE TABLE Tasks_v2 (
    taskID INT AUTO_INCREMENT PRIMARY KEY,
    taskTitle VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    taskDescription VARCHAR(500) CHARACTER SET utf8mb4,
    taskAssignees VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    statusID INT,
    createdOn DATETIME default current_timestamp NOT NULL,
    updatedOn DATETIME default now() on update now() NOT NULL,
    
    FOREIGN KEY (statusID) REFERENCES Status(statusID) ON DELETE restrict
);

INSERT INTO `tasks_v2` VALUES (1,'NS01',NULL,NULL,1,'2024-04-22 09:00:00','2024-05-28 16:19:04'),(2,'TD01',NULL,NULL,2,'2024-04-22 09:05:00','2024-05-28 23:18:37'),(3,'IP01',NULL,NULL,3,'2024-04-22 09:10:00','2024-05-28 16:19:22'),(4,'TD02',NULL,NULL,2,'2024-04-22 09:15:00','2024-05-28 16:19:38'),(5,'DO01',NULL,NULL,7,'2024-05-28 16:19:47','2024-05-28 16:19:47'),(6,'IP02',NULL,NULL,3,'2024-05-28 16:19:54','2024-05-28 16:19:54');
