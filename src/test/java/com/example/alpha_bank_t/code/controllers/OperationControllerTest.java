package com.example.alpha_bank_t.code.controllers;

import com.example.alpha_bank_t.code.dbEntityes.CurrantCourses;
import com.example.alpha_bank_t.code.dbEntityes.Currency;
import com.example.alpha_bank_t.code.dbEntityes.Operation;
import com.example.alpha_bank_t.code.domains.DateFilter;
import com.example.alpha_bank_t.code.services.CourseService;
import com.example.alpha_bank_t.code.services.OperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@Ignore
class OperationControllerTest {


    @MockBean
    private CourseService courseService;

    @MockBean
    private OperationService operationService;

    @Autowired
    private ObjectMapper mapper;


    private final String url = "/api/v1/exchanger";


    private MockMvc mockMvc;


    @Autowired
    private void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void notFound() throws Exception {

        this.mockMvc.perform(get(url + "/somethingElse"))
                .andDo(print())
                .andExpect(status()
                        .isNotFound())
                .andExpect(content().string(containsString("Sorry")));
    }


    @Test
    void sendCurrantCourse() throws Exception {
        CurrantCourses currantCourses = new CurrantCourses();
        currantCourses.setCurrencies(Set.of(new Currency("usd", 1.0, 2.0)));

        Mockito.doReturn(currantCourses)
                .when(courseService)
                .getCurrantCourse();

        this.mockMvc.perform(get(url + "/startWork"))
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void getMethod() throws Exception {

        Mockito.doReturn(new Operation())
                .when(operationService)
                .wholePreparationForImplementation(ArgumentMatchers.any());

        String json = mapper.writeValueAsString(new Operation());

        this.mockMvc.perform(post(url + "/newOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void deleteOperation() throws Exception {
        Mockito.doReturn(true)
                .when(operationService)
                .delete(1);

        this.mockMvc.perform(delete(url + "/delete/1"))
                .andExpect(status()
                        .isOk())
                .andDo(print())
                .andExpect(content().string(containsString("Operation")));
    }

    @Test
    void deleteOperationFalse() throws Exception {
        Mockito.doReturn(false)
                .when(operationService)
                .delete(1);

        this.mockMvc.perform(delete(url + "/delete/1"))
                .andExpect(status()
                        .isNotFound())
                .andDo(print())
                .andExpect(content().string(containsString("Operation")));
    }

    @Test
    void setCompletedStatus() throws Exception {
        Mockito.doReturn(true)
                .when(operationService)
                .saveCompleteOperation(ArgumentMatchers.any());

        String json = mapper.writeValueAsString("code");

        this.mockMvc.perform(patch(url + "/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void setCompletedStatusFalse() throws Exception {
        Mockito.doReturn(false)
                .when(operationService)
                .saveCompleteOperation(ArgumentMatchers.any());

        String json = mapper.writeValueAsString("code");

        this.mockMvc.perform(patch(url + "/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void finishWorkDay() throws Exception {
        Operation operation = new Operation();
        operation.setCurrencyToSell("USD");
        operation.setCurrencyToBuy("EUR");
        operation.setAmount(10.0);
        operation.setMoneyToIssued(24.0);
        Operation operation2 = new Operation();
        operation2.setCurrencyToSell("USD");
        operation2.setCurrencyToBuy("EUR");
        operation2.setAmount(10.0);
        operation2.setMoneyToIssued(24.0);

        Mockito.doReturn(List.of(operation, operation2))
                .when(operationService)
                .findAllByThisDay();

        this.mockMvc.perform(get(url + "/finish"))
                .andExpect(status()
                        .isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void filterOperationByDate() throws Exception {

        DateFilter filter = new DateFilter();
        filter.setFrom(Date.valueOf("2022-02-02"));
        filter.setTo(Date.valueOf("2022-03-01"));

        Mockito.doReturn(List.of(new Operation(), new Operation()))
                .when(operationService)
                .findByTwoDate(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());

        String json = mapper.writeValueAsString(filter);

        this.mockMvc.perform(post(url + "/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void filterOperationByDateNotAcceptable() throws Exception {

        DateFilter filter = new DateFilter();
        filter.setFrom(Date.valueOf("1999-02-02"));
        filter.setTo(Date.valueOf("2022-03-03"));

        Mockito.doReturn(List.of(new Operation(), new Operation()))
                .when(operationService)
                .findByTwoDate(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());

        String json = mapper.writeValueAsString(filter);

        this.mockMvc.perform(post(url + "/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotAcceptable())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void filterBYId() throws Exception {
        Mockito.doReturn(new Operation())
                .when(operationService)
                .findByID(1);

        this.mockMvc.perform(get(url + "/filter/id/1"))
                .andExpect(status()
                        .isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void filterBYIdNotFound() throws Exception {

        Mockito.doReturn(null)
                .when(operationService)
                .findByID(1);

        this.mockMvc.perform(get(url + "/filter/id/1"))
                .andExpect(status()
                        .isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void filterOperationByPhone() throws Exception {
        String phoneNumber = "+380950000000";

        Mockito.doReturn(List.of(new Operation(), new Operation()))
                .when(operationService)
                .findByPhoneNumber(phoneNumber);

        this.mockMvc.perform(get(url + "/filter/" + phoneNumber))
                .andExpect(status()
                        .isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void filterOperationByPhoneNotFound() throws Exception {
        String phoneNumber = "+38095";
        Operation operation = new Operation();
        operation.setExceptionMessage(Set.of("exception"));

        Mockito.doReturn(List.of(operation))
                .when(operationService)
                .findByPhoneNumber(phoneNumber);

        this.mockMvc.perform(get(url + "/filter/" + phoneNumber))
                .andExpect(status()
                        .isNotFound())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}