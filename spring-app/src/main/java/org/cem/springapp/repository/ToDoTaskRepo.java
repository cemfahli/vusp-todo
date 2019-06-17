package org.cem.springapp.repository;

import java.util.List;

import org.cem.springapp.model.ToDoTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoTaskRepo extends CrudRepository<ToDoTask, Long> {
  List<ToDoTask> findByUser(String user);
  void deleteByIdAndUser(Long id, String user);
}