package com.nicksimpson.VideoGameRadar.dao;

import com.nicksimpson.VideoGameRadar.model.User;

import java.util.List;

public interface UserDetailsDao {

  User findUserByUsername(String username);

  void save(User user);

  List<User> findAll();
}
