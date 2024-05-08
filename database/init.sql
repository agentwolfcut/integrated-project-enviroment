drop database taskBoard;
create database taskBoard ;
use taskBoard;

CREATE TABLE Tasks (
    taskID INT AUTO_INCREMENT PRIMARY KEY,
    taskTitle VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    taskDescription VARCHAR(500) CHARACTER SET utf8mb4,
    taskAssignees VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    taskStatus ENUM('NO_STATUS', 'TO_DO', 'DOING', 'DONE') NOT NULL DEFAULT 'NO_STATUS',
    createdOn DATETIME default current_timestamp NOT NULL,
    updatedOn DATETIME default now() on update now() NOT NULL,
    CONSTRAINT check_taskTitle_not_empty CHECK (LENGTH(TRIM(taskTitle)) > 0)
);

INSERT INTO Tasks (taskTitle, taskDescription, taskAssignees, taskStatus, createdOn, updatedOn) VALUES 
('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0', 'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5', 'Assignees1Assignees2Assignees3', 'NO_STATUS', '2024-04-22 09:00:00', '2024-04-22 09:00:00'),
('Repository', NULL, NULL, 'TO_DO', '2024-04-22 09:05:00', '2024-04-22 14:00:00'),
('ดาต้าเบส', 'ສ້າງຖານຂໍ້ມູນ', 'あなた、彼、彼女 (私ではありません)', 'DOING', '2024-04-22 09:10:00', '2024-04-25 00:00:00'),
('_Infrastructure_', '_Setup containers_', 'ไก่งวง กับ เพนกวิน', 'DONE', '2024-04-22 09:15:00', '2024-04-22 10:00:00');
