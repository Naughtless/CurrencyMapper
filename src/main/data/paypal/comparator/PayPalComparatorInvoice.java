package main.data.paypal.comparator;

import main.data.paypal.PayPal;

import java.util.Comparator;

public class PayPalComparatorInvoice implements Comparator<PayPal> {
    @Override
    public int compare(PayPal r1, PayPal r2) {
        return r1.getInvoiceNumber().compareTo(r2.getInvoiceNumber());
    }
}
