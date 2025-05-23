package main.java.read;

import main.java.process.afterpay.AfterpayModule;
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
        double fee_percentage = -1;
        double fee_flat = -1;
        double gst  = -1;

        try (BufferedReader rateReader = new BufferedReader(new FileReader(dir)))
        {
            String line;
            while ((line = rateReader.readLine()) != null)
            {
                String[] parts = line.split("=");
                if ("fee_percentage".equals(parts[0].trim())) 
                    fee_percentage = Double.parseDouble(parts[1].trim());
                if ("fee_flat".equals(parts[0].trim()))
                    fee_flat = Double.parseDouble(parts[1].trim());
                if ("gst".equals(parts[0].trim())) 
                    gst = Double.parseDouble(parts[1].trim());
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

        if (fee_percentage != -1 && gst != -1)
        {
            return new AfterpayRate(fee_percentage, fee_flat, gst);
        }

        ConsoleMessage.error(
                "FATAL - Read 'AfterPay' sucessfully but unable to comprehend it!"
        );
        ConsoleMessage.warning(
                "That doesn't look like an 'AfterPay' file! Are you being silly? Is the file dirty/broken?"
        );
        System.exit(1);
        
        return null;
    }
}
