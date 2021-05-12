package br.ufal.ic.db.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Professor;
import br.ufal.ic.db.models.Student;
import br.ufal.ic.db.models.Discipline.DisciplineType;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CourseDAOTest {
    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
    .addEntityClass(Course.class)
    .addEntityClass(Discipline.class)
    .addEntityClass(Student.class)
    .addEntityClass(Professor.class).build();

    private DisciplineDAO daoDiscipline;
    private ProfessorDAO daoProfessor;
    private CourseDAO daoCourse;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("CourseDAO - setUp ");
        daoDiscipline = new DisciplineDAO(dbTesting.getSessionFactory());
        daoProfessor = new ProfessorDAO(dbTesting.getSessionFactory());
        daoCourse = new CourseDAO(dbTesting.getSessionFactory());
    }
    

    @Test
    public void testCreate() {

        System.out.println("CourseDAO - test create");

        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));

        assertNotNull(c_saved);

        assertAll(
        			() -> assertNotNull(c_saved.getId()),
        			() -> assertNotNull(c_saved.getName()),
                    () -> assertNotNull(c_saved.getSecretaryType())
        		);

        assertAll(
                    () -> assertEquals(c.getId(), c_saved.getId()),
                    () -> assertEquals(c.getName(), c_saved.getName()),
                    () -> assertEquals(c.getSecretaryType(), c_saved.getSecretaryType())
                );
    }

    @Test
    public void testRead() {
        System.out.println("CourseDAO - test read");

        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));

        Course c1 = dbTesting.inTransaction(() -> daoCourse.get(c_saved.getId()));

        assertAll(
        			() -> assertEquals(c1.getId(), c_saved.getId()),
        			() -> assertEquals(c1.getName(), c_saved.getName()),
                    () -> assertEquals(c1.getSecretaryType(), c_saved.getSecretaryType())
        		);

        Course c2 = new Course("CC", SecretaryType.GRADUATE);
        Course c2_saved = dbTesting.inTransaction(() -> daoCourse.persist(c2));

        List<Course> courses = new ArrayList<Course>();
        courses.add(c_saved);
        courses.add(c2_saved);

        assertEquals(courses, dbTesting.inTransaction(() -> daoCourse.findAll()));

        Professor p = new Professor("Willy");
        Professor p_saved = dbTesting.inTransaction(() -> daoProfessor.persist(p));

        List<Discipline> disciplines = new ArrayList<Discipline>();
        Discipline d1 = new Discipline("P4","COMP206", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);
        Discipline d1_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d1));

        Discipline d2 = new Discipline("P2","COMP358", DisciplineType.ELECTIVE, SecretaryType.POSTGRADUATE, p_saved);
        Discipline d2_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d2));

        disciplines.add(d1_saved);
        disciplines.add(d2_saved);

        c_saved.setDisciplines(disciplines);

        assertEquals(c_saved.getDisciplines(), disciplines);
    }

    @Test
    public void testUpdate() {
        System.out.println("CourseDAO - test update");

        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));

        Course c1 = new Course("EC", SecretaryType.POSTGRADUATE);
        Course c1_saved = dbTesting.inTransaction(() -> daoCourse.persist(c1));

        Professor p = new Professor("Willy");
        Professor p_saved = dbTesting.inTransaction(() -> daoProfessor.persist(p));
        
        List<Discipline> disciplines = new ArrayList<Discipline>();
        Discipline d = new Discipline("P4","COMP206", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);
        Discipline d_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d));

        disciplines.add(d_saved);

        c1_saved.setName(c_saved.getName());
        c1_saved.setSecretaryType(c_saved.getSecretaryType());
        c1_saved.setDisciplines(disciplines);

        assertAll(
            () -> assertEquals("CC", c1_saved.getName()),
            () -> assertEquals(SecretaryType.GRADUATE, c1_saved.getSecretaryType()),
            () -> assertEquals(disciplines, c1_saved.getDisciplines())
        );

    }

    @Test
    public void testDelete() {
        System.out.println("CourseDAO - test delete");

        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));

        dbTesting.inTransaction(() -> daoCourse.delete(c_saved));

        assertNull(dbTesting.inTransaction(() -> daoCourse.get(c_saved.getId()))); 
    }
}
