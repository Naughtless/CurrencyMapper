package main.file.reader.format;

import main.data.paymentdata.PaymentData;
import main.file.reader.CSVReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class PaymentDataReader extends CSVReader {
    //<editor-fold desc="Properties">
    private ArrayList<PaymentData> contents;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public PaymentDataReader(String dir) throws FileNotFoundException {
        super(dir);
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void process() {
        contents = new ArrayList<>();

        for(int i = 0; i < getResults().size(); i++) {
            if(i>0) {
                contents.add(new PaymentData(
                        getResults().get(i)[0],
                        getResults().get(i)[1],
                        getResults().get(i)[2],
                        getResults().get(i)[3],
                        getResults().get(i)[4],

                        getResults().get(i)[5],
                        getResults().get(i)[6],
                        getResults().get(i)[7],
                        getResults().get(i)[8],
                        getResults().get(i)[9],

                        getResults().get(i)[10],
                        getResults().get(i)[11],
                        getResults().get(i)[12],
                        getResults().get(i)[13],
                        getResults().get(i)[14],

                        getResults().get(i)[15],
                        getResults().get(i)[16],
                        getResults().get(i)[17],
                        getResults().get(i)[18]
                ));
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public ArrayList<PaymentData> getContents() {
        return contents;
    }

    public void setContents(ArrayList<PaymentData> contents) {
        this.contents = contents;
    }
    //</editor-fold>
}
