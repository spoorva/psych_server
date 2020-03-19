CREATE SCHEMA IF NOT EXISTS psych;
USE psych;

DROP TABLE IF EXISTS ParticipantDetails;
DROP TABLE IF EXISTS TargetGroup;
DROP TABLE IF EXISTS TRAININGQUESTIONMAP;
DROP TABLE IF EXISTS TRAININGIMAGEMAP;

DROP TABLE IF EXISTS Admin;
DROP TABLE IF EXISTS Image;
DROP TABLE IF EXISTS ImageCategory;


DROP TABLE IF EXISTS Question;
DROP TABLE IF EXISTS QuestionCategory;
DROP TABLE IF EXISTS Training;
DROP TABLE IF EXISTS FieldLookup;
DROP TABLE IF EXISTS Location;
DROP TABLE IF EXISTS Participant;




CREATE TABLE Participant (
	username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    age INT,
    gender VARCHAR(255),
    ethnicity VARCHAR(255),
    disability VARCHAR(255),
    Education VARCHAR(255),
    mobileHandlingExperience VARCHAR(255),
    psycothereputicMedications VARCHAR(255),
    colorblind VARCHAR(255),
	regCode VARCHAR(255),
	targetGroupId INT,
    CONSTRAINT pk_Participant_username PRIMARY KEY (username),
    CONSTRAINT uc_Participant_username UNIQUE (username)
);


CREATE TABLE Admin (
	firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    password varchar(255),
    role INT,
    id INT AUTO_INCREMENT,
    CONSTRAINT pk_Admin_id PRIMARY KEY (id),
    CONSTRAINT uc_Admin_id UNIQUE (id)
);

Create Table FieldLookup (
	id INT AUTO_INCREMENT,
	fieldname varchar(255),
    groupname varchar(255),
	CONSTRAINT pk_FieldLookup_id PRIMARY KEY (id)
    );
    
CREATE TABLE TargetGroup (
	id INT AUTO_INCREMENT,
    name VARCHAR(255),
	description VARCHAR(255),
    keywords VARCHAR(255),
    locationId INT,
    trainingId INT,
    locactionCode VARCHAR(255),
    registrationCode VARCHAR(255),
    CONSTRAINT pk_TargetGroup_id PRIMARY KEY (id),
    CONSTRAINT uc_TargetGroup_id UNIQUE (id)
);



CREATE TABLE Location (
	id INT AUTO_INCREMENT,
    locName VARCHAR(255),
	description VARCHAR(255),
    keywords VARCHAR(255),
	locCode VARCHAR(255),
	addressLine1 VARCHAR(255),
    addressLine2 VARCHAR(255),
    city VARCHAR(255),
    state INT,
    zipCode VARCHAR(255),
    phoneNumber VARCHAR(255),
    faxNumber VARCHAR(255),
	email VARCHAR(255),
    CONSTRAINT pk_Location_id PRIMARY KEY (id)
);

CREATe TABLE ImageCategory (
    name VARCHAR(255),
	description VARCHAR(255),
	id INT auto_increment,
	CONSTRAINT pk_ImageCategory_ImageCategoryId PRIMARY KEY (id)
);

CREATe TABLE QuestionCategory (
    name VARCHAR(255),
	description VARCHAR(255),
	startLabel VARCHAR(255),
	endLabel VARCHAR(255),
	responseType INT,
	id INT auto_increment,
	CONSTRAINT pk_QuestionCategory_QuestionCategoryId PRIMARY KEY (id)
);


