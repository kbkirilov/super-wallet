package com.superwallet.models;

import jakarta.persistence.*;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private int currencyId;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "country")
    private String country;

    public Currency() {
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
