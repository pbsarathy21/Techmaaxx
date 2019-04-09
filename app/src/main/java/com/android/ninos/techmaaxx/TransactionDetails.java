package com.android.ninos.techmaaxx;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.ninos.techmaaxx.Utils.CustomTextViewBold;
import com.android.ninos.techmaaxx.pojo.transaction_response.TransactionResponse;

/**
 * Created by Parthasarathy D on 1/5/2019.
 * Ninos IT Solution
 * parthasarathy.d@ninositsolution.com
 */
public class TransactionDetails extends Dialog implements View.OnClickListener {

    CustomTextViewBold sender_value, amount_value, status_value;
    Button accept_button, cancel_button;
    Activity activity;
    TransactionResponse transactionResponse;

    public TransactionDetails(@NonNull Activity activity, @NonNull TransactionResponse transactionResponse) {
        super(activity);
        this.activity = activity;
        this.transactionResponse = transactionResponse;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.transaction_details);

        sender_value = findViewById(R.id.sender_value);
        amount_value = findViewById(R.id.amount_value);
        status_value = findViewById(R.id.status_value);

        accept_button = findViewById(R.id.accept_button);
        cancel_button = findViewById(R.id.cancel_button);

        accept_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);

        String sender = transactionResponse.getData().getSenderFirstName();
        String amount = transactionResponse.getData().getAmountSent();
        String status = transactionResponse.getData().getStatus();

        if (!TextUtils.isEmpty(sender))
        {
            sender_value.setText(sender);
        }

        else
        {
            sender_value.setText("NA");
        }


        if (!TextUtils.isEmpty(amount))
        {
            amount_value.setText("₵ " + amount);
        }

        else
        {
            amount_value.setText("NA");
        }


        if (!TextUtils.isEmpty(status))
        {
             status_value.setText(status);

             if (status.equalsIgnoreCase("success"))
             {
                 accept_button.setEnabled(true);
                 SharedPreferences preferences = activity.getSharedPreferences("customer", Context.MODE_PRIVATE);
                 SharedPreferences.Editor editor = preferences.edit();
                 editor.putBoolean("status", true);
                 editor.commit();
             }
        }

        else
        {
            status_value.setText("NA");
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.accept_button:
                dismiss();
                break;

            case R.id.cancel_button:
                dismiss();
                break;
        }

    }
}
