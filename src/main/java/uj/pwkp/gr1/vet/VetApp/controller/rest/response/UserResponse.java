package uj.pwkp.gr1.vet.VetApp.controller.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private final String username;
    private final String role;
}
