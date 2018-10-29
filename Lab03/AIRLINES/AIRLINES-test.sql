-- Griffin Aswegan (gaswegan)
-- AIRLINES-test.sql - Tests the "Airlines", "Airports", and "Flights" tables
SELECT * FROM Airlines;
SELECT 12 AS Expected, COUNT(*) AS Actual FROM Airlines;

SELECT * FROM Airports;
SELECT 100 AS Expected, COUNT(*) AS Actual FROM Airports;

SELECT * FROM Flights;
SELECT 1200 AS Expected, COUNT(*) AS Actual FROM Flights;
