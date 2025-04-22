package main.java.OLDdata.exchangerate;

import main.java.read.externaldata.ExchangeRate;

import java.util.ArrayList;
import java.util.Objects;

public class ExchangeRateGBSource {
    //<editor-fold desc="Properties">
    private ArrayList<ExchangeRate> members;

    private String source;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public ExchangeRateGBSource(String source) {
        this.source = source;

        this.members = new ArrayList<>();
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRateGBSource that = (ExchangeRateGBSource) o;
        return Objects.equals(members, that.members) && Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(members, source);
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public ArrayList<ExchangeRate> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<ExchangeRate> members) {
        this.members = members;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    //</editor-fold>
}
