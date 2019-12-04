package com.nicksimpson.VideoGameRadar.service;

import com.nicksimpson.VideoGameRadar.dao.SortDao;
import com.nicksimpson.VideoGameRadar.model.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SortServiceImpl implements SortService{

  @Autowired
  private SortDao sortDao;


  @Override
  public List<Sort> findAll() {
    return sortDao.findAll();
  }

  @Override
  public Sort findById(Long id) {
    return sortDao.findById(id);
  }
}
