package main.java.read;

import main.java.process.paypal.model.PayPal;
import main.java.read.common.CSVReader;
import main.java.util.ConsoleMessage;

import java.util.ArrayList;

public class PayPalReader
        extends CSVReader
{
    public ArrayList<PayPal> readPayPal(String dir)
    {
        ArrayList<String[]> results  = super.read(dir);
        ArrayList<PayPal>   contents = new ArrayList<>();

        for (int i = 0; i < results.size(); i++)
        {
            if (i > 0)
            {
                try
                {
                    contents.add(new PayPal(
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
                            results.get(i)[18].trim(),
                            results.get(i)[19].trim(),
                            results.get(i)[20].trim(),
                            results.get(i)[21].trim(),
                            results.get(i)[22].trim(),
                            results.get(i)[23].trim(),
                            results.get(i)[24].trim(),
                            results.get(i)[25].trim(),
                            results.get(i)[26].trim(),
                            results.get(i)[27].trim(),
                            results.get(i)[28].trim(),
                            results.get(i)[29].trim(),
                            results.get(i)[30].trim(),
                            results.get(i)[31].trim(),
                            results.get(i)[32].trim(),
                            results.get(i)[33].trim(),
                            results.get(i)[34].trim(),
                            results.get(i)[35].trim(),
                            results.get(i)[36].trim(),
                            results.get(i)[37].trim(),
                            results.get(i)[38].trim(),
                            results.get(i)[39].trim(),
                            results.get(i)[40].trim(),
                            results.get(i)[41].trim(),
                            results.get(i)[42].trim(),
                            results.get(i)[43].trim(),
                            results.get(i)[44].trim(),
                            results.get(i)[45].trim(),
                            results.get(i)[46].trim(),
                            results.get(i)[47].trim()
                    ));
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    ConsoleMessage.error("FATAL - Read 'PayPal' sucessfully but unable to comprehend it!");
                    ConsoleMessage.warning(
                            "That doesn't look like a 'PayPal' file! Are you being silly? Is the file dirty/broken?"
                    );
                    System.exit(1);
                }
                
            }
        }
        
        return contents;
    }
}
