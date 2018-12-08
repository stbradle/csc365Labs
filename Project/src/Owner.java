import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Owner extends User {
   private Room rooms[];
   private Reservation reservations[] = null;
   private int rsCount;
   private int rCount;
   
   private boolean setupRooms() {
      String countSql = "SELECT COUNT(*) AS 'Count' FROM rooms;";
      String sql = "SELECT * FROM rooms;";
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
      String countSql = "SELECT COUNT(*) AS 'Count' FROM reservations;";
      String sql = "SELECT * FROM reservations;";
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
               findOccupancy();
               break;
            case 'd':   
               System.out.println("revenueData\n");
               break;
            case 's':   
               System.out.println("browseRes()\n");
               browseReservations();
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
   
   private void findOccupancy() {
      int numDates = owner.getNumDates();
      
      if (numDates == 1) {
         checkSingleRoom();
      }
      else if (numDates == 2) {
         checkDoubleRoom();
      }
   }
   
   private void checkSingleRoom() {
      String checkIn = "bad";
      String resp;
      String status[] = new String[rCount];
      int rCodes[] = new int[rCount];
      ResultSet rs;
      boolean exit;
     
      while ("bad".compareTo(checkIn) == 0) {
         System.out.println("Enter a day to check in Month Day format (e.g. 'January 17'): ");
         checkIn = owner.getDate();
      }
      
      rs = owner.executeQuery("SELECT Room, Code FROM reservations WHERE CheckIn <= '" + checkIn + "' && "
                            + "CheckOut >= '" + checkIn + "';");
      
      try {
         for (int index = 0; index < rCount; index++) {
            status[index] = "Available";
            rCodes[index] = -1;
            while(rs.next()) {
               if (rs.getString("Room").compareTo(rooms[index].RoomId) == 0) {
                  status[index] = "Occupied";
                  rCodes[index] = rs.getInt("Code");
                  break;
               }
            }
            rs.beforeFirst();
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
         return;
      }
      
      System.out.println("");
      System.out.printf("%-4s | %-10s\n", "Room", "Status");
      System.out.println("================");
      for (int index = 0; index < rCount; index++)
         System.out.printf("%-4s | %-10s \n", rooms[index].RoomId, status[index]);
      System.out.println("");
      
      exit = false;
      while (!exit) {
         resp = owner.getRoomCode().toUpperCase();
         switch (resp) {
         case "Q":
            exit = true;
            break;
         default:
            for (int index = 0; index < rCount; index++) {
               if (resp.compareTo(rooms[index].RoomId) == 0 && "Occupied".compareTo(status[index]) == 0) {
                  for (int rIndex = 0; rIndex < rsCount; rIndex++) {
                     if (rCodes[index] == reservations[rIndex].Code) {
                        displayReservationInformation(rIndex);
                        exit = true;
                        break;
                     }
                  }
               }
               else if (resp.compareTo(rooms[index].RoomId) == 0) {
                  System.out.println("Room is available - no reservation");
                  break;
               }
            }
         }
      }
      System.out.println("");
   }
   
   private void checkDoubleRoom() {
      String checkIn = "", checkOut = "";
      while (checkIn.compareTo("bad") == 0) {
         System.out.println("Enter a check-in day in Month Day format (e.g. 'January 17'): ");
         checkIn = owner.getDate();
      }
      while (checkOut.compareTo("bad") == 0) {
         System.out.println("Enter a check-out day in Month Day format (e.g. 'January 17'): ");
         checkOut = owner.getDate();
      }
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
                                      + "FROM reservations WHERE Room = '" + rooms[roomIndex].RoomId + "' && YEAR(CheckIn) = 2010;");
      ResultSet rsTotal = owner.executeQuery("SELECT SUM(DATEDIFF(CheckOut, CheckIn) * Rate) AS 'Total' FROM reservations "
                                           + "WHERE YEAR(CheckIn) = 2010;");
      
      try {
         rs.next();
         rsTotal.next();
         days = rs.getInt("Nights");
         percent = rs.getDouble("Percent");
         profit = rs.getDouble("Profit");
         total = rsTotal.getInt("Total");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      
      System.out.println("");
      System.out.printf("%-3s | %-4s | %-30s | %-4s | %-8s | %-9s | %-5s | %-15s \n", 
            "Num", "ID", "Name", "Beds", "Bed Type", "Occupancy", "Price", "Decor");
      System.out.printf("%-3d | %-4s | %-30s | %-4d | %-8s | %-9d | %-5d | %-15s \n", roomIndex + 1, 
            rooms[roomIndex].RoomId, rooms[roomIndex].RoomName, rooms[roomIndex].Beds, rooms[roomIndex].BedType,
            rooms[roomIndex].MaxOcc, rooms[roomIndex].BasePrice, rooms[roomIndex].Decor);
      System.out.println("Total nights occupied in " + rooms[roomIndex].RoomId + ": " + days);
      System.out.println("Percent of the year occupied: " + percent);
      System.out.println("Total Profit from " + rooms[roomIndex].RoomId + ": " + profit);
      System.out.println("Percentage of Total Profits from " + rooms[roomIndex].RoomId + ": " + ((profit / total) * 100));
      
   }
   
   private void listRoomReservations(String roomID) {
      String resp;
      char option;
      int index = 0, rsCode = -1;
      boolean exit = false;
     
      String sql = "SELECT * FROM reservations WHERE Room = '" + roomID + "' ORDER BY CheckIn, CheckOut;";
      System.out.println(sql);
      ResultSet rs = owner.executeQuery("SELECT * FROM reservations WHERE Room = '" + roomID + "' ORDER BY CheckIn, CheckOut;");
      
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
      System.out.println("====================================================================================================================================================================");
      System.out.printf(" %-6d | %-7s | %-30s | %-4d | %-8s | %-8d | %-10s | %-10s | %-4d | %-15s | %-15s | %-6d | %-4d \n",
            reservations[rsIndex].Code, rooms[rmIndex].RoomId, rooms[rmIndex].RoomName, rooms[rmIndex].Beds, rooms[rmIndex].BedType, rooms[rmIndex].MaxOcc,
            reservations[rsIndex].CheckIn, reservations[rsIndex].CheckOut, reservations[rsIndex].Rate, reservations[rsIndex].LastName, reservations[rsIndex].FirstName, reservations[rsIndex].Adults, reservations[rsIndex].Kids);
   }

   private void displayReservations(){
      int index = 0;

      if (rsCount == 0){
         System.out.println("There are no reservations at this hotel");
         return;
      }

      System.out.printf("%-5s | %-4s | %-10s | %-10s | %-5s | %-30s | %-30s | %-7s | %-7s \n",
            "Code", "Room", "CheckIn", "CheckOut", "Rate", "LastName", "FirstName", "Adults", "Kids");
      System.out.println("=================================================================================================================================");
      while(index < rsCount){
         System.out.printf("%-5d | %-4s | %-10s | %-10s | %-5s | %-30s | %-30s | %-7s | %-7s \n",
               reservations[index].Code, reservations[index].Room, reservations[index].CheckIn,
               reservations[index].CheckOut,reservations[index].Rate, reservations[index].LastName,
               reservations[index].FirstName, reservations[index].Adults, reservations[index].Kids);
         index++;
      }
   }

   private void displayReservations(ResultSet res){
      if(res == null)
         return;

      int index = 0;
      ArrayList<Reservation> reservations = new ArrayList<>();
      try {
         while (res.next())
            reservations.add(new Reservation(res));
      } catch (SQLException e){
         System.out.println("SQL Error: " + e.getMessage());
      }
      if (reservations.size() == 0){
         System.out.println("No Results Found");
         return;
      }

      System.out.printf("%-5s | %-4s | %-10s | %-10s | %-5s | %-30s | %-30s | %-7s | %-7s \n",
            "Code", "Room", "CheckIn", "CheckOut", "Rate", "LastName", "FirstName", "Adults", "Kids");
      System.out.println("=================================================================================================================================");
      while(index < reservations.size()){
         System.out.printf("%-5d | %-4s | %-10s | %-10s | %-5s | %-30s | %-30s | %-7s | %-7s \n",
               reservations.get(index).Code, reservations.get(index).Room, reservations.get(index).CheckIn,
               reservations.get(index).CheckOut,reservations.get(index).Rate, reservations.get(index).LastName,
               reservations.get(index).FirstName, reservations.get(index).Adults, reservations.get(index).Kids);
         index++;
      }
   }

   private int getReservationIndex(int resCode){
      int i = 0;
      for(Reservation r: reservations) {
         if (r.Code == resCode)
            return i;
         i++;
      }
      return -1;
   }

   private void filterReservations(){
      String checkIn = null;
      String checkOut = null;
      String room = null;

      System.out.println("What would you like to filter by:\n" +
                         "- (D)ate\n" +
                         "- (R)oom ID\n" +
                         "- (B)oth");
      System.out.print("Response: ");
      char resp = owner.getResponse();
      switch (resp){
         case 'd':
            checkIn = getCheckInDate();
            checkOut = getCheckOutDate();
            displayFilteredResults(checkIn, checkOut);
            break;
         case 'r':
            room = getRoomID();
            displayFilteredResults(room);
            break;
         case 'b':
           checkIn = getCheckInDate();
           checkOut = getCheckOutDate();
           room = getRoomID();
           displayFilteredResults(checkIn, checkOut, room);
           break;
         default:
            System.out.println("Invalid Input");
      }
   }

   private void browseReservations(){
      boolean exit = false;
      while(!exit){
         System.out.println();
         System.out.println("Enter one of the following:");
         System.out.print(  " - (F)ilter - Filter reservations\n" +
                            " - (V)iew [reservation code] - View the details of the given reservation\n" +
                            " - (A)ll - lists all reservations\n" +
                            " - (B)ack - Go back\n\n");
         System.out.print("Response: ");
         String[] toks = owner.getStrResponse().split(" ");
         char mode = '0';
         if(toks[0].length() > 0)
            mode = toks[0].charAt(0);
         switch(mode){
            case 'f':
               filterReservations();
               break;
            case 'a':
               displayReservations();
               break;
            case 'v':
               int resCode = Integer.valueOf(toks[1]);
               int resIdx = getReservationIndex(resCode);
               if(resIdx >= 0)
                  displayReservationInformation(resIdx);
               else
                  System.out.println("Invalid reservation code.");
               break;
            case 'b':
               exit = true;
               break;
            default:
               System.out.println("Invalid Input");
         }
      }
   }

   private String getCheckInDate(){
      String out = "bad";
      while(out == "bad") {
         System.out.println("Enter a check-in day in 'Month Day Year' format (e.g. 'January 17 2016'):");
         out = owner.getDate();
         if(out == "bad")
            System.out.println("Invalid date, please try again");
      }
      return out;
   }

   private String getCheckOutDate(){
      String out = "bad";

      while(out == "bad") {
         System.out.println("Enter a check-out day in 'Month Day Year' format (e.g. 'January 17 2016'):");
         out = owner.getDate();
         if(out == "bad")
            System.out.println("Invalid date, please try again");
      }
      return out;
   }

   private String getRoomID(){
      System.out.println("Enter a room ID:");
      return owner.getStrResponse();
   }

   private void displayFilteredResults(String room){
      ResultSet res = owner.executeQuery(
            "SELECT * FROM reservations " +
                  "WHERE Room LIKE " + "\""+room+"\"");
      displayReservations(res);
   }

   private void displayFilteredResults(String checkIn, String checkOut){
      ResultSet res = owner.executeQuery(
            "SELECT * FROM reservations " +
                  "WHERE DATEDIFF(CheckIn, " + "\""+checkIn+"\""+") >= 0 && " +
                  "DATEDIFF(CheckIn, "+"\""+checkOut+"\""+") <= 0");
      displayReservations(res);
   }

   private void displayFilteredResults(String checkIn, String checkOut, String room){
      ResultSet res = owner.executeQuery(
            "SELECT * FROM reservations " +
                 "WHERE DATEDIFF(CheckIn," + "\""+checkIn+"\"" + ") >= 0 &&" +
                 "      DATEDIFF(CheckIn," + "\""+checkOut+"\"" + ") <= 0 &&" +
                 "      Room LIKE " + "\""+room+"\"");
      displayReservations(res);
   }
}
