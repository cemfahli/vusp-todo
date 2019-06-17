package org.cem.springapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ToDoTask {

//work on normalizing, set up relationship with User

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String user;
  private String task;

  public Long getId(){
    return this.id;
  }

  public String getUser(){
    return this.user;
  }

  public String getTask(){
    return this.task;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public void setTask(String task) {
    this.task = task;
  }

}