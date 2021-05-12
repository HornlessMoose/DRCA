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

public class SecretaryTest {

    private static Validator validator;

    University university = new University("UFAL");
    Department department = new Department("IC", university);

    @BeforeEach
	@SneakyThrows
	public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
    @Test
	public void testDepartment() {
        Secretary s = new Secretary(null, SecretaryType.GRADUATE);

        Set<ConstraintViolation<Secretary>> constraintViolations = validator.validate(s);

        assertEquals("Department can't be null", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testSecretaryType() {
        Secretary s = new Secretary(department, null);

        Set<ConstraintViolation<Secretary>> constraintViolations = validator.validate(s);

        assertEquals("Secretary Type can't be null", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testDepartmentAndSecretaryType() {
        Secretary s = new Secretary();

        Set<ConstraintViolation<Secretary>> constraintViolations = validator.validate(s);
        
         assertThat(constraintViolations).extracting("message")
         .containsOnly("Secretary Type can't be null" , "Department can't be null");
    }
    
}
