

import console.*;
import domain.*;
import repository.*;
import service.*;
import validation.*;

public class Main {
    public static void main(String[] args) {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "src\\main\\resources\\students.xml");
        HomeworkXMLRepository fileRepository2 = new HomeworkXMLRepository(homeworkValidator, "src\\main\\resources\\homework.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "src\\main\\resources\\grades.xml");

        Service service = new Service(fileRepository1, fileRepository2, fileRepository3);
        UI console = new UI(service);
        console.run();

    }
}
