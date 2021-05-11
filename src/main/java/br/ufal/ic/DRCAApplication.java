package br.ufal.ic;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import br.ufal.ic.db.dao.CourseDAO;
import br.ufal.ic.db.dao.DepartmentDAO;
import br.ufal.ic.db.dao.DisciplineDAO;
import br.ufal.ic.db.dao.EnrollmentDAO;
import br.ufal.ic.db.dao.ProfessorDAO;
import br.ufal.ic.db.dao.SecretaryDAO;
import br.ufal.ic.db.dao.StudentDAO;
import br.ufal.ic.db.dao.UniversityDAO;

import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Department;
import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Enrollment;
import br.ufal.ic.db.models.Professor;
import br.ufal.ic.db.models.Secretary;
import br.ufal.ic.db.models.Student;
import br.ufal.ic.db.models.University;

//import br.ufal.ic.health.TemplateHealthCheck;
import br.ufal.ic.resources.CourseResource;
import br.ufal.ic.resources.DepartmentResource;
import br.ufal.ic.resources.DisciplineResource;
import br.ufal.ic.resources.EnrollmentResource;
import br.ufal.ic.resources.ProfessorResource;
import br.ufal.ic.resources.SecretaryResource;
import br.ufal.ic.resources.StudentResource;
import br.ufal.ic.resources.UniversityResource;



public class DRCAApplication extends Application<DRCAConfiguration> {

    public static void main(String[] args) throws Exception {
        new DRCAApplication().run(args);
    }

    @Override
    public String getName() {
        return "DRCA";
    }

    @Override
    public void initialize(Bootstrap<DRCAConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(DRCAConfiguration configuration, Environment environment) {

        final CourseDAO courseDAO = new CourseDAO(hibernate.getSessionFactory());
        final DepartmentDAO departmentDAO = new DepartmentDAO(hibernate.getSessionFactory());
        final DisciplineDAO disciplineDAO = new DisciplineDAO(hibernate.getSessionFactory());
        final EnrollmentDAO enrollmentDAO = new EnrollmentDAO(hibernate.getSessionFactory());
        final ProfessorDAO professorDAO = new ProfessorDAO(hibernate.getSessionFactory());
        final SecretaryDAO secretaryDAO = new SecretaryDAO(hibernate.getSessionFactory());
        final StudentDAO studentDAO = new StudentDAO(hibernate.getSessionFactory());
        final UniversityDAO universityDAO = new UniversityDAO(hibernate.getSessionFactory());

        final CourseResource course = new CourseResource(courseDAO, disciplineDAO, secretaryDAO);
        final DepartmentResource department = new DepartmentResource(departmentDAO, universityDAO);
        final DisciplineResource discipline = new DisciplineResource(disciplineDAO, professorDAO);
        final EnrollmentResource enrollment = new EnrollmentResource(enrollmentDAO, courseDAO, studentDAO, disciplineDAO, secretaryDAO);
        final ProfessorResource professor = new ProfessorResource(professorDAO);
        final SecretaryResource secretary = new SecretaryResource(departmentDAO, secretaryDAO, courseDAO);
        final StudentResource student = new StudentResource(studentDAO, courseDAO);
        final UniversityResource university = new UniversityResource(universityDAO);

        environment.jersey().register(course);
        environment.jersey().register(department);
        environment.jersey().register(discipline);
        environment.jersey().register(enrollment);
    	environment.jersey().register(professor);
    	environment.jersey().register(secretary);
    	environment.jersey().register(student);
        environment.jersey().register(university);
    	
    }

    private final HibernateBundle<DRCAConfiguration> hibernate
    = new HibernateBundle<DRCAConfiguration>(Course.class, Department.class, Discipline.class, Enrollment.class,
            Professor.class, Secretary.class, Student.class, University.class) {
    	 
    	@Override
         public DataSourceFactory getDataSourceFactory(DRCAConfiguration configuration) {
             return configuration.getDatabase();
         }
     };

}
