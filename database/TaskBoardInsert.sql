SELECT * FROM taskboard.tasks;

INSERT INTO Tasks (taskTitle, taskDescription, taskAssignees, taskStatus) 
VALUES 
    ('TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0',
    'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5',
	'Assignees1Assignees2Assignees3','No Status'),
    ('Repository','','','To Do'),
    ('ดาต้าเบส','ສ້າງຖານຂໍ້ມູນ','あなた、彼、彼女','Doing'),
    ('_Infrastructure_','_Setup containers_','ไก่งวง กับ เพนกวิน','Done');
    
SELECT * FROM taskboard.tasks;

UPDATE Tasks SET taskStatus = 'Done' WHERE taskId = 1 ;