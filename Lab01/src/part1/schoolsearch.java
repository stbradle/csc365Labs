package part1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Map;

public class schoolsearch {
    public static void main(String[] args){
        String FileName = "part1/students.txt";
        File input;
        Scanner UserInput;
        Scanner StudentStr;
        LinkedList<Student> students = new LinkedList<>();
        input = new File(FileName);
        boolean test = false;

        if (args.length > 0)
           test = true;

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

        Search(students, test);
        UserInput.close();
    }

    private static void Search(LinkedList<Student> students, boolean test){
        String inputStr = "", cmd1, cmd2;
        Scanner UserInput;

        UserInput = new Scanner(System.in);


        while(!inputStr.equals("Q") && !inputStr.equals("Quit") && !inputStr.equals("q") && !inputStr.equals("quit")){
           if (test == false) {
              System.out.println("\n");
              System.out.println("Commands:");
              System.out.println("  'S[tudent]: <lastname>' - Searches for all students with lastname, displaying last name, first name, grade, and classroom");
              System.out.println("  'S[tudent]: <lastname> B[us]' - Searches for all students with lastname, displaying last name, first name, and taken bus route");
              System.out.println("  'T[eacher]: <lastname>' - Searches for all students with the instructor with lastname");
              System.out.println("  'G[rade]: <number>' - Searches for all students in the grade labeled by number");
              System.out.println("  'G[rade]: <number> H[igh] | L[ow]' - Searches for all students in the grade labeled by number, reporting only the student with the [H]ighest or [L]owest GPA");
              System.out.println("  'C[lassroom]: <number> <T[eacher] | S[tudent]>' - Searches for all Students (S) or Teachers (T) in the specified classroom number");
              System.out.println("  'B[us]: <number>' - Searches for all students that take the bus route labeled by number");
              System.out.println("  'A[verage]: <number>' - Computes the average GPA of all students in the grade labeled by number");
              System.out.println("  'I[nfo]' - Dislays the number of students in each grade, sorted in ascending order by grade");
              System.out.println("  'E[nrollment]' - Displays a list of all classrooms and the number of students in each classroom");
              System.out.println("  'Q[uit]' - Quits the program");
           }

           inputStr = UserInput.nextLine();
           StringTokenizer token = new StringTokenizer(inputStr);
           if (!token.hasMoreTokens()) {
              System.out.println("Invalid command");
              continue;
           }
           
           System.out.println();
           
           cmd1 = token.nextToken();
           switch (cmd1) {
           case "S:":
           case "s:":
           case "Student:":
           case "student:":
              if (!token.hasMoreTokens()) {
                 System.out.println("Invalid Command for 'S[tudent]:' - no last name specified");
                 continue;
              }
              cmd2 = token.nextToken();
              String cmd3 = "";
              if (token.hasMoreTokens()) {
                 cmd3 = token.nextToken();
                 if (!cmd3.equals("B") && !cmd3.equals("Bus") && !cmd3.equals("b") && !cmd3.equals("bus")) {
                    System.out.println("Invalid third argument for 'S[tudent]:' - " + cmd3);
                    continue;
                 }
              }
              cmd2 = cmd2.toUpperCase();
              
              for (Student current: students) {
                 if (current.StLastName.equals(cmd2)) {
                    System.out.print(current.StLastName + ", " + current.StFirstName + ", ");
                    if (!cmd3.equals("")) {
                       System.out.println("Bus: " + current.Bus);
                    }
                    else {
                       System.out.println("Grade: " + current.Grade + ", Classroom: " + current.Classroom +
                        ", Teacher: " + current.TLastName + ", " + current.TFirstName);
                    }
                 }
              }
              break;
              
           case "T:":
           case "t:":
           case "Teacher:":
           case "teacher:":
              if (!token.hasMoreTokens()) {
                 System.out.println("Invalid Command for 'T[eacher]:' - no last name specified");
                 break;
              }
              cmd2 = token.nextToken();
              cmd2 = cmd2.toUpperCase();
              
              for (Student current: students) {
                 if (current.TLastName.equals(cmd2))
                    System.out.println(current.StLastName + ", " + current.StFirstName);
              }
              break;
              
           case "B:":
           case "b:":
           case "Bus:":
           case "bus:":
              if (!token.hasMoreTokens()){
                  System.out.println("Invalid Command for 'B[us]:' - no bus number specified.");
                  break;
              }
              cmd2 = token.nextToken();

              int busNum;
              try {
                  busNum = Integer.valueOf(cmd2);
              }
              catch (NumberFormatException e){
                  System.out.println("Invalid value for B[us] - value must be an integer");
                  break;
              }
              for(Student current: students){
                  if(current.Bus == busNum){
                      System.out.println(current.StLastName + ", " + current.StFirstName + ", Grade: "
                       + current.Grade + ", Classroom: " + current.Classroom);
                  }
              }

              break;
           
           case "G:":
           case "g:":
           case "Grade:":
           case "grade:":
              int currentGrade;
              String mode;
              Student trg = null;

              if(!token.hasMoreTokens()){
                  System.out.println("Invalid Format for 'G[rade]:' - no grade specified");
                  break;
              }

              cmd2 = token.nextToken();
              try {
                  currentGrade = Integer.parseInt(cmd2);
              }
              catch (NumberFormatException e){
                  System.out.println("Invalid value for G[rade]: value must be an integer.");
                  break;
              }
              //Add a check for proper conversion

              if(!token.hasMoreTokens()){
                  for(Student current: students)
                      if(current.Grade == currentGrade)
                          System.out.println(current.StLastName + ", " + current.StFirstName);
              }
              else {
                  mode = token.nextToken();
                  switch (mode) {
                      case "H":
                      case "h":
                      case "High" :
                      case "high" :
                          Student max = null;
                          float maxGPA = 0;
                          for (Student current : students) {
                              if (current.GPA >= maxGPA && current.Grade == currentGrade) {
                                  maxGPA = current.GPA;
                                  max = current;
                              }
                          }
                          if(!students.isEmpty())
                             trg = max;
                          break;

                      case "L":
                      case "l":
                      case "Low":
                      case "low":
                          Student min = null;
                          float minGPA = 10;
                          for (Student current : students) {
                              if (current.GPA <= minGPA && current.Grade == currentGrade) {
                                  minGPA = current.GPA;
                                  min = current;
                              }
                          }
                          if(!students.isEmpty())
                             trg = min;
                          break;

                      default:
                          System.out.println("Invalid mode specified for G[rade].");
                          break;
                  }
                  if(trg != null)
                    System.out.println(trg.StLastName + ", " + trg.StFirstName + ", GPA: " + trg.GPA +
                     ", Teacher: " + trg.TLastName + ", " + trg.TFirstName + ", Bus: " + trg.Bus);
              }
              break;
             
           case "C:":
           case "c:":
           case "Classroom:":
           case "Classroom:":
              int classroom;
              
              if (!token.hasMoreTokens()) {
                 System.out.println("Invalid command for 'C[lassroom]:' - no grade specified");
                 break;
              }
              cmd2 = token.nextToken();
              try {
                 classroom = Integer.parseInt(cmd2);
              }
              catch (NumberFormatException e) {
                 System.out.println("Invalid second argument for 'C[lassroom]:' - argument is not an integer");
                 break;
              }
              
              if (!token.hasMoreTokens()) {
                 System.out.println("Invalid command for 'C[lassroom]:' - Student or Teacher was not specified");
                 break;
              }
              cmd3 = token.nextToken();
              
              switch (cmd3) {
              case "Student":
              case "S":
                 for (Student current : students) 
                    if (current.Classroom == classroom)
                       System.out.println(current.StLastName + ", " + current.StFirstName);
                 break;
                 
              case "Teacher":
              case "T":
                 for (Teacher current : teachers)
                    if (current.Classroom == classroom)
                       System.out.println(current.TLastName + ", " current.TFirstName);
                 break;
                 
              default:
                 System.out.println("Invalid third argument for 'C[lassroom]:'");
                 break;
              }
              
              break;

           case "A:":
           case "a:":
           case "Average:":
           case "average:":
              float totalGPA = 0 , avgGPA = 0;
              int studentCount = 0, grade;

              if (!token.hasMoreTokens()) {
                 System.out.println("Invalid Command for 'A[verage]:' - no grade specified");
                 break;
              }

              cmd2 = token.nextToken();
              try {
                 grade = Integer.parseInt(cmd2);
              }
              catch (NumberFormatException e) {
                 System.out.println("Invalid command for 'A[verage]:' - argument is not an integer");
                 break;
              }

              for (Student current : students) {
                 if (current.Grade == grade) {
                    totalGPA += current.GPA;
                    studentCount++;
                 }
              }
              if (studentCount != 0) {
                 avgGPA = totalGPA / studentCount;
                 System.out.print("Average GPA for Grade " + grade + ": ");
                 System.out.printf("%.2f", avgGPA);
                 System.out.println();
              }
              else
                 System.out.println("No students in Grade " + grade);
              break;
              
           case "I":
           case "i":
           case "Info":
           case "info":
              for (int i = 0; i < 7; i++) {
                 int gradeCount = 0;
                 for(Student current : students) {
                    if (current.Grade == i)
                       gradeCount++;
                 }
                 System.out.println("Grade " + i + ": " + gradeCount);
              }
              break;
              
           case "E":
           case "e":
           case "Enrollment":
           case "enrollment":
              Map<Integer, Integer> roomCount = new Map<>();
              for (Student current : students) {
                 if (!roomCount.containsKey(current.Classroom))
                    roomCount.put(current.Classroom, 1);
                 else 
                    roomCount.put(current.Classroom, roomCount.get(current.Classroom) + 1);
              }
              break;
              
           case "Q":
           case "q":
           case "Quit":
           case "quit":
              System.out.println("Exiting...");
              break;

           default:
              System.out.println("Invalid command - '" + cmd1 + "'");
              break;
           }
        }
    }

    private static class Student{
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
