package main.file.matcher;

import main.console.M;
import main.data.matches.MatchPDPP;
import main.data.paymentdata.PaymentData;
import main.data.paypal.PayPalGBInvoice;
import main.data.paypal.comparator.PayPalGBInvoiceComparator;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class Matcher_PaymentData_PayPalGBN {
    //<editor-fold desc="Properties">
    private ArrayList<PaymentData> pdSource;
    private ArrayList<PayPalGBInvoice> ppSource;

    private ArrayList<MatchPDPP> sidMatches;

    private ArrayList<PaymentData> pdSingles;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public Matcher_PaymentData_PayPalGBN() {}

    public Matcher_PaymentData_PayPalGBN(ArrayList<PaymentData> pdSource, ArrayList<PayPalGBInvoice> ppSource) {
        this.pdSource = pdSource;
        this.ppSource = ppSource;

        // Sort by 'studentID'
        this.ppSource.sort(new PayPalGBInvoiceComparator());

//        // Debug
//        int index = 0;
//        for(PayPalGBInvoice x: ppSource) {
//            if(x.getName().equals("Jennifer Lee")) System.out.println(index + ": " + x);
//            index++;
//        }

        sidMatches = new ArrayList<>();

        pdSingles = new ArrayList<>();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void process() throws ParseException {
        // Populate matches.
        for (int i = 0; i < pdSource.size(); i++) {
            /*
            Process only:
            - PayPal data!
            - payment_status = "Paid" (no refunds, yet)
            */
            if(
                    pdSource.get(i).getPaymentType().trim().equals("PayPal")
                    && pdSource.get(i).getPaymentStatus().trim().equals("Paid")
            ) {
                String currentSid = pdSource.get(i).getStudentId().trim();
                String currentDate = pdSource.get(i).getPaymentDate().trim();

                int ppGBNSearchResults = binarySearchPayPalSid(ppSource, currentSid, currentDate);

                // If found a matching student id.
                if(ppGBNSearchResults != -1) {
                    // Instantiate new match object.
                    MatchPDPP newMatch = new MatchPDPP(pdSource.get(i), ppSource.get(ppGBNSearchResults));

                    // Add it to the sidMatches array.
                    sidMatches.add(newMatch);
                }

                // If no match sid found.
                else {
                    pdSingles.add(pdSource.get(i));
                }
            }
            // For REFUNDS. Don't know what to do with em yet.
            else if(
                    pdSource.get(i).getPaymentType().trim().equals("PayPal")
                    && pdSource.get(i).getPaymentStatus().trim().equals("Refund")
            ) {
                pdSingles.add(pdSource.get(i));
            }
        }

        // Re-Process Singles data.
        /*
        Check for "REFUNDS", with matching studentId, currency, |amount|, and within +7 days after payment date.
         */
        // Iterator to loop through pdSingles
        Iterator<PaymentData> iterator = pdSingles.iterator();

        while (iterator.hasNext()) {
            PaymentData pdSingle = iterator.next();

            // Extract fields from PaymentData
            String sid = pdSingle.getStudentId();
            String amount = pdSingle.getAmount();
            String date = pdSingle.getPaymentDate();

            // Use binary search to find a match in sidMatches
            int matchIndex = binarySearchSidMatches(sidMatches, sid, amount, date);

            if (matchIndex != -1) {
                // Match found, add it to the sidMatches.
                // First need to spoof a 'PayPalGBInvoice' object.
                PayPalGBInvoice spoof = new PayPalGBInvoice();
                spoof.setAudNet(amount);
                spoof.setCurrency("Refund");
                spoof.setDate("");
                spoof.setName("");
                spoof.setInvoiceNumber("");
                spoof.setStudentId("");

                // Add to sid matches.
                sidMatches.add(new MatchPDPP(pdSingle, spoof));

                // Remove the current PaymentData from pdSingles
                iterator.remove();
            }
        }
    }

    public int binarySearchPayPalSid(ArrayList<PayPalGBInvoice> source, String desiredSid, String desiredDate) throws ParseException {
        int result = -1;

        int lowIndex = 0;
        int highIndex = source.size() - 1;
        int midIndex;

        /* Note:
            1. PayPal happens first, on avg. 1 day AHEAD of PaymentData record.
            2. We are looking for PayPal based on desired PaymentData.
            3. PaymentData should have an added range of -3 DAYS before:
                3a. In event that PaymentData is on MONDAY and PayPal happened on FRIDAY.
                    3aa. -1 from MONDAY is SUNDAY
                    3ab. -2 from MONDAY is SATURDAY
                    3ac. -3 from MONDAY is FRIDAY
             */

        // PaymentData
        LocalDate paymentDataDate = LocalDate.parse(desiredDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate paymentDataDateOffset = paymentDataDate.minusDays(3);

        while (lowIndex <= highIndex) {
            midIndex = lowIndex + ((highIndex - lowIndex) / 2);

            // PayPal
            PayPalGBInvoice currentPayPal = source.get(midIndex);

            String currentPayPalSid = currentPayPal.getStudentId();
            LocalDate currentPayPalDate = currentPayPal.getParsedDate();

            int strCompareValue = desiredSid.compareTo(currentPayPalSid);

            if(strCompareValue < 0) { // Desired result in the LEFT RANGE.
                highIndex = midIndex - 1;
            }
            else if (strCompareValue > 0) { // Desired result in the RIGHT RANGE.
                lowIndex = midIndex + 1;
            }
            else { // FOUND DESIRED studentId.
                // Now search for the appropriate date!
                if(currentPayPalDate.isBefore(paymentDataDateOffset)) {
                    lowIndex = midIndex + 1;
                }
                else if(currentPayPalDate.isAfter(paymentDataDate)) {
                    highIndex = midIndex - 1;
                }
                else { // FOUND DESIRED DATE TOO!
                    result = midIndex;
                    break;
                }
            }
        }

        return result;
    }

    public int binarySearchSidMatches(ArrayList<MatchPDPP> sidMatches, String sid, String amount, String date) {
        int result = -1;

        int lowIndex = 0;
        int highIndex = sidMatches.size() - 1;
        int midIndex;

        // Parse input date
        LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate inputDateOffset = inputDate.plusDays(7);

        // Convert amount to double
        double desiredAmount = Math.abs(Double.parseDouble(amount));

        while (lowIndex <= highIndex) {
            midIndex = lowIndex + ((highIndex - lowIndex) / 2);

            // Get current match
            MatchPDPP currentMatch = sidMatches.get(midIndex);

            String currentSid = currentMatch.getPayment().getStudentId();
            double currentAmount = Math.abs(Double.parseDouble(currentMatch.getPayment().getAmount()));
            LocalDate currentDate = LocalDate.parse(currentMatch.getPayment().getPaymentDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            int strCompareValue = sid.compareTo(currentSid);

            if (strCompareValue < 0) { // Desired result in the LEFT RANGE.
                highIndex = midIndex - 1;
            } else if (strCompareValue > 0) { // Desired result in the RIGHT RANGE.
                lowIndex = midIndex + 1;
            } else { // FOUND matching sid.
                // Now check the additional fields.
                if (Double.compare(desiredAmount, currentAmount) != 0) {
                    // Amount does not match.
                    highIndex = midIndex - 1;
                } else if (currentDate.isBefore(inputDate) || currentDate.isAfter(inputDateOffset)) {
                    // Date is out of range.
                    highIndex = midIndex - 1;
                } else {
                    // All fields match.
                    result = midIndex;
                    break;
                }
            }
        }

        return result;
    }

    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public ArrayList<PaymentData> getPdSource() {
        return pdSource;
    }

    public void setPdSource(ArrayList<PaymentData> pdSource) {
        this.pdSource = pdSource;
    }

    public ArrayList<PayPalGBInvoice> getPpSource() {
        return ppSource;
    }

    public void setPpSource(ArrayList<PayPalGBInvoice> ppSource) {
        this.ppSource = ppSource;
    }

    public ArrayList<MatchPDPP> getSidMatches() {
        return sidMatches;
    }

    public void setSidMatches(ArrayList<MatchPDPP> sidMatches) {
        this.sidMatches = sidMatches;
    }

    public ArrayList<PaymentData> getPdSingles() {
        return pdSingles;
    }

    public void setPdSingles(ArrayList<PaymentData> pdSingles) {
        this.pdSingles = pdSingles;
    }
    //</editor-fold>
}
