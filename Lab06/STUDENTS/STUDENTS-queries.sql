-- Names: Steven Bradley & Griffen Aswegan
-- CalPolyIDs: stbradle & gaswegan

-- 1. For each grade, report number of classrooms that it's taught in and how many students are in the grade
SELECT Grade, COUNT(DISTINCT Classroom) as 'NumClassrooms', COUNT(FirstName) as 'NumStudents'
FROM list
GROUP BY Grade
ORDER BY COUNT(DISTINCT Classroom) DESC, Grade;

-- 2. For each 4th grade classroom, report student who is last on the roster
SELECT Classroom, MAX(LastName)
FROM list
WHERE Grade = 4
GROUP BY ClassRoom;
