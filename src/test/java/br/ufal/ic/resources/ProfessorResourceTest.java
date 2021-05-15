package br.ufal.ic.resources;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.ufal.ic.DRCAApplication;
import br.ufal.ic.DRCAConfiguration;
import br.ufal.ic.db.models.Professor;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class ProfessorResourceTest {

    public static DropwizardAppExtension<DRCAConfiguration> RULE = 
    new DropwizardAppExtension(DRCAApplication.class,  
    ResourceHelpers.resourceFilePath("config-test.yml"));

    @Test
    public void testSave(){
       
        Professor p = new Professor("Willy");

        Professor p_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/professor", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(p), Professor.class);

        assertNotNull(p_saved, "Object is null");
    }

    @Test
    public void testGetAll(){
        
        Professor p = new Professor("Willy");

        RULE.client().target(
            String.format("http://localhost:%d/%s/professor", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(p), Professor.class);

        List<Professor> p_list =  RULE.client().target(
            String.format("http://localhost:%d/%s/professor", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<Professor>>() {});

        assertTrue(p_list.size() > 0);
    }

    @Test
    public void testGetById(){

        Professor p = new Professor("Willy");

        Professor p_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/professor", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(p), Professor.class);
        
        Professor p_getted = RULE.client().target(
            String.format("http://localhost:%d/%s/professor/%d", RULE.getLocalPort(), "academicotest", p_saved.getId()))
           .request()
           .get(new GenericType<Professor>() {});

        assertEquals(p_getted.getId(), p_saved.getId());
    }

    @Test
    public void testUpdate(){
        
        Professor p = new Professor("Willy");

        Professor p_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/professor", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(p), Professor.class);

        p_saved.setName("Ylliw");

        Professor p_updated = RULE.client().target(
            String.format("http://localhost:%d/%s/professor/%d", RULE.getLocalPort(), "academicotest", p_saved.getId()))
            .request()
            .put(Entity.json(p_saved), Professor.class);


        assertEquals("Ylliw", p_updated.getName());
    }

    @Test
    public void testDelete(){

        Professor p = new Professor("Willy");

        Professor p_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/professor", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(p), Professor.class);

        Professor p_deleted = RULE.client().target(
            String.format("http://localhost:%d/%s/professor/%d", RULE.getLocalPort(), "academicotest", p_saved.getId()))
            .request()
            .delete(new GenericType<Professor>() {});
        
        assertNull(p_deleted);
    }
}
