package com.example.alpha_bank_t.code.staticClasses;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CorrectPhoneNumber {


    private static boolean ukrOperatorCheck(String phoneNumber) {
        List<String> operatorCodes = List.of("067", "096", "097", "098", "050", "066", "095", "099", "063", "073", "093", "091", "092", "094");
        String includingOperatorCodeCheck = phoneNumber.toCharArray()[0] == '3' ? phoneNumber.substring(2, 5) : phoneNumber.substring(0, 3);
        return operatorCodes.contains(includingOperatorCodeCheck);
    }

    private static boolean lenghtAndDigitCheck(String phoneNumber) {
        return phoneNumber.matches("[\\d]+") && phoneNumber.length() >= 7 && phoneNumber.length() <= 13;
    }

    private static String formatNumber(String phoneNumber) {
        String countryCodeCheck = phoneNumber.substring(0, 2);
        String result = "+38";
        if (countryCodeCheck.equals("38"))
            result = phoneNumber.replace("38", result);
        else result += phoneNumber;
        return result;
    }

    public static String formatIfCorrect(String phoneNumber) {
        if (phoneNumber == null)
            return "WRONG";
        phoneNumber = phoneNumber.replace("+", "");
        phoneNumber = phoneNumber.replace("-", "");
        phoneNumber = phoneNumber.replace(" ", "");
        if (!lenghtAndDigitCheck(phoneNumber))
            return "WRONG";
        if (!ukrOperatorCheck(phoneNumber)) {
            return "WRONG";
        }
        return formatNumber(phoneNumber);
    }

}
