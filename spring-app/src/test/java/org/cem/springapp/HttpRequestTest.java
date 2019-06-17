package org.cem.springapp;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.cem.springapp.model.DAOUser;
import org.cem.springapp.model.DTOUser;
import org.cem.springapp.model.JwtRequest;
import org.cem.springapp.model.ToDoTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
  
  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private String username = "botb";
  private String password = "password";

  private Logger log = LoggerFactory.getLogger("Debug");

  @Test
  @Order(1)
  public void registerTest() throws Exception {
    DTOUser user = new DTOUser();
    user.setUsername(username);
    user.setPassword(password);
    HttpEntity<DTOUser> request = new HttpEntity<DTOUser>(user);
    ResponseEntity<DAOUser> registeredUser = restTemplate.postForEntity(
        "http://localhost:" + port + "/register",
        request,
        DAOUser.class);
    Assertions.assertThat(registeredUser.getBody().getUsername()).isEqualTo(user.getUsername());

    registeredUser = restTemplate.postForEntity(
        "http://localhost:" + port + "/register",
        request,
        DAOUser.class);

    log.info(Integer.toString(registeredUser.getStatusCodeValue()));
  }

  @Test
  @Order(2)
  public void authenticateTest() throws Exception {

    DTOToken receivedToken = authenticate();

    Assertions.assertThat(receivedToken).isNotNull();
    log.info(receivedToken.getToken());
  }
  
  @Test
  @Order(3)
  public void authorizeTest() throws Exception {

    HttpHeaders header = authorizeHeader(authenticate().getToken());

    HttpEntity<?> request = new HttpEntity<>(header);

    ResponseEntity<List<String>> result = restTemplate.exchange(
      "http://localhost:" + port + "/items",
      HttpMethod.GET,
      request,
      new ParameterizedTypeReference<List<String>>(){});

    Assertions.assertThat(result.getBody()).isNotNull();
    for(String task : result.getBody()){
      log.info(task);
    }
  }

  @Test
  @Order(3)
  public void writeTaskTest() throws Exception {

    HttpHeaders header = authorizeHeader(authenticate().getToken());
    String newTask = "Sample Task";

    HttpEntity<String> request = new HttpEntity<>(newTask, header);

    ResponseEntity<ToDoTask> result = restTemplate.exchange(
      "http://localhost:" + port + "/items",
      HttpMethod.POST,
      request,
      ToDoTask.class);

    String task = result.getBody().getTask();

    Assertions.assertThat(task).isEqualTo(newTask);

  }

  @Test
  @Order(3)
  public void writeReadTaskTest() throws Exception {

    HttpHeaders header = authorizeHeader(authenticate().getToken());
    List<String> tasks = new ArrayList<String>();
    
    for(int i = 0; i < 10; i++){

      String newTask = "Sample Task: " + i;
      tasks.add(newTask);

      HttpEntity<String> request = new HttpEntity<>(newTask, header);

      restTemplate.exchange(
        "http://localhost:" + port + "/items",
        HttpMethod.POST,
        request,
        ToDoTask.class);

    }

    HttpEntity<?> request = new HttpEntity<>(header);
    ResponseEntity<List<ToDoTask>> result = restTemplate.exchange(
      "http://localhost:" + port + "/items",
      HttpMethod.GET,
      request,
      new ParameterizedTypeReference<List<ToDoTask>>(){});

    List<ToDoTask> receivedTasks = result.getBody();

    for(int i = 0; i < receivedTasks.size(); i++){
      Assertions.assertThat(receivedTasks.get(i).getTask()).isEqualTo(tasks.get(i));
      log.info(receivedTasks.get(i).getTask());
    }
  }

  @Test
  @Order(3)
  public void writeReadDeleteTaskTest() throws Exception {

    HttpHeaders header = authorizeHeader(authenticate().getToken());
    List<String> tasks = new ArrayList<String>();
    
    for(int i = 0; i < 10; i++){

      String newTask = "Sample Task: " + i;
      tasks.add(newTask);

      HttpEntity<String> request = new HttpEntity<>(newTask, header);

      restTemplate.exchange(
        "http://localhost:" + port + "/items",
        HttpMethod.POST,
        request,
        ToDoTask.class);

    }

    HttpEntity<?> request = new HttpEntity<>(header);
    ResponseEntity<List<ToDoTask>> result = restTemplate.exchange(
      "http://localhost:" + port + "/items",
      HttpMethod.GET,
      request,
      new ParameterizedTypeReference<List<ToDoTask>>(){});

    List<ToDoTask> receivedTasks = result.getBody();

    ResponseEntity<?> response = restTemplate.exchange(
      "http://localhost:" +
        port +
        "/items/" +
        receivedTasks.get(2).getId(),
      HttpMethod.DELETE,
      request,
      ResponseEntity.class);
    
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    result = restTemplate.exchange(
      "http://localhost:" + port + "/items",
      HttpMethod.GET,
      request,
      new ParameterizedTypeReference<List<ToDoTask>>(){});

    receivedTasks = result.getBody();
    tasks.remove(2);

    for(int i = 0; i < tasks.size(); i++){
      Assertions.assertThat(receivedTasks.get(i).getTask()).isEqualTo(tasks.get(i));
      log.info(receivedTasks.get(i).getTask());
    }
  }

  //PRIVATE METHODS

  /**
   * Method to construct an authorized header 
   * @param token the jwt token recieved by authenticating
   * @return the header containing the Authorization value
   */
  private HttpHeaders authorizeHeader(String token) {

    HttpHeaders header = new HttpHeaders();
    String bearerToken = "Bearer " + token;

    header.set("Authorization", bearerToken);
    return header;
  }

  /**
   * Method to get jwt token from server using correct credentials
   * @return Jwt token in string form
   */
  private DTOToken authenticate() {
    JwtRequest user = new JwtRequest(username, password);

    HttpEntity<JwtRequest> request = new HttpEntity<JwtRequest>(user);

    DTOToken receivedToken = restTemplate.postForObject(
      "http://localhost:" + port + "/authenticate",
      request,
      DTOToken.class);

    return receivedToken;
  }

  static class DTOToken {
    private String token;

    public DTOToken(String token) {
      setToken(token);
    }

    public DTOToken(){}

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }
  }
}