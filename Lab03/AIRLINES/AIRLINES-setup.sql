-- Griffin Aswegan (gaswegan)
-- Professor E. Buckalew, CSC 365
-- October 10th, 2018
-- AIRLINES-setup.sql - Sets up the "Airlines", "Airports", and "Flights" tables

CREATE TABLE Airlines(
   ID INT PRIMARY KEY,
   Airline CHAR(30),
   Abbreviation CHAR(20),
   Country CHAR(30)
);

CREATE TABLE Airports(
   City CHAR(30),
   AirportCode CHAR(3) PRIMARY KEY,
   AirportName CHAR(100),
   Country CHAR(30),
   CountryAbbrev CHAR(10)
);

CREATE TABLE Flights(
   Airline CHAR(30) REFERENCES Airlines(Airline),
   FlightNo INT,
   SourceAirport CHAR(3) REFERENCES Airports(AirportCode),
   DestAirport CHAR(3) REFERENCES Airports(AirportCode),
   PRIMARY KEY (Airline, FlightNo)
);
