package com.hoffmans.rush.utils;

import java.util.regex.Pattern;

/**
 * Created by amandeep on 16/10/15.
 */
public class Validation {

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }

        return (password.trim().length() >= 4) ? true : false;

    }
    public static boolean isMatchedPassword(String firstPassword, String secondPassword) {
        return (firstPassword.trim().equals(secondPassword)) ? true : false;
    }


    public static boolean isValidMobile(String mobilenumber) {
        return Pattern.matches("^[+][0-9]{11,13}$", mobilenumber);
//        return (android.util.Patterns.PHONE.matcher(mobilenumber).matches()) ? true : false;
    }


    public static boolean isValidLengthMobile(String mobilenumber) {
        return Pattern.matches("^[0-9]{10,10}$", mobilenumber);
    }


    /**
     *
     * @param cardNumber card number of user
     * @return is card valid
     */
    public static  boolean isValidCreditCard(String cardNumber) {
        if (cardNumber.toString().length() == 15||cardNumber.toString().length() == 16||cardNumber.toString().length() == 17 ) {
            return  true;
        } else {
            return false;
        }
    }
}
