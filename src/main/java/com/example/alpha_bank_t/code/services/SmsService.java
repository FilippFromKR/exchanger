package com.example.alpha_bank_t.code.services;

import com.example.alpha_bank_t.code.staticClasses.SmsCreate;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SmsService {

    @Value("${account.sid}")
    private String ACCOUNT_SID;

    @Value("${auth.token}")
    private String AUTH_TOKEN;

    @Value("${phone_number}")
    private String FROM_NUMBER;

    public String send(SmsCreate sms) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        try {
            Message message = Message.creator(new PhoneNumber(sms.getNumberOfRecipient()), new PhoneNumber(FROM_NUMBER), sms.getMessage())
                    .create();
            return message.getBody();
        } catch (com.twilio.exception.ApiException ex) {
            return ex.getMoreInfo();
        }
    }

    public MultiValueMap<String, String> receive(MultiValueMap<String, String> smsCallback) {
        smsCallback.forEach((key, value) -> System.out.println("key " + key + " \n" +
                "value " + value));
        return smsCallback;
    }

}



