package main.java.process.paypal;

import main.java.common.Match;
import main.java.common.PaymentData;
import main.java.process.paypal.comparator.PaypalComparatorInvoiceDate;
import main.java.process.paypal.model.PaypalGrouped;
import main.java.common.Paypal;
import main.java.util.ConsoleMessage;
import main.java.util.DateParser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

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
        ArrayList<PaypalGrouped> groupedPaypal = groupPayments(rawPaypal);
        ArrayList<Paypal>        refunds       = getRefunds(rawPaypal);

        // 2. Return matches between 'groupedPaypal' and 'rawMaster'
        return match(rawMaster, groupedPaypal, refunds);
    }

    private ArrayList<PaypalGrouped> groupPayments(ArrayList<Paypal> paypal)
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

            // Skip refunds. Not processed here!
            if (current.getType().equals("Payment Refund")) continue;

            // Skip entries with blank invoice numbers.
            if (invoice.isEmpty()) continue;

            boolean isFirstEntry = groupedPaypal.isEmpty();
            boolean isSameGroup = !isFirstEntry && invoice.equals(groupedPaypal.getLast()
                    .getInvoiceNumber());

            if (isSameGroup)
            {
                groupedPaypal.getLast().getMembers().add(current);
            }
            else
            {
                groupedPaypal.add(new PaypalGrouped(invoice, date, current));
            }
        }

        // 4. Run getCommons() on each Paypal group.
        for (PaypalGrouped gr : groupedPaypal) gr.getCommons();

        // 5. Return
        return groupedPaypal;
    }

    private ArrayList<Paypal> getRefunds(ArrayList<Paypal> paypal)
    {
        // Use streams to filter "Payment Refund" into new array.
        return new ArrayList<>(paypal.stream()
                .filter(p -> p.getType().equals("Payment Refund"))
                .toList());

        // OLD. Doesn't work due to casting immutable to mutable (ClassCastException)
//        return (ArrayList<Paypal>) paypal.stream()
//                .filter(p -> p.getType().equals("Payment Refund"))
//                .toList();
    }

    private ArrayList<Match> match(
            ArrayList<PaymentData> rawMaster,
            ArrayList<PaypalGrouped> paypalGrouped,
            ArrayList<Paypal> paypalRefunds
    )
    {
        /*
         * PROCESSING MANUAL:
         * Data is categorized into 4 categories:
         * 1. AUD, Paid
         * 2. AUD, Refund
         * 3. Not AUD, Paid
         * 4. Not AUD, Refund
         *
         * --------
         * FOR ALL:
         * + payment_type must be 'PayPal'
         * + SID must match.
         * + DATE must match.
         *   ++ PD date (in practice) can be LATE by 1 day compared to PP date.
         *   ++ In theory (unconfirmed) by 3 days.
         *
         * ---------------------------
         * For Category 1 (AUD, Paid):
         * - Look for 'currency' = 'AUD' in PaymentData.
         * - Look for 'payment_status' = 'Paid' in PaymentData.
         * + Usually single entry in PayPal (group of 1 in PaypalGroup).
         * + Type: 'Pre-approved Payment Bill User Payment'
         * +- 'amount' in PaymentData and 'Gross' in PayPal should be same.
         *
         * -------------------------------
         * For Category 2 (Not AUD, Paid):
         * - 'currency' in PaymentData != 'AUD'
         * - 'payment_status' in PaymentData = 'Paid'
         * + Usually a group of 3 in PayPal. 1x Original Trf, and 2x currency conv.
         * + MATCH using 'amount' in PD with 'Gross' in PP
         *   ++ PP 'Type' = 'Pre-approved Payment Bill User Payment' IN ORIGINAL CURRENCY.
         * + RESULTS, look for the last 'Type' = 'General Currency Conversion' && 'Currency' = 'AUD'
         *   ++ SAME 'currency' in PP = PD.
         *

         *
         * ---------------------------------
         * For Category 4 (Not AUD, Refund):
         * - 'currency' in PaymentData != 'AUD'
         * - 'payment_status' in PaymentData = 'Refund'
         * + Usually in a group of 4 in Paypal. 1x Original Trf, 2x curr conv, 1x Refund.
         * + MATCH using 'amount' in PD with 'Gross' in PP
         *   ++ PP Type: 'Payment Refund'
         *   ++ SAME CURRENCY (because taxes and fees are also refunded??)
         * -> Therefor 'audValue' in Match.java should be ZERO.
         */

        // 1. Instantiate result ArrayList.
        ArrayList<Match> matches = new ArrayList<>();

        // 2. Process matches.
        Iterator<PaymentData> masterIterator = rawMaster.iterator();
        while (masterIterator.hasNext())
        {
            PaymentData currentMaster = masterIterator.next();
            boolean     foundMatch    = false;

            // 2X. Common filter(s).
            /* In this case, filter for ONLY PAYPAL! */
            if (currentMaster.getPaymentType().equals("PayPal"))
            {
                // Common data.
                String currency      = currentMaster.getCurrency();
                String paymentStatus = currentMaster.getPaymentStatus();
                String studentId     = currentMaster.getStudentId();
                String date          = currentMaster.getPaymentDate();


                // 2A. Process: Category 1 (AUD, Paid)
                /* For Category 1 (AUD, Paid):
                 * - Look for 'currency' = 'AUD' in PaymentData.
                 * - Look for 'payment_status' = 'Paid' in PaymentData.
                 * + Usually single entry in PayPal (group of 1 in PaypalGroup).
                 * + Type: 'Pre-approved Payment Bill User Payment'
                 * +- 'amount' in PaymentData and 'Gross' in PayPal should be same.
                 */
                if (currency.equals("AUD") && paymentStatus.equals("Paid"))
                {
                    int searchResults = searchPaypalGroup(paypalGrouped, studentId, date);
                    if (searchResults == -1) continue;
                    foundMatch = true;

                    PaypalGrouped ppg = paypalGrouped.get(searchResults);
                    /*
                     Safety Checks for expected behaviour
                     - 'ppg' should only have length of 1.
                     - 'ppg[0]' should have type = "Pre-approved Payment Bill User Payment"
                     - 'Gross' in 'ppg[0]' should be equals to 'amount' in 'currentMaster'
                     */

                    // CPG length should be 1.
                    if (ppg.getMembers().size() != 1)
                    {
                        ConsoleMessage.error(
                                new RuntimeException(),
                                "Unexpected behaviour in 'PaypalModule', Category 1: PPgr has more than one member!" + "\nExpected: CPG member(s) count is 1" + "\nFound: CPG member(s) count is " + ppg.getMembers()
                                        .size()
                        );
                    }
                    // ppg single member should have type "Pre-approved..."
                    if (!ppg.getMembers()
                            .getFirst()
                            .getType()
                            .equals("Pre-approved Payment Bill User Payment"))
                    {
                        ConsoleMessage.error(
                                new RuntimeException(),
                                "Unexpected behaviour in 'PaypalModule', Category 1: PPgr type is wrong!" + "\nExpected: \"Pre-approved Payment Bill User Payment\"" + "\nFound: " + ppg.getMembers()
                                        .getFirst()
                                        .getType()
                        );
                    }
                    // gross and amount should be equals.
                    if (!ppg.getMembers()
                            .getFirst()
                            .getGross()
                            .equals(currentMaster.getAmount()))
                    {
                        ConsoleMessage.error(
                                new RuntimeException(),
                                "Unexpected behaviour in 'PaypalModule', Category 1: PPgr gross mismatch with PD amount!" + "\nCPG gross: " + ppg.getMembers()
                                        .getFirst()
                                        .getGross() + "\nPD amount: " + currentMaster.getAmount()
                        );
                    }

                    Match currentMatch = new Match(
                            currentMaster,
                            ppg.getMembers().getFirst(),
                            "Already AUD, Payment",
                            false
                    );
                    matches.add(currentMatch);

                    paypalGrouped.remove(searchResults);
                }

                // 2B. Process: Category 2 (Not AUD, Paid)
                /* For Category 2 (Not AUD, Paid):
                 * - 'currency' in PaymentData != 'AUD'
                 * - 'payment_status' in PaymentData = 'Paid'
                 * + Usually a group of 3 in PayPal. 1x Original Trf, and 2x currency conv.
                 * + MATCH using 'amount' in PD with 'Gross' in PP
                 *   ++ PP 'Type' = 'Pre-approved Payment Bill User Payment' IN ORIGINAL CURRENCY.
                 * + RESULTS, look for the last 'Type' = 'General Currency Conversion' && 'Currency' = 'AUD'
                 *   ++ SAME 'currency' in PP = PD.
                 */
                else if (!currency.equals("AUD") && paymentStatus.equals("Paid"))
                {
                    int searchResults = searchPaypalGroup(paypalGrouped, studentId, date);
                    if (searchResults == -1) continue;
                    foundMatch = true;

                    PaypalGrouped ppg = paypalGrouped.get(searchResults);

                    // Logic to find which of the group of 3 in ppg's members contains AUD.
                    ArrayList<Paypal> ppgMembers = ppg.getMembers();
                    Paypal            target     = null;

                    // First check the original currency.
                    boolean originalCurrencyMatchFound = false;
                    for (Paypal cpp : ppgMembers)
                    {
                        if (cpp.getType()
                                    .equals("Pre-approved Payment Bill User Payment") && (Double.parseDouble(
                                cpp.getGross()) == Double.parseDouble(currentMaster.getAmount())))
                        {
                            originalCurrencyMatchFound = true;
                        }
                    }

                    // If original currency matches, find the converted value.
                    if (originalCurrencyMatchFound)
                    {
                        for (Paypal cpp : ppgMembers)
                        {
                            if (
                                    cpp.getType().equals("General Currency Conversion") 
                                    && cpp.getCurrency().equals("AUD")
                            )
                            {
                                target = cpp;
                            }

                        }
                    }
                    else
                    {
                        ConsoleMessage.debug("Comparing master: " + currentMaster.getPaymentType() + " " + currentMaster.getAmount());
                        for(Paypal cpp : ppgMembers)
                        {
                            ConsoleMessage.debug("PaypalGroup: " + cpp.getType() + " " + cpp.getGross());
                        }
                        ConsoleMessage.error(
                                new RuntimeException(),
                                "Error in Category 2!"
                        );
                    }
                    
                    Match currentMatch = new Match(
                            currentMaster,
                            target,
                            "Converted, Payment",
                            false
                    );
                    matches.add(currentMatch);

                    paypalGrouped.remove(searchResults);
                }

                // 2C. Process: Category 3 (AUD, Refund)
                /* -----------------------------
                 * For Category 3 (AUD, Refund):
                 * - Look for 'currency' = 'AUD' in PaymentData.
                 * - Look for 'payment_status' = 'Refund' in PaymentData.
                 * + Usually in a group of 1 in Paypal.
                 * + Original payment and refund should have the same invoiceNumber.
                 * + Type: 'Payment Refund'
                 * +- 'amount' in PD and 'Gross' in PP should be same (NEGATIVE NUMBER).
                 */
                else if (currency.equals("AUD") && paymentStatus.equals("Refund"))
                {
                    int searchResults = searchPaypal(paypalRefunds, studentId, date);
                    if (searchResults == -1) continue;
                    foundMatch = true;

                    Paypal pp = paypalRefunds.get(searchResults);

                    // pp should have type "Payment Refund"
                    if (!pp.getType().equals("Payment Refund"))
                    {
                        ConsoleMessage.error(
                                new RuntimeException(),
                                "Unexpected behaviour in 'PaypalModule', Category 3: PP type is wrong!" + "\nExpected: \"Payment Refund\"" + "\nFound: " + pp.getType()
                        );
                    }
                    // gross and amount should be equals.
                    if (!(Double.parseDouble(pp.getGross()) == Double.parseDouble(
                            currentMaster.getAmount())))
                    {
                        ConsoleMessage.error(
                                new RuntimeException(),
                                "Unexpected behaviour in 'PaypalModule', Category 3: PP gross mismatch with PD amount!" + "\nCPG gross: " + pp.getGross() + "\nPD amount: " + currentMaster.getAmount()
                        );
                    }

                    Match currentMatch = new Match(
                            currentMaster,
                            pp,
                            "Already AUD, Refund",
                            true
                    );
                    matches.add(currentMatch);

                    paypalRefunds.remove(searchResults);
                }

                // 2D. Process: Category 4 (Not AUD, Refund)
                /* For Category 4 (Not AUD, Refund):
                 * - 'currency' in PaymentData != 'AUD'
                 * - 'payment_status' in PaymentData = 'Refund'
                 * + Usually in a group of 1 in Paypal.
                 * + MATCH using 'amount' in PD with 'Gross' in PP
                 *   ++ PP Type: 'Payment Refund'
                 *   ++ SAME CURRENCY (because taxes and fees are also refunded??)
                 * -> Therefor 'audValue' in Match.java should be ZERO.
                 */
                else if (!currency.equals("AUD") && paymentStatus.equals("Refund"))
                {
                    int searchResults = searchPaypal(paypalRefunds, studentId, date);
                    if (searchResults == -1) continue;
                    foundMatch = true;

                    Paypal pp = paypalRefunds.get(searchResults);

                    // pp should have type "Payment Refund"
                    if (!pp.getType().trim().equals("Payment Refund"))
                    {
                        ConsoleMessage.error(
                                new RuntimeException(),
                                "Unexpected behaviour in 'PaypalModule', Category 4: PP type is wrong!" + "\nExpected: Payment Refund" + "\nFound: " + pp.getType()
                        );
                    }
                    // gross and amount should be equals.
                    if (!(Double.parseDouble(pp.getGross()) == Double.parseDouble(
                            currentMaster.getAmount())))
                    {
                        ConsoleMessage.error(
                                new RuntimeException(),
                                "Unexpected behaviour in 'PaypalModule', Category 3: PP gross mismatch with PD amount!" + "\nCPG gross: " + pp.getGross() + "\nPD amount: " + currentMaster.getAmount()
                        );
                    }

                    Match currentMatch = new Match(
                            currentMaster,
                            pp,
                            "Converted, Refund",
                            true
                    );
                    matches.add(currentMatch);

                    paypalRefunds.remove(searchResults);
                }

                // 2-Final. If a match was found, then remove it from the master array.
                if (foundMatch) masterIterator.remove();

            }
        }

        return matches;
    }

    private int searchPaypalGroup(
            ArrayList<PaypalGrouped> source,
            String targetStudentId,
            String targetDate
    )
    {
        /* Note:
         *  1. PayPal happens first, on avg. 1 day AHEAD of PaymentData record.
         *  2. We are looking for PayPal based on desired PaymentData date.
         *  3. PaymentData should have an added range of -3 DAYS before:
         *      3a. In event that PaymentData is on MONDAY and PayPal happened on FRIDAY.
         *          3aa. -1 from MONDAY is SUNDAY
         *          3ab. -2 from MONDAY is SATURDAY
         *          3ac. -3 from MONDAY is FRIDAY
         * SO: We need to apply a "ranged searching". It can't be only one specific date.
         * The ranged search will LOOKUP the PayPal 4 times, using PD's date: H, H-1, H-2, and H-3
         */

        int resultIndex = -1;

        int lowIndex  = 0;
        int highIndex = source.size() - 1;
        int midIndex;

        // Declare Payment Data Date & find its value.
        LocalDate masterDate = DateParser.parseDate(targetDate);

        // Declare the "ranged search" offset date (days BEFORE the payment_date).
        LocalDate masterDateOffset = masterDate.minusDays(1);

        // [OUTER LOOP] => We look for the matching SID first!
        while (lowIndex <= highIndex)
        {
            midIndex = lowIndex + ((highIndex - lowIndex) / 2);

            // Extract studentId & date from the current PayPal group.
            String paypalStudentId = source.get(midIndex).getStudentId().trim();
            LocalDate paypalDate = source.get(midIndex).getParsedDate();

            int strCompareValue = targetStudentId.compareTo(paypalStudentId);

            if (strCompareValue < 0)
            {
                // Desired result in the LEFT RANGE.
                highIndex = midIndex - 1;
            }
            else if (strCompareValue > 0)
            {
                // Desired result in the RIGHT RANGE.
                lowIndex = midIndex + 1;
            }
            else
            {
                // FOUND DESIRED studentId -> Now search for the appropriate date!
                if (paypalDate.isBefore(masterDateOffset))
                {
                    lowIndex = midIndex + 1;
                }
                else if (paypalDate.isAfter(masterDate))
                {
                    highIndex = midIndex - 1;
                }
                else
                {
                    // FOUND DESIRED DATE TOO!
                    resultIndex = midIndex;
                    break;
                }
            }
        }
        return resultIndex;
    }

    private int searchPaypal(
            ArrayList<Paypal> source,
            String targetStudentId,
            String targetDate
    )
    {
        /* DEBUG */
        ConsoleMessage.debug("Executing searchPaypal(), looking for SID: " + targetStudentId + " and DATE: " + targetDate);

        /* Note:
         *  1. PayPal happens first, on avg. 1 day AHEAD of PaymentData record.
         *  2. We are looking for PayPal based on desired PaymentData date.
         *  3. PaymentData should have an added range of -3 DAYS before:
         *      3a. In event that PaymentData is on MONDAY and PayPal happened on FRIDAY.
         *          3aa. -1 from MONDAY is SUNDAY
         *          3ab. -2 from MONDAY is SATURDAY
         *          3ac. -3 from MONDAY is FRIDAY
         * SO: We need to apply a "ranged searching". It can't be only one specific date.
         * The ranged search will LOOKUP the PayPal 4 times, using PD's date: H, H-1, H-2, and H-3
         */

        int resultIndex = -1;

        int lowIndex  = 0;
        int highIndex = source.size() - 1;
        int midIndex;

        // Declare Payment Data Date & find its value.
        LocalDate masterDate = DateParser.parseDate(targetDate);

        // Declare the "ranged search" offset date (days BEFORE the payment_date).
        LocalDate masterDateOffset = masterDate.minusDays(1);

        // [OUTER LOOP] => We look for the matching SID first!
        int counter = 0;
        while (lowIndex <= highIndex)
        {
            /* DEBUG */
            counter++;
            ConsoleMessage.debug("Iteration #" + counter);

            // Get Mid Index
            midIndex = lowIndex + ((highIndex - lowIndex) / 2);

            /* DEBUG */
            ConsoleMessage.debug("Search: " + source.get(midIndex));

            // Extract studentId & date from the current PayPal group.
            String paypalStudentId = source.get(midIndex).getStudentId().trim();
            LocalDate paypalDate = source.get(midIndex).getParsedDate();

            int strCompareValue = targetStudentId.compareTo(paypalStudentId);

            if (strCompareValue < 0)
            {
                // Desired result in the LEFT RANGE.
                highIndex = midIndex - 1;
            }
            else if (strCompareValue > 0)
            {
                // Desired result in the RIGHT RANGE.
                lowIndex = midIndex + 1;
            }
            else
            {
                // FOUND DESIRED studentId -> Now search for the appropriate date!
                if (paypalDate.isBefore(masterDateOffset))
                {
                    lowIndex = midIndex + 1;
                }
                else if (paypalDate.isAfter(masterDate))
                {
                    highIndex = midIndex - 1;
                }
                else
                {
                    // FOUND DESIRED DATE TOO!
                    resultIndex = midIndex;
                    break;
                }
            }
        }
        return resultIndex;
    }
}
