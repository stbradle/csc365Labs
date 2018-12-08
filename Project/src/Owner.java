import java.sql.ResultSet;
import java.sql.SQLException;

public class Owner extends User {
   private Room rooms[];
   private Reservation reservations[] = null;
   private int rsCount;
   private int rCount;
   
   private boolean setupRooms() {
      String countSql = "SELECT COUNT(*) AS 'Count' FROM testRooms;";
      String sql = "SELECT * FROM testRooms;";
      int index = 0;
      
      try {
         ResultSet rs = owner.executeQuery(sql);
         ResultSet count = owner.executeQuery(countSql);
         count.next();
         rCount = count.getInt("Count");
         rooms = new Room[rCount];
         while (rs.next() && index < rCount) {
            rooms[index] = new Room(rs);
            index++;
         }
      } 
      catch (Exception e) {
         System.out.println("SQL Error: " + e.getMessage());
         return false;
      }
      return true;
   }
   
   private boolean setupReservations() {
      String countSql = "SELECT COUNT(*) AS 'Count' FROM testReservations;";
      String sql = "SELECT * FROM testReservations;";
      int index = 0;
      
      try {
         ResultSet rs = owner.executeQuery(sql);
         ResultSet count = owner.executeQuery(countSql);
         count.next();
         rsCount = count.getInt("Count");
         reservations = new Reservation[rsCount];
         while (rs.next() && index < rsCount) {
            reservations[index] = new Reservation(rs);
            index++;
         }
      } 
      catch (SQLException e) {
         System.out.println("SQL Error: " + e.getMessage());
         return false;
      }
      return true;
   }
   
   public User startSession(){
      boolean exit = false;
      
      if (!setupRooms()) {
         System.out.println("Error while setting up owner subsystem.");
         return null;
      }
      if (!setupReservations()) {
         System.out.println("Error while setting up owner subsystem.");
         return null;
      }
      
      while (!exit) {
         owner.displayMenu(this);

         String out = owner.getStrResponse();
         char option = out.length() > 0 ? out.charAt(0) : 'f';

         switch(option) {
            case 'o':   
               System.out.println("occupancyMenu\n");
               break;
            case 'd':   
               System.out.println("revenueData\n");
               break;
            case 's':   
               System.out.println("browseRes()\n");
               break;
            case 'r':   
               viewRooms();
               break;
            case 'b':   
               exit = true;
               break;
            default:
               System.out.println("Invalid input - please enter one of the options specificed");
               break;
         }
      }
      return null;
   }
   
   private void viewRooms() {
      int index = 0;
      String response;
      boolean exit = false;
      String roomID = "";
      
      if (rCount == 0) {
         System.out.println("There are no rooms at this hotel... are you even a hotel?");
         return;
      }
      
      System.out.printf("%-3s | %-4s | %-30s | %-4s | %-8s | %-9s | %-5s | %-15s \n", 
            "Num", "ID", "Name", "Beds", "Bed Type", "Occupancy", "Price", "Decor");
      System.out.println("================================================================================================");
      while (index < rCount) {          
         System.out.printf("%-3d | %-4s | %-30s | %-4d | %-8s | %-9d | %-5d | %-15s \n", index + 1, 
               rooms[index].RoomId, rooms[index].RoomName, rooms[index].Beds, rooms[index].BedType,
               rooms[index].MaxOcc, rooms[index].BasePrice, rooms[index].Decor);
         index++;
      }
      
      while (!exit) {
         response = owner.viewRooms().toUpperCase();
         char output = response.length() > 2 ? response.charAt(0) : 'f';
         
         switch (output) {
         case 'V':
            roomID = response.substring(2, response.length());
            for (int i = 0; i < rCount; i++) {
               if (rooms[i].RoomId.compareTo(roomID) == 0) {
                  viewFullRoomInfo(i);
                  exit = true;
                  break;
               }
            }
            if (!exit)
               System.out.println("Invalid room code");
            exit = true;
            break;
         case 'R':
            roomID = response.substring(2, response.length());
            for (int i = 0; i < rCount; i++) {
               if (rooms[i].RoomId.compareTo(roomID) == 0) {
                  listRoomReservations(roomID);
                  exit = true;
                  break;
               }
            }
            if (!exit)
               System.out.println("Invalid room code");
            break;
         case 'Q':
            exit = true;
            break;
         default:
            System.out.println("Invalid input");
            break;
         }
         System.out.println("");
      }
   }
   
