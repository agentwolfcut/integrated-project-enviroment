drop database taskboard;
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
