package main.java.read;

import main.java.process.paypal.model.Paypal;
import main.java.read.common.CSVReader;

import java.util.ArrayList;

public class PaypalReader
        extends CSVReader
{
    public ArrayList<Paypal> readPayPal(String dir)
    {
        ArrayList<String[]> results  = super.read(dir);
        ArrayList<Paypal>   contents = new ArrayList<>();

        for (int i = 0; i < results.size(); i++)
        {
            if (i > 0)
            {
                contents.add(new Paypal(
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
                        results.get(i)[18],
                        results.get(i)[19],
                        results.get(i)[20],
                        results.get(i)[21],
                        results.get(i)[22],
                        results.get(i)[23],
                        results.get(i)[24],
                        results.get(i)[25],
                        results.get(i)[26],
                        results.get(i)[27],
                        results.get(i)[28],
                        results.get(i)[29],
                        results.get(i)[30],
                        results.get(i)[31],
                        results.get(i)[32],
                        results.get(i)[33],
                        results.get(i)[34],
                        results.get(i)[35],
                        results.get(i)[36],
                        results.get(i)[37],
                        results.get(i)[38],
                        results.get(i)[39],
                        results.get(i)[40],
                        results.get(i)[41],
                        results.get(i)[42],
                        results.get(i)[43],
                        results.get(i)[44],
                        results.get(i)[45],
                        results.get(i)[46],
                        results.get(i)[47]
                ));
            }
        }
        
        return contents;
    }
}
