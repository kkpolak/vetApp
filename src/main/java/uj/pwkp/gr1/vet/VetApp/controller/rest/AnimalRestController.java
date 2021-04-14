package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.AnimalRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.service.AnimalService;

@RestController
@RequestMapping(path = "/api/animals")
public class AnimalRestController {

  @Autowired
  private AnimalService animalService;


  //@GetMapping(path = "/{id}", produces = "application/hal+json")
  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<Animal> getAnimal(@PathVariable int id) {
    var animal = animalService.getAnimalById(id);
    if (animal.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var result = animal.get();
    Link linkAnimal = linkTo(AnimalRestController.class).slash(id).withSelfRel();
    Link linkOwner = linkTo(ClientRestController.class).slash(result.getOwner().getId()).withSelfRel();
    result.add(linkAnimal);
    result.getOwner().add(linkOwner);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  //@GetMapping(path = "/all", produces = "application/hal+json")
  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<Animal> getAllAnimals() {
    List<Animal> animals = animalService.getAllAnimals();
    animals.forEach(animal -> animal
        .add(linkTo(AnimalRestController.class).slash(animal.getId()).withSelfRel()));
    animals.forEach(animal -> animal.getOwner()
        .add(linkTo(ClientRestController.class).slash(animal.getOwner().getId()).withSelfRel()));
    Link link = linkTo(AnimalRestController.class).withSelfRel();
    return CollectionModel.of(animals, link);
  }

  //@PostMapping(path = "/create", produces = "application/hal+json")
  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> createAnimal(@RequestBody AnimalRequest animalRequest) {
    var animal = animalService.createAnimal(animalRequest);
    if (animal.isLeft()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(animal.left().get());
    }
    var result = animal.right().get();
    Link linkAnimal = linkTo(AnimalRestController.class).slash(result.getId()).withSelfRel();
    Link linkOwner = linkTo(ClientRestController.class).slash(result.getOwner().getId()).withSelfRel();
    result.add(linkAnimal);
    result.getOwner().add(linkOwner);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  //@DeleteMapping(path = "/delete/{id}", produces = "application/hal+json")
  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  ResponseEntity<?> deleteAnimal(@PathVariable int id) {
    var animal = animalService.delete(id);
    if (animal.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var result = animal.get();
    Link linkAnimal = linkTo(AnimalRestController.class).slash(id).withSelfRel();
    Link linkOwner = linkTo(ClientRestController.class).slash(result.getOwner().getId()).withSelfRel();
    result.add(linkAnimal);
    result.getOwner().add(linkOwner);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
