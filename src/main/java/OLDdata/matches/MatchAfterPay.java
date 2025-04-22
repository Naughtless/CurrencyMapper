package main.java.OLDdata.matches;

import main.java.common.CSV;
import main.java.read.masterdata.PaymentData;

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
