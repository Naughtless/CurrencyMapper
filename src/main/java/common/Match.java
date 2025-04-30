package main.java.common;

import main.java.process.paypal.model.PaypalGrouped;

public class Match
{
    // Header
    private String header;

    // Original Payment Data entry.
    private PaymentData paymentData;
    private String response;
    
    // "Match" data, more flexible.
    private String audValue;
    private String[] infoDump;
    
    
    public Match()
    {}
    
    // Constructor: PayPal
    public Match(PaymentData paymentData,  Paypal paypal, String response, boolean refund) {
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
