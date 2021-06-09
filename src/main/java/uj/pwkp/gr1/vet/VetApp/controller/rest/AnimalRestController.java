package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;

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
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.AnimalRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.service.AnimalService;

@Slf4j
@RestController
@RequestMapping(path = "/api/animals")
public class AnimalRestController {

  @Autowired
  private AnimalService animalService;

  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<Animal> getAnimalById(@PathVariable int id) {
    log.info("Getting animal by id - controller");
    var result = animalService.getAnimalById(id);
    Link linkAnimal = linkTo(AnimalRestController.class).slash(id).withSelfRel();
    Link linkOwner = linkTo(ClientRestController.class).slash(result.getOwner().getId())
        .withSelfRel();
    result.add(linkAnimal);
    result.getOwner().add(linkOwner);
    return ResponseEntity.ok(result);
  }

  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<Animal> getAllAnimals() {
    log.info("Getting all animals - controller");
    List<Animal> animals = animalService.getAllAnimals();
    animals.forEach(animal -> animal
        .add(linkTo(AnimalRestController.class).slash(animal.getId()).withSelfRel()));
    animals.forEach(animal -> {
      var owner = animal.getOwner();
      if(owner != null) {
        animal.getOwner().add(linkTo(ClientRestController.class).slash(owner.getId()).withSelfRel());
      }
  });
    Link link = linkTo(AnimalRestController.class).withSelfRel();
    return CollectionModel.of(animals, link);
  }

  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<Animal> createAnimal(@RequestBody AnimalRequest animalRequest) {
    log.info("Creating animal - controller");
    var result = animalService.createAnimal(animalRequest);
    Link linkAnimal = linkTo(AnimalRestController.class).slash(result.getId()).withSelfRel();
    Link linkOwner = linkTo(ClientRestController.class).slash(result.getOwner().getId())
        .withSelfRel();
    result.add(linkAnimal);
    result.getOwner().add(linkOwner);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  ResponseEntity<Animal> deleteAnimal(@PathVariable int id) {
    log.info("Deleting animal - controller");
    var result = animalService.delete(id);
    Link linkAnimal = linkTo(AnimalRestController.class).slash(id).withSelfRel();
    Link linkOwner = linkTo(ClientRestController.class).slash(result.getOwner().getId())
        .withSelfRel();
    result.add(linkAnimal);
    result.getOwner().add(linkOwner);
    return ResponseEntity.ok(result);
  }
}
