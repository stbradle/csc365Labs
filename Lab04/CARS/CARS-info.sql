-- Names: Steven Bradley & Griffen Aswegan
-- CalPolyIDs: stbradle & gaswegan

use CARS;

-- 1. Find all Renaults in the database.
SELECT cn.Description as Name, cd.YearMade as Year
FROM carNames cn, carsData cd
WHERE cn.Model LIKE 'renault' &&
      cn.Id = cd.Id;

-- 2. Find all cars produced by volvo btwn 1977 & 1981 
SELECT cn.Description as Name, cd.YearMade as Year
FROM carNames cn, carsData cd
WHERE cn.Model LIKE 'Volvo' &&
      cn.Id = cd.Id &&
      cd.YearMade >= 1977 &&
      cd.YearMade <= 1981;

-- 3. Find all Asian automakers
SELECT m.FullName, cn.Name
FROM carMakers m, countries cn, continents c
WHERE c.Name LIKE 'asia' &&
      c.Id = cn.Continent &&
      m.Country = cn.Id
ORDER BY m.FullName;

-- 4. Find all non 4-cylinder cars produced in 1980 that have fuel economy better that 20 MPG and that have 0-60 better than 15s.
SELECT cn.Description as Name, cm.FullName
FROM carNames cn, carMakers cm, carsData cd, modelList m
WHERE cn.Id = cd.Id &&
      m.Model LIKE cn.Model &&
      m.Maker = cm.Id &&
      cd.Cylinders <> 4 &&
      cd.YearMade = 1980 &&
      cd.MPG > 20 &&
      cd.Accelerate < 15;

-- 5. Find all non-euro carmakers who made a light car btwn 1979 and 1981
SELECT DISTINCT cm.FullName, countries.Name
FROM carsData cd, carMakers cm, countries, continents cont, modelList ml, carNames cn
WHERE cont.Name <> 'europe' &&
      cont.Id = countries.Continent &&
      cd.weight < 2000 &&
      cd.YearMade >= 1979 && 
      cd.YearMade <= 1981 && 
      cd.Id = cn.Id &&
      cn.Model LIKE ml.Model && 
      ml.Maker = cm.Id &&
      countries.Id = cm.Country;

-- 6.Get ratio of weight to hp for saab after 1978
SELECT cn.Description, cd.YearMade, cd.Weight/cd.Horsepower as WeightToHPRatio
FROM  carMakers cm, carsData cd, carNames cn
WHERE cm.Maker = 'saab' &&
      cd.YearMade > 1978 &&
      cm.Maker LIKE cn.Model &&
      cn.Id = cd.Id;
