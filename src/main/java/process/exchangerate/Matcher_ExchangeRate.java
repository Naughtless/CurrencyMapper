package main.java.process.exchangerate;

import main.java.process.exchangerate.model.ExchangeRateGrouped;
import main.java.common.PaymentData;
import main.java.common.ExchangeRate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Matcher_ExchangeRate
{
    //<editor-fold desc="Properties">
    private ArrayList<PaymentData>               pdSource;
    private HashMap<String, ExchangeRateGrouped> excSource;

    private ArrayList<MatchPDExc>  matches;
    private ArrayList<PaymentData> pdSingles;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public Matcher_ExchangeRate() {}

    public Matcher_ExchangeRate(ArrayList<PaymentData> pdSource, HashMap<String, ExchangeRateGrouped> excSource)
    {
        this.pdSource = pdSource;
        this.excSource = excSource;

        this.matches = new ArrayList<>();
        this.pdSingles = new ArrayList<>();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void process()
    {
        // Cycle-check for appropriate exchange AfterPay Rate.txt, with priority:
                /*
                1. 'Monthly Exch Rate'
                2. Anything else. ('RBA rate' & 'XE Rate')
                 */

        // -> First declare iterator so we can modify pdSource without index jumping issue.
        Iterator<PaymentData> itr = pdSource.iterator();

        // Iteration 1: Eliminate 'AUD' pd currencies. No conversion needed.
        while (itr.hasNext())
        {
            PaymentData cpd = itr.next();

            String pt       = cpd.getPaymentType().trim();
            String currency = cpd.getCurrency().trim();

            /*
            Process Only:
            - BT:   'BankTransfer'
            - CB:   'ChargeBack'
            - CC:   'CreditCard'
            - T:    'Transfer'
             */
            if (pt.equals("BankTransfer") || pt.equals("ChargeBack") || pt.equals("CreditCard") || pt.equals("Transfer"))
            {
                if (currency.equals("AUD"))
                {
                    matches.add(new MatchPDExc(cpd, null));
                    itr.remove();
                }
            }
        }

        // Iteration 2: Find in 'Monthly Exch Rate'
        itr = pdSource.iterator(); // New iterator
        ExchangeRateGrouped monthlyExchangeRate = excSource.get("monthly exch rate");
        while (itr.hasNext())
        {
            PaymentData cpd = itr.next();

            String pt       = cpd.getPaymentType().trim();
            String currency = cpd.getCurrency().trim();

            /*
            Process Only:
            - BT:   'BankTransfer'
            - CB:   'ChargeBack'
            - CC:   'CreditCard'
            - T:    'Transfer'
             */
            if (pt.equals("BankTransfer") || pt.equals("ChargeBack") || pt.equals("CreditCard") || pt.equals("Transfer"))
            {
                for (ExchangeRate cur : monthlyExchangeRate.getMembers())
                {
                    if (cur.getCurrency().equals(currency))
                    {
                        matches.add(new MatchPDExc(cpd, cur));
                        itr.remove();
                    }
                }
            }
        }

        // Iteration 3: Find in 'RBA rate'
        itr = pdSource.iterator(); // New iterator
        ExchangeRateGrouped rbaRate = excSource.get("rba rate");
        while (itr.hasNext())
        {
            PaymentData cpd = itr.next();

            String pt       = cpd.getPaymentType().trim();
            String currency = cpd.getCurrency().trim();

            /*
            Process Only:
            - BT:   'BankTransfer'
            - CB:   'ChargeBack'
            - CC:   'CreditCard'
            - T:    'Transfer'
             */
            if (pt.equals("BankTransfer") || pt.equals("ChargeBack") || pt.equals("CreditCard") || pt.equals("Transfer"))
            {
                for (ExchangeRate cur : rbaRate.getMembers())
                {
                    if (cur.getCurrency().equals(currency))
                    {
                        matches.add(new MatchPDExc(cpd, cur));
                        itr.remove();
                    }
                }
            }
        }

        // Iteration 4: Find in 'XE Rate'
        itr = pdSource.iterator(); // New iterator
        ExchangeRateGrouped xeRate = excSource.get("xe rate");
        while (itr.hasNext())
        {
            PaymentData cpd = itr.next();

            String pt       = cpd.getPaymentType().trim();
            String currency = cpd.getCurrency().trim();

            /*
            Process Only:
            - BT:   'BankTransfer'
            - CB:   'ChargeBack'
            - CC:   'CreditCard'
            - T:    'Transfer'
             */
            if (pt.equals("BankTransfer") || pt.equals("ChargeBack") || pt.equals("CreditCard") || pt.equals("Transfer"))
            {
                for (ExchangeRate cur : xeRate.getMembers())
                {
                    if (cur.getCurrency().equals(currency))
                    {
                        matches.add(new MatchPDExc(cpd, cur));
                        itr.remove();
                    }
                }
            }
        }

        // Iteration FINAL: Finally the remnants with no matches!
        itr = pdSource.iterator();
        while (itr.hasNext())
        {
            PaymentData cpd = itr.next();

            String pt = cpd.getPaymentType().trim();

            /*
            NOT PROCESSED YET, but still fulfills the following conditions:
            - BT:   'BankTransfer'
            - CB:   'ChargeBack'
            - CC:   'CreditCard'
            - T:    'Transfer'
             */
            if (pt.equals("BankTransfer") || pt.equals("ChargeBack") || pt.equals("CreditCard") || pt.equals("Transfer"))
            {
                pdSingles.add(cpd);
                itr.remove();
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public ArrayList<PaymentData> getPdSource()
    {
        return pdSource;
    }

    public void setPdSource(ArrayList<PaymentData> pdSource)
    {
        this.pdSource = pdSource;
    }

    public HashMap<String, ExchangeRateGrouped> getExcSource()
    {
        return excSource;
    }

    public void setExcSource(HashMap<String, ExchangeRateGrouped> excSource)
    {
        this.excSource = excSource;
    }

    public ArrayList<MatchPDExc> getMatches()
    {
        return matches;
    }

    public void setMatches(ArrayList<MatchPDExc> matches)
    {
        this.matches = matches;
    }

    public ArrayList<PaymentData> getPdSingles()
    {
        return pdSingles;
    }

    public void setPdSingles(ArrayList<PaymentData> pdSingles)
    {
        this.pdSingles = pdSingles;
    }
    //</editor-fold>
}
