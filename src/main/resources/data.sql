-- users dummy data
INSERT INTO users (users_id, name, password, email, nickname, country, social, created_at)
VALUES
    ('users', 'John Doe', 'password', 'john.doe@example.com', 'Johnny', 'USA', 'facebook', '2023-07-29 00:00:00'),
    ('dummy', 'Jane Smith', 'password', 'jane.smith@example.com', 'Janny', 'Canada', 'linkedin', '2023-07-29 00:00:00');


-- video dummy data
INSERT INTO video (video_id, link, video_title, creator, duration, is_default, views, youtube_views, created_at)
VALUES
    (1, 'https://www.example.com/video1', 'Video Title 1', 'Creator 1', 100, false, 1000, 10000, '2023-08-01 00:00:00'),
    (2, 'https://www.example.com/video2', 'Video Title 2', 'Creator 2', 200, false, 2000, 20000, '2023-08-01 00:00:00'),
    (3, 'https://www.example.com/video3', 'Video Title 3', 'Creator 3', 300, true, 3000, 30000, '2023-08-01 00:00:00'),
    (4, 'https://www.example.com/video4', 'Video Title 4', 'Creator 4', 400, false, 4000, 40000, '2023-08-01 00:00:00'),
    (5, 'https://www.example.com/video5', 'Video Title 5', 'Creator 5', 500, true, 5000, 50000, '2023-08-01 00:00:00'),
    (6, 'https://www.example.com/video6', 'Video Title 6', 'Creator 6', 600, false, 6000, 60000, '2023-08-01 00:00:00'),
    (7, 'https://www.example.com/video7', 'Video Title 7', 'Creator 7', 700, false, 7000, 70000, '2023-08-01 00:00:00'),
    (8, 'https://www.example.com/video8', 'Video Title 8', 'Creator 8', 800, true, 8000, 80000, '2023-08-01 00:00:00'),
    (9, 'https://www.example.com/video9', 'Video Title 9', 'Creator 9', 900, false, 9000, 90000, '2023-08-01 00:00:00'),
    (10, 'https://www.example.com/video10', 'Video Title 10', 'Creator 10', 1000, false, 10000, 100000, '2023-08-01 00:00:00');

-- user_video dummy data
INSERT INTO User_Video (uservideo_id, created_at, updated_at, video_id, users_id) VALUES
                                                                                      (1, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 1, 'dummy'),
                                                                                      (2, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 2, 'dummy'),
                                                                                      (3, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 3, 'dummy'),
                                                                                      (4, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 4, 'dummy'),
                                                                                      (5, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 5, 'dummy'),
                                                                                      (6, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 1, 'users'),
                                                                                      (7, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 2, 'users'),
                                                                                      (8, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 3, 'users'),
                                                                                      (9, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 4, 'users'),
                                                                                      (10, '2023-08-01 00:00:00', '2023-08-01 00:00:00', 5, 'users');


-- Category Data
INSERT INTO category (category_id, category_name)
VALUES (1, 'Sports'),
       (2, 'Music'),
       (3, 'Travel'),
       (4, 'Cooking'),
       (5, 'Education');

-- CategoryVideo Data
INSERT INTO category_video (categoryvideo_id, video_id, category_id)
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 3, 3),
       (4, 4, 4),
       (5, 5, 5);

