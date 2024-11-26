package main.data.matches;

import main.data.exchangerate.ExchangeRate;
import main.data.interfaces.CSV;
import main.data.paymentdata.PaymentData;

public class MatchPDExc implements CSV {
    private PaymentData paymentData;
    private ExchangeRate exchangeRate;

    public MatchPDExc() {}
    public MatchPDExc(PaymentData paymentData, ExchangeRate exchangeRate) {
        this.paymentData = paymentData;
        this.exchangeRate = exchangeRate;
    }

    public String buildCSVLine() {
        if(exchangeRate != null) {
            double originalAmount = Double.parseDouble(paymentData.getAmount());
            double exchangeRate = Double.parseDouble(this.exchangeRate.getRate());
            double convertedValue = (originalAmount / exchangeRate);

            return (paymentData.buildCSVLine() + "|" + convertedValue
                    + "|" + this.exchangeRate.getSource()
                    + "|" + this.exchangeRate.getCurrency()
                    + "|" + this.exchangeRate.getRate()
            );
        }
        else {
            double originalAmount = Double.parseDouble(paymentData.getAmount());
            double gst = Double.parseDouble(paymentData.getGst());

            originalAmount -= gst;

            return (paymentData.buildCSVLine() + "|" + originalAmount + "|Already AUD!");
        }
    }
}
