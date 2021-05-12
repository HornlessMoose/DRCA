package br.ufal.ic.db.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.db.models.University;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;

@ExtendWith(DropwizardExtensionsSupport.class)
public class UniversityDAOTest {
    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
    .addEntityClass(University.class).build();

    private UniversityDAO dao;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("UniversityDAO - setUp");
        dao = new UniversityDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCreate() {

        System.out.println("UniversityDAO - test create");
        
        University u = new University("UFAL");
        University u_saved = dbTesting.inTransaction(() -> dao.persist(u));
        
        assertNotNull(u_saved);
        
        assertAll(
        			() -> assertNotNull(u_saved.getId()),
        			() -> assertNotNull(u_saved.getName())
        		);
        
        assertAll(
                    () -> assertEquals(u.getId(), u_saved.getId()),
        			() -> assertEquals(u.getName(), u_saved.getName()),
        			() -> assertNotNull(u.getId())
        		);
    }

    @Test
    public void testRead() {

        System.out.println("UniversityDAO - test read");
        
        University u = new University("UFAL");
        University u_saved = dbTesting.inTransaction(() -> dao.persist(u));

        University u1 = dbTesting.inTransaction(() -> dao.get(u_saved.getId()));
        
        assertAll(
        			() -> assertEquals(u1.getId(), u_saved.getId()),
        			() -> assertEquals("UFAL", u_saved.getName())	
        		);
        
        
        University u2 = new University("LAFU");
        University u2_saved = dbTesting.inTransaction(() -> dao.persist(u2));
        
        assertAll(
    			() -> assertEquals(u2.getId(), u2_saved.getId()),
        		() -> assertEquals("LAFU", u2_saved.getName())	
    		);
        
        List<University> universities = new ArrayList<University>();
        universities.add(u);
        universities.add(u2);
        
        assertEquals(universities, dbTesting.inTransaction(() -> dao.findAll()));
    }

    @Test
    public void testUpdate() {

        System.out.println("UniversityDAO - test update");
        
        University u = new University("UFAL");
        University u_saved = dbTesting.inTransaction(() -> dao.persist(u));

        University u1 = new University("LAFU");
        University u1_saved = dbTesting.inTransaction(() -> dao.persist(u1));
        
        u1_saved.setName("UFAL");
        
        assertEquals("UFAL", u1_saved.getName());

        u_saved.setName("LAFU");

        assertEquals("LAFU", u_saved.getName());
        
    }

    @Test
    public void testDelete() {

        System.out.println("UniversityDAO - test delete");
        
        University u = new University("UFAL");
        University u_saved = dbTesting.inTransaction(() -> dao.persist(u));
        
        dbTesting.inTransaction(() -> dao.delete(u_saved));

        assertNull(dbTesting.inTransaction(() -> dao.get(u_saved.getId()))); 
    }
}
