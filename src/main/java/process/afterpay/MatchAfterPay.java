package main.java.process.afterpay;

import main.java.process.common.CSV;
import main.java.process.common.PaymentData;

public class MatchAfterPay implements CSV {
    private PaymentData paymentData;
    private double aud;

    public MatchAfterPay() {}
    public MatchAfterPay(PaymentData paymentData, double aud) {
        this.paymentData = paymentData;
        this.aud = aud;
    }

    public String buildCSVLine() {
        return paymentData.buildCSVLine() + "|" + aud;
    }
}
