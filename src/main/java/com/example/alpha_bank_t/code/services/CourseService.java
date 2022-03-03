package com.example.alpha_bank_t.code.services;

import com.example.alpha_bank_t.code.dbEntityes.CurrantCourses;
import com.example.alpha_bank_t.code.dbEntityes.Currency;
import com.example.alpha_bank_t.code.domains.CurrantCoursePB;
import com.example.alpha_bank_t.code.repositoryes.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Set;

@Service
public class CourseService {

    private CourseRepository repository;

    private CurrencyService currencyService;


    @Autowired
    private void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    void setRepository(CourseRepository repository) {
        this.repository = repository;
    }


    public CurrantCourses getCurrantCourse() {
        return repository.findByDate(Date.from(Instant.now()));
    }

    public void saveCourse(CurrantCoursePB[] coursesPB) {
        Set<Currency> currencies = currencyService.setListOfCourses(coursesPB);
        CurrantCourses currantCourses = repository.findByDate(Date.from(Instant.now()));
        if (currantCourses == null) {
            currantCourses = new CurrantCourses();
            currantCourses.setCurrencies(currencies);
            currantCourses.setDate(Date.from(Instant.now()));
            repository.save(currantCourses);
        }
    }


}

