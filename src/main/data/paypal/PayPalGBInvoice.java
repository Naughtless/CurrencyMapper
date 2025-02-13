package main.data.paypal;

import main.console.M;
import main.data.interfaces.CSV;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class PayPalGBInvoice implements CSV {
    //<editor-fold desc="Properties">
    private static String header = "Date|Name|Invoice Number|Student ID|Currency|AUD Converted Net";

    private ArrayList<PayPal> members;

    private String date;
    private String name;
    private String invoiceNumber;
    private String currency;
    private String audNet;

    private String studentId; // Is contained in (thus extracted from the) invoiceNumber.
    private LocalDate parsedDate;

    private boolean converted;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public PayPalGBInvoice() {
        this.converted = false;

        this.members = new ArrayList<>();
    }

    public PayPalGBInvoice(String name) {
        this.converted = false;

        this.name = name;

        this.members = new ArrayList<>();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void calculate() {
        for (int i = 0; i < members.size(); i++) {
            PayPal cm = members.get(i);

            // Name must be the same. Get any.
            if(!cm.getName().isEmpty()) {
                this.name = cm.getName();
            }

            // Populate invoice number & student id. all must be the same. get any.
            if(!cm.getInvoiceNumber().isEmpty()) {
                this.invoiceNumber = cm.getInvoiceNumber();

                String[] sidVal = invoiceNumber.split("-");

                if(sidVal.length >= 2) {
                    this.studentId = sidVal[1];
                }
                else {
                    this.studentId = "n/a";
                }
            }

            // Date must also be the same. Get any.
            // PATCH: 'type' "Refund" excepted!
            if(
                    !cm.getDate().isEmpty()
            ) {
                this.date = cm.getDate();
                this.parsedDate = LocalDate.parse(this.date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                //DEBUG
//                System.out.println("Set DATE val of " + cm.getName() + " to " + cm.getDate());

//                if(cm.getName().equals("Alexa Ditz")) {
//                    System.out.println("Set DATE val of " + cm.getName() + " to " + cm.getDate());
//                }

            }

            // Now check currency.
            if(!converted) {
                if(
                        cm.getType().trim().equals("General Currency Conversion")
                                && cm.getCurrency().trim().equals("AUD")
                ) {
                    this.currency = "Converted";
                    this.audNet = cm.getNet();

                    converted = true;
                }
                else {
                    this.currency = "Already AUD!";
                    this.audNet = cm.getNet();
                }
            }


        }
    }

    public String buildCSVLine() {
        StringBuilder sb = new StringBuilder();

        sb.append(audNet).append("|");
        sb.append(currency).append("|");
        sb.append(date).append("|");
        sb.append(name).append("|");
        sb.append(invoiceNumber).append("|");
        sb.append(studentId);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayPalGBInvoice that = (PayPalGBInvoice) o;
        return converted == that.converted && Objects.equals(members, that.members) && Objects.equals(date, that.date) && Objects.equals(name, that.name) && Objects.equals(invoiceNumber, that.invoiceNumber) && Objects.equals(currency, that.currency) && Objects.equals(audNet, that.audNet) && Objects.equals(studentId, that.studentId) && Objects.equals(parsedDate, that.parsedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(members, date, name, invoiceNumber, currency, audNet, studentId, parsedDate, converted);
    }

    @Override
    public String toString() {
        return buildCSVLine();
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public static String getHeader() {
        return header;
    }

    public static void setHeader(String header) {
        PayPalGBInvoice.header = header;
    }

    public ArrayList<PayPal> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<PayPal> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAudNet() {
        return audNet;
    }

    public void setAudNet(String audNet) {
        this.audNet = audNet;
    }

    public boolean isConverted() {
        return converted;
    }

    public void setConverted(boolean converted) {
        this.converted = converted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalDate getParsedDate() {
        return parsedDate;
    }

    public void setParsedDate(LocalDate parsedDate) {
        this.parsedDate = parsedDate;
    }
    //</editor-fold>
}
