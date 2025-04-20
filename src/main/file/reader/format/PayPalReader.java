package main.file.reader.format;

import main.data.paymentdata.PaymentData;
import main.data.paypal.PayPal;
import main.file.reader.CSVReader;

import java.io.IOException;
import java.util.ArrayList;

public class PayPalReader extends CSVReader {
    //<editor-fold desc="Properties">
    private ArrayList<PayPal> contents;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public PayPalReader(String dir) throws IOException {
        super(dir);
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void process() {
        contents = new ArrayList<>();

        // DEBUG!
//        for(String[] p: getResults()) {
//            if(p[3].contains("Alexa Ditz")) {
//                System.out.println("DATE: " + p[0]);
//            }
//        }

        for(int i = 0; i < getResults().size(); i++) {
            if(i>0) {
                contents.add(new PayPal(
                        getResults().get(i)[0],
                        getResults().get(i)[1],
                        getResults().get(i)[2],
                        getResults().get(i)[3], // Name
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
                        getResults().get(i)[18],
                        getResults().get(i)[19],

                        getResults().get(i)[20],
                        getResults().get(i)[21],
                        getResults().get(i)[22],
                        getResults().get(i)[23],
                        getResults().get(i)[24],

                        getResults().get(i)[25],
                        getResults().get(i)[26],
                        getResults().get(i)[27],
                        getResults().get(i)[28],
                        getResults().get(i)[29],

                        getResults().get(i)[30],
                        getResults().get(i)[31],
                        getResults().get(i)[32],
                        getResults().get(i)[33],
                        getResults().get(i)[34],

                        getResults().get(i)[35],
                        getResults().get(i)[36],
                        getResults().get(i)[37],
                        getResults().get(i)[38],
                        getResults().get(i)[39],

                        getResults().get(i)[40],
                        getResults().get(i)[41],
                        getResults().get(i)[42],
                        getResults().get(i)[43],
                        getResults().get(i)[44],

                        getResults().get(i)[45],
                        getResults().get(i)[46],
                        getResults().get(i)[47]
                ));
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public ArrayList<PayPal> getContents() {
        return contents;
    }

    public void setContents(ArrayList<PayPal> contents) {
        this.contents = contents;
    }
    //</editor-fold>
}
