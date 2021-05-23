package br.ufal.ic.resources;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.ufal.ic.DRCAApplication;
import br.ufal.ic.DRCAConfiguration;
import br.ufal.ic.db.models.Department;
import br.ufal.ic.db.models.University;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class DepartmentResourceTest {
    
    public static DropwizardAppExtension<DRCAConfiguration> RULE = 
    new DropwizardAppExtension<DRCAConfiguration>(DRCAApplication.class,  
    ResourceHelpers.resourceFilePath("config-test.yml"));

    University u = new University("UFAL");

    University u_saved =  RULE.client().target(
        String.format("http://localhost:%d/%s/university", RULE.getLocalPort(), "academicotest"))
        .request()
        .post(Entity.json(u), University.class);

    @Test
    public void testSave(){

        Department d = new Department("IC", u_saved);

        Department d_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/department", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Department.class);
        
            assertNotNull(d_saved, "Object is null");
    }

    @Test
    public void testGetAll(){

        Department d = new Department("IC", u_saved);

        RULE.client().target(
            String.format("http://localhost:%d/%s/department", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Department.class);

        List<Department> d_list =  RULE.client().target(
            String.format("http://localhost:%d/%s/department", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<Department>>() {});

        assertTrue(d_list.size() > 0);
        
    }

    @Test
    public void testGetById(){

        Department d = new Department("IC", u_saved);

        Department d_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/department", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Department.class);
        
        Department d_getted = RULE.client().target(
            String.format("http://localhost:%d/%s/department/%d", RULE.getLocalPort(), "academicotest", d_saved.getId()))
           .request()
           .get(new GenericType<Department>() {});

        assertEquals(d_getted.getId(), d_saved.getId());
    }

    @Test
    public void testUpdate(){
        
        Department d = new Department("IC", u_saved);

        Department d_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/department", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Department.class);

        d_saved.setName("IM");

        Department d_updated = RULE.client().target(
            String.format("http://localhost:%d/%s/department/%d", RULE.getLocalPort(), "academicotest", d_saved.getId()))
            .request()
            .put(Entity.json(d_saved), Department.class);

        assertEquals("IM", d_updated.getName());
    }

    @Test
    public void testDelete(){

        Department d = new Department("IC", u_saved);

        Department d_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/department", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Department.class);

        RULE.client().target(
            String.format("http://localhost:%d/%s/department/%d", RULE.getLocalPort(), "academicotest", d_saved.getId()))
            .request()
            .delete();
        
        Response r = RULE.client().target(
            String.format("http://localhost:%d/%s/secretary/%d", RULE.getLocalPort(), "academicotest", d_saved.getId()))
            .request()
            .get();

        assertEquals(404, r.getStatus());

    }
}
