package main.java.read.externaldata;

import main.java.read.CSVReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ExchangeRateReader extends CSVReader
{
    private ArrayList<ExchangeRate> contents;

    public ExchangeRateReader(String dir) throws FileNotFoundException {
        super(dir);
    }
    
    public void process() {
        contents = new ArrayList<>();

        for(int i = 0; i < getResults().size(); i++) {
            if(i>0) {
                contents.add(new ExchangeRate(
                        getResults().get(i)[0],
                        getResults().get(i)[1],
                        getResults().get(i)[2]
                ));
            }
        }
    }
    
    //<editor-fold desc="Getters & Setters">
    public ArrayList<ExchangeRate> getContents() {
        return contents;
    }

    public void setContents(ArrayList<ExchangeRate> contents) {
        this.contents = contents;
    }
    //</editor-fold>
}
