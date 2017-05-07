package com.gettingstarted.erik.tfg_appdiscvisual;

import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by Erik on 07/05/2017.
 */

public class SpinnerHandler implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "SpinnerHandler";
    private FilterHandler filterHandler;
    private int typeSpinner;


    public SpinnerHandler (int type){
        typeSpinner = type;
        filterHandler = FilterHandler.getInstance();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG,"SpinnerSelected position:");
        Log.d(TAG, String.valueOf(position));
        if (typeSpinner==0){
            filterHandler.setBackgroundColorOption(position);
        }else{
            filterHandler.setTextColorOption(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
