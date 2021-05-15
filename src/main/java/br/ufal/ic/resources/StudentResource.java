package br.ufal.ic.resources;

import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.ufal.ic.api.StudentDTO;
import br.ufal.ic.db.dao.CourseDAO;
import br.ufal.ic.db.dao.StudentDAO;
import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Student;

@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class StudentResource {
    StudentDAO studentDAO;
    CourseDAO courseDAO;

    @GET
	@UnitOfWork
	public Response getAll() {

		return Response.ok(studentDAO.findAll()).build();
	}

    @POST
	@UnitOfWork
	public Response save(StudentDTO entity) {
        Course course = courseDAO.get(entity.getCourse().getId());

        if (course == null) {
			return Response.status(Status.NOT_FOUND).entity("Course not found").build();
		}

        Student student = new Student(entity.getName(), course);

		return Response.ok(studentDAO.persist(student)).build();
	}

    @GET
	@Path("/{id}")
	@UnitOfWork
	public Response getById(@PathParam("id") Long id) {
		
		Student student = studentDAO.get(id);
        if (student == null) { 
            return Response.status(Status.NOT_FOUND).entity("Student not found").build();
        }
		
		return Response.ok(student).build();	
	}

    @PUT
    @Path("/{id}")
    @UnitOfWork
    public Response update(@PathParam("id") Long id, StudentDTO entity) {
        
        Student student = studentDAO.get(id);

        if(student == null){
            return Response.status(Status.NOT_FOUND).entity("Student not found").build();
        }

        student.setCredits(entity.getCredits());
        student.setCompletedDisciplines(entity.getCompletedDisciplines());
        
        Course course = courseDAO.get(entity.getCourse().getId());

        if(course == null){
            return Response.status(Status.NOT_FOUND).entity("Course not found").build();
        }

        student.setName(entity.getName());
        student.setCourse(course);

        return Response.ok(studentDAO.persist(student)).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
    
        Student student = studentDAO.get(id);

        if(student == null) {
        	return Response.status(Status.NOT_FOUND).entity("Student not found").build();
        }
    
        return Response.ok(studentDAO.delete(student)).build();
    }


}

    
