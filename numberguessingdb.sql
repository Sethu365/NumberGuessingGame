CREATE DATABASE numberguessingdb;

USE numberguessingdb;

CREATE TABLE IF NOT EXISTS highscores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    score INT NOT NULL
);
select * from highscores;