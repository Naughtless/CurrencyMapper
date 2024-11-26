package main.file.reader.format;

import main.data.exchangerate.ExchangeRate;
import main.data.paymentdata.PaymentData;
import main.file.reader.CSVReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ExchangeRateReader extends CSVReader {
    //<editor-fold desc="Properties">
    private ArrayList<ExchangeRate> contents;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public ExchangeRateReader(String dir) throws FileNotFoundException {
        super(dir);
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
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
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public ArrayList<ExchangeRate> getContents() {
        return contents;
    }

    public void setContents(ArrayList<ExchangeRate> contents) {
        this.contents = contents;
    }
    //</editor-fold>
}
