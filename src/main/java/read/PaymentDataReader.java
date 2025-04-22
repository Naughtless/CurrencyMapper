package main.java.read;

import main.java.process.common.PaymentData;
import main.java.read.common.CSVReader;

import java.util.ArrayList;

public class PaymentDataReader
        extends CSVReader
{
    public ArrayList<PaymentData> readPaymentData(String dir)
    {
        ArrayList<String[]>    results  = super.read(dir);
        ArrayList<PaymentData> contents = new ArrayList<>();

        for (int i = 0; i < results.size(); i++)
        {
            if (i > 0)
            {
                contents.add(new PaymentData(
                        results.get(i)[0],
                        results.get(i)[1],
                        results.get(i)[2],
                        results.get(i)[3],
                        results.get(i)[4],
                        results.get(i)[5],
                        results.get(i)[6],
                        results.get(i)[7],
                        results.get(i)[8],
                        results.get(i)[9],
                        results.get(i)[10],
                        results.get(i)[11],
                        results.get(i)[12],
                        results.get(i)[13],
                        results.get(i)[14],
                        results.get(i)[15],
                        results.get(i)[16],
                        results.get(i)[17],
                        results.get(i)[18]
                ));
            }
        }

        return contents;
    }
}
