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
import uj.pwkp.gr1.vet.VetApp.repository.OfficeRepository;
import uj.pwkp.gr1.vet.VetApp.repository.VetRepository;
import uj.pwkp.gr1.vet.VetApp.repository.VisitRepository;

@ExtendWith(MockitoExtension.class)
public class VisitServiceTest {

  @Mock
  private AnimalRepository animalRepository;

  @Mock
  private ClientRepository clientRepository;

  @Mock
  private OfficeRepository officeRepository;

  @Mock
  private VetRepository vetRepository;

  @Mock
  private VisitRepository visitRepository;

  @InjectMocks
  private VisitService visitService;

  @BeforeEach
  private void setUp() {
    initAnimals();
    initClients();
    initOffices();
    initVets();
    initVisits();
  }

  @Test
  public void getAllVisitsReturnsProperEmptyList() {
    given(visitRepository.findAll()).willReturn(Collections.emptyList());

    var allVisits = visitService.getAllVisits();

    assertThat(allVisits).isEmpty();
  }

  @Test
  public void getAllVisitsReturnsProperList() {
    given(visitRepository.findAll()).willReturn(List.of(visit1, visit2));

    var allVisits = visitService.getAllVisits();

    assertThat(allVisits).containsExactlyInAnyOrderElementsOf(List.of(visit1, visit2));
  }

  @Test
  public void getVisitById() {
    given(visitRepository.findById(1)).willReturn(Optional.of(visit1));

    var visit = visitService.getVisitById(1);

    assertThat(visit).isEqualTo(Optional.of(visit1));
  }

  @Test
  public void getVisitByIdException() {
//    given(visitRepository.findById(100)).willReturn(Optional.empty());
//
//    assertThatThrownBy(() -> {
//      var v = visitService.getVisitById(100);
//    });
  }

  @Test
  public void createVisit() {
//    var visitRequest = new VisitRequest(LocalDateTime.of(2021, Month.APRIL, 14, 10, 15),
//        Duration.parse("PT0H15M"), AnimalType.DOG, BigDecimal.ONE, 1, "mess1", 1, 1,
//        1);
//
//    var visitToSave = Visit.builder()
//        .id(-1)
//        .startTime(LocalDateTime.of(2021, Month.APRIL, 14, 10, 15))
//        .duration(Duration.parse("PT0H15M"))
//        .price(BigDecimal.ONE)
//        .status(Status.PLANNED)
//        .description("mess1")
//        .animal(animal1)
//        .vet(vet1)
//        .office(office1)
//        .client(client1)
//        .build();
//
//    given(animalRepository.findById(1)).willReturn(Optional.of(animal1));
//    given(vetRepository.findById(1)).willReturn(Optional.of(vet1));
//    given(officeRepository.findById(1)).willReturn(Optional.of(office1));
//    given(clientRepository.findById(1)).willReturn(Optional.of(client1));
//
//    given(visitRepository.save(visitToSave)).willReturn(visit1);
//
//    var visit = visitService.createVisit(visitRequest);
//
//    assertThat(visit).isEqualTo(visit);
  }

  @Test
  public void createVisitException() {

  }

}