SHOW VARIABLES LIKE 'character_set_system';

create database taskBoard ;
use taskBoard;
CREATE TABLE Tasks (
    taskID INT AUTO_INCREMENT PRIMARY KEY,
    taskTitle VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    taskDescription VARCHAR(500) CHARACTER SET utf8mb4,
    taskAssignees VARCHAR(30) CHARACTER SET utf8mb4,
    taskStatus ENUM('No Status', 'To Do', 'Doing', 'Done') NOT NULL DEFAULT 'No Status',
    createdOn DATETIME NOT NULL,
    updatedOn DATETIME NOT NULL,
    CONSTRAINT check_taskTitle_not_empty CHECK (LENGTH(TRIM(taskTitle)) > 0),
    CONSTRAINT check_taskDescription_length CHECK (LENGTH(taskDescription) <= 500),
    CONSTRAINT check_taskAssignees_length CHECK (LENGTH(taskAssignees) <= 30)
);

