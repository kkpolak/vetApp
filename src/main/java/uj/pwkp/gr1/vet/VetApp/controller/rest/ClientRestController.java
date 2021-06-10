package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
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
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.AnimalResponseDto;
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.ClientResponseDto;
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.VisitResponseDto;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.service.ClientService;

@Slf4j
@RestController
@RequestMapping(path = "/api/clients")
public class ClientRestController {

  @Autowired
  private ClientService clientService;

  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<ClientResponseDto> getClient(
      @PathVariable int id) {
    log.info("Getting client by id - controller");
    var resultDAO = clientService.getClientById(id);
    var result = new ClientResponseDto(resultDAO);
    Link linkClient = linkTo(ClientRestController.class).slash(id)
        .withSelfRel();
    result.add(linkClient);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<ClientResponseDto> getAllClients() {
    log.info("Getting all clients - controller");
    var clients = clientService.getAllClients().stream()
        .map(ClientResponseDto::new).collect(
            Collectors.toList());
    clients.forEach(c -> c.add(
        linkTo(ClientRestController.class).slash(c.getId())
            .withSelfRel()));
    var link = linkTo(ClientRestController.class).withSelfRel();
    return CollectionModel.of(clients, link);
  }

  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<ClientResponseDto> createClient(
      @RequestBody ClientRequest clientRequest) {
    log.info("Creating client - controller");
    var resultDAO = clientService.createClient(clientRequest);
    var result = new ClientResponseDto(resultDAO);
    var linkClient = linkTo(ClientRestController.class)
        .slash(result.getId()).withSelfRel();
    result.add(linkClient);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<ClientResponseDto> deleteClient(
      @PathVariable int id) {
    log.info("Deleting client - controller");
    var resultDAO = clientService.delete(id);
    var result = new ClientResponseDto(resultDAO);
    var linkClient = linkTo(ClientRestController.class).slash(id)
        .withSelfRel();
    result.add(linkClient);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(path = "/animals/{id}")
  public CollectionModel<AnimalResponseDto> getClientsAnimals(
      @PathVariable Integer id) {
    log.info("Getting client by id - controller");
    var result = clientService.getClientsAnimals(id).stream()
        .map(AnimalResponseDto::new).collect(Collectors.toList());
    result.forEach(c -> c
        .add(linkTo(Animal.class).slash(c.getId()).withSelfRel()));
    Link link = linkTo(AnimalRestController.class).withSelfRel();
    return CollectionModel.of(result, link);
  }

  @GetMapping(path = "/visits/{id}")
  public CollectionModel<VisitResponseDto> getClientsVisits(
      @PathVariable Integer id) {
    log.info("Getting client by id - controller");
    var result = clientService.getClientsVisits(id).stream()
        .map(VisitResponseDto::new).collect(Collectors.toList());
    result.forEach(c -> c
        .add(linkTo(Visit.class).slash(c.getId()).withSelfRel()));
    Link link = linkTo(VisitsRestController.class).withSelfRel();
    return CollectionModel.of(result, link);
  }
}
