package com.nicksimpson.VideoGameRadar.dao;

import com.nicksimpson.VideoGameRadar.model.Game;
import com.nicksimpson.VideoGameRadar.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Repository
public class UserDetailsDaoImpl implements UserDetailsDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public User findUserByUsername(String username) {
    Query<User> query = sessionFactory.getCurrentSession().createQuery("FROM User u where u.username=:username", User.class);
    query.setParameter("username", username);
    return query.uniqueResult();
  }

  @Override
  public void save(User user) {
    Session session = sessionFactory.openSession();
    session.beginTransaction();
    session.saveOrUpdate(user);
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public List<User> findAll() {
    Session session = sessionFactory.openSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<User> criteria = builder.createQuery(User.class);
    criteria.from(User.class);
    List<User> users = session.createQuery(criteria).getResultList();
    session.close();

    return users;
  }
}
