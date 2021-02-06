package com.lynhill.ghpc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.BanksListAdapter;

import java.util.ArrayList;

public class Banks extends AppCompatActivity {
    private RecyclerView bankList;
    private BanksListAdapter banksListAdapter;
    private ArrayList<String> bankname;
    private ArrayList<String> hyperlink;
    private ArrayList<Integer> bankIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banks);
        findViews();

    }

    private void findViews() {
        bankList = findViewById(R.id.bank_list);

        addData();
        bankRecyclerView();
    }

    private void bankRecyclerView() {
        banksListAdapter = new BanksListAdapter(hyperlink,bankname, bankIcon, this);
        bankList.setLayoutManager(new LinearLayoutManager(this));
        bankList.setAdapter(banksListAdapter);
        bankList.setHasFixedSize(true);
    }

    private void addData() {
        bankname = new ArrayList<>();
        bankIcon = new ArrayList<>();
        hyperlink = new ArrayList<>();

        bankIcon.add(R.drawable.axis_bank_logo);
        bankname.add("Axis Bank");
        hyperlink.add("https://www.axisbank.com/retail/loans");

        bankIcon.add(R.drawable.bank_of_baroda);
        bankname.add("Bank Of Baroda");
        hyperlink.add("https://www.bankofbaroda.in/apply-for-education-loan.htm?4&utm_source=GoogleSearch&utm_medium=CPC&utm_campaign=EducationLoan&utm_content=DecEnPanIO&gclid=CjwKCAiA9vOABhBfEiwATCi7GGRMN8PtQoJ2nFa42_lCBpIhIivlEde6aLGQhgczebRh0C7GGMzJHhoC9ZwQAvD_BwE");

        bankIcon.add(R.drawable.bank_of_ndia);
        bankname.add("Bank Of India");
        hyperlink.add("https://www.bankofindia.co.in/Personalloan");

        bankIcon.add(R.drawable.hdfc);
        bankname.add("HDFC");
        hyperlink.add("https://portal.hdfc.com/campaign/new");

        bankIcon.add(R.drawable.icici_ban);
        bankname.add("ICICI Bank");
        hyperlink.add("https://www.icicibank.com/Personal-Banking/loans/personal-loan/index.page");

        bankIcon.add(R.drawable.indusind);
        bankname.add("Indusland");
        hyperlink.add("https://www.indusind.com/in/en/personal/loans/personal-loan.html");

        bankIcon.add(R.drawable.kotak_mahindra_bank);
        bankname.add("Kotak Mahindra Bank");
        hyperlink.add("https://www.kotak.com/en/customer-service/contact-us/email-us/kotak-loan.html");

        bankIcon.add(R.drawable.punjab_national_bank);
        bankname.add(" Punjab National Bank");
        hyperlink.add("https://www.pnbindia.in/loans.html");

        bankIcon.add(R.drawable.state_bank_of_india);
        bankname.add("State Bank Of India");
        hyperlink.add("https://www.onlinesbi.com/");

        bankIcon.add(R.drawable.syndicate_bank);
        bankname.add(" Syndicate Bank");
        hyperlink.add("https://www.myloancare.in/personal-loan-emi-calculator/syndicate-bank");

        bankIcon.add(R.drawable.union_bank_of_india);
        bankname.add("Union Bank Of India");
        hyperlink.add("https://www.unionbankofindia.co.in/english/personal-loans.aspx");
    }
}