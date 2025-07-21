CREATE DATABASE career_simulation;
 USE career_simulation;
 CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    current_stage INT DEFAULT 0,
    is_active_game BOOLEAN DEFAULT TRUE
 );
 CREATE TABLE player_stats (
    user_id INT,
    money INT DEFAULT 0,
    knowledge INT DEFAULT 0,
    energy INT DEFAULT 100,
    stress INT DEFAULT 0,
    reputation INT DEFAULT 0,
    career_title VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
 );
 CREATE TABLE game_choices (
    choice_id INT PRIMARY KEY AUTO_INCREMENT,
    stage INT NOT NULL,
    description TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL
 );
 CREATE TABLE choice_effects (
    choice_id INT,
    choice_option ENUM('A', 'B', 'C') NOT NULL, 	
    money_change INT DEFAULT 0,
    knowledge_change INT DEFAULT 0,
    FOREIGN KEY (choice_id) REFERENCES game_choices(choice_id)
);
-- STAGE 1
INSERT INTO game_choices (stage, description, option_a, option_b, option_c) VALUES
(1, 'Your internship ended. What next?', 'Join a startup', 'Prepare for govt exams', 'Go abroad for masters');

SET @choice_id = LAST_INSERT_ID();
INSERT INTO choice_effects (choice_id, choice_option, money_change, knowledge_change) VALUES
(@choice_id, 'A', 1200, 15),
(@choice_id, 'B', -100, 30),
(@choice_id, 'C', -800, 40);

-- STAGE 2
INSERT INTO game_choices (stage, description, option_a, option_b, option_c) VALUES
(2, 'You receive a job offer!', 'Accept it', 'Negotiate salary', 'Reject and wait');

SET @choice_id = LAST_INSERT_ID();
INSERT INTO choice_effects (choice_id, choice_option, money_change, knowledge_change) VALUES
(@choice_id, 'A', 2000, 5),
(@choice_id, 'B', 2500, 5),
(@choice_id, 'C', 0, 0);

-- STAGE 3
INSERT INTO game_choices (stage, description, option_a, option_b, option_c) VALUES
(3, 'You are offered to work on a team project.', 'Take the lead', 'Join passively', 'Avoid it');

SET @choice_id = LAST_INSERT_ID();
INSERT INTO choice_effects (choice_id, choice_option, money_change, knowledge_change) VALUES
(@choice_id, 'A', 500, 20),
(@choice_id, 'B', 300, 10),
(@choice_id, 'C', 0, 0);
