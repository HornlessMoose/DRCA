package br.ufal.ic.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.ufal.ic.api.ProfessorDTO;
import br.ufal.ic.db.dao.ProfessorDAO;
import br.ufal.ic.db.models.Professor;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;

@Path("/professor")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class ProfessorResource {
    ProfessorDAO professorDAO;
	
	@GET
    @UnitOfWork
	public Response getAll() {

        return Response.ok(professorDAO.findAll()).build();
    }
	
	@POST
    @UnitOfWork
    public Response save(ProfessorDTO entity) {

        Professor professor = new Professor(entity.getName());
    
        return Response.ok(professorDAO.persist(professor)).build();
    }
	
	@GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {
	        
	    Professor professor = professorDAO.get(id);

        return Response.ok(professor).build();
    }
	
	@PUT
    @Path("/{id}")
    @UnitOfWork
    public Response update(@PathParam("id") Long id, ProfessorDTO entity) {   
        Professor professor = professorDAO.get(id);

        if (professor == null) { 
            return Response.status(Status.NOT_FOUND).entity("Professor not found").build();
        }

        professor.setName(entity.getName());

        return Response.ok(professorDAO.persist(professor)).build();
    }
	
	@DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        Professor professor =  professorDAO.get(id);
        
        if (professor == null) { 
            return Response.status(Status.NOT_FOUND).entity("Professor not found").build();
        }

        return Response.ok(professorDAO.delete(professor)).build();
    }
}
