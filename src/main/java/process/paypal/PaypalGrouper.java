package main.java.process.paypal;

import main.java.process.paypal.model.PaypalGrouped;
import main.java.process.paypal.comparator.PaypalComparatorInvoiceDate;
import main.java.process.paypal.model.Paypal;

import java.util.ArrayList;

public class PaypalGrouper
{
    private ArrayList<Paypal>        sourceArray;
    private ArrayList<PaypalGrouped> groupedArray;

    public PaypalGrouper() {}
    public PaypalGrouper(ArrayList<Paypal> sourceArray) {
        this.sourceArray = sourceArray;

        sourceArray.sort(new PaypalComparatorInvoiceDate());
    }

    public void group() {
        groupedArray = new ArrayList<>();

        for(int i = 0; i < sourceArray.size(); i++) {
            // Expose necessary fields.
            Paypal currentPaypal = sourceArray.get(i);

            String currentInvoice = currentPaypal.getInvoiceNumber().trim();
            String currentDate = currentPaypal.getDate().trim();

            // Operate only if 'currentInvoice' is not blank!
            if(!currentPaypal.getInvoiceNumber().isEmpty()) {
                // For first entry only:
                if(i==0) {
                    addNewGroup(currentInvoice, currentDate, currentPaypal);
                }

                else { // Second entry onwards.
                    // Previous entry's invoice.
                    Paypal previousPaypal  = sourceArray.get(i - 1);
                    String previousInvoice = previousPaypal.getInvoiceNumber().trim();
                    String previousDate    = previousPaypal.getDate().trim();

                    // If 'currentInvoice' is equals to 'previousInvoice'
                    // Add it as member to the incumbent GBN's members ArrayList.
                    if (currentInvoice.equals(previousInvoice) && currentDate.equals(previousDate)) {
                        groupedArray.getLast().getMembers().add(currentPaypal);
                    }

                    // If 'currentInvoice' is neither equals to previous nor blank (new invoice!)
                    // Create new GBN object.
                    else {
                        addNewGroup(currentInvoice, currentDate, currentPaypal);
                    }

                }
            }
        }
    }

    private void addNewGroup(String invoice, String date, Paypal payPal) {
        PaypalGrouped newGroup = new PaypalGrouped();
        newGroup.setInvoiceNumber(invoice);
        newGroup.setDate(date); // Assuming PayPalGBInvoice has a date field
        newGroup.getMembers().add(payPal);
        groupedArray.add(newGroup);
    }


    public void calculate() {
        for(PaypalGrouped pp: groupedArray) {
            pp.calculate();
        }
    }

    //<editor-fold desc="Getters & Setters">
    public ArrayList<Paypal> getSourceArray() {
        return sourceArray;
    }

    public void setSourceArray(ArrayList<Paypal> sourceArray) {
        this.sourceArray = sourceArray;
    }

    public ArrayList<PaypalGrouped> getGroupedArray() {
        return groupedArray;
    }

    public void setGroupedArray(ArrayList<PaypalGrouped> groupedArray) {
        this.groupedArray = groupedArray;
    }
    //</editor-fold>
}
