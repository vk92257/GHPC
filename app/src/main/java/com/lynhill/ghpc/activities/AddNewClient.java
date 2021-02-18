package com.lynhill.ghpc.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.AddMoreAdapter;
import com.lynhill.ghpc.adapter.UserDataAdapter;
import com.lynhill.ghpc.listener.UserInfoListener;
import com.lynhill.ghpc.pojo.Representatives;
import com.lynhill.ghpc.util.Constants;
import com.lynhill.ghpc.util.StorageManager;
import com.lynhill.ghpc.util.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewClient extends BaseActivity implements UserInfoListener {
    private static final String TAG = AddNewClient.class.getSimpleName();
    private ImageView scanCode;
    private TextInputLayout userName, address, dob, email, phoneNumber;
    private int OCR_DATA_REQUEST = 675;
    private UserDataAdapter userDataAdapter;
    private ArrayList<String> strings;
    private ImageView fillName, fillAddress, fillDOB, addPhoneNumber, addEmail;
    private Dialog dialog;
    private TextView solor, roofing, hvac;
    private String project = "";
    private String stringName, stringAddress, stringDob, stringEmail, stringPhoneNumber;
    private ArrayList<String> phoneArrayList = new ArrayList<>();
    private ArrayList<String> emailArrayList = new ArrayList<>();
    private AddMoreAdapter phoneAdapter;
    private AddMoreAdapter emailAdapter;
    private RecyclerView emailList, phoneList;
    private String[] lenders = {"LoanPal - Interest Rate 2.99%",
            "LoanPal - Interest Rate 3.99%",
            "LoanPal - Interest Rate 4.49%",
            "LoanPal - Interest Rate 4.99%",
            "LoanPal - Interest Rate 5.99%",
            "LoanPal - Interest Rate 6.99%",
            "LoanPal - Interest Rate 7.99%",};
    private String lenderweburl = "http://apply.loanpal.com/GreenHouseSolarAndAir/ghsaa1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_client);
        findViews();
        project = roofing.getText().toString();
        setSpinner();
        Log.e(TAG, "onCreate: current user " + StorageManager.getInstance(this).getCurrentUser());

    }

    public void showCustomDialog() {
        final String[] status = {""};
        Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.alert_dialog);
        TextView success = dialog1.findViewById(R.id.success);
        TextView pending = dialog1.findViewById(R.id.pending);
        TextView fail = dialog1.findViewById(R.id.fail);
        TextView next = dialog1.findViewById(R.id.next);

        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
                fail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
                success.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
                status[0] = success.getText().toString();
                next.setEnabled(true);
                next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));

            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
                success.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
                fail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
                status[0] = pending.getText().toString();
                next.setEnabled(true);
                next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
            }
        });
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
                success.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
                fail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                status[0] = fail.getText().toString();
                next.setEnabled(true);
                next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageManager.getInstance(AddNewClient.this).setLoanStatus(status[0]);
                Log.e(TAG, "onClick: " + StorageManager.getInstance(AddNewClient.this).getLoanStatus());
                dialog1.dismiss();
                startBankActivity2();
            }
        });

        dialog1.show();
    }


    private void autoFillform(int position) {
        Log.e(TAG, "autoFillform: inside fill form");
        ArrayList<Representatives> representatives = Utils.getArrayList(this, Constants.REPRESENTATIVE_LIST);
        if (representatives != null) {
            Representatives representatives1 = representatives.get(position);
            Log.e(TAG, "autoFillform: " + representatives1.getProject());

            if (!TextUtils.isEmpty(representatives1.getProject())) {

                if (representatives1.getProject().equalsIgnoreCase("solar")) {
                    selectSolor();
                    Log.e(TAG, "autoFillform: "+representatives1.getProject().equalsIgnoreCase("solar") );
                }
                if (representatives1.getProject().equalsIgnoreCase("Hvac")) {
                    selectHvac();
                    Log.e(TAG, "autoFillform: "+representatives1.getProject().equalsIgnoreCase("hvac") );
                }
                if (representatives1.getProject().equalsIgnoreCase("roofing")) {
                    selectRoofing();
                    Log.e(TAG, "autoFillform: "+representatives1.getProject().equalsIgnoreCase("roofing") );
                }
            }

            if (!TextUtils.isEmpty(representatives1.getName()))
                userName.getEditText().setText(representatives1.getName());
            if (!TextUtils.isEmpty(representatives1.getAddress()))
                address.getEditText().setText(representatives1.getAddress());
            if (!TextUtils.isEmpty(representatives1.getDob()))
                dob.getEditText().setText(representatives1.getDob());

        if (representatives1.getEmali()!=null)
            emailArrayList.addAll(representatives1.getEmali());
            if (representatives1.getPhoneNumber()!=null)
            phoneArrayList.addAll(representatives1.getPhoneNumber());
//            emailArrayList = Utils.getStringArrayList(this, Constants.PAPER_EMAIL);
//            phoneArrayList = Utils.getStringArrayList(this, Constants.PAPER_CONTACT);
            if (!emailArrayList.isEmpty() && !phoneArrayList.isEmpty()) {
                Log.e(TAG, "autoFillform: " + emailArrayList.size());
                Log.e(TAG, "autoFillform: " + phoneArrayList.size());
                emailAdapter.notifyDataSetChanged();
                phoneAdapter.notifyDataSetChanged();
//                addingListView();
            }
        }
    }


    private void setSpinner() {
        AppCompatSpinner selectlenderbank = findViewById(R.id.spinner);
        selectlenderbank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    lenderweburl = "http://apply.loanpal.com/GreenHouseSolarAndAir/ghsaa1";
                } else if (position == 1) {
                    lenderweburl = "http://apply.loanpal.com/GreenHouseSolarAndAir/ghsaa2";
                } else if (position == 2) {
                    lenderweburl = "http://apply.loanpal.com/GreenHouseSolarAndAir/ghsaa3";
                } else if (position == 3) {
                    lenderweburl = "http://apply.loanpal.com/GreenHouseSolarAndAir/ghsaa4";
                } else if (position == 4) {
                    lenderweburl = "http://apply.loanpal.com/GreenHouseSolarAndAir/ghsaa5";
                } else if (position == 5) {
                    lenderweburl = "http://apply.loanpal.com/GreenHouseSolarAndAir/ghsaa6";
                } else if (position == 6) {
                    lenderweburl = "http://apply.loanpal.com/GreenHouseSolarAndAir/ghsaa7";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        selectlenderbank.setAdapter(new Adapter());
    }

    private void findViews() {
        addPhoneNumber = findViewById(R.id.add_phone);
        addEmail = findViewById(R.id.add_email);
        scanCode = findViewById(R.id.scan_code);
        userName = findViewById(R.id.username_til);
        address = findViewById(R.id.user_address_layout);
        dob = findViewById(R.id.user_dob_layout);
        email = findViewById(R.id.email_til);
        phoneNumber = findViewById(R.id.user_phone_number_layout);
        fillName = findViewById(R.id.fill_user_name);
        fillAddress = findViewById(R.id.fill_user_address);
        fillDOB = findViewById(R.id.fill_user_dob);
        solor = findViewById(R.id.solar);
        hvac = findViewById(R.id.hvac);
        roofing = findViewById(R.id.roofing);
        emailList = findViewById(R.id.email_list);
        phoneList = findViewById(R.id.phone_list);
        clickListener();
        addingListView();
        if (StorageManager.getInstance(this).getCurrentUser() != -1) {
            Log.e(TAG, "onCreate: current user " + StorageManager.getInstance(this).getCurrentUser());
            autoFillform(StorageManager.getInstance(this).getCurrentUser());
        }
//        showDialog();
    }


    //    onclick's
    public void backpress(View view) {
//        finish();
    }


    private void addingListView() {
        phoneList.setLayoutManager(new LinearLayoutManager(this));
        emailList.setLayoutManager(new LinearLayoutManager(this));
//    phoneAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phoneArrayList);
        phoneAdapter = new AddMoreAdapter(phoneArrayList, this);
//    emailAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emailArrayList);
        emailAdapter = new AddMoreAdapter(emailArrayList, this);
        phoneList.setAdapter(phoneAdapter);
        emailList.setAdapter(emailAdapter);
    }


    private void clickListener() {
        // scan click listener on find views
        addPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(phoneNumber.getEditText().getText().toString()) && phoneNumber.getEditText().getText().length() < 15 && phoneNumber.getEditText().getText().length() > 6) {
                    phoneArrayList.add(phoneNumber.getEditText().getText().toString());
                    phoneNumber.getEditText().setText("");
                    phoneAdapter.notifyDataSetChanged();
                } else {
                    phoneNumber.setErrorIconDrawable(null);
                    phoneNumber.setError("Enter you phone number.");
                }
            }
        });
        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(email.getEditText().getText().toString())) {
                    if (isEmailValid(email.getEditText().getText().toString())) {
                        emailArrayList.add(email.getEditText().getText().toString());
                        email.getEditText().setText("");
                        emailAdapter.notifyDataSetChanged();
                        email.setErrorEnabled(false);
                    } else {
                        email.setError("Email is invalid");
                        email.setErrorIconDrawable(null);
                    }
                } else {
                    email.setError("Enter email");
                    email.setErrorIconDrawable(null);
                }
            }
        });

        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewClient.this, OCR.class);
                overridePendingTransition(R.anim.fadeout, R.anim.fadeout);
                startActivityForResult(intent, OCR_DATA_REQUEST);

            }
        });

        fillName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(1, "Name");

            }
        });
        fillDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(3, "Date of birth");
            }
        });
        fillAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(2, "Address");
            }
        });

        solor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                solor.setBackgroundColor(getResources().getColor(R.color.primary));
