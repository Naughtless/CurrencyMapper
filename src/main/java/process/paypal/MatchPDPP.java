package main.java.process.paypal;

import main.java.common.CSV;
import main.java.common.PaymentData;
import main.java.process.paypal.model.PaypalGrouped;

public class MatchPDPP implements CSV {
    private static String header = PaymentData.getHeader() + "||";// + PaypalGrouped.getHeader();

    private PaymentData   payment;
    private PaypalGrouped paypal;

    public MatchPDPP() {}
    public MatchPDPP(PaymentData payment, PaypalGrouped paypal) {
        this.payment = payment;
        this.paypal = paypal;
    }

    public String buildCSVLine() {
        if(payment.getCurrency().trim().equals("AUD")) {
            double gst = Double.parseDouble(payment.getGst().trim());
            double net = 0; // Double.parseDouble(paypal.getAudNet().replaceAll(",", "").trim());

            net -= gst;

            StringBuilder sb = new StringBuilder();

            sb.append(net).append("|");
//            sb.append(paypal.getCurrency()).append("|");
            sb.append(paypal.getDate()).append("|");
            sb.append(paypal.getName()).append("|");
            sb.append(paypal.getInvoiceNumber()).append("|");
            sb.append(paypal.getStudentId());

            return payment.buildCSVLine() + "|" + sb.toString();
        }
        else {
            return payment.buildCSVLine() + "|"; // + paypal.buildCSVLine();
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

    public PaypalGrouped getPaypal() {
        return paypal;
    }

    public void setPaypal(PaypalGrouped paypal) {
        this.paypal = paypal;
    }
    //</editor-fold>
}
