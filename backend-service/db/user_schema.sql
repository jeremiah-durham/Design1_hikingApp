USE project;


CREATE TABLE users (
    pk INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    id binary(16) NOT NULL,
    weight INT NOT NULL,
    height INT NOT NULL,
    eemail VARCHAR(255) NOT NULL,
    UNIQUE(id)
);
