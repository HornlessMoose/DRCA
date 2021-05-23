package br.ufal.ic.resources;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.ufal.ic.DRCAApplication;
import br.ufal.ic.DRCAConfiguration;
import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Department;
import br.ufal.ic.db.models.Secretary;
import br.ufal.ic.db.models.University;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class SecretaryResourceTest {

    public static DropwizardAppExtension<DRCAConfiguration> RULE = 
    new DropwizardAppExtension<DRCAConfiguration>(DRCAApplication.class,  
    ResourceHelpers.resourceFilePath("config-test.yml"));

    University u = new University("UFAL");

    University u_saved =  RULE.client().target(
        String.format("http://localhost:%d/%s/university", RULE.getLocalPort(), "academicotest"))
        .request()
        .post(Entity.json(u), University.class);
    
    Department d = new Department("IC", u_saved);

    Department d_saved =  RULE.client().target(
        String.format("http://localhost:%d/%s/department", RULE.getLocalPort(), "academicotest"))
        .request()
        .post(Entity.json(d), Department.class);
    

    @Test
    public void testSave(){

        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);

        Secretary s_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/secretary", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Secretary.class);
        
            assertNotNull(s_saved, "Object is null");
    }

    @Test
    public void testGetAll(){

        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);

        RULE.client().target(
            String.format("http://localhost:%d/%s/secretary", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Secretary.class);

        List<Secretary> s_list =  RULE.client().target(
            String.format("http://localhost:%d/%s/secretary", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<Secretary>>() {});

        assertTrue(s_list.size() > 0);
        
    }

    @Test
    public void testGetById(){

        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);

        Secretary s_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/secretary", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Secretary.class);
        
        Secretary s_getted = RULE.client().target(
            String.format("http://localhost:%d/%s/secretary/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
           .request()
           .get(new GenericType<Secretary>() {});

        assertEquals(s_getted.getId(), s_saved.getId());
    }

    @Test
    public void testUpdate(){
        
        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);

        Secretary s_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/secretary", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Secretary.class);

        s_saved.setSecretaryType(SecretaryType.POSTGRADUATE);

        Secretary s_updated = RULE.client().target(
            String.format("http://localhost:%d/%s/secretary/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
            .request()
            .put(Entity.json(s_saved), Secretary.class);

        assertEquals(SecretaryType.POSTGRADUATE, s_updated.getSecretaryType());
    }

    @Test
    public void testAddCourse(){

        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);

        Secretary s_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/secretary", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Secretary.class);

        Course c = new Course("CC", SecretaryType.GRADUATE);

        Course c_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/course", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(c), Course.class);
        
        Secretary s_add_c = RULE.client().target(
            String.format("http://localhost:%d/%s/secretary/", RULE.getLocalPort(), "academicotest")
             + s_saved.getId() + "/add_course/" + c_saved.getId())
            .request()
            .put(Entity.json(s), Secretary.class);
        
        assertThat(s_add_c.getCourses()).extracting("id").contains(c_saved.getId());
            
    }

    @Test
    public void testDelete(){

        Secretary s = new Secretary(d_saved, SecretaryType.GRADUATE);

        Secretary s_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/secretary", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Secretary.class);

        RULE.client().target(
            String.format("http://localhost:%d/%s/secretary/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
            .request()
            .delete();

        Response r = RULE.client().target(
            String.format("http://localhost:%d/%s/secretary/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
            .request()
            .get();

        assertEquals(404, r.getStatus());

    }
    
}
