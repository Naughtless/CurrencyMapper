package main.java.OLDfile.grouper;

import main.java.read.externaldata.ExchangeRate;
import main.java.OLDdata.exchangerate.ExchangeRateGBSource;
import main.java.OLDdata.exchangerate.comparator.ExchangeRateComparatorSource;

import java.util.ArrayList;
import java.util.HashMap;

public class ExchangeRateSourceGrouper {
    private ArrayList<ExchangeRate> sourceArray;
    private HashMap<String, ExchangeRateGBSource> groupedArray;

    public ExchangeRateSourceGrouper() {}
    public ExchangeRateSourceGrouper(ArrayList<ExchangeRate> sourceArray) {
        this.sourceArray = sourceArray;

        this.sourceArray.sort(new ExchangeRateComparatorSource());
    }

    public void group() {
        groupedArray = new HashMap<>();

        for(int i = 0; i < sourceArray.size(); i++) {
            // Expose necessary fields.
            ExchangeRate currentExc = sourceArray.get(i);

            // Check if the exchange rate is empty. If it is, ignore.
            if(currentExc.getRate().trim().isEmpty()) continue;

            String currentSource = currentExc.getSource().trim().toLowerCase();

            /*
            Begin Logic
             */
            if(groupedArray.containsKey(currentSource)) {
                groupedArray.get(currentSource).getMembers().add(currentExc);
            }
            else {
                groupedArray.put(currentSource, new ExchangeRateGBSource(currentSource));
                groupedArray.get(currentSource).getMembers().add(currentExc);
            }
        }
    }

    public ArrayList<ExchangeRate> getSourceArray() {
        return sourceArray;
    }
    public void setSourceArray(ArrayList<ExchangeRate> sourceArray) {
        this.sourceArray = sourceArray;
    }
    public HashMap<String, ExchangeRateGBSource> getGroupedArray() {
        return groupedArray;
    }
    public void setGroupedArray(HashMap<String, ExchangeRateGBSource> groupedArray) {
        this.groupedArray = groupedArray;
    }
}
