package main.java.read;

import main.java.process.afterpay.model.AfterpayRate;
import main.java.util.ConsoleMessage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AfterpayReader
{
    public AfterpayRate readRate(String dir)
    {
        double rate = -1;
        double gst  = -1;

        try (BufferedReader rateReader = new BufferedReader(new FileReader(dir)))
        {
            String line;
            while ((line = rateReader.readLine()) != null)
            {
                String[] parts = line.split("=");
                if ("rate".equals(parts[0].trim())) rate = Double.parseDouble(parts[1].trim());
                if ("gst".equals(parts[0].trim())) gst = Double.parseDouble(parts[1].trim());
            }
        }
        catch (FileNotFoundException e)
        {
            ConsoleMessage.error(e, "FATAL - Could not find file!" + " Attempted to read: " + dir);
        }
        catch (IOException e)
        {
            ConsoleMessage.error(e, "FATAL - Could not read file!");
        }

        if (rate != -1 && gst != -1)
        {
            return new AfterpayRate(rate, gst);
        }
        
        ConsoleMessage.error(
                new RuntimeException("FATAL - Read sucessfully but unable to get rate for AfterPay!"),
                "FATAL - Read sucessfully but unable to get rate for AfterPay!"
        );
        return null;
    }
}
