package uj.pwkp.gr1.vet.VetApp.controller.rest.response;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;

@EqualsAndHashCode(callSuper = true)
@Value
public class VetResponseDto extends RepresentationModel<VetResponseDto> {
  int id;
  String firstName;
  String lastName;
  String photo;
  LocalDateTime admissionStart;
  LocalDateTime admissionEnd;
  Duration duration;

  public VetResponseDto(Vet vet) {
    this.id = vet.getId();
    this.firstName = vet.getFirstName();
    this.lastName = vet.getLastName();
    this.photo = vet.getPhoto();
    this.admissionStart = vet.getAdmissionStart();
    this.admissionEnd = vet.getAdmissionEnd();
    this.duration = vet.getDuration();
  }
}
