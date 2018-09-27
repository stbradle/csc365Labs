package part2;
import com.sun.prism.shader.Solid_TextureFirstPassLCD_AlphaTest_Loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class schoolsearch {
    public static void main(String[] args){
        File input;
        Scanner UserInput;
        Scanner StudentStr;
        boolean test = false;
        LinkedList<Student> students;
        LinkedList<Teacher> teachers;

        input = new File("part2/list.txt");

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

        input = new File("part2/teachers.txt");
        try {
            UserInput = new Scanner(input);
        }
        catch (FileNotFoundException e){
            System.out.println("Bad filename");
            return;
        }

        teachers = GetTeachersList(UserInput);
        if(teachers == null)
            return;

        Search(students, teachers, test);
        UserInput.close();
    }

    private static void Search(LinkedList<Student> students, LinkedList<Teacher> teachers, boolean test){
        String inputStr = "", cmd1, cmd2;
        Scanner UserInput;

        UserInput = new Scanner(System.in);

        while(!inputStr.equals("Q") && !inputStr.equals("Quit") && !inputStr.equals("q") && !inputStr.equals("quit")) {
            if (!test) {
                System.out.println("\n");
                System.out.println("Commands:");
                System.out.println("  'G[rade]: <number>' - Searches for all teachers that teach the grade signified by number");
                System.out.println("  '[G]P[A]: [G[rade] | [T[eacher] | B[us]]' - Separates GPAs by Grade, Teacher, or Bus Route");
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
            switch (cmd1){
                case "G:":
                case "g:":
                case "Grade:":
                case "grade:":
                    ArrayList<Integer> classrooms = new ArrayList<>();
                    int currentGrade;

                    cmd2 = token.nextToken();
                    try{
                        currentGrade = Integer.valueOf(cmd2);
                    }
                    catch (NumberFormatException e){
                        System.out.println("Invalid value for classroom in G[rade]");
                        break;
                    }

                    for(Student student: students)
                        if(student.Grade == currentGrade && !classrooms.contains(currentGrade))
                            classrooms.add(student.Classroom);

                    System.out.format("Grade %d:\n", currentGrade);

                    for(Teacher teacher: teachers)
                        if(classrooms.contains(teacher.Classroom))
                            System.out.println(teacher.TLastName + ", " + teacher.TFirstName +
                             ", Classroom: " + teacher.Classroom);
                    break;

                case "P:":
                case "p:":
                case "GPA:":
                case "gpa:":
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
                                int grade = entry.getKey();
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
                    }
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
