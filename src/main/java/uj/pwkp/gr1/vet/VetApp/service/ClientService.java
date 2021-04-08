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
import uj.pwkp.gr1.vet.VetApp.repository.ClientRepository;

@Slf4j
@Service
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;

//  @Autowired
//  private AnimalRepository animalRepository;

  public List<Client> getAllClients() {
    return clientRepository.findAll();
  }

  public Optional<Client> getClientById(int id) {
    return clientRepository.findById(id);
  }

  public Either<String, Client> createClient(ClientRequest req) {
    Client c;
    try {
      //List<Animal> animalList = animalRepository.findAllById(req.getAnimalIdList());
      c = clientRepository.save(Client.builder()
          .firstName(req.getFirstName())
          .lastName(req.getLastName())
          //.animals(animalList)
          .build());
    } catch (Exception e) {
      return Either.left("Client creation error");
    }

    return Either.right(c);
  }

  public Optional<Client> delete(@NotNull int id) {
    var client = clientRepository.findById(id);
    return Optional.ofNullable(client).map(c -> {
      clientRepository.deleteById(c.get().getId());
      return c;
    }).orElseGet(()->{
      log.info(String.format("Client with such id: %x was not found",id));
      return Optional.empty();
    });
  }

}
