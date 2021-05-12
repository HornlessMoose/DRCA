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
import br.ufal.ic.db.models.Professor;
import br.ufal.ic.db.models.Secretary;
import br.ufal.ic.db.models.Student;
import br.ufal.ic.db.models.University;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;

@ExtendWith(DropwizardExtensionsSupport.class)
public class SecretaryDAOTest {
    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
    .addEntityClass(University.class)
    .addEntityClass(Department.class)
    .addEntityClass(Secretary.class)
    .addEntityClass(Course.class)
    .addEntityClass(Discipline.class)
    .addEntityClass(Professor.class)
    .addEntityClass(Student.class).build();

    private DepartmentDAO daoDepartment;
    private UniversityDAO daoUniversity;
    private SecretaryDAO daoSecretary;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("SecretaryDAO - setUp ");
        daoUniversity = new UniversityDAO(dbTesting.getSessionFactory());
        daoDepartment = new DepartmentDAO(dbTesting.getSessionFactory());
        daoSecretary = new SecretaryDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCreate() {

        System.out.println("SecretaryDAO - test create");
        
        University u = new University("UFAL");        
        University u_saved = dbTesting.inTransaction(() -> daoUniversity.persist(u));
        
        Department d = new Department("IC", u_saved);
        Department d_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d));
        
        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);
        Secretary s_saved = dbTesting.inTransaction(() -> daoSecretary.persist(s));
        
        assertNotNull(s_saved);
        
        assertAll(
        			() -> assertNotNull(s_saved.getId()),
        			() -> assertNotNull(s_saved.getDepartment()),
        			() -> assertNotNull(s_saved.getSecretaryType())
        		);
        
        assertAll(
                    () -> assertEquals(s.getId(), s_saved.getId()),
        			() -> assertEquals(s.getDepartment(), s_saved.getDepartment()),
                    () -> assertEquals(s.getSecretaryType(), s_saved.getSecretaryType())
        		);
        
    }

    @Test
    public void testRead() {

        System.out.println("SecretaryDAO - test read");
        
        University u = new University("UFAL");        
        University u_saved = dbTesting.inTransaction(() -> daoUniversity.persist(u));
        
        Department d = new Department("IC", u_saved);
        Department d_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d));
        
        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);
        Secretary s_saved = dbTesting.inTransaction(() -> daoSecretary.persist(s));

        Secretary s1 = dbTesting.inTransaction(() -> daoSecretary.get(s_saved.getId()));
             
        assertAll(
        			() -> assertEquals(s1.getId(), s_saved.getId()),
        			() -> assertEquals(s1.getDepartment(), s_saved.getDepartment()),
                    () -> assertEquals(s1.getSecretaryType(), s_saved.getSecretaryType())
        		);
        
        
        Secretary s2 = new Secretary(d_saved, SecretaryType.POSTGRADUATE);
        Secretary s2_saved = dbTesting.inTransaction(() -> daoSecretary.persist(s2));
        
        List<Secretary> secretaries = new ArrayList<Secretary>();
        secretaries.add(s_saved);
        secretaries.add(s2_saved);

        assertEquals(secretaries, dbTesting.inTransaction(() -> daoSecretary.findAll()));
        
        List<Course> courses = new ArrayList<Course>();
    	Course c = new Course("CC", SecretaryType.POSTGRADUATE);
        Course c1 = new Course("EG", SecretaryType.GRADUATE);
    	courses.add(c);
        courses.add(c1);
    	
    	s_saved.setCourses(courses);
    	
        assertEquals(s_saved.getCourses(), courses);
    }

    @Test
    public void testUpdate() {

        System.out.println("SecretaryDAO - test update");
        
        University u = new University("UFAL");        
        University u_saved = dbTesting.inTransaction(() -> daoUniversity.persist(u));
        
        Department d = new Department("IC", u_saved);
        Department d_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d));

        Department d2 = new Department("IM", u_saved);
        Department d2_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d2));
        
        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);
        Secretary s_saved = dbTesting.inTransaction(() -> daoSecretary.persist(s));
    
        s_saved.setDepartment(d2_saved);
        s_saved.setSecretaryType(SecretaryType.POSTGRADUATE); 
        
        assertAll(
        			() -> assertEquals("IM", s_saved.getDepartment().getName()),
                    () -> assertEquals(SecretaryType.POSTGRADUATE, s_saved.getSecretaryType()),
                    () -> assertEquals(d2_saved, s_saved.getDepartment())
        		);
    }

    @Test
    public void testDelete() {

        System.out.println("SecretaryDAO - test delete");
        
        University u = new University("UFAL");        
        University u_saved = dbTesting.inTransaction(() -> daoUniversity.persist(u));
        
        Department d = new Department("IC", u_saved);
        Department d_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d));
        
        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);
        Secretary s_saved = dbTesting.inTransaction(() -> daoSecretary.persist(s));

        dbTesting.inTransaction(() -> daoSecretary.delete(s_saved));

        assertNull(dbTesting.inTransaction(() -> daoSecretary.get(s_saved.getId()))); 
    }

}
