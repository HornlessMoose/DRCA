package br.ufal.ic.db.models;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ufal.ic.db.models.Secretary.SecretaryType;
import lombok.SneakyThrows;

public class StudentTest {
    
    private static Validator validator;

    Course course = new Course("CC", SecretaryType.GRADUATE);

    @BeforeEach
	@SneakyThrows
	public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
    @Test
	public void testStudentName() {
        Student s = new Student("", course);

        Set<ConstraintViolation<Student>> constraintViolations = validator.validate(s);

        assertEquals("Person name can't be blank", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void tesCourse() {
        Student s = new Student("Allef", null);

        Set<ConstraintViolation<Student>> constraintViolations = validator.validate(s);

        assertEquals("Course can't be null", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testStudentNameAndCourse() {
        Student s = new Student();

        Set<ConstraintViolation<Student>> constraintViolations = validator.validate(s);
        
         assertThat(constraintViolations).extracting("message")
         .containsOnly("Course can't be null" , "Person name can't be blank");
    }
}
