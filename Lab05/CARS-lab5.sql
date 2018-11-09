-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)
-- CARS-lab5.sql

-- Q1
SELECT cn1.Description, cm.FullName, cd1.YearMade, cd1.Id
FROM carNames cn1, carNames cn2, carMakers cm, carsData cd1, carsData cd2,
      modelList ml
WHERE cd2.YearMade = 1982 && cd2.Id = cn2.Id && 
      cn2.Description = 'honda civic' && cd1.YearMade > 1980 && 
      cd1.Id = cn1.Id && cn1.Model = ml.Model && 
      ml.Maker = cm.Id && cd1.MPG > cd2.MPG
ORDER BY cd1.MPG;

-- Q2
SELECT ROUND(AVG(cd.Horsepower), 0) AS 'Average HP', 
       MAX(cd.Horsepower) AS 'Max HP', MIN(cd.Horsepower) AS 'Min HP'
FROM carsData cd, carNames cn
WHERE cd.YearMade >= 1971 && cd.YearMade <= 1976 && Cylinders = 4 &&
      cd.Id = cn.Id && cn.Model = 'renault';

-- Q3
SELECT COUNT(*) AS 'Number of Cars'
FROM carsData cd1, carsData cd2, carNames cn
WHERE cn.Description = 'volvo 145e (sw)' && cn.Id = cd1.Id && 
      cd1.YearMade = 1972 && cd2.YearMade = 1971 && 
      cd2.Accelerate > cd1.Accelerate;

-- Q4
SELECT COUNT(DISTINCT cn.Model) AS 'Distinct Car Makers'
FROM carsData cd, carNames cn
WHERE cd.Weight > 4000 && cd.Id = cn.Id;
