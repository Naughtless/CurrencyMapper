package main.java.common;

import main.java.process.exchangerate.model.ExchangeRate;
import main.java.process.paypal.model.PayPal;

public class Match implements CSV
{
    // Header
    public static final String header = PaymentData.header + "|RESPONSE|AUD VALUE|INFO DUMP|d1|d2|d3|d4|d5|d6|d7|d8|d9|d0";

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
    
    @Override
    public String buildCSVLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.paymentData.buildCSVLine()).append("|");
        sb.append(this.response).append("|");
        sb.append(this.audValue).append("|");
        for(int i = 0; i < infoDump.length; i++)
        {
            sb.append(infoDump[i]).append("|");
        }
        
        return sb.toString();
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
