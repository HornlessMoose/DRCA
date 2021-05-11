package br.ufal.ic.db.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.db.models.University;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UniversityDAO extends AbstractDAO<University> {

	public UniversityDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public University get(Serializable id) throws HibernateException {
		log.info("getting university: id={}", id);
		return super.get(id);
	}

	public List<University> findAll() throws HibernateException {
		log.info("getting universities");
		return super.list(query("from University"));
	}

	@Override
	public University persist(University entity) throws HibernateException {
		return super.persist(entity);
	}

    public University update(University entity) throws HibernateException {
    	log.info("updatting university: id={}", entity.getId());
 
    	super.currentSession().update(entity);

		return entity;
    }
    
    public University delete(University entity) throws HibernateException {
    	log.info("deletting university: id={}", entity.getId());
 
    	super.currentSession().delete(entity);
		return entity;
    }
}