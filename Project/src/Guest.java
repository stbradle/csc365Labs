import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;

// TODO: finish up the single room reservation, find the price proper, and then test.

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
      char option;
      
      while (!exit) {
         owner.displayMenu(this);

         option = owner.getResponse();
   
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
      String checkIn = "bad", checkOut = "bad", response = "", rCode;
      String checkInTok[], checkOutTok[];
      int inDay, outDay, inMonth, outMonth, inYear, outYear;
      boolean exit = false;
      char option;

      while (checkIn.compareTo("bad") == 0) {
         System.out.println("Enter a check-in day in Month Day Year format (e.g. 'January 17 2010'): ");
         checkIn = owner.getDate();
      }
      while (checkOut.compareTo("bad") == 0) {
         System.out.println("Enter a check-out day in Month Day Year format (e.g. 'January 17 2010'): ");
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
      
      // get the available dates for every room
      if ("".compareTo(roomID) == 0) {
         int index;
         String status[] = new String[rCount];
         ResultSet rs = owner.executeQuery("SELECT * "
                                           + "FROM testReservations "
                                           + "WHERE (CheckIn < '" + checkOut + "' && NOT(CheckOut <= '" + checkIn + "')) || "
                                                 + "(CheckOut > '" + checkIn + "' && NOT(CheckIn >= '" + checkOut + "')) || "
                                                 + "(CheckIn >= '" + checkIn + "' && CheckOut <= '" + checkOut + "');");
         
         try {
            System.out.printf(" %-4s | %-10s | %-9s \n", "Room", "Base Price", "Status");
            System.out.println("===============================");
            for (index = 0; index < rCount; index++) {
               status[index] = "Available";
               while (rs.next()) {
                  if (rs.getString("Room").compareTo(rooms[index].RoomId) == 0) {
                     status[index] = "Occupied";
                     break;
                  }
               }
               rs.beforeFirst();
               System.out.printf(" %-4s | %-10d | %-9s \n", rooms[index].RoomId, rooms[index].BasePrice, status[index]);
            }
            
            // reserve a room
            System.out.println("");
            while (!exit) {
               System.out.println("Enter (R)eserve [room code] to reserve a room, or (q) to quit.");
               response = owner.getStrResponse();
               response = response.toUpperCase();
               option = response.charAt(0);
               
               switch(option) {
                  case 'Q':
                     exit = true;
                     break;
                  case 'R':
                     if (!(response.length() > 1)) {
                        System.out.println("Please enter a room code with 'R'");
                        break;
                     }
                     rCode = response.substring(2, response.length());
                     for (index = 0; index < rCount; index++) {
                        if (rCode.compareTo(rooms[index].RoomId) == 0 && status[index] == "Available") {
                           createRegistration(index, checkIn, checkOut);
                           exit = true;
                           break;
                        }
                        else if (rCode.compareTo(rooms[index].RoomId) == 0) {
                           System.out.println("I'm sorry, but that room isn't available; please try a different room.");
                           break;
                        }
                     }
                     if (index == rCount)
                        System.out.println("Invalid room code");
                     break;     
                  default:
                     System.out.println("Invalid command");
               }
            }            
         }
         catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
      // get the available dates for a single room
      else {
         
         System.out.println("Make a reservation for a single room...");
      }
   }
   
   private void createRegistration(int rmIndex, String checkIn, String checkOut) {
      String firstName, lastName, disc, weekday;     
      int adults = 0, kids = 0, sum = 99;
      int endPrice = 0;
      double discountAmount = 1.0, dayAmount = 1.0, price = 0;
      boolean valid = false, exit = false;
      Date inDate;
      int day, month, year;
      Calendar c1 = Calendar.getInstance();
      ResultSet rs;
      int diff = 0;
      
      // get day discount information
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
      
      try {
         price = rooms[rmIndex].BasePrice;
         for (int i = 0; i <= diff; i++) {
            day = c1.get(Calendar.DAY_OF_MONTH);
            month = c1.get(Calendar.MONTH) + 1; // month starts at 0??? this is a java thing...
            year = c1.get(Calendar.YEAR);
            disc = year + "-" + month + "-" + day;
            rs = owner.executeQuery("SELECT DAYNAME('" + disc + "') AS 'Weekday';");
            rs.next();
            weekday = rs.getString("Weekday");
            if ("Sunday".compareTo(weekday) == 0 || "Saturday".compareTo(weekday) == 0) {
               dayAmount = 1.1;
            }
            
            if ((day == 1 && month == 1) || (day == 4 && month == 7) || (day == 6 && month == 9) || (day == 30 && month == 10)) {
               dayAmount = 1.25;
               break;
            }
            c1.add(Calendar.DATE, 1);
         }
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
         return;
      }
      price *= dayAmount; 
      
      System.out.println("");
      System.out.println("");
      System.out.println("Room Information:");
      System.out.println("  RoomID: " + rooms[rmIndex].RoomId);
      System.out.println("  Room Name: " + rooms[rmIndex].RoomName);
      System.out.println("  Number of Beds: " + rooms[rmIndex].Beds);
      System.out.println("  Bed Type: " + rooms[rmIndex].BedType);
      System.out.println("  Max Occupancy: " + rooms[rmIndex].MaxOcc);
      System.out.println("  Decor: " + rooms[rmIndex].Decor);
      System.out.println("");
      System.out.println("Price per night of Room (sans discounts): " + Math.ceil(price));
      
      while (!exit) {
         firstName = owner.getFirstName();
         lastName = owner.getLastName();
         
         sum = rooms[rmIndex].MaxOcc + 1;
         while (sum > rooms[rmIndex].MaxOcc) {
            adults = owner.getNumAdults();
            kids = owner.getNumChildren();
            sum = adults + kids;
            if (sum > rooms[rmIndex].MaxOcc)
               System.out.println("Exceeding maximum room occupancy - please enter a smaller number of people.");
         }
         
         valid = false;
         while (!valid) {
            disc = owner.getDiscount();
            switch (disc) {
            case "AAA":
               discountAmount = 0.9;
               valid = true;
               break;
            case "AARP":
               discountAmount = 0.85;
               valid = true;
               break;
            case "NONE":
               discountAmount = 1.0;
               valid = true;
               break;
            default:
               System.out.println("Invalid discount; please try again");
               break;
            }
         }
         endPrice = (int) Math.ceil(price * discountAmount);
         
         System.out.println("");
         System.out.println("");
         System.out.println("Reservation Information: ");
         System.out.println("First Name: " + firstName);
         System.out.println("Last Name: " + lastName);
         System.out.println("Number of Adults: " + adults);
         System.out.println("Number of Children: " + kids);
         System.out.println("Room: " + rooms[rmIndex].RoomName + "(" + rooms[rmIndex].RoomId + ")");
         System.out.println("Price per night: " + endPrice);
         System.out.println("");
         System.out.println("Is this OK? Enter (Y) to confirm, (N) to reject, or (S) to start over.");
         
         valid = false;
         while (!valid) {
            disc = owner.getStrResponse().toUpperCase();
            switch (disc) {
            case "Y":
               exit = true;
               valid = true;
               addNewReservation(rooms[rmIndex].RoomId, checkIn, checkOut, endPrice, lastName, firstName, adults, kids);
               break;
            case "N":
               exit = true;
               valid = true;
               break;
            case "S":
               valid = true;
               break;
            default:
               System.out.println("Please enter (Y) to confirm or (N) to reject.");
            }
         }
      }
   }
   
   private void addNewReservation(String rmCode, String checkIn, String checkOut, int rate, String lastName, String firstName, int adults, int kids) {
      int newCode = -1;
      ResultSet rs = owner.executeQuery("SELECT MAX(Code) AS 'Max' FROM testReservations;");
      try {
         rs.next();
         newCode = rs.getInt("Max") + 1;     
      } catch (SQLException e) {
         System.out.println(e.getMessage());
         return;
      }  
      String sql = "INSERT INTO testReservations(Code, Room, CheckIn, CheckOut, Rate, LastName, FirstName, Adults, Kids"
                   + "VALUES(" + newCode + ", '" + rmCode + "', '" + checkIn + "', '" + checkOut + "', " + rate 
                   + ", '" + lastName + "', '" + firstName + "', " + adults + ", " + kids + ");";
      owner.executeQuery(sql);
      
      System.out.println("Your reservation is complete: your reservation code is " + newCode);
   }  
}
