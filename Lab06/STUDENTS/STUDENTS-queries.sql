-- Names: Steven Bradley & Griffen Aswegan
-- CalPolyIDs: stbradle & gaswegan

-- 1. For each grade, report number of classrooms that it's taught in and how many students are in the grade
SELECT Grade, COUNT(DISTINCT Classroom) as 'NumClassrooms', COUNT(FirstName) as 'NumStudents'
FROM list
GROUP BY Grade
ORDER BY COUNT(DISTINCT Classroom) DESC, Grade;

-- 2. For each 4th grade classroom, report student who is last on the roster
SELECT Classroom, MAX(LastName) as 'Last Name'
FROM list
WHERE Grade = 4
GROUP BY ClassRoom;

-- 3. Find Teacher(s) that theach(es) the most students
SELECT t.FirstName, t.LastName, COUNT(s.FirstName) as 'NumStudents'
FROM teachers t, list s
WHERE t.Classroom = s.Classroom
GROUP BY t.Classroom, t.FirstName, t.LastName
HAVING COUNT(s.FirstName) >= ALL(SELECT COUNT(*)
                                 FROM list s, teachers t
                                 WHERE t.Classroom = s.Classroom
                                 GROUP BY t.Classroom);

-- 4. Find grades with the largest number of students whose last names start with A, B, or C

SELECT s.Grade, COUNT(s.FirstName) as 'NumStudents'
FROM list s
WHERE s.LastName LIKE "A%" ||
      s.LastName LIKE "B%" ||
      s.LastName LIKE "C%"
GROUP BY Grade
HAVING COUNT(s.FirstName) >= ALL(SELECT COUNT(*)
                                 FROM list s
                                 WHERE s.LastName LIKE "A%" ||
                                       s.LastName LIKE "B%" ||
                                       s.LastName LIKE "C%"
                                 GROUP BY s.Grade);

-- 5. Find all classrooms with a less than average number of students and report classroom number and students in ascending order.
SELECT Classroom, COUNT(FirstName) as 'Number of Students'
FROM list
GROUP BY Classroom
HAVING COUNT(FirstName) < (SELECT AVG(sub.numStudents)
                              FROM (SELECT COUNT(*) as 'numStudents'
                                    FROM list
                                    GROUP BY Classroom) sub)
ORDER BY Classroom;

-- 6. Find all pairs of classroom that have the same number of students
SELECT *
FROM
      (SELECT Classroom as 'Room1', COUNT(FirstName) as 'numStudents'
       FROM list
       GROUP BY Classroom) r1

      JOIN (SELECT Classroom as 'Room2', COUNT(FirstName) as 'numStudents'
            FROM list
            GROUP BY Classroom) r2

      USING(numStudents)
WHERE r1.Room1 < r2.Room2
ORDER BY numStudents;

