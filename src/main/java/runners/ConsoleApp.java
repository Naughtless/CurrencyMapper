package main.java.runners;

import main.java.process.afterpay.AfterpayModule;
import main.java.common.AfterpayRate;
import main.java.common.Match;
import main.java.common.ExchangeRate;
import main.java.common.Paypal;
import main.java.process.exchangerate.ExchangerateModule;
import main.java.process.paypal.PaypalModule;
import main.java.read.AfterpayReader;
import main.java.user.UserInputGUI;
import main.java.util.Ansi;
import main.java.util.ConsoleMessage;
import main.java.process.afterpay.MatchAfterPay;
import main.java.process.exchangerate.MatchPDExc;
import main.java.process.paypal.MatchPDPP;
import main.java.common.PaymentData;
import main.java.process.exchangerate.ExchangerateSourceGrouper;
import main.java.process.paypal.PaypalGrouper;
import main.java.process.afterpay.AfterPayRateProcessor;
import main.java.process.exchangerate.Matcher_ExchangeRate;
import main.java.process.paypal.Matcher_PaymentData_PayPalGBN;
import main.java.read.ExchangeRateReader;
import main.java.read.PaypalReader;
import main.java.read.PaymentDataReader;
import main.java.write.OldWriter;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;

public class ConsoleApp
{
    public static void main(String[] args)
            throws IOException
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
        String                 masterDir = UserInputGUI.getFileName("PaymentData");
        ArrayList<PaymentData> rawMaster = new PaymentDataReader().readPaymentData(masterDir);

        // 1B. Read external data: PayPal.
        String            paypalDir = UserInputGUI.getFileName("PayPal");
        ArrayList<Paypal> rawPaypal = new PaypalReader().readPayPal(paypalDir);

        // 1C. Read external data: ExchangeRate.
        String                  exchrateDir = UserInputGUI.getFileName("ExchangeRate");
        ArrayList<ExchangeRate> rawExchrate = new ExchangeRateReader().readExchangeRate(exchrateDir);

        // 1D. Read external data: AfterPay.
        String       afterpayDir  = UserInputGUI.getFileName("AfterPay Rate");
        AfterpayRate afterpayRate = new AfterpayReader().readRate(afterpayDir);

        // 2. Instantiate ArrayList<Matches> to store processing results.
        ArrayList<Match> matches = new ArrayList<>();

        // 2A. Process: PaymentData x PayPal
        ArrayList<Match> ppMatches = new PaypalModule(rawMaster, rawPaypal).getMatches();
        matches.addAll(ppMatches);
        
        /* CURRENT PROGRESS IS HERE */
        
        // 2B. Process: PaymentData x ExchangeRate
        ArrayList<Match> erMatches = new ExchangerateModule(rawMaster, rawExchrate).getMatches();
        matches.addAll(erMatches);
        
        // 2C. Process: PaymentData x AfterPay
        ArrayList<Match> apMatches = new AfterpayModule(rawMaster, afterpayRate).getMatches();
        matches.addAll(apMatches);
    }

    
    /* OLD & NEW BARRIER -------------------------------------- */
    
    
