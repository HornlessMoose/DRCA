package br.ufal.ic.resources;

import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.ufal.ic.api.CourseDTO;
import br.ufal.ic.db.dao.CourseDAO;
import br.ufal.ic.db.dao.DisciplineDAO;
import br.ufal.ic.db.dao.SecretaryDAO;
import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Discipline;

@Path("/course")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class CourseResource {
    CourseDAO courseDAO;
	DisciplineDAO disciplineDAO;
    SecretaryDAO secretaryDAO;

    @GET
	@UnitOfWork
	public Response getAll() {
		return Response.ok(courseDAO.findAll()).build();
	}

    @GET
	@Path("/{id}")
	@UnitOfWork
	
	public Response getById(@PathParam("id") Long id) {
		
		Course course = courseDAO.get(id);
        if (course == null) { 
            return Response.status(Status.NOT_FOUND).entity("Course not found").build();
        }
		
		return Response.ok(course).build();	
	}

    @DELETE
	@Path("/{id}")
	@UnitOfWork
	public Response delete(@PathParam("id") Long id) {
		
		Course course = courseDAO.get(id);
        if (course == null) { 
            return Response.status(Status.NOT_FOUND).entity("Course not found").build();
        }
		
		return Response.ok(courseDAO.delete(course)).build();	
	}

    @POST
	@UnitOfWork
	public Response save(CourseDTO entity) {

		Course course = new Course(entity.getName(), entity.getSecretaryType());
		
		return Response.ok(courseDAO.persist(course)).build();
	}

    @PUT
	@Path("/{id}/add_discipline/{Did}")
	@UnitOfWork
	public Response addDiscipline(@PathParam("id") Long id, @PathParam("Did") Long dId) {
		Course course = courseDAO.get(id);

		if (course == null) {
			return Response.status(Status.NOT_FOUND).entity("Course not found").build();
		}
		Discipline discipline = disciplineDAO.get(dId);

		if (discipline == null) {
			return Response.status(Status.NOT_FOUND).entity("Discipline not found").build();
		}

		if(discipline.getSecretaryType() != course.getSecretaryType()) {
			return Response.status(Status.FORBIDDEN).entity("this discipline doesn't belong to this secretary").build();
		}

		course.getDisciplines().add(discipline);
		// disciplineDAO.persist(discipline);
		return Response.ok(courseDAO.persist(course)).build();
	}

	@PUT
    @Path("/{id}")
    @UnitOfWork
    public Response update(@PathParam("id") Long id, CourseDTO entity) {
        
        Course course = courseDAO.get(id);
        
        if(course == null) {

            return Response.status(Status.NOT_FOUND).entity("Course not found").build();
        }

        course.setName(entity.getName());
        course.setSecretaryType(entity.getSecretaryType());
		course.setDisciplines(entity.getDisciplines());
        
        return Response.ok(courseDAO.update(course)).build();
    
    }
}
