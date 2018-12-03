import java.util.Scanner;

public class ReservationSystemInput{
   private Scanner input = new Scanner(System.in);

   public String getResponse(){
      return input.nextLine();
   }

   public char getUserType(){ return input.nextLine().toLowerCase().charAt(0); }

   // Get a date from input
   private String getDate() {
      String monthName = input.next();
      int month = monthNum(monthName);
      int day = input.nextInt();
      return "'2010-" + month + "-" + day + "'";
   }

   // Convert month name to month number
   private static int monthNum(String month) {
      switch (month) {
         case "january": return 1;
         case "february": return 2;
         case "march": return 3;
         case "april": return 4;
         case "may": return 5;
         case "june": return 6;
         case "july": return 7;
         case "august": return 8;
         case "september": return 9;
         case "october": return 10;
         case "november": return 11;
         case "december": return 12;
      }

      return 0;
   }

   private int getNumDates() {
      System.out.print("Enter number of dates (1 or 2): ");

      int numDates = input.nextInt();
      while (numDates != 1 && numDates != 2) {
         System.out.print("Enter number of dates (1 or 2): ");
         numDates = input.nextInt();
      }
      return numDates;
   }

   private String getRoomCode() {
      System.out.print("Enter room code for more details "
            + "(or (q)uit to exit): ");

      return input.next();
   }


   private String getReservationCode() {
      System.out.print("Enter reservation code for more details "
            + "(or (q)uit to exit): ");
      return input.next();
   }

   // Revenue and volume data subsystem -- option to continue or quit
   private char revenueData() {
      char opt;
      System.out.print("Type (c)ount, (d)ays, or (r)evenue to view "
            + "different table data (or (q)uit to exit): ");
      opt = input.next().toLowerCase().charAt(0);

      return opt;
   }

   // potentially useful for Rooms Viewing Subsystem -- gets option to
   // view room code or reservations room code or exit
   private String viewRooms() {
      System.out.print("Type (v)iew [room code] or "
            + "(r)eservations [room code], or (q)uit to exit: ");

      char option = input.next().toLowerCase().charAt(0);
      String roomCode = String.valueOf(option);
      return option != 'q' ? roomCode + " '" + input.next() + "'" : roomCode;
   }

   // ask user if they wish to quit
   private char askIfQuit() {
      System.out.print("Enter (q)uit to quit: ");
      return input.next().toLowerCase().charAt(0);
   }


   // ask user if they wish to go back
   private char askIfGoBack() {
      System.out.print("Enter (b)ack to go back: ");
      return input.next().toLowerCase().charAt(0);
   }


   // potentially useful for check availability subsystem
   private char availabilityOrGoBack() {
      System.out.print("Enter (a)vailability, or "
            + "(b)ack to go back: ");
      return input.next().toLowerCase().charAt(0);
   }

   // Check availability subsystem:
   // ask if they want to place reservation or renege
   private char reserveOrGoBack() {
      System.out.print("Enter (r)eserve to place a reservation, "
            + "or (b)ack to go back: ");
      return input.next().toLowerCase().charAt(0);
   }

   // Get the user's first name (for making a reservation)
   private String getFirstName() {
      System.out.print("Enter your first name: ");
      return "'" + input.next() + "'";
   }

   // Get the user's last name (for making a reservation)
   private String getLastName() {
      System.out.print("Enter your last name: ");
      return "'" + input.next() + "'";
   }

   // Get the number of adults for a reservation
   private int getNumAdults() {
      System.out.print("Enter number of adults: ");
      return input.nextInt();
   }

   // Get the number of children for a reservation
   private int getNumChildren() {
      System.out.print("Enter number of children: ");
      return input.nextInt();
   }

   // get discount for a room reservation
   private String getDiscount() {
      System.out.print("Enter discount (AAA or AARP, if applicable): ");
      return input.nextLine().toUpperCase();
   }
}
