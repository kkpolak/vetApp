package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.service.ClientService;

@RestController
@RequestMapping(path = "/api/clients")
public class ClientRestController {

  @Autowired
  private ClientService clientService;

  //@GetMapping(path = "/{id}", produces = "application/hal+json")
  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> getClient(@PathVariable int id) {
    var client = clientService.getClientById(id);
    if (client.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var result = client.get();
    Link linkClient = linkTo(ClientRestController.class).slash(id).withSelfRel();
    result.add(linkClient);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  //@GetMapping(path = "/all", produces = "application/hal+json")
  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<Client> getAllClients() {
    var clients = clientService.getAllClients();
    clients.forEach(c -> c.add(linkTo(ClientRestController.class).slash(c.getId()).withSelfRel()));
    Link link = linkTo(ClientRestController.class).withSelfRel();
    return CollectionModel.of(clients, link);
  }

  //@PostMapping(path = "/create", produces = "application/hal+json")
  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> createClient(@RequestBody ClientRequest clientRequest) {
    var client = clientService.createClient(clientRequest);
    if (client.isLeft()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(client.left().get());
    }
    var result = client.right().get();
    Link linkClient = linkTo(ClientRestController.class).slash(result.getId()).withSelfRel();
    result.add(linkClient);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  //@DeleteMapping(path = "/delete/{id}", produces = "application/hal+json")
  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> deleteClient(@PathVariable int id) {
    var client = clientService.delete(id);
    if (client.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var result = client.get();
    Link linkClient = linkTo(ClientRestController.class).slash(id).withSelfRel();
    result.add(linkClient);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
