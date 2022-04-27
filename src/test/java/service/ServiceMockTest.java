package service;

import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import repository.*;
import service.matcher.StudentInvalidGradeMatcher;
import validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceMockTest {
    private Service service;

    @Mock
    StudentXMLRepository studentRepository;
    @Mock
    HomeworkXMLRepository homeworkRepository;
    @Mock
    GradeXMLRepository gradeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new Service(studentRepository, homeworkRepository, gradeRepository);

        Mockito.when(studentRepository.findOne("1"))
                .thenReturn(new Student("1", "Geza", 555));
        Mockito.when(homeworkRepository.findOne("12"))
                .thenReturn(new Homework("12", "TestHW", 3, 1));

        Mockito.when(studentRepository.save(argThat(new StudentInvalidGradeMatcher())))
                .thenThrow(new ValidationException("Group invalid! \n"));

        Mockito.when(homeworkRepository.delete(isNull()))
                .thenThrow(new IllegalArgumentException());
    }


    @Test
    void deleteHomework() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteHomework(null));
        Mockito.verify(homeworkRepository).delete(null);
    }

    @Test
    void saveValidGrade() {
        assertEquals(1, service.saveGrade("1", "12", 9.5, 2, "ok"));
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 10000, 938, 110})
    void saveInvalidStudent(int group) {
        assertEquals(-1, service.saveStudent("1", "Abel", group));
    }
}
