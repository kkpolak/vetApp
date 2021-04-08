package uj.pwkp.gr1.vet.VetApp.controller.rest.request;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class VetRequest {
  private final String firstName;
  private final String lastName;
  private final String photo;
  private final LocalDateTime admissionStart;
  private final LocalDateTime admissionEnd;
  private final Duration duration;
}
