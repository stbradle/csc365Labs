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
SELECT DAYNAME(r.saleDate), r.saleDate, COUNT(r.RNumber), SUM(i.Ordinal), 
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

-- Q4
SELECT t.Count, t.Flavor, t.Food, t.LastName, t.FirstName   
FROM (
   SELECT COUNT(r.RNumber) AS 'Count', g.Food, g.Flavor,
          c.LastName, c.FirstName
   FROM goods g, receipts r, customers c, items i
   WHERE g.Food = 'Cake' && g.GId = i.Item && i.Receipt = r.RNumber &&
         r.Customer = c.CId && MONTH(r.saleDate) = 10 && YEAR(r.saleDate) = 2007
   GROUP BY g.Food, g.Flavor, c.LastName, c.FirstName) t
WHERE t.Count >= ALL (
   SELECT COUNT(r2.RNumber)
   FROM receipts r2, goods g2, items i2, customers c2
   WHERE g2.Food = 'Cake' && g2.Flavor = t.Flavor && g2.GId = i2.Item &&
         i2.Receipt = r2.RNumber && r2.Customer = c2.CId &&
         MONTH(r2.saleDate) = 10 && YEAR(r2.saleDate) = 2007
   GROUP BY g2.Food, g2.Flavor, c2.LastName, c2.FirstName)
ORDER BY t.Count DESC, t.LastName ASC;

-- Q5
SELECT c1.LastName, c1.FirstName
FROM customers c1
WHERE c1. CId NOT IN (
   SELECT Customer
   FROM receipts r
   WHERE r.saleDate >= '2007-10-5' && r.SaleDate <= '2007-10-11')
ORDER BY c1.LastName ASC;

-- Q6 - Know this one is wrong
SELECT c.FirstName, c.LastName, r.saleDate
FROM customers c, receipts r
WHERE r.saleDate >= ALL (
         SELECT r2.saleDate
         FROM customers c2, receipts r2
         WHERE c2.CId = c.CID && MONTH(r2.saleDate) = 10 && 
               YEAR(r2.saleDate) = 2007) &&
      c.CId = r.Customer && MONTH(r.saleDate) = 10 && 
      YEAR(r.saleDate) = 2007
GROUP BY c.FirstName, c.LastName, r.saleDate
HAVING COUNT(r.RNumber) > 1;

-- Q7
SELECT
   CASE WHEN (
      SELECT SUM(g.price)
      FROM items i, receipts r, goods g
      WHERE g.GId = i.Item && i.Receipt = r.RNumber && 
            MONTH(r.saleDate) = 10 && YEAR(r.saleDate) = 2007 &&
            g.Flavor = 'Chocolate') > (
      SELECT SUM(g.Price)
      FROM items i, receipts r, goods g
      WHERE g.GId = i.Item && i.Receipt = r.RNumber &&
            MONTH(r.saleDate) = 10 && YEAR(r.saleDate) = 2007 &&
            g.Food = 'Croissant')
      THEN 'Chocolate'
      ELSE 'Croissant'
      END
AS 'Winner';
