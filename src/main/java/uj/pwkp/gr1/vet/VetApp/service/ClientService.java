package uj.pwkp.gr1.vet.VetApp.service;

import io.vavr.control.Either;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VetRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.repository.ClientRepository;

@Slf4j
@Service
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;

//  @Autowired
//  private AnimalRepository animalRepository;

  public List<Client> getAllClients() {
    log.info("Getting all clients - service");
    return clientRepository.findAll();
  }

  public Client getClientById(int id) {
    var result = clientRepository.findById(id);
    return result.orElseThrow(
        () -> new ObjectNotFoundVetAppException(String.format("Wrong id: %s", id),
            VetAppResourceType.CLIENT));
  }

  public Client createClient(ClientRequest req) {
    Client c;
    try {
      //List<Animal> animalList = animalRepository.findAllById(req.getAnimalIdList());
      c = clientRepository.save(Client.builder()
          .firstName(req.getFirstName())
          .lastName(req.getLastName())
          //.animals(animalList)
          .build());
    } catch (Exception e) {
      throw new CreateVetAppException(
          String
              .format("An attempt to add a client: %s to the database has failed", req.toString()),
          VetAppResourceType.CLIENT);
    }

    return c;
  }

  public Client delete(@NotNull int id) {
    var client = getClientById(id);
    try {
      clientRepository.delete(client);
      return client;
    } catch (Exception e) {
      throw new DeleteVetAppException(
          String.format("An attempt to add a client: %s to the database has failed", client),
          VetAppResourceType.CLIENT);
    }
  }
}
