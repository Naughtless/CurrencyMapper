package main.java.OLDdata.matches;

import main.java.read.externaldata.ExchangeRate;
import main.java.common.CSV;
import main.java.read.masterdata.PaymentData;

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
//            // DEBUG
//            System.out.println(paymentData.getAmount());
//            // DEBUG
            
            /*
             * TAMBAL SULAM:
             * Data disini suka jelek. jadi "paymentData.getAmount()" returns a string of some shit like " -  ",
             * which obviously fails to read!
             * 
             * Jadi ditambal sulam:
             * First remove comma! to make sure parse failure tidak dikarenakan koma "," -> thousands separator.
             * If parse double attempt fails, then assume 0.
             */
            String originalAmount_ts = paymentData.getAmount().trim().replaceAll(",", "");
            double originalAmount = 0;
            try {
                // If parse fails, then assume 0.
                originalAmount = Double.parseDouble(paymentData.getAmount().trim());
            }
            catch(NumberFormatException e) {
                // Set to 0.
                originalAmount = 0;
            }
            double gst = Double.parseDouble(paymentData.getGst());

            originalAmount -= gst;

            return (paymentData.buildCSVLine() + "|" + originalAmount + "|Already AUD!");
        }
    }
}
