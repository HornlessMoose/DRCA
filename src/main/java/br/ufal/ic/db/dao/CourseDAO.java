package br.ufal.ic.db.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.db.models.Course;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CourseDAO extends AbstractDAO<Course>{
    
    
	public CourseDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
    public Course get(Serializable id) throws HibernateException {
        log.info("getting course: id={}", id);
        return super.get(id);
    }
    
    public List<Course> findAll() throws HibernateException {
        log.info("getting courses");
        return super.list(query("from Course"));
    }

    @Override
    public Course persist(Course entity) throws HibernateException {
        return super.persist(entity);
    }
    
    public void update(Course entity) throws HibernateException {
        super.currentSession().update(entity);
    }

	public Course delete(Course entity) throws HibernateException {
        super.currentSession().delete(entity);
        return entity;
    }
}
