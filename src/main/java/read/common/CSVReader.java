package main.java.read.common;

import main.java.util.ConsoleMessage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader
{
    public ArrayList<String[]> read(String dir)
    {
        ArrayList<String[]> results = new ArrayList<>();
        BufferedReader      reader  = null;
        try
        {
            reader = new BufferedReader(new FileReader(dir));
        }
        catch (FileNotFoundException e)
        {
            ConsoleMessage.error(e, "FATAL - Could not find file!" + " Attempted to read: " + dir);
        }
        String line;
        
        try
        {
            while ((line = reader.readLine()) != null)
            {
                String fixed =
                        // Clean the string.
                        line.replace("\n", "").replace("\r", "").replace(System.lineSeparator(), "")
                        // Add empty entry at the end of the string for more predictable results.
                        + "|?";

                // Split by "|" separator.
                results.add(fixed.split("\\|"));
            }
        }
        catch (IOException e)
        {
            ConsoleMessage.error(e, "FATAL - Could not read file!");
        }
        catch (NullPointerException e)
        {
            ConsoleMessage.error(e, "FATAL - Could not instantiate BufferedReader!");
        }

        return results;
    }
}
