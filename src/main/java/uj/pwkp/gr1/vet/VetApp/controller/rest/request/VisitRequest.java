package uj.pwkp.gr1.vet.VetApp.controller.rest.request;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Data;
import uj.pwkp.gr1.vet.VetApp.entity.AnimalType;

@Data
public class VisitRequest {
  private final LocalDateTime startTime;
  private final Duration duration;
  private final AnimalType animalType;
  private final BigDecimal price;
  private final int vetId;
  private final String description;
}
