package uj.pwkp.gr1.vet.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.UserRequest;
import uj.pwkp.gr1.vet.VetApp.security.JwtGenerator;
import uj.pwkp.gr1.vet.VetApp.service.UserDetailsServiceImpl;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Incorrect login details.");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userRequest.getUsername());
        var result = JwtGenerator.generateJWT(userDetails);
        return result.isPresent()
                ? ResponseEntity.ok(result.get())
                : ResponseEntity.status(401).body("Incorrect login details.");
    }
}
