import java.io.File;
/*
   Griffin Aswegan, Steven Bradley
   gaswegan, stbradle
   CSC 365 Project A UI
*/
public class InnReservations {

   public static void main(String[] args) {
      try{
         File config = new File("ServerSettings.txt");
         ReservationSystem sys = new ReservationSystem(config);
         sys.start();
      } catch (Exception  e){
         e.printStackTrace();
      }
   }
}