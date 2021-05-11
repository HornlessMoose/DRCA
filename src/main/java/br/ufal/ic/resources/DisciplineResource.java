package br.ufal.ic.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.ufal.ic.api.DisciplineDTO;
import br.ufal.ic.db.dao.DisciplineDAO;
import br.ufal.ic.db.dao.ProfessorDAO;
import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Professor;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;


@Path("/discipline")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class DisciplineResource {
	DisciplineDAO disciplineDAO;
	ProfessorDAO professorDAO;
	
	@GET
    @UnitOfWork
	public Response getAll() {
        return Response.ok(disciplineDAO.findAll()).build();
	}
	
	@POST
    @UnitOfWork
    public Response save(DisciplineDTO entity) {
        
        Discipline discipline = new Discipline(entity.getName(), entity.getCode(), entity.getDisciplineType(), entity.getSecretaryType(), entity.getProfessor());
        
        if( entity.getCredits() != null ) {
        	discipline.setCredits(entity.getCredits());	
        }

        if( entity.getMin_credits() != null ) {
        	discipline.setMin_credits(entity.getMin_credits());	
        }
        
        discipline.setPre_discipline(entity.getPre_disciplines());
        
        if(entity.getProfessor() != null){
            Professor professor = professorDAO.get(entity.getProfessor().getId());
            discipline.setProfessor(professor);
        }
        
        return Response.ok(disciplineDAO.persist(discipline)).build();
    }
	
	@GET
    @Path("/{id}")
    @UnitOfWork
    public Response getById(@PathParam("id") Long id) {
	    
	    Discipline discipline = disciplineDAO.get(id);

        if (discipline == null) { 
            return Response.status(Status.NOT_FOUND).entity("Discipline not found").build();
        }

        return Response.ok(discipline).build();
    }
	
	@PUT
    @Path("/{id}")
    @UnitOfWork
    public Response update(@PathParam("id") Long id, DisciplineDTO entity) {
        
        Discipline discipline = disciplineDAO.get(id);

        if(discipline == null){
            return Response.status(Status.NOT_FOUND).entity("Discipline not found").build();
        }

        discipline.setCredits(entity.getCredits());	
        discipline.setMin_credits(entity.getMin_credits() );	
        discipline.setPre_discipline(entity.getPre_disciplines());
        
        Professor professor = professorDAO.get(entity.getProfessor().getId());

        if(professor == null){
            return Response.status(Status.NOT_FOUND).entity("Professor not found").build();
        }
        
        discipline.setProfessor( professor );
        
        return Response.ok(disciplineDAO.persist(  discipline )).build();
    }

    @PUT
	@Path("/{id}/add_pre_discipline/{Pid}")
	@UnitOfWork
	public Response addPreDiscipline(@PathParam("id") Long id, @PathParam("Pid") Long pId) {
        Discipline discipline = disciplineDAO.get(id);

		if (discipline == null) {
			return Response.status(Status.NOT_FOUND).entity("Course not found").build();
		}
		Discipline pre_discipline = disciplineDAO.get(pId);

		if (pre_discipline == null) {
			return Response.status(Status.NOT_FOUND).entity("Discipline not found").build();
		}

        if(discipline.getSecretaryType() != pre_discipline.getSecretaryType()) {
			return Response.status(Status.FORBIDDEN).entity("this discipline doesn't belong to this secretary").build();
		}

        discipline.getPre_discipline().add(pre_discipline);

        return Response.ok(disciplineDAO.persist(discipline)).build();
    }
	
	@DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {

        Discipline discipline = disciplineDAO.get(id);

        if(discipline == null){
            return Response.status(Status.NOT_FOUND).entity("Subject not found").build();
        }
        
        return Response.ok(disciplineDAO.delete(discipline)).build();
    }
}