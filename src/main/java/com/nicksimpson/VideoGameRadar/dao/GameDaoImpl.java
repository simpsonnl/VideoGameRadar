package com.nicksimpson.VideoGameRadar.dao;

import com.nicksimpson.VideoGameRadar.model.Game;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class GameDaoImpl implements GameDao{


    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public List<Game> findAll() {
        //open session
        Session session = sessionFactory.openSession();

        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Create CriteriaQuery
        CriteriaQuery<Game> criteria = builder.createQuery(Game.class);

        // Specify criteria root
        criteria.from(Game.class);

        // Execute query
        List<Game> games = session.createQuery(criteria).getResultList();

        session.close();


        return games;
    }

    @Override
    public Game findById(Long id) {
        Session session = sessionFactory.openSession();
        Game game = session.get(Game.class,id);
        session.close();
        return game;
    }

    @Override
    public void save(Game game) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(game);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Game game) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(game);
        session.getTransaction().commit();
        session.close();
    }
}
