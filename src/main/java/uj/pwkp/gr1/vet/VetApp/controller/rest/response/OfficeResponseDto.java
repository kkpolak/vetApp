package uj.pwkp.gr1.vet.VetApp.controller.rest.response;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import uj.pwkp.gr1.vet.VetApp.entity.Office;

@EqualsAndHashCode(callSuper = true)
@Value
public class OfficeResponseDto extends RepresentationModel<OfficeResponseDto> {
  int id;
  String name;

  public OfficeResponseDto(Office office) {
    this.id = office.getId();
    this.name = office.getName();
  }
}
