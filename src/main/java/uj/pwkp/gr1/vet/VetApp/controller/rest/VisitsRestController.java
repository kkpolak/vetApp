package uj.pwkp.gr1.vet.VetApp.controller.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.pwkp.gr1.vet.VetApp.entity.Status;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.service.VisitCreationResult;
import uj.pwkp.gr1.vet.VetApp.service.VisitService;

@RestController
@RequestMapping(path = "/api/visits")
public class VisitsRestController {

  private final VisitService visitsService;

  @Autowired
  public VisitsRestController(VisitService visitsService) {
    this.visitsService = visitsService;
  }

  @GetMapping(path="{id}")
  public ResponseEntity<?> getVisit(@PathVariable int id) {
    return visitsService.getVisitById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Visit> getAllVisits() {
    return visitsService.getAllVisits();
  }

  @PostMapping()
  public ResponseEntity<?> createVisit(@RequestBody VisitRequest visitReq) {
    var result = visitsService.createVisit(visitReq);
    return result.map(this::visitCreationResultToBadRequest, this::visitToResult);
  }

  @DeleteMapping(path = "/{id}")
  ResponseEntity<?> delete(@PathVariable int id) {
    var result = visitsService.delete(id);
    return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  private ResponseEntity<?> visitToResult(Visit visit) {
    return ResponseEntity.status(HttpStatus.CREATED).body(visit);
  }

  private ResponseEntity<?> visitCreationResultToBadRequest(VisitCreationResult result) {
    switch (result) {
      case OVERLAP:
        return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"reason\": \"Overlapping with other visit.\"}");
      case REPOSITORY_PROBLEM:
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"reason\": \"Problem with server, please try again later.\"}");
      default:
        return ResponseEntity.badRequest().body("{\"reason\": \"Unknown.\"}");
    }
  }

  @PatchMapping(path = "/update/{id}/{status}")
  ResponseEntity<?> updateStatus(@PathVariable("id") int id, @PathVariable("status") String status) {
    Status newStatus;
    try {
      newStatus = Status.valueOf(status);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("{\"reason\": \"Unknown status\"}");
    }
    var result = visitsService.updateVisitStatus(id, newStatus);
    return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
}

