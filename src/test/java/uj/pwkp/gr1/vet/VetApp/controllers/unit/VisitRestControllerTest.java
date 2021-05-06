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
import uj.pwkp.gr1.vet.VetApp.controller.rest.VisitsRestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.ClientRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.VisitRequest;
import uj.pwkp.gr1.vet.VetApp.entity.*;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.ObjectNotFoundVetAppException;
import uj.pwkp.gr1.vet.VetApp.service.VisitCreationResult;
import uj.pwkp.gr1.vet.VetApp.service.VisitService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(VisitsRestController.class)
public class VisitRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VisitService visitService;

    @Test
    public void givenVisits_whenGetAllVisits_thenReturnJsonArray() throws Exception {
        Visit visit = Visit.builder()
                .id(1)
                .startTime(null)
                .duration(null)
                .status(Status.PLANNED)
                .price(BigDecimal.ZERO)
                .animal((new Animal(1, null, null, null, null)))
                .client(null)
                .vet(null)
                .office(null)
                .build();
        List<Visit> allVisits = Collections.singletonList(visit);
        given(visitService.getAllVisits()).willReturn(allVisits);
        String uri = "/api/visits/all";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(visit.getId())));
    }

    @Test
    public void givenVisitId_whenGetVisitById_thenReturnJsonWithOk() throws Exception {
        Visit visit = Visit.builder()
                .id(1)
                .startTime(null)
                .duration(null)
                .status(Status.PLANNED)
                .price(BigDecimal.ZERO)
                .animal((new Animal(1, null, null, null, null)))
                .client(new Client(1, null, null))
                .vet(new Vet(1, null, null, null, null, null, null))
                .office(new Office(1, null))
                .build();
        given(visitService.getVisitById(1)).willReturn(visit);
        String uri = "/api/visits/1";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(visit.getId())));
    }

    @Test
    public void givenVisitWrongId_whenGetVisitById_thenReturnJsonWithNotFound() throws Exception {
        given(visitService.getVisitById(1)).willThrow(new ObjectNotFoundVetAppException("", VetAppResourceType.VISIT));
        String uri = "/api/visits/1";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenVisitRequest_whenCreateVisit_thenReturnJsonWithCreated() throws Exception {
        //given
        VisitRequest visitRequest = new VisitRequest(null, null, null, null,
                1, "special visits", 1, 1, 1);
        Visit visit = Visit.builder()
                .id(1)
                .startTime(null)
                .duration(null)
                .status(Status.PLANNED)
                .price(BigDecimal.ZERO)
                .description("special visits")
                .animal((new Animal(1, null, null, null, null)))
                .client(new Client(1, null, null))
                .vet(new Vet(1, null, null, null, null, null, null))
                .office(new Office(1, null))
                .build();
        given(visitService.createVisit(visitRequest)).willReturn(visit);
        String uri = "/api/visits/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(visitRequest))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains(visitRequest.getDescription()));
    }

    @Test
    public void givenWrongVisitRequest_whenCreateVisit_thenReturnJsonWithBadRequest() throws Exception {
        //given
        VisitRequest visitRequest = new VisitRequest(LocalDateTime.now(), Duration.ZERO, AnimalType.CAT, BigDecimal.ZERO,
                1, "special visits", 1, 1, 1);
        given(visitService.createVisit(visitRequest)).willThrow(new CreateVetAppException("", VetAppResourceType.VISIT));
        String uri = "/api/visits/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(visitRequest))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void givenVisitId_whenDeleteVisitById_thenReturnJsonWithOk() throws Exception {
        Visit visit = Visit.builder()
                .id(1)
                .startTime(null)
                .duration(null)
                .status(Status.PLANNED)
                .price(BigDecimal.ZERO)
                .animal((new Animal(1, null, null, null, null)))
                .client(new Client(1, null, null))
                .vet(new Vet(1, null, null, null, null, null, null))
                .office(new Office(1, null))
                .build();
        given(visitService.delete(1)).willReturn(visit);
        String uri = "/api/visits/delete/1";

        mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(visit.getId())));
    }

    @Test
    public void givenWrongVisitId_whenDeleteVisitById_thenReturnJsonWithNotFound() throws Exception {
        given(visitService.delete(1)).willThrow(new DeleteVetAppException("", VetAppResourceType.VISIT));
        String uri = "/api/visits/delete/1";

        mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
