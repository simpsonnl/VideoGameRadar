package com.nicksimpson.VideoGameRadar.dao;


import com.nicksimpson.VideoGameRadar.model.Sort;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Repository
public class SortDaoImpl implements SortDao{

  @Autowired
  private SessionFactory sessionFactory;


  @Override
  public List<Sort> findAll() {
    //open session
    Session session = sessionFactory.openSession();

    // Create CriteriaBuilder
    CriteriaBuilder builder = session.getCriteriaBuilder();

    // Create CriteriaQuery
    CriteriaQuery<Sort> criteria = builder.createQuery(Sort.class);

    // Specify criteria root
    criteria.from(Sort.class);

    // Execute query
    List<Sort> sortOptions = session.createQuery(criteria).getResultList();


    session.close();

    return sortOptions;
  }

  @Override
  public Sort findById(long id) {
    Session session = sessionFactory.openSession();
    Sort sort = session.get(Sort.class,id);
    session.close();
    return sort;
  }
}
