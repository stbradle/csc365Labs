public abstract class User {
   ReservationSystem owner;

   abstract User startSession();

   public void setOwner(ReservationSystem owner){ this.owner = owner;}
}
