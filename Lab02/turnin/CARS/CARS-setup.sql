-- Griffin Aswegan
-- Creates the Continents, Countries, ModelList, CarData, CarMakers, and CarNames tables

CREATE TABLE Continents(
   ContID INT PRIMARY KEY,
   Continent VARCHAR(10)
);

CREATE TABLE Countries (
   CountryID INT PRIMARY KEY,
   CountryName VARCHAR(50),
   Continent INT REFERENCES Continents(ContID)
);

CREATE TABLE CarMakers(
   ID INT PRIMARY KEY,
   Maker VARCHAR(25),
   FullName VARCHAR(50),
   Country INT REFERENCES Countries(CountryID)
);

CREATE TABLE ModelList(
   ModelID INT PRIMARY KEY,
   Maker INT REFERENCES CarMakers(ID),
   Model VARCHAR(20) UNIQUE
);

CREATE TABLE CarNames(
   MakeID INT PRIMARY KEY,
   Model VARCHAR(20) REFERENCES ModelList(Model),
   MakeDescription VARCHAR(100)
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
