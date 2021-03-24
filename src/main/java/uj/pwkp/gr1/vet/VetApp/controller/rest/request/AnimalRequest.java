package uj.pwkp.gr1.vet.VetApp.controller.rest.request;

import java.time.LocalDateTime;
import lombok.Data;
import uj.pwkp.gr1.vet.VetApp.entity.AnimalType;

@Data
public class AnimalRequest {
  private final LocalDateTime dateOfBirth;
  private final AnimalType type;
  private final int ownerId;
  private final String name;
}
