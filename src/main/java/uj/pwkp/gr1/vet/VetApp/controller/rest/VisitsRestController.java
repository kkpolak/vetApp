package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.SearchRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VisitRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.service.VisitCreationResult;
import uj.pwkp.gr1.vet.VetApp.service.VisitService;

@Slf4j
@RestController
@RequestMapping(path = "/api/visits")
public class VisitsRestController {

  @Autowired
  private VisitService visitsService;

  //@GetMapping(path = "/{id}", produces = "application/hal+json")
  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> getVisit(@PathVariable int id) {
    var result = visitsService.getVisitById(id);
    Link linkVisit = linkTo(VisitsRestController.class).slash(id).withSelfRel();
    Link linkAnimal = linkTo(AnimalRestController.class).slash(result.getAnimal().getId())
        .withSelfRel();
    Link linkClient = linkTo(ClientRestController.class).slash(result.getClient().getId())
        .withSelfRel();
    Link linkVet = linkTo(VetRestController.class).slash(result.getVet().getId()).withSelfRel();
    result.add(linkVisit);
    result.getAnimal().add(linkAnimal);
    result.getClient().add(linkClient);
    result.getVet().add(linkVet);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  //@GetMapping(path = "/all", produces = "application/hal+json")
  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<Visit> getAllVisits() {
    var visits = visitsService.getAllVisits();
    visits.forEach(
        visit -> visit.add(linkTo(VisitsRestController.class).slash(visit.getId()).withSelfRel()));
    Link link = linkTo(VisitsRestController.class).withSelfRel();
    return CollectionModel.of(visits, link);
  }

  //@PostMapping(path = "/create", produces = "application/hal+json")
  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> createVisit(@RequestBody VisitRequest visitReq) {
    var result = visitsService.createVisit(visitReq);
    Link linkVisit = linkTo(VisitsRestController.class).slash(result.getId()).withSelfRel();
    Link linkAnimal = linkTo(AnimalRestController.class).slash(result.getAnimal().getId())
        .withSelfRel();
    Link linkClient = linkTo(ClientRestController.class).slash(result.getClient().getId())
        .withSelfRel();
    Link linkVet = linkTo(VetRestController.class).slash(result.getVet().getId()).withSelfRel();
    result.add(linkVisit);
    result.getAnimal().add(linkAnimal);
    result.getClient().add(linkClient);
    result.getVet().add(linkVet);

    return visitToResult(result);
  }

  //@DeleteMapping(path = "/delete/{id}", produces = "application/hal+json")
  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> deleteVisit(@PathVariable int id) {
    var result = visitsService.delete(id);
    Link linkVisit = linkTo(VisitsRestController.class).slash(id).withSelfRel();
    Link linkAnimal = linkTo(AnimalRestController.class).slash(result.getAnimal().getId())
        .withSelfRel();
    Link linkClient = linkTo(ClientRestController.class).slash(result.getClient().getId())
        .withSelfRel();
    Link linkVet = linkTo(VetRestController.class).slash(result.getVet().getId()).withSelfRel();
    result.add(linkVisit);
    result.getAnimal().add(linkAnimal);
    result.getClient().add(linkClient);
    result.getVet().add(linkVet);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PatchMapping(path = "/update/{id}/{status}")
  public ResponseEntity<?> updateStatus(@PathVariable("id") int id,
      @PathVariable("status") String status) {
    Status newStatus;
    try {
      newStatus = Status.valueOf(status);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("{\"reason\": \"Unknown status\"}");
    }
    var result = visitsService.updateVisitStatus(id, newStatus);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PatchMapping(path = "update/{vetId}/{visitId}/{status}")
  public ResponseEntity<?> updateStatusByVet(@PathVariable("vetId") int vetId,
      @PathVariable("visitId") int visitId, @PathVariable("status") @Min(1) @Max(2) int status) {
    var result = visitsService.changeVisitStatus(vetId, visitId, status);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  private ResponseEntity<?> visitToResult(Visit visit) {
    return ResponseEntity.status(HttpStatus.CREATED).body(visit);
  }

  private ResponseEntity<?> visitCreationResultToBadRequest(VisitCreationResult result) {
    switch (result) {
      case OVERLAP:
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("{\"reason\": \"Overlapping with other visit.\"}");
      case REPOSITORY_PROBLEM:
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("{\"reason\": \"Problem with server, please try again later.\"}");
      default:
        return ResponseEntity.badRequest().body("{\"reason\": \"Unknown.\"}");
    }
  }

  @PostMapping(path = "/search")
  public ResponseEntity<?> searchTerms(@RequestBody SearchRequest searchRequest) {
    LocalDateTime start = searchRequest.getStartTime();
    LocalDateTime end = searchRequest.getEndTime();
    int officeId = searchRequest.getOfficeId();
    int vetId = searchRequest.getVetId();
    var terms = visitsService.searchTerms(start, end, officeId, vetId);
    return ResponseEntity.status(HttpStatus.CREATED).body(terms);
  }
}

