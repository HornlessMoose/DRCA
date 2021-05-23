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
import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Enrollment;
import br.ufal.ic.db.models.Professor;
import br.ufal.ic.db.models.Student;
import br.ufal.ic.db.models.Discipline.DisciplineType;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class EnrollmentResourceTest {
    
    public static DropwizardAppExtension<DRCAConfiguration> RULE = 
    new DropwizardAppExtension<DRCAConfiguration>(DRCAApplication.class,  
    ResourceHelpers.resourceFilePath("config-test.yml"));

    Course c = new Course("CC", SecretaryType.GRADUATE);

    Course c_saved =  RULE.client().target(
        String.format("http://localhost:%d/%s/course", RULE.getLocalPort(), "academicotest"))
        .request()
        .post(Entity.json(c), Course.class);

    Student s = new Student("Allef", c_saved);

    Student s_saved =  RULE.client().target(
        String.format("http://localhost:%d/%s/student", RULE.getLocalPort(), "academicotest"))
        .request()
        .post(Entity.json(s), Student.class);

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

    @Test
    public void testSave(){
        Enrollment e = new Enrollment(s_saved, 1000);

        Enrollment e_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);

        assertNotNull(e_saved, "Object is null");
    }

    @Test
    public void testGetAll(){
        Enrollment e = new Enrollment(s_saved, 1000);

        RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);

        List<Enrollment> e_list =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<Enrollment>>() {});

        assertTrue(e_list.size() > 0);
    }

    @Test
    public void testGetById(){

        Enrollment e = new Enrollment(s_saved, 1000);

        Enrollment e_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);
        
        Enrollment e_getted = RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment/%d", RULE.getLocalPort(), "academicotest", e_saved.getId()))
           .request()
           .get(new GenericType<Enrollment>() {});

        assertEquals(e_getted.getId(), e_saved.getId());
    }

    @Test
    public void testEnrollDiscipline(){

        Enrollment e = new Enrollment(s_saved, 1000);

        Enrollment e_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);

        Enrollment e_enroll_d =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment/", RULE.getLocalPort(), "academicotest")
            + e_saved.getId() + "/enrollDiscipline/" + d_saved.getId())
            .request()
            .put(Entity.json(e), Enrollment.class);

        assertThat(e_enroll_d.getCurrentDisciplines()).extracting("id").contains(d_saved.getId());
    }

    @Test
    public void testEnrollGraduateStudentToPostgraduate(){

        s_saved.setCredits((long) 180);

        s_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/student/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
            .request()
            .put(Entity.json(s_saved), Student.class);

        Enrollment e = new Enrollment(s_saved, 1000);

        Enrollment e_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);

        d_saved.setSecretaryType(SecretaryType.POSTGRADUATE);

        d_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/discipline/%d", RULE.getLocalPort(), "academicotest", d_saved.getId()))
            .request()
            .put(Entity.json(d_saved), Discipline.class);

        Enrollment e_enroll_d =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment/", RULE.getLocalPort(), "academicotest")
            + e_saved.getId() + "/enrollDiscipline/" + d_saved.getId())
            .request()
            .put(Entity.json(e), Enrollment.class);

        assertThat(e_enroll_d.getCurrentDisciplines()).extracting("id").contains(d_saved.getId());
    }

    @Test
    public void testCreditsUnder170(){

        Enrollment e = new Enrollment(s_saved, 1000);

        Enrollment e_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);

        d_saved.setSecretaryType(SecretaryType.POSTGRADUATE);

        d_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/discipline/%d", RULE.getLocalPort(), "academicotest", d_saved.getId()))
            .request()
            .put(Entity.json(d_saved), Discipline.class);

        Response r =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment/", RULE.getLocalPort(), "academicotest")
            + e_saved.getId() + "/enrollDiscipline/" + d_saved.getId())
            .request()
            .put(Entity.json(e));

            assertEquals(403, r.getStatus());
    }

    @Test
    public void testMissingPreDiscipline(){
        Enrollment e = new Enrollment(s_saved, 1000);

        Enrollment e_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);

        Discipline dp = new Discipline("p4", "COMP210", DisciplineType.MANDATORY, SecretaryType.GRADUATE, p_saved);

        Discipline dp_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/discipline", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(dp), Discipline.class);

        d_saved.getPre_discipline().add(dp_saved);

        d_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/discipline/", RULE.getLocalPort(), "academicotest")
            + d_saved.getId() + "/add_pre_discipline/" + dp_saved.getId())
            .request()
            .put(Entity.json(d_saved), Discipline.class);

        Response r =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment/", RULE.getLocalPort(), "academicotest")
            + e_saved.getId() + "/enrollDiscipline/" + d_saved.getId())
            .request()
            .put(Entity.json(e));

            assertEquals(403, r.getStatus());
            
    }

    @Test
    public void testPostgraduateStudent(){

        c_saved.setSecretaryType(SecretaryType.POSTGRADUATE);

        c_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/course/%d", RULE.getLocalPort(), "academicotest", c_saved.getId()))
            .request()
            .put(Entity.json(c_saved), Course.class);

        Enrollment e = new Enrollment(s_saved, 1000);

        Enrollment e_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);

        Response r =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment/", RULE.getLocalPort(), "academicotest")
            + e_saved.getId() + "/enrollDiscipline/" + d_saved.getId())
            .request()
            .put(Entity.json(e));

        assertEquals(403, r.getStatus());
    }

    @Test
    public void testAlreadyEnrolled(){
        Enrollment e = new Enrollment(s_saved, 1000);

        Enrollment e_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);

        Response r =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment/", RULE.getLocalPort(), "academicotest")
            + e_saved.getId() + "/enrollDiscipline/" + d_saved.getId())
            .request()
            .put(Entity.json(e));

            assertEquals(200, r.getStatus());

        Response r1 =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment/", RULE.getLocalPort(), "academicotest")
            + e_saved.getId() + "/enrollDiscipline/" + d_saved.getId())
            .request()
            .put(Entity.json(e));

            assertEquals(403, r1.getStatus());
    }

    @Test
    public void testCompletedDiscipline(){

        s_saved.getCompletedDisciplines().add(d_saved);

        s_saved = RULE.client().target(
            String.format("http://localhost:%d/%s/student/%d", RULE.getLocalPort(), "academicotest", s_saved.getId()))
            .request()
            .put(Entity.json(s_saved), Student.class);

        Enrollment e = new Enrollment(s_saved, 1000);

        Enrollment e_saved =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(e), Enrollment.class);

        Response r =  RULE.client().target(
            String.format("http://localhost:%d/%s/enrollment/", RULE.getLocalPort(), "academicotest")
            + e_saved.getId() + "/enrollDiscipline/" + d_saved.getId())
            .request()
            .put(Entity.json(e));

        assertEquals(403, r.getStatus());
    }

}
