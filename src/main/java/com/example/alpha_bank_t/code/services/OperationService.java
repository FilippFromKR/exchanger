package com.example.alpha_bank_t.code.services;

import com.example.alpha_bank_t.code.dbEntityes.Operation;
import com.example.alpha_bank_t.code.repositoryes.OperationRepository;
import com.example.alpha_bank_t.code.staticClasses.CalculatorCourse;
import com.example.alpha_bank_t.code.staticClasses.CorrectPhoneNumber;
import com.example.alpha_bank_t.code.staticClasses.SmsCreate;
import com.example.alpha_bank_t.code.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class OperationService {

    private OperationRepository operationRepository;
    private SmsService smsService;
    private CurrencyService currencyService;


    @Autowired
    private void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    private void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }


    @Autowired
    private void setOperationRepository(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public boolean delete(long id) {
        if (operationRepository.findById(id).isPresent()) {
            Operation operation = operationRepository.findById(id).get();
            if (operation.getStatus().equals(Status.ACTIVE.getStatus())) {
                operationRepository.delete(operation);
                return true;
            }
        }
        return false;
    }


    public boolean saveCompleteOperation(String activationCode) {
        Operation operation = findByActivationCode(activationCode);
        if (operation != null && operation.getStatus().equals(Status.ACTIVE.getStatus())) {
            operation.setStatus(Status.COMPLETED.getStatus());
            operation.setActivationCode("");
            saveOperation(operation);
            return true;
        }
        return false;
    }

    public List<Operation> findAllByThisDay() {
        return operationRepository.findAllByDateOfOperation(Date.from(Instant.now()));
    }

    public Operation findByID(long id) {
        return operationRepository.findById(id).orElse(null);
    }

    public List<Operation> findByTwoDate(Date from, Date to, @Nullable String currency) {
        List<Operation> operations = operationRepository.findAllByDateOfOperationBetween(from, to);
        if (currency != null)
            return operations.stream().filter(operation ->
                    operation.getCurrencyToSell().equals(currency)).collect(Collectors.toList());
        return operations;
    }

    public Operation findByActivationCode(String activationCode) {
        return operationRepository.findByActivationCode(activationCode);
    }

    private String setActivationCode(Operation operation) {
        operation.setActivationCode(UUID.randomUUID().toString());
        return operation.getActivationCode();
    }

    private double getAmountOfMoneyToIssued(Operation operation) {
        CalculatorCourse calculatorCourse = new CalculatorCourse(operation, currencyService.getCurrencies());
        return calculatorCourse.calculateAmountOfMoneyToIssued();
    }

    private Operation sendMessage(Operation operation) {
        String code = setActivationCode(operation);
        SmsCreate sms = new SmsCreate(operation.getCustomerPhoneNumber());
        sms.setSms(code);
        smsService.send(sms);
        return operation;
    }

    public List<Operation> findByPhoneNumber(String phoneNumber) {
        String correctNumber = CorrectPhoneNumber.formatIfCorrect(phoneNumber);
        if (correctNumber.equals("WRONG"))
            return List.of(new Operation(Set.of("WRONG number format,example +38-000-000-00-00")));

        List<Operation> operations = operationRepository.findByCustomerPhoneNumber(correctNumber);

        if (operations.size() == 0)
            return List.of(new Operation(Set.of("No Operations on this number")));
        return operations;
    }

    public String setCorrectNumber(String phoneNumber) {
        return CorrectPhoneNumber.formatIfCorrect(phoneNumber);
    }

    private Set<String> getErrors(Operation operation) {
        Set<String> errorMessages = new HashSet<>();

        if (operation.getAmount() <= 0)
            errorMessages.add("Amount should be more then 0");

        if (setCorrectNumber(operation.getCustomerPhoneNumber()
        ).equals("WRONG"))
            errorMessages.add("Format number should be +38-095-000-00-00");

        if (operation.getCurrencyToBuy() == null || operation.getCurrencyToSell() == null)
            errorMessages.add("Currency can't be null");

        return errorMessages;

    }


    public Operation wholePreparationForImplementation(Operation operation) {
        Set<String> errors = getErrors(operation);
        if (errors.size() > 0) {
            operation.setExceptionMessage(errors);
            return operation;
        }
        operation.setStatus(Status.ACTIVE.getStatus());
        operation.setDateOfOperation(Date.valueOf(LocalDate.now()));
        sendMessage(operation);
        operation.setMoneyToIssued(getAmountOfMoneyToIssued(operation));

        return operation;
    }

    public void saveOperation(Operation operation) {
        operationRepository.save(operation);

    }
}