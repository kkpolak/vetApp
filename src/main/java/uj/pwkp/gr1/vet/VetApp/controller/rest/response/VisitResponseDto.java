package uj.pwkp.gr1.vet.VetApp.controller.rest.response;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;

@EqualsAndHashCode(callSuper = true)
@Value
public class VisitResponseDto extends RepresentationModel<VisitResponseDto> {

  int id;
  LocalDateTime startTime;
  Duration duration;
  Status status;
  BigDecimal price;
  String description;
  int animalID;
  int clientID;
  int vetID;
  int officeID;

  public VisitResponseDto(Visit visit) {
    this.id = visit.getId();
    this.startTime = visit.getStartTime();
    this.duration = visit.getDuration();
    this.status = visit.getStatus();
    this.price = visit.getPrice();
    this.description = visit.getDescription();
    this.animalID = visit.getAnimal().getId();
    this.clientID = visit.getClient().getId();
    this.vetID = visit.getVet().getId();
    this.officeID = visit.getOffice().getId();
  }
}
