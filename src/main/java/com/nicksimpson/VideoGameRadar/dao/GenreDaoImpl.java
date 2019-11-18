package com.nicksimpson.VideoGameRadar.dao;

import com.nicksimpson.VideoGameRadar.model.Genre;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class GenreDaoImpl implements GenreDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Genre> findAll() {
        //open session
        Session session = sessionFactory.openSession();

        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Create CriteriaQuery
        CriteriaQuery<Genre> criteria = builder.createQuery(Genre.class);

        // Specify criteria root
        criteria.from(Genre.class);

        // Execute query
        List<Genre> genres = session.createQuery(criteria).getResultList();


        session.close();

        return genres;
    }

    @Override
    public Genre findById(Long id) {
        //open session
        Session session = sessionFactory.openSession();

        //use session to grab and create a genre obj by the given id
        Genre genre = session.get(Genre.class,id);
        Hibernate.initialize(genre.getGames());
        //close session
        session.close();

        //return the genre
        return genre;
    }

    @Override
    public Genre findByName(String name) {
        Session session = sessionFactory.openSession();
        Genre genre = session.get(Genre.class,name);
        session.close();
        return genre;
    }


    @Override
    public void save(Genre genre) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    session.saveOrUpdate(genre);
    session.getTransaction().commit();
    session.close();
    }

    @Override
    public void delete(Genre genre) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    session.delete(genre);
    session.getTransaction().commit();
    session.close();
    }


}