CREATe TABLE Image (
	id int auto_increment,
    name VARCHAR(255),
	description VARCHAR(255),
	categoryId INT,
    imageType INT,
    intensity INT,
    imageloc Varchar(255),
	CONSTRAINT pk_Image_id PRIMARY KEY (id),
	CONSTRAINT fk_Image_categoryId FOREIGN KEY (categoryId) REFERENCES ImageCategory(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATe TABLE Question (
	id int auto_increment,
    name VARCHAR(255),
	description VARCHAR(255),
	categoryId INT,
	CONSTRAINT pk_question_id PRIMARY KEY (id),
	CONSTRAINT fk_question_categoryId FOREIGN KEY (categoryId) REFERENCES QuestionCategory(id) ON UPDATE CASCADE ON DELETE CASCADE


);

CREATE TABLE Training (
	id INT auto_increment,
    name VARCHAR(255),
	description VARCHAR(255),
    keywords VARCHAR(255),
	categoryId INT,
	CONSTRAINT pk_Training_TrainingId PRIMARY KEY (id)

);

CREATE TABLE TRAININGQUESTIONMAP (
	id INT auto_increment,
    TRAININGID INT,
    QUESTIONID INT, 
	CONSTRAINT pk_QuestionMap_id PRIMARY KEY (id),
	CONSTRAINT fk_QuestionMap_trainingId FOREIGN KEY (trainingId) REFERENCES Training(id),
	CONSTRAINT fk_QuestionMap_questionId FOREIGN KEY (questionId) REFERENCES Question(id)
);

CREATE TABLE TRAININGIMAGEMAP (
	id INT auto_increment,
    TRAININGID INT,
    IMAGECATEGORYID INT, 
    DURATION INT,
    NOOFIMAGES INT,
    IMAGETYPE INT,
	CONSTRAINT pk_ImageMap_id PRIMARY KEY (id),
	CONSTRAINT fk_ImageMap_trainingId FOREIGN KEY (trainingId) REFERENCES Training(id),
	CONSTRAINT fk_ImageMap_imageCategoryId FOREIGN KEY (imageCategoryId) REFERENCES ImageCategory(id)
);


INSERT INTO Admin(firstName, lastName, email, role, password)
VALUES
("Tymofii", "Kryvtsun", "kryvtsun.t@husky.neu.edu", 1, "qwerty");

INSERT INTO QuestionCategory(name, description, startLabel, endlabel, responseType)
VALUES
("question category 1", "test question category", "start", "end", 1);


INSERT INTO QUESTION (name, description, categoryId) VALUES ("question 1", "test question", 1);

INSERT INTO ImageCategory(name, description)
VALUES
("image category 1", "test image category");

INSERT INTO Image(name, description, categoryId, imageloc, imageType, intensity)
VALUES
("image 1", "test image", 2, "C:\Users\Tim\Desktop\Test.jpg", 1, 5);


INSERT INTO FieldLookup(fieldname)
VALUES
("GlobalAdministrator");

INSERT INTO FieldLookup(fieldname, groupname)
VALUES
("image", "imageType");


INSERT INTO FieldLookup(fieldname, groupname)
VALUES
("simple response", "responseType");



INSERT INTO Training(name, description, keywords)
Values 
("training 1", "test training", "test");


INSERT INTO LOCATION (locCode, locName, description, keywords, addressLine1, addressLine2,  
city, state, zipcode, phoneNumber, faxNumber, email) 
values ("zxvw12", "home", "my house", "home", "20 Sunset Str", "Apt 3", "Boston", 1, "02120", "6179010041", "6179010041", "kryvtsun.t@husky.neu.edu");

INSERT INTO targetgroup (name, description, keywords, locationId, trainingId, registrationCode) 
values ("group 1", "test group", "test", 1, 1, "qwerty");

INSERT INTO participant (username, password, age, gender, ethnicity, 
disability, education,mobileHandlingExperience,psycothereputicMedications, 
colorblind,targetGroupId) values ("tim", "qwerty", 24, "male","white", "none", "high school", "none", "none", "none", 1); 

INSERT INTO TRAININGQUESTIONMAP (TRAININGID, QUESTIONID) VALUES (1, 1);

INSERT INTO TRAININGIMAGEMAP (TRAININGID, IMAGECATEGORYID, DURATION, NOOFIMAGES, IMAGETYPE)
VALUES (1, 1, 1, 1, 1);
