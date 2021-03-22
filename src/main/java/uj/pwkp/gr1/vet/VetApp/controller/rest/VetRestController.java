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
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VetRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.service.VetService;

@RestController
@RequestMapping(path = "/api/vet")
public class VetRestController {

  @Autowired
  private VetService vetService;

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getVet(@PathVariable int id) {
    return vetService.getVetById(id).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping(path = "/all")
  public List<Vet> getAllVets() {
    return vetService.getAllVets();
  }

  @PostMapping(path = "/create")
  public ResponseEntity<?> createVet(@RequestBody VetRequest vetRequest) {
    var result = vetService.createVet(vetRequest);
    return result.isLeft() ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR")
        : ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping(path = "/delete/{id}")
  ResponseEntity<?> deleteVet(@PathVariable int id) {
    var result = vetService.delete(id);
    return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

}
