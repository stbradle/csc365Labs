-- Add attribute gpa to students table
ALTER TABLE students
   ADD gpa FLOAT;

-- Drop student tuples from table if they are not in 5th or 6th grade
DELETE FROM students
WHERE students.Grade != 5 &&
      students.Grade != 6;

-- Add a new classroom to database
INSERT INTO teachers
   VALUES('GAWAIN', 'Ap-Mawr', 120);

-- Move some existing teachers to classroom 120
UPDATE teachers
SET Classroom = 120
WHERE (FirstName = 'JEFFRY' && LastName = 'FLACHS') ||
      (FirstName = 'TAWANNA' && LastName = 'HUANG')  ||
      (FirstName = 'EMILE'   && LastName = 'GRUNIN');

-- Set GPA of 6th graders to 3.25
UPDATE students
SET gpa = 3.25
WHERE Grade = 6;

-- Set GPA of 5th graders in room 109
UPDATE students
SET gpa = 2.9
WHERE Grade = 5 && Classroom = 109;

-- Set GPA of 5th graders in room 120
UPDATE students
SET gpa = 3.5
WHERE Grade = 5 && Classroom = 120;

-- Update Chet Maciag GPA
UPDATE students
SET gpa = 4.0
WHERE FirstName = 'CHET' && LastName = 'MACIAG';

-- Update Al Gerstein GPA to be 0.3 higher
UPDATE students
SET gpa = gpa + 0.3
WHERE FirstName = 'AL' && LastName = 'GERSTEIN';

-- Update GPA of Tawanna and Elvira
UPDATE students
SET gpa = gpa * 1.10
WHERE (FirstName = 'TAWANNA' && LastName = 'HUANG') ||
      (FirstName = 'ELVIRA'  && LastName = 'JAGNEAUX');

-- Finish it up
SELECT * FROM students
ORDER BY gpa, Grade, LastName;

SELECT * FROM teachers;
