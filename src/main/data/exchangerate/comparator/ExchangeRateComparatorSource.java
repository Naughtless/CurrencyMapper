package main.data.exchangerate.comparator;

import main.data.exchangerate.ExchangeRate;

import java.util.Comparator;

public class ExchangeRateComparatorSource implements Comparator<ExchangeRate> {
    @Override
    public int compare(ExchangeRate o1, ExchangeRate o2) {
        return o1.getSource().compareTo(o2.getSource());
    }
}
