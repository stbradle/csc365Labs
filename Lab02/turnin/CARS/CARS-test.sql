-- Griffin Aswegan (gaswegan), Steven Bradley(stbradle)
SELECT * FROM CarData;
SELECT * FROM CarMakers;
SELECT * FROM CarNames;
SELECT * FROM Continents;
SELECT * FROM Countries;
SELECT * FROM ModelList;

SELECT 406 AS Expected, COUNT(*) AS Actual FROM CarData;
SELECT 23 AS Expected, COUNT(*) AS Actual FROM CarMakers;
SELECT 406 AS Expected, COUNT(*) AS Actual FROM CarNames;
SELECT 5 AS Expected, COUNT(*) AS Actual FROM Continents;
SELECT 15 AS Expected, COUNT(*) AS Actual FROM Countries;
SELECT 36 AS Expected, COUNT(*) AS Actual FROM ModelList;
