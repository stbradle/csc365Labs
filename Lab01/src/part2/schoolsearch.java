import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.ArrayList;

public class schoolsearch {
    public static void main(String[] args){
        File input;
        Scanner UserInput;
        Scanner StudentStr;
        boolean test = false;
        LinkedList<Student> students;
        LinkedList<Teacher> teachers;

        input = new File("list.txt");

        if (args.length > 0)
           test = true;

        try {
            UserInput = new Scanner(input);
        }
        catch (FileNotFoundException e){
            System.out.println("Bad File");
            return;
        }

        students = GetStudentsList(UserInput);
        UserInput.close();
        if(students == null)
            return;

        input = new File("teachers.txt");
        try {
            UserInput = new Scanner(input);
        }
        catch (FileNotFoundException e){
            System.out.println("Bad filename");
            return;
        }

        teachers = GetTeachersList(UserInput);
        
        if (students == null || teachers == null) {
           return;
        }

        Search(students, teachers, test);
        UserInput.close();
    }

    private static void Search(LinkedList<Student> students, LinkedList<Teacher> teachers, boolean test){
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
              System.out.println("  'G[rade]: <number> <S[tudent] | T[eacher]>' - Searches for all students in the grade or teachers teaching the grade labeled by number");
              System.out.println("  'G[rade]: <number> <H[igh] | L[ow]>' - Searches for all students in the grade labeled by number, reporting only the student with the [H]ighest or [L]owest GPA");
              System.out.println("  'C[lassroom]: <number> <S[tudent] | T[eacher]>' - Searches for all students (S) or teachers (T) in the specified classroom");
              System.out.println("  'B[us]: <number>' - Searches for all students that take the bus route labeled by number");
              System.out.println("  'A[verage]: <number>' - Computes the average GPA of all students in the grade labeled by number");
              System.out.println("  '[G]P[A]: [G[rade] | [T[eacher] | B[us]]' - Separates GPAs by Grade, Teacher, or Bus Route");
              System.out.println("  'I[nfo]' - Dislays the number of students in each grade, sorted in ascending order by grade");
              System.out.println("  'E[nrollment]' - Displays the number of students in each classroom, sorted in ascending order by classroom");
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
                       for (Teacher teacher : teachers)
                          if (teacher.Classroom == current.Classroom)
                             System.out.println("Grade: " + current.Grade + ", Classroom: " + current.Classroom +
                              ", Teacher: " + teacher.TLastName + ", " + teacher.TFirstName);
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
              
              int classroom = -1;
              for (Teacher teacher : teachers) {
                 if (cmd2.equals(teacher.TLastName)) {
                    classroom = teacher.Classroom;
                    break;
                 }
              }
                 
              for (Student current: students) {
                 if (current.Classroom == classroom)
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
              
              if(!token.hasMoreTokens()){
                 System.out.println("Invalid format for 'G[rade]:' - S[tudent], T[eacher], H[igh], L[ow] not specified");
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
                          float minGPA = 10  ;
                          for (Student current : students) {
                              if (current.GPA <= minGPA && current.Grade == currentGrade) {
                                  minGPA = current.GPA;
                                  min = current;
                              }
                          }
                          if(!students.isEmpty())
                             trg = min;
                          break;

                      /*****************************************************************************
                      * Requirement: NR3
                      *
                      * Description: List all teachers for the specified grade.
                      ****************************************************************************/
                      case "T":
                      case "t":
                      case "Teacher":
                      case "teacher":
                         ArrayList<Integer> classrooms = new ArrayList<>();

                         for(Student student: students)
                             if(student.Grade == currentGrade && !classrooms.contains(currentGrade))
                                 classrooms.add(student.Classroom);

                         System.out.format("Grade %d:\n", currentGrade);

                         for(Teacher teacher: teachers)
                             if(classrooms.contains(teacher.Classroom))
                                 System.out.println(teacher.TLastName + ", " + teacher.TFirstName +
                                  ", Classroom: " + teacher.Classroom);
                         break;
                         
                      case "S":
                      case "s":
                      case "Student":
                      case "student":
                         for(Student current: students)
                            if(current.Grade == currentGrade)
                                System.out.println(current.StLastName + ", " + current.StFirstName);
                         break;
                         
                      default:
                          System.out.println("Invalid mode specified for G[rade].");
                          break;
                  }
                  if(trg != null) {
                     for (Teacher teacher : teachers) {
                        if (teacher.Classroom == trg.Classroom)
                           System.out.println(trg.StLastName + ", " + trg.StFirstName + ", GPA: " + trg.GPA +
                            ", Teacher: " + teacher.TLastName + ", " + teacher.TFirstName + ", Bus: " + trg.Bus);
                     }
                  }

              }
              break;
              
           case "C:":
           case "c:":
           case "Classroom:":
           case "classroom:":
              int room;
              if (!token.hasMoreTokens()) {
                 System.out.println("Invalid Format for C[lassroom]: - No classroom number specified");
                 break;
              }
              cmd2 = token.nextToken();
              try {
                 room = Integer.parseInt(cmd2);
              }
              catch (NumberFormatException e) {
                 System.out.println("Invalid first argument for C[lassroom]: - first argument must be an integer");
                 break;
              }
              
              if (!token.hasMoreTokens()) {
                 System.out.println("Invalid Format for C[lassroom]: - S[tudent] or T[eacher] not specified");
                 break;
              }
              cmd3 = token.nextToken();
              
              switch (cmd3) {
             /*****************************************************************************
              * Requirement: NR1
              *
              * Description: List all students for a given classroom number.
              ****************************************************************************/
              case "S":
              case "s":
              case "Student":
              case "student":
                 for (Student current : students)
                    if (current.Classroom == room)
                       System.out.println(current.StLastName + ", " + current.StFirstName);
                 break;

              /*****************************************************************************
               * Requirement: NR2
               *
               * Description: List all teachers that teach in a given classroom number.
               ****************************************************************************/
              case "T":
              case "t":
              case "Teacher":
              case "teacher":
                  for (Teacher current : teachers)
                      if (current.Classroom == room)
                          System.out.println(current.TLastName + ", " + current.TFirstName);
                  break;

                  default:
                 System.out.println("Invalid third argument for C[lassroom]: - third argument must be S[tudent] or T[eacher]");
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
           /*****************************************************************************
           * Requirement: NR4
           *
           * Description: List enrollment numbers for each classroom.
           ****************************************************************************/
           case "E":
           case "e":
           case "Enrollment":
           case "enrollment":
              TreeMap<Integer, Integer> roomCount = new TreeMap<>();
              for (Student current : students) {
                 if (roomCount.containsKey(current.Classroom))
                    roomCount.put(current.Classroom, roomCount.get(current.Classroom) + 1);
                 else
                    roomCount.put(current.Classroom,  1);
              }
              
              for (Map.Entry<Integer, Integer> entry : roomCount.entrySet()) {
                 Integer Classroom = entry.getKey();
                 Integer count = entry.getValue();
                 System.out.println("Classroom " + Classroom + ": " + count + " Students");
              }
              break;
           
           case "Q":
           case "q":
           case "Quit":
           case "quit":
              System.out.println("Exiting...");
              break;

           /*****************************************************************************
           * Requirement: NR5
           *
           * Description: Lists GPAs separated based on Grade, Teacher, or Bus Route.
           ****************************************************************************/
           case "P:":
           case "p:":
           case "GPA:":
           case "gpa:":
               if (!token.hasMoreTokens()) {
                  System.out.println("Invalid format for [G]P[A]: - No second argument specified");
                  break;
               }
               cmd2 = token.nextToken();
               switch (cmd2) {
                   case "G":
                   case "g":
                   case "Grade":
                   case "grade":
                       SortedMap<Integer, ArrayList<Float>> gradeMap = new TreeMap<>();
                       for (Student student : students) {
                           if (!gradeMap.containsKey(student.Grade)) {
                               ArrayList<Float> gpaList = new ArrayList<>();
                               gpaList.add(student.GPA);
                               gradeMap.put(student.Grade, gpaList);
                           }
                           else {
                               gradeMap.get(student.Grade).add(student.GPA);
                           }
                       }
                       for (Map.Entry<Integer, ArrayList<Float>> entry : gradeMap.entrySet()) {
                           grade = entry.getKey();
                           System.out.format("GRADE %d GPAS\n", grade);

                           for (float gpa : entry.getValue())
                               System.out.println(gpa);

                           System.out.println();
                       }
                       break;

                   case "T":
                   case "t":
                   case "Teacher":
                   case "teacher":
                       SortedMap<String, ArrayList<Float>> teacherMap = new TreeMap<>();
                       for (Student student : students) {

                           int curClassroom = student.Classroom;
                           String teacherName = GetTeacherName(teachers, curClassroom);

                           if (!teacherMap.containsKey(teacherName)) {
                               ArrayList<Float> gpaList = new ArrayList<>();
                               gpaList.add(student.GPA);
                               teacherMap.put(teacherName, gpaList);
                           }
                           else {
                               teacherMap.get(teacherName).add(student.GPA);
                           }
                       }
                       for (Map.Entry<String, ArrayList<Float>> entry : teacherMap.entrySet()) {
                           String teacherName = entry.getKey();
                           System.out.format("%s GPAS\n", teacherName);

                           for (float gpa : entry.getValue())
                               System.out.println(gpa);

                           System.out.println();
                       }
                       break;
                   case "B":
                   case "b":
                   case "Bus":
                   case "bus":
                       SortedMap<Integer, ArrayList<Float>> busMap = new TreeMap<>();
                       for (Student student : students) {
                           if (!busMap.containsKey(student.Bus)) {
                               ArrayList<Float> gpaList = new ArrayList<>();
                               gpaList.add(student.GPA);
                               busMap.put(student.Bus, gpaList);
                           }
                           else {
                               busMap.get(student.Bus).add(student.GPA);
                           }
                       }
                       for (Map.Entry<Integer, ArrayList<Float>> entry : busMap.entrySet()) {
                           int bus = entry.getKey();
                           System.out.format("Bus Route %d GPAS\n", bus);

                           for (float gpa : entry.getValue())
                               System.out.println(gpa);

                           System.out.println();
                       }
                       break;
                       
                   default:
                      System.out.println("Invalid second argument for [G]P[A]");
                      break;
               }
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
    }

    private static class Teacher{
        private String TLastName;
        private String TFirstName;
        private int Classroom;
    }

    private static String GetTeacherName(LinkedList<Teacher> teachers, int curClassroom){
        for(Teacher teacher: teachers)
            if(curClassroom == teacher.Classroom)
                return teacher.TLastName + ", " + teacher.TFirstName;
        return null;
    }

    private static LinkedList<Student> GetStudentsList(Scanner UserInput){
        LinkedList<Student> students = new LinkedList<>();
        boolean err = false;

        while(UserInput != null && UserInput.hasNextLine()){
            Scanner StudentStr = new Scanner(UserInput.nextLine());
            StudentStr.useDelimiter("[\\s]*,[\\s]*");

            Student student = new Student();

            if (!StudentStr.hasNext()) {
                System.out.println("Invalid Students File, missing StLastName");
                err = true;
                break;
            }
            student.StLastName = StudentStr.next();

            if (!StudentStr.hasNext()) {
                System.out.println("Invalid Students File, missing StFirstName");
                err = true;
                break;
            }
            student.StFirstName = StudentStr.next();

            if (!StudentStr.hasNextInt()) {
                System.out.println("Invalid Students File, missing Grade");
                err = true;
                break;
            }
            student.Grade = StudentStr.nextInt();

            if (!StudentStr.hasNextInt()) {
                System.out.println("Invalid Students File, missing Classroom");
                err = true;
                break;
            }
            student.Classroom = StudentStr.nextInt();

            if (!StudentStr.hasNextInt()) {
                System.out.println("Invalid Students File, missing Bus");
                err = true;
                break;
            }
            student.Bus = StudentStr.nextInt();

            if (!StudentStr.hasNextFloat()) {
                System.out.println("Invalid Students File, missing GPA");
                err = true;
                break;
            }
            student.GPA = StudentStr.nextFloat();

            students.add(student);
            StudentStr.close();
        }
        if(!err)
            return students;

        return null;
    }

    private static LinkedList<Teacher> GetTeachersList(Scanner UserInput){
        LinkedList<Teacher> teachers = new LinkedList<>();
        boolean err = false;

        while(UserInput != null && UserInput.hasNextLine()){
            Scanner teacherScn = new Scanner(UserInput.nextLine());
            teacherScn.useDelimiter("[\\s]*,[\\s]*");

            Teacher teacher = new Teacher();

            if(!teacherScn.hasNext()){
                System.out.println("Invalid Teachers file, missing TLastName");
                err = true;
                break;
            }
            teacher.TLastName = teacherScn.next().trim();

            if(!teacherScn.hasNext()){
                System.out.println("Invalid Teachers file, missing TFirstName");
                err = true;
                break;
            }
            teacher.TFirstName = teacherScn.next().trim();

            if(!teacherScn.hasNextInt()){
                System.out.println("Invalid Teachers file, missing or invalid value for Classroom.");
                err = true;
                break;
            }
            teacher.Classroom = teacherScn.nextInt();

            teachers.add(teacher);
            teacherScn.close();
        }
        if(!err)
            return teachers;
        return null;
    }
}
