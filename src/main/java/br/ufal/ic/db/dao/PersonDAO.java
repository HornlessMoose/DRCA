package br.ufal.ic.db.dao;

import io.dropwizard.hibernate.AbstractDAO;
import java.io.Serializable;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.db.models.Person;


@Slf4j
public class PersonDAO extends AbstractDAO<Person> {
    
    public PersonDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Person get(Serializable id) throws HibernateException {
        log.info("getting person: id={}", id);
        return super.get(id);
    }
    
    public List<Person> list() throws HibernateException {
        log.info("getting persons");
        return super.list(query("from Person"));
    }
    
    @Override
    public Person persist(Person entity) throws HibernateException {
        return super.persist(entity);
    }
}