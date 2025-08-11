INSERT INTO Agent (id, agent001) VALUES (1, 'Alice');
INSERT INTO Agent (id, agent001) VALUES (2, 'Bob');
INSERT INTO Agent (id, agent001) VALUES (3, 'Charlie');


INSERT INTO Ticket (id, description, status, created_date_time, closed_date_time, agent_id, resolution_summary) VALUES
(100, 'Issue with login', 0, '2025-08-01 14:18:56', NULL, 1, NULL),
(201, 'Page not loading', 1, '2025-08-02 09:30:00', NULL, 2, NULL),
(301, 'Feature request', 2, '2025-07-25 11:00:00', '2025-07-26 15:45:00', 3, 'Feature added successfully'),
(401, 'Bug in checkout', 0, '2025-08-03 16:20:00', NULL, 1, NULL);
