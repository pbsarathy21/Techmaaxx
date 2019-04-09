package com.android.ninos.techmaaxx.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Toast;

public class Util


{

    public static ProgressDialog pDialog;
    public static void setFont(int fontNo, Context ctx, TextView view , String text)
    {
        String fontPath = "";
        //font-style:Roboto

        if(fontNo==1)
        {
            fontPath="fonts/eden mills bd.ttf";
        }
        if(fontNo==2)
        {
            fontPath="fonts/OpenSans-Regular.ttf";
        }
        if(fontNo==3)
        {
            fontPath="fonts/OpenSans-Semibold.ttf";
        }
        if(fontNo==4)
        {
            fontPath="fonts/OpenSans-Bold.ttf";
        }
        //font-style:Poppins
        if(fontNo==5){
            fontPath="fonts/IndianRupee.ttf";
        }
        if(fontNo==6){
            fontPath="fonts/Roboto-Regular.ttf";
        }
        if(fontNo==7){
            fontPath="fonts/Roboto-Medium.ttf";
        }
        if(fontNo==8){
            fontPath="fonts/Poppins-Regular.ttf";
        }
        if(fontNo==9){
            fontPath="fonts/Poppins-SemiBold.ttf";
        }
        //font-style:Rajdhani
        if(fontNo==10){
            fontPath="fonts/Rajdhani-Medium.ttf";
        }
        Typeface tf2 = Typeface.createFromAsset(ctx.getAssets(), fontPath);
        view.setTypeface(tf2);
        view.setText(text);

    }

    public static void makeToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showProgress(Activity activity) {


        pDialog = new ProgressDialog(activity);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
       // pDialog.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.pgroup));
        pDialog.setMessage("Loading..");
        pDialog.show();

    }

    public static void showProgress_wallet(Activity activity) {


        pDialog = new ProgressDialog(activity);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        // pDialog.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.pgroup));
        pDialog.setMessage("Loading.."+"Please wait\nPayment is processing");
        pDialog.show();

    }


    public static void hideProgress() {

        if(pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

    }


}
