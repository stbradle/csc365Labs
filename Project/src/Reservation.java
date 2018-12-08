import java.sql.ResultSet;

public class Reservation {
   public int Code;
   public String Room;
   public String CheckIn;
   public String CheckOut;
   public int Rate;
   public String LastName;
   public String FirstName;
   public int Adults;
   public int Kids;

   Reservation(){}

   Reservation(ResultSet res){
      try{
         this.Code = res.getInt("Code");
         this.Room = res.getString("Room");
         this.CheckIn = res.getString("CheckIn");
         this.CheckOut = res.getString("CheckOut");
         this.Rate = res.getInt("Rate");
         this.LastName = res.getString("LastName");
         this.FirstName = res.getString("FirstName");
         this.Adults = res.getInt("Adults");
         this.Kids = res.getInt("Kids");
      } catch (Exception e){
         e.printStackTrace();
      }
   }
}
