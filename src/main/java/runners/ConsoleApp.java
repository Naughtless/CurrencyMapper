package main.java.runners;

import main.java.common.Match;
import main.java.process.afterpay.AfterpayModule;
import main.java.process.afterpay.model.AfterpayRate;
import main.java.process.exchangerate.model.ExchangeRate;
import main.java.process.paypal.model.PayPal;
import main.java.process.exchangerate.ExchangerateModule;
import main.java.process.paypal.PayPalModule;
import main.java.read.AfterpayReader;
import main.java.user.UserInputGUI;
import main.java.util.Ansi;
import main.java.util.ConsoleMessage;
import main.java.common.PaymentData;
import main.java.read.ExchangeRateReader;
import main.java.read.PayPalReader;
import main.java.read.PaymentDataReader;
import main.java.write.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.XMLFormatter;

public class ConsoleApp
{
    public static boolean DEBUG_MODE = false;
    
    public static void main(String[] args)
    {

        /*
         * This is the main Console Application runner, which will loop forever until terminated.
         */

        while (true)
        {
            // Which application to launch?
            int launchCode = UserInputGUI.getLaunchCode();
            switch (launchCode)
            {
                // In this case, only one application exists!
                case 1:
                    currencyMapper();
                    break;
            }
        }
    }
    
    public static void currencyMapper()
    {
        // 1A. Read master data: PaymentData.
        String                 masterDir = UserInputGUI.getFileName("Payment-Data");
        ArrayList<PaymentData> rawMaster = new PaymentDataReader().readPaymentData(masterDir);
        int rawMasterOriginalSize = rawMaster.size();
        
        // 1B. Read external data: PayPal.
        String            paypalDir = UserInputGUI.getFileName("PayPal");
        ArrayList<PayPal> rawPayPal = new PayPalReader().readPayPal(paypalDir);

        // 1C. Read external data: ExchangeRate.
        String                  exchrateDir = UserInputGUI.getFileName("Exchange Rate");
        ArrayList<ExchangeRate> rawExchrate = new ExchangeRateReader().readExchangeRate(exchrateDir);

        // 1D. Read external data: AfterPay.
        String       afterpayDir  = UserInputGUI.getFileName("AfterPay Rate");
        AfterpayRate afterpayRate = new AfterpayReader().readRate(afterpayDir);

        // 2. Instantiate ArrayList<Matches> to store processing results.
        ArrayList<Match> finalMatches = new ArrayList<>();

        // 2A. Process: PaymentData x PayPal
        PayPalModule     paypalModule = new PayPalModule(rawMaster, rawPayPal);
        ArrayList<Match> ppMatches    = paypalModule.getMatches();
        finalMatches.addAll(ppMatches);
        ConsoleMessage.info(
                "Results, PaymentData x PayPal: "
                + "\n-> Matches: " + Ansi.GREEN + ppMatches.size() + Ansi.BLUE
                + "\n-> No-Matches: " + Ansi.RED + paypalModule.getRemainder() + Ansi.RESET
        );
        ConsoleMessage.br();
        
        // 2B. Process: PaymentData x ExchangeRate
        ExchangerateModule exchangerateModule = new ExchangerateModule(rawMaster, rawExchrate);
        ArrayList<Match> erMatches = exchangerateModule.getMatches();
        finalMatches.addAll(erMatches);
        ConsoleMessage.info(
                "Results, PaymentData x ExchangeRate: "
                + "\n-> Matches: " + Ansi.GREEN + erMatches.size() + Ansi.BLUE
                + "\n-> No-Matches: " + Ansi.RED + exchangerateModule.getRemainder() + Ansi.RESET
        );
        ConsoleMessage.br();

        // 2C. Process: PaymentData x AfterPay
        AfterpayModule afterpayModule = new AfterpayModule(rawMaster, afterpayRate);
        ArrayList<Match> apMatches = afterpayModule.getMatches();
        finalMatches.addAll(apMatches);
        ConsoleMessage.info(
                "Results, PaymentData x AfterPay: "
                + "\n-> Matches: " + Ansi.GREEN + apMatches.size() + Ansi.BLUE
                + "\n-> No-Matches: " + Ansi.RED + afterpayModule.getRemainder() + Ansi.RESET
        );
        ConsoleMessage.br();
        
        // 3. Begin writing!
        CSVWriter<Match> matchWriter = new CSVWriter<>();
        CSVWriter<PaymentData> noMatchWriter = new CSVWriter<>();
        
        // 3A. Matches, Header.
        try
        {
            CSVWriter.writeHeader("results\\matches.csv", Match.header);
        }
        catch (IOException e)
        {
            ConsoleMessage.error(e, "Error writing: Matches, Header");
        }
        
        
        // 3B. Matches, Body - PayPal
        try
        {
            matchWriter.writeBody("results\\matches.csv", finalMatches, true);
        }
        catch (IOException e)
        {
            ConsoleMessage.error(e, "Error writing: Matches, Header");
        }
        
        // 4A. No-Matches, Header.
        try
        {
            CSVWriter.writeHeader("results\\no_matches.csv", PaymentData.header);
        }
        catch (IOException e)
        {
            ConsoleMessage.error(e, "Error writing: Matches, Header");
        }
        
        // 4B. No-Matches, Body.
        try
        {
            noMatchWriter.writeBody("results\\no_matches.csv", rawMaster, true);
        }
        catch (IOException e)
        {
            ConsoleMessage.error(e, "Error writing: Matches, Header");
        }
        
        // 5. Final Tally
        ConsoleMessage.info(
                "FINAL TALLY OF RESULTS:\n"
                + "=-> Original Payment Data: " + Ansi.YELLOW + rawMasterOriginalSize + Ansi.RESET
                + Ansi.BLUE + "\n\n=-> MATCH: " + Ansi.GREEN + finalMatches.size() + Ansi.RESET
                + Ansi.BLUE + "\n=-> NON-MATCH: " + Ansi.RED + rawMaster.size() + Ansi.RESET
                + Ansi.BLUE + "\n--- TOTAL: " + Ansi.WHITE + (finalMatches.size() + rawMaster.size()) + Ansi.RESET
        );
    }
}
