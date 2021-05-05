package uj.pwkp.gr1.vet.VetApp.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.client1;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.client2;
import static uj.pwkp.gr1.vet.VetApp.TestConfig.initClients;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.pwkp.gr1.vet.VetApp.repository.ClientRepository;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

  @Mock
  private ClientRepository clientRepository;

  @InjectMocks
  private ClientService clientService;

  @BeforeEach
  private void setUp() {
    initClients();
  }

  @Test
  public void getAllClientsReturnsProperEmptyList() {
    given(clientRepository.findAll()).willReturn(Collections.emptyList());

    var allClients = clientService.getAllClients();

    assertThat(allClients).isEmpty();
  }

  @Test
  public void getAllClientsReturnsProperList() {
    given(clientRepository.findAll()).willReturn(List.of(client1, client2));

    var allClients = clientService.getAllClients();

    assertThat(allClients).containsExactlyInAnyOrderElementsOf(List.of(client1, client2));
  }

  @Test
  public void getClientById() {
    given(clientRepository.findById(1)).willReturn(Optional.of(client1));

    var client = clientService.getClientById(1);

    assertThat(client).isEqualTo(client1);
  }

  @Test
  public void getClientByIdException() {
//    given(clientRepository.findById(100)).willReturn(Optional.empty());
//
//    assertThatThrownBy(() -> {
//      var c = clientService.getClientById(100);
//    });
  }

  @Test
  public void createClient() {
//    var clientRequest = new ClientRequest("client1F", "client1L");
//
//    var clientToSave = Client.builder()
//        .id(-1)
//        .firstName(clientRequest.getFirstName())
//        .lastName(clientRequest.getLastName())
//        .build();
//
//    given(clientRepository.save(clientToSave)).willReturn(client1);
//
//    var client = clientService.createClient(clientRequest);
//
//    assertThat(client).isEqualTo(client1);
  }

  @Ignore
  @Test
  public void createClientException() {

  }

}