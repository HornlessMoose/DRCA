package br.ufal.ic.db.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.db.models.Secretary;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SecretaryDAO extends AbstractDAO<Secretary>{
	public SecretaryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Secretary get(Serializable id) throws HibernateException {
        log.info("getting secretary: id={}", id);
        return super.get(id);
    }
    
    public List<Secretary> findAll() throws HibernateException {
        log.info("getting Secretaries");
        return super.list(query("from Secretary"));
    }
    
    @Override
    public Secretary persist(Secretary entity) throws HibernateException {
        return super.persist(entity);
    }

    public Secretary update(Secretary entity) throws HibernateException {
    	log.info("updatting secretary: id={}", entity.getId());
 
    	super.currentSession().update(entity);

        return entity;
    }
    
    public Secretary delete(Secretary entity) throws HibernateException {
    	log.info("deletting secretary: id={}", entity.getId());
 
    	super.currentSession().delete(entity);

        return entity;
    }
}