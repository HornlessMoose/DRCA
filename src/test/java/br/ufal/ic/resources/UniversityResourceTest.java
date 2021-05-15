package br.ufal.ic.resources;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.ufal.ic.DRCAApplication;
import br.ufal.ic.DRCAConfiguration;
import br.ufal.ic.db.models.University;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class UniversityResourceTest {
    
    public static DropwizardAppExtension<DRCAConfiguration> RULE = 
    new DropwizardAppExtension(DRCAApplication.class,  
    ResourceHelpers.resourceFilePath("config-test.yml"));

    @Test
    public void testSave(){

        University u = new University("UFAL");

        University u_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/university", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(u), University.class);
        
            assertNotNull(u_saved, "Object is null");
    }

    @Test
    public void testGetAll(){

        University u = new University("UFAL");

        RULE.client().target(
            String.format("http://localhost:%d/%s/university", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(u), University.class);

        List<University> u_list =  RULE.client().target(
            String.format("http://localhost:%d/%s/university", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<University>>() {});

        //assertEquals(u_list.size(), 1);
        assertTrue(u_list.size() > 0);
        
    }

    @Test
    public void testGetById(){

        University u = new University("UFAL");

        University u_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/university", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(u), University.class);
        
        University u_getted = RULE.client().target(
            String.format("http://localhost:%d/%s/university/%d", RULE.getLocalPort(), "academicotest", u_saved.getId()))
           .request()
           .get(new GenericType<University>() {});

        assertEquals(u_getted.getId(), u_saved.getId());
    }

    @Test
    public void testUpdate(){
        
        University u = new University("UFAL");

        University u_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/university", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(u), University.class);

        u_saved.setName("LAFU");

        University u_updated = RULE.client().target(
            String.format("http://localhost:%d/%s/university/%d", RULE.getLocalPort(), "academicotest", u_saved.getId()))
            .request()
            .put(Entity.json(u_saved), University.class);

        assertEquals("LAFU", u_updated.getName());
    }

    @Test
    public void testDelete(){

        University u = new University("UFAL");

        University u_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/university", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(u), University.class);

        University u_deleted = RULE.client().target(
            String.format("http://localhost:%d/%s/university/%d", RULE.getLocalPort(), "academicotest", u_saved.getId()))
            .request()
            .delete(new GenericType<University>() {});
        
        assertEquals(u_deleted.getId(), u_saved.getId());

    }
}
