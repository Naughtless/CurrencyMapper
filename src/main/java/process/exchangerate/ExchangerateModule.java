package main.java.process.exchangerate;

import main.java.process.common.Match;
import main.java.process.common.PaymentData;
import main.java.process.exchangerate.model.ExchangeRate;

import java.util.ArrayList;

public class ExchangerateModule
{
    private ArrayList<PaymentData>  rawMaster;
    private ArrayList<ExchangeRate> rawExchrate;

    public ExchangerateModule(ArrayList<PaymentData> rawMaster, ArrayList<ExchangeRate> rawExchrate)
    {
        this.rawMaster = rawMaster;
        this.rawExchrate = rawExchrate;
    }

    public ArrayList<Match> getMatches()
    {

    }
}
