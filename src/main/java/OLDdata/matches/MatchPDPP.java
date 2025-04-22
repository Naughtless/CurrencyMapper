package main.java.OLDdata.matches;

import main.java.common.CSV;
import main.java.read.masterdata.PaymentData;
import main.java.OLDdata.paypal.PayPalGBInvoice;

public class MatchPDPP implements CSV {
    private static String header = PaymentData.getHeader() + "||" + PayPalGBInvoice.getHeader();

    private PaymentData payment;
    private PayPalGBInvoice paypal;

    public MatchPDPP() {}
    public MatchPDPP(PaymentData payment, PayPalGBInvoice paypal) {
        this.payment = payment;
        this.paypal = paypal;
    }

    public String buildCSVLine() {
        if(payment.getCurrency().trim().equals("AUD")) {
            double gst = Double.parseDouble(payment.getGst().trim());
            double net = Double.parseDouble(paypal.getAudNet().replaceAll(",", "").trim());

            net -= gst;

            StringBuilder sb = new StringBuilder();

            sb.append(net).append("|");
            sb.append(paypal.getCurrency()).append("|");
            sb.append(paypal.getDate()).append("|");
            sb.append(paypal.getName()).append("|");
            sb.append(paypal.getInvoiceNumber()).append("|");
            sb.append(paypal.getStudentId());

            return payment.buildCSVLine() + "|" + sb.toString();
        }
        else {
            return payment.buildCSVLine() + "|" + paypal.buildCSVLine();
        }

    }

    //<editor-fold desc="Getters & Setters".
    public static String getHeader() {
        return header;
    }

    public static void setHeader(String header) {
        MatchPDPP.header = header;
    }

    public PaymentData getPayment() {
        return payment;
    }

    public void setPayment(PaymentData payment) {
        this.payment = payment;
    }

    public PayPalGBInvoice getPaypal() {
        return paypal;
    }

    public void setPaypal(PayPalGBInvoice paypal) {
        this.paypal = paypal;
    }
    //</editor-fold>
}
