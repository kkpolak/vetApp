package uj.pwkp.gr1.vet.VetApp.controller.rest.request;

import lombok.Data;

@Data
public class OfficeRequest {
    private final String name;

    public OfficeRequest() {
        this.name = null;
    }

    public OfficeRequest(String name) {
        this.name = name;
    }
}
