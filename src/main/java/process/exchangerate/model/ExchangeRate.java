package main.java.process.exchangerate.model;

import java.util.Objects;

public class ExchangeRate {


    //<editor-fold desc="Properties">
    private static String header = "source|currency|rate";

    public String source;
    public String currency;
    public String rate;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public ExchangeRate() {}

    public ExchangeRate(String source, String currency, String rate) {
        this.source = source;
        this.currency = currency;
        this.rate = rate;
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public String buildCSVLine() {
        StringBuilder sb = new StringBuilder();

        sb.append(source).append("|");
        sb.append(currency).append("|");
        sb.append(rate);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(source, that.source) && Objects.equals(currency, that.currency) && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, currency, rate);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "source='" + source + '\'' +
                ", currency='" + currency + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public static String getHeader() {
        return header;
    }

    public static void setHeader(String header) {
        ExchangeRate.header = header;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    //</editor-fold>
}
