package uj.pwkp.gr1.vet.VetApp.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.CreateUserRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.UserResponse;
import uj.pwkp.gr1.vet.VetApp.service.UserDetailsServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        log.info("Getting all users - controller");
        return userDetailsService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest userRequest) {
        log.info("Creating user - controller");
        var result = userDetailsService.createUser(userRequest);
        return new ResponseEntity<>(
                new UserResponse(result.getUsername(), result.getRole().name()),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        var result = userDetailsService.deleteUser(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}