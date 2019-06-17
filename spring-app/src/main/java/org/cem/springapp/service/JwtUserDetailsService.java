package org.cem.springapp.service;

import java.util.ArrayList;

import org.cem.springapp.model.DAOUser;
import org.cem.springapp.model.DTOUser;
import org.cem.springapp.repository.DAOUserRepo;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
//import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Autowired
  private DAOUserRepo dAOUserRepo;

  @Autowired
  private PasswordEncoder bcryptEncoder;

/*   @Autowired
  private Logger logger; */

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    DAOUser user = dAOUserRepo.findByUsername(username);
    if (user != null) {
      return new User(user.getUsername(),
        user.getPassword(),
        new ArrayList<>());
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }

  public DAOUser save(DTOUser user) throws JdbcSQLIntegrityConstraintViolationException {
    DAOUser newUser = new DAOUser();
    newUser.setUsername(user.getUsername());
    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
    return dAOUserRepo.save(newUser);
  }
}