   private void viewFullRoomInfo(int roomIndex) {
      int days = -1;
      double percent = -1, profit = -1, total = -1;
      ResultSet rs = owner.executeQuery("SELECT SUM(DATEDIFF(CheckOut, CheckIn)) AS 'Nights',"
                                      + "SUM(DATEDIFF(CheckOut, CheckIn)) / 365 * 100 AS 'Percent',"
                                      + "SUM(DATEDIFF(CheckOut, CheckIn) * Rate) AS 'Profit'"
                                      + "FROM testReservations WHERE Room = '" + rooms[roomIndex].RoomId + "' && YEAR(CheckIn) = 2010;");
      ResultSet rsTotal = owner.executeQuery("SELECT SUM(DATEDIFF(CheckOut, CheckIn) * Rate) AS 'Total' FROM testReservations"
                                           + "WHERE YEAR(CheckIn) = 2010;");
      
      try {
         rs.next();
         rsTotal.next();
         days = rs.getInt("Nights");
         percent = rs.getDouble("Percent");
         profit = rs.getDouble("Profit");
         total = rsTotal.getDouble("Total");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      
      System.out.printf("%-3s | %-4s | %-30s | %-4s | %-8s | %-9s | %-5s | %-15s \n", 
            "Num", "ID", "Name", "Beds", "Bed Type", "Occupancy", "Price", "Decor");
      System.out.printf("%-3d | %-4s | %-30s | %-4d | %-8s | %-9d | %-5d | %-15s \n", roomIndex + 1, 
            rooms[roomIndex].RoomId, rooms[roomIndex].RoomName, rooms[roomIndex].Beds, rooms[roomIndex].BedType,
            rooms[roomIndex].MaxOcc, rooms[roomIndex].BasePrice, rooms[roomIndex].Decor);
      System.out.println("Total nights occupied in " + rooms[roomIndex].RoomId + ": " + days);
      System.out.println("Percent of the year occupied: " + percent);
      System.out.println("Total Profit from " + rooms[roomIndex].RoomId + ": " + profit);
      System.out.println("Percentage of Total Profits from " + rooms[roomIndex].RoomId + ": " + (percent / total));
      
   }
   
   private void listRoomReservations(String roomID) {
      String resp;
      char option;
      int index = 0, rsCode = -1;
      boolean exit = false;
     
      String sql = "SELECT * FROM testReservations WHERE Room = '" + roomID + "' ORDER BY CheckIn, CheckOut;";
      System.out.println(sql);
      ResultSet rs = owner.executeQuery("SELECT * FROM testReservations WHERE Room = '" + roomID + "' ORDER BY CheckIn, CheckOut;");
      
      try {
         System.out.println("");
         System.out.printf("%-6s | %-4s | %-10s | %-10s | %-4s | %-15s | %-15s | %-6s | %-4s \n",
               "Code", "Room", "Check In", "Check Out", "Rate", "Last Name", "First name", "Adults", "Kids");
         System.out.println("===================================================================================================");
         while(rs.next()) {
            System.out.printf("%-6s | %-4s | %-10s | %-10s | %-4s | %-15s | %-15s | %-6s | %-4s \n",
               rs.getInt("Code"), rs.getString("Room"), rs.getString("CheckIn"), rs.getString("CheckOut"), rs.getInt("Rate"),
               rs.getString("LastName"), rs.getString("FirstName"), rs.getInt("Adults"), rs.getInt("Kids"));
         }
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
         return;
      }
      exit = false;
      while (!exit) {
         resp = owner.getReservationCode();
         option = resp.length() > 0? resp.toLowerCase().charAt(0) : 'f';
         switch (option) {
         case 'q':
            exit = true;
         default:
            try {
               rsCode = Integer.parseInt(resp);
            }
            catch (NumberFormatException e) {
               System.out.println("Invalid response");
               break;
            }
            index = 0;
            while (index < rsCount) {
               if (reservations[index].Code == rsCode)
                  break;
               index++;
            }
            if (index == rsCount)
               System.out.println("Reservation Code " + rsCode + " does not exist! Please enter a valid reservation.");
            else {
               displayReservationInformation(index);
               exit = true;
            }
            break;
         }
      }
   }
   
   private void displayReservationInformation(int rsIndex) {
      int rmIndex;
      for (rmIndex = 0; rmIndex < rCount; rmIndex++)
         if (reservations[rsIndex].Room.compareTo(rooms[rmIndex].RoomId) == 0)
            break;
      
      System.out.println("");

      System.out.printf(" %-6s | %-7s | %-30s | %-4s | %-8s | %-8s | %-10s | %-10s | %-4s | %-15s | %-15s | %-6s | %-4s \n", 
            "Code", "Room ID", "Room Name", "Beds", "Bed Type", "Max Occ.",
            "Check In", "Check Out", "Rate", "Last Name", "First Name", "Adults", "Kids");
      System.out.printf(" %-6d | %-7s | %-30s | %-4d | %-8s | %-8d | %-10s | %-10s | %-4d | %-15s | %-15s | %-6d | %-4d \n",
            reservations[rsIndex].Code, rooms[rmIndex].RoomId, rooms[rmIndex].RoomName, rooms[rmIndex].Beds, rooms[rmIndex].BedType, rooms[rmIndex].MaxOcc,
            reservations[rsIndex].CheckIn, reservations[rsIndex].CheckOut, reservations[rsIndex].Rate, reservations[rsIndex].LastName, reservations[rsIndex].FirstName, reservations[rsIndex].Adults, reservations[rsIndex].Kids);
   }
}
