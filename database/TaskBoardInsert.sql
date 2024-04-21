SELECT * FROM taskboard.tasks;

INSERT INTO Tasks (taskTitle, taskDescription, taskAssignees, taskStatus, createdOn, updatedOn)
VALUES 
('Design API specs for PBI1-2', 'Need to specify endpoints, request body, response body, status', 'Pang; Pong;', 'To Do', '2024-04-19 01:00:00', '2024-04-19 01:00:00'),
('Implement backend for authentication', 'Implement authentication logic using JWT tokens', 'John Doe', 'Doing', '2024-04-20 09:30:00', '2024-04-20 09:30:00');

INSERT INTO Tasks (taskTitle, taskDescription, taskAssignees, taskStatus, createdOn, updatedOn)
VALUES 
('ออกแบบสเปค API สำหรับ PBI1-2', 'ต้องระบุเอ็นด์พอยท์, ร้องขอข้อมูล, ตอบกลับข้อมูล, สถานะ', 'ป้าง; ป้อง;', 'To Do', '2024-04-19 01:00:00', '2024-04-19 01:00:00'),
('สร้างระบบการยืนยันตัวตนด้านหลัง', 'สร้างโลจิกการยืนยันตัวตนโดยใช้โทเค็น JWT', 'John Doe', 'Doing', '2024-04-20 09:30:00', '2024-04-20 09:30:00');
