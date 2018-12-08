import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.in;

@SuppressWarnings("SqlNoDataSourceInspection")
public class ReservationSystem {
   private Connection conn;
   private ReservationSystemUI ui = new ReservationSystemUI(this);
   private User user;
   private static ReservationSystemInput input = new ReservationSystemInput();

   private TableStatus tblStatus;

   ReservationSystem(File config){
      try {
         loadDriver();

         Scanner configScanner = new Scanner(config);
         String url = configScanner.nextLine();
         String userName = configScanner.nextLine();
         String pw = configScanner.nextLine();
         conn = DriverManager.getConnection(url, userName, pw);
      } catch (DriverNotFoundException cnf){
         System.out.println("Error while loading jdbc driver.");
         exit(1);
      } catch (FileNotFoundException fnf){
         System.out.println("Unable to find ServerSettings.txt");
         exit(1);
      } catch (SQLException sql){
         System.out.println("SQLException: " + sql.getMessage());
         System.out.println("SQLState: " + sql.getSQLState());
         System.out.println("VendorError: " + sql.getErrorCode());
         exit(1);
      }
   }

   public void start(){
      boolean running = true;
      checkForInnTables();
      checkTableStatus();
      while(running) {
         if(user == null) {
            ui.displayMainMenu();
            user = UserFactory.getUserForType(input.getUserType());
         }

         if (user != null) {
            user.setOwner(this);
            user = user.startSession();
         }
         else
            running = false;
      }
   }


   public char getResponse(){ return input.getResponse().toLowerCase().charAt(0); }

   public String getStrResponse() { return input.getResponse().toLowerCase(); }

   public void displayMenu(User usr){ ui.displayMenu(usr); }

   public ResultSet executeQuery(String sql) {
      try {
         PreparedStatement ps = conn.prepareStatement(sql);
         return ps.executeQuery();
      } catch (SQLException ex){
         System.out.println("SQL: " + sql);
         System.out.println("SQLException: " + ex.getMessage());
         System.out.println("SQLState: " + ex.getSQLState());
         System.out.println("VendorError: " + ex.getErrorCode());
      }
      return null;
   }

   public void execute(String sql){
      try{
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.execute();
      }catch (SQLException ex){
         System.out.println(ex.getMessage());
         System.out.println("SQL: " + sql);
      }
   }

   public String getDBStatus(){
      String s = "";
      switch(tblStatus){
         case FULL:
            s = "FULL";
            break;
         case EMPTY:
            s = "EMPTY";
            break;
         case NO_DATABASE:
            s = "NO DATABASE";
            break;
      }
      return s;
   }

   public String getNumRooms(){
      if(tblStatus == TableStatus.NO_DATABASE)
         return "Rooms table is missing.";
      ResultSet res = executeQuery("SELECT COUNT(*) FROM rooms");
      try {
         if (res != null && res.next())
            return Integer.toString(res.getInt(1));
      } catch (SQLException e){
         e.printStackTrace();
      }
      return "Rooms table is missing.";
   }

   public String getNumReservations(){
      if(tblStatus == TableStatus.NO_DATABASE)
         return "Reservation table is missing.";
      ResultSet res = executeQuery("SELECT COUNT(*) FROM reservations");
      try {
         if (res != null && res.next())
            return Integer.toString(res.getInt(1));
      } catch (SQLException e){
         e.printStackTrace();
      }
      return "Reservation table is missing.";
   }

   private void checkForInnTables(){
      try {
         PreparedStatement createRooms = conn.prepareStatement(
               "CREATE TABLE IF NOT EXISTS rooms LIKE INN.rooms");
         createRooms.execute();

         PreparedStatement createReservations = conn.prepareStatement(
               "CREATE TABLE IF NOT EXISTS reservations LIKE INN.reservations");
         createReservations.execute();
      } catch (SQLException e){
         System.out.println("SQLException: " + e.getMessage());
         System.out.println("SQLState: " + e.getSQLState());
         System.out.println("VendorError: " + e.getErrorCode());
      }
   }
   
   public void checkTableStatus(){
      checkIfNoDataBase();

      if(tblStatus != TableStatus.NO_DATABASE)
         checkIfEmpty();
   }

   private void checkIfNoDataBase(){
      try{
         PreparedStatement ps = conn.prepareStatement("SELECT  1 FROM rooms LIMIT 1");
         ps.executeQuery();
         ps.executeQuery("SELECT 1 FROM reservations LIMIT 1");
      } catch (SQLException sql){
         this.tblStatus = TableStatus.NO_DATABASE;
      }
   }

   private void checkIfEmpty(){
      ResultSet res;
      try {
         res = executeQuery("SELECT COUNT(*) FROM rooms");
         res.first();
         if(res.getInt(1) == 0) {
            tblStatus = TableStatus.EMPTY;
            return;
         }

         res = executeQuery("SELECT COUNT(*) FROM reservations");
         res.first();
         if(res.getInt(1) == 0) {
            tblStatus = TableStatus.EMPTY;
            return;
         }

         tblStatus = TableStatus.FULL; //might change later to support Partial
      } catch (SQLException e){
         System.out.println(e.getMessage());
      }
   }

   private void loadDriver() throws DriverNotFoundException {
      try {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
      } catch (Exception e){
         throw new DriverNotFoundException();
      }
   }

   public TableStatus getTblStatus(){ return tblStatus; }
   
   
   /*========================================================================*/
   // Functions for input
   
   public char getUserType(){ return input.getUserType(); }

   // Get a date from input
   public String getDate() { return input.getDate(); }

   // Convert month name to month number
   public static int monthNum(String month) { return input.monthNum(month); }
   
   public static String monthName(int month) { return input.monthName(month); }

   public int getNumDates() { return input.getNumDates(); }

   public String getRoomCode() { return input.getRoomCode(); }

   public String getReservationCode() { return input.getReservationCode(); }

   // Revenue and volume data subsystem -- option to continue or quit
   public char revenueData() { return input.revenueData();}

   // potentially useful for Rooms Viewing Subsystem -- gets option to
   // view room code or reservations room code or exit
   public String viewRooms() { return input.viewRooms(); }

   // ask user if they wish to quit
   public char askIfQuit() { return input.askIfQuit(); }

   // ask user if they wish to go back
   public char askIfGoBack() { return askIfGoBack(); }

   // potentially useful for check availability subsystem
   public char availabilityOrGoBack() { return input.availabilityOrGoBack(); }

   // ask if they want to place reservation or renege
   public char reserveOrGoBack() { return input.reserveOrGoBack(); }

   // Get the user's first name (for making a reservation)
   public String getFirstName() { return input.getFirstName(); }

   // Get the user's last name (for making a reservation)
   public String getLastName() { return input.getLastName(); }

   // Get the number of adults for a reservation
   public int getNumAdults() { return input.getNumAdults(); }

   // Get the number of children for a reservation
   public int getNumChildren() { return input.getNumChildren(); }

   // get discount for a room reservation
   public String getDiscount() { return input.getDiscount(); }

}
