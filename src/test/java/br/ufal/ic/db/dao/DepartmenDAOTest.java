package br.ufal.ic.db.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.ufal.ic.db.models.Department;
import br.ufal.ic.db.models.University;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;

@ExtendWith(DropwizardExtensionsSupport.class)
public class DepartmenDAOTest {
    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
    .addEntityClass(University.class)
    .addEntityClass(Department.class).build();


    private DepartmentDAO daoDepartment;
    private UniversityDAO daoUniversity;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("DepartmentDAO - setUp ");
        daoDepartment = new DepartmentDAO(dbTesting.getSessionFactory());
        daoUniversity = new UniversityDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCreate() {

        System.out.println("DepartmentDAO - test create");
        
        University u = new University("UFAL");        
        University u_saved = dbTesting.inTransaction(() -> daoUniversity.persist(u));
        
        Department d = new Department("IC", u_saved);
        Department d_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d));
        
        assertNotNull(d_saved);
        
        assertAll(
        			() -> assertNotNull(d_saved.getId()),
        			() -> assertNotNull(d_saved.getName()),
                    () -> assertNotNull(d_saved.getUniversity())
        		);
        
        assertAll(
                    () -> assertEquals(d.getId(), d_saved.getId()),
        			() -> assertEquals(d.getName(), d_saved.getName()),
                    () -> assertEquals(d.getUniversity(), d_saved.getUniversity())
        		);
    }

    @Test
    public void testRead() {

        System.out.println("DepartmentDAO - test read");
        
        University u = new University("UFAL");
        University u_saved = dbTesting.inTransaction(() -> daoUniversity.persist(u));

        Department d = new Department("IC", u_saved);
        Department d_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d));

        Department d1 = dbTesting.inTransaction(() -> daoDepartment.get(d_saved.getId()));
             
        assertAll(
        			() -> assertEquals(d1.getId(), d_saved.getId()),
        			() -> assertEquals(d1.getName(), d_saved.getName()),
        			() -> assertEquals(d1.getUniversity(), d_saved.getUniversity())
        		);
        
        
        Department d2 = new Department("IM", u_saved);
        Department d2_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d2));
        
        List<Department> departments = new ArrayList<Department>();
        departments.add(d_saved);
        departments.add(d2_saved);
        
        assertEquals(departments, dbTesting.inTransaction(() -> daoDepartment.findAll()));
    }

    @Test
    public void testUpdate() {

        System.out.println("DepartmentDAO - test update");
        
        University u = new University("UFAL");
        University u_saved = dbTesting.inTransaction(() -> daoUniversity.persist(u));

        Department d = new Department("IC", u_saved);
        Department d_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d));
        
        d_saved.setName("IM");
        d_saved.getUniversity().setName("LAFU");
    
        
        assertAll(
        			() -> assertEquals("LAFU",  d_saved.getUniversity().getName()),
        			() -> assertEquals("IM", d_saved.getName())
        		); 
    }

    @Test
    public void testDelete() {

        System.out.println("DepartmentDAO - test delete");
        
        University u = new University("UFAL");
        University u_saved = dbTesting.inTransaction(() -> daoUniversity.persist(u));

        Department d = new Department("IC", u_saved);
        Department d_saved = dbTesting.inTransaction(() -> daoDepartment.persist(d));

        dbTesting.inTransaction(() -> daoDepartment.delete(d_saved));

        assertNull(dbTesting.inTransaction(() -> daoDepartment.get(d_saved.getId())));
    }


}
