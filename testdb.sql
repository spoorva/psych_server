CREATE SCHEMA IF NOT EXISTS psych;
USE psych;

DROP TABLE IF EXISTS ParticipantDetails;
DROP TABLE IF EXISTS TargetGroup;
DROP TABLE IF EXISTS Admin;

CREATE TABLE ParticipantDetails (
	username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    age VARCHAR(255),
    ethnicity VARCHAR(255),
    disability VARCHAR(255),
    Education VARCHAR(255),
    mobileHandlingExperience VARCHAR(255),
    psycothereputicMedications VARCHAR(255),
    colorblind VARCHAR(255),
	regCode VARCHAR(255),
	targetGroupId INT,
    CONSTRAINT pk_ParticipantDetails_username PRIMARY KEY (username),
    CONSTRAINT uc_ParticipantDetails_username UNIQUE (username)
);

CREATE TABLE Admin (
	firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    role INT,
    id INT AUTO_INCREMENT,
    CONSTRAINT pk_Admin_id PRIMARY KEY (id),
    CONSTRAINT uc_Admin_id UNIQUE (id)
);


CREATE TABLE TargetGroup (
	id INT AUTO_INCREMENT,
    name VARCHAR(255),
    keywords VARCHAR(255),
    locId INT,
    trainingId INT,
    locCode VARCHAR(255),
    regCode VARCHAR(255),
    trainingCount INT,
    CONSTRAINT pk_TargetGroup_id PRIMARY KEY (id),
    CONSTRAINT uc_TargetGroup_id UNIQUE (id)
);


INSERT INTO Admin(firstName, lastName, email, role)
VALUES
("Tim", "Kryvtsun", "kryvtsun.t@husky.neu.edu", 1);
