-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)
CREATE TABLE campuses(
   id       INT UNIQUE,
   name     VARCHAR(60) PRIMARY KEY,
   location VARCHAR(30),
   county   VARCHAR(25),
   year     VARCHAR(4)
);

CREATE TABLE fees(
   cid  INT,
   year YEAR,
   fee  INT,
   PRIMARY KEY(cid, year)
);

CREATE TABLE degrees(
   year       YEAR,
   cid        INT,
   numDegrees INT,
   PRIMARY KEY(cid, year)
);

CREATE TABLE discEnroll(
   cid         INT REFERENCES campuses(id),
   did         INT REFERENCES disciplines(id),
   year        YEAR,
   undergrad   INT,
   grad        INT,
   PRIMARY KEY (cid, did)
);

CREATE TABLE disciplines(
   id    INT PRIMARY KEY,
   name  VARCHAR(30)
);

CREATE TABLE enrollments(
   cid                 INT REFERENCES campuses(id),
   year                YEAR,
   totalEnroll         INT,
   fullTimeEquivEnroll INT,
   PRIMARY KEY(cid, year)
);

CREATE TABLE faculty(
   cid      INT REFERENCES campuses(id),
   year     YEAR,
   facNum   INT,
   PRIMARY KEY(cid, year)
);
