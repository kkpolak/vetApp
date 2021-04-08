package uj.pwkp.gr1.vet.VetApp.service;

import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.AnimalRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VetRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;
import uj.pwkp.gr1.vet.VetApp.repository.AnimalRepository;
import uj.pwkp.gr1.vet.VetApp.repository.ClientRepository;

@Slf4j
@Service
public class AnimalService {

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private ClientRepository clientRepository;

  public List<Animal> getAllAnimals() {
    return animalRepository.findAll();
  }

  public Optional<Animal> getAnimalById(int id) {
    return animalRepository.findById(id);
  }

  public Either<String, Animal> createAnimal(AnimalRequest req) {
    Animal a;
    try {
      Client client = clientRepository.findById(req.getOwnerId()).orElseGet(
          () -> {
            log.info(String.format("client with this id: %x was not found", req.getOwnerId()));
            return Client.builder().firstName("anonymous").lastName("anonymous")
                .id(req.getOwnerId())
                .build();
          });

      a = animalRepository.save(
          Animal.builder()
              .dateOfBirth(req.getDateOfBirth())
              .type(req.getType())
              .owner(client).build());
    } catch (Exception e) {
      return Either.left("vet creation error");
    }

    return Either.right(a);
  }

  public Optional<Animal> delete(@NotNull int id) {
    var animal = animalRepository.findById(id);
    return Optional.ofNullable(animal).map(a -> {
      animalRepository.deleteById(a.get().getId());
      return a;
    }).orElseGet(() -> {
      log.info(String.format("Animal with such id: %x was not found", id));
      return Optional.empty();
    });
  }

}
