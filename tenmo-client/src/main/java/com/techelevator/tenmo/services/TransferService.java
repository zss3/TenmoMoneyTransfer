package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();


    public TransferService(String url) {
        this.BASE_URL = url;
    }


    public Transfer[] getTransferHistory(String token){
        return restTemplate.exchange(BASE_URL+"transfers/view", HttpMethod.GET, makeAuthEntity(token),
               Transfer[].class).getBody();
    }

    public Transfer getTransferDetails(String token, int transferID){
        return restTemplate.exchange(BASE_URL+"transfer/" + transferID, HttpMethod.GET, makeAuthEntity(token),
                Transfer.class).getBody();
    }

    public String makeTransfer(String token, Long userFromId, Long userToID, BigDecimal amount){
        TransferDTO transferDTO = new TransferDTO(userFromId,userToID,amount);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<TransferDTO> entity = new HttpEntity<>(transferDTO, headers);

        return restTemplate.exchange(BASE_URL+"transfer", HttpMethod.POST, entity, String.class).getBody();
    }


    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }



}
