package uj.pwkp.gr1.vet.VetApp.controller.rest.request;

import lombok.*;

@Data
public class LogoutRequest {
    private final String token;

    public LogoutRequest() {
        this.token = null;
    }
}
