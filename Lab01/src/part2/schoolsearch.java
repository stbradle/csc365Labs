package part2;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class schoolsearch {
    public static void main(String[] args){
        File input;
        Scanner UserInput;
        Scanner StudentStr;
        boolean test = false;

        LinkedList<Student> students = new LinkedList<>();
        LinkedList<Teacher> teachers = new LinkedList<>();
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

        input = new File("teachers.txt");
        try {
            UserInput = new Scanner(input);
        }
        catch (FileNotFoundException e){
            System.out.println("Bad filename");
            return;
        }

        teachers = GetTeachersList(UserInput);

        System.out.println(teachers);
        System.out.println(students);

        //Search(students, teachers, test);
        UserInput.close();
    }

    private static void Search(LinkedList<Student> students, LinkedList<Teacher> teachers, boolean test){
        String inputStr = "", cmd1, cmd2;
        Scanner UserInput;

        UserInput = new Scanner(System.in);


        while(!inputStr.equals("Q") && !inputStr.equals("Quit") && !inputStr.equals("q") && !inputStr.equals("quit")) {
            if (test == false) {
                System.out.println("\n");
                System.out.println("Commands:");
                System.out.println("  'S[tudent]: <lastname>' - Searches for all students with lastname, displaying last name, first name, grade, and classroom");
                System.out.println("  'S[tudent]: <lastname> B[us]' - Searches for all students with lastname, displaying last name, first name, and taken bus route");
                System.out.println("  'T[eacher]: <lastname>' - Searches for all students with the instructor with lastname");
                System.out.println("  'G[rade]: <number>' - Searches for all students in the grade labeled by number");
                System.out.println("  'G[rade]: <number. H[igh] | L[ow]' - Searches for all students in the grade labeled by number, reporting only the student with the [H]ighest or [L]owest GPA");
                System.out.println("  'B[us]: <number>' - Searches for all students that take the bus route labeled by number");
                System.out.println("  'A[verage]: <number>' - Computes the average GPA of all students in the grade labeled by number");
                System.out.println("  'I[nfo]' - Dislays the number of students in each grade, sorted in ascending order by grade");
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
            //add switch statement for search
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

    private static LinkedList<Student> GetStudentsList(Scanner UserInput){
        LinkedList<Student> students = new LinkedList<>();
        boolean err = false;

        while(UserInput != null && UserInput.hasNextLine()){
            Scanner StudentStr = new Scanner(UserInput.nextLine());
            StudentStr.useDelimiter(",");

            Student student = new Student();

            if (!StudentStr.hasNext()) {
                System.out.println("Invalid CSV File, missing StLastName");
                err = true;
                break;
            }
            student.StLastName = StudentStr.next();

            if (!StudentStr.hasNext()) {
                System.out.println("Invalid CSV File, missing StFirstName");
                err = true;

                break;
            }
            student.StFirstName = StudentStr.next();

            if (!StudentStr.hasNextInt()) {
                System.out.println("Invalid CSV File, missing Grade");
                err = true;
                break;
            }
            student.Grade = StudentStr.nextInt();

            if (!StudentStr.hasNextInt()) {
                System.out.println("Invalid CSV File, missing Classroom");
                err = true;
                break;
            }
            student.Classroom = StudentStr.nextInt();

            if (!StudentStr.hasNextInt()) {
                System.out.println("Invalid CSV File, missing Bus");
                err = true;
                break;
            }
            student.Bus = StudentStr.nextInt();

            if (!StudentStr.hasNextFloat()) {
                System.out.println("Invalid CSV File, missing GPA");
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
            teacherScn.useDelimiter(",");

            Teacher teacher = new Teacher();

            if(!teacherScn.hasNext()){
                System.out.println("Invalid CSV file, missing TLastName");
                err = true;
                break;
            }
            teacher.TLastName = teacherScn.next();

            if(!teacherScn.hasNext()){
                System.out.println("Invalid CSV file, missing TFirstName");
                err = true;
                break;
            }
            teacher.TFirstName = teacherScn.next();

            if(!teacherScn.hasNextInt()){
                System.out.println("Invalid CSV file, missing or invalid value for Classroom.");
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
