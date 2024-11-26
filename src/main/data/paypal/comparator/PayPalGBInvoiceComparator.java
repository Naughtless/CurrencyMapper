package main.data.paypal.comparator;

import main.data.paypal.PayPalGBInvoice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class PayPalGBInvoiceComparator implements Comparator<PayPalGBInvoice> {
    @Override
    public int compare(PayPalGBInvoice r1, PayPalGBInvoice r2) {
        // First compare by studentId.
        int sidComparison = r1.getStudentId().compareTo(r2.getStudentId());

        /*
        If the two 'studentId' being compared are the same, return it.
         */
        // Why: this method is used for sorting, and will be called multiple times!
        if(sidComparison != 0) {
            return sidComparison;
        }

        /*
        If the two 'studentId' are EQUAL, now start comparing the date.
         */
        else {
            // Have to format the date to a sort-friendly format.
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Debug
//            System.out.println("R1: " + r1.getDate());
//            System.out.println(r1.getName());
//            System.out.println(r1.getInvoiceNumber());
//
//            System.out.println("R2: " + r2.getDate());
//            System.out.println(r2.getName());
//            System.out.println(r2.getInvoiceNumber());

            LocalDate d1 = LocalDate.parse(r1.getDate(), df);
            LocalDate d2 = LocalDate.parse(r2.getDate(), df);

            return d1.compareTo(d2);
        }


    }
}
