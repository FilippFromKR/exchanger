package com.example.alpha_bank_t.code.services;

import com.example.alpha_bank_t.code.dbEntityes.Operation;
import com.example.alpha_bank_t.code.enums.Status;
import com.example.alpha_bank_t.code.repositoryes.OperationRepository;
import com.example.alpha_bank_t.code.staticClasses.SmsCreate;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
class OperationServiceTest {

    private OperationService operationService;

    @MockBean
    private OperationRepository operationRepository;

    @MockBean
    private SmsService smsService;

    @Autowired
    public void setOperation(OperationService operationService) {
        this.operationService = operationService;
    }

    @Test
    public void saveCompleteOperation() {
        Operation operation = new Operation();
        operation.setStatus(Status.ACTIVE.getStatus());

        Mockito.doReturn(operation)
                .when(operationRepository)
                .findByActivationCode(ArgumentMatchers.any());

        Assert.assertTrue(operationService.saveCompleteOperation("code"));
        Assert.assertTrue(operation.getStatus().equals(Status.COMPLETED.getStatus()));
        Assert.assertTrue(operation.getActivationCode().equals(""));

    }

    @Test
    public void saveCompleteOperationFalse() {
        Operation operation = new Operation();
        operation.setStatus(Status.COMPLETED.getStatus());
        Mockito.doReturn(operation)
                .when(operationRepository)
                .findByActivationCode(ArgumentMatchers.any());
        Assert.assertFalse(operationService.saveCompleteOperation("code"));
    }


    @Test
    void findByTwoDate() {
        Date from = Date.valueOf("2022-02-02");
        Date to = Date.valueOf("2022-02-25");
        Mockito.doReturn(List.of(new Operation()))
                .when(operationRepository)
                .findAllByDateOfOperationBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
        List<Operation> result = operationService.findByTwoDate(from, to, null);
        Assert.assertNull(result.get(0).getExceptionMessage());
        Mockito.verify(operationRepository, Mockito.times(1))
                .findAllByDateOfOperationBetween(ArgumentMatchers.any(), ArgumentMatchers.any());

    }

    @Test
    void findByActivationCode() {
        String activationCode = "12456";
        Mockito.doReturn(new Operation())
                .when(operationRepository)
                .findByActivationCode(activationCode);
        Assert.assertNotNull(operationService.findByActivationCode(activationCode));
    }

    @Test
    void findByPhoneNumber() {
        String phone = "+380950000000";
        operationService.findByPhoneNumber(phone);
        Mockito.verify(operationRepository, Mockito.times(1)).findByCustomerPhoneNumber(phone);

        Mockito.doReturn(List.of(new Operation()))
                .when(operationRepository)
                .findByCustomerPhoneNumber(phone);
        List<Operation> operations = operationService.findByPhoneNumber(phone);
        Assert.assertNotNull(operations);
    }

    @Test
    void findByPhoneNumberWrongNumber() {
        String phone = "+00000000";
        Assert.assertTrue(operationService.findByPhoneNumber(phone).iterator().next().getExceptionMessage() != null);
    }


    @Test
    void setCorrectNumber() {
        String correct = operationService.setCorrectNumber("095 053 24-00");
        Assert.assertTrue(correct.equals("+380950532400"));

    }

    @Test
    void wholePreparationForImplementation() {
        Operation operation = new Operation();

        operation.setCustomerPhoneNumber("+380950000000");
        operation.setAmount(2.0);
        operation.setCurrencyToSell("USD");
        operation.setCurrencyToBuy("EUR");

        operation = operationService.wholePreparationForImplementation(operation);

        Assertions.assertNotNull(operation.getStatus());
        Assert.assertNotNull(operation.getActivationCode());
        Assert.assertTrue(Objects.equals(operation.getDateOfOperation(), Date.valueOf(LocalDate.now())));


        SmsCreate sms = new SmsCreate(operation.getCustomerPhoneNumber());
        sms.setSms(operation.getActivationCode());
        Mockito.verify(smsService, Mockito.times(1)).send(sms);
    }

    @Test
    void wholePreparationForImplementationEx() {
        Operation operation = operationService.wholePreparationForImplementation(new Operation());
        Assert.assertNull(operation.getStatus());
        Assert.assertNotNull(operation.getExceptionMessage());
    }


}