package br.ufal.ic.db.dao;

import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.db.models.Department;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class DepartmentDAO extends AbstractDAO<Department> {
    public DepartmentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Department get(Serializable id) throws HibernateException {
        log.info("getting department: id={}", id);
        return super.get(id);
    }

    public List<Department> findAll() throws HibernateException {
        log.info("getting department");
        return super.list(query("from Department"));
    }

    @Override
    public Department persist(Department entity) throws HibernateException {
        return super.persist(entity);
    }

    public Department update(Department entity)  throws HibernateException{
		log.info("updatting department: id={}", entity.getId());    		
        super.currentSession().update(entity);

        return entity;
	}

    public Department delete(Department entity) throws HibernateException {
        log.info("deletting department: id={}", entity.getId());    		
        super.currentSession().delete(entity);

        return entity;
    }
}