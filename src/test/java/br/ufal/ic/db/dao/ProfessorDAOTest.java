package br.ufal.ic.db.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.ufal.ic.db.models.Professor;

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

public class ProfessorDAOTest {
    public DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
    .addEntityClass(Professor.class).build();

    private ProfessorDAO dao;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        System.out.println("ProfessorDAO - setUp ");
        dao = new ProfessorDAO(dbTesting.getSessionFactory());
    }

    @Test
    public void testCreate() {

        System.out.println("ProfessorDAO - test create");
        
        Professor p = new Professor("Willy");  
        Professor saved = dbTesting.inTransaction(() -> dao.persist(p));

        assertNotNull(saved);
 
        assertAll(
        			() -> assertNotNull(saved.getId()),
        			() -> assertNotNull(saved.getName())
        		);
        
        
        assertAll(
                    () -> assertEquals(p.getId(), saved.getId()),
                    () -> assertEquals(p.getName(), saved.getName())
        		);
    }
    
    @Test
    public void testRead() {

        System.out.println("ProfessorDAO - test read");
        
        Professor p = new Professor("Willy");
        Professor saved = dbTesting.inTransaction(() -> dao.persist(p));

        Professor p1 = dbTesting.inTransaction(() -> dao.get(saved.getId()));
        
        assertAll(
        			() -> assertEquals(p1.getId(), saved.getId()),
        			() -> assertEquals(p1.getName(), saved.getName())	
        		);
        
        
        Professor p2 = new Professor("Lucas");
        Professor p2_saved = dbTesting.inTransaction(() -> dao.persist(p2));
        
        List<Professor> professors = new ArrayList<Professor>();

        professors.add(p);
        professors.add(p2_saved);

        assertEquals(professors, dbTesting.inTransaction(() -> dao.findAll()));

        
    }

    @Test
    public void testUpdate() {

        System.out.println("ProfessorDAO - test update");
        
        Professor p = new Professor("Willy");
        Professor saved = dbTesting.inTransaction(() -> dao.persist(p));
        
        saved.setName("Lucas");

        assertEquals("Lucas", saved.getName());
        
    }

    @Test
    public void testDelete() {

        System.out.println("ProfessorDAO - test delete");
        
        Professor p = new Professor("Willy");
        
        Professor saved = dbTesting.inTransaction(() -> dao.persist(p));
        dbTesting.inTransaction(() -> dao.delete(saved));

        assertNull(dbTesting.inTransaction(() -> dao.get(saved.getId()))); 
    }
}
