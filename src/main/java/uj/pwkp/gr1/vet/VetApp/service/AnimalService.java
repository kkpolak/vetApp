package uj.pwkp.gr1.vet.VetApp.service;

import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.AnimalRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.repository.AnimalRepository;
import uj.pwkp.gr1.vet.VetApp.repository.ClientRepository;

@Slf4j
@Service
public class AnimalService {
  private final AnimalRepository animalRepository;
  private final ClientService clientService;

  public AnimalService(AnimalRepository animalRepository, ClientService clientService) {
    this.animalRepository = animalRepository;
    this.clientService = clientService;
  }

  public List<Animal> getAllAnimals() {
    log.info("Getting all animals - service");
    return animalRepository.findAll();
  }

  public Animal getAnimalById(int id) {
    var result = animalRepository.findById(id);
    log.info("Getting animal by id: " + id);
    return result.orElseThrow(() -> {
          String message = String.format("Wrong id: %s", id);
          log.error(message);
          throw new ObjectNotFoundVetAppException(message,
            VetAppResourceType.ANIMAL);
        });
  }

  public Animal createAnimal(AnimalRequest req) {
    Animal a;
    try {
      Client client = clientService.getClientById(req.getOwnerId());

      a = animalRepository.save(
          Animal.builder()
              .dateOfBirth(req.getDateOfBirth())
              .type(req.getType())
              .owner(client)
              .name(req.getName())
              .build());
    } catch (Exception e) {
      String message = String.format("An attempt to add a animal: %s to the database has failed", req.toString());
      log.error(message);
      throw new CreateVetAppException(message, VetAppResourceType.ANIMAL);
    }
    log.info(String.format("Animal %s created", req.toString()));
    return a;
  }

  public Animal delete(@NotNull int id) {
    var animal = getAnimalById(id);
    try {
      animalRepository.delete(animal);
      log.info(String.format("Animal %s deleted", animal.toString()));
      return animal;
    } catch (Exception e) {
      String message = String.format("An attempt to delete a animal: %s from the database has failed", id);
      log.error(message);
      throw new DeleteVetAppException(message, VetAppResourceType.ANIMAL);
    }
  }
}
