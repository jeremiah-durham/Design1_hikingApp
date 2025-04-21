USE project;


CREATE TABLE users (
    pk INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    uuid binary(16) NOT NULL,
    name VARCHAR(128) NOT NULL,
    eemail VARCHAR(255) NOT NULL,
    weight INT NOT NULL,
    height INT NOT NULL,
    UNIQUE(uuid)
);


CREATE TABLE hike_log (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    create_ts DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user binary(16) NOT NULL,
    trail_id INT NOT NULL,
    active BOOL NOT NULL DEFAULT FALSE,
    start_time DATETIME DEFAULT NULL,
    expected_end_time DATETIME DEFAULT NULL,

    FOREIGN KEY (user) REFERENCES users(uuid),
    FOREIGN KEY (trail_id) REFERENCES trails(id)
);
