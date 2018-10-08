-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)
CREATE TABLE marathon (
    place INT,
    time TIME,
    pace TIME,
    groupPlace INT,
    grp CHAR(6),
    age INT,
    sex CHAR(1),
    bibNumber INT,
    firstName CHAR(20),
    lasName CHAR(20),
    town CHAR(25),
    state CHAR(2),
    PRIMARY KEY(grp, bibNumber, place)
 );
