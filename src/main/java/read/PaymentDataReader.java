package main.java.read;

import main.java.common.PaymentData;
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
                        results.get(i)[0].trim(),
                        results.get(i)[1].trim(),
                        results.get(i)[2].trim(),
                        results.get(i)[3].trim(),
                        results.get(i)[4].trim(),
                        results.get(i)[5].trim(),
                        results.get(i)[6].trim(),
                        results.get(i)[7].trim(),
                        results.get(i)[8].trim(),
                        results.get(i)[9].trim(),
                        results.get(i)[10].trim(),
                        results.get(i)[11].trim(),
                        results.get(i)[12].trim(),
                        results.get(i)[13].trim(),
                        results.get(i)[14].trim(),
                        results.get(i)[15].trim(),
                        results.get(i)[16].trim(),
                        results.get(i)[17].trim(),
                        results.get(i)[18].trim()
                ));
            }
        }

        return contents;
    }
}
