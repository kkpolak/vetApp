package uj.pwkp.gr1.vet.VetApp.controller.rest.response;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import uj.pwkp.gr1.vet.VetApp.entity.Client;

@EqualsAndHashCode(callSuper = true)
@Value
public class ClientResponseDto extends
    RepresentationModel<ClientResponseDto> {

  int id;
  String firstName;
  String lastName;

  public ClientResponseDto(Client client) {
    this.id = client.getId();
    this.firstName = client.getFirstName();
    this.lastName = client.getLastName();
  }
}
