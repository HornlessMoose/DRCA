package br.ufal.ic.db.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.SneakyThrows;

public class ProfessorTest {
    
    private static Validator validator;

    @BeforeEach
	@SneakyThrows
	public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
    @Test
	public void testProfessorName() {

        Professor u = new Professor();

        Set<ConstraintViolation<Professor>> constraintViolations = validator.validate(u);

        assertEquals("Person name can't be blank", constraintViolations
        .iterator().next().getMessage());

    }

}
