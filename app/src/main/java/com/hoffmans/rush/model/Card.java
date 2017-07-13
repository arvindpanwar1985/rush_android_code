package com.hoffmans.rush.model;

/**
 * Created by devesh on 22/12/16.
 */

public class Card {
    private  String cardNumber;
    private String cardExpiry;
    private String cardCvv;
    private String countryCard;
    private String cityCard;
    private String cardHeadline;
    private String cardExpirationMonth;
    private String cardExpirationYear;

    public String getCardExpirationMonth() {
        return cardExpirationMonth;
    }

    public void setCardExpirationMonth(String cardExpirationMonth) {
        this.cardExpirationMonth = cardExpirationMonth;
    }

    public String getCardExpirationYear() {
        return cardExpirationYear;
    }

    public void setCardExpirationYear(String cardExpirationYear) {
        this.cardExpirationYear = cardExpirationYear;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    private String cardType;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public String getCountryCard() {
        return countryCard;
    }

    public void setCountryCard(String countryCard) {
        this.countryCard = countryCard;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    public String getCityCard() {
        return cityCard;
    }

    public void setCityCard(String cityCard) {
        this.cityCard = cityCard;
    }

    public String getCardHeadline() {
        return cardHeadline;
    }

    public void setCardHeadline(String cardHeadline) {
        this.cardHeadline = cardHeadline;
    }
}
