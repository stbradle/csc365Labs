public class Guest extends User{

   public void startSession(){
      boolean exit = false;

      while (!exit) {
         owner.displayMenu(this);

         char option = owner.getResponse();
         System.out.println("option chosen: " + option);
   
         switch(option) {
            case 'r':   
               showRooms();
               break;
            case 's':   
               makeReservation();
               break;
            case 'b':   
               exit = true;
               break;
         }
      }
   }
   
   private void showRooms() {
      
   }
   
   private void makeReservation() {
         
   }
}
