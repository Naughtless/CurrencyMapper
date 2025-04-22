package main.java.OLDfile.matcher;

import main.java.OLDdata.matches.MatchAfterPay;
import main.java.read.masterdata.PaymentData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import main.java.util.ConsoleMessage;

public class AfterPayRateProcessor {
    private ArrayList<PaymentData> pdSource;
    private ArrayList<MatchAfterPay> matches;


    public AfterPayRateProcessor() {}
    public AfterPayRateProcessor(ArrayList<PaymentData> pdSource) {
        this.pdSource = pdSource;
        this.matches = new ArrayList<>();
    }


    public void process() throws FileNotFoundException {
        double configRate = 0;
        double configGst = 0;

        try (BufferedReader rateReader = new BufferedReader(new FileReader("rates.txt"))) {
            String line;
            while ((line = rateReader.readLine()) != null) {
                String[] parts = line.split("=");
                if ("rate".equals(parts[0].trim())) configRate = Double.parseDouble(parts[1].trim());
                if ("gst".equals(parts[0].trim())) configGst = Double.parseDouble(parts[1].trim());
            }
        } catch (IOException e) {
            ConsoleMessage.error(e, "Could not read rates file! Does it exist??");
        }

        for(int i = 0; i < pdSource.size(); i++) {
            PaymentData pd = pdSource.get(i);
            String paymentType = pd.getPaymentType().trim();

            if(paymentType.equals("AfterPay")) {
                double amount = Double.parseDouble(pd.getAmount());
                double gst = Double.parseDouble(pd.getGst());

                double net = amount - gst - (((amount * configRate) + 0.3) + ((amount * configRate) + 0.3) * configGst);
                matches.add(new MatchAfterPay(pd, net));
            }
        }
    }

    //<editor-fold desc="Getters & Setters">
    public ArrayList<PaymentData> getPdSource() {
        return pdSource;
    }

    public void setPdSource(ArrayList<PaymentData> pdSource) {
        this.pdSource = pdSource;
    }

    public ArrayList<MatchAfterPay> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<MatchAfterPay> matches) {
        this.matches = matches;
    }
    //</editor-fold>
}


