public class Admin extends User {

   public void startSession(){
      boolean exit = false;

      while (!exit) {
         owner.displayMenu(this);

         char option = owner.getResponse();
         System.out.println("option chosen: " + option);

         switch(option) {
            case 'v':
               System.out.println("displayTable\n");
               break;
            case 'c':
               System.out.println("clearDB\n");
               break;
            case 'l':
               System.out.println("loadDB\n");
               break;
            case 'r':
               System.out.println("removeDB\n");
               break;
            case 's':
               System.out.println("switch subsystem\n");
               break;
            case 'b':
               exit = true;
               break;
         }
      }
   }
}
