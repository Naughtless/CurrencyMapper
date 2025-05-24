package main.java.process.exchangerate;

import main.java.common.Match;
import main.java.common.PaymentData;
import main.java.process.exchangerate.model.ExchangeRate;
import main.java.util.ConsoleMessage;

import java.util.*;

public class ExchangerateModule
{
    private ArrayList<PaymentData>  rawMaster;
    private ArrayList<ExchangeRate> rawExchrate;

    public ExchangerateModule(
            ArrayList<PaymentData> rawMaster,
            ArrayList<ExchangeRate> rawExchrate
    )
    {
        this.rawMaster = rawMaster;
        this.rawExchrate = rawExchrate;
    }

    public ArrayList<Match> getMatches()
    {

        // 1. Exchangerate needs to be grouped by type (MER, RBA, XE)]
        ArrayList<ExchangeRate> merRate = getRatesMER(rawExchrate);
        ArrayList<ExchangeRate> rbaRate = getRatesRBA(rawExchrate);
        ArrayList<ExchangeRate> xeRate  = getRatesXE(rawExchrate);

        ConsoleMessage.debug("Grouped rates: MER=" + merRate.size() + ", RBA=" + rbaRate.size() + ", XE=" + xeRate.size());

        // 2. Return matches between 'ratesMap' and 'rawMaster'
        return match(rawMaster, merRate, rbaRate, xeRate);
    }

    private ArrayList<Match> match(
            ArrayList<PaymentData> rawMaster,
            ArrayList<ExchangeRate> mer,
            ArrayList<ExchangeRate> rba,
            ArrayList<ExchangeRate> xe
    )
    {
        ConsoleMessage.debug("Starting match()...");
        ArrayList<Match> matches = new ArrayList<>();

        Set<String> allowedTypes = Set.of(
                "BankTransfer",
                "ChargeBack",
                "CreditCard",
                "Transfer"
        );

        Map<String, ExchangeRate> exchangeRateMap = new HashMap<>();
        for (ExchangeRate rate : mer)
        {
            exchangeRateMap.putIfAbsent(rate.getCurrency().toUpperCase(), rate);
        }
        for (ExchangeRate rate : rba)
        {
            exchangeRateMap.putIfAbsent(rate.getCurrency().toUpperCase(), rate);
        }
        for (ExchangeRate rate : xe)
        {
            exchangeRateMap.putIfAbsent(rate.getCurrency().toUpperCase(), rate);
        }

        ConsoleMessage.debug("Built exchangeRateMap with " + exchangeRateMap.size() + " entries.");

        Iterator<PaymentData> masterIterator = rawMaster.iterator();
        while (masterIterator.hasNext())
        {
            // Pull out common vars.
            PaymentData currentMaster  = masterIterator.next();
            String      masterType     = currentMaster.getPaymentType();
            String      masterCurrency = currentMaster.getCurrency().toUpperCase();

            /* Fck around to generate filter vars here. */
            // This is a var to check if the PayPal data (amount) is broken or not.
            String amountStr = currentMaster.getAmount();
            boolean isAmountDouble = true;
            try {
                Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                isAmountDouble = false;
            }

            /* APPLY ACTUAL FILTER HERE. */
            if (
                    !allowedTypes.contains(masterType) 
                    || !isAmountDouble
            ) continue;

            ConsoleMessage.debug("Processing exchange rate for SID: " + currentMaster.getStudentId());

            if (masterCurrency.equals("AUD"))
            {
                matches.add(new Match(
                        currentMaster,
                        "EXC - Already AUD",
                        currentMaster.getAmount(),
                        new String[]{}
                ));
                ConsoleMessage.debug("Match found (AUD) for SID: " + currentMaster.getStudentId());
                masterIterator.remove();
            }
            else
            {
                ExchangeRate rate = exchangeRateMap.get(masterCurrency);
                if (rate != null)
                {
                    matches.add(new Match(currentMaster, rate, "EXC - Converted"));
                    ConsoleMessage.debug(
                            "Match found (Converted) for SID: " + currentMaster.getStudentId() 
                            + " using rate " + rate.getSource() 
                            + " on currency " + rate.getCurrency()
                    );
                    masterIterator.remove();
                }
                else
                {
                    ConsoleMessage.warning("[Exchange Rate] No exchange rate / currency combination found for SID: " + currentMaster.getStudentId());
                }
            }
        }

        ConsoleMessage.debug("Exchange rate matching complete. Total matches: " + matches.size());
        return matches;
    }

    private ArrayList<ExchangeRate> getRatesMER(ArrayList<ExchangeRate> rawExchrate)
    {
        ArrayList<ExchangeRate> results = new ArrayList<>();

        for (int i = 0; i < rawExchrate.size(); i++)
        {
            ExchangeRate cr = rawExchrate.get(i);
            if (cr.getRate().isEmpty()) continue;
            if (cr.getSource().toLowerCase().equals("monthly exch rate"))
            {
                results.add(cr);
            }
        }

        ConsoleMessage.debug("MER rates extracted: " + results.size());
        return results;
    }

    private ArrayList<ExchangeRate> getRatesRBA(ArrayList<ExchangeRate> rawExchrate)
    {
        ArrayList<ExchangeRate> results = new ArrayList<>();

        for (int i = 0; i < rawExchrate.size(); i++)
        {
            ExchangeRate cr = rawExchrate.get(i);
            if (cr.getRate().isEmpty()) continue;
            if (cr.getSource().toLowerCase().equals("rba rate"))
            {
                results.add(cr);
            }
        }

        ConsoleMessage.debug("RBA rates extracted: " + results.size());
        return results;
    }

    private ArrayList<ExchangeRate> getRatesXE(ArrayList<ExchangeRate> rawExchrate)
    {
        ArrayList<ExchangeRate> results = new ArrayList<>();

        for (int i = 0; i < rawExchrate.size(); i++)
        {
            ExchangeRate cr = rawExchrate.get(i);
            if (cr.getRate().isEmpty()) continue;
            if (cr.getSource().toLowerCase().equals("xe rate"))
            {
                results.add(cr);
            }
        }

        ConsoleMessage.debug("XE rates extracted: " + results.size());
        return results;
    }
    
    public int getRemainder() 
    {
        Set<String> allowedTypes = Set.of(
                "BankTransfer",
                "ChargeBack",
                "CreditCard",
                "Transfer"
        );
        
        int counter = 0;
        for(PaymentData pd : rawMaster)
        {
            if (allowedTypes.contains(pd.getPaymentType())) counter++;
        }
        
        return counter;
    }
}
