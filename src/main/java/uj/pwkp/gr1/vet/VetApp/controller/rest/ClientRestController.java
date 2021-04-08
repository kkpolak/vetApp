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
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.service.ClientService;

@RestController
@RequestMapping(path = "/api/clients")
public class ClientRestController {

  @Autowired
  private ClientService clientService;

  @GetMapping(path = "{id}")
  public ResponseEntity<?> getClient(@PathVariable int id) {
    return clientService.getClientById(id).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping(path = "/all")
  public List<Client> getAllClients() {
    return clientService.getAllClients();
  }

  @PostMapping(path = "/create")
  public ResponseEntity<?> createClient(@RequestBody ClientRequest clientRequest) {
    var result = clientService.createClient(clientRequest);
    return result.isLeft() ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.left().get())
        : ResponseEntity.status(HttpStatus.CREATED).body(result.right().get());
  }

  @DeleteMapping(path = "/delete/{id}")
  public ResponseEntity<?> deleteClient(@PathVariable int id) {
    var result = clientService.delete(id);
    return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
}
