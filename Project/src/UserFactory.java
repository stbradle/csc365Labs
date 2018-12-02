public class UserFactory {
   public static User getUserForType(char userType){
      switch(userType){
         case 'g': return new Guest();
         case 'o': return new Owner();
         case 'a': return new Admin();
         default: return null;
      }
   }
}
