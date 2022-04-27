package service.matcher;

import domain.Student;
import org.mockito.ArgumentMatcher;

public class StudentInvalidGradeMatcher implements ArgumentMatcher<Student> {
    @Override
    public boolean matches(Student student) {
        return student.getGroup() <= 110 || student.getGroup() >= 938;
    }
}
