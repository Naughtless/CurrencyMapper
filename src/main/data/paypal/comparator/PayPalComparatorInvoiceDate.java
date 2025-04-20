package main.data.paypal.comparator;

import main.data.paypal.PayPal;

import java.util.Comparator;

public class PayPalComparatorInvoiceDate implements Comparator<PayPal> {
    @Override
    public int compare(PayPal p1, PayPal p2) {
        int invoiceComparison = p1.getInvoiceNumber().compareTo(p2.getInvoiceNumber());
        if (invoiceComparison != 0) {
            return invoiceComparison;
        }
        return p1.getDate().compareTo(p2.getDate()); // Compare by date if invoices match
    }
}
