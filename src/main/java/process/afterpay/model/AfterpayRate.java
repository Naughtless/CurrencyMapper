package main.java.process.afterpay.model;

public class AfterpayRate
{
    private double rate;
    private double gst;

    public AfterpayRate(double rate, double gst)
    {
        this.rate = rate;
        this.gst = gst;
    }
    
    public double calculateNet(double amount) {
        /*
         * THIS IS VERY LIKELY WRONG.
         */
        return amount - gst - (((amount * rate) + 0.3) + ((amount * rate) + 0.3) * gst);
    }
}
