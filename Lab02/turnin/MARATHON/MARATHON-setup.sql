-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)
CREATE TABLE marathon (
    place INT,
    time TIME,
    pace TIME,
    groupPlace INT,
    grp VARCHAR(6),
    age INT,
    sex VARCHAR(1),
    bibNumber INT,
    firstName VARCHAR(20),
    lasName VARCHAR(20),
    town VARCHAR(25),
    state VARCHAR(2),
    PRIMARY KEY(grp, bibNumber, place)
 );
