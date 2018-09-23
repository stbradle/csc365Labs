import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class schoolsearch {
    public static void main(String[] args){
        String FileName = "students.txt";
        File input;
        Scanner UserInput;
        Scanner StudentStr;
        LinkedList<Student> students = new LinkedList<>();
        input = new File(FileName);

        try {
            UserInput = new Scanner(input);
        }
        catch (FileNotFoundException e){
            System.out.println("Bad File");
            return;
        }
        while(UserInput.hasNextLine()){
            StudentStr = new Scanner(UserInput.nextLine());
            StudentStr.useDelimiter(",");

            Student student = new Student();
            
            if (!StudentStr.hasNext()) {
               System.out.println("Invalid CSV File, missing StLastName");
               return;
            }
            student.StLastName = StudentStr.next();
            
            if (!StudentStr.hasNext()) {
               System.out.println("Invalid CSV File, missing StFirstName");
               return;
            }
            student.StFirstName = StudentStr.next();
           
            if (!StudentStr.hasNextInt()) {
               System.out.println("Invalid CSV File, missing Grade");
               return;
            }
            student.Grade = StudentStr.nextInt();
            
            if (!StudentStr.hasNextInt()) {
               System.out.println("Invalid CSV File, missing Classroom");
               return;
            }
            student.Classroom = StudentStr.nextInt();
            
            if (!StudentStr.hasNextInt()) {
               System.out.println("Invalid CSV File, missing Bus");
               return;
            }
            student.Bus = StudentStr.nextInt();
            
            if (!StudentStr.hasNextFloat()) {
               System.out.println("Invalid CSV File, missing GPA");
               return;
            }
            student.GPA = StudentStr.nextFloat();
            
            if (!StudentStr.hasNext()) {
               System.out.println("Invalid CSV File, missing TLastName");
               return;
            }
            student.TLastName = StudentStr.next();
            
            if (!StudentStr.hasNext()) {
               System.out.println("Invalid CSV File, missing TFirstName");
               return;
            }
            student.TFirstName = StudentStr.next();

            students.add(student);
            StudentStr.close();
        }

        Search(students);
        UserInput.close();
    }

    private static void Search(LinkedList<Student> students){
        String inputStr = "", cmd1, cmd2;
        Scanner UserInput;

        UserInput = new Scanner(System.in);


        while(!inputStr.equals("Q") && !inputStr.equals("q")){
           System.out.println("Commands:");
           System.out.println("  'S: <lastname>' - Searches for all students with lastname, displaying last name, first name, grade, and classroom");
           System.out.println("  'S: <lastname> [B]' - Searches for all students with lastname, displaying last name, first name, and taken bus route");
           System.out.println("  'T: <lastname>' - Searches for all students with the instructor with lastname");
           System.out.println("  'G: <number>' - Searches for all students in the grade labeled by number");
           System.out.println("  'G: <number. [H | L]' - Searches for all students in the grade labeled by number, reporting only the student with the [H]ighest or [L]owest GPA");
           System.out.println("  'B: <number>' - Searches for all students that take the bus route labeled by number");
           System.out.println("  'A: <number>' - Computes the average GPA of all students in the grade labeled by number");
           System.out.println("  'I' - Dislays the number of students in each grade, sorted in ascending order by grade");
           System.out.println("  'Q' - Quits the program");

           inputStr = UserInput.nextLine();
           StringTokenizer token = new StringTokenizer(inputStr);
           if (!token.hasMoreTokens()) {
              System.out.println("Invalid command");
              continue;
           }
           
           System.out.println("");
           
           cmd1 = token.nextToken();
           switch (cmd1) {
           case "S:":
           case "s:":
              if (!token.hasMoreTokens()) {
                 System.out.println("Invalid Command for 'S:' - no last name specified");
                 continue;
              }
              cmd2 = token.nextToken();
              String cmd3 = "";
              if (token.hasMoreTokens()) {
                 cmd3 = token.nextToken();
                 if (!cmd3.equals("B") && !cmd3.equals("b")) {
                    System.out.println("Invalid third argument for 'S:' - " + cmd3);
                    continue;
                 }
              }
              cmd2 = cmd2.toUpperCase();
              
              for (int i = 0; i < students.size(); i++) {
                 Student current = students.get(i);
                 if (current.StLastName.equals(cmd2)) {
                    System.out.print(current.StLastName + ", " + current.StFirstName + ", ");
                    if (cmd3 != "") {
                       System.out.println("Bus: " + current.Bus);
                    }
                    else {
                       System.out.println("Grade: " + current.Grade + ", Classroom: " + current.Classroom + ", Teacher: " + current.TLastName + ", " + current.TFirstName);
                    }
                 }
              }
              break;
              
           case "T:":
           case "t:":
              if (!token.hasMoreTokens()) {
                 System.out.println("Invalid Command for 'T:' - no last name specified");
                 continue;
              }
              cmd2 = token.nextToken();
              cmd2 = cmd2.toUpperCase();
              
              for (int i = 0; i < students.size(); i++) {
                 Student current = students.get(i);
                 if (current.TLastName.equals(cmd2))
                    System.out.println(current.StLastName + ", " + current.StFirstName);
              }
              break;
              
           case "B:":
           case "b:":
              System.out.println("B specified!");
              break;
           
           case "G:":
           case "g:":
              System.out.println("G specified!");
              break;
              
           case "A:":
           case "a:":
              System.out.println("A specified!");
              break;
              
           case "I":
           case "i":
              System.out.println("I specified!");
              break;
              
           case "Q":
           case "q":
              System.out.println("Exiting...");
              
           default:
              System.out.println("Invalid command");
              break;
           }
           System.out.println("\n");
        }
    }

    public static class Student{
        private String StLastName;
        private String StFirstName;
        private int Grade;
        private int Classroom;
        private int Bus;
        private float GPA;
        private String TLastName;
        private String TFirstName;
    }
    
    
}
