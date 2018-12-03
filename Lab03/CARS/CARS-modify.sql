-- Griffin Aswegan (gaswegan), Steven Bradleu (stbradle)

-- Removes from CarData any values that do not satisfy one of these rules:
--    a) Cars made in 1979 or 1980 with 20+ MPG
--    b) Cars that have an MPG of 26+ and that have a 110+ horsepower engine
--    c) Cars that have 8 cylinders and do 0-60 in less than 10 seconds
--    d) Cars that have ID 11, 12, 13, 14, 15, or 362*
-- * These cars don't end up getting deleted from the table with just the
--   boolean query (because of NULL's logic with AND and OR statements), and I
--   am unable to find a way to delete them using boolean logic or without
--   using subqueries, so I'm removing them by hand. See the commented out
--   query below for the logic I used.
DELETE FROM CarData
WHERE
           (NOT ((Year=1979 OR Year=1980) AND MPG>=20)) AND
           (NOT (MPG>=26 AND Horsepower>110)) AND
           (NOT (Cylinders=8 AND Acceleration<10.0))
           OR ID=11 OR ID=12 or ID=13 OR ID=14 OR ID=15 OR ID=362;

-- Selects all cars in CarData, ordered ascending by Year, then by CarID
SELECT *
FROM CarData
ORDER BY Year, ID;

-- Removes all attributes from CarData except ID, Year, Acceleration, 
--    MPG, and Cylinders
ALTER TABLE CarData
DROP COLUMN EDispl;
ALTER TABLE CarData
DROP COLUMN Horsepower;
ALTER TABLE CarData
DROP COLUMN Weight;

-- Removes all cars with 5 cylinders or fewer
DELETE FROM CarData
WHERE Cylinders<=5;

-- Selects all cars in CarData, ordered ascending by Year, then CarID
SELECT *
FROM CarData
ORDER BY Year, ID;



-- *
-- SELECT * FROM CarData C where C.ID NOT IN (
--    SELECT ID FROM CarData WHERE (
--       NOT ((Year=1979 OR Year=1980) AND MPG>=20) AND
--       NOT (MPG>=26 AND Horsepower>110) AND
--       NOT (Cylinders=8 AND Acceleration<10.0)
--    ) AND (
--       ((Year=1979 OR Year=1980) AND MPG>=20) OR
--       (MPG>=26 AND Horsepower>110) OR
--       (Cylinders=8 AND Acceleration<10.0)
--    )
-- );
