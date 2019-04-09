package com.android.ninos.techmaaxx;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.ninos.techmaaxx.Countable.CountableActivity;
import com.android.ninos.techmaaxx.Login.LoginActivity;
import com.android.ninos.techmaaxx.Setting.SettingActivity;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.database.Bluetooth;
import com.android.ninos.techmaaxx.database.DatabaseHelper;
import com.android.ninos.techmaaxx.session.Session;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    Activity activity;
    Session session;
    public static final String TAG = SplashActivity.class.getSimpleName();
    List<Bluetooth> count_list = new ArrayList<>();
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        databaseHelper = new DatabaseHelper(SplashActivity.this);

        activity = SplashActivity.this;
        session = new Session(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!session.getUser_id().equalsIgnoreCase("") && session.getUser_id() != null) {

                    session.setName("");
                    session.setU_Mobile("");
                    session.setFleet("");
                    session.setOrgin("");
                    session.setOrgin_value("Orgin");
                    session.setDestination("");
                    session.setDes_value("Destination");
                    session.setProduct("");
                    session.setProduct_value("Product");
                    session.setCategory("");
                    session.setCategory_value("Category");
                    Consts.Method = "";
                    session.setClose_cat_id("");
                    session.setWeighable_total("");
                    session.setW_Weight("");
                    session.setW_Prize("");
                    count_list.clear();
                    session.setCount_Prize("");
                    count_list.size();
                    databaseHelper.deleteAllRecord();

                    Consts.Domain_url = session.getCompany_domain();
                    Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                } else {


                    Intent intent = new Intent(SplashActivity.this, SettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();

                }
            }


        }, SPLASH_DISPLAY_LENGTH);

    }
}
