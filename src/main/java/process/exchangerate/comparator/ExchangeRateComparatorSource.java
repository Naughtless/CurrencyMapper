package main.java.process.exchangerate.comparator;

import main.java.process.exchangerate.model.ExchangeRate;

import java.util.Comparator;

public class ExchangeRateComparatorSource implements Comparator<ExchangeRate> {
    @Override
    public int compare(ExchangeRate o1, ExchangeRate o2) {
        return o1.getSource().compareTo(o2.getSource());
    }
}
