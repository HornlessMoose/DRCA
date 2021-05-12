package br.ufal.ic.db.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

public class DepartmentTest {

    private static Validator validator;

    University university = new University("UFAL");

    @BeforeEach
	@SneakyThrows
	public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

    @Test
    public void testDepartmentName() {

        Department d = new Department("", university);

        Set<ConstraintViolation<Department>> constraintViolations = validator.validate(d);

        assertEquals("Department name can't be blank", constraintViolations.iterator().next().getMessage());

    }

    @Test
    public void testUniversity() {
        Department d = new Department("UFAL", null);

        Set<ConstraintViolation<Department>> constraintViolations = validator.validate(d);

        assertEquals("University can't be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testDepartmentNameAndUniversity() {
        Department d = new Department("", null);

        Set<ConstraintViolation<Department>> constraintViolations = validator.validate(d);
        
         assertThat(constraintViolations).extracting("message")
         .containsOnly("Department name can't be blank" , "University can't be null");
    }

}
