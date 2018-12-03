-- Names: Steven Bradley & Griffen Aswegan
-- CalPolyID: stbradle & gaswegan

use CSU;
-- 1. Report campus names from Los Angeles county in alphabetical order
SELECT Campus
FROM campuses
WHERE County = 'Los Angeles'
ORDER BY Campus;

-- 2. Report number of students who graduated from California Maritime Academy btwn 1994 and 2000 inclusive
-- NOTE: 'California Maritime Academy' has 2 spaces btwn California and Maritime
SELECT d.YR, d.Degrees
FROM degrees d, campuses c
WHERE d.Campusid = c.Id &&
      c.Campus LIKE '%California%Maritime%Academy' &&
      d.YR >= 1994 && d.YR <= 2000
ORDER BY YR;

-- 3. Report undergrad/graduate numbers for Math, Engineering, and Info Science for both CalPolys

SELECT c.Campus, d.Name, de.UGrad, de.Grad
FROM campuses c, disciplines d, discEnr de
WHERE de.YR = 2004 &&
      c.Campus LIKE '%Polytechnic%' &&
      (d.Name LIKE '%Mathematics%' ||
       d.Name LIKE '%Engineering%' ||
       d.Name LIKE '%Computer%and%Info.%Sciences%') &&
      c.Id = de.CampusId &&
      d.Id = de.DiscId
ORDER BY c.Campus, d.Name;

-- 4.Report grad enrollments in 2004 for Agriculture and Biological Sciences if university has both as graduate programs
SELECT de1.Grad as Agriculture, de2.Grad as BiologicalSciences
FROM discEnr de1, discEnr de2, disciplines d1, disciplines d2
WHERE de1.Grad > 0 && de2.Grad > 0 &&
      de1.CampusId = de2.CampusId &&
      de1.YR = 2004 && de2.YR = 2004 &&
      (de1.DiscId = d1.Id && d1.Name = 'Agriculture') &&
      (de2.DiscId = d2.Id && d2.Name = 'Biological Sciences')
ORDER BY de1.Grad DESC;

-- 5.Find disciplines and campuses where Grad >= 3*UGrad
SELECT c.Campus, d.Name
FROM campuses c, disciplines d, discEnr de
WHERE de.YR = 2004 &&
      de.CampusId = c.Id &&
      de.DiscId = d.Id &&
      de.Grad >= 3*de.UGrad;

-- 6.Get Money from Fresno State for 2002-2004
SELECT f.YR, e.FTE*f.Fee as Total, e.FTE*f.Fee/fc.FTE as PerFaculty
FROM fees f, faculty fc, enrollments e, campuses c
WHERE (f.YR = 2002 || f.YR = 2003 || f.YR = 2004) &&
      c.Id = f.CampusId && c.Id = fc.CampusId && e.CampusId = c.Id &&
      f.YR = fc.YR && fc.YR = e.YR &&
      c.Campus LIKE '%Fresno%State%University'
ORDER BY f.YR;

-- 7. Find all campuses with higher enrollment than SJSU in 2003
SELECT c2.Campus, e2.FTE as NumStudents, fc.FTE as NumFaculty, e2.FTE/fc.FTE as StudentToFacultyRatio
FROM campuses sjsu, campuses c2, enrollments sjsuEnr, enrollments e2, faculty fc
WHERE sjsu.Campus LIKE '%San%Jose%State%University' &&
      sjsu.Id <> c2.Id &&
      sjsu.Id = sjsuEnr.CampusId && 
      c2.Id = e2.CampusId &&
      sjsuEnr.FTE < e2.FTE &&
      c2.Id = fc.CampusId && 
      e2.YR = fc.YR &&
      sjsuEnr.YR = e2.YR &&
      fc.YR = 2003;
