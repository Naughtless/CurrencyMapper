package main.java.OLDfile.grouper;

import main.java.read.externaldata.PayPal;
import main.java.OLDdata.paypal.PayPalGBInvoice;
import main.java.OLDdata.paypal.comparator.PayPalComparatorInvoiceDate;

import java.util.ArrayList;

public class PayPalInvoiceGrouper {
    private ArrayList<PayPal> sourceArray;
    private ArrayList<PayPalGBInvoice> groupedArray;

    public PayPalInvoiceGrouper() {}
    public PayPalInvoiceGrouper(ArrayList<PayPal> sourceArray) {
        this.sourceArray = sourceArray;

        sourceArray.sort(new PayPalComparatorInvoiceDate());
    }

    public void group() {
        groupedArray = new ArrayList<>();

        for(int i = 0; i < sourceArray.size(); i++) {
            // Expose necessary fields.
            PayPal currentPayPal = sourceArray.get(i);

            String currentInvoice = currentPayPal.getInvoiceNumber().trim();
            String currentDate = currentPayPal.getDate().trim();

            // Operate only if 'currentInvoice' is not blank!
            if(!currentPayPal.getInvoiceNumber().isEmpty()) {
                // For first entry only:
                if(i==0) {
                    addNewGroup(currentInvoice, currentDate, currentPayPal);
                }

                else { // Second entry onwards.
                    // Previous entry's invoice.
                    PayPal previousPayPal = sourceArray.get(i - 1);
                    String previousInvoice = previousPayPal.getInvoiceNumber().trim();
                    String previousDate = previousPayPal.getDate().trim();

                    // If 'currentInvoice' is equals to 'previousInvoice'
                    // Add it as member to the incumbent GBN's members ArrayList.
                    if (currentInvoice.equals(previousInvoice) && currentDate.equals(previousDate)) {
                        groupedArray.getLast().getMembers().add(currentPayPal);
                    }

                    // If 'currentInvoice' is neither equals to previous nor blank (new invoice!)
                    // Create new GBN object.
                    else {
                        addNewGroup(currentInvoice, currentDate, currentPayPal);
                    }

                }
            }
        }
    }

    private void addNewGroup(String invoice, String date, PayPal payPal) {
        PayPalGBInvoice newGroup = new PayPalGBInvoice();
        newGroup.setInvoiceNumber(invoice);
        newGroup.setDate(date); // Assuming PayPalGBInvoice has a date field
        newGroup.getMembers().add(payPal);
        groupedArray.add(newGroup);
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
