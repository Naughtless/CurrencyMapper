package main.java.process.paypal.comparator;

import main.java.process.paypal.model.PayPalGrouped;

import java.util.Comparator;

public class PayPalGroupedComparator
        implements Comparator<PayPalGrouped> {
    @Override
    public int compare(PayPalGrouped r1, PayPalGrouped r2) {
        int sidComparison = r1.getStudentId().compareTo(r2.getStudentId());
        int dateComparison = r1.getParsedDate().compareTo(r2.getParsedDate());
        
        if(sidComparison != 0) {
            return sidComparison;
        }
        else {
            return dateComparison;
        }
        
    }
}
