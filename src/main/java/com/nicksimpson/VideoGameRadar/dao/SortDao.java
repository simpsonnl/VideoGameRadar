package com.nicksimpson.VideoGameRadar.dao;

import com.nicksimpson.VideoGameRadar.model.Sort;

import java.util.List;

public interface SortDao {

  List<Sort> findAll();
  Sort findById(long id);
}
