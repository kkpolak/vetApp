package uj.pwkp.gr1.vet.VetApp.controller.rest.response;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.AnimalType;
import uj.pwkp.gr1.vet.VetApp.entity.Client;

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
