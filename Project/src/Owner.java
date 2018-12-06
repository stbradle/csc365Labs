public class Owner extends User {
   
   public void startSession(){
      boolean exit = false;

      while (!exit) {
         owner.displayMenu(this);

         char option = owner.getResponse();
         System.out.println("option chosen: " + option);

         switch(option) {
            case 'o':   System.out.println("occupancyMenu\n");
               break;
            case 'd':   System.out.println("revenueData\n");
               break;
            case 's':   System.out.println("browseRes()\n");
               break;
            case 'r':   System.out.println("viewRooms\n");
               break;
            case 'b':   exit = true;
               break;
         }
      }
   }
}
