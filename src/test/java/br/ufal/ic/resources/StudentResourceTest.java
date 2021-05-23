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
import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Student;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class StudentResourceTest {

    public static DropwizardAppExtension<DRCAConfiguration> RULE = 
    new DropwizardAppExtension<DRCAConfiguration>(DRCAApplication.class,  
    ResourceHelpers.resourceFilePath("config-test.yml"));

    Course c = new Course("CC", SecretaryType.GRADUATE);

    Course c_saved =  RULE.client().target(
        String.format("http://localhost:%d/%s/course", RULE.getLocalPort(), "academicotest"))
        .request()
        .post(Entity.json(c), Course.class);

    @Test
    public void testSave(){

        Student s = new Student("Allef", c_saved);

        Student s_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/student", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Student.class);
        
            assertNotNull(s_saved, "Object is null");
    }

    @Test
    public void testGetAll(){
        Student s = new Student("Allef", c_saved);

        RULE.client().target(
            String.format("http://localhost:%d/%s/student", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Student.class);

        List<Student> s_list =  RULE.client().target(
            String.format("http://localhost:%d/%s/student", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<Student>>() {});

        assertTrue(s_list.size() > 0);
    }

    @Test
    public void testGetById(){

        Student s = new Student("Allef", c_saved);

        Student s_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/student", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Student.class);
        
        Student s_getted = RULE.client().target(
            String.format("http://localhost:%d/%s/student/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
           .request()
           .get(new GenericType<Student>() {});

        assertEquals(s_getted.getId(), s_saved.getId());
    }

    @Test
    public void testUpdate(){
        
        Student s = new Student("Allef", c_saved);

        Student s_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/student", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Student.class);

        s_saved.setName("Fella");

        Student s_updated = RULE.client().target(
            String.format("http://localhost:%d/%s/student/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
            .request()
            .put(Entity.json(s_saved), Student.class);

        assertEquals("Fella", s_updated.getName());
    }

    @Test
    public void testDelete(){

        Student s = new Student("Allef", c_saved);

        Student s_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/student", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(s), Student.class);

        RULE.client().target(
            String.format("http://localhost:%d/%s/student/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
            .request()
            .delete();
        
        Response r = RULE.client().target(
            String.format("http://localhost:%d/%s/student/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
            .request()
            .get();

        assertEquals(404, r.getStatus());

    }

    
}
