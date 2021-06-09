package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.service.ClientService;

@Slf4j
@RestController
@RequestMapping(path = "/api/clients")
public class ClientRestController {

  @Autowired
  private ClientService clientService;

  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> getClient(@PathVariable int id) {
    log.info("Getting client by id - controller");
    var result = clientService.getClientById(id);
    Link linkClient = linkTo(ClientRestController.class).slash(id)
        .withSelfRel();
    result.add(linkClient);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<Client> getAllClients() {
    log.info("Getting all clients - controller");
    var clients = clientService.getAllClients();
    clients.forEach(c -> c.add(
        linkTo(ClientRestController.class).slash(c.getId())
            .withSelfRel()));
    Link link = linkTo(ClientRestController.class).withSelfRel();
    return CollectionModel.of(clients, link);
  }

  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> createClient(
      @RequestBody ClientRequest clientRequest) {
    log.info("Creating client - controller");
    var result = clientService.createClient(clientRequest);
    Link linkClient = linkTo(ClientRestController.class)
        .slash(result.getId()).withSelfRel();
    result.add(linkClient);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> deleteClient(@PathVariable int id) {
    log.info("Deleting client - controller");
    var result = clientService.delete(id);
    Link linkClient = linkTo(ClientRestController.class).slash(id)
        .withSelfRel();
    result.add(linkClient);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(path = "/animals")
  public ResponseEntity<?> getClientsAnimals(@PathVariable int id) {
    log.info("Getting client by id - controller");
    var result = clientService.getClientsAnimals(id);
//    Link linkClient = linkTo(ClientRestController.class).slash(id).withSelfRel();
//    result.add(linkClient);
    return ResponseEntity
        .ok(result);//new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(path = "/visits")
  public ResponseEntity<?> getClientsVisits(@PathVariable int id) {
    log.info("Getting client by id - controller");
    var result = clientService.getClientsVisits(id);
//    Link linkClient = linkTo(ClientRestController.class).slash(id).withSelfRel();
//    result.add(linkClient);
    return ResponseEntity
        .ok(result);//new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PatchMapping("/animal/{aid}/{cid}")
  public ResponseEntity<?> addClientsAnimal(@PathVariable Integer aid,
      @PathVariable Integer cid) {
    var result = clientService.addClientsAnimal(aid, cid);
    return ResponseEntity.ok(result);
  }

//  @PatchMapping("/visit/{vid}/{cid}")
//  public ResponseEntity<?> addClientsVisit(@PathVariable Integer vid,
//      @PathVariable Integer cid) {
//    var result = clientService.addClientsVisit(vid, cid);
//    return ResponseEntity.ok(result);
//  }
}
