package uj.pwkp.gr1.vet.VetApp.controllers.unit;

import io.vavr.control.Either;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uj.pwkp.gr1.vet.VetApp.controller.rest.ClientRestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.service.ClientService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ClientRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClientRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientService clientService;

    @Test
    public void givenClients_whenGetAllClients_thenReturnJsonArray() throws Exception {
        Client client = Client.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        List<Client> allClients = Collections.singletonList(client);
        given(clientService.getAllClients()).willReturn(allClients);
        String uri = "/api/clients/all";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is(client.getFirstName())));

    }

    @Test
    public void givenClients_whenGetClientById_thenReturnJsonWithClient() throws Exception {
        Client client = Client.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        given(clientService.getClientById(1)).willReturn(client);
        String uri = "/api/clients/1";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(client.getFirstName())));
    }

    @Test
    public void givenClients_whenGetClientByWrongId_thenReturnNotFoundJson() throws Exception {
        given(clientService.getClientById(1)).willThrow(new ObjectNotFoundVetAppException("", VetAppResourceType.CLIENT));
        String uri = "/api/clients/1";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenClientRequest_whenCreateClient_thenReturnJsonWithCreated() throws Exception {
        //given
        ClientRequest clientRequest = new ClientRequest("John", "Doe");
        Client client = Client.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        given(clientService.createClient(clientRequest)).willReturn(client);
        String uri = "/api/clients/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(clientRequest))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains(clientRequest.getFirstName()));
    }

    @Test
    public void givenClientRequest_whenCreateClient_thenReturnJsonWithBadRequest() throws Exception {
        //given
        ClientRequest clientRequest = new ClientRequest("John", "Doe");
        given(clientService.createClient(clientRequest)).willThrow(new CreateVetAppException("", VetAppResourceType.CLIENT));
        String uri = "/api/clients/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content(String.valueOf(mapper.writeValueAsString(clientRequest)))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
//        Assertions.assertTrue(response.getContentAsString().equals("Client creation error"));
    }

    @Test
    public void givenClientId_whenDeleteClient_thenReturnJsonWithOk() throws Exception {
        //given
        Client client = Client.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        given(clientService.delete(1)).willReturn(client);
        String uri = "/api/clients/delete/1";

        //when
        var result = mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(client.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(client.getLastName())));
    }

    @Test
    public void givenClientId_whenDeleteClient_thenReturnJsonWithNotFound() throws Exception {
        //given
        given(clientService.delete(1)).willThrow(new DeleteVetAppException("", VetAppResourceType.CLIENT));
        String uri = "/api/clients/delete/1";

        //when
        var result = mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isInternalServerError());
    }
}
