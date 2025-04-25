package main.java.process.paypal;

import main.java.common.Match;
import main.java.common.PaymentData;
import main.java.process.paypal.comparator.PaypalComparatorInvoiceDate;
import main.java.process.paypal.model.PaypalGrouped;
import main.java.common.Paypal;

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

    private ArrayList<PaypalGrouped> group(ArrayList<Paypal> paypal)
    {
        // 1. Sort rawPaypal by InvoiceDate for grouping.
        paypal.sort(new PaypalComparatorInvoiceDate());

        // 2. Instantiate ArrayLists
        ArrayList<PaypalGrouped> groupedPaypal = new ArrayList<>();

        // 3. Group raw PayPal data by non-blank invoice number only.
        for (int i = 0; i < paypal.size(); i++)
        {
            Paypal current = paypal.get(i);
            String invoice = current.getInvoiceNumber();
            String date    = current.getDate();

            // Skip entries with blank invoice numbers.
            if (invoice.isEmpty()) continue;

            boolean isFirstEntry = groupedPaypal.isEmpty();
            boolean isSameGroup  = !isFirstEntry && invoice.equals(groupedPaypal.getLast().getInvoiceNumber());

            if (isSameGroup)
            {
                groupedPaypal.getLast().getMembers().add(current);
            }
            else
            {
                groupedPaypal.add(new PaypalGrouped(invoice, date, current));
            }
        }
        return groupedPaypal;
    }
}
