package uj.pwkp.gr1.vet.VetApp.controller.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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


  @GetMapping(path = "{id}")
  public ResponseEntity<?> getAnimal(@PathVariable int id) {
    return animalService.getAnimalById(id).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping(path = "/all")
  public List<Animal> getAllAnimals() {
    return animalService.getAllAnimals();
  }

  @PostMapping(path = "/create")
  public ResponseEntity<?> createAnimal(@RequestBody AnimalRequest animalRequest) {
    var result = animalService.createAnimal(animalRequest);
    return result.isLeft() ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error")
        : ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping(path = "/delete/{id}")
  ResponseEntity<?> deleteAnimal(@PathVariable int id) {
    var result = animalService.delete(id);
    return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
}
