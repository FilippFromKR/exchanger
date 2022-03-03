package com.example.alpha_bank_t.code.repositoryes;

import com.example.alpha_bank_t.code.dbEntityes.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency,Long > {

    public Currency findByCurrency(Currency currency);
}
