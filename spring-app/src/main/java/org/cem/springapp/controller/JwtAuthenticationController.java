package org.cem.springapp.controller;

import org.cem.springapp.config.JwtTokenUtil;
import org.cem.springapp.exceptions.UnoriginalUsernameException;
import org.cem.springapp.model.DAOUser;
import org.cem.springapp.model.DTOUser;
import org.cem.springapp.model.JwtRequest;
import org.cem.springapp.model.JwtResponse;
import org.cem.springapp.service.JwtUserDetailsService;
//import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUserDetailsService jwtUserDetailsService;

/*   @Autowired
  private Logger logger; */

  private void authenticate(String username, String password) throws Exception {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }

  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new JwtResponse(token));
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ResponseEntity<?> saveUser(@RequestBody DTOUser user) {
    try {
      DAOUser newUser = jwtUserDetailsService.save(user);
      return ResponseEntity.ok(newUser);
    } catch (Exception e) {
      throw new UnoriginalUsernameException();
    }
  }

  @ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason = "Incorrect Username/Password")
  @ExceptionHandler(BadCredentialsException.class)
  public void badCredentials() {}
  
  @ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason = "User Disabled")
  @ExceptionHandler(DisabledException.class)
  public void userDisabled() {}
}