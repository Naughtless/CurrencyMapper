package main.java.process.common;

public class Match
{
    // Header
    private String header;
    
    // Original Payment Data entry.
    private PaymentData paymentData;
    
    // "Match" data, more flexible.
    private String response;
    private String audValue;
    private String[] infoDump;
}
