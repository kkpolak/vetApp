package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.stream.Collectors;
import javax.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.VetResponseDto;
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.VisitResponseDto;
import uj.pwkp.gr1.vet.VetApp.service.VetService;

@Slf4j
@RestController
@RequestMapping(path = "/api/vet")
public class VetRestController {

  @Autowired
  private VetService vetService;

  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<VetResponseDto> getVet(@PathVariable int id) {
    log.info("Getting vet by id - controller");
    var resultDAO = vetService.getVetById(id);
    var result = new VetResponseDto(resultDAO);
    var linkVet = linkTo(VetRestController.class).slash(id)
        .withSelfRel();
    result.add(linkVet);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<VetResponseDto> getAllVets() {
    log.info("Getting all vets - controller");
    var vets = vetService.getAllVets().stream()
        .map(VetResponseDto::new).collect(
            Collectors.toList());
    vets.forEach(v -> v.add(
        linkTo(VetRestController.class).slash(v.getId())
            .withSelfRel()));
    var link = linkTo(VetRestController.class).withSelfRel();
    return CollectionModel.of(vets, link);
  }

  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<VetResponseDto> createVet(
      @RequestBody VetRequest vetRequest) {
    log.info("Creating vet - controller");
    var resultDAO = vetService.createVet(vetRequest);
    var result = new VetResponseDto(resultDAO);
    var linkVet = linkTo(VetRestController.class)
        .slash(result.getId()).withSelfRel();
    result.add(linkVet);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<VetResponseDto> deleteVet(
      @PathVariable int id) {
    log.info("Deleting vet - controller");
    var resultDAO = vetService.delete(id);
    var result = new VetResponseDto(resultDAO);
    var linkVet = linkTo(VetRestController.class).slash(id)
        .withSelfRel();
    result.add(linkVet);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PatchMapping(path = "update/{vetId}/{visitId}/{description}")
  public ResponseEntity<Object> updateStatusByVet(
      @PathVariable("vetId") int vetId,
      @PathVariable("visitId") int visitId,
      @PathVariable("description") @Size(min = 1) String description) {
    log.info("Updating status by vet - controller");
    var resultDAO = vetService
        .changeVisitDescription(vetId, visitId, description);
    var result = new VisitResponseDto(resultDAO);
    var linkVet = linkTo(VetRestController.class)
        .slash(result.getId()).withSelfRel();
    result.add(linkVet);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
