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
import br.ufal.ic.db.models.Secretary;
import br.ufal.ic.db.models.Student;
import br.ufal.ic.db.models.Discipline.DisciplineType;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;

@ExtendWith(DropwizardExtensionsSupport.class)
public class DisciplineDAOTest {
    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
    .addEntityClass(Course.class)
    .addEntityClass(Discipline.class)
    .addEntityClass(Student.class)
    .addEntityClass(Professor.class).build();

    private DisciplineDAO daoDiscipline;
    private ProfessorDAO daoProfessor;
    private CourseDAO daoCourse;
    private StudentDAO daoStudent;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("DisciplineDAO - setUp ");
        daoDiscipline = new DisciplineDAO(dbTesting.getSessionFactory());
        daoProfessor = new ProfessorDAO(dbTesting.getSessionFactory());
        daoStudent = new StudentDAO(dbTesting.getSessionFactory());
        daoCourse = new CourseDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCreate() {

        System.out.println("DisciplineDAO - test create");
        
        Professor p = new Professor("Willy");
        Professor p_saved = dbTesting.inTransaction(() -> daoProfessor.persist(p));

        Discipline d = new Discipline("TS","COMP208", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);
        Discipline d_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d));
        
        assertNotNull(d_saved);

        assertAll(
        			() -> assertNotNull(d_saved.getId()),
        			() -> assertNotNull(d_saved.getName()),
                    () -> assertNotNull(d_saved.getCode()),
                    () -> assertNotNull(d_saved.getDisciplineType()),
                    () -> assertNotNull(d_saved.getSecretaryType()),
                    () -> assertNotNull(d_saved.getProfessor())
        		);
        assertAll(
                    () -> assertEquals(d.getId(), d_saved.getId()),
                    () -> assertEquals(d.getName(), d_saved.getName()),
                    () -> assertEquals(d.getCode(), d_saved.getCode()),
                    () -> assertEquals(d.getDisciplineType(), d_saved.getDisciplineType()),
                    () -> assertEquals(d.getSecretaryType(), d_saved.getSecretaryType()),
                    () -> assertEquals(d.getProfessor(), d_saved.getProfessor())
                );
    }
    @Test
    public void testRead() {

        System.out.println("DisciplineDAO - test read");
        
        Professor p = new Professor("Willy");
        Professor p_saved = dbTesting.inTransaction(() -> daoProfessor.persist(p));

        Discipline d = new Discipline("TS","COMP208", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);
        Discipline d_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d));

        Discipline d1 = dbTesting.inTransaction(() -> daoDiscipline.get(d_saved.getId()));

        assertAll(
        			() -> assertEquals(d1.getId(), d_saved.getId()),
        			() -> assertEquals(d1.getName(), d_saved.getName()),
        			() -> assertEquals(d1.getCode(), d_saved.getCode()),
                    () -> assertEquals(d1.getDisciplineType(), d_saved.getDisciplineType()),
                    () -> assertEquals((long) 0, d_saved.getCredits()),
                    () -> assertEquals((long) 0, d_saved.getMin_credits()),
        			() -> assertEquals(d1.getSecretaryType(), d_saved.getSecretaryType())
        		);

        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));
        
        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));

        List<Student> students = new ArrayList<Student>();
        students.add(s_saved);

        Discipline d2 = new Discipline("P4","COMP206", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);
        Discipline d2_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d2));

        List<Discipline> disciplines = new ArrayList<Discipline>();

        disciplines.add(d_saved);
        disciplines.add(d2_saved);

        assertEquals(disciplines, dbTesting.inTransaction(() -> daoDiscipline.findAll()));

        d_saved.setStudents(students);;
        d_saved.setPre_discipline(disciplines);

        assertAll(
        			() -> assertEquals(d_saved.getStudents(), students),
                    () -> assertEquals(d_saved.getPre_discipline(), disciplines)
        		);
    }

    @Test
    public void testUpdate() {
        System.out.println("DisciplineDAO - test update");
        
        Professor p = new Professor("Willy");
        Professor p_saved = dbTesting.inTransaction(() -> daoProfessor.persist(p));

        Discipline d = new Discipline("TS","COMP208", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);
        Discipline d_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d));

        Professor p1 = new Professor("Ylliw");
        Professor p1_saved = dbTesting.inTransaction(() -> daoProfessor.persist(p1));

        Discipline d1 = new Discipline("BD","COMP41", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);
        Discipline d1_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d1));

        Course c = new Course("CC", Secretary.SecretaryType.POSTGRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));

        List<Student> students = new ArrayList<Student>();

        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));

        List<Discipline> pre_disciplines = new ArrayList<Discipline>();

        pre_disciplines.add(d1_saved);
        students.add(s_saved);

        d_saved.setName("Programação 53");
        d_saved.setProfessor(p1_saved);
        d_saved.setCode("COMP514");
        d_saved.setCredits((long) 170);
        d_saved.setMin_credits((long) 5);
        d_saved.setDisciplineType(DisciplineType.ELECTIVE);
        d_saved.setSecretaryType(SecretaryType.POSTGRADUATE);
        d_saved.setId((long) 187);
        d_saved.setStudents(students);
        d_saved.setPre_discipline(pre_disciplines);

        assertAll(
            () -> assertEquals(p1_saved, d_saved.getProfessor()),
            () -> assertEquals("Programação 53", d_saved.getName()),
            () -> assertEquals("COMP514", d_saved.getCode()),
            () -> assertEquals(DisciplineType.ELECTIVE, d_saved.getDisciplineType()),
            () -> assertEquals(SecretaryType.POSTGRADUATE, d_saved.getSecretaryType()),
            () -> assertEquals((long) 170, d_saved.getCredits()),
            () -> assertEquals((long) 187, d_saved.getId()),
            () -> assertEquals(students, d_saved.getStudents()),
            () -> assertEquals(pre_disciplines, d_saved.getPre_discipline()),
            () -> assertEquals((long) 5, d_saved.getMin_credits())
        );
    }
    @Test
    public void testDelete() {
        System.out.println("DisciplineDAO - test delete");
        
        Professor p = new Professor("Willy");
        Professor p_saved = dbTesting.inTransaction(() -> daoProfessor.persist(p));

        Discipline d = new Discipline("TS","COMP208", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);
        Discipline d_saved = dbTesting.inTransaction(() -> daoDiscipline.persist(d));

        dbTesting.inTransaction(() -> daoDiscipline.delete(d_saved));

        assertNull(dbTesting.inTransaction(() -> daoDiscipline.get(d_saved.getId()))); 
    }
    
}
