package uj.pwkp.gr1.vet.VetApp.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.initVets;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.vet1;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.vet2;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.pwkp.gr1.vet.VetApp.repository.VetRepository;

@ExtendWith(MockitoExtension.class)
public class VetServiceTest {

  @Mock
  private VetRepository vetRepository;

  @InjectMocks
  private VetService vetService;

  @BeforeEach
  public void setUp() {
    initVets();
  }

  @Test
  public void getAllVetsReturnsProperEmptyList() {
    given(vetRepository.findAll()).willReturn(Collections.emptyList());

    var allVets = vetService.getAllVets();

    assertThat(allVets).isEmpty();
  }

  @Test
  public void getAllVetsReturnsProperList() {
    given(vetRepository.findAll()).willReturn(List.of(vet1, vet2));

    var allVets = vetService.getAllVets();

    assertThat(allVets).containsExactlyInAnyOrderElementsOf(List.of(vet1, vet2));
  }

  @Test
  public void getVetById() {
    given(vetRepository.findById(1)).willReturn(Optional.of(vet1));

    var vet = vetService.getVetById(1);

    assertThat(vet).isEqualTo(Optional.of(vet1));
  }

  @Test
  public void getVetByIdException() {
//    given(vetRepository.findById(100)).willReturn(Optional.empty());
//
//    assertThatThrownBy(() -> {
//      var c = vetService.getVetById(100);
//    });
  }

  @Test
  public void createVet() {
//    var vetRequest = new VetRequest("first", "last", "photo",
//        LocalDateTime.of(2021, Month.JANUARY, 1, 8, 0),
//        LocalDateTime.of(2021, Month.JANUARY, 1, 16, 0),
//        Duration.parse("PT8H"));
//
//    var clientToSave = Vet.builder()
//        .id(-1)
//        .firstName("first")
//        .lastName("last")
//        .photo("photo")
//        .admissionStart(LocalDateTime.of(2021, Month.JANUARY, 1, 8, 0))
//        .admissionEnd(LocalDateTime.of(2021, Month.JANUARY, 1, 16, 0))
//        .duration(Duration.parse("PT8H"))
//        .build();
//
//    given(vetRepository.save(clientToSave)).willReturn(vet1);
//
//    var vet = vetService.createVet(vetRequest);
//
//    assertThat(vet).isEqualTo(vet1);
  }

  @Test
  public void createVetException() {

  }

}