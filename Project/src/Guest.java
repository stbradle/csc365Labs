import java.sql.*;

public class Guest extends User{
   private Room rooms[];
   private int rCount;
   
   private void setupRooms() {
      String countSql = "SELECT COUNT(*) AS 'Count' FROM testRooms;";
      String sql = "SELECT * FROM testRooms;";
      ResultSet rs = owner.executeQuery(sql);
      ResultSet count = owner.executeQuery(countSql);
      int index = 0;
      
      try {
         count.next();
         rCount = count.getInt("Count");
         rooms = new Room[rCount];
         while (rs.next() && index < rCount) {
            rooms[index] = new Room();
            rooms[index].RoomId = rs.getString("RoomId");
            rooms[index].RoomName = rs.getString("RoomName");
            rooms[index].Beds = rs.getInt("Beds");
            rooms[index].BedType = rs.getString("BedType");
            rooms[index].MaxOcc = rs.getInt("MaxOcc");
            rooms[index].BasePrice = rs.getInt("BasePrice");
            rooms[index].Decor = rs.getString("Decor");            
            index++;
         }
      } 
      catch (SQLException e) {
         System.out.println("SQL Error: " + e.getMessage());
      }
   }
   
   public User startSession(){
      boolean exit = false;
      setupRooms();
      
      while (!exit) {
         owner.displayMenu(this);

         char option = owner.getResponse();
   
         switch(option) {
            case 'r':   
               showRooms();
               break;
            case 's':   
               makeReservation("");
               break;
            case 'b':   
               exit = true;
               break;
            default:
               System.out.println("Invalid option - please try again.");
               break;
         }
         System.out.println("");
      }
      return null;
   }
   
   private void showRooms() {
      int index = 0;
      String response;
      boolean exit = false;
      String roomID = "";
      
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
      
      while (!exit) {
         response = owner.getRoomCode();
         
         switch (response) {
         case "q":
            exit = true;
            break;
         default:
            for (index = 0; index < rCount; index++) {
               if (response.compareTo(rooms[index].RoomId) == 0) {
                  roomID = rooms[index].RoomId;
                  break;
               }
            }
            if (index == rCount) 
               System.out.println("Please enter a valid room code.");
            else {
               exit = true;
               makeReservation(roomID);
            }
            break;
         }
         System.out.println("");
      }
   }
   
   private void makeReservation(String roomID) {
      String checkIn = "bad", checkOut = "bad";
      String checkInTok[], checkOutTok[];
      int inDay, outDay, inMonth, outMonth, inYear, outYear;


      while (checkIn.compareTo("bad") == 0) {
         System.out.println("Enter a check-in day in Month Day format (e.g. 'January 17'): ");
         checkIn = owner.getDate();
      }
      while (checkOut.compareTo("bad") == 0) {
         System.out.println("Enter a check-out day in Month Day format (e.g. 'January 17'): ");
         checkOut = owner.getDate();
      }
      
      checkInTok = checkIn.split("-");
      checkOutTok = checkOut.split("-");
      inYear = Integer.parseInt(checkInTok[0]);
      inMonth = Integer.parseInt(checkInTok[1]);
      inDay = Integer.parseInt(checkInTok[2]);
      outYear = Integer.parseInt(checkOutTok[0]);
      outMonth = Integer.parseInt(checkOutTok[1]);
      outYear = Integer.parseInt(checkOutTok[2]);
      
      if ("".compareTo(roomID) == 0) {
         int index, rsCount;
         String status[] = new String[rCount];
         ResultSet rs = owner.executeQuery("");
         
         try {
            System.out.printf(" %-4s | %-9s \n", "Room", "Status");
            System.out.println("=================");
            for (index = 0; index < rCount; index++) {
               status[index] = "Available";
               while (rs.next()) {
                  System.out.println("Room " + rs.getString("Room") + ", dates are " + rs.getString("CheckIn") + " " + rs.getString("CheckOut"));
                  if (rs.getString("Room").compareTo(rooms[index].RoomId) == 0) {
                     System.out.println("comparing room to room: " + rs.getString("Room") + " " + rooms[index].RoomId);
                     status[index] = "Occupied";
                     break;
                  }
                  
               }
               rs.beforeFirst();
               System.out.printf(" %-4s | %-9s \n", rooms[index].RoomId, status[index]);
            }
            
         }
         catch (SQLException e) {
            
         }
      }
      else {
         
         System.out.println("Make a reservation for a single room...");
      }
   }
}
