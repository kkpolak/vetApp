package uj.pwkp.gr1.vet.VetApp.controller.rest.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchRequest {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final int vetId;
    private final int officeId;
}
