import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
         String[] toks = out.split(" ");
         char option = toks[0].length() > 0 ? toks[0].charAt(0) : 'f';

         switch(option) {
            case 'o':   
               findOccupancy();
               break;
            case 'd':   
               System.out.println("revenueData\n");
               if(toks.length < 2)
                  System.out.println("Usage: (D)ata [(c)ounts | (d)ays | (r)evenue]");
               else
                  viewData(toks[1]);
               break;
            case 's':   
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

   private void viewData(String mode){
      char choice = mode.length() > 0 ? mode.charAt(0) : '0';
      switch (choice){
         case 'c':
            viewCountsData();
            break;
         case 'd':
            viewDaysData();
            break;
         case 'r':
            viewRevenueData();
            break;
         default:
            System.out.println("Invalid response");
      }
   }

   private void viewRevenueData(){
      //language=SQL
      ResultSet res = owner.executeQuery(
            "SELECT r.RoomId, MONTH(rs.CheckOut) as `Month`, SUM(Rate * DATEDIFF(rs.CheckOut, rs.CheckIn)) as `Revenue`" +
                  "FROM rooms r, reservations rs " +
                  "WHERE rs.Room LIKE r.RoomId " +
                  "GROUP BY r.RoomId, MONTH(rs.CheckOut)");
      try {
         HashMap<String, ArrayList<Integer>> table = new HashMap<>();
         while (res.next()){
            String roomId = res.getString("RoomId");
            int month = res.getInt("Month");
            int revenue = res.getInt("Revenue");

            if(!table.containsKey(roomId))
               table.put(roomId, new ArrayList<>(13));

            table.get(roomId).add(month-1, revenue);
         }

         table = buildTable(table);

         printTable(table, "Revenue");

      } catch (SQLException e){
         System.out.println("SQL Error: " + e.getMessage());
      }

   }

   private void viewDaysData(){
      //language=SQL
      ResultSet res = owner.executeQuery(
            "SELECT r.RoomId, MONTH(rs.CheckOut) as `Month`, SUM(DATEDIFF(rs.CheckOut, rs.CheckIn)) as `DaysOccupied`" +
                  "FROM rooms r, reservations rs " +
                  "WHERE rs.Room LIKE r.RoomId " +
                  "GROUP BY r.RoomId, MONTH(rs.CheckOut)");
      try {
         HashMap<String, ArrayList<Integer>> table = new HashMap<>();
         while (res.next()){
            String roomId = res.getString("RoomId");
            int month = res.getInt("Month");
            int daysOccupied = res.getInt("DaysOccupied");

            if(!table.containsKey(roomId))
               table.put(roomId, new ArrayList<>(13));

            table.get(roomId).add(month-1, daysOccupied);
         }

         table = buildTable(table);

         printTable(table, "DaysOccupied");

      } catch (SQLException e){
         System.out.println("SQL Error: " + e.getMessage());
      }

   }

   private void viewCountsData(){
      //language=SQL
      ResultSet res = owner.executeQuery(
            "SELECT r.RoomId, MONTH(rs.CheckOut) as `Month`, COUNT(DISTINCT rs.Code) as `Reservations`" +
                  "FROM rooms r, reservations rs " +
                  "WHERE rs.Room LIKE r.RoomId " +
                  "GROUP BY r.RoomId, MONTH(rs.CheckOut)");
      try {
         HashMap<String, ArrayList<Integer>> table = new HashMap<>();
         while (res.next()){
            String roomId = res.getString("RoomId");
            int month = res.getInt("Month");
            int reservations = res.getInt("Reservations");

            if(!table.containsKey(roomId))
               table.put(roomId, new ArrayList<>(13));

            table.get(roomId).add(month-1, reservations);
         }

         table = buildTable(table);

         printTable(table, "Reservations");

      } catch (SQLException e){
         System.out.println("SQL Error: " + e.getMessage());
      }

   }

   private HashMap<String, ArrayList<Integer>> buildTable(HashMap<String, ArrayList<Integer>> table){
      ArrayList<Integer> totals = new ArrayList<>(
            Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0));

      for(String key: table.keySet()){
         int sum = 0;
         ArrayList<Integer> vals = table.get(key);
         int i = 0;
         for(int val: vals) {
            int curTotal = totals.get(i);
            totals.set(i, curTotal + val);
            sum += val;
            i++;
         }
         table.get(key).add(sum);
      }
      System.out.println(totals);
      int sumOfTotals = 0;
      for(int total: totals) {
         sumOfTotals += total;
         System.out.println(sumOfTotals);
      }

      totals.set(12, sumOfTotals);
      table.put("Total", totals);

      return table;
   }

   private void printTable(HashMap<String, ArrayList<Integer>> table, String mode){
      System.out.println("__________________________________________________________________________________________________________________________________");
      System.out.printf("| %-7s | %-5s | %-5s | %-5s | %-5s | %-5s | %-5s | %-5s | %-5s | %-5s | %-5s | %-5s | %-5s | %-20s |\n",
            "Room", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Total (" + mode + ")");
      System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
      ArrayList<Integer> totals = null;
      for(String key: table.keySet()){
         if(key == "Total") {
            totals = table.get(key);
            continue;
         }
         ArrayList<Integer> v = table.get(key);
         System.out.printf("| %-7s | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-20d |\n",
               key, v.get(0), v.get(1), v.get(2), v.get(3), v.get(4), v.get(5), v.get(6), v.get(7), v.get(8), v.get(9), v.get(10), v.get(11), v.get(12));
      }
      if(totals != null)
         System.out.printf("| %-7s | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-5d | %-20d |\n",
            "Total", totals.get(0), totals.get(1), totals.get(2), totals.get(3), totals.get(4), totals.get(5), totals.get(6), totals.get(7), totals.get(8), totals.get(9), totals.get(10), totals.get(11), totals.get(12));
      System.out.println("----------------------------------------------------------------------------------------------------------------------------------");

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
                        displayReservationInformationHeader();
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
      String checkIn = "bad", checkOut = "bad", status[] = new String[rCount], resp = "";
      ResultSet rs;
      int diff;
      boolean exit = false;
      Date inDate;
      Calendar c1 = Calendar.getInstance();
      
      while (checkIn.compareTo("bad") == 0) {
         System.out.println("Enter a check-in day in Month Day format (e.g. 'January 17'): ");
         checkIn = owner.getDate();
      }
      while (checkOut.compareTo("bad") == 0) {
         System.out.println("Enter a check-out day in Month Day format (e.g. 'January 17'): ");
         checkOut = owner.getDate();
      }
      
      // basic setup and error checking
      try {
         rs = owner.executeQuery("SELECT DATEDIFF('" + checkOut + "', '" + checkIn + "') AS 'Diff';");
         rs.next();
         diff = rs.getInt("Diff");
         
      } catch (SQLException e) {
         System.out.println(e.getMessage());
         return;
      }
      try {
         inDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkIn);
         c1.setTime(inDate);
      } catch (ParseException e) {
         System.out.println(e.getMessage());
         return;
      }
      
      // get occupation status for rooms
      System.out.printf("%-4s | %-20s \n", "Room", "Status");
      System.out.println("===========================");
      for (int index = 0; index < rCount; index++) {
         status[index] = "Available";
         for (int i = 0; i <= diff; i++) {
            status[index] = getRoomOccupationStatus(c1, status[index], i, index);
            if ("Partially Occupied".compareTo(status[index]) == 0)
               break;
            else if ("bad".compareTo(status[index]) == 0)
               return;
            c1.add(Calendar.DATE, 1);
         }
         c1.setTime(inDate);
         System.out.printf("%-4s | %-20s \n", rooms[index].RoomId, status[index]);
      }
      System.out.println("");
      
      exit = false;
      while (!exit) {
         resp = owner.getRoomCode().toUpperCase();
         switch (resp) {
         case "Q":
            exit = true;
            break;
         default:
            // print out all reservations associated with that room and time
            for (int index = 0; index < rCount; index++) {
               if (resp.compareTo(rooms[index].RoomId) == 0 && "Available".compareTo(status[index]) != 0) {
                  rs = owner.executeQuery("SELECT * "
                        + "FROM reservations "
                        + "WHERE ((CheckIn < '" + checkOut + "' && NOT(CheckOut <= '" + checkIn + "')) || "
                              + "(CheckOut > '" + checkIn + "' && NOT(CheckIn >= '" + checkOut + "')) || "
                              + "(CheckIn >= '" + checkIn + "' && CheckOut <= '" + checkOut + "')) "
                              + "&& Room = '" + rooms[index].RoomId + "' ORDER BY CheckIn, CheckOut;");
                  try {
                     System.out.printf("%-4s | %-6s \n", "Room", "Code");
                     System.out.println("=============");
                     while(rs.next()) {
                        for (int index2 = 0; index2 < rsCount; index2++) {
                           if (reservations[index2].Code == rs.getInt("Code"))
                              System.out.printf("%-4s | %-6d \n", reservations[index2].Room, reservations[index2].Code);
                        }
                     }
                  
                  } catch (SQLException e) {
                     System.out.println(e.getMessage());
                     return;
                  }
                  
                  // check a specific reservation
                  while (!exit)
                     exit = getSpecificReservation();
                  
                  System.out.println("");
                  break;
               }
               else if (resp.compareTo(rooms[index].RoomId) == 0) {
                  System.out.println("Room is available - no reservation");
                  break;
               }
            }
         }
      }
   }
   
   private String getRoomOccupationStatus(Calendar c1, String currString, int currIndex, int rmIndex) {
      int day, month, year, count;
      String tripDay;
      ResultSet rs;
      
      day = c1.get(Calendar.DAY_OF_MONTH);
      month = c1.get(Calendar.MONTH) + 1; // month starts at 0??? this is a java thing...
      year = c1.get(Calendar.YEAR);
      tripDay = year + "-" + month + "-" + day;
      
      try {
         rs = owner.executeQuery("SELECT COUNT(*) AS 'Count' FROM reservations "
                                + "WHERE CheckIn <= '" + tripDay + "' && CheckOut >= '" + tripDay + "' && Room = '" + rooms[rmIndex].RoomId + "';");
         rs.next();
         count = rs.getInt("Count");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
         return "bad";
      }
      
      if (count != 0 && currIndex == 0 && "Available".compareTo(currString) == 0) {
         return "Fully Occupied";
      }
      else if (count != 0 && "Available".compareTo(currString) == 0) {
         return "Partially Occupied";
      }
      else if (count == 0 && "Fully Occupied".compareTo(currString) == 0) {
         return "Partially Occupied";
      }
      return currString;
   }
   
   private boolean getSpecificReservation() {
      int rsCode;
      String resp = owner.getReservationCode();
      char option = resp.length() > 0? resp.toLowerCase().charAt(0) : 'f';
      switch (option) {
      case 'q':
         return true;
      default:
         try {
            rsCode = Integer.parseInt(resp);
         }
         catch (NumberFormatException e) {
            System.out.println("Invalid response");
            return false;
         }
         int index3 = getReservationIndex(rsCode);
         if (index3 == -1) {
            System.out.println("Reservation Code " + rsCode + " does not exist! Please enter a valid reservation.");
            return false;
         }
         else {
            displayReservationInformationHeader();
            displayReservationInformation(index3);
            return true;
         }
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
               displayReservationInformationHeader();
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
      
      System.out.printf(" %-6d | %-7s | %-30s | %-4d | %-8s | %-8d | %-10s | %-10s | %-4d | %-15s | %-15s | %-6d | %-4d \n",
            reservations[rsIndex].Code, rooms[rmIndex].RoomId, rooms[rmIndex].RoomName, rooms[rmIndex].Beds, rooms[rmIndex].BedType, rooms[rmIndex].MaxOcc,
            reservations[rsIndex].CheckIn, reservations[rsIndex].CheckOut, reservations[rsIndex].Rate, reservations[rsIndex].LastName, reservations[rsIndex].FirstName, reservations[rsIndex].Adults, reservations[rsIndex].Kids);
   }

   private void displayReservationInformationHeader() {
      System.out.println("");
      System.out.printf(" %-6s | %-7s | %-30s | %-4s | %-8s | %-8s | %-10s | %-10s | %-4s | %-15s | %-15s | %-6s | %-4s \n", 
            "Code", "Room ID", "Room Name", "Beds", "Bed Type", "Max Occ.",
            "Check In", "Check Out", "Rate", "Last Name", "First Name", "Adults", "Kids");
      System.out.println("====================================================================================================================================================================");
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
               if(resIdx >= 0) {
                  displayReservationInformationHeader();
                  displayReservationInformation(resIdx);
               }
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
