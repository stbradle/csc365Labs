-- Griffin Aswegan (gaswegan)
-- Sets up the Rooms and Reservations tables

CREATE TABLE Rooms(
   RoomID CHAR(3) PRIMARY KEY,
   RoomName CHAR(30),
   Beds INT,
   BedType CHAR(10),
   MaxOccupancy INT,
   BasePrice CHAR(10),
   Decor CHAR(20)
);

CREATE TABLE Reservations(
   Code        INT PRIMARY KEY,
   Room        CHAR(3) REFERENCES Rooms(RoomID),
   CheckIn     DATE,
   CheckOut    DATE, 
   Rate        DECIMAL(10, 2),
   LastName    CHAR(20),
   FirstName   CHAR(20),
   Adults      INT,
   Kids        INT
);
