package br.ufal.ic.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.ufal.ic.api.SecretaryDTO;
import br.ufal.ic.db.dao.CourseDAO;
import br.ufal.ic.db.dao.DepartmentDAO;
import br.ufal.ic.db.dao.SecretaryDAO;
import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Department;
import br.ufal.ic.db.models.Secretary;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

@Path("/secretary")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class SecretaryResource {
    
    DepartmentDAO departmentDAO;
	SecretaryDAO secretaryDAO;
	CourseDAO courseDAO;
	
	
	@GET
    @UnitOfWork
	public Response getAll() {
        
        return Response.ok(secretaryDAO.findAll()).build();
	}
	
	@POST
    @UnitOfWork
    public Response save(SecretaryDTO entity) {
        
         Department department = departmentDAO.get(entity.getDepartment().getId());

         if (department == null) {
		 	return Response.status(Status.NOT_FOUND).entity("Department not found").build();
		 }

         Secretary secretary = new Secretary(department, entity.getSecretaryType());

         secretary.setDepartment(department);
        
        return Response.ok(secretaryDAO.persist(secretary)).build();
	}
	
	@GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {
	        
	    Secretary secretary = secretaryDAO.get(id);

        if(secretary == null){
            return Response.status(Status.NOT_FOUND).entity("Secretary not found").build();
        }

        return Response.ok(secretary).build();
    }
	
	@PUT
    @Path("/{id}")
    @UnitOfWork
    public Response update(@PathParam("id") Long id, SecretaryDTO entity) {
        
        Secretary secretary = secretaryDAO.get(id);

		if (secretary == null) {
			return Response.status(Status.NOT_FOUND).entity("Secretary not found").build();
		}

        Department department = departmentDAO.get(entity.getDepartment().getId());

        if(department == null){
            return Response.status(Status.NOT_FOUND).entity("Department not found").build();
        }

        if(entity.getCourses() != null){
            secretary.setCourses(entity.getCourses());
        }

        secretary.setSecretaryType(entity.getSecretaryType());
        secretary.setDepartment(department);

        return Response.ok(secretaryDAO.update(secretary)).build();
    }

    @PUT
    @Path("/{id}/add_course/{cId}")
    @UnitOfWork
    public Response addCourse(@PathParam("id") Long id, @PathParam("cId") long cId) {

        Secretary secretary = secretaryDAO.get(id);

		if (secretary == null) {
			return Response.status(Status.NOT_FOUND).entity("Secretary not found").build();
		}
        Course course = courseDAO.get(cId);

        if (course == null){
            return Response.status(Status.NOT_FOUND).entity("Course not found").build();
        }

        if(course.getSecretaryType() != secretary.getSecretaryType()){
            return Response.status(Status.FORBIDDEN).entity("this course does not belong to this secretary").build();
        }

        secretary.getCourses().add(course);
        return Response.ok(secretaryDAO.persist(secretary)).build();
    }
	
	@DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {

        Secretary secretary = secretaryDAO.get(id);

        if(secretary == null) {
        	return Response.status(Status.NOT_FOUND).entity("Secretary not found").build();
        }
    
        return Response.ok(secretaryDAO.delete(secretary)).build();
    }
}
