package com.example.alpha_bank_t.code.services;

import com.example.alpha_bank_t.code.dbEntityes.CurrantCourses;
import com.example.alpha_bank_t.code.domains.CurrantCoursePB;
import com.example.alpha_bank_t.code.repositoryes.CourseRepository;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
class CourseServiceTest {
    private CourseService service;

    @MockBean
    private CourseRepository repository;

    @Autowired
    private void setCourseService(CourseService service) {
        this.service = service;
    }

    @Test
    void getCurrantCourse() {
        Mockito.doReturn(new CurrantCourses())
                .when(repository)
                .findByDate(ArgumentMatchers.any());
        CurrantCourses currantCourses = service.getCurrantCourse();
        Assert.assertNotNull(currantCourses);

    }

    @Test
    void saveCourse() {
        CurrantCoursePB[] coursePB = {new CurrantCoursePB("USD", "UAH", 1.0, 1.5),
                new CurrantCoursePB("EUR", "UAH", 1.5, 2.0)};
        service.saveCourse(coursePB);
        Mockito.verify(repository, Mockito.times(1))
                .save(ArgumentMatchers.any());
    }

    @Test
    void saveCourseAlreadyExist() {
        CurrantCoursePB[] coursePB = {new CurrantCoursePB("USD", "UAH", 1.0, 1.5),
                new CurrantCoursePB("EUR", "UAH", 1.5, 2.0)};
        Mockito.doReturn(new CurrantCourses())
                .when(repository)
                .findByDate(ArgumentMatchers.any());
        service.saveCourse(coursePB);
        Mockito.verify(repository, Mockito.times(0))
                .save(ArgumentMatchers.any());
    }
}