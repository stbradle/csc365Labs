-- Griffin Aswegan (gaswegan)

-- Q1
SELECT d.YR, c.Campus, SUM(d.Degrees)
FROM degrees d, campuses c
WHERE c.County LIKE 'Los Angeles' || c.County LIKE 'Orange' &&
      d.Campusid = c.Id
GROUP BY d.YR, c.Campus
ORDER BY SUM(d.Degrees) DESC;

-- Q2 Not written

-- Q3
SELECT SUM(de.UGrad), c.Campus
FROM enrollments e, campuses c, discEnr de
WHERE c.Id = e.CampusId && 
      e.Enrolled = (SELECT MAX(Enrolled) FROM enrollments WHERE YR = 2000) && 
      de.CampusId = c.Id
GROUP BY c.Campus;

-- Q4 Not written

-- Q5 Not written

-- Q6 Not written

-- Q7 Not written
