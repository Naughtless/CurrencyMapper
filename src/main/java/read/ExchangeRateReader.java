package main.java.read;

import main.java.process.exchangerate.model.ExchangeRate;
import main.java.read.common.CSVReader;
import main.java.util.ConsoleMessage;

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
                try
                {
                    contents.add(
                            new ExchangeRate(
                                    results.get(i)[0].trim(),
                                    results.get(i)[1].trim(),
                                    results.get(i)[2].trim()
                            )
                    );
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    ConsoleMessage.error("FATAL - Read 'ExchangeRate' sucessfully but unable to comprehend it!");
                    ConsoleMessage.warning(
                            "That doesn't look like a 'ExchangeRate' file! Are you being silly? Is the file dirty/broken?"
                    );
                    System.exit(1);
                }
                
            }
        }

        return contents;
    }
}
