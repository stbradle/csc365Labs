 CREATE TABLE students(
    LastName  VARCHAR(20) NOT NULL,
    FirstName VARCHAR(20) NOT NULL,
    Grade     INT,
    Classroom INT,
    PRIMARY KEY (LastName, FirstName)
 );

 CREATE TABLE teachers(
    LastName  VARCHAR(20) NOT NULL,
    FirstName VARCHAR(20) NOT NULL,
    Classroom INT,
    PRIMARY KEY (LastName, FirstName)
 );
