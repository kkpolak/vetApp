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
import uj.pwkp.gr1.vet.VetApp.controller.rest.OfficeRestController;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.OfficeRequest;
import uj.pwkp.gr1.vet.VetApp.entity.Office;
import uj.pwkp.gr1.vet.VetApp.service.OfficeService;

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
@WebMvcTest(OfficeRestController.class)
public class OfficeRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OfficeService officeService;

    @Test
    public void givenOffices_whenGetAllOffices_thenReturnJsonArray() throws Exception {
        Office office = Office.builder()
                .id(1)
                .name("office")
                .build();
        List<Office> allOffices = Collections.singletonList(office);
        given(officeService.getAllOffices()).willReturn(allOffices);
        String uri = "/api/offices/all";

        mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(office.getName())));

    }

    @Test
    public void givenOffices_whenGetOfficeById_thenReturnJsonWithOk() throws Exception {
        //given
        Office office = Office.builder()
                .id(1)
                .name("office")
                .build();
        given(officeService.getOffcieById(1)).willReturn(Optional.ofNullable(office));
        String uri = "/api/offices/1";

        //when
        var result = mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(office.getName())));
    }

    @Test
    public void givenOffices_whenGetOfficeByWrongId_thenReturnJsonWithNotFound() throws Exception {
        //given
        given(officeService.getOffcieById(1)).willReturn(Optional.empty());
        String uri = "/api/offices/1";

        //when
        var result = mvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNotFound());
    }

    //testy create'ow dziala po wyrzuceniu finalnego pola z OfficeRequest i dodania konstruktorow
    @Test
    public void givenOfficeRequest_whenCreateOffice_thenReturnJsonWithCreated() throws Exception {
        //given
        OfficeRequest officeRequest = new OfficeRequest("office");
        Office office = Office.builder()
                .id(1)
                .name("office")
                .build();
        Either<String, Office> res = new Either<String, Office>() {
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
            public Office get() {
                return office;
            }

            @Override
            public String stringPrefix() {
                return null;
            }
        };
        given(officeService.createOffice(officeRequest)).willReturn(res);
        String uri = "/api/offices/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content("{\"name\":\"office\"}")//mapper.writeValueAsString(officeRequest))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains(officeRequest.getName()));
    }

    @Test
    public void givenOfficeRequest_whenCreateOffice_thenReturnJsonWithBadRequest() throws Exception {
        //given
        OfficeRequest officeRequest = new OfficeRequest("office");
        Either<String, Office> res = new Either<String, Office>() {
            @Override
            public String getLeft() {
                return "Office creation error";
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
            public Office get() {
                return null;
            }

            @Override
            public String stringPrefix() {
                return null;
            }
        };
        given(officeService.createOffice(officeRequest)).willReturn(res);
        String uri = "/api/offices/create";
        ObjectMapper mapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri)
                .content(mapper.writeValueAsString(officeRequest))
                .contentType(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().equals("Office creation error"));
    }

    @Test
    public void givenOfficeId_whenDeleteOffice_thenReturnJsonWithOk() throws Exception {
        //given
        Office office = Office.builder()
                .id(1)
                .name("office")
                .build();
        given(officeService.deleteOffice(1)).willReturn(Optional.ofNullable(office));
        String uri = "/api/offices/delete/1";

        //when
        var result = mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(office.getName())));
    }

    @Test
    public void givenOfficeId_whenDeleteOffice_thenReturnJsonWithNotFound() throws Exception {
        //given
        given(officeService.deleteOffice(1)).willReturn(Optional.empty());
        String uri = "/api/offices/delete/1";

        //when
        var result = mvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNotFound());
    }
}
