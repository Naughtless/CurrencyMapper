package main.java.common;

import main.java.process.exchangerate.model.ExchangeRate;
import main.java.process.paypal.model.PayPal;

public class Match
{
    // Header
    private String header;

    // Original Payment Data entry.
    private PaymentData paymentData;
    private String      response;

    // "Match" data, more flexible.
    private String audValue;
    private String[] infoDump;


    public Match()
    {}

    // Constructor: Variable...
    public Match(
            PaymentData paymentData,
            String response,
            String audValue,
            String[] infoDump
    )
    {
        this.paymentData = paymentData;
        this.response = response;
        this.audValue = audValue;
        this.infoDump = infoDump;
    }

    // Constructor: PayPal
    public Match(PaymentData paymentData, PayPal paypal, String response, boolean refund)
    {
        this.paymentData = paymentData;
        this.response = response;

        this.audValue = refund ? "0" : paypal.getGross();

        this.infoDump = new String[]{
                paypal.getDate(), 
                paypal.getName(), 
                paypal.getStudentId()
        };
    }


    // Constructor: ExchangeRate
    public Match(
            PaymentData paymentData,
            ExchangeRate exchangeRate,
            String response
    )
    {
        this.paymentData = paymentData;
        this.response = response;
        
        this.audValue = String.valueOf(
                Double.parseDouble(paymentData.getAmount())
                / Double.parseDouble(exchangeRate.getRate())
        );
        
        this.infoDump = new String[]{
                exchangeRate.getSource(), 
                exchangeRate.getCurrency(),
                exchangeRate.getRate()
        };
    }

    //<editor-fold desc="Getters & Setters">
    public PaymentData getPaymentData()
    {
        return paymentData;
    }

    public void setPaymentData(PaymentData paymentData)
    {
        this.paymentData = paymentData;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }

    public String getAudValue()
    {
        return audValue;
    }

    public void setAudValue(String audValue)
    {
        this.audValue = audValue;
    }

    public String[] getInfoDump()
    {
        return infoDump;
    }

    public void setInfoDump(String[] infoDump)
    {
        this.infoDump = infoDump;
    }
    //</editor-fold>
}
