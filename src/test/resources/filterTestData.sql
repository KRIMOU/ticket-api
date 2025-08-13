INSERT INTO Agent (agent_id, agent001) VALUES (1, 'Alice');
INSERT INTO Agent (agent_id, agent001) VALUES (2, 'Bob');
INSERT INTO Agent (agent_id, agent001) VALUES (3, 'Charlie');


INSERT INTO Ticket (id, description, status, created_date_time, closed_date_time, agent_id, resolution_summary) VALUES
(1, 'Issue with login', 0, '2025-08-01 14:18:56', NULL, 1, NULL),
(2, 'Page not loading', 1, '2025-08-02 09:30:00', NULL, 2, NULL),
(3, 'Feature request', 2, '2025-07-25 11:00:00', '2025-07-26 15:45:00', 3, 'Feature added successfully'),
(4, 'Bug in checkout', 0, '2025-08-03 16:20:00', NULL, 1, NULL);

insert into course(id, name, created_date, last_updated_date,is_deleted)
values(10001,'JPA in 50 Steps', CURRENT_DATE(), CURRENT_DATE(),false);
insert into course(id, name, created_date, last_updated_date,is_deleted)
values(10002,'Spring in 50 Steps', CURRENT_DATE(), CURRENT_DATE(),false);
insert into course(id, name, created_date, last_updated_date,is_deleted)
values(10003,'Spring Boot in 100 Steps', CURRENT_DATE(), CURRENT_DATE(),false);

INSERT INTO Passport(id , name)VALUES(1,'name');
INSERT INTO Student(id , name, passport_id)VALUES(1,'name' , 1);
insert into review(id,rating,description,course_id)
values(50001,'FIVE', 'Great Course',10001);
insert into review(id,rating,description,course_id)
values(50002,'FOUR', 'Wonderful Course',10001);
insert into review(id,rating,description,course_id)
values(50003,'FIVE', 'Awesome Course',10003);
