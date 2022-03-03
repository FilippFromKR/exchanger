package com.example.alpha_bank_t.code.repositoryes;

import com.example.alpha_bank_t.code.dbEntityes.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long > {
    public void delete(Operation operation);

    public Operation findByActivationCode(String activationCode);

    public List<Operation > findAllByDateOfOperation(Date date);

    public List<Operation >findAllByDateOfOperationBetween(Date from,Date to);

    public List<Operation> findByCustomerPhoneNumber(String phoneNumber);


}
