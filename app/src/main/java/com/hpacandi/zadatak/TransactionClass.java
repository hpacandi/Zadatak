package com.hpacandi.zadatak;


import java.io.Serializable;
import java.util.Date;

public class TransactionClass implements Serializable {

    private int transID;
    private Date transDate;
    private String transDesc;
    private String transAmount;
    private int accountID;

    void setTransactionID(int transID)
    {
        this.transID = transID;
    }

    public Integer getTransactionID()
    {
        return transID;
    }

    void setDate(Date transDate)
    {
        this.transDate = transDate;
    }

    public Date getDate()
    {
        return transDate;
    }

    void setDescription(String transDesc)
    {
        this.transDesc = transDesc;
    }

    public String getDescription()
    {
        return transDesc;
    }

    void setAmount(String transAmount)
    {
        this.transAmount = transAmount;
    }

    public String getTransAmount()
    {
        return transAmount;
    }

    void setAccountID(int accountID)
    {
        this.accountID = accountID;
    }

    public Integer getAccountID()
    {
        return accountID;
    }

}
