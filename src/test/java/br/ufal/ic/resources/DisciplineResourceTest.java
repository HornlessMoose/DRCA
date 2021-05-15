package br.ufal.ic.resources;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.ufal.ic.DRCAApplication;
import br.ufal.ic.DRCAConfiguration;
import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Professor;
import br.ufal.ic.db.models.Discipline.DisciplineType;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class DisciplineResourceTest {
    
    public static DropwizardAppExtension<DRCAConfiguration> RULE = 
    new DropwizardAppExtension(DRCAApplication.class,  
    ResourceHelpers.resourceFilePath("config-test.yml"));

    Professor p = new Professor("Willy");

    Professor p_saved =  RULE.client().target(
        String.format("http://localhost:%d/%s/professor", RULE.getLocalPort(), "academicotest"))
        .request()
        .post(Entity.json(p), Professor.class);


    @Test
    public void testSave(){
        Discipline d = new Discipline("P5", "COMP200", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);

        Discipline d_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Discipline.class);

        assertNotNull(d_saved, "Object is null");
    }

    @Test
    public void testGetAll(){
        Discipline d = new Discipline("P5", "COMP200", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);

        RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Discipline.class);

        List<Discipline> d_list =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<Discipline>>() {});

        assertTrue(d_list.size() > 0);
    }

    @Test
    public void testGetById(){

        Discipline d = new Discipline("P5", "COMP200", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);

        Discipline d_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Discipline.class);
        
        Discipline d_getted = RULE.client().target(
            String.format("http://localhost:%d/%s/discipline/%d", RULE.getLocalPort(), "academicotest", d_saved.getId()))
           .request()
           .get(new GenericType<Discipline>() {});

        assertEquals(d_getted.getId(), d_saved.getId());
    }

    @Test
    public void testUpdate(){
        
        Discipline d = new Discipline("P5", "COMP200", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);

        Discipline d_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Discipline.class);

        d_saved.setName("PAA");
        d_saved.setCode("COMP500");
        d_saved.setDisciplineType(DisciplineType.ELECTIVE);
        d_saved.setSecretaryType(SecretaryType.POSTGRADUATE);

        Discipline d_updated = RULE.client().target(
            String.format("http://localhost:%d/%s/discipline/%d", RULE.getLocalPort(), "academicotest", d_saved.getId()))
            .request()
            .put(Entity.json(d_saved), Discipline.class);

        assertAll(
            () -> assertEquals("PAA", d_updated.getName()),
            () -> assertEquals("COMP500", d_updated.getCode()),
            () -> assertEquals(SecretaryType.POSTGRADUATE, d_updated.getSecretaryType()),
            () -> assertEquals(DisciplineType.ELECTIVE, d_updated.getDisciplineType())
        );
    }

    @Test
    public void testAddPreDiscipline(){

        Discipline d = new Discipline("P5", "COMP200", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);

        Discipline d_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Discipline.class);

        Discipline pd = new Discipline("p4", "COMP201", DisciplineType.ELECTIVE, SecretaryType.GRADUATE, p_saved);

        Discipline pd_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(pd), Discipline.class);

        Discipline d_add_pd =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline/", RULE.getLocalPort(), "academicotest")
                + d_saved.getId() + "/add_pre_discipline/" + pd_saved.getId())
            .request()
            .put(Entity.json(d), Discipline.class);

        assertThat(d_add_pd.getPre_discipline()).extracting("id").contains(pd_saved.getId());
    }

    @Test
    public void testDelete(){

        Discipline d = new Discipline("P5", "COMP200", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);

        Discipline d_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(d), Discipline.class);

        Discipline d_deleted = RULE.client().target(
            String.format("http://localhost:%d/%s/discipline/%d", RULE.getLocalPort(), "academicotest", d_saved.getId()))
            .request()
            .delete(new GenericType<Discipline>() {});
        
        assertNull(d_deleted);
    }
}
