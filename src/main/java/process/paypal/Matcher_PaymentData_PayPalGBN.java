package main.java.process.paypal;

import main.java.util.ConsoleMessage;
import main.java.common.PaymentData;
import main.java.process.paypal.model.PaypalGrouped;
import main.java.process.paypal.comparator.PaypalGroupedComparator;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class Matcher_PaymentData_PayPalGBN
{
    //<editor-fold desc="Properties">
    private ArrayList<PaymentData>   pdSource;
    private ArrayList<PaypalGrouped> ppSource;

    private ArrayList<MatchPDPP> sidMatches;

    private ArrayList<PaymentData> pdSingles;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public Matcher_PaymentData_PayPalGBN() {}

    public Matcher_PaymentData_PayPalGBN(ArrayList<PaymentData> pdSource, ArrayList<PaypalGrouped> ppSource)
    {
        this.pdSource = pdSource;
        this.ppSource = ppSource;

        // Sort by 'studentID'
        this.ppSource.sort(new PaypalGroupedComparator());

        sidMatches = new ArrayList<>();

        pdSingles = new ArrayList<>();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void process()
            throws ParseException
    {

        //<editor-fold desc="Populate matches.">
        for (int i = 0; i < pdSource.size(); i++)
        {

            /*
            Specifically for: 'PAID' Trx.
             */
            if (pdSource.get(i).getPaymentType().trim().equals("PayPal") && pdSource.get(i).getPaymentStatus().trim().equals("Paid"))
            {

                String currentSid  = pdSource.get(i).getStudentId().trim();
                String currentDate = pdSource.get(i).getPaymentDate().trim();

                int ppGBNSearchResults = binarySearchPayPalBySidGroup(ppSource, currentSid, currentDate);

                // If found a matching student id.
                if (ppGBNSearchResults != -1)
                {
                    // Instantiate new match object.
                    MatchPDPP newMatch = new MatchPDPP(pdSource.get(i), ppSource.get(ppGBNSearchResults));

                    // Add it to the sidMatches array.
                    sidMatches.add(newMatch);
                }

                // If no match sid found.
                else
                {
                    pdSingles.add(pdSource.get(i));
                }
            }
            /*
            Specifically for: 'REFUND' Trx.
             */
            else if (pdSource.get(i).getPaymentType().trim().equals("PayPal") && pdSource.get(i).getPaymentStatus().trim().equals("Refund"))
            {
                pdSingles.add(pdSource.get(i));
            }
        }
        //</editor-fold>

        // Re-Process Singles data.
        /*
        Check for "REFUNDS", with matching studentId, currency, |amount|, and within +7 days after payment date.
         */
        // Iterator to loop through pdSingles
        Iterator<PaymentData> iterator = pdSingles.iterator();

        while (iterator.hasNext())
        {
            PaymentData pdSingle = iterator.next();

            // Extract fields from PaymentData
            String sid    = pdSingle.getStudentId();
            String amount = pdSingle.getAmount();
            String date   = pdSingle.getPaymentDate();

            // Use binary search to find a match in sidMatches
            int matchIndex = binarySearchSidMatches(sidMatches, sid, amount, date);

            if (matchIndex != -1)
            {
                // Match found, add it to the sidMatches.
                // First need to spoof a 'PayPalGBInvoice' object.
                PaypalGrouped spoof = new PaypalGrouped();
//                spoof.setAudNet(amount);
//                spoof.setCurrency("Refund");
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

    public int binarySearchPayPalBySidGroup(ArrayList<PaypalGrouped> ppSource, String desiredSid, String desiredDate)
            throws ParseException
    {
        int result = -1;

        int lowIndex  = 0;
        int highIndex = ppSource.size() - 1;
        int midIndex;

        /* Note:
            1. PayPal happens first, on avg. 1 day AHEAD of PaymentData record.
            2. We are looking for PayPal based on desired PaymentData date.
            3. PaymentData should have an added range of -3 DAYS before:
                3a. In event that PaymentData is on MONDAY and PayPal happened on FRIDAY.
                    3aa. -1 from MONDAY is SUNDAY
                    3ab. -2 from MONDAY is SATURDAY
                    3ac. -3 from MONDAY is FRIDAY

            SO: We need to apply a "ranged searching". It can't be only one specific date.
            The ranged search will LOOKUP the PayPal 4 times, using PD's date: H, H-1, H-2, and H-3
             */

        // Declare Payment Data Date & find its value.
        LocalDate paymentDataDate = null;


        //<editor-fold desc="Payment Data Date Logic">
        try
        {
            // Try dd/MM/yyyy
            paymentDataDate = LocalDate.parse(desiredDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        catch (Exception ignored)
        {
            try
            {
                // Else if, try d/MM/yyyy
                paymentDataDate = LocalDate.parse(desiredDate, DateTimeFormatter.ofPattern("d/MM/yyyy"));
            }
            catch (Exception ignored2)
            {
                try
                {
                    // Finally else, try to parse days since base date (five numbers)

                    // Parse the number string to int.
                    int dateNumber = Integer.parseInt(desiredDate);

                    // Declare excel's base date.
                    LocalDate baseDate = LocalDate.of(1900, 1, 1);

                    // Get actual date.
                    paymentDataDate = baseDate.plusDays(dateNumber - 1);
                    // -1 to account for excel leap year bug.

                }
                catch (Exception x)
                {
                    ConsoleMessage.error(x, "INVALID DATE FORMAT!");
                }
            }
        }
        //</editor-fold>

        // Declare the "ranged search" offset date.
        LocalDate paymentDataDateOffset3 = paymentDataDate.minusDays(3);
        LocalDate paymentDataDateOffset2 = paymentDataDate.minusDays(2);
        LocalDate paymentDataDateOffset1 = paymentDataDate.minusDays(1);

        // [OUTER LOOP] => We look for the matching SID first!
        while (lowIndex <= highIndex)
        {
            midIndex = lowIndex + ((highIndex - lowIndex) / 2);

            // Current PayPal invoice(contains SID) group.
            PaypalGrouped currentPayPal = ppSource.get(midIndex);

            // Extract the SID from currentPayPal.
            String currentPayPalSid = currentPayPal.getStudentId().trim();
            // Extract the DATE from currentPayPal.
            LocalDate currentPayPalDate = currentPayPal.getParsedDate();

            int strCompareValue = desiredSid.compareTo(currentPayPalSid);

            if (strCompareValue < 0)
            { // Desired result in the LEFT RANGE.
                highIndex = midIndex - 1;
            }

            else if (strCompareValue > 0)
            { // Desired result in the RIGHT RANGE.
                lowIndex = midIndex + 1;
            }

            else
            { // FOUND DESIRED studentId.

                // Now search for the appropriate date!
                if (currentPayPalDate.isBefore(paymentDataDateOffset3))
                {
                    lowIndex = midIndex + 1;
                }
                else if (currentPayPalDate.isAfter(paymentDataDate))
                {
                    highIndex = midIndex - 1;
                }
                else
                { // FOUND DESIRED DATE TOO!
                    result = midIndex;
                    break;
                }
            }
        }

        return result;
    }

    public int binarySearchSidMatches(ArrayList<MatchPDPP> sidMatches, String sid, String amount, String date)
    {
        int result = -1;

        int lowIndex  = 0;
        int highIndex = sidMatches.size() - 1;
        int midIndex;

        // Parse input date
        LocalDate inputDate = null;

        try
        {
            // Try dd/MM/yyyy
            inputDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        catch (Exception ignored)
        {
            try
            {
                // Else if, try d/MM/yyyy
                inputDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"));
            }
            catch (Exception ignored2)
            {
                try
                {
                    // Finally else, try to parse days since base date (five numbers)

                    // Parse the number string to int.
                    int dateNumber = Integer.parseInt(date);

                    // Declare excel's base date.
                    LocalDate baseDate = LocalDate.of(1900, 1, 1);

                    // Get actual date.
                    inputDate = baseDate.plusDays(dateNumber - 1);
                    // -1 to account for excel leap year bug.
                }
                catch (Exception x)
                {
                    ConsoleMessage.error(x, "INVALID DATE FORMAT!");
                }
            }
        }

        LocalDate inputDateOffset = inputDate.plusDays(7);

        // Convert amount to double
        double desiredAmount = Math.abs(Double.parseDouble(amount));

        while (lowIndex <= highIndex)
        {
            midIndex = lowIndex + ((highIndex - lowIndex) / 2);

            // Get current match
            MatchPDPP currentMatch = sidMatches.get(midIndex);

            String    currentSid    = currentMatch.getPayment().getStudentId();
            double    currentAmount = Math.abs(Double.parseDouble(currentMatch.getPayment().getAmount()));
            LocalDate currentDate   = null;

            try
            {
                currentDate = LocalDate.parse(currentMatch.getPayment().getPaymentDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            catch (Exception ignored)
            {
                currentDate = LocalDate.parse(currentMatch.getPayment().getPaymentDate(), DateTimeFormatter.ofPattern("d/MM/yyyy"));
            }
            int strCompareValue = sid.compareTo(currentSid);

            if (strCompareValue < 0)
            { // Desired result in the LEFT RANGE.
                highIndex = midIndex - 1;
            }
            else if (strCompareValue > 0)
            { // Desired result in the RIGHT RANGE.
                lowIndex = midIndex + 1;
            }
            else
            { // FOUND matching sid.
                // Now check the additional fields.
                if (Double.compare(desiredAmount, currentAmount) != 0)
                {
                    // Amount does not match.
                    highIndex = midIndex - 1;
                }
                else if (currentDate.isBefore(inputDate) || currentDate.isAfter(inputDateOffset))
                {
                    // Date is out of range.
                    highIndex = midIndex - 1;
                }
                else
                {
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
    public ArrayList<PaymentData> getPdSource()
    {
        return pdSource;
    }

    public void setPdSource(ArrayList<PaymentData> pdSource)
    {
        this.pdSource = pdSource;
    }

    public ArrayList<PaypalGrouped> getPpSource()
    {
        return ppSource;
    }

    public void setPpSource(ArrayList<PaypalGrouped> ppSource)
    {
        this.ppSource = ppSource;
    }

    public ArrayList<MatchPDPP> getSidMatches()
    {
        return sidMatches;
    }

    public void setSidMatches(ArrayList<MatchPDPP> sidMatches)
    {
        this.sidMatches = sidMatches;
    }

    public ArrayList<PaymentData> getPdSingles()
    {
        return pdSingles;
    }

    public void setPdSingles(ArrayList<PaymentData> pdSingles)
    {
        this.pdSingles = pdSingles;
    }
    //</editor-fold>
}
