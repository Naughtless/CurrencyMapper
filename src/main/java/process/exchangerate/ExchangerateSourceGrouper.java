package main.java.process.exchangerate;

import main.java.process.exchangerate.model.ExchangeRateGrouped;
import main.java.process.exchangerate.comparator.ExchangeRateComparatorSource;
import main.java.process.exchangerate.model.ExchangeRate;

import java.util.ArrayList;
import java.util.HashMap;

public class ExchangerateSourceGrouper
{
    private ArrayList<ExchangeRate>              sourceArray;
    private HashMap<String, ExchangeRateGrouped> groupedArray;

    public ExchangerateSourceGrouper() {}
    public ExchangerateSourceGrouper(ArrayList<ExchangeRate> sourceArray) {
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
                groupedArray.put(currentSource, new ExchangeRateGrouped(currentSource));
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
    public HashMap<String, ExchangeRateGrouped> getGroupedArray() {
        return groupedArray;
    }
    public void setGroupedArray(HashMap<String, ExchangeRateGrouped> groupedArray) {
        this.groupedArray = groupedArray;
    }
}
