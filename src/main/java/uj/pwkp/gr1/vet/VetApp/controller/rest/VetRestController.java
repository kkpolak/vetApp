package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
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
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VetRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.service.VetService;

@RestController
@RequestMapping(path = "/api/vet")
public class VetRestController {

  @Autowired
  private VetService vetService;

  //@GetMapping(path = "/{id}", produces = "application/hal+json")
  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> getVet(@PathVariable int id) {
    var vet = vetService.getVetById(id);
    if (vet.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var result = vet.get();
    Link linkVet = linkTo(VetRestController.class).slash(id).withSelfRel();
    result.add(linkVet);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  //@GetMapping(path = "/all", produces = "application/hal+json")
  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<Vet> getAllVets() {
    var vets = vetService.getAllVets();
    vets.forEach(v -> v.add(linkTo(VetRestController.class).slash(v.getId()).withSelfRel()));
    Link link = linkTo(VetRestController.class).withSelfRel();
    return CollectionModel.of(vets, link);
  }

  //@PostMapping(path = "/create", produces = "application/hal+json")
  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> createVet(@RequestBody VetRequest vetRequest) {
    var vet = vetService.createVet(vetRequest);
    if (vet.isLeft()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(vet.left().get());
    }
    var result = vet.get();
    Link linkVet = linkTo(VetRestController.class).slash(result.getId()).withSelfRel();
    result.add(linkVet);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  //@DeleteMapping(path = "/delete/{id}", produces = "application/hal+json")
  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  ResponseEntity<?> deleteVet(@PathVariable int id) {
    var vet = vetService.delete(id);
    if (vet.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var result = vet.get();
    Link linkVet = linkTo(VetRestController.class).slash(id).withSelfRel();
    result.add(linkVet);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PatchMapping(path = "update/{vetId}/{visitId}/{description}")
  public ResponseEntity<?> updateStatusByVet(@PathVariable("vetId") int vetId,
      @PathVariable("visitId") int visitId,
      @PathVariable("description") @Size(min = 1) String description) {
    var vet = vetService.changeVisitDescription(vetId, visitId, description);
    if (vet.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var result = vet.get();
    Link linkVet = linkTo(VetRestController.class).slash(result.getId()).withSelfRel();
    result.add(linkVet);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
