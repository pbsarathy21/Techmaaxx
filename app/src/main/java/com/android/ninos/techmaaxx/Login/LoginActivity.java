package com.android.ninos.techmaaxx.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.ninos.techmaaxx.BeanClass.CustomerDetails;
import com.android.ninos.techmaaxx.NavigationActivity;
import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.Utils.CustomEditTextviewRegular;
import com.android.ninos.techmaaxx.Utils.MLog;
import com.android.ninos.techmaaxx.Utils.Util;
import com.android.ninos.techmaaxx.Utils.Validation;
import com.android.ninos.techmaaxx.request.CommonRequest;
import com.android.ninos.techmaaxx.retrofit.ListDetails;
import com.android.ninos.techmaaxx.retrofit.RetrofitInterface;
import com.android.ninos.techmaaxx.session.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Validation validation;
    Activity activity;
    RetrofitInterface apiInterface;
    LinearLayout linear_signin;
    CustomEditTextviewRegular edit_email, edit_password;
    TextInputLayout text_email, text_password;
    String Email, Password;
    boolean email, password;
    private Vibrator vib;
    Animation animShake;
    public CustomerDetails obj_userDetails;
    Session session;
    public static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validation = new Validation();
        session = new Session(this);


        Intilization();

        linear_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();

            }
        });


    }


    public void Intilization() {

        activity = LoginActivity.this;

        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.url)
                // .baseUrl("https://192.168.1.2/hostel/public/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(RetrofitInterface.class);


        edit_email = (CustomEditTextviewRegular) findViewById(R.id.edit_email);
        edit_password = (CustomEditTextviewRegular) findViewById(R.id.edit_password);

        text_email = (TextInputLayout) findViewById(R.id.text_email);
        text_password = (TextInputLayout) findViewById(R.id.text_password);


        linear_signin = (LinearLayout) findViewById(R.id.linear_signin);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }


    public void Validation() {

        Email = edit_email.getText().toString();
        Password = edit_password.getText().toString();


        if (validation.isValidEmail(Email)) {
            email = true;
            edit_email.setError(null);
        } else {
            email = false;
            edit_email.setError("Enter Valid Email!");
            edit_email.setAnimation(animShake);
            edit_email.startAnimation(animShake);
            vib.vibrate(120);
        }


        if (validation.isValidPassword(
                Password)) {

            password = true;
            edit_password.setError(null);

        } else {
            password = false;
            edit_password.setError("Atleast Minimum 6 Characters");
            edit_password.setAnimation(animShake);
            edit_password.startAnimation(animShake);
            vib.vibrate(120);
        }


        if (email && password) {
            SignIn();

        } else

        {
            Toast.makeText(LoginActivity.this, "Check and Fill the Details", Toast.LENGTH_SHORT).show();
        }


    }

    public void SignIn() {

        Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();

        commonRequest.email = edit_email.getText().toString().trim();
        commonRequest.password = edit_password.getText().toString().trim();
        Call<ListDetails> Login = apiInterface.Login(commonRequest);
        Login.enqueue(new Callback<ListDetails>() {

            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                if (response.body() != null) {


                    if (response.body().status.equalsIgnoreCase("success")) {
                        MLog.e(TAG, "response==>" + response.body().status);
                        if (response.body().customer != null) {
                            obj_userDetails = response.body().customer;
                            MLog.e(TAG, "id==>" + obj_userDetails.id);
                            MLog.e(TAG, "name==>" + obj_userDetails.name);
                            session.setUser_id(obj_userDetails.id);
                            session.setEmail(obj_userDetails.email);
                            //   session.setMobile(obj_userDetails.mobile);
                            //   session.setStatus(obj_userDetails.status);

                            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }
                        Util.hideProgress();


                    } else {
                        Toast.makeText(LoginActivity.this, response.body().msg, Toast.LENGTH_LONG).show();
                        Util.hideProgress();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "response null!", Toast.LENGTH_LONG).show();

                }


            }


            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();

            }


        });


    }

}
