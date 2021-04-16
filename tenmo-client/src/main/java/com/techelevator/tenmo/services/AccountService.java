package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();


    public AccountService(String url) {
        this.BASE_URL = url;
    }


    public BigDecimal getBalance(String token){
         return restTemplate.exchange(BASE_URL+"balance", HttpMethod.GET, makeAuthEntity(token),
                                                    BigDecimal.class).getBody();

    }

    public User[] listUsers(String token){
        return restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(token),
                User[].class).getBody();
    }


    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
