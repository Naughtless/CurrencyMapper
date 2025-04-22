package main.java.read;

import main.java.process.exchangerate.model.ExchangeRate;
import main.java.read.common.CSVReader;

import java.util.ArrayList;

public class ExchangeRateReader
        extends CSVReader
{

    public ArrayList<ExchangeRate> readExchangeRate(String dir)
    {
        ArrayList<String[]>     results  = super.read(dir);
        ArrayList<ExchangeRate> contents = new ArrayList<>();

        for (int i = 0; i < results.size(); i++)
        {
            if (i > 0)
            {
                contents.add(new ExchangeRate(results.get(i)[0], results.get(i)[1], results.get(i)[2]));
            }
        }

        return contents;
    }
}
