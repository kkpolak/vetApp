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
import uj.pwkp.gr1.vet.VetApp.controller.rest.ClientRestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.VetRestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VetRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Client;
import uj.pwkp.gr1.vet.VetApp.entity.Vet;
import uj.pwkp.gr1.vet.VetApp.service.VetService;

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
@WebMvcTest(VetRestController.class)
public class VetRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private VetService vetService;

    @Test
    public void givenVets_whenGetAllVets_thenReturnJsonArray() throws Exception {
        Vet vet = Vet.builder()
                .id(1)
                .firstName("vet")
                .lastName("vet")
                .photo("vet")
                .admissionStart(null)
                .admissionEnd(null)
                .duration(null)
                .build();
        List<Vet> allVets = Collections.singletonList(vet);
        given(vetService.getAllVets()).willReturn(allVets);
        String uri = "/api/vet/all";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is(vet.getFirstName())));

    }

    @Test
    public void givenVetsId_whenGetVetById_thenReturnJsonWithOk() throws Exception {
        Vet vet = Vet.builder()
                .id(1)
                .firstName("vet")
                .lastName("vet")
                .photo("vet")
                .admissionStart(null)
                .admissionEnd(null)
                .duration(null)
                .build();
        given(vetService.getVetById(1)).willReturn(java.util.Optional.ofNullable(vet));
        String uri = "/api/vet/1";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(vet.getFirstName())));
    }

    @Test
    public void givenVetsId_whenGetVetByWrongId_thenReturnJsonWithNotFound() throws Exception {
        given(vetService.getVetById(1)).willReturn(java.util.Optional.empty());
        String uri = "/api/vet/1";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenVetRequest_whenCreateVet_thenReturnJsonWithCreated() throws Exception {
        //given
        VetRequest vetRequest = new VetRequest("vet", "vet", "vet",
                null, null, null);
        Vet vet = Vet.builder()
                .id(1)
                .firstName("vet")
                .lastName("vet")
                .photo("vet")
                .admissionStart(null)
                .admissionEnd(null)
                .duration(null)
                .build();
        Either<String, Vet> res = new Either<String, Vet>() {
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
            public Vet get() {
                return vet;
            }

            @Override
            public String stringPrefix() {
                return null;
            }
        };
        given(vetService.createVet(vetRequest)).willReturn(res);
        String uri = "/api/vet/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(vetRequest))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains(vetRequest.getFirstName()));
    }

    @Test
    public void givenVetRequest_whenCreateExistingVet_thenReturnJsonWithBadRequest() throws Exception {
        //given
        VetRequest vetRequest = new VetRequest("vet", "vet", "vet",
                null, null, null);
        Either<String, Vet> res = new Either<String, Vet>() {
            @Override
            public String getLeft() {
                return "vet creation error";
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
            public Vet get() {
                return null;
            }

            @Override
            public String stringPrefix() {
                return null;
            }
        };
        given(vetService.createVet(vetRequest)).willReturn(res);
        String uri = "/api/vet/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(vetRequest))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains(vetRequest.getFirstName()));
    }

    @Test
    public void givenVetId_whenDeleteVet_thenReturnJsonWithOk() throws Exception {
        //given
        Vet vet = Vet.builder()
                .id(1)
                .firstName("vet")
                .lastName("vet")
                .photo("vet")
                .admissionStart(null)
                .admissionEnd(null)
                .duration(null)
                .build();
        given(vetService.delete(1)).willReturn(Optional.ofNullable(vet));
        String uri = "/api/vet/delete/1";

        //when
        var result = mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(vet.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(vet.getLastName())));
    }

    @Test
    public void givenWrongVetId_whenDeleteVet_thenReturnJsonWithNotFound() throws Exception {
        //given
        given(vetService.delete(1)).willReturn(Optional.empty());
        String uri = "/api/vet/delete/1";

        //when
        var result = mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNotFound());
    }
}
