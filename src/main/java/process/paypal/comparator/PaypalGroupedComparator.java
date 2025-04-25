package main.java.process.paypal.comparator;

import main.java.util.ConsoleMessage;
import main.java.process.paypal.model.PaypalGrouped;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class PaypalGroupedComparator
        implements Comparator<PaypalGrouped> {
    @Override
    public int compare(PaypalGrouped r1, PaypalGrouped r2) {
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
            DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter df2 = DateTimeFormatter.ofPattern("d/MM/yyyy");

            // Debug
//            System.out.println("R1: " + r1.getDate());
//            System.out.println(r1.getName());
//            System.out.println(r1.getInvoiceNumber());
//
//            System.out.println("R2: " + r2.getDate());
//            System.out.println(r2.getName());
//            System.out.println(r2.getInvoiceNumber());

            LocalDate d1 = null;
            LocalDate d2 = null;

            // d1
            try {
                // Try dd/MM/yyyy
                d1 = LocalDate.parse(r1.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            catch(Exception ignored) {
                try{
                    // Else if, try d/MM/yyyy
                    d1 = LocalDate.parse(r1.getDate(), DateTimeFormatter.ofPattern("d/MM/yyyy"));
                }
                catch(Exception ignored2) {
                    try {
                        // Finally else, try to parse days since base date (five numbers)

                        // Parse the number string to int.
                        int dateNumber = Integer.parseInt(r1.getDate());

                        // Declare excel's base date.
                        LocalDate baseDate = LocalDate.of(1900, 1, 1);

                        // Get actual date.
                        d1 = baseDate.plusDays(dateNumber - 1);
                        // -1 to account for excel leap year bug.
                    } catch (Exception x) {
                        ConsoleMessage.error(x, "INVALID DATE FORMAT!");
                    }
                }
            }

            // d2
            try {
                // Try dd/MM/yyyy
                d2 = LocalDate.parse(r2.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            catch(Exception ignored) {
                try{
                    // Else if, try d/MM/yyyy
                    d2 = LocalDate.parse(r2.getDate(), DateTimeFormatter.ofPattern("d/MM/yyyy"));
                }
                catch(Exception ignored2) {
                    try {
                        // Finally else, try to parse days since base date (five numbers)

                        // Parse the number string to int.
                        int dateNumber = Integer.parseInt(r2.getDate());

                        // Declare excel's base date.
                        LocalDate baseDate = LocalDate.of(1900, 1, 1);

                        // Get actual date.
                        d2 = baseDate.plusDays(dateNumber - 1);
                        // -1 to account for excel leap year bug.
                    } catch (Exception x) {
                        ConsoleMessage.error(x, "INVALID DATE FORMAT!");
                    }
                }
            }

            return d1.compareTo(d2);
        }


    }
}
