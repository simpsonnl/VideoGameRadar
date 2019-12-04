package com.nicksimpson.VideoGameRadar.service;

import com.nicksimpson.VideoGameRadar.model.Sort;

import java.util.List;

public interface SortService {
  List<Sort> findAll();
  Sort findById(Long id);
}
