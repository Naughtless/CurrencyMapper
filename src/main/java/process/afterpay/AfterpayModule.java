package main.java.process.afterpay;

import main.java.common.Match;
import main.java.common.PaymentData;
import main.java.process.afterpay.model.AfterpayRate;
import main.java.util.ConsoleMessage;

import java.util.ArrayList;
import java.util.Iterator;

public class AfterpayModule
{
    private ArrayList<PaymentData> rawMaster;
    private AfterpayRate           afterpayRate;

    public AfterpayModule(ArrayList<PaymentData> rawMaster, AfterpayRate afterpayRate)
    {
        this.rawMaster = rawMaster;
        this.afterpayRate = afterpayRate;
    }

    public ArrayList<Match> getMatches()
    {
        ArrayList<Match> matches = new ArrayList<>();

        Iterator<PaymentData> masterIterator = rawMaster.iterator();
        while (masterIterator.hasNext())
        {
            PaymentData currentMaster = masterIterator.next();

            if (currentMaster.getPaymentType().equalsIgnoreCase("AfterPay"))
            {
                if (currentMaster.getCurrency().equalsIgnoreCase("AUD"))
                {
                    double net = afterpayRate.calculateNet(Double.parseDouble(
                            currentMaster.getAmount()));
                    matches.add(new Match(
                            currentMaster,
                            "AfterPay",
                            String.valueOf(net),
                            new String[]{}
                    ));
                    ConsoleMessage.debug("[AfterPay] Match found for SID: " + currentMaster.getStudentId());
                    masterIterator.remove();
                }
                else
                {
                    ConsoleMessage.warning(
                            "[AfterPay] Found an AfterPay entry with a non-AUD currency! -> Assuming 'Non-Match'.");
                }
            }
        }
        
        return matches;
    }

    public int getRemainder()
    {
        int counter = 0;
        for(PaymentData pd : rawMaster)
        {
            if(pd.getPaymentType().equalsIgnoreCase("AfterPay")) counter++;
        }

        return counter;
    }
}
