-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)
-- CARS-queries.sql

-- Q1
SELECT MAX(cd.MPG), AVG(cd.Accelerate), cm.Maker
FROM carMakers cm, carNames cn, modelList ml, carsData cd, countries c
WHERE c.Name = 'japan' && cm.Country = c.Id && cm.ID = ml.Maker && 
      ml.Model = cn.Model && cn.Id = cd.Id
GROUP BY cm.Maker
ORDER BY MAX(cd.MPG) ASC;

-- Q2
SELECT cm.Maker, COUNT(cd.Id)
FROM carMakers cm, carsData cd, modelList ml, carNames cn, countries c
WHERE c.Name = 'usa' && cm.Country = c.Id && cm.ID = ml.Maker &&
      ml.Model = cn.Model && cn.Id = cd.Id && cd.Weight < 4000 &&
      cd.Cylinders = 4 && cd.Accelerate < 14.0
GROUP BY cm.Maker
ORDER BY COUNT(cd.Id);

-- Q3 - I'm confused - the question implies there's multiple cars but there's
-- only one car with the best gas mileage?
SELECT cn.Description, cd.YearMade
FROM carNames cn, carsData cd
WHERE cn.Id = cd.Id && cd.MPG =
   (SELECT MAX(MPG)
   FROM carsData);

-- Q4 - Same with this one... I fail to understand where the multiple cars
-- comes into play here...
SELECT cn.Description, cd.YearMade, MAX(cd.Accelerate)
FROM carNames cn, carsData cd
WHERE cn.Id = cd.Id && cd.MPG =
   (SELECT MAX(MPG)
   FROM carsData)
GROUP BY cn.Description, cd.YearMade;

-- Q5 - I know this one is wrong.
SELECT cm.Maker, cd.YearMade, COUNT(cd.Id), AVG(cd.Accelerate)
FROM carMakers cm, carsData cd, carNames cn, modelList ml
WHERE cm.ID = ml.Maker && ml.Model = cn.Model && cn.Id = cd.Id
GROUP BY cd.YearMade, cm.Maker
HAVING MAX(cd.Weight) && COUNT(cd.Id) > 1;

-- Q6
SELECT 
   (SELECT MAX(MPG)
    FROM carsData
    WHERE Cylinders = 8) -
   (SELECT MIN(MPG)
    FROM carsData
    WHERE Cylinders = 4)
   AS 'MPG Difference';

-- Q7
SELECT cd1.YearMade, (CASE WHEN
   (SELECT COUNT(cn.Id)
    FROM carMakers cm, carNames cn, modelList ml, countries c, carsData cd2
    WHERE c.Name = 'usa' && c.Id = cm.Country && cm.ID = ml.Maker &&
    ml.Model = cn.Model && cd2.YearMade = cd1.YearMade) >
   (SELECT COUNT(cn.Id)
    FROM carMakers cm, carNames cn, modelList ml, countries c, carsData cd2
    WHERE c.Name != 'usa' && c.Id = cm.Country && cm.ID = ml.Maker &&
    ml.Model = cn.Model && cd1.YearMade = cd2.YearMade) 
   THEN 'US' ELSE 'Rest of the World' END) AS 'Winner'
FROM carsData cd1
WHERE cd1.YearMade >= 1972 && cd1.YearMade <= 1976
GROUP BY cd1.YearMade;
