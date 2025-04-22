package main.java.runners;

import main.java.user.UserInputGUI;
import main.java.util.Ansi;
import main.java.util.ConsoleMessage;
import main.java.OLDdata.matches.MatchAfterPay;
import main.java.OLDdata.matches.MatchPDExc;
import main.java.OLDdata.matches.MatchPDPP;
import main.java.read.masterdata.PaymentData;
import main.java.OLDfile.grouper.ExchangeRateSourceGrouper;
import main.java.OLDfile.grouper.PayPalInvoiceGrouper;
import main.java.OLDfile.matcher.AfterPayRateProcessor;
import main.java.OLDfile.matcher.Matcher_ExchangeRate;
import main.java.OLDfile.matcher.Matcher_PaymentData_PayPalGBN;
import main.java.read.externaldata.ExchangeRateReader;
import main.java.read.externaldata.PayPalReader;
import main.java.read.masterdata.PaymentDataReader;
import main.java.write.Writer;

import java.io.*;
import java.text.ParseException;
import java.util.logging.XMLFormatter;

public class ConsoleApp
{
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
        // 1. Read master data: PaymentData.
        String masterDir = UserInputGUI.getFileName("PaymentData");
        ? rawMaster = read(masterDir);
        
        // 2. Read external data: PayPal.
        String paypalDir = UserInputGUI.getFileName("PayPal");
        ? rawPaypal = read(paypalDir);
        
