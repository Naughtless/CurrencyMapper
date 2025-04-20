package main;

import main.console.Ansi;
import main.console.M;
import main.data.matches.MatchAfterPay;
import main.data.matches.MatchPDExc;
import main.data.matches.MatchPDPP;
import main.data.paymentdata.PaymentData;
import main.file.grouper.ExchangeRateSourceGrouper;
import main.file.grouper.PayPalInvoiceGrouper;
import main.file.matcher.AfterPayRateProcessor;
import main.file.matcher.Matcher_ExchangeRate;
import main.file.matcher.Matcher_PaymentData_PayPalGBN;
import main.file.reader.format.ExchangeRateReader;
import main.file.reader.format.PayPalReader;
import main.file.reader.format.PaymentDataReader;
import main.file.writer.Writer;

import java.io.*;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        // Instantiate user-input reader.
        BufferedReader uReader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.println("---------- ---------- ---------- ---------- ----------");
            System.out.println(Ansi.BOLD + "What would you like to do?" + Ansi.RESET);
            System.out.println(Ansi.CYAN + "[" + Ansi.RED + Ansi.BOLD + "1" + Ansi.RESET + Ansi.CYAN + "] Start.");
            M.input("Make a choice...");

            String selectionInput = "0";

            try{
                selectionInput = uReader.readLine();
            }
            catch(IOException iox) {
                M.error(iox, "Something has gone very wrong whilst attempting to read user input.");
            }


            switch(selectionInput) {
                case "1":
                    start(uReader);
                    break;
                default:
                    M.warning("That is an invalid choice!");
            }
        }
    }

    public static void start(BufferedReader uReader) {
        //<editor-fold desc="Read Source Files">
        PaymentDataReader paymentReader;
        PayPalReader paypalReader;
        ExchangeRateReader excReader;

        //<editor-fold desc="Files Detection">
        File directory = new File("sources\\");

        File[] files = directory.listFiles(file -> file.isFile());
        M.info("Source files detected:");
        for(int i = 0; i < files.length; i++) {
            System.out.println(Ansi.MAGENTA + "[" + (i+1) + "] " + Ansi.RESET + files[i].getName());
        }
        //</editor-fold>

        // Read PaymentData source file.
        while(true) {
            M.input("Specify " + Ansi.RED + "PaymentData" + Ansi.YELLOW + " source file!");

            String paymentSourceDir = "";
            try {
                paymentSourceDir = "sources\\" + files[Integer.parseInt(uReader.readLine())-1].getName();
                M.br();
            }
            catch(IOException iox) {
                M.error(iox, "Something has gone very wrong whilst attempting to read user input.");
            }

            try {
                M.info("Attempting to read file \"" + paymentSourceDir + "\"...");

                paymentReader = new PaymentDataReader(paymentSourceDir);
                paymentReader.readFile();
                paymentReader.process();

                M.info("DONE!");
                M.br();

                break;
            }
            catch(IOException ignored) {
                M.warning("File reading failed!");
                M.info("Make sure the main.file name is typed correctly, case sensitive.");
                M.info("Try again!");
                M.br();
                continue;
            }
        }

        // Read PayPal source file.
        while(true) {
            M.input("Specify " + Ansi.RED + "PayPal" + Ansi.YELLOW + " source file!");

            String paypalSourceDir = "";
            try {
                paypalSourceDir = "sources\\" + files[Integer.parseInt(uReader.readLine())-1].getName();
                M.br();
            }
            catch(IOException iox) {
                M.error(iox, "Something has gone very wrong whilst attempting to read user input.");
            }

            try {
                M.info("Attempting to read main.file \"" + paypalSourceDir + "\"...");

                paypalReader = new PayPalReader(paypalSourceDir);
                paypalReader.readFile();
                paypalReader.process();

                M.info("DONE!");
                M.br();

                break;
            }
            catch(IOException e1) {
                M.warning("File reading failed!");
                M.info("Make sure the main.file name is typed correctly, case sensitive.");
                M.info("Try again!");
                M.br();
                continue;
            }
        }

        // Read exchange rate source file.
        while(true) {
            M.input("Specify " + Ansi.RED + "Exchange Rate" + Ansi.YELLOW + " source file!");

            String exchangeRateSourceDir = "";
            try {
                exchangeRateSourceDir = "sources\\" + files[Integer.parseInt(uReader.readLine())-1].getName();
                M.br();
            }
            catch(IOException iox) {
                M.error(iox, "Something has gone very wrong whilst attempting to read user input.");
            }

            try {
                M.info("Attempting to read main.file \"" + exchangeRateSourceDir + "\"...");

                excReader = new ExchangeRateReader(exchangeRateSourceDir);
                excReader.readFile();
                excReader.process();

                M.info("DONE!");
                M.br();

                break;
            }
            catch(IOException e1) {
                M.warning("File reading failed!");
                M.info("Make sure the main.file name is typed correctly, case sensitive.");
                M.info("Try again!");
                M.br();
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
            M.error(e, "Parse failure!");
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
            M.error(x, "Rates configuration file not found!");
        }
        //</editor-fold>

        //<editor-fold desc="Writing">
        M.info("Writing results...");

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
                M.warning("Failed to write to files. Please close all result files, then try again.");
            }
        }

        M.info("DONE!");
        M.br();

//        M.info("# of PaymentData source: " + Ansi.DARK_YELLOW + paymentReader.getContents().size());
//        M.info("# of PayPal source (GROUPED): " + Ansi.DARK_YELLOW + paypalGrouper.getGroupedArray().size());
//        M.br();

//        M.warning("PayPal data with a blank 'Invoice Number' field are ignored!");
//        M.br();

        int pdppNum = pdppMatcher.getSidMatches().size();
        int excNum = excMatcher.getMatches().size();
        int apNum = app.getMatches().size();
        int matchNum = pdppNum + excNum + apNum;
        M.info(
                "Matches: " + Ansi.DARK_YELLOW +  matchNum);
        M.br();

        M.info("No-matches: " + Ansi.DARK_YELLOW +  pdppMatcher.getPdSingles().size());
        M.br();

        File dirMatches = new File("results\\matches.csv");
        File dirNoMatches = new File("results\\no_matches.csv");

        M.info("Results can be found in the " + Ansi.DARK_YELLOW + "./results/" + Ansi.CYAN + " folder.");
        M.info(dirMatches.getAbsolutePath());
        M.info(dirNoMatches.getAbsolutePath());
        M.br();
        //</editor-fold>
        //</editor-fold>
    }
}
