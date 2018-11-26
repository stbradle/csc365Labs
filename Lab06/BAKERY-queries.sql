-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)
-- BAKERY queries

-- Q1
SELECT COUNT(Food) AS 'Number of Items', 
       ROUND(AVG(Price), 2) as 'Average Price', Flavor
FROM goods
GROUP BY Flavor
HAVING COUNT(Food) > 3
ORDER BY ROUND(AVG(Price), 2) ASC;

-- Q2 - I know this one isn't correct, but I'm not sure why.
SELECT DAYNAME(r.saleDate), r.saleDate, COUNT(r.RNumber), COUNT(i.Ordinal), 
       ROUND(SUM(g.Price), 2)
FROM receipts r, items i, goods g
WHERE r.RNumber = i.Receipt && i.Item = g.GId &&
      r.saleDate >= '2007-10-8' && r.saleDate <= '2007-10-14'
GROUP BY r.saleDate;

-- Q3
SELECT c1.LastName, c1.FirstName
FROM customers c1
WHERE c1.CId NOT IN (
   SELECT c2.CId
   FROM customers c2, items i, goods g, receipts r
   WHERE r.Customer = c2.CId && r.RNumber = i.Receipt && i.Item = g.GId &&
         g.Food = 'Twist')
ORDER BY c1.LastName, c1.FirstName ASC;

-- Q4 Not written
      
-- Q5
SELECT c1.LastName, c1.FirstName
FROM customers c1
WHERE c1. CId NOT IN (
   SELECT Customer
   FROM receipts r
   WHERE r.saleDate >= '2007-10-5' && r.SaleDate <= '2007-10-11')
ORDER BY c1.LastName ASC;

-- Q6 Not written

-- Q7 Not written
