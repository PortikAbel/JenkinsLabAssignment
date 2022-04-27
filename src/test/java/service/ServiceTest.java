package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.GradeValidator;
import validation.HomeworkValidator;
import validation.StudentValidator;
import validation.Validator;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private Service service;

    @BeforeEach
    void setUp() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        try {
            FileUtils.copyDirectory(
                    new File("src\\main\\resources"),
                    new File("src\\test\\resources")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "src\\test\\resources\\students.xml");
        HomeworkXMLRepository fileRepository2 = new HomeworkXMLRepository(homeworkValidator, "src\\test\\resources\\homework.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "src\\test\\resources\\grades.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @AfterEach
    void tearDown() {
        try {
            FileUtils.cleanDirectory(new File("src\\test\\resources"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveGradeWithNoExistingStudent() {
        assertNotEquals(1, service.saveGrade("1", "11", 9.5, 2, "ok"));
    }

    @Test
    void saveValidGrade() {
        assertEquals(1, service.saveStudent("1", "Geza", 555));
        assertEquals(1, service.saveHomework("12", "TestHW", 3, 1));
        assertEquals(1, service.saveGrade("1", "12", 9.5, 2, "ok"));
    }

    @Test
    void saveInvalidStudent() {
        assertEquals(-1, service.saveStudent("1", "Abel", 11));
    }

    @Test
    void deleteHomework() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteHomework(null));
    }

    @Test
    void updateStudent() {
        assertEquals(1, service.updateStudent("4", "Abel", 533));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void extendDeadline(String id) {
        assertEquals(0, service.extendDeadline(id, 3));
    }
}