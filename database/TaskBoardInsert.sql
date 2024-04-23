SELECT * FROM taskboard.tasks;

INSERT INTO Tasks (taskTitle, taskDescription, taskAssignees, taskStatus) 
VALUES 
    ('Implement Login Page', 'Create HTML/CSS for the login page and integrate with backend',
    'John', 'To Do');
    
UPDATE Tasks
SET taskStatus = 'To Do'
WHERE taskId = 1 ;

    
    
--     ('Design Database Schema', 'Define tables, relationships, and constraints for the database', 'Alice, Bob', 'Doing', NOW(), NOW()),
--     ('Refactor Codebase', 'Review existing code and refactor for better performance and readability', 'Carol', 'To Do', NOW(), NOW()),
--     ('Deploy Application to Production', 'Prepare deployment scripts and deploy the application to production servers', 'David', 'Doing', NOW(), NOW()),
--     ('Write User Documentation', 'Create user guides and documentation for the application', 'Emily', 'To Do', NOW(), NOW());
