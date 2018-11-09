-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)
-- STUDENTS-lab5.sql

-- Q1
SELECT s1.FirstName, s1.LastName, s1.Grade, s2.FirstName, s2.LastName, s2.Grade
FROM list s1, list s2
WHERE s1.FirstName = s2.FirstName && s1.LastName != s2.LastName &&
      s1.LastName < s2.LastName;

-- Q2
SELECT s.LastName, s.FirstName
FROM list s, teachers t
WHERE t.Classroom = s.Classroom && t.FirstName != 'OTHA' && 
      t.LastName != "MOYER" && s.Grade = 1
ORDER BY s.LastName;

-- Q3
SELECT COUNT(*) AS Count
FROM list
WHERE Grade = 3 || Grade = 4;

-- Q4
SELECT COUNT(*)
FROM list s, teachers t
WHERE s.Classroom = t.Classroom && t.FirstName = "LORIA" && 
      t.LastName = "ONDERSMA";
