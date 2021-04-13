package uj.pwkp.gr1.vet.VetApp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.OfficeRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Office;
import uj.pwkp.gr1.vet.VetApp.service.OfficeService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/offices")
public class OfficeRestController {
    @Autowired
    private OfficeService officeService;

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getOffice(@PathVariable int id) {
        return officeService.getOffcieById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/all")
    public List<Office> getAllOffices() {
        return officeService.getAllOffices();
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createOffice(@RequestBody OfficeRequest officeRequest) {
        var result = officeService.createOffice(officeRequest);
        return result.isLeft() ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.left().get())
                : ResponseEntity.status(HttpStatus.CREATED).body(result.right().get());
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable int id) {
        var result = officeService.deleteOffice(id);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
