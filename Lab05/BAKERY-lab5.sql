-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)
-- BAKERY-lab5.sql

-- Q1
SELECT DISTINCT c.LastName, c.FirstName
FROM customers c, receipts r, items i1, items i2, goods g1, goods g2
WHERE r.RNumber = i1.Receipt && r.RNumber = i2.Receipt && i1.Item = g1.GId && 
      i2.Item = g2.GId && r.Customer = c.CId && g1.Food = 'Croissant' && 
      g2.Food = 'Croissant' && g1.GId != g2.GId && g1.GId < g2.GId
ORDER BY c.LastName;

-- Q2
SELECT DISTINCT r.saleDate
FROM receipts r, customers c, goods g, items i
WHERE (r.Customer = c.CId && c.FirstName = 'ALMETA' && c.LastName = 'DOMKOWSKI') 
      || 
      (r.RNumber = i.Receipt && i.Item = g.GId && g.Food = 'Cookie' &&
      g.Flavor = 'Gongolais')
ORDER BY r.SaleDate ASC;

-- Q3
SELECT ROUND(SUM(g.Price), 2) AS 'Total Spent'
FROM goods g, items i, customers c, receipts r
WHERE c.FirstName = 'NATACHA' && c.LastName = 'STENZ' && c.CId = r.Customer &&
      r.RNumber = i.Receipt && i.Item = g.GId && MONTH(r.saleDate) = 10 &&
      YEAR(r.saleDate) = 2007;

-- Q4
SELECT ROUND(SUM(g.Price), 2) AS 'Total Spent'
FROM goods g, receipts r, items i
WHERE g.Flavor = 'Chocolate' && g.GId = i.Item && i.Receipt = r.RNumber &&
      MONTH(r.saleDate) = 10 && YEAR(r.saleDate) = 2007;
