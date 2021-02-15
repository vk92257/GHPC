package com.lynhill.ghpc.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.BanksListAdapter;
import com.lynhill.ghpc.listener.BankClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class BankActivity extends AppCompatActivity implements BankClickListener {
    private static final int BANK_CODE = 234;
    private RecyclerView bankList;
    private ImageView backpress_id;
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
        backpress_id = findViewById(R.id.backpress_id);
        onclicked();
        addData();
        bankRecyclerView();
    }

    private void onclicked() {
        backpress_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void bankRecyclerView() {
        banksListAdapter = new BanksListAdapter(hyperlink, bankname, bankIcon, this);
        banksListAdapter.onPress(this::onBankCLikck);
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


    @Override
    public void onBankCLikck(int position) {
        Log.e("TAG", "onBankCLikck: reached the clickListener " + position);
        Intent intent = new Intent(this, MyWebView.class);
        intent.putExtra("url", hyperlink.get(position));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        startActivityForResult(intent, BANK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        List<HashMap<String, ArrayList<String>>> list = new ArrayList<>();

        Paper.book().write("merilist", list);

        list = Paper.book().read("merilidt", new ArrayList<>());


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == BANK_CODE) {
            Intent intent = new Intent(this, UploadSignature.class);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            startActivity(intent);
        }
    }
}