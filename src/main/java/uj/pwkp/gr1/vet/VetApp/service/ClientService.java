package uj.pwkp.gr1.vet.VetApp.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.repository.AnimalRepository;
import uj.pwkp.gr1.vet.VetApp.repository.ClientRepository;
import uj.pwkp.gr1.vet.VetApp.repository.VisitRepository;

@Slf4j
@Service
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private VisitRepository visitRepository;

  public List<Client> getAllClients() {
    log.info("Getting all clients - service");
    return clientRepository.findAll();
  }

  public Client getClientById(int id) {
    var result = clientRepository.findById(id);
    log.info("Getting client by id: " + id);
    return result.orElseThrow(() -> {
      String message = String.format("Wrong client  id: %s ", id);
      log.error(message);
      throw new ObjectNotFoundVetAppException(message,
          VetAppResourceType.CLIENT);
    });
  }

  public Client createClient(ClientRequest req) {
    Client c;
    try {
      c = clientRepository.save(Client.builder()
          .firstName(req.getFirstName())
          .lastName(req.getLastName())
          .build());
    } catch (Exception e) {
      String message = String.format(
          "An attempt to add a client: %s to the database has failed",
          req.toString());
      log.error(message);
      throw new CreateVetAppException(message,
          VetAppResourceType.CLIENT);
    }
    log.info(String.format("Client %s created", req.toString()));
    return c;
  }

  public List<Animal> getClientsAnimals(int id) {
    return animalRepository.findAll().stream()
        .filter(a -> a.getOwner().getId() == id)
        .collect(Collectors.toList());
  }

  public List<Visit> getClientsVisits(int id) {
    return visitRepository.findAll().stream()
        .filter(v -> v.getClient().getId() == id).collect(
            Collectors.toList());
  }

  public Client delete(@NotNull int id) {
    var client = getClientById(id);
    try {
      clientRepository.delete(client);
      return client;
    } catch (Exception e) {
      String message = String.format(
          "An attempt to add a client: %s to the database has failed",
          client);
      log.error(message);
      throw new DeleteVetAppException(message,
          VetAppResourceType.CLIENT);
    }
  }
}
