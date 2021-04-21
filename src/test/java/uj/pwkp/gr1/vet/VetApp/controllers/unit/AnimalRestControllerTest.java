package uj.pwkp.gr1.vet.VetApp.controllers.unit;

import io.vavr.control.Either;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uj.pwkp.gr1.vet.VetApp.controller.rest.AnimalRestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.ClientRestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.AnimalRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Animal;
import uj.pwkp.gr1.vet.VetApp.entity.AnimalType;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.service.AnimalService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AnimalRestController.class)
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
        given(animalService.getAnimalById(1)).willReturn(Optional.ofNullable(animal));
        String uri = "/api/animals/1";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(animal.getName())));
    }

    @Test
    public void givenAnimalId_whenGetAnimalById_thenReturnJsonWithNotFound() throws Exception {
        given(animalService.getAnimalById(1)).willReturn(Optional.empty());
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
        Either<String, Animal> res = new Either<>() {
            @Override
            public String getLeft() {
                return null;
            }

            @Override
            public boolean isLeft() {
                return false;
            }

            @Override
            public boolean isRight() {
                return true;
            }

            @Override
            public Animal get() {
                return animal;
            }

            @Override
            public String stringPrefix() {
                return null;
            }
        };
        given(animalService.createAnimal(animalRequest)).willReturn(res);
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
        Either<String, Animal> res = new Either<>() {
            @Override
            public String getLeft() {
                return "animal creation error";
            }

            @Override
            public boolean isLeft() {
                return true;
            }

            @Override
            public boolean isRight() {
                return false;
            }

            @Override
            public Animal get() {
                return null;
            }

            @Override
            public String stringPrefix() {
                return null;
            }
        };
        given(animalService.createAnimal(animalRequest)).willReturn(res);
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
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().equals("animal creation error"));
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
        given(animalService.delete(1)).willReturn(Optional.ofNullable(animal));
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
        given(animalService.delete(1)).willReturn(Optional.empty());
        String uri = "/api/animals/delete/1";

        //when
        var result = mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNotFound());
    }
}
