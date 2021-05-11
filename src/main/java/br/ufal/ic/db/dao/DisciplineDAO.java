package br.ufal.ic.db.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.db.models.Discipline;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DisciplineDAO extends AbstractDAO<Discipline>{
    
    
	public DisciplineDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
    public Discipline get(Serializable id) throws HibernateException {
        log.info("getting Discipline: id={}", id);
        return super.get(id);
    }
    
    public List<Discipline> findAll() throws HibernateException {
        log.info("getting Disciplines");
        return super.list(query("from Discipline"));
    }

    @Override
    public Discipline persist(Discipline entity) throws HibernateException {
        return super.persist(entity);
    }
    
    public Discipline update(Discipline entity) throws HibernateException {
        super.currentSession().update(entity);

        return entity;
    }

	public Discipline delete(Discipline entity) throws HibernateException {
        super.currentSession().delete(entity);
        return entity;
    }
}
