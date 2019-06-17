package org.cem.springapp.repository;

import org.cem.springapp.model.DAOUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DAOUserRepo extends CrudRepository<DAOUser, Long> {
  DAOUser findByUsername(String username);
}