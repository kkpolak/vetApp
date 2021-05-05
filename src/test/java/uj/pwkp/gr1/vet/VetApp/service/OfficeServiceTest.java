package uj.pwkp.gr1.vet.VetApp.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.initOffices;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.office1;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.office2;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.pwkp.gr1.vet.VetApp.repository.OfficeRepository;

@ExtendWith(MockitoExtension.class)
public class OfficeServiceTest {

  @Mock
  private OfficeRepository officeRepository;

  @InjectMocks
  private OfficeService officeService;

  @BeforeEach
  private void setUp() {
    initOffices();
  }

  @Test
  public void getAllOfficesReturnsProperEmptyList() {
    given(officeRepository.findAll()).willReturn(Collections.emptyList());

    var offices = officeService.getAllOffices();

    assertThat(offices).isEmpty();
  }

  @Test
  public void getAllOfficesReturnsProperList() {
    given(officeRepository.findAll()).willReturn(List.of(office1, office2));

    var offices = officeService.getAllOffices();

    assertThat(offices).containsExactlyInAnyOrderElementsOf(List.of(office1, office2));
  }

  @Test
  public void getOfficeById() {
    given(officeRepository.findById(1)).willReturn(Optional.of(office1));

    var office = officeService.getOfficeById(1);

    assertThat(office).isEqualTo(office1);
  }

  @Test
  public void getOfficeByIdException() {
//    given(officeRepository.findById(100)).willReturn(Optional.empty());
//
//    assertThatThrownBy(() -> {
//      var o = officeService.getOffcieById(100);
//    });
  }

  @Test
  public void createOffice() {
//    var officeRequest = new OfficeRequest("office1");
//
//    var clientToSave = Office.builder()
//        .id(-1)
//        .name("office1")
//        .build();
//
//    given(officeRepository.save(clientToSave)).willReturn(office1);
//
//    var office = officeService.createOffice(officeRequest);
//
//    assertThat(office).isEqualTo(office1);
  }

  @Test
  public void createOfficeException() {

  }

}