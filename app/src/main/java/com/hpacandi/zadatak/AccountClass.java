package com.hpacandi.zadatak;


import java.io.Serializable;

public class AccountClass implements Serializable {

    private int accountID;
    private String IBAN;
    private String amount;
    private String currency;

    void setAccountID(int accountID)
    {
        this.accountID = accountID;
    }

    public Integer getAccountID()
    {
        return accountID;
    }

    void setIBAN(String IBAN)
    {
        this.IBAN = IBAN;
    }

    public String getIBAN()
    {
        return IBAN;
    }

    void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getAmount()
    {
        return amount;
    }

    void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getCurrency()
    {
        return currency;
    }

}
