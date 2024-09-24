drop database taskBoard;
create database taskBoard ;
use taskBoard;

-- Create 'users' table
CREATE TABLE users (
    oid CHAR(36) NOT NULL PRIMARY KEY,  -- Universally unique identifier (UUID)
    name VARCHAR(100) NOT NULL,  -- User full name
    username VARCHAR(50) NOT NULL,  -- User name
    email VARCHAR(50) NOT NULL ,  -- User email
    created_on DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,  -- User creation timestamp
    updated_on DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL  -- User update timestamp
);

CREATE TABLE boards(
    id CHAR(10) NOT NULL PRIMARY KEY UNIQUE,
    name VARCHAR(120) CHARACTER SET utf8mb4 NOT NULL ,
    owner_id CHAR(36) NOT NULL,
    created_on DATETIME default current_timestamp NOT NULL,
    updated_on DATETIME default now() on update now() NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(oid) ON DELETE CASCADE
);

CREATE TABLE statuses (
  id int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
  board_id CHAR(10),
  name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  description  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '',
  created_on DATETIME default current_timestamp NOT NULL,
  updated_on DATETIME default now() on update now() NOT NULL,
  FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE
) ;

CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    board_id CHAR(10),
    title  VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
    description  VARCHAR(500) CHARACTER SET utf8mb4,
    assignees  VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    status_id INT not null,
    created_on DATETIME default current_timestamp NOT NULL,
    updated_on DATETIME default now() on update now() NOT NULL,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,  -- Foreign key to boards table
    FOREIGN KEY (status_id) REFERENCES statuses(id) ON DELETE RESTRICT 
);
