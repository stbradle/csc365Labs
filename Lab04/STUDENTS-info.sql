-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)

-- Q1
SELECT LastName, FirstName
FROM list
WHERE Classroom=111
ORDER BY LastName;

-- Q2
SELECT DISTINCT Classroom, Grade
FROM list
ORDER BY Classroom DESC;

-- Q3
SELECT DISTINCT t.LastName, t.FirstName, t.Classroom
FROM teachers t, list s
WHERE t.Classroom=s.Classroom && s.grade=5
ORDER BY t.Classroom;

-- Q4
SELECT s.LastName, s.FirstName
FROM list s, teachers t
WHERE s.Classroom=t.Classroom && t.FirstName="Otha" && t.LastName="Moyer"
ORDER BY s.LastName;

-- Q5
SELECT DISTINCT t.LastName, t.FirstName, s.Grade
FROM teachers t, list s
WHERE s.Classroom=t.Classroom && s.Grade<=3
ORDER BY s.Grade, t.LastName;
