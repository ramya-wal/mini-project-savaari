package com.example.ramya.savaari.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by LENONO on 4/4/2018.
 */

public class ProgressDialogClass {

    ProgressDialog progressDialog;
    Context context;

    public ProgressDialogClass(Context context){
        progressDialog = new ProgressDialog(context);
    }

    public void showProgressDialog() {
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }
}
