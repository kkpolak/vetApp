package uj.pwkp.gr1.vet.VetApp.controller.rest.response;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;

@EqualsAndHashCode(callSuper = true)
@Value
public class AnimalResponseDto extends RepresentationModel<AnimalResponseDto> {

  int id;
  String name;
  LocalDateTime dateOfBirth;
  String type;
  int ownerID;

  public AnimalResponseDto(Animal animal) {
    this.id = animal.getId();
    this.name = animal.getName();
    this.dateOfBirth = animal.getDateOfBirth();
    this.type = animal.getType().name();
    this.ownerID = animal.getOwner().getId();
  }
}
