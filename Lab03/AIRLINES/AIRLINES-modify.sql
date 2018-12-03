-- Griffin Aswegan (gaswegan), Steven Bradley (stbradle)

-- Removes all flights from "Flights" except for those to and from 'AKI'
DELETE FROM Flights
WHERE (
   NOT (SourceAirport="AKI" OR DestAirport="AKI")
);

-- Increases the flight number of all flights not operated by
-- Continential, Airtran, or Virgin
UPDATE Flights
SET FlightNo=FlightNo+2000
WHERE (
   NOT (Airline=7 OR Airline=10 OR Airline=12)
);

-- Updates all even flights to be one greater and odd flights to be one smaller
UPDATE Flights
SET FlightNo=(
   IF(MOD(FlightNo, 2)=0, (FlightNo+1)*-1, FlightNo))
WHERE (
   NOT (Airline=7 OR Airline=10 OR Airline=12)
);
UPDATE Flights
SET FlightNo=(
   IF(FlightNo>0, FLightNo-1, FlightNo))
WHERE (
   NOT (Airline=7 OR Airline=10 OR Airline=12)
);
UPDATE Flights
SET FlightNo=FlightNo*-1
WHERE FlightNo<0;

-- Sets all Airlines to "Continental" except for those to and from AKI operated
-- by "Virgin" or "Airtran"
UPDATE Flights
SET Airline=7
WHERE (
   NOT ((Airline=10 OR Airline=12) AND 
   (SourceAirport="AKI" OR DestAirport="AKI"))
);

-- Displays the flights table after the above commands.
SELECT *
FROM Flights
ORDER BY Airline, FlightNo;
