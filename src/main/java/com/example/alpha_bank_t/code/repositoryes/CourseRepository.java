package com.example.alpha_bank_t.code.repositoryes;

import com.example.alpha_bank_t.code.dbEntityes.CurrantCourses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface CourseRepository extends JpaRepository<CurrantCourses, Long> {

    CurrantCourses findByDate(Date date);


}
