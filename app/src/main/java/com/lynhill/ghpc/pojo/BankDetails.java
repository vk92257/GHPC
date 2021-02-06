package com.lynhill.ghpc.pojo;

import java.util.ArrayList;

public class BankDetails {
    private ArrayList<String> bankName;
    private ArrayList<Integer> bankIcon;
    private ArrayList<String> hyperLinks;

    public ArrayList<String> getBankName() {
        return bankName;
    }

    public ArrayList<Integer> getBankIcon() {
        return bankIcon;
    }

    public ArrayList<String> getHyperLinks() {
        return hyperLinks;
    }

    public BankDetails() {
    }
}
