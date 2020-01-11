package com.nicksimpson.VideoGameRadar.service;

import com.nicksimpson.VideoGameRadar.dao.UserDetailsDao;
import com.nicksimpson.VideoGameRadar.model.User;
import com.nicksimpson.VideoGameRadar.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserDetailsDao userDetailsDao;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userDetailsDao.findUserByUsername(username);
    org.springframework.security.core.userdetails.User.UserBuilder builder = null;
    if (user != null) {

      builder = org.springframework.security.core.userdetails.User.withUsername(username);

      builder.password(user.getPassword());
      String[] authorities = user.getUserRoles()
          .stream().map(a -> a.getRole()).toArray(String[]::new);

      builder.authorities(authorities);
    } else {
      throw new UsernameNotFoundException("User not found.");
    }
    return builder.build();
  }

  public void save(User user){

    UserRole userRole = new UserRole();
    userRole.setId(user.getId());
    userRole.setRole("ROLE_USER");
    Set<UserRole> userRoles = new HashSet<>();
    userRoles.add(userRole);

    String encoded = new BCryptPasswordEncoder().encode(user.getPassword());

    user.setPassword(encoded);

    user.setUserRoles(userRoles);

    userDetailsDao.save(user);

  }

  //returns true if the user exists
  public boolean exists(User user){
    List<User> users = userDetailsDao.findAll();

    for (User u: users
         ) {
      if(user.getUsername().equals(u.getUsername())){
        return true;
      }
    }

    return false;
  }
}
