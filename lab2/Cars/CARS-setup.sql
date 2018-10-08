-- Griffin Aswegan
-- Creates the Continents, Countries, ModelList, CarData, CarMakers, and CarNames tables

CREATE TABLE Continents(
   ContID INT PRIMARY KEY,
   Continent CHAR(10)
);

CREATE TABLE Countries (
   CountryID INT PRIMARY KEY,
   CountryName CHAR(50),
   Continent INT REFERENCES Continents(ContID)
);

CREATE TABLE CarMakers(
   ID INT PRIMARY KEY,
   Maker CHAR(25),
   FullName CHAR(50),
   Country INT REFERENCES Countries(CountryID)
);

CREATE TABLE ModelList(
   ModelID INT PRIMARY KEY,
   Maker INT REFERENCES CarMakers(ID),
   Model CHAR(20) UNIQUE
);

CREATE TABLE CarNames(
   MakeID INT PRIMARY KEY,
   Model CHAR(20) REFERENCES ModelList(Model),
   MakeDescription CHAR(100)
);

CREATE TABLE CarData(
   ID INT REFERENCES CarNames(MakeID),
   MPG INT,
   Cylinders INT,
   EDispl INT,
   Horsepower INT,
   Weight INT, 
   Acceleration DECIMAL(8, 2),
   Year INT
);
