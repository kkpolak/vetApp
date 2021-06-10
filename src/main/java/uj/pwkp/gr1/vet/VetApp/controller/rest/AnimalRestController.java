package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.AnimalResponseDto;
import uj.pwkp.gr1.vet.VetApp.service.AnimalService;

@Slf4j
@RestController
@RequestMapping(path = "/api/animals")
public class AnimalRestController {

  private final AnimalService animalService;

  @Autowired
  public AnimalRestController(AnimalService animalService) {
    this.animalService = animalService;
  }

  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<AnimalResponseDto> getAnimalById(
      @PathVariable int id) {
    log.info("Getting animal by id - controller");
    var resultDAO = animalService.getAnimalById(id);
    var result = new AnimalResponseDto(resultDAO);
    var linkAnimal = linkTo(AnimalRestController.class).slash(id)
        .withSelfRel();
    result.add(linkAnimal);
    return ResponseEntity.ok(result);
  }

  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<AnimalResponseDto> getAllAnimals() {
    log.info("Getting all animals - controller");
    var animals = animalService.getAllAnimals().stream()
        .map(AnimalResponseDto::new).collect(
            Collectors.toList());
    animals.forEach(animal -> animal
        .add(linkTo(AnimalRestController.class).slash(animal.getId())
            .withSelfRel()));
    var link = linkTo(AnimalRestController.class).withSelfRel();
    return CollectionModel.of(animals, link);
  }

  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<AnimalResponseDto> createAnimal(
      @RequestBody AnimalRequest animalRequest) {
    log.info("Creating animal - controller");
    var resultDAO = animalService.createAnimal(animalRequest);
    var result = new AnimalResponseDto(resultDAO);
    var linkAnimal = linkTo(AnimalRestController.class)
        .slash(result.getId()).withSelfRel();
    result.add(linkAnimal);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<AnimalResponseDto> deleteAnimal(@PathVariable int id) {
    log.info("Deleting animal - controller");
    var resultDAO = animalService.delete(id);
    var result = new AnimalResponseDto(resultDAO);
    var linkAnimal = linkTo(AnimalRestController.class).slash(id)
        .withSelfRel();
    result.add(linkAnimal);
    return ResponseEntity.ok(result);
  }
}
