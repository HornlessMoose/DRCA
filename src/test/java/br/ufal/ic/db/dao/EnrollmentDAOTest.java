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
import br.ufal.ic.db.models.Department;
import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Enrollment;
import br.ufal.ic.db.models.Professor;
import br.ufal.ic.db.models.Secretary;
import br.ufal.ic.db.models.Student;
import br.ufal.ic.db.models.University;
import br.ufal.ic.db.models.Discipline.DisciplineType;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;

@ExtendWith(DropwizardExtensionsSupport.class)
public class EnrollmentDAOTest {
    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
    .addEntityClass(University.class)
    .addEntityClass(Department.class)
    .addEntityClass(Secretary.class)
    .addEntityClass(Course.class)
    .addEntityClass(Discipline.class)
    .addEntityClass(Professor.class)
    .addEntityClass(Enrollment.class)
    .addEntityClass(Student.class).build();

    private StudentDAO daoStudent;
    private EnrollmentDAO daoEnrollment;
    private CourseDAO daoCourse;
    private DisciplineDAO daoDiscipline;
    private ProfessorDAO daoProfessor;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("EnrollmentDAO - setUp ");
        daoDiscipline = new DisciplineDAO(dbTesting.getSessionFactory());
        daoStudent = new StudentDAO(dbTesting.getSessionFactory());
        daoEnrollment = new EnrollmentDAO(dbTesting.getSessionFactory());
        daoCourse = new CourseDAO(dbTesting.getSessionFactory());
        daoProfessor = new ProfessorDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCreate() {

        System.out.println("EnrollmentDAO - test create");
        
        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));

        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));

        Enrollment e = new Enrollment(s_saved, 1000);
        Enrollment e_saved = dbTesting.inTransaction(() -> daoEnrollment.persist(e));

        assertNotNull(e_saved);

        assertAll(
        			() -> assertNotNull(e_saved.getId()),
        			() -> assertNotNull(e_saved.getNumber()),
                    () -> assertNotNull(e_saved.getStudent())
        		);

        assertAll(
                    () -> assertEquals(e.getId(), e_saved.getId()),
                    () -> assertEquals(e.getNumber(), e_saved.getNumber()),
                    () -> assertEquals(e.getStudent(), e_saved.getStudent())
                );
    }

    @Test
    public void testRead() {
        System.out.println("EnrollmentDAO - test read");

        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));

        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));

        Enrollment e = new Enrollment(s_saved, 1000);
        Enrollment e_saved = dbTesting.inTransaction(() -> daoEnrollment.persist(e));

        Enrollment e1 = dbTesting.inTransaction(() -> daoEnrollment.get(e_saved.getId()));

        assertAll(
                    () -> assertEquals(e1.getId(), e_saved.getId()),
                    () -> assertEquals(e1.getNumber(), e_saved.getNumber()),
                    () -> assertEquals(e1.getStudent(), e_saved.getStudent())
                );

        Enrollment e2 = new Enrollment(s_saved, 1000);
        Enrollment e2_saved = dbTesting.inTransaction(() -> daoEnrollment.persist(e2));

        List<Enrollment> enrollments = new ArrayList<Enrollment>();

        enrollments.add(e_saved);
        enrollments.add(e2_saved);

        assertEquals(enrollments, dbTesting.inTransaction(() -> daoEnrollment.findAll()));

    }

    @Test
    public void testUpdate() {
        System.out.println("EnrollmentDAO - test update");

        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));

        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));

        Enrollment e = new Enrollment(s_saved, 1000);
        Enrollment e_saved = dbTesting.inTransaction(() -> daoEnrollment.persist(e));

        Student s1 = new Student("Fella", c_saved);
        Student s1_saved = dbTesting.inTransaction(() -> daoStudent.persist(s1));

        Enrollment e1 = new Enrollment(s1_saved, 2000);
        Enrollment e1_saved = dbTesting.inTransaction(() -> daoEnrollment.persist(e1));

        Professor p = new Professor("Willy");
        Professor p_saved = dbTesting.inTransaction(() -> daoProfessor.persist(p));

        List<Discipline> currentDisciplines = new ArrayList<Discipline>();
        Discipline d = new Discipline("P4","COMP206", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);
        Discipline d_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d));

        currentDisciplines.add(d_saved);

        e1_saved.setNumber(514);
        e1_saved.setStudent(e_saved.getStudent());
        e1_saved.setCurrentDisciplines(currentDisciplines);

        assertAll(
            () -> assertEquals(514, e1_saved.getNumber()),
            () -> assertEquals(e_saved.getStudent(), e1_saved.getStudent()),
            () -> assertEquals(currentDisciplines, e1_saved.getCurrentDisciplines())
        );
    }

    @Test
    public void testDelete() {
        System.out.println("EnrollmentDAO - test delete");

        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));

        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));

        Enrollment e = new Enrollment(s_saved, 1000);
        Enrollment e_saved = dbTesting.inTransaction(() -> daoEnrollment.persist(e));

        dbTesting.inTransaction(() -> daoEnrollment.delete(e_saved));

        assertNull(dbTesting.inTransaction(() -> daoEnrollment.get(e_saved.getId()))); 
    }
}