        // 3. Read external data: ExchangeRate.
        String rateDir = UserInputGUI.getFileName("ExchangeRate");
        ? rawRate = read(rateDir);
        
    }

    public static void start(BufferedReader uReader) {
        //<editor-fold desc="Read Source Files">
        PaymentDataReader paymentReader;
        PayPalReader paypalReader;
        ExchangeRateReader excReader;

        //<editor-fold desc="Files Detection">
        File directory = new File("sources\\");

        File[] files = directory.listFiles(file -> file.isFile());
        ConsoleMessage.info("Source files detected:");
        for(int i = 0; i < files.length; i++) {
            System.out.println(Ansi.MAGENTA + "[" + (i+1) + "] " + Ansi.RESET + files[i].getName());
        }
        //</editor-fold>

        // Read PaymentData source file.
        while(true) {
            ConsoleMessage.input("Specify " + Ansi.RED + "PaymentData" + Ansi.YELLOW + " source file!");

            String paymentSourceDir = "";
            try {
                paymentSourceDir = "sources\\" + files[Integer.parseInt(uReader.readLine())-1].getName();
                ConsoleMessage.br();
            }
            catch(IOException iox) {
                ConsoleMessage.error(iox, "Something has gone very wrong whilst attempting to read user input.");
            }

            try {
                ConsoleMessage.info("Attempting to read file \"" + paymentSourceDir + "\"...");

                paymentReader = new PaymentDataReader(paymentSourceDir);
                paymentReader.readFile();
                paymentReader.process();

                ConsoleMessage.info("DONE!");
                ConsoleMessage.br();

                break;
            }
            catch(IOException ignored) {
                ConsoleMessage.warning("File reading failed!");
                ConsoleMessage.info("Make sure the main.file name is typed correctly, case sensitive.");
                ConsoleMessage.info("Try again!");
                ConsoleMessage.br();
                continue;
            }
        }

        // Read PayPal source file.
        while(true) {
            ConsoleMessage.input("Specify " + Ansi.RED + "PayPal" + Ansi.YELLOW + " source file!");

            String paypalSourceDir = "";
            try {
                paypalSourceDir = "sources\\" + files[Integer.parseInt(uReader.readLine())-1].getName();
                ConsoleMessage.br();
            }
            catch(IOException iox) {
                ConsoleMessage.error(iox, "Something has gone very wrong whilst attempting to read user input.");
            }

            try {
                ConsoleMessage.info("Attempting to read main.file \"" + paypalSourceDir + "\"...");

                paypalReader = new PayPalReader(paypalSourceDir);
                paypalReader.readFile();
                paypalReader.process();

                ConsoleMessage.info("DONE!");
                ConsoleMessage.br();

                break;
            }
            catch(IOException e1) {
                ConsoleMessage.warning("File reading failed!");
                ConsoleMessage.info("Make sure the main.file name is typed correctly, case sensitive.");
                ConsoleMessage.info("Try again!");
                ConsoleMessage.br();
                continue;
            }
        }

        // Read exchange rate source file.
        while(true) {
            ConsoleMessage.input("Specify " + Ansi.RED + "Exchange Rate" + Ansi.YELLOW + " source file!");

            String exchangeRateSourceDir = "";
            try {
                exchangeRateSourceDir = "sources\\" + files[Integer.parseInt(uReader.readLine())-1].getName();
                ConsoleMessage.br();
            }
            catch(IOException iox) {
                ConsoleMessage.error(iox, "Something has gone very wrong whilst attempting to read user input.");
            }

            try {
                ConsoleMessage.info("Attempting to read main.file \"" + exchangeRateSourceDir + "\"...");

                excReader = new ExchangeRateReader(exchangeRateSourceDir);
                excReader.readFile();
                excReader.process();

                ConsoleMessage.info("DONE!");
                ConsoleMessage.br();

                break;
            }
            catch(IOException e1) {
                ConsoleMessage.warning("File reading failed!");
                ConsoleMessage.info("Make sure the main.file name is typed correctly, case sensitive.");
                ConsoleMessage.info("Try again!");
                ConsoleMessage.br();
                continue;
            }
        }

        //</editor-fold>

        //<editor-fold desc="1A. PAYPAL: Group by Name">
        PayPalInvoiceGrouper paypalGrouper = new PayPalInvoiceGrouper(paypalReader.getContents());

        paypalGrouper.group();
        paypalGrouper.calculate();
        //</editor-fold>

        //<editor-fold desc="1B. PAYPAL: Matching">
        Matcher_PaymentData_PayPalGBN pdppMatcher = new Matcher_PaymentData_PayPalGBN(paymentReader.getContents(), paypalGrouper.getGroupedArray());
        try {
            pdppMatcher.process();
        }
        catch (ParseException e) {
            ConsoleMessage.error(e, "Parse failure!");
        }

        //</editor-fold>

        //<editor-fold desc="2A. BT, CB, CC, T: Exchange Rate, Group by Source">
        ExchangeRateSourceGrouper excGrouper = new ExchangeRateSourceGrouper(excReader.getContents());

        excGrouper.group();
        //</editor-fold>

        //<editor-fold desc="2A. BT, CB, CC, T: Exchange Rate, Matching">
        Matcher_ExchangeRate excMatcher = new Matcher_ExchangeRate(paymentReader.getContents(), excGrouper.getGroupedArray());

        excMatcher.process();
        //</editor-fold>

        //<editor-fold desc="3. AfterPay: Apply Formula">
        /*
        Amount - GST - a( b( c(Amount * Rate)c + 0.3)b + b( c(Amount * Rate)c + 0.3)b * GST )a
         */
        AfterPayRateProcessor app = new AfterPayRateProcessor(paymentReader.getContents());

        try{
            app.process();
        }
        catch(FileNotFoundException x) {
            ConsoleMessage.error(x, "Rates configuration file not found!");
        }
        //</editor-fold>

        //<editor-fold desc="Writing">
        ConsoleMessage.info("Writing results...");

        // Instantiate writer(s).
        Writer<MatchPDPP, PaymentData> pdppWriter = new Writer<>(); // PD(PayPal) with PayPal
        Writer<MatchPDExc, PaymentData> pdExcWriter = new Writer<>(); // PD(Others) with Exchange Rate
        Writer<MatchAfterPay, MatchAfterPay> apWriter = new Writer(); // AfterPay...

        // Matched data
        pdppWriter.setMatches(pdppMatcher.getSidMatches());
        pdExcWriter.setMatches(excMatcher.getMatches());

        // Singles data
        pdppWriter.setSingles(pdppMatcher.getPdSingles());
        pdExcWriter.setSingles(excMatcher.getPdSingles());

        // AfterPay data
        apWriter.setMatches(app.getMatches());

        // Attempt to write.
        while(true) {
            try{
                // Start with header first.
                pdppWriter.writeMatchHeader("results\\matches.csv");
                pdppWriter.writeMatches("results\\matches.csv", true);
                pdExcWriter.writeMatches("results\\matches.csv", true);
                apWriter.writeMatches("results\\matches.csv", true);

                // Start with header first.
                pdppWriter.writeSinglesHeader("results\\no_matches.csv");
                pdppWriter.writeSingles("results\\no_matches.csv", true);
                pdExcWriter.writeSingles("results\\matches.csv", true);

                break;
            }
            catch(IOException iox) {
                ConsoleMessage.warning("Failed to write to files. Please close all result files, then try again.");
            }
        }

        ConsoleMessage.info("DONE!");
        ConsoleMessage.br();

//        M.info("# of PaymentData source: " + Ansi.DARK_YELLOW + paymentReader.getContents().size());
//        M.info("# of PayPal source (GROUPED): " + Ansi.DARK_YELLOW + paypalGrouper.getGroupedArray().size());
//        M.br();

//        M.warning("PayPal data with a blank 'Invoice Number' field are ignored!");
//        M.br();

        int pdppNum = pdppMatcher.getSidMatches().size();
        int excNum = excMatcher.getMatches().size();
        int apNum = app.getMatches().size();
        int matchNum = pdppNum + excNum + apNum;
        ConsoleMessage.info(
                "Matches: " + Ansi.DARK_YELLOW +  matchNum);
        ConsoleMessage.br();

        ConsoleMessage.info("No-matches: " + Ansi.DARK_YELLOW + pdppMatcher.getPdSingles().size());
        ConsoleMessage.br();

        File dirMatches = new File("results\\matches.csv");
        File dirNoMatches = new File("results\\no_matches.csv");

        ConsoleMessage.info("Results can be found in the " + Ansi.DARK_YELLOW + "./results/" + Ansi.CYAN + " folder.");
        ConsoleMessage.info(dirMatches.getAbsolutePath());
        ConsoleMessage.info(dirNoMatches.getAbsolutePath());
        ConsoleMessage.br();
        //</editor-fold>
        //</editor-fold>
    }
}
