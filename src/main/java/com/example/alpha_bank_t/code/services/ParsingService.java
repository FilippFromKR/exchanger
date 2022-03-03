package com.example.alpha_bank_t.code.services;

import com.example.alpha_bank_t.code.domains.CurrantCoursePB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ParsingService {
    @Value("${pb.url}")
    private String GET_USD;

    RestTemplate restTemplate;

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CurrantCoursePB[] parse() {
        return restTemplate.getForObject(GET_USD, CurrantCoursePB[].class);
    }
}
