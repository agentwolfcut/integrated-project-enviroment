drop database taskboard;
create database taskBoard ;
use taskBoard;

CREATE TABLE Tasks (
    taskID INT AUTO_INCREMENT PRIMARY KEY,
    taskTitle VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    taskDescription VARCHAR(500) CHARACTER SET utf8mb4,
    taskAssignees VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    taskStatus ENUM('No Status', 'To Do', 'Doing', 'Done') NOT NULL DEFAULT 'No Status',
    createdOn DATETIME default current_timestamp NOT NULL,
    updatedOn DATETIME default now() on update now() NOT NULL,
    CONSTRAINT check_taskTitle_not_empty CHECK (LENGTH(TRIM(taskTitle)) > 0),
    CONSTRAINT check_taskDescription_length CHECK (LENGTH(taskDescription) <= 500),
    CONSTRAINT check_taskAssignees_length CHECK (LENGTH(taskAssignees) <= 120)
);

-- taskAssignees VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
-- taskAssignees VARCHAR(30) CHARACTER SET utf8mb4 ,
