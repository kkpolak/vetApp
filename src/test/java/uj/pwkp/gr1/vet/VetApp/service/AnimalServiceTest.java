package uj.pwkp.gr1.vet.VetApp.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.pwkp.gr1.vet.VetApp.repository.AnimalRepository;
import uj.pwkp.gr1.vet.VetApp.repository.ClientRepository;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

  @Mock
  private AnimalRepository animalRepository;

  @Mock
  private ClientRepository clientRepository;

  @InjectMocks
  private AnimalService animalService;

  @BeforeEach
  private void setUp() {
    initAnimals();
    initClients();
  }

  @Test
  public void getAllAnimalsReturnsProperEmptyList() {
    given(animalRepository.findAll()).willReturn(Collections.emptyList());

    var allAnimals = animalService.getAllAnimals();

    assertThat(allAnimals).isEmpty();
  }

  @Test
  public void getAllAnimalsReturnsProperList() {
    given(animalRepository.findAll()).willReturn(List.of(animal1, animal2));

    var allAnimals = animalService.getAllAnimals();

    assertThat(allAnimals).containsExactlyInAnyOrderElementsOf(List.of(animal1, animal2));
  }

  @Test
  public void getAnimalById() {
    given(animalRepository.findById(1)).willReturn(Optional.of(animal1));

    var animal = animalService.getAnimalById(1);

    assertThat(animal).isEqualTo(animal1);
  }

  @Test
  public void getAnimalByIdException() {
//    given(animalRepository.findById(100)).willReturn(Optional.empty());
//
//    assertThatThrownBy(() -> {
//      var c = animalService.getAnimalById(100);
//    });
  }

  @Test
  public void createAnimal() {
//    var animalRequest = new AnimalRequest(LocalDateTime.of(2020, Month.APRIL, 20, 19, 31, 29),
//        AnimalType.DOG, 1, "animal1");
//
//    var animalToSave = Animal.builder()
//        .id(-1)
//        .type(AnimalType.DOG)
//        .dateOfBirth(LocalDateTime.of(2020, Month.APRIL, 20, 19, 31, 29))
//        .name("animal1")
//        .owner(client1)
//        .build();
//
//    given(clientRepository.findById(1)).willReturn(Optional.of(client1));
//    given(animalRepository.save(animalToSave)).willReturn(animal1);
//
//    var animal = animalService.createAnimal(animalRequest);
//
//    assertThat(animal).isEqualTo(animal1);
  }

  @Test
  public void createAnimalException() {

  }

}