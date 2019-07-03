package org.cem.springapp.controller;

import org.springframework.web.bind.annotation.RestController;

/* import java.util.ArrayList; */
import java.util.List;

import org.cem.springapp.exceptions.EmptyTaskException;
import org.cem.springapp.model.DTOTask;
import org.cem.springapp.model.ToDoTask;
import org.cem.springapp.repository.ToDoTaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/* import org.springframework.security.access.prepost.PostAuthorize; */
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/* import org.springframework.web.bind.annotation.RequestParam; */


@RestController
public class ToDoListController {

  @Autowired
  private ToDoTaskRepo toDoTaskRepo;
  
/*   @RequestMapping(value="/items", method=RequestMethod.GET)
  public List<String> getToDoList() throws Exception {
      List<String> placeholder = new ArrayList<String>();
      placeholder.add("Take out the trash");
      return placeholder;
  } */

  @RequestMapping(value="/items", method=RequestMethod.GET)
  public List<ToDoTask> getUsersToDoList(Authentication authentication) throws Exception {
    String user = authentication.getName();
    List<ToDoTask> tasks = toDoTaskRepo.findByUser(user);
    return tasks;
  }

  @RequestMapping(value="/items", method=RequestMethod.POST)
  public ResponseEntity<?> addToUsersToDoList(
    Authentication authentication,
    @RequestBody(required = false) DTOTask task) {
    
    if (task == null) throw new EmptyTaskException();
    String user = authentication.getName();
    ToDoTask newTask = new ToDoTask();
    newTask.setTask(task.getTask());
    newTask.setUser(user);
    return ResponseEntity.ok(toDoTaskRepo.save(newTask));
  }

  @Transactional
  @RequestMapping(value="/items/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteFromUsersToDoList(
    Authentication authentication,
    @PathVariable("id") Long id) throws Exception {

    String user = authentication.getName();
    toDoTaskRepo.deleteByIdAndUser(id, user);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}