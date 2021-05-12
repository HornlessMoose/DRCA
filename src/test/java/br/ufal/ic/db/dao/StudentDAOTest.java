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
public class StudentDAOTest {
    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
    .addEntityClass(Course.class)
    .addEntityClass(Discipline.class)
    .addEntityClass(Student.class)
    .addEntityClass(Professor.class).build();

    private CourseDAO daoCourse;
    private StudentDAO daoStudent;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("StudentDAO - setUp ");
        daoCourse = new CourseDAO(dbTesting.getSessionFactory());
        daoStudent = new StudentDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCreate() {

        System.out.println("StudentDAO - test create");
        
        Course c = new Course("CC", SecretaryType.GRADUATE);        
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));
        
        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));
        
        assertNotNull(s_saved);
        
        assertAll(
        			() -> assertNotNull(s_saved.getId()),
        			() -> assertNotNull(s_saved.getName()),
                    () -> assertNotNull(s_saved.getCourse()),
                    () -> assertNotNull(s_saved.getCredits()),
                    () -> assertNotNull(s_saved.getCompletedDisciplines())
        		);
        
        assertAll(
                    () -> assertEquals(s.getId(), s_saved.getId()),
        			() -> assertEquals(s.getName(), s_saved.getName()),
                    () -> assertEquals(s.getCourse(), s_saved.getCourse()),
                    () -> assertEquals(s.getCredits(), s_saved.getCredits()),
                    () -> assertEquals(s.getCompletedDisciplines(), s_saved.getCompletedDisciplines())
        		);
    }

    @Test
    public void testRead() {

        System.out.println("StudentDAO - test read");
        
        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));
        
        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));

        Student s1 = dbTesting.inTransaction(() -> daoStudent.get(s_saved.getId()));
             
        assertAll(
        			() -> assertEquals(s1.getId(), s_saved.getId()),
        			() -> assertEquals(s1.getName(), s_saved.getName()),
        			() -> assertEquals(s1.getCourse(), s_saved.getCourse()),
                    () -> assertEquals(s1.getCredits(), s_saved.getCredits()),
                    () -> assertEquals(s1.getCompletedDisciplines(), s_saved.getCompletedDisciplines())
        		);  
        
        List<Student> students = new ArrayList<Student>();
        students.add(s);

        assertEquals(students, dbTesting.inTransaction(() -> daoStudent.findAll()));
        
        Professor p = new Professor("Rodrigo");
        List<Discipline> disciplines = new ArrayList<Discipline>();
        Discipline d = new Discipline("PAA","COMP200", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p);
        Discipline d1 = new Discipline("SO","COMP202", DisciplineType.ELECTIVE, SecretaryType.POSTGRADUATE, p);

    	disciplines.add(d);
        disciplines.add(d1);
    	
    	s_saved.setCompletedDisciplines(disciplines);

        assertEquals(s_saved.getCompletedDisciplines(), disciplines);

    }

    @Test
        public void testUpdate() {
        System.out.println("StudentDAO - test update");
        
        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));
        
        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));
        
        Course c1 = new Course("EC", SecretaryType.POSTGRADUATE);
        Course c1_saved = dbTesting.inTransaction(() -> daoCourse.persist(c1));;

        Discipline d = new Discipline();

        List<Discipline> completedDisciplines = new ArrayList<Discipline>();

        completedDisciplines.add(d);

        s_saved.setCourse(c1_saved);
        s_saved.setName("Fella");
        s_saved.setCredits((long) 13);
        s_saved.setCompletedDisciplines(completedDisciplines);
        
        assertAll(
                    () -> assertEquals(c1_saved, s_saved.getCourse()),
                    () -> assertEquals("Fella", s.getName()),
                    () -> assertEquals(13, s.getCredits()),
                    () -> assertEquals(completedDisciplines, s.getCompletedDisciplines())
                );
    }

    @Test
    public void testDelete() {

        System.out.println("StudentDAO - test delete");
        
        Course c = new Course("CC", SecretaryType.GRADUATE);
        Course c_saved = dbTesting.inTransaction(() -> daoCourse.persist(c));
        
        Student s = new Student("Allef", c_saved);
        Student s_saved = dbTesting.inTransaction(() -> daoStudent.persist(s));

        dbTesting.inTransaction(() -> daoStudent.delete(s_saved));

        assertNull(dbTesting.inTransaction(() -> daoStudent.get(s_saved.getId()))); 
    }
    
}
