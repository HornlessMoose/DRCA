package br.ufal.ic.db.dao;

import java.io.Serializable;
import java.util.List;

import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.db.models.Student;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StudentDAO extends AbstractDAO<Student> {

    public StudentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    @Override
    public Student get(Serializable id) throws HibernateException {
        log.info("getting student: id={}", id);
        return super.get(id);
    }

    public List<Student> findAll() throws HibernateException {
        log.info("getting students");
        return super.list(query("from Student"));
    }
    
    @Override
	public Student persist(Student entity) throws HibernateException {
		return super.persist(entity);
	}

    public void update(Student entity)  throws HibernateException{
		log.info("updatting student: id={}", entity.getId());    		
        super.currentSession().update(entity);
	}

	public Student delete(Student entity)  throws HibernateException{
		log.info("deletting student: id={}", entity.getId());    		
        super.currentSession().delete(entity);
        return entity;
	}
    
}