//    public static void start(BufferedReader uReader)
//    {
//        //<editor-fold desc="Read Source Files">
//        PaymentDataReader  paymentReader;
//        PaypalReader       paypalReader;
//        ExchangeRateReader excReader;
//
//        //<editor-fold desc="Files Detection">
//        File directory = new File("sources\\");
//
//        File[] files = directory.listFiles(file -> file.isFile());
//        ConsoleMessage.info("Source files detected:");
//        for (int i = 0; i < files.length; i++)
//        {
//            System.out.println(Ansi.MAGENTA + "[" + (i + 1) + "] " + Ansi.RESET + files[i].getName());
//        }
//        //</editor-fold>
//
//        // Read PaymentData source file.
//        while (true)
//        {
//            ConsoleMessage.input("Specify " + Ansi.RED + "PaymentData" + Ansi.YELLOW + " source file!");
//
//            String paymentSourceDir = "";
//            try
//            {
//                paymentSourceDir = "sources\\" + files[Integer.parseInt(uReader.readLine()) - 1].getName();
//                ConsoleMessage.br();
//            }
//            catch (IOException iox)
//            {
//                ConsoleMessage.error(iox, "Something has gone very wrong whilst attempting to read user input.");
//            }
//
//            try
//            {
//                ConsoleMessage.info("Attempting to read file \"" + paymentSourceDir + "\"...");
//
//                paymentReader = new PaymentDataReader(paymentSourceDir);
//                paymentReader.read();
//                paymentReader.readPaymentData();
//
//                ConsoleMessage.info("DONE!");
//                ConsoleMessage.br();
//
//                break;
//            }
//            catch (IOException ignored)
//            {
//                ConsoleMessage.warning("File reading failed!");
//                ConsoleMessage.info("Make sure the main.file name is typed correctly, case sensitive.");
//                ConsoleMessage.info("Try again!");
//                ConsoleMessage.br();
//                continue;
//            }
//        }
//
//        // Read PayPal source file.
//        while (true)
//        {
//            ConsoleMessage.input("Specify " + Ansi.RED + "PayPal" + Ansi.YELLOW + " source file!");
//
//            String paypalSourceDir = "";
//            try
//            {
//                paypalSourceDir = "sources\\" + files[Integer.parseInt(uReader.readLine()) - 1].getName();
//                ConsoleMessage.br();
//            }
//            catch (IOException iox)
//            {
//                ConsoleMessage.error(iox, "Something has gone very wrong whilst attempting to read user input.");
//            }
//
//            try
//            {
//                ConsoleMessage.info("Attempting to read main.file \"" + paypalSourceDir + "\"...");
//
//                paypalReader = new PaypalReader(paypalSourceDir);
//                paypalReader.read();
//                paypalReader.readPayPal();
//
//                ConsoleMessage.info("DONE!");
//                ConsoleMessage.br();
//
//                break;
//            }
//            catch (IOException e1)
//            {
//                ConsoleMessage.warning("File reading failed!");
//                ConsoleMessage.info("Make sure the main.file name is typed correctly, case sensitive.");
//                ConsoleMessage.info("Try again!");
//                ConsoleMessage.br();
//                continue;
//            }
//        }
//
//        // Read Exchange Rate source file.
//        while (true)
//        {
//            ConsoleMessage.input("Specify " + Ansi.RED + "Exchange Rate" + Ansi.YELLOW + " source file!");
//
//            String exchangeRateSourceDir = "";
//            try
//            {
//                exchangeRateSourceDir = "sources\\" + files[Integer.parseInt(uReader.readLine()) - 1].getName();
//                ConsoleMessage.br();
//            }
//            catch (IOException iox)
//            {
//                ConsoleMessage.error(iox, "Something has gone very wrong whilst attempting to read user input.");
//            }
//
//            try
//            {
//                ConsoleMessage.info("Attempting to read main.file \"" + exchangeRateSourceDir + "\"...");
//
//                excReader = new ExchangeRateReader(exchangeRateSourceDir);
//                excReader.read();
//                excReader.readExchangeRate();
//
//                ConsoleMessage.info("DONE!");
//                ConsoleMessage.br();
//
//                break;
//            }
//            catch (IOException e1)
//            {
//                ConsoleMessage.warning("File reading failed!");
//                ConsoleMessage.info("Make sure the main.file name is typed correctly, case sensitive.");
//                ConsoleMessage.info("Try again!");
//                ConsoleMessage.br();
//                continue;
//            }
//        }
//
//        //</editor-fold>
//
//        //<editor-fold desc="1A. PAYPAL: Group by Invoice">
//        PaypalGrouper paypalGrouper = new PaypalGrouper(paypalReader.getContents());
//
//        paypalGrouper.group();
//        paypalGrouper.calculate();
//        //</editor-fold>
//
//        //<editor-fold desc="1B. PAYPAL: Matching">
//        Matcher_PaymentData_PayPalGBN pdppMatcher = new Matcher_PaymentData_PayPalGBN(
//                paymentReader.getContents(),
//                paypalGrouper.getGroupedArray()
//        );
//        try
//        {
//            pdppMatcher.process();
//        }
//        catch (ParseException e)
//        {
//            ConsoleMessage.error(e, "Parse failure!");
//        }
//
//        //</editor-fold>
//
//        //<editor-fold desc="2A. BT, CB, CC, T: Exchange Rate, Group by Source">
//        ExchangerateSourceGrouper excGrouper = new ExchangerateSourceGrouper(excReader.getContents());
//
//        excGrouper.group();
//        //</editor-fold>
//
//        //<editor-fold desc="2A. BT, CB, CC, T: Exchange Rate, Matching">
//        Matcher_ExchangeRate excMatcher = new Matcher_ExchangeRate(paymentReader.getContents(), excGrouper.getGroupedArray());
//
//        excMatcher.process();
//        //</editor-fold>
//
//        //<editor-fold desc="3. AfterPay: Apply Formula">
//        /*
//        Amount - GST - a( b( c(Amount * Rate)c + 0.3)b + b( c(Amount * Rate)c + 0.3)b * GST )a
//         */
//        AfterPayRateProcessor app = new AfterPayRateProcessor(paymentReader.getContents());
//
//        try
//        {
//            app.process();
//        }
//        catch (FileNotFoundException x)
//        {
//            ConsoleMessage.error(x, "Rates configuration file not found!");
//        }
//        //</editor-fold>
//
//        //<editor-fold desc="Writing">
//        ConsoleMessage.info("Writing results...");
//
//        // Instantiate writer(s).
//        OldWriter<MatchPDPP, PaymentData>       pdppWriter  = new OldWriter<>(); // PD(PayPal) with PayPal
//        OldWriter<MatchPDExc, PaymentData>      pdExcWriter = new OldWriter<>(); // PD(Others) with Exchange Rate
//        OldWriter<MatchAfterPay, MatchAfterPay> apWriter    = new OldWriter(); // AfterPay...
//
//        // Matched data
//        pdppWriter.setMatches(pdppMatcher.getSidMatches());
//        pdExcWriter.setMatches(excMatcher.getMatches());
//
//        // Singles data
//        pdppWriter.setSingles(pdppMatcher.getPdSingles());
//        pdExcWriter.setSingles(excMatcher.getPdSingles());
//
//        // AfterPay data
//        apWriter.setMatches(app.getMatches());
//
//        // Attempt to write.
//        while (true)
//        {
//            try
//            {
//                // Start with header first.
//                pdppWriter.writeMatchHeader("results\\matches.csv");
//                pdppWriter.writeMatches("results\\matches.csv", true);
//                pdExcWriter.writeMatches("results\\matches.csv", true);
//                apWriter.writeMatches("results\\matches.csv", true);
//
//                // Start with header first.
//                pdppWriter.writeSinglesHeader("results\\no_matches.csv");
//                pdppWriter.writeSingles("results\\no_matches.csv", true);
//                pdExcWriter.writeSingles("results\\matches.csv", true);
//
//                break;
//            }
//            catch (IOException iox)
//            {
//                ConsoleMessage.warning("Failed to write to files. Please close all result files, then try again.");
//            }
//        }
//
//        ConsoleMessage.info("DONE!");
//        ConsoleMessage.br();
//
////        M.info("# of PaymentData source: " + Ansi.DARK_YELLOW + paymentReader.getContents().size());
////        M.info("# of PayPal source (GROUPED): " + Ansi.DARK_YELLOW + paypalGrouper.getGroupedArray().size());
////        M.br();
//
////        M.warning("PayPal data with a blank 'Invoice Number' field are ignored!");
////        M.br();
//
//        int pdppNum  = pdppMatcher.getSidMatches().size();
//        int excNum   = excMatcher.getMatches().size();
//        int apNum    = app.getMatches().size();
//        int matchNum = pdppNum + excNum + apNum;
//        ConsoleMessage.info("Matches: " + Ansi.DARK_YELLOW + matchNum);
//        ConsoleMessage.br();
//
//        ConsoleMessage.info("No-matches: " + Ansi.DARK_YELLOW + pdppMatcher.getPdSingles().size());
//        ConsoleMessage.br();
//
//        File dirMatches   = new File("results\\matches.csv");
//        File dirNoMatches = new File("results\\no_matches.csv");
//
//        ConsoleMessage.info("Results can be found in the " + Ansi.DARK_YELLOW + "./results/" + Ansi.CYAN + " folder.");
//        ConsoleMessage.info(dirMatches.getAbsolutePath());
//        ConsoleMessage.info(dirNoMatches.getAbsolutePath());
//        ConsoleMessage.br();
//        //</editor-fold>
//        //</editor-fold>
//    }
}