//                roofing.setBackgroundColor(getResources().getColor(R.color.light_black));
//                hvac.setBackgroundColor(getResources().getColor(R.color.light_black));

                selectSolor();
//                roofing.setBackgroundColor(getResources().getColor(R.color.darkGray));
//                hvac.setBackgroundColor(getResources().getColor(R.color.darkGray));
            }
        });
        hvac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                solor.setBackgroundColor(getResources().getColor(R.color.light_black));
//                roofing.setBackgroundColor(getResources().getColor(R.color.light_black));
//                hvac.setBackgroundColor(getResources().getColor(R.color.primary));

                selectHvac();
//                solor.setBackgroundColor(getResources().getColor(R.color.darkGray));
//                roofing.setBackgroundColor(getResources().getColor(R.color.darkGray));
            }
        });
        roofing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                solor.setBackgroundColor(getResources().getColor(R.color.light_black));
//                roofing.setBackgroundColor(getResources().getColor(R.color.primary));
//                hvac.setBackgroundColor(getResources().getColor(R.color.light_black));

                selectRoofing();
//                solor.setBackgroundColor(getResources().getColor(R.color.darkGray));
//                hvac .setBackgroundColor(getResources().getColor(R.color.darkGray));
            }
        });
    }

    private void selectRoofing() {
        solor.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
        ;
        roofing.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
        ;
        hvac.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
        ;

        project = roofing.getText().toString();
    }

    private void selectHvac() {
        solor.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
        roofing.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
        hvac.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
        project = hvac.getText().toString();
    }

    private void selectSolor() {
        solor.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
        roofing.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
        hvac.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
        project = solor.getText().toString();
    }

    public void Signup_click(View view) {
        StorageManager storageManager = StorageManager.getInstance(this);
        if (!TextUtils.isEmpty(userName.getEditText().getText().toString())) {
            stringName = userName.getEditText().getText().toString();
            userName.setErrorEnabled(false);
            storageManager.setUserName(stringName);
        } else {
            userName.setError("Enter your user name.");
            userName.setErrorIconDrawable(null);
            return;
        }
        if (!TextUtils.isEmpty(address.getEditText().getText().toString())) {
            stringAddress = address.getEditText().getText().toString();
            address.setErrorEnabled(false);
            storageManager.setUserAddress(stringAddress);
        } else {
            address.setError("Enter your address.");
            address.setErrorIconDrawable(null);
            return;
        }
        if (!TextUtils.isEmpty(dob.getEditText().getText().toString())) {
            stringDob = dob.getEditText().getText().toString();
            dob.setErrorEnabled(false);
            storageManager.setUserPhoneDOB(stringDob);
        } else {
            dob.setError("Enter your date of birth.");
            dob.setErrorIconDrawable(null);
            return;
        }

        if (!TextUtils.isEmpty(email.getEditText().getText().toString()) || !emailArrayList.isEmpty()) {
            if (!TextUtils.isEmpty(email.getEditText().getText().toString())) {
                if (isEmailValid(email.getEditText().getText().toString())) {
                    stringEmail = email.getEditText().getText().toString();
                    emailArrayList.add(email.getEditText().getText().toString());
                    email.getEditText().setText("");
                    emailAdapter.notifyDataSetChanged();
                    email.setErrorEnabled(false);
                    storageManager.setUserEmail(stringEmail);
                } else {
                    email.setError("Enter your valid e-mail.");
                    email.setErrorIconDrawable(null);
                    return;
                }
            } else {
                email.setErrorEnabled(false);
            }


        } else {
            email.setError("Enter your e-mail.");
            email.setErrorIconDrawable(null);
            return;


        }

        if (!TextUtils.isEmpty(phoneNumber.getEditText().getText().toString()) || !phoneArrayList.isEmpty()) {
            if (!TextUtils.isEmpty(phoneNumber.getEditText().getText().toString())) {
                if (phoneNumber.getEditText().getText().length() < 6 && phoneNumber.getEditText().getText().length() > 15) {
                    phoneNumber.setError("enter a valid phone number");
                } else {
                    stringPhoneNumber = phoneNumber.getEditText().getText().toString();
                    phoneNumber.setErrorEnabled(false);
                    storageManager.setUserPhoneNumber(stringPhoneNumber);
                    phoneArrayList.add(phoneNumber.getEditText().getText().toString());
                    phoneNumber.getEditText().setText("");
                    phoneAdapter.notifyDataSetChanged();
                }
            } else {

            }
        } else {
            phoneNumber.setError("Enter your phone number.");
            phoneNumber.setErrorIconDrawable(null);
            return;
        }
        if (!TextUtils.isEmpty(project.trim())) {
            Log.e(TAG, "Signup_click: " + project);
            storageManager.setUserProject(project);
            Log.e(TAG, "Signup_click: " + storageManager.getUserProject());
        } else {
            Toast.makeText(AddNewClient.this, "please select a project", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.saveStringArrayList(this, emailArrayList, Constants.PAPER_EMAIL);
//        Paper.book().write(Constants.PAPER_EMAIL, emailArrayList);
//        Paper.book().write(Constants.PAPER_CONTACT, phoneArrayList);
        Utils.saveStringArrayList(this, phoneArrayList, Constants.PAPER_CONTACT);
        startBankActivity();
    }

    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void startBankActivity2() {
        Intent intent = new Intent(this, UploadSignature.class);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        startActivity(intent);
        finish();
    }
//    sir code

    private void startBankActivity() {
        Intent intent = new Intent(this, MyWebView.class);
        intent.putExtra("url", lenderweburl);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        startActivityForResult(intent, 2);
//        finish();
    }

    public void showCustomDialog(int p, String token) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.bottomsheetformenutl);
        RecyclerView recyclerView = dialog.findViewById(R.id.user_data);
        userDataAdapter = new UserDataAdapter(strings, this, p);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userDataAdapter.onPress(this::onClick);
        TextView dialogHeading = dialog.findViewById(R.id.txt_dia);
        dialogHeading.setText(getString(R.string.dialog_heading_msg) + " " + token + ".");
        ImageView close = dialog.findViewById(R.id.close);
        recyclerView.setAdapter(userDataAdapter);
        recyclerView.setHasFixedSize(true);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG, "onActivityResult: --> "+requestCode+"  == ");
        if (resultCode == RESULT_OK) {
//            Log.e(TAG, "onActivityResult: --> "+requestCode+"  == "+OCR_DATA_REQUEST);
            if (requestCode == OCR_DATA_REQUEST) {
                String code = data.getStringExtra("data");
                strings = data.getStringArrayListExtra("user_info");
                if (strings != null) {
                    showFillFormOption();
                    showCustomDialog(1, "Name");
                }
            }
            if (requestCode == 2) {
                Log.e(TAG, "onActivityResult: in the back press");
                showCustomDialog();
            }


        }
    }

    private void showFillFormOption() {
        fillName.setVisibility(View.VISIBLE);
        fillDOB.setVisibility(View.VISIBLE);
        fillAddress.setVisibility(View.VISIBLE);
    }

    private void hideFillFormOption() {
        fillName.setVisibility(View.GONE);
        fillDOB.setVisibility(View.GONE);
        fillAddress.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StorageManager.getInstance(this).setCurrentUser(-1);
    }

    @Override
    public void onClick(int i, String info) {
        if (i == 1) {
            userName.getEditText().setText(info);
            Log.e(TAG, "onClick: " + i + " " + info);
            dialog.dismiss();
        } else if (i == 2) {
            address.getEditText().setText(info);
            Log.e(TAG, "onClick: " + i + " " + info);
            dialog.dismiss();
        } else if (i == 3) {
            Log.e(TAG, "onClick: " + i + " " + info);
            dob.getEditText().setText(info);
            dialog.dismiss();
        }

    }

    public class Adapter extends BaseAdapter {
        LayoutInflater inflator;

        public Adapter() {
            inflator = LayoutInflater.from(AddNewClient.this);
        }

        @Override
        public int getCount() {
            return lenders.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflator.inflate(R.layout.spinneritem, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tvCust);
            tv.setText(lenders[position]);
            return convertView;
        }
    }
}