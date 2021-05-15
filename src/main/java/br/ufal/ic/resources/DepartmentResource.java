package br.ufal.ic.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.ufal.ic.api.DepartmentDTO;
import br.ufal.ic.db.dao.DepartmentDAO;
import br.ufal.ic.db.dao.UniversityDAO;
import br.ufal.ic.db.models.Department;
import br.ufal.ic.db.models.University;

import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;


@Path("/department")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class DepartmentResource {
	DepartmentDAO departmentDAO;
	UniversityDAO universityDAO;
	
	@GET
    @UnitOfWork
	public Response getAll() {
        return Response.ok(departmentDAO.findAll()).build();
	}
	
	 @POST
     @UnitOfWork
     public Response save(DepartmentDTO entity) {
        
         University university = universityDAO.get(entity.getUniversity().getId());

         if(university == null){
            return Response.status(Status.NOT_FOUND).entity("University not found").build();
         }
        
         Department department = new Department(entity.getName(), university);
        
         return Response.ok(departmentDAO.persist(department)).build();
     }
	
	@GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {
	        
	    Department department = departmentDAO.get(id);
	    
        if (department == null) { 
            return Response.status(Status.NOT_FOUND).entity("Department not found").build();
        }
		return Response.ok(departmentDAO.get(id)).build();
        
	}
	
	@PUT
    @Path("/{id}")
    @UnitOfWork
    public Response update(@PathParam("id") Long id, DepartmentDTO entity) {
        
        
        University university = universityDAO.get(entity.getUniversity().getId());

        if(university == null){
            return Response.status(Status.NOT_FOUND).entity("University not found").build();
        }
        
        Department department = departmentDAO.get(id);
        
        if( department == null) {

            return Response.status(Status.NOT_FOUND).entity("Department not found").build();
        }

        department.setName(entity.getName());
        department.setUniversity(university);
        
        return Response.ok(departmentDAO.update(department)).build();
    
    }
	
	@DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {

        Department department = departmentDAO.get(id);
        
        if( department == null) {
        	return Response.status(Status.NOT_FOUND).entity("Department not found").build();
        }

        return Response.ok(departmentDAO.delete(department)).build();
    }
}