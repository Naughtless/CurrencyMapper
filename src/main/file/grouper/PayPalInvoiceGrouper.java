package main.file.grouper;

import main.data.paypal.PayPal;
import main.data.paypal.PayPalGBInvoice;
import main.data.paypal.comparator.PayPalComparatorInvoice;

import java.util.ArrayList;

public class PayPalInvoiceGrouper {
    private ArrayList<PayPal> sourceArray;
    private ArrayList<PayPalGBInvoice> groupedArray;

    public PayPalInvoiceGrouper() {}
    public PayPalInvoiceGrouper(ArrayList<PayPal> sourceArray) {
        this.sourceArray = sourceArray;

        sourceArray.sort(new PayPalComparatorInvoice());
    }

    public void group() {
        groupedArray = new ArrayList<>();

        for(int i = 0; i < sourceArray.size(); i++) {
            // Expose necessary fields.
            PayPal currentPayPal = sourceArray.get(i);

            String currentInvoice = currentPayPal.getInvoiceNumber().trim();

            // Operate only if 'currentInvoice' is not blank!
            if(!currentPayPal.getInvoiceNumber().isEmpty()) {
                // For first entry only:
                if(i==0) {
                    PayPalGBInvoice newGroup = new PayPalGBInvoice();
                    newGroup.setInvoiceNumber(currentInvoice);

                    newGroup.getMembers().add(currentPayPal);
                    groupedArray.add(newGroup);
                }

                else { // Second entry onwards.
                    // Previous entry's invoice.
                    String previousInvoice = sourceArray.get(i-1).getInvoiceNumber().trim();

                    // If 'currentInvoice' is equals to 'previousInvoice'
                    // Add it as member to the incumbent GBN's members ArrayList.
                    if(currentInvoice.equals(previousInvoice)) {
                        groupedArray.getLast().getMembers().add(currentPayPal);
                    }

                    // If 'currentInvoice' is neither equals to previous nor blank (new invoice!)
                    // Create new GBN object.
                    else {
                        PayPalGBInvoice newGroup = new PayPalGBInvoice();
                        newGroup.setInvoiceNumber(currentInvoice);

                        newGroup.getMembers().add(currentPayPal);
                        groupedArray.add(newGroup);
                    }

                }
            }
        }
    }

    public void calculate() {
        for(PayPalGBInvoice pp: groupedArray) {
            pp.calculate();
        }
    }

    //<editor-fold desc="Getters & Setters">
    public ArrayList<PayPal> getSourceArray() {
        return sourceArray;
    }

    public void setSourceArray(ArrayList<PayPal> sourceArray) {
        this.sourceArray = sourceArray;
    }

    public ArrayList<PayPalGBInvoice> getGroupedArray() {
        return groupedArray;
    }

    public void setGroupedArray(ArrayList<PayPalGBInvoice> groupedArray) {
        this.groupedArray = groupedArray;
    }
    //</editor-fold>
}
