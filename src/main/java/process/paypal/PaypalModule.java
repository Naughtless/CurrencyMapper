package main.java.process.paypal;

import main.java.process.common.Match;
import main.java.process.common.PaymentData;
import main.java.process.paypal.comparator.PaypalComparatorInvoiceDate;
import main.java.process.paypal.model.PaypalGrouped;
import main.java.process.paypal.model.Paypal;

import java.util.ArrayList;

public class PaypalModule
{
    private ArrayList<PaymentData> rawMaster;
    private ArrayList<Paypal>      rawPaypal;

    public PaypalModule(ArrayList<PaymentData> rawMaster, ArrayList<Paypal> rawPaypal)
    {
        this.rawMaster = rawMaster;
        this.rawPaypal = rawPaypal;
    }
    
    public ArrayList<Match> getMatches()
    {
        /*
         * Mapping Payment Data -> PayPal is a One-to-Many relationship.
         * To change this relationship into a One-to-One, PayPal data will be grouped by Invoice Number!
         * Why?
         *  -> PayPal has multiple entries (credits & debits) in a single invoice number.
         * For what?
         *  -> Currency conversion!
         *  -> Example: +849 GBP(cr) | -18.03 GBP(GST) | -830.97 GBP(conv) | +1688.6 AUD(conv)
         */
        
        // 1. Paypal data needs to be grouped by invoice.
        ArrayList<PaypalGrouped> groupedPaypal = group(rawPaypal);
    }
    
    private ArrayList<PaypalGrouped> group(ArrayList<Paypal> paypal) {
        ArrayList<PaypalGrouped> groups = new ArrayList<>();
        
        // 1. Sort rawPaypal by InvoiceDate for grouping.
        rawPaypal.sort(new PaypalComparatorInvoiceDate());
    }
}
