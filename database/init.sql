DROP DATABASE IF EXISTS taskBoard;
CREATE DATABASE IF NOT EXISTS taskBoard;

USE taskBoard;

DROP TABLE IF EXISTS tasks;
CREATE TABLE tasks (
    taskID INT AUTO_INCREMENT PRIMARY KEY,
    taskTitle VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    taskDescription VARCHAR(500) CHARACTER SET utf8mb4,
    taskAssignees VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    taskStatus ENUM('No Status', 'To Do', 'Doing', 'Done') NOT NULL DEFAULT 'No Status',
    createdOn DATETIME default current_timestamp NOT NULL,
    updatedOn DATETIME default now() on update now() NOT NULL,
    CONSTRAINT check_taskTitle_not_empty CHECK (LENGTH(TRIM(taskTitle)) > 0),
    CONSTRAINT check_taskDescription_length CHECK (LENGTH(taskDescription) <= 500),
    CONSTRAINT check_taskAssignees_length CHECK (LENGTH(taskAssignees) <= 120)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO tasks (taskTitle, taskDescription, taskAssignees, taskStatus, createdOn, updatedOn) VALUES 
('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0', 'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5', 'Assignees1Assignees2Assignees3', 'No Status', '2024-04-22 09:00:00', '2024-04-22 09:00:00'),
('Repository', NULL, NULL, 'To Do', '2024-04-22 09:05:00', '2024-04-22 14:00:00'),
('ดาต้าเบส', 'ສ້າງຖານຂໍ້ມູນ', 'あなた、彼、彼女 (私ではありません)', 'Doing', '2024-04-22 09:10:00', '2024-04-25 00:00:00'),
('_Infrastructure_', '_Setup containers_', 'ไก่งวง กับ เพนกวิน', 'Done', '2024-04-22 09:15:00', '2024-04-22 10:00:00');
