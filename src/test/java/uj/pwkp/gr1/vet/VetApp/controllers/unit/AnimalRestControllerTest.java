package uj.pwkp.gr1.vet.VetApp.controllers.unit;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uj.pwkp.gr1.vet.VetApp.controller.rest.AnimalRestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.AnimalRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.AnimalType;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.service.AnimalService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AnimalRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AnimalRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AnimalService animalService;

    @Test
    public void givenAnimals_whenGetAllAnimals_thenReturnJsonArray() throws Exception {
        Client client = Client.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        Animal animal = Animal.builder()
                .id(1)
                .name("animal")
                .dateOfBirth(LocalDateTime.now())
                .type(AnimalType.CAT)
                .owner(client)
                .build();
        List<Animal> allAnimals = Collections.singletonList(animal);
        given(animalService.getAllAnimals()).willReturn(allAnimals);
        String uri = "/api/animals/all";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(animal.getName())));
    }

    @Test
    public void givenAnimalId_whenGetAnimalById_thenReturnJsonWithOk() throws Exception {
        Client client = Client.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        Animal animal = Animal.builder()
                .id(1)
                .name("animal")
                .dateOfBirth(LocalDateTime.now())
                .type(AnimalType.CAT)
                .owner(client)
                .build();
        given(animalService.getAnimalById(1)).willReturn(animal);
        String uri = "/api/animals/1";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(animal.getName())));
    }

    @Test
    public void givenAnimalId_whenGetAnimalById_thenReturnJsonWithNotFound() throws Exception {
        given(animalService.getAnimalById(1)).willThrow(new ObjectNotFoundVetAppException("", VetAppResourceType.ANIMAL));
        String uri = "/api/animals/1";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenAnimalRequest_whenCreateAnimal_thenReturnJsonWithCreated() throws Exception {
        //given
        AnimalRequest animalRequest = new AnimalRequest(null, AnimalType.CAT, 1, "animal");
        Client client = Client.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        Animal animal = Animal.builder()
                .id(1)
                .name("animal")
                .dateOfBirth(null)
                .type(AnimalType.CAT)
                .owner(client)
                .build();
        given(animalService.createAnimal(animalRequest)).willReturn(animal);
        String uri = "/api/animals/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(animalRequest))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains(animalRequest.getName()));
    }

    @Test
    public void givenAnimalRequest_whenCreateAnimal_thenReturnJsonWithBadRequest() throws Exception {
        //given
        AnimalRequest animalRequest = new AnimalRequest(null, AnimalType.CAT, 1, "animal");
        given(animalService.createAnimal(animalRequest)).willThrow(new CreateVetAppException("", VetAppResourceType.ANIMAL));
        String uri = "/api/animals/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(animalRequest))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
//        Assertions.assertTrue(response.getContentAsString().contains("An attempt to add a animal: 1 to the database has failed"));
    }

    @Test
    public void givenAnimalId_whenDeleteAnimal_thenReturnJsonWithOk() throws Exception {
        //given
        Client client = Client.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        Animal animal = Animal.builder()
                .id(1)
                .name("animal")
                .dateOfBirth(null)
                .type(AnimalType.CAT)
                .owner(client)
                .build();
        given(animalService.delete(1)).willReturn(animal);
        String uri = "/api/animals/delete/1";

        //when
        var result = mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(animal.getName())));
    }

    @Test
    public void givenAnimalId_whenDeleteWrongId_thenReturnJsonWithNotFound() throws Exception {
        //given
        given(animalService.delete(1)).willThrow(new DeleteVetAppException("", VetAppResourceType.ANIMAL));
        String uri = "/api/animals/delete/1";

        //when
        var result = mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isInternalServerError());
    }
}
