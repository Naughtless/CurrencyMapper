package main.java.process.afterpay.model;

public class AfterpayRate
{
    private double fee_percentage;
    private double fee_flat;
    private double gst;

    public AfterpayRate(double fee_percentage, double fee_flat, double gst)
    {
        this.fee_percentage = fee_percentage;
        this.fee_flat = fee_flat;
        this.gst = gst;
    }

    public double calculateNet(double amount) {
        // Formula fixed as of 22/04/2025
        return amount - (((amount * fee_percentage) + fee_flat) * (1 + gst));
    }
}
