package br.ufal.ic.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.ufal.ic.db.dao.CourseDAO;
import br.ufal.ic.db.dao.DisciplineDAO;
import br.ufal.ic.db.dao.EnrollmentDAO;
import br.ufal.ic.db.dao.SecretaryDAO;
import br.ufal.ic.db.dao.StudentDAO;
import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Enrollment;
import br.ufal.ic.db.models.Student;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

@Path("/enrollment")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class EnrollmentResource {
    EnrollmentDAO enrollmentDAO;
	CourseDAO courseDAO;
	StudentDAO studentDAO;
	DisciplineDAO disciplineDAO;
    SecretaryDAO secretaryDAO;

    @GET
	@UnitOfWork
	public Response getAll() {
		return Response.ok(enrollmentDAO.findAll()).build();
	}

    
	@GET
	@Path("/{id}")
	@UnitOfWork
	public Response getById(@PathParam("id") Long id) {
		
		Enrollment enrollment = enrollmentDAO.get(id);
        if (enrollment == null) { 
			return Response.status(Status.NOT_FOUND).entity("Enrollment not found").build();
        }
		return Response.ok(enrollmentDAO.get(id)).build();
			
	}

    @POST
	@UnitOfWork
	public Response save(Enrollment enrollment) {

		Student student = studentDAO.get(enrollment.getStudent().getId());

		if(student == null){
			return Response.status(Status.NOT_FOUND).entity("Student not found").build();
		}
        enrollment.setStudent(student);

		return Response.ok(enrollmentDAO.persist(enrollment)).build();
	}


    @POST
	@UnitOfWork
	@Path("/{id}/enroll/{dId}")
	public Response addDiscipline(@PathParam("id") Long id, @PathParam("dId") Long dId) {
		Enrollment enrollment = enrollmentDAO.get(id);
		Discipline discipline = disciplineDAO.get(dId);
		Student student = enrollment.getStudent();

		if (student.getCompletedDisciplines().contains(discipline)) {
			return Response.status(Status.FORBIDDEN).entity("You already completed this discipline").build();
		}

        else if(discipline.getStudents().contains(student)){
            return Response.status(Status.FORBIDDEN).entity("You are already enrolled in this discipline").build();
        }

		else if (student.getCredits() < discipline.getMin_credits()) {
			return Response.status(Status.FORBIDDEN).entity("You don't have credtis enough").build();
		}

		else if(student.getCourse().getSecretaryType() == SecretaryType.POSTGRADUATE &&
		discipline.getSecretaryType() == SecretaryType.GRADUATE) {
			return Response.status(Status.FORBIDDEN).entity("You can't be a graduate student").build();
		}

		else if(student.getCompletedDisciplines().containsAll(discipline.getPre_discipline() ) == false) {
			return Response.status(Status.FORBIDDEN).entity("You don't have the pre requisites disciplines").build();
		}

		else if (student.getCourse().getSecretaryType() == SecretaryType.GRADUATE && 
			discipline.getSecretaryType() == SecretaryType.POSTGRADUATE) {
			if(student.getCredits() < 170) {
				return Response.status(Status.FORBIDDEN).entity("You don't have enough credits(170)").build();
			}
	
		 	discipline.getStudents().add(student);
			return Response.ok(enrollmentDAO.persist(enrollment)).build();
		 }
		
		discipline.getStudents().add(student);
		return Response.ok(enrollmentDAO.persist(enrollment)).build();

	}

}
