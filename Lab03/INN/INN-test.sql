-- Griffin Aswegan (gaswegan)
-- Tests the Rooms and Reservations table

SELECT * FROM Rooms;
SELECT * FROM Reservations;

SELECT 10 AS Expected, COUNT(*) AS Actual FROM Rooms;
SELECT 600 AS Expected, COUNT(*) AS Actual FROM Reservations;
