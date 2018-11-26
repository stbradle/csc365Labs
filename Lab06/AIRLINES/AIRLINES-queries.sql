-- Names: Steven Bradley & Griffen Aswegan
-- CalPolyIDs: stbradle & gaswegan

-- 1. Find all airports with exactly 17 outgoing flights
SELECT p.Code, p.Name
FROM airports p, flights f
WHERE f.Source LIKE p.Code
GROUP BY p.Code, p.Name
HAVING COUNT(DISTINCT f.FlightNo) = 17
ORDER BY p.Code;

-- 2.Find number of airports where ANP can be reached with one transfer
SELECT COUNT(DISTINCT p.Code)
FROM airports p, flights f1, flights f2
WHERE f1.Source NOT LIKE "ANP" &&
      f1.Destination LIKE f2.Source &&
      f2.Destination LIKE "ANP" &&
      p.Code LIKE f1.Source;

-- 3. Find number of airports that can reach ATE with at most 1 transfer
SELECT COUNT(DISTINCT p.Code)
FROM airports p, flights f1, flights f2
WHERE f1.Source NOT LIKE "ATE" &&
      ((f1.Destination LIKE f2.Source &&
       f2.Destination LIKE "ATE") ||
      f1.Destination LIKE "ATE") &&
      p.Code LIKE f1.Source;

-- 4.For each airline, Find number of airports where they have an outgoing flight 
SELECT l.Airline, COUNT(DISTINCT p.Code) as 'Number of Airports'
FROM airlines l, airports p, flights f
WHERE l.Id = f.Airline &&
      f.Source LIKE p.Code
GROUP BY l.Airline;
