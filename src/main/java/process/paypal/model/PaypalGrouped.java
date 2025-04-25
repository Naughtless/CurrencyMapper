package main.java.process.paypal.model;

import main.java.common.Paypal;
import main.java.util.ConsoleMessage;
import main.java.common.CSV;
import main.java.util.DateParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class PaypalGrouped
{
    private ArrayList<Paypal> members   = new ArrayList<>();

    /* Common Denominators */
    private String invoiceNumber; // Obtained during instantiation
    private String date; // Obtained during instantiation
    private String studentId; // Obtained during instantiation, extracted from invoiceNumber
    
    private String name; // Obtained during getCommons()
    private LocalDate parsedDate; // Obtained during getCommons()
    
    
    public PaypalGrouped()
    {}

    public PaypalGrouped(String invoiceNumber, String date, Paypal member)
    {
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.studentId = this.invoiceNumber.split("-")[1]; // Prone to breakage if invoiceNumber format changes!

        this.members.add(member);
    }


    public void getCommons() {
        for (Paypal currentMember : members) {
            // NAME - Has to be the same, so get any.
            if(!currentMember.getName().trim().isEmpty())
            {
                this.name = currentMember.getName();
            }
            
            // PARSED DATE - Should also be the same across the same invoice.
            if(!currentMember.getDate().trim().isEmpty())
            {
                this.parsedDate = DateParser.parseDate(currentMember.getDate());
            }
            
        }
    }
    

    //<editor-fold desc="Getters & Setters">
    public ArrayList<Paypal> getMembers()
    {
        return members;
    }

    public void setMembers(ArrayList<Paypal> members)
    {
        this.members = members;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getInvoiceNumber()
    {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }

    public String getStudentId()
    {
        return studentId;
    }

    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public LocalDate getParsedDate()
    {
        return parsedDate;
    }

    public void setParsedDate(LocalDate parsedDate)
    {
        this.parsedDate = parsedDate;
    }
    //</editor-fold>
}
