package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        String paypal = "05/07/2024";
        String payment = "06/07/2024";

        LocalDate cPaypal = LocalDate.parse(paypal, df);
        LocalDate cPayment = LocalDate.parse(payment, df);
        LocalDate cPaymentOffset = cPayment.minusDays(3);


        if (cPaypal.isBefore(cPaymentOffset)) {
            System.out.println("NOT FOUND: LOW!");
        } else if (cPaypal.isAfter(cPayment)) {
            System.out.println("NOT FOUND: HIGH!");
        } else { // Found.
            System.out.println("FOUND!");
        }
    }
}
