package uj.pwkp.gr1.vet.VetApp.controller.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.OfficeRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Office;
import uj.pwkp.gr1.vet.VetApp.service.OfficeService;

@Slf4j
@RestController
@RequestMapping(path = "/api/offices")
public class OfficeRestController {

  @Autowired
  private OfficeService officeService;

  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<?> getOffice(@PathVariable int id) {
    log.info("Getting office by id - controller");
    var result = officeService.getOfficeById(id);
    Link linkOffice = linkTo(OfficeRestController.class).slash(id).withSelfRel();
    result.add(linkOffice);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<Office> getAllOffices() {
    log.info("Getting all offices - controller");
    var offices = officeService.getAllOffices();
    offices.forEach(o -> o.add(linkTo(OfficeRestController.class).slash(o.getId()).withSelfRel()));
    Link link = linkTo(OfficeRestController.class).withSelfRel();
    return CollectionModel.of(offices, link);
  }

  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<Office> createOffice(@RequestBody OfficeRequest officeRequest) {
    log.info("Creating office - controller");
    var result = officeService.createOffice(officeRequest);
    Link linkOffice = linkTo(OfficeRestController.class).slash(result.getId()).withSelfRel();
    result.add(linkOffice);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<Office> deleteOffice(@PathVariable int id) {
    log.info("Deleting office - controller");
    var result = officeService.deleteOffice(id);
    Link linkOffice = linkTo(OfficeRestController.class).slash(id).withSelfRel();
    result.add(linkOffice);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
