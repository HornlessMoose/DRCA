package br.ufal.ic.db.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.db.models.Enrollment;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnrollmentDAO extends AbstractDAO<Enrollment> {
	
	public EnrollmentDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
    public Enrollment get(Serializable id) throws HibernateException {
        log.info("getting enrollment: id={}", id);
        return super.get(id);
    }
    
    public List<Enrollment> findAll() throws HibernateException {
        log.info("getting enrollment");
        return super.list(query("from Enrollment"));
    }
    
    @Override
    public Enrollment persist(Enrollment entity) throws HibernateException {
        return super.persist(entity);
    }

	public void update(Enrollment entity)  throws HibernateException{
		log.info("updatting enrollment: id={}", entity.getId());    		
        super.currentSession().update(entity);
	}

	public Enrollment delete(Enrollment entity)  throws HibernateException{
		log.info("deletting enrollment: id={}", entity.getId());    		
        super.currentSession().delete(entity);

        return entity;
	}
}