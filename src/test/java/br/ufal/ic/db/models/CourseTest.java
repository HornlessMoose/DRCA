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

public class CourseTest {
    
    private static Validator validator;

    @BeforeEach
	@SneakyThrows
	public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
    @Test
	public void testCourseName() {
        Course c = new Course("", SecretaryType.GRADUATE);

        Set<ConstraintViolation<Course>> constraintViolations = validator.validate(c);

        assertEquals("Course name can't be blank", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testSecretaryType() {
        Course c = new Course("CC", null);

        Set<ConstraintViolation<Course>> constraintViolations = validator.validate(c);

        assertEquals("Secretary type can't be null", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testCourseNameAndSecretaryType() {
        Course c = new Course();

        Set<ConstraintViolation<Course>> constraintViolations = validator.validate(c);
        
         assertThat(constraintViolations).extracting("message")
         .containsOnly("Secretary type can't be null" , "Course name can't be blank");
    }
}
