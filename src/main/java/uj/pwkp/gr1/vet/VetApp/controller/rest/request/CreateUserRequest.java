package uj.pwkp.gr1.vet.VetApp.controller.rest.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private final String username;
    private final String password;
    private final String role;
}
