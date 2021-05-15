package br.ufal.ic.resources;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.ufal.ic.DRCAApplication;
import br.ufal.ic.DRCAConfiguration;
import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Professor;
import br.ufal.ic.db.models.Discipline.DisciplineType;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CourseResourceTest {
    
    public static DropwizardAppExtension<DRCAConfiguration> RULE = 
    new DropwizardAppExtension(DRCAApplication.class,  
    ResourceHelpers.resourceFilePath("config-test.yml"));


    @Test
    public void testSave(){

        Course c = new Course("CC", SecretaryType.GRADUATE);

        Course c_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/course", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(c), Course.class);
        
            assertNotNull(c_saved, "Object is null");
    }

    @Test
    public void testGetAll(){

        Course c = new Course("CC", SecretaryType.GRADUATE);

        RULE.client().target(
            String.format("http://localhost:%d/%s/course", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(c), Course.class);

        List<Course> c_list =  RULE.client().target(
            String.format("http://localhost:%d/%s/course", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<Course>>() {});

        assertTrue(c_list.size() > 0);
        
    }

    @Test
    public void testGetById(){

        Course c = new Course("CC", SecretaryType.GRADUATE);

        Course c_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/course", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(c), Course.class);
        
        Course c_getted = RULE.client().target(
            String.format("http://localhost:%d/%s/course/%d", RULE.getLocalPort(), "academicotest", c_saved.getId()))
           .request()
           .get(new GenericType<Course>() {});

        assertEquals(c_getted.getId(), c_saved.getId());
    }

    @Test
    public void testUpdate(){
        
        Course c = new Course("CC", SecretaryType.GRADUATE);

        Course c_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/course", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(c), Course.class);

        c_saved.setName("EC");
        c_saved.setSecretaryType(SecretaryType.POSTGRADUATE);

        Course c_updated = RULE.client().target(
            String.format("http://localhost:%d/%s/course/%d", RULE.getLocalPort(), "academicotest", c_saved.getId()))
            .request()
            .put(Entity.json(c_saved), Course.class);

        assertEquals("EC", c_updated.getName());
        assertEquals(SecretaryType.POSTGRADUATE, c_updated.getSecretaryType());
    }

    @Test
    public void testAddDiscipline(){

        Course c = new Course("CC", SecretaryType.GRADUATE);

        Course c_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/course", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(c), Course.class);

        Professor p = new Professor("Willy");

        Professor p_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/professor", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(p), Professor.class);

        Discipline d = new Discipline("P5", "COMP200", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);

        Discipline d_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Discipline.class);

        Course c_add_d =  RULE.client().target(
            String.format("http://localhost:%d/%s/course/", RULE.getLocalPort(), "academicotest")
             + c_saved.getId() + "/add_discipline/" + d_saved.getId())
            .request()
            .put(Entity.json(c), Course.class);

        assertThat(c_add_d.getDisciplines()).extracting("id").contains(d_saved.getId());
    }

}
