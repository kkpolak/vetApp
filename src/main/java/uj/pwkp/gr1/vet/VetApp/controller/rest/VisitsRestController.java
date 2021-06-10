package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.SearchRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VisitRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.VisitResponseDto;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.service.VisitService;

@Slf4j
@RestController
@RequestMapping(path = "/api/visits")
public class VisitsRestController {

  @Autowired
  private VisitService visitsService;

  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<VisitResponseDto> getVisit(
      @PathVariable int id) {
    log.info("Getting visit by id - controller");
    var resultDAO = visitsService.getVisitById(id);
    var result = new VisitResponseDto(resultDAO);
    var linkVisit = linkTo(VisitsRestController.class).slash(id)
        .withSelfRel();
    result.add(linkVisit);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<VisitResponseDto> getAllVisits() {
    log.info("Getting all visits - controller");
    var visits = visitsService.getAllVisits().stream()
        .map(VisitResponseDto::new).collect(
            Collectors.toList());
    visits.forEach(
        visit -> visit.add(
            linkTo(VisitsRestController.class).slash(visit.getId())
                .withSelfRel()));
    var link = linkTo(VisitsRestController.class).withSelfRel();
    return CollectionModel.of(visits, link);
  }

  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<VisitResponseDto> createVisit(
      @RequestBody VisitRequest visitReq) {
    log.info("Creating visit - controller");
    var resultDAO = visitsService.createVisit(visitReq);
    var result = new VisitResponseDto(resultDAO);
    Link linkVisit = linkTo(VisitsRestController.class)
        .slash(result.getId()).withSelfRel();
    result.add(linkVisit);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<VisitResponseDto> deleteVisit(
      @PathVariable int id) {
    log.info("Deleting visit - controller");
    var resultDAO = visitsService.delete(id);
    var result = new VisitResponseDto(resultDAO);
    Link linkVisit = linkTo(VisitsRestController.class).slash(id)
        .withSelfRel();
    result.add(linkVisit);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PatchMapping(path = "/update/{id}/{status}")
  public ResponseEntity<Object> updateStatus(
      @PathVariable("id") int id,
      @PathVariable("status") String status) {
    Status newStatus;
    try {
      newStatus = Status.valueOf(status);
    } catch (Exception e) {
      log.error("Unknown status");
      return ResponseEntity.badRequest()
          .body("{\"reason\": \"Unknown status\"}");
    }
    var result = visitsService.updateVisitStatus(id, newStatus);
    log.info(String
        .format("Updated status of visit %s to: %s - controller", id,
            status));
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PatchMapping(path = "/update/{vetId}/{visitId}/{status}")
  public ResponseEntity<Object> updateStatusByVet(
      @PathVariable("vetId") int vetId,
      @PathVariable("visitId") int visitId,
      @PathVariable("status") @Min(1) @Max(2) int status) {
    var result = visitsService
        .changeVisitStatus(vetId, visitId, status);
    log.info(String
        .format("Updated status of visit %s to: %s - controller",
            visitId, status));
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  private ResponseEntity<Object> visitToResult(Visit visit) {
    return ResponseEntity.status(HttpStatus.CREATED).body(visit);
  }

  @PostMapping(path = "/search")
  public ResponseEntity<Object> searchTerms(
      @RequestBody SearchRequest searchRequest) {
    LocalDateTime start = searchRequest.getStartTime();
    LocalDateTime end = searchRequest.getEndTime();
    int officeId = searchRequest.getOfficeId();
    int vetId = searchRequest.getVetId();
    var terms = visitsService
        .searchTerms(start, end, officeId, vetId);
    log.info(String
        .format("In searching terms functionality found following %s",
            terms.toString()));
    return ResponseEntity.status(HttpStatus.CREATED).body(terms);
  }
}

