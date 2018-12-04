import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;

@SuppressWarnings("SqlNoDataSourceInspection")
public class ReservationSystem {
   private enum TableStatus{EMPTY, FULL, NO_DATABASE, PARTIAL}
   private Connection conn;
   private ReservationSystemUI ui = new ReservationSystemUI();
   private User user;
   private ReservationSystemInput input = new ReservationSystemInput();

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
         ui.displayMainMenu();
         user = UserFactory.getUserForType(input.getUserType());
         if (user != null) {
            user.setOwner(this);
            user.startSession();
         }
         else
            running = false;
      }
   }

   public char getResponse(){
      String[] tokens = input.getResponse().toLowerCase().split(" ");
      return tokens[0].charAt(0);
   }

   public void displayMenu(User usr){ ui.displayMenu(usr); }

   public ResultSet executeQuery(String sql) {
      try {
         PreparedStatement ps = conn.prepareStatement(sql);
         return ps.executeQuery();
      } catch (SQLException ex){
         System.out.println("SQLException: " + ex.getMessage());
         System.out.println("SQLState: " + ex.getSQLState());
         System.out.println("VendorError: " + ex.getErrorCode());
      }
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

   private void checkTableStatus(){

   }


   private void loadDriver() throws DriverNotFoundException {
      try {
         Class.forName("com.mysql.jdbc.Driver").newInstance();
      } catch (Exception e){
         throw new DriverNotFoundException();
      }
   }
}
