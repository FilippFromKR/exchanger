package com.example.alpha_bank_t.code.services;

import com.example.alpha_bank_t.code.domains.CurrantCoursePB;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
class ParsingServiceTest {

    private RestTemplate restTemplate;
    @Value("${pb.url}")
    private String url;

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    void parse() {
        CurrantCoursePB[] coursePB = restTemplate.getForObject(url,CurrantCoursePB[].class);
        Assert.assertNotNull(coursePB);

    }
}