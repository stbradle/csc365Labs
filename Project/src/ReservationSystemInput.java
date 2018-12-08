import java.util.Scanner;

public class ReservationSystemInput{
   public Scanner input = new Scanner(System.in);
   
   public String getResponse(){
      return input.nextLine();
   }

   public char getUserType(){
      String nextLine = input.nextLine().toLowerCase();
      return nextLine.length() > 0 ? nextLine.charAt(0) : '0';
   }

   // Get a date from input
   public String getDate() {
      String monthName = input.next().toLowerCase();
      String dayVal, monthVal;
      int month = monthNum(monthName);
      int day = -1;
      if (input.hasNextInt())
         day = input.nextInt();
      if (month == 0 || day == -1) 
         return "bad";
      input.nextLine();
      if (day >= 10)
         dayVal = Integer.toString(day);
      else
         dayVal = "0" + Integer.toString(day);
      if (month >= 10)
         monthVal = Integer.toString(month);
      else
         monthVal = "0" + Integer.toString(month);
      return "2010-" + monthVal + "-" + dayVal;
   }

   // Convert month name to month number
   public static int monthNum(String month) {
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
         default: return 0;
      }
   }
   
   // Convert month name to month number
   public static String monthName(int month) {
      switch (month) {
         case 1: return "january";
         case 2: return "february";
         case 3: return "march";
         case 4: return "april";
         case 5: return "may";
         case 6: return "june";
         case 7: return "july";
         case 8: return "august";
         case 9: return "september";
         case 10: return "october";
         case 11: return "november";
         case 12: return "december";
      }

      return "bad";
   }

   public int getNumDates() {
      int numDates = -1;
      while (numDates != 1 && numDates != 2) {
         System.out.print("Enter number of dates (1 or 2): ");
         if (input.hasNextInt())
            numDates = input.nextInt();
         input.nextLine();
      }
      return numDates;
   }

   public String getRoomCode() {
      System.out.print("Enter room code for more details "
            + "(or (q)uit to exit): ");

      return input.nextLine();
   }


   public String getReservationCode() {
      System.out.print("Enter reservation code for more details "
            + "(or (q)uit to exit): ");
      return input.nextLine();
   }

   // Revenue and volume data subsystem -- option to continue or quit
   public char revenueData() {
      char opt;
      System.out.print("Type (c)ount, (d)ays, or (r)evenue to view "
            + "different table data (or (q)uit to exit): ");
      opt = input.nextLine().toLowerCase().charAt(0);

      return opt;
   }

   // potentially useful for Rooms Viewing Subsystem -- gets option to
   // view room code or reservations room code or exit
   public String viewRooms() {
      System.out.print("Type (v)iew [room code] or "
            + "(r)eservations [room code], or (q)uit to exit: ");

      return input.nextLine().toLowerCase();
   }

   // ask user if they wish to quit
   public char askIfQuit() {
      System.out.print("Enter (q)uit to quit: ");
      return input.nextLine().toLowerCase().charAt(0);
   }


   // ask user if they wish to go back
   public char askIfGoBack() {
      System.out.print("Enter (b)ack to go back: ");
      return input.nextLine().toLowerCase().charAt(0);
   }


   // potentially useful for check availability subsystem
   public char availabilityOrGoBack() {
      System.out.print("Enter (a)vailability, or "
            + "(b)ack to go back: ");
      return input.nextLine().toLowerCase().charAt(0);
   }

   // Check availability subsystem:
   // ask if they want to place reservation or renege
   public char reserveOrGoBack() {
      System.out.print("Enter (r)eserve to place a reservation, "
            + "(v)iew [reservation code] to view a reservation,"
            + "or (b)ack to go back: ");
      return input.nextLine().toLowerCase().charAt(0);
   }

   // Get the user's first name (for making a reservation)
   public String getFirstName() {
      System.out.print("Enter your first name: ");
      return input.nextLine();
   }

   // Get the user's last name (for making a reservation)
   public String getLastName() {
      System.out.print("Enter your last name: ");
      return input.nextLine();
   }

   // Get the number of adults for a reservation
   public int getNumAdults() {
      System.out.print("Enter number of adults: ");
      int x = input.nextInt();
      input.nextLine();
      return x;
   }

   // Get the number of children for a reservation
   public int getNumChildren() {
      System.out.print("Enter number of children: ");
      int x = input.nextInt();
      input.nextLine();
      return x;
   }

   // get discount for a room reservation
   public String getDiscount() {
      System.out.print("Enter discount ('AAA' or 'AARP', if applicable), otherwise enter 'none': ");
      return input.nextLine().toUpperCase();
   }
}
