package uj.pwkp.gr1.vet.VetApp.controller.rest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Data;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;

@Data
public class VisitRequest {
  private final LocalDateTime startTime;
  private final Duration duration;
  private final Animal animal;
  private final BigDecimal price;
}
