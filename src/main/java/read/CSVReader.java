package main.java.read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader
{
    //<editor-fold desc="Properties">
    private BufferedReader      reader;
    private ArrayList<String[]> results;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public CSVReader(String dir)
            throws FileNotFoundException
    {
        reader = new BufferedReader(new FileReader(dir));
        results = new ArrayList<>();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void readFile()
            throws IOException
    {
        String line;
        while ((line = reader.readLine()) != null)
        {
            String fixed = line.replace("\n", "").replace("\r", "").replace(System.lineSeparator(), "");

            fixed += "|x";

            results.add(fixed.split("\\|"));
        }
    }
    //</editor-fold>

    //<editor-fold desc="Setters & Getters">
    public BufferedReader getReader()
    {
        return reader;
    }

    public void setReader(BufferedReader reader)
    {
        this.reader = reader;
    }

    public ArrayList<String[]> getResults()
    {
        return results;
    }

    public void setResults(ArrayList<String[]> results)
    {
        this.results = results;
    }
    //</editor-fold>
}
