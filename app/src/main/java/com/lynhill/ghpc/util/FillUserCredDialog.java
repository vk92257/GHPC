package com.lynhill.ghpc.util;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lynhill.ghpc.R;

import java.util.Objects;

public class FillUserCredDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "logout is click";
    public LinearLayout linearLayout;
    public FillUserCredDialog() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheetformenutl,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initvalue(view);
    }

    public void initvalue(View view){

    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){


            }
    }

    private void logoutallsession(){

        Log.e(TAG, "logoutallsession: logout........" );


        Objects.requireNonNull(getActivity()).finish();
    }

}
