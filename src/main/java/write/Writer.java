package main.java.write;

import main.java.common.CSV;
import main.java.read.masterdata.PaymentData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer<T extends CSV, X extends CSV> {
    //<editor-fold desc="Properties">
    private static String matchesHeader =
            PaymentData.getHeader() + "|Converted (AUD)|" + "Info Dump";
    private ArrayList<T> matches;
    private static String singlesHeader = PaymentData.getHeader();
    private ArrayList<X> singles;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Writer() {}
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void writeMatchHeader(String dir) throws IOException {
        // Ensure directory exists!
        File dirmaker = new File(dir);
        File parentDirmaker = dirmaker.getParentFile();
        if(parentDirmaker != null && !parentDirmaker.exists()) {
            parentDirmaker.mkdirs();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(dir));

        // Header.
        writer.write(matchesHeader);
        writer.newLine();

        writer.flush();
        writer.close();
    }

    public void writeMatches(String dir, boolean append) throws IOException {
        // Ensure directory exists!
        File dirmaker = new File(dir);
        File parentDirmaker = dirmaker.getParentFile();
        if(parentDirmaker != null && !parentDirmaker.exists()) {
            parentDirmaker.mkdirs();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(dir, append));

        // Contents.
        for (int i = 0; i < matches.size(); i++) {
            writer.write(matches.get(i).buildCSVLine());
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }

    public void writeSinglesHeader(String dir) throws IOException {
        // Ensure directory exists!
        File dirmaker = new File(dir);
        File parentDirmaker = dirmaker.getParentFile();
        if(parentDirmaker != null && !parentDirmaker.exists()) {
            parentDirmaker.mkdirs();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(dir));

        // Header.
        writer.write(singlesHeader);
        writer.newLine();

        writer.flush();
        writer.close();
    }

    public void writeSingles(String dir, boolean append) throws IOException {
        // Ensure directory exists!
        File dirmaker = new File(dir);
        File parentDirmaker = dirmaker.getParentFile();
        if(parentDirmaker != null && !parentDirmaker.exists()) {
            parentDirmaker.mkdirs();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(dir, append));

        // Contents
        for (int i = 0; i < singles.size(); i++) {
            writer.write(singles.get(i).buildCSVLine());
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public static String getMatchesHeader() {
        return matchesHeader;
    }

    public static void setMatchesHeader(String matchesHeader) {
        Writer.matchesHeader = matchesHeader;
    }

    public static String getSinglesHeader() {
        return singlesHeader;
    }

    public static void setSinglesHeader(String singlesHeader) {
        Writer.singlesHeader = singlesHeader;
    }

    public ArrayList<T> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<T> matches) {
        this.matches = matches;
    }

    public ArrayList<X> getSingles() {
        return singles;
    }

    public void setSingles(ArrayList<X> singles) {
        this.singles = singles;
    }
    //</editor-fold>
}
