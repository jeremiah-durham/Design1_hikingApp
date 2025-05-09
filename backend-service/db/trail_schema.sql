CREATE DATABASE IF NOT EXISTS project;
USE project;

CREATE TABLE parks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    park_name VARCHAR(255) NOT NULL
);

CREATE TABLE trails (
    id INT PRIMARY KEY AUTO_INCREMENT,
    park_id INT NOT NULL,
    trail_name VARCHAR(255) NOT NULL,
    distance DEC(3,1),
    elevation_delta DEC(6,1),
    difficulty ENUM("easy", "moderate", "hard"),
    est_time_min DEC,
    lat DEC(10,5),
    lon DEC(10,5),

    FOREIGN KEY (park_id) REFERENCES parks(id),
    FULLTEXT KEY (trail_name)
);

CREATE TABLE traits (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    trail_id INT NOT NULL,
    hiking BOOL,
    biking BOOL,
    mountain_views BOOL,
    river BOOL,
    forest BOOL,
    hist_sites BOOL,
    lake BOOL,
    FOREIGN KEY (trail_id)
        REFERENCES trails(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);
