package main.java.process.afterpay;

import main.java.process.afterpay.model.AfterpayRate;
import main.java.process.common.Match;
import main.java.process.common.PaymentData;

import java.util.ArrayList;

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

    }
}
