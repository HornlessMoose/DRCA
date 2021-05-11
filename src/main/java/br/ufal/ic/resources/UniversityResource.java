package br.ufal.ic.resources;

import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.ufal.ic.api.UniversityDTO;
import br.ufal.ic.db.dao.UniversityDAO;
import br.ufal.ic.db.models.University;

@Path("/university")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor 
public class UniversityResource {
    UniversityDAO universityDAO;
	
	@GET
	@UnitOfWork
	public Response getAll() {
		
		return Response.ok(universityDAO.findAll()).build();			
	}
	
	@POST
	@UnitOfWork
	public Response save(UniversityDTO entity) {

		University university = new University(entity.getName());
		
		return Response.ok(universityDAO.persist(university)).build();
	}
	
	@GET
	@Path("/{id}")
	@UnitOfWork
	
	public Response getById(@PathParam("id") Long id) {
		
		University university = universityDAO.get(id);
        if (university == null) {  
            return Response.status(Status.NOT_FOUND).entity("University not found").build();
        }
		return Response.ok(universityDAO.get(id)).build();
			
	}
	
	@PUT
	@Path("/{id}")
	@UnitOfWork
	public Response update(@PathParam("id") Long id, UniversityDTO entity) {

		University university = universityDAO.get(id);
        if (university == null) { 
            return Response.status(Status.NOT_FOUND).entity("University not found").build();
        }

        university.setName(entity.getName());
        return Response.ok(universityDAO.update(university)).build();
    
	}
	
	@DELETE
	@Path("/{id}")
	@UnitOfWork
	public Response delete(@PathParam("id") Long id) {
		
		University university = universityDAO.get(id);
        if (university == null) { 
            return Response.status(Status.NOT_FOUND).entity("University not found").build();
        }
		
		return Response.ok(universityDAO.delete(university)).build();	
	}
}
