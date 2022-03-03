package com.example.alpha_bank_t.code.controllers;

import com.example.alpha_bank_t.code.dbEntityes.CurrantCourses;
import com.example.alpha_bank_t.code.dbEntityes.Operation;
import com.example.alpha_bank_t.code.domains.AmountForEachCurrency;
import com.example.alpha_bank_t.code.services.CourseService;
import com.example.alpha_bank_t.code.services.OperationService;
import com.example.alpha_bank_t.code.services.ParsingService;
import com.example.alpha_bank_t.code.domains.DateFilter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/exchanger")
@Api
public class OperationController {

    private ParsingService parsingService;

    private CourseService courseService;

    private OperationService operationService;


    @Autowired
    private void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    @Autowired
    private void createOperationService(CourseService operationsService) {
        this.courseService = operationsService;
    }

    @Autowired
    private void createsService(ParsingService parsingService) {
        this.parsingService = parsingService;
    }

    private static boolean correctTime(DateFilter twoDates) {
        String lowerTimeLimit = "2000-01-01T00:00:00.00Z";
        Date from = Date.from(Instant.parse(lowerTimeLimit));
        return twoDates.getFrom().compareTo(twoDates.getTo()) < 0
                && twoDates.getFrom().compareTo(from) > 0
                && twoDates.getTo().compareTo(Date.from(Instant.now())) <= 0;
    }


//
//    @RequestMapping(value = "/**", method = {
//            RequestMethod.POST,
//            RequestMethod.GET,
//            RequestMethod.DELETE,
//            RequestMethod.PATCH})
//    public ResponseEntity<String> notFound() {
//        return new ResponseEntity<>("Sorry, we didn't find anything", HttpStatus.NOT_FOUND);
//    }

    @ApiOperation(value = "Начало работы и получение текущего курса валют"
            ,response = CurrantCourses.class)
    @RequestMapping(value = "/startWork",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object[]> sendCurrantCourse() {
        courseService.saveCourse(parsingService.parse());
        CurrantCourses course = courseService.getCurrantCourse();
        if (course != null)
            return new ResponseEntity<>(course.getCurrencies().toArray(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Получение и валидация заявки на новую операцию." +
            "Если она пройдена, отправка сообщение с кодом сообщения на телефон пользователя " +
            "и результат операции в ответе."
            ,response = Operation.class)
    @PostMapping(value = "/newOperation",
            produces = "application/json")
    public ResponseEntity<Object> getNewOperation(@RequestBody Operation operation) {
        Operation statusActiveOperation = operationService.wholePreparationForImplementation(operation);
        if (statusActiveOperation.getExceptionMessage() != null)
            return new ResponseEntity<>(statusActiveOperation, HttpStatus.BAD_REQUEST);
        operationService.saveOperation(statusActiveOperation);
        return new ResponseEntity<>(statusActiveOperation, HttpStatus.OK);
    }

    @ApiOperation(value = "Удаление операции по Id",
    response = String.class)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteOperation(@PathVariable long id) {
        if (operationService.delete(id)) {
            return new ResponseEntity<>("Operation deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Operation not found or have Completed status", HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Подтверждение операции с помощью кода активации." +
            "если он верен уставновка статуса COMPLETED. ",
            response = String.class)
    @PatchMapping(value = "/complete")
    public ResponseEntity<String> setCompletedStatus(@RequestBody String activationCode) {
        if (operationService.saveCompleteOperation(activationCode))
            return new ResponseEntity<>("Operation has done", HttpStatus.OK);
        return new ResponseEntity<>("Wrong code", HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Завершение рабочего дня, получение всех операций за день и суммы по каждой валюте.",
    response = Map.class)
    @RequestMapping(value = "/finish",
            method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> finishWorkDay() {
        List<Operation> operations = operationService.findAllByThisDay();
        if (operations.size() > 0)
            return new ResponseEntity<>(Map.of("Operations", operations, "OperationSum", AmountForEachCurrency.counterSum(operations)), HttpStatus.OK);

        else
            return new ResponseEntity<>(Map.of("Operations", List.of(new Operation(Set.of("No operations today")))), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Получение операций в промежутке полученных дат и валюты(не обязательно).",
            response = List.class)
    @RequestMapping(value = "/filter",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Operation>> filterOperationByDate(@RequestBody DateFilter date) {

        if (correctTime(date)) {
            List<Operation> response = operationService.findByTwoDate(date.getFrom(), date.getTo(), date.getCurrency());
            if (response.size() > 0)
                return new ResponseEntity<>(response, HttpStatus.OK);
            else
                return new ResponseEntity<>(List.of(new Operation(Set.of("No Operations during this date"))), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(List.of(new Operation(Set.of("Dates should be during 2000-01-01 and today"))), HttpStatus.NOT_ACCEPTABLE);
    }

    @ApiOperation(value = "Получение операции с указанным ID.",
            response = Operation.class)
    @RequestMapping(value = "/filter/id/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Operation> filterBYId(@PathVariable long id) {
        Operation operation = operationService.findByID(id);
        if (operation != null)
            return new ResponseEntity<>(operation, HttpStatus.OK);
        return new ResponseEntity<>(new Operation(Set.of("Wrong id")), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Получение операций по указанному номеру телефона.",
            response = List.class)
    @RequestMapping(value = "/filter/{phoneNumber}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Operation>> filterOperationByPhone(@PathVariable String phoneNumber) {
        List<Operation> operations = operationService.findByPhoneNumber(phoneNumber);
        if (operations == null || operations.get(0).getExceptionMessage() != null)
            return new ResponseEntity<>(operations, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(operations, HttpStatus.OK);
    }
}
