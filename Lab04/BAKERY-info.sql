-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)

-- Q1
SELECT Flavor, Food, Price
FROM goods
WHERE Flavor="Chocolate" && Price<5.00
ORDER BY Price DESC;

-- Q2
SELECT Flavor, Food, Price
FROM goods
WHERE 
   (Food="Cookie" AND Price>1.10) OR
   Flavor="Lemon" OR
   (Flavor="Apple" AND Food!="Pie")
ORDER BY Flavor, Food;

-- Q3
SELECT DISTINCT c.LastName, c.FirstName
FROM customers c, receipts r
WHERE c.CId=r.Customer && r.saleDate="2007-10-3"
ORDER BY LastName;

-- Q4
SELECT Distinct g.Flavor, g.Food
FROM goods g, receipts r, items i
WHERE r.saleDate="2007-10-4" && r.RNumber=i.Receipt && i.Item=g.GId &&
      g.Food="Cake"
ORDER BY g.Flavor;

-- Q5
SELECT g.Flavor, g.Food
FROM goods g, receipts r, items i, customers c
WHERE c.LastName="Cruzen" && c.FirstName="Ariane" && c.CId=r.Customer &&
      r.saleDate="2007-10-25" && r.RNumber=i.Receipt && i.Item=g.GId
ORDER BY i.Receipt, i.Ordinal;

-- Q6
SELECT DISTINCT g.Flavor, g.Food
FROM goods g, receipts r, items i, customers c
WHERE c.LastName ="Arnn" && c.FirstName="Kip" && c.CId=r.Customer &&
      MONTH(r.saleDate)=10 AND YEAR(r.saleDate)=2007 && r.RNumber=i.Receipt && 
      i.Item=g.GId && g.Food="Cookie"
ORDER BY g.Flavor;
