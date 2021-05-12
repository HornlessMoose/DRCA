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

public class EnrollmentTest {
    
     
    private static Validator validator;

    Course course = new Course("CC", SecretaryType.GRADUATE);
    Student student = new Student("Allef", course);

    @BeforeEach
	@SneakyThrows
	public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

    @Test
	public void testStudent() {
        Enrollment e = new Enrollment(null, 1000);

        Set<ConstraintViolation<Enrollment>> constraintViolations = validator.validate(e);

        assertEquals("Student can't be null", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testEnrollmentNumber() {
        Enrollment e = new Enrollment(student, null);

        Set<ConstraintViolation<Enrollment>> constraintViolations = validator.validate(e);

        assertEquals("Enrollment number can't be null", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testEnrollmentNumberAndStudent() {
        Enrollment e = new Enrollment();

        Set<ConstraintViolation<Enrollment>> constraintViolations = validator.validate(e);
        
         assertThat(constraintViolations).extracting("message")
         .containsOnly("Enrollment number can't be null" , "Student can't be null");
    }
}
