package com.hoffmans.rush.model;

/**
 * Created by devesh on 22/12/16.
 */

public class Card {
    private  String cardNumber,cardExpiry,cardCvv,countryCard,cityCard,cardHeadline;

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
