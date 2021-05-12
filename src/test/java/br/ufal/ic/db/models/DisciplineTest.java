package br.ufal.ic.db.models;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ufal.ic.db.models.Discipline.DisciplineType;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import lombok.SneakyThrows;

public class DisciplineTest {
    
    private static Validator validator;

    Professor professor = new Professor("Willy");

    @BeforeEach
	@SneakyThrows
	public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

    @Test
	public void testDisciplineName() {
        Discipline d = new Discipline("","COMP200", DisciplineType.MANDATORY,
                                            SecretaryType.GRADUATE, professor);

        Set<ConstraintViolation<Discipline>> constraintViolations = validator.validate(d);

        assertEquals("Discipline name can't be blank", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testDisciplineCode() {
        Discipline d = new Discipline("Teste de software","", DisciplineType.MANDATORY,
                                            SecretaryType.GRADUATE, professor);

        Set<ConstraintViolation<Discipline>> constraintViolations = validator.validate(d);

        assertEquals("Discipline code can't be blank", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testDisciplineType() {
        Discipline d = new Discipline("Teste de software","COMP200", null,
                                    SecretaryType.GRADUATE, professor);

        Set<ConstraintViolation<Discipline>> constraintViolations = validator.validate(d);

        assertEquals("Discipline type can't be null", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testSecretaryType() {
        Discipline d = new Discipline("Teste de software","COMP200", DisciplineType.MANDATORY,
                                            null, professor);

        Set<ConstraintViolation<Discipline>> constraintViolations = validator.validate(d);

        assertEquals("Secretary type can't be null", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
    public void testProfessor() {
        Discipline d = new Discipline("Teste de software","COMP200", DisciplineType.MANDATORY,
                                            SecretaryType.GRADUATE, null);

        Set<ConstraintViolation<Discipline>> constraintViolations = validator.validate(d);

        assertEquals("Professor type can't be null", constraintViolations
        .iterator().next().getMessage());
    }

    @Test
	public void testAll() {
        Discipline d = new Discipline();

        Set<ConstraintViolation<Discipline>> constraintViolations = validator.validate(d);
        
         assertThat(constraintViolations).extracting("message")
         .containsOnly("Professor type can't be null" , "Secretary type can't be null", 
         "Discipline type can't be null", "Discipline code can't be blank", 
         "Discipline name can't be blank");
    }
}
