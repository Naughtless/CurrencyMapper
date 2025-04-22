package main.java.process.paypal.comparator;

import main.java.process.paypal.model.Paypal;

import java.util.Comparator;

public class PaypalComparatorInvoiceDate
        implements Comparator<Paypal> {
    @Override
    public int compare(Paypal p1, Paypal p2) {
        int invoiceComparison = p1.getInvoiceNumber().compareTo(p2.getInvoiceNumber());
        if (invoiceComparison != 0) {
            return invoiceComparison;
        }
        return p1.getDate().compareTo(p2.getDate()); // Compare by date if invoices match
    }
}
