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
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.OfficeRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.OfficeResponseDto;
import uj.pwkp.gr1.vet.VetApp.service.OfficeService;

@Slf4j
@RestController
@RequestMapping(path = "/api/offices")
public class OfficeRestController {

  @Autowired
  private OfficeService officeService;

  @GetMapping(path = "/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<OfficeResponseDto> getOffice(
      @PathVariable int id) {
    log.info("Getting office by id - controller");
    var resultDAO = officeService.getOfficeById(id);
    var result = new OfficeResponseDto(resultDAO);
    var linkOffice = linkTo(OfficeRestController.class).slash(id)
        .withSelfRel();
    result.add(linkOffice);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(path = "/all", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public CollectionModel<OfficeResponseDto> getAllOffices() {
    log.info("Getting all offices - controller");
    var offices = officeService.getAllOffices().stream()
        .map(OfficeResponseDto::new).collect(
            Collectors.toList());
    offices.forEach(o -> o.add(
        linkTo(OfficeRestController.class).slash(o.getId())
            .withSelfRel()));
    var link = linkTo(OfficeRestController.class).withSelfRel();
    return CollectionModel.of(offices, link);
  }

  @PostMapping(path = "/create", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<OfficeResponseDto> createOffice(
      @RequestBody OfficeRequest officeRequest) {
    log.info("Creating office - controller");
    var resultDAO = officeService.createOffice(officeRequest);
    var result = new OfficeResponseDto(resultDAO);
    var linkOffice = linkTo(OfficeRestController.class)
        .slash(result.getId()).withSelfRel();
    result.add(linkOffice);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @DeleteMapping(path = "/delete/{id}", produces = MediaTypes.HAL_FORMS_JSON_VALUE)
  public ResponseEntity<OfficeResponseDto> deleteOffice(
      @PathVariable int id) {
    log.info("Deleting office - controller");
    var resultDAO = officeService.deleteOffice(id);
    var result = new OfficeResponseDto(resultDAO);
    var linkOffice = linkTo(OfficeRestController.class).slash(id)
        .withSelfRel();
    result.add(linkOffice);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
