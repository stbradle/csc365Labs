public abstract class User {
   ReservationSystem owner;

   abstract void startSession();

   public void setOwner(ReservationSystem owner){ this.owner = owner;}
}
