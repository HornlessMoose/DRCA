package br.ufal.ic.db.dao;

import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.db.models.Professor;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class ProfessorDAO extends AbstractDAO<Professor> {

    public ProfessorDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Professor> findAll() throws HibernateException {
        return super.list(query("from Professor"));
    }

    @Override
    public Professor get(Serializable id) throws HibernateException {
        log.info("getting professor: id={}", id);

        return super.get(id);
    }

    @Override
    public Professor persist(Professor entity) throws HibernateException {
        return super.persist(entity);
    }

    public void update(Professor entity) throws HibernateException {
		log.info("updatting professor: id={}", entity.getId());    		
        super.currentSession().update(entity);
	}

    public void delete(Professor entity) throws HibernateException {
        super.currentSession().delete(entity);

    }

}