-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)

-- Adds a 'Description' value to the 'Rooms' table.
ALTER TABLE Rooms
ADD Description VARCHAR(280);

-- Adds descriptions for rooms RND, IBS, AOB, MWC, and HBB
UPDATE Rooms
SET Description="Hidden away from the world, this one-of-a-kind room lets you defy the standard hotel experience!"
WHERE RoomID="RND";

UPDATE Rooms
SET Description="A home away from home - cozy, comfortable, and nostalgic, experience our room that feels just like home!"
WHERE RoomID="IBS";

UPDATE Rooms
SET Description="Need a quick getaway from the rote, daily lifestyle? We have just the room! Enjoy our escape from the world for the lowest price!"
WHERE RoomID="AOB";

UPDATE Rooms
SET Description="An affordable room with a hidden secret... Save your money while experiencing the mystery of the 'Cryptic' Hotel Room!"
WHERE RoomID="MWC";

UPDATE Rooms
SET Description="Want to feel like a king? This is the room for you! Royal, large, fancy, experience what many call our 'Kingly Experience' with this wonderful room!"
WHERE RoomID="HBB";

UPDATE Rooms
SET Description='Experience the timeless nature of our B&B by staying in this colonial style room!'
WHERE RoomID = 'IBD';

UPDATE Rooms
SET Description='Enjoy this modern ''mini'' room designed to attract young broke travelers.'
WHERE RoomID = 'TAA';

UPDATE Rooms
SET Description='This room is frequently referred to as our ''honeymoon suite'' due to the deep red color of the room and it''s large size.'
WHERE RoomID = 'CAS';

UPDATE Rooms
SET Description = 'In this room, you will experience a murder mystery! If you are able to solve the mystery during your stay, you receive a free breakfast!'
WHERE RoomID = 'RTE';

UPDATE Rooms
SET Description='In our Bed & Breakfast, you will find that this room is modeled after a traditional ''motel'' room. Despite this, this room has the best view available!'
WHERE RoomID = 'FNA';

