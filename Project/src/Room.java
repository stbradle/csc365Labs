import java.sql.ResultSet;
import java.sql.SQLException;

public class Room {
   public String RoomId;
   public String RoomName;
   public int Beds;
   public String BedType;
   public int MaxOcc;
   public int BasePrice;
   public String Decor;

   Room(){}

   Room(ResultSet res){
      try {
         this.RoomId = res.getString("RoomId");
         this.RoomName = res.getString("RoomName");
         this.Beds = res.getInt("Beds");
         this.BedType = res.getString("BedType");
         this.MaxOcc = res.getInt("MaxOcc");
         this.BasePrice = res.getInt("BasePrice");
         this.Decor = res.getString("Decor");
      } catch (SQLException e){
         e.printStackTrace();
      }
   }
}
