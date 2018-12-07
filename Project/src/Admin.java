import java.sql.ResultSet;

public class Admin extends User {
   private int rCount;
   private Room[] rooms;
   private Reservation[] reservations;

   public User startSession(){
      boolean exit = false;

      while (!exit) {
         owner.checkTableStatus();
         owner.displayMenu(this);

         String[] toks = owner.getStrResponse().split(" ");
         char option = toks[0].charAt(0);

         switch(option) {
            case 'v':
               System.out.println("displayTable\n");
               if (toks.length < 2){
                  System.out.println("Usage: View [tablename]");
                  break;
               }
               displayTable(toks[1]);
               break;
            case 'c':
               System.out.println("clearDB\n");
               deleteTableEntries();
               break;
            case 'l':
               System.out.println("loadDB\n");
               loadTables();
               break;
            case 'r':
               System.out.println("removeDB\n");
               deleteTables();
               break;
            case 's':
               System.out.println("switch subsystem\n");
               return switchSubsystem(toks[1]);
            case 'b':
               exit = true;
               break;
         }
      }
      return null;
   }

   private void displayTable(String table){
      System.out.println(table);
      ResultSet res = owner.executeQuery("SELECT * FROM " + table);
      if(res == null)
         return;
      ResultSet cnt = owner.executeQuery("SELECT COUNT(*) FROM " + table);
      if(cnt == null)
         return;
      try {
         cnt.next();
         rCount = cnt.getInt(1);
         int index = 0;
         switch (table){
            case "rooms":
               rooms = new Room[rCount];
               while (res.next() && index < rCount) {
                  rooms[index++] = new Room(res);
               }
               showRooms();
               break;
            case "reservations":
               reservations = new Reservation[rCount];
               while(res.next() && index < rCount)
                  reservations[index++] = new Reservation(res);
               showReservations();
               break;
         }

      } catch (Exception e){
         e.printStackTrace();
      }
   }

   private User switchSubsystem(String type){
      return UserFactory.getUserForType(type.charAt(0));
   }

   private void showRooms() {
      int index = 0;

      if (rCount == 0) {
         System.out.println("There are no rooms at this hotel... what a bad hotel");
         return;
      }

      System.out.printf("%-3s | %-4s | %-30s | %-4s | %-8s | %-9s | %-5s | %-15s \n",
            "Num", "ID", "Name", "Beds", "Bed Type", "Occupancy", "Price", "Decor");
      System.out.println("================================================================================================");
      while (index < rCount) {
         System.out.printf("%-3d | %-4s | %-30s | %-4d | %-8s | %-9d | %-5d | %-15s \n", index,
               rooms[index].RoomId, rooms[index].RoomName, rooms[index].Beds, rooms[index].BedType,
               rooms[index].MaxOcc, rooms[index].BasePrice, rooms[index].Decor);
         index++;
      }
   }

   private void showReservations(){
      int index = 0;

      if (rCount == 0){
         System.out.println("There are no reservations at this hotel");
         return;
      }

      System.out.printf("%-5s | %-4s | %-10s | %-10s | %-5s | %-30s | %-30s | %-7s | %-7s \n",
            "Code", "Room", "CheckIn", "CheckOut", "Rate", "LastName", "FirstName", "Adults", "Kids");
      System.out.println("=================================================================================================================================");
      while(index < rCount){
         System.out.printf("%-5d | %-4s | %-10s | %-10s | %-5s | %-30s | %-30s | %-7s | %-7s \n",
               reservations[index].Code, reservations[index].Room, reservations[index].CheckIn,
               reservations[index].CheckOut,reservations[index].Rate, reservations[index].LastName,
               reservations[index].Firstname, reservations[index].Adults, reservations[index].Kids);
         index++;
      }
   }

   private void deleteTableEntries(){
      owner.execute("DELETE FROM rooms");
      owner.execute("DELETE FROM reservations");
   }

   private void deleteTables(){
      owner.execute("DROP TABLE rooms");
      owner.execute("DROP TABLE reservations");
   }

   private void loadTables(){
      owner.checkTableStatus();
      switch (owner.getTblStatus()){
         case FULL:
            System.out.println("Tables already exist and are populated");
            break;
         case NO_DATABASE:
            owner.execute("CREATE TABLE rooms AS SELECT * FROM INN.rooms");
            owner.execute("CREATE TABLE reservations AS SELECT * FROM INN.reservations");
            break;
         case EMPTY:
            owner.execute("INSERT INTO rooms SELECT * FROM INN.rooms");
            owner.execute("INSERT INTO reservations SELECT * FROM INN.reservations");
            break;
      }
   }
}