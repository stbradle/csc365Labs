import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

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
            student.StLastName = StudentStr.next();
            student.StFirstName = StudentStr.next();
            student.Grade = StudentStr.nextInt();
            student.Classroom = StudentStr.nextInt();
            student.Bus = StudentStr.nextInt();
            student.GPA = StudentStr.nextFloat();
            student.TLastName = StudentStr.next();
            student.TFirstName = StudentStr.next();

            students.add(student);
            StudentStr.close();
        }

        Search(students);
        UserInput.close();
    }

    private static void Search(LinkedList<Student> students){
        String inputStr;
        Scanner UserInput;

        UserInput = new Scanner(System.in);
        inputStr = UserInput.next();

        while(!inputStr.equals("Q") && !inputStr.equals("q")){


            
            if(UserInput.hasNext())
                inputStr = UserInput.next();
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
