package com.android.ninos.techmaaxx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.ninos.techmaaxx.BeanClass.Category_list;
import com.android.ninos.techmaaxx.BeanClass.CountList;
import com.android.ninos.techmaaxx.BeanClass.Destination_list;
import com.android.ninos.techmaaxx.BeanClass.NavigationList;
import com.android.ninos.techmaaxx.BeanClass.Orgin_list;
import com.android.ninos.techmaaxx.BeanClass.Product_list;
import com.android.ninos.techmaaxx.BeanClass.Ship;
import com.android.ninos.techmaaxx.BeanClass.Weight_list;
import com.android.ninos.techmaaxx.Countable.Category_Adapter;
import com.android.ninos.techmaaxx.Countable.Product_Adapter;
import com.android.ninos.techmaaxx.Countable.Shipment_adapter;
import com.android.ninos.techmaaxx.Login.LoginActivity;
import com.android.ninos.techmaaxx.MyApi.Api;
import com.android.ninos.techmaaxx.MyApi.RetrofitClient;
import com.android.ninos.techmaaxx.Report.ReportActivity;
import com.android.ninos.techmaaxx.Setting.SettingActivity;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.CustomEditTextviewRegular;
import com.android.ninos.techmaaxx.Utils.CustomTextViewBold;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.android.ninos.techmaaxx.Utils.MLog;
import com.android.ninos.techmaaxx.Utils.Util;
import com.android.ninos.techmaaxx.Utils.Validation;
import com.android.ninos.techmaaxx.common.CmdDialog;
import com.android.ninos.techmaaxx.common.MessageType;
import com.android.ninos.techmaaxx.database.Bluetooth;
import com.android.ninos.techmaaxx.database.DatabaseHelper;
import com.android.ninos.techmaaxx.language.LanguageModel;
import com.android.ninos.techmaaxx.pojo.request_transaction.RequestResponse;
import com.android.ninos.techmaaxx.printer.PrinterHelper;
import com.android.ninos.techmaaxx.printer.entity.SupermakerBill;
import com.android.ninos.techmaaxx.request.CommonRequest;
import com.android.ninos.techmaaxx.retrofit.ListDetails;
import com.android.ninos.techmaaxx.retrofit.RetrofitInterface;
import com.android.ninos.techmaaxx.session.Session;
import com.android.ninos.techmaaxx.util.ExecutorFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progressDialog;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    int count_back = 0;

    @Override
    protected void onStart() {

        Consts.Domain_url = session.getCompany_domain();
        super.onStart();
    }

    //BT Modification starts

    ImageView bt_connected, bt_disconnected;
    int REQUEST_ENABLE = 1;
    BluetoothManager bluetoothManager;
    SimpleBluetoothDeviceInterface deviceInterface;
    String[] mac;
    ListView pairedList;
    BluetoothAdapter bluetoothAdapter;
    LinearLayout main_layout;
    RelativeLayout pair_layout;
    CustomTextviewRegular zee_pay_text;
    /*Dialog dialog;*/

    LinearLayout mobile_layout, linear_new_submit,zee_pay_id;
    RadioGroup payment_radio, mno_radio;
    RadioButton cash_radio, mobile_radio;

    Button request_button, status_button;

    CustomEditTextviewRegular button_weight;

    //BT Modification ends

    CustomTextViewBold sender_value, amount_value, status_value;
    Button accept_button, cancel_button;

    ListView navDrawerList;
    List<NavigationList> drawerItems;

    public NavigationView navigationView;
    Toolbar toolbar;

    DrawerList_Adapter obj_drawerlist_adapter;
    DrawerLayout drawer;
    public static final String TAG = NavigationActivity.class.getSimpleName();

    CustomTextViewBold logout_button;

    Button testPrint;


    //Main Activity


    public ArrayList<Orgin_list> obj_orgin = new ArrayList<>();
    public ArrayList<Destination_list> obj_destinations = new ArrayList<>();

    // Spinner spinner_orgin, spinner_destination;

    boolean orgin_boolen = false, destination_boolen = false;


    String Name, Mobile, Fleet, Box, Prize;
    boolean name, mobile, fleet, box, prize;

    CustomEditTextviewRegular edit_name, edit_mobile, edit_fleet_no, text_box;
    TextInputLayout text_name, text_mobile, text_fleet_no;


    /*  LinearLayout dialog_linear, linear_re, linear_submit;
      CustomTextViewBold sub_title;
      ImageView close_icon;*/
    RecyclerView language_recyclerview, des_recyclerview;
    public Language_Adapter obj_language_adapter;
    public Destination_Adapter destination_adapter;

    FrameLayout frame;

    CustomTextviewRegular orgin_value, Destination_value;

    LinearLayout linear_l_w, linear_w, linear_l_c, linear_c, linear_weight;

    public Weight_list obj_group;

    CustomTextviewRegular text_total;
    CustomEditTextviewRegular text_p;
    //int imageType = 0;


    //Print

    private static final int REQUEST_EX = 1;
    private int fontType = 0;

    private String printTextString = "";
    private boolean checkedPicFlag = false;
    boolean isPrint = true;
    long startTimes = 0;
    long endTimes = 0;
    long timeSpace = 0;
    private Bitmap mBitmap = null;

    @Override
    protected void onStop() {
        super.onStop();
    }

    //线程运行标志 the running flag of thread
    private boolean runFlag = true;
    //打印机检测标志 the detect flag of printer
    private boolean detectFlag = false;
    //打印机连接超时时间 link timeout of printer
    private float PINTER_LINK_TIMEOUT_MAX = 30 * 1000L;
    //标签打印标记 the flag of tag print
    private boolean autoOutputPaper = false;
    //自动打印线程 thread of auto printer
    // AutoPrintThread mAutoPrintThread = null;
    String text;
    /**
     * 图片打印类型
     * image type of print
     */
    int imageType = 0;
    final String[] imageTypeArray = new String[]{"POINT", "GRAY", "RASTER"};
    private RadioGroup radio_cut;
    DetectPrinterThread mDetectPrinterThread;
    LinearLayout li_ok, lin_ca;

    //Count

    RetrofitInterface apiInterface;
    public ArrayList<Category_list> obj_categories = new ArrayList<>();
    public ArrayList<Product_list> obj_products = new ArrayList<>();
    public ArrayList<Ship> obj_shipments = new ArrayList<>();

    private Vibrator vib;
    Animation animShake;

    Activity activity;
    Context context;
    Session session;

    LinearLayout dialog_linear, linear_re, linear_submit, linear_s;
    CustomTextViewBold sub_title;
    ImageView close_icon;
    RecyclerView cat_recyclerview, pro_recyclerview, Count_recyclerview;
    Category_Adapter category_adapter;
    Product_Adapter product_adapter;

    Shipment_adapter shipment_adapter;
    CustomTextviewRegular text_cat, text_prod, text_total_count;
    CustomEditTextviewRegular text_b;
    CustomTextviewRegular text_prize;
    LinearLayout linear_cat, linear_p, linear_relative;
    List<Bluetooth> count_list = new ArrayList<>();

    ImageView menu;
    Validation validation;

    public int /*total_count = 0,*//* total_countable = 0,*/ /*count = 0,*/ wallet, total;

    public float total_count = 0,count = 0, total_countable = 0;

    public String total_amt, tot_amt_countable;

    String Bo, Pr;
    boolean bo, pr;
    public CountList obj_count_p;

    DatabaseHelper databaseHelper;
    LinearLayout linear_cou;

    String Category, Product;

    double nf;
    Bitmap bit;

    boolean wei = false, cou = false;

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait...");

        //BT Modification starts

        bt_connected = findViewById(R.id.bt_connected);
        bt_disconnected = findViewById(R.id.bt_disconnected);
        pairedList = findViewById(R.id.list_devices1);
       /* dialog = new Dialog(this);
        pairedList = dialog.findViewById(R.id.list_devices);*/
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pair_layout = findViewById(R.id.pair_layout);
        main_layout = findViewById(R.id.main_layout);

        mobile_layout = findViewById(R.id.mobile_layout);
        linear_new_submit = findViewById(R.id.linear_new_submit);

        payment_radio = findViewById(R.id.payment_radio);

        cash_radio = findViewById(R.id.cash_radio);
        mobile_radio = findViewById(R.id.mobile_radio);

        mno_radio = findViewById(R.id.mno_radio);

        //transaction_edit = findViewById(R.id.transaction_edit);

        request_button = findViewById(R.id.request_button);
        status_button = findViewById(R.id.status_button);


        zee_pay_id = findViewById(R.id.zee_pay_id);
        zee_pay_text = findViewById(R.id.zee_pay_text);

        button_weight = findViewById(R.id.button_weight);


        //BT Modification ends

        accept_button = findViewById(R.id.accept_button);
        cancel_button = findViewById(R.id.cancel_button);


        activity = NavigationActivity.this;
        context = NavigationActivity.this;
        validation = new Validation();
        session = new Session(this);

        if (zee_pay_id.getVisibility() == View.VISIBLE)
            zee_pay_id.setVisibility(View.GONE);


        request_button.setOnClickListener(v -> {

            if (linear_weight.getVisibility()==View.VISIBLE)
            {
                if (text_p.getText().toString().equalsIgnoreCase("000.000"))
                {
                    vib.vibrate(500);
                    Toast.makeText(activity, "Weight data is zero", Toast.LENGTH_SHORT).show();
                } else
                {
                    requestPayment();
                }

            }

            requestPayment();


            //bluetoothManager.close();

        });

     /*   testPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setTestPrint(true);

                printPurcase(false, false);

                if (DEVICE_MODEL == 800) {
                    try {
                        //半切
                        mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
                        mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
                        mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

*/

     button_weight.setOnClickListener(view -> {

         if (button_weight.getText().toString().equalsIgnoreCase("update"))
         {
             bluetoothManager.close();
         }

         boolean BT_available = checkBTAvailability();

         if (BT_available) {
             enableBT();
         }

         if (!bluetoothAdapter.isEnabled())
         {
             enableBT();
         }
         else
         {
             executeBTOperations();
         }

       /*  new Handler().postDelayed(() -> {
             if (!bluetoothAdapter.isEnabled())
             {
                 executeBTOperations();
             }
         }, 3000);*/
     });



        status_button.setOnClickListener(v -> startPaymentVerification());

        initViews();

        radioGroupListener();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        if (Consts.orgin_value.equalsIgnoreCase("true")) {
            Orgin_Dialog();
        } else {


        }

        if (Consts.Des_value.equalsIgnoreCase("true")) {
            Destination_Dialog();
        } else {


        }
        orgin_value.setOnClickListener(v -> {

            if (linear_new_submit.getVisibility() == View.VISIBLE)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Your current progress will be resetted");
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                builder.setPositiveButton("Edit Anyway", (dialog, which) -> Orgin_Dialog());

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                builder.setOnCancelListener(null);
                builder.show();
            }

            else
            {
                Orgin_Dialog();
            }

        });

        Destination_value.setOnClickListener(v -> {

            if (linear_new_submit.getVisibility() == View.VISIBLE)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Your current progress will be resetted");
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                builder.setPositiveButton("Edit Anyway", (dialog, which) -> Get_Destination());

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                builder.setOnCancelListener(null);
                builder.show();
            }

            else
            {
                Get_Destination();
            }

        });

        edit_name.setText(session.getName());
        edit_mobile.setText(session.getU_Mobile());
        edit_fleet_no.setText(session.getFleet());
        orgin_value.setText(session.getOrgin_value());
        Destination_value.setText(session.getDes_value());

        //Count

        text_cat.setText(session.getCategory_value());
        text_prod.setText(session.getProduct_value());
        Util.hideProgress();


        if (Consts.Cat_value.equalsIgnoreCase("true")) {
            Category_Dialog();
            linear_cou.setVisibility(View.VISIBLE);
            View_Count();

        } else {


        }

        if (Consts.Pro_Value.equalsIgnoreCase("true")) {
            Product_Dialog();
            linear_cou.setVisibility(View.VISIBLE);

            View_Count();


        } else {


        }

        if (Consts.ship_value.equalsIgnoreCase("true")) {
            //Delete();
            linear_cou.setVisibility(View.VISIBLE);
            View_Count();
            Consts.ship_value = "false";
        } else {

        }

        text_cat.setOnClickListener(v -> Category_Dialog());

        text_prod.setOnClickListener(v -> Get_Product());


        //NAVIGATION DRAWER
        drawerItems = new ArrayList<>();
        drawerItems.add(new NavigationList("Home", R.mipmap.fleet));
        //   drawerItems.add(new NavigationList("Profile", R.mipmap.user));
        drawerItems.add(new NavigationList("Printer Settings", R.mipmap.phone));
        // drawerItems.add(new NavigationList("Report", R.mipmap.destination));
        drawerItems.add(new NavigationList("Today Report", R.mipmap.destination));
        drawerItems.add(new NavigationList("Previous Day Report", R.mipmap.destination));
        //  drawerItems.add(new NavigationList("", R.mipmap.orgin));


        obj_drawerlist_adapter = new DrawerList_Adapter(NavigationActivity.this, drawerItems);
        navDrawerList.setAdapter(obj_drawerlist_adapter);
        navDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // toggle.setDrawerIndicatorEnabled(false);
        //   toolbar.setNavigationIcon(null);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // getSupportActionBar().setHomeButtonEnabled(false);

        navigationView.setNavigationItemSelectedListener(this);

        menu.setOnClickListener(v -> {

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        bt_connected.setOnClickListener(v -> getPairedDevices());

        linear_l_w.setOnClickListener(v -> {

            Consts.Method = "weighable";


            Name = edit_name.getText().toString();
            Mobile = edit_mobile.getText().toString();
            Fleet = edit_fleet_no.getText().toString();

            SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", Name);
            editor.putString("msidn", Mobile);
            editor.commit();


            if (!validation.isEmpty(Name)) {
                name = true;
                edit_name.setError(null);

            } else {
                name = false;
                edit_name.setError("Enter Your Name!");
                edit_name.setAnimation(animShake);
                edit_name.startAnimation(animShake);
                vib.vibrate(120);
            }


            if (validation.isValidPassword(Mobile)) {

                mobile = true;
                text_mobile.setError(null);

            } else {
                mobile = false;
                text_mobile.setError("Enter Valid  Mobile Number!");
                text_mobile.setAnimation(animShake);
                text_mobile.startAnimation(animShake);
                vib.vibrate(120);
            }

            if (!validation.isEmpty(Fleet)) {
                fleet = true;
                text_fleet_no.setError(null);

            } else {
                fleet = false;
                text_fleet_no.setError("Enter Your Fleet No!");
                text_fleet_no.setAnimation(animShake);
                text_fleet_no.startAnimation(animShake);
                vib.vibrate(120);
            }


            if (name && mobile && fleet) {


                if (!session.getOrgin().equalsIgnoreCase("") && !session.getDestination().equalsIgnoreCase("")) {

                    linear_l_w.setVisibility(View.GONE);
                    linear_c.setVisibility(View.GONE);
                    linear_w.setVisibility(View.VISIBLE);
                    linear_l_c.setVisibility(View.VISIBLE);
                    linear_weight.setVisibility(View.VISIBLE);
                  //  linear_submit.setVisibility(View.VISIBLE);
                    linear_new_submit.setVisibility(View.VISIBLE);
                    linear_cou.setVisibility(View.GONE);
                    Get_Weight_Prize();

                    Consts.Method = "weighable";


                } else {

                    Toast.makeText(NavigationActivity.this, "Select the Orgin ,Destination", Toast.LENGTH_SHORT).show();
                }
            }


        });

        linear_l_c.setOnClickListener(v -> {

            Name = edit_name.getText().toString();
            Mobile = edit_mobile.getText().toString();
            Fleet = edit_fleet_no.getText().toString();

            SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", Name);
            editor.putString("msidn", Mobile);
            editor.commit();

            if (!validation.isEmpty(Name)) {
                name = true;
                edit_name.setError(null);

            } else {
                name = false;
                edit_name.setError("Enter Your Name!");
                edit_name.setAnimation(animShake);
                edit_name.startAnimation(animShake);
                vib.vibrate(120);
            }

            if (validation.isValidPassword(Mobile)) {

                mobile = true;
                text_mobile.setError(null);

            } else {
                mobile = false;
                text_mobile.setError("Enter Valid  Mobile Number!");
                text_mobile.setAnimation(animShake);
                text_mobile.startAnimation(animShake);
                vib.vibrate(120);
            }
            if (!validation.isEmpty(Fleet)) {
                fleet = true;
                text_fleet_no.setError(null);

            } else {
                fleet = false;
                text_fleet_no.setError("Enter Your Fleet No!");
                text_fleet_no.setAnimation(animShake);
                text_fleet_no.startAnimation(animShake);
                vib.vibrate(120);
            }

            if (name && mobile && fleet) {

                if (!session.getOrgin().equalsIgnoreCase("") && !session.getDestination().equalsIgnoreCase("")) {

                    linear_cou.setVisibility(View.VISIBLE);
                    linear_l_c.setVisibility(View.GONE);
                    linear_w.setVisibility(View.GONE);
                    linear_weight.setVisibility(View.GONE);
                    linear_c.setVisibility(View.VISIBLE);
                    linear_l_w.setVisibility(View.VISIBLE);
                 //   linear_submit.setVisibility(View.GONE);
                    linear_new_submit.setVisibility(View.GONE);
                    Consts.Method = "countable";
                    session.setProduct("");
                    session.setProduct_value("Product");
                    session.setCategory("");
                    session.setCategory_value("Category");
                    //  View_Count();

                } else {

                    Toast.makeText(NavigationActivity.this, "Select the Orgin ,Destination", Toast.LENGTH_SHORT).show();
                }

            } else {

            }
        });

        linear_s.setOnClickListener(v -> {

            Bo = text_b.getText().toString().trim();
            Pr = text_prize.getText().toString().trim();

            if (!validation.isEmpty(Bo)) {
                bo = true;
                text_b.setError(null);

            } else {
                bo = false;
                text_b.setError("Enter Your Box!");
                text_b.setAnimation(animShake);
                text_b.startAnimation(animShake);
                vib.vibrate(120);
            }

            if (!validation.isEmpty(Pr)) {
                pr = true;
                text_prize.setError(null);

            } else {
                pr = false;
                text_prize.setError("Enter Box !!");
                text_prize.setAnimation(animShake);
                text_prize.startAnimation(animShake);
                vib.vibrate(120);
            }

            if (bo && pr) {
             //   linear_submit.setVisibility(View.GONE);
                linear_new_submit.setVisibility(View.GONE);
                linear_relative.setVisibility(View.GONE);

                Countable();
            } else {

                Toast.makeText(NavigationActivity.this, "Select the Category ,Product", Toast.LENGTH_SHORT).show();
            }
        });


        linear_submit.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
            boolean status = preferences.getBoolean("status", false);
            SharedPreferences.Editor editor = preferences.edit();

            if (status)
            {
                editor.putBoolean("status", false).commit();
                if (Consts.Method.equalsIgnoreCase("weighable")) {

                    Box = text_box.getText().toString().trim();
                    Prize = text_p.getText().toString().trim();

                    if (Prize.equalsIgnoreCase("000.000"))
                    {
                        vib.vibrate(120);
                        Toast.makeText(activity, "Weight data is zero", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!validation.isEmpty(Box)) {
                        box = true;
                        text_box.setError(null);

                    } else {
                        box = false;
                        text_box.setError("Enter Your Box!");
                        text_box.setAnimation(animShake);
                        text_box.startAnimation(animShake);
                        vib.vibrate(120);
                    }

                    if (!validation.isEmpty(Prize)) {
                        prize = true;
                        text_p.setError(null);

                    } else {
                        prize = false;
                        text_p.setError("Enter Weight !!");
                        text_p.setAnimation(animShake);
                        text_p.startAnimation(animShake);
                        vib.vibrate(120);
                    }

                    if (box && prize) {

                        session.setBox(Box);
                        session.setW_Weight(Prize);
                        PrintPopUp();

                    } else {

                    }

                } else {

                    if (bo && pr) {
                        PrintPopUp();

                    } else {
                        Toast.makeText(NavigationActivity.this, "Select the Category ,Product", Toast.LENGTH_SHORT).show();

                    }

                }

            } else if (cash_radio.isChecked())
            {
                if (Consts.Method.equalsIgnoreCase("weighable")) {

                    Box = text_box.getText().toString().trim();
                    Prize = text_p.getText().toString().trim();


                    if (Prize.equalsIgnoreCase("000.000"))
                    {
                        vib.vibrate(120);
                        Toast.makeText(activity, "Weight data is zero", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (!validation.isEmpty(Box)) {
                        box = true;
                        text_box.setError(null);

                    } else {
                        box = false;
                        text_box.setError("Enter Your Box!");
                        text_box.setAnimation(animShake);
                        text_box.startAnimation(animShake);
                        vib.vibrate(120);
                    }

                    if (!validation.isEmpty(Prize)) {
                        prize = true;
                        text_p.setError(null);

                    } else {
                        prize = false;
                        text_p.setError("Enter Weight !!");
                        text_p.setAnimation(animShake);
                        text_p.startAnimation(animShake);
                        vib.vibrate(120);
                    }

                    if (box && prize) {

                        session.setBox(Box);
                        session.setW_Weight(Prize);
                        PrintPopUp();


                    } else {

                    }

                } else {

                    if (bo && pr) {
                        PrintPopUp();

                    } else {
                        Toast.makeText(NavigationActivity.this, "Select the Category ,Product", Toast.LENGTH_SHORT).show();

                    }

                }

            } else
            {
                Toast.makeText(activity, "your payment is not success", Toast.LENGTH_SHORT).show();
            }
        });

        text_p.addTextChangedListener(textWatcher);

        text_b.addTextChangedListener(textWatcher);


        edit_name.addTextChangedListener(nameWatcher);
        edit_mobile.addTextChangedListener(mobileWatcher);
    }

    private void executeBTOperations() {

            progressDialog.show();

            boolean pair = checkDeviceConnected();

            if (pair)
            {
                String mac_ad = session.getMac_address();
                connectDevice(mac_ad);
            }
            else
            {
                getPairedDevices();
            }



    }

    private boolean checkBTAvailability() {

            bluetoothManager = BluetoothManager.getInstance();
            if (bluetoothManager == null) {

                finish();

                return false;
            } else
            {
                return true;
            }

    }

    private void requestPayment() {

            String reference = random();
            String cus_desc = "Luggage Payment";

            mno_radio.setOnCheckedChangeListener((group, checkedId) -> {
                SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                switch (checkedId)
                {
                    case R.id.mtn_radio :
                        editor.putString("mno", "MTN");
                        editor.commit();
                        break;

                    case R.id.airtel_radio :
                        editor.putString("mno", "Airtel");
                        editor.commit();
                        break;

                    case R.id.tigo_radio :
                        editor.putString("mno", "Tigo");
                        editor.commit();
                        break;

                    case R.id.vodafone_radio :
                        editor.putString("mno", "Vodafone");
                        editor.commit();
                        break;

                        default:
                            editor.putString("mno", "MTN");
                            editor.commit();
                            break;
                }
            });


            SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
            String cus_name = preferences.getString("name", "default");
            String cus_msidn = preferences.getString("msidn", "default");
            String payment = preferences.getString("amount", "1");
            String cus_mno = preferences.getString("mno", "MTN");


        progressDialog.show();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("customerName", cus_name);
            jsonObject.put("mno", cus_mno);
            jsonObject.put("amount", payment);
            jsonObject.put("msisdn", cus_msidn);
            jsonObject.put("description", cus_desc);
            jsonObject.put("reference", reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImU1ZDU1YzM" +
                "2OWZiNjAxYzgxMmMzMDhiMzRjODMwNmQ0Y2Q5ODk3YmQzNWU0YjY5OGY2ZmE3ZTgyYjJlOTQxYTYyYzdmMTA5MDE0ZDIzZDhkIn0." +
                "eyJhdWQiOiIyMiIsImp0aSI6ImU1ZDU1YzM2OWZiNjAxYzgxMmMzMDhiMzRjODMwNmQ0Y2Q5ODk3YmQzNWU0YjY5OGY2ZmE3ZTgyYjJlOTQx" +
                "YTYyYzdmMTA5MDE0ZDIzZDhkIiwiaWF0IjoxNTQ2NjY0NDY2LCJuYmYiOjE1NDY2NjQ0NjYsImV4cCI6MTU3ODIwMDQ2Niwic3ViIjoiNTkiLCJz" +
                "Y29wZXMiOltdfQ.bDxO_o4lCUjDrz3ts6IiZJQP1XhbzKqLwndMTzdRfL-PBhbixnDidaj-BS6n_R2ZW3ulp5qltheNKQoKCfU_T-1XeIyXk34v3Zo07s" +
                "uX5kLlaogzNV_Swz3l-Kp0wcItNgOORTy8eAcs1dJkMvXT33LzU5Rncs8a9ZASxL_IAvyHSbq6I_aJVi06I0Shf92aCtguUEwMZK-J6_RZ9hAleqFuMn1yZSwEbv" +
                "VElBI3cgNBNjKlnQVpdaTBQeGSGNTppyhHPwIpltoyWgCMLF12gFVRudQeJvicQoCUOW06g6-ohhVrSBe-hEp9KNOBzCGY3ApLeC6_1BpYQeg6EXzesh6nO5UoKvp8eTKL" +
                "vD5TjN9CcxXU49ceP9U-enUJcsjyvspIxK4dMgAMpr5T1YJVbtviu9zIhS11VDTfR73JjN7HBHjeQsqP3kVi-XvlZRAxdqMDhexgDXoE19m2Abu0fRjvh2fd5SSt-33WqP00dJCj" +
                "4TMCSkGrj_6q19OESdVm0X08Zvw8QYXQ-on9P1Q2z897Yy4BrgA8Fks1DaPu9t_WTlQNN6G7lJNQAFsEyVpMqJk-piejoOgbf5wwKRsrhmAIrz25EJdr1wq2JtQbpVX6EUhLOXn6qqndjV" +
                "lt9lKEOwWjoGo4QkcT9dPBuywu1xWRwDqmwueZFtDQV0iBY2U";

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer "+access_token)
                            .build();
                    return chain.proceed(request);
                }).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(RetrofitInterface.payment_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiInterface = retrofit.create(RetrofitInterface.class);

        Call<RequestResponse> responseCall = apiInterface.sendRequest(jsonObject.toString());

        responseCall.enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                progressDialog.dismiss();
                if (response.code() == 200)
                {
                    RequestResponse requestResponse = response.body();
                    try {
                        if (requestResponse.getCode() == 411)
                        {
                            //SUCCESS
                            //Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
                            String amount = requestResponse.getAmount().toString();
                            String zee_id = requestResponse.getZeepayId().toString().substring(0,4);
                            String message = requestResponse.getMessage();
                            createAlert(amount, zee_id, message);
                        }
                        else if (requestResponse.getCode() == 400)
                        {
                            //Error on Transactional details
                            //Toast.makeText(activity, "Error on Transactional details", Toast.LENGTH_SHORT).show();
                            String message = requestResponse.getMessage();
                            createErrorAlert(message);
                        }

                        else
                        {
                            //response body error
                            Toast.makeText(activity, "response body error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(activity, ""+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(activity, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void createErrorAlert(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {

        });
        builder.create();
        builder.show();
    }

    private void createAlert(String amount, String zee_id, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Amount - ₵ "+ amount);
        builder.setMessage("ZeePay ID :  "+zee_id+"\n"+message);
        builder.setPositiveButton("OK", (dialog, which) -> {
                if (zee_pay_id.getVisibility() == View.GONE)
                {
                    zee_pay_id.setVisibility(View.VISIBLE);
                }

                zee_pay_text.setText(zee_id);
        });
        builder.create();
        builder.show();
    }

    public static String random() {

        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(5);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void startPaymentVerification() {

        String transaction_id = zee_pay_text.getText().toString().trim();
        getTransactionDetails(transaction_id);
    }

    private void getTransactionDetails(String transaction_id) {

        progressDialog.show();

        Retrofit retrofit = RetrofitClient.getRetrofit();
        Api api = retrofit.create(Api.class);

        compositeDisposable.add(api.getTransactions(transaction_id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(transactionResponse -> {

            progressDialog.dismiss();

            try {
                if (transactionResponse.getData().getZeepayId() == Integer.parseInt(transaction_id))
                {
                    //show dialog
                    TransactionDetails transactionDetails = new TransactionDetails(NavigationActivity.this, transactionResponse);
                    transactionDetails.show();
                }

                else
                {
                   //transaction_edit.setError("Enter valid transaction id");
                   vib.vibrate(1000);
                }

            }
            catch (Exception e) {

               // transaction_edit.setError("Enter valid transaction id");
                vib.vibrate(1000);
            }



        }));

    }

    private void radioGroupListener() {

        payment_radio.setOnCheckedChangeListener((group, checkedId) -> {

            switch (checkedId)
            {
                case R.id.cash_radio :
                if (mobile_layout.getVisibility() == View.VISIBLE)
                    mobile_layout.setVisibility(View.GONE);
                if (zee_pay_id.getVisibility() == View.VISIBLE)
                    zee_pay_id.setVisibility(View.GONE);
                session.setPayment_mode("Cash");
                break;

                case R.id.mobile_radio :
                    if (zee_pay_id.getVisibility() == View.GONE)
                        zee_pay_id.setVisibility(View.VISIBLE);
                    if (mobile_layout.getVisibility() == View.GONE)
                        mobile_layout.setVisibility(View.VISIBLE);
                    session.setPayment_mode("Mobile Money");
                    break;
            }
        });
    }

    private boolean checkDeviceConnected() {
        List<BluetoothDevice> pairedDevices = bluetoothManager.getPairedDevicesList();
        String[] strings = new String[pairedDevices.size()];
        mac = new String[pairedDevices.size()];
        BluetoothDevice[] btArray = new BluetoothDevice[pairedDevices.size()];
        int index = 0;
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d("My Bluetooth App", "Device name: " + device.getName());
                Log.d("My Bluetooth App", "Device MAC Address: " + device.getAddress());
                btArray[index] = device;
                strings[index] = device.getName();
                mac[index] = device.getAddress();
                index++;
            }
        }


        String mac_add = session.getMac_address();

        if (Arrays.asList(mac).contains(mac_add))
        {
            Arrays.fill(strings, null);
            Arrays.fill(mac, null);
            return true;
        }

        else
        {
            Arrays.fill(strings, null);
            Arrays.fill(mac, null);
            return false;
        }

    }

    private void getPairedDevices() {

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        String[] strings = new String[pairedDevices.size()];
        mac = new String[pairedDevices.size()];
        BluetoothDevice[] btArray = new BluetoothDevice[pairedDevices.size()];
        int index = 0;
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d("My Bluetooth App", "Device name: " + device.getName());
                Log.d("My Bluetooth App", "Device MAC Address: " + device.getAddress());

                btArray[index] = device;
                strings[index] = device.getName();
                mac[index] = device.getAddress();
                index++;

            }

            progressDialog.dismiss();

               // new PairDialog(this, strings);

        /*main_layout.setVisibility(View.GONE);*/
            pair_layout.setVisibility(View.VISIBLE);
            main_layout.setAlpha(0.2f);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, strings);
            pairedList.setAdapter(arrayAdapter);

            pairedList.setOnItemClickListener((parent, view, position, id) -> {


                session.setMac_address(mac[position]);


                //Toast.makeText(activity, ""+mac[position], Toast.LENGTH_SHORT).show();

                connectDevice(mac[position]);

                pair_layout.setVisibility(View.GONE);
                main_layout.setAlpha(1f);
//                main_layout.setVisibility(View.VISIBLE);

            });

     /*   SharedPreferences preferences = getSharedPreferences("BT", MODE_PRIVATE);
        int position = preferences.getInt("MAC", 0);
*/



        }
    }

    @SuppressLint("CheckResult")
    private void connectDevice(String s) {


        bluetoothManager.openSerialDevice(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnected, this::onError);
    }

    private void onConnected(BluetoothSerialDevice bluetoothSerialDevice) {

        deviceInterface = bluetoothSerialDevice.toSimpleDeviceInterface();

        // Listen to bluetooth events
        deviceInterface.setListeners(this::onMessageReceived, this::onMessageSent, this::onError);
        if (progressDialog.isShowing())
            progressDialog.dismiss();

    }


    int receiveCount = 0;
    int errorCount = 0;

    private void onMessageSent(String message) {
        // We sent a message! Handle it here.
        //Toast.makeText(this, "Sent a message! Message was: " + message, Toast.LENGTH_LONG).show(); // Replace context with your context instance.
    }

    private void onMessageReceived(String s) {

        if (progressDialog.isShowing())
            progressDialog.dismiss();

        receiveCount++;

        if (receiveCount>10)
        {
            bluetoothManager.close();
            receiveCount = 0;
        }

        if (button_weight.getText().toString().equalsIgnoreCase("get"))
        {
            button_weight.setText("UPDATE");
            Toast.makeText(activity, "Bluetooth device connection successful", Toast.LENGTH_SHORT).show();
        }

        /*Toast.makeText(activity, ""+s, Toast.LENGTH_SHORT).show();*/
        text_p.setText(s);
    }

    private void onError(Throwable error) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();

        MLog.e("BT:onError ->", error.getMessage());

     bluetoothManager.close();

        Toast.makeText(activity, "Bluetooth device connection failed, Please check the connection and try again", Toast.LENGTH_SHORT).show();
        //Toast.makeText(activity, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

    }

    private void enableBT() {

        if (!bluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,REQUEST_ENABLE);
        }

        else
        {
           // Toast.makeText(activity, "BT already Enabled", Toast.LENGTH_SHORT).show();
            bt_disconnected.setVisibility(View.GONE);
            bt_connected.setVisibility(View.VISIBLE);
        }

    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            double pri = 0.0;
            int w_prize = 0;


            if (Consts.Method.equalsIgnoreCase("weighable")) {


                try {
                    pri = Double.parseDouble(session.getW_Prize());
                    w_prize = (int) pri;
                    Log.e("W-pRIZE", "double==>" + pri + "\nInteger::w-Prize===>" + w_prize);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                try {

                    count = Float.parseFloat(text_p.getText().toString());
                    MLog.e(TAG, "count==>" + count);
                    total_count = count * w_prize;
                    MLog.e(TAG, "total_count==>" + total_count);
                    total_amt = String.valueOf(total_count);


                    MLog.e(TAG, "total_amt_string==>" + total_amt);
                    text_total.setText("" + total_amt);

                    SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("amount", total_amt);
                    editor.commit();


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String yourFormattedString = formatter.format(total_count);


                //NumberFormat format = NumberFormat.getCurrencyInstance();
                //   String currency = format.format(total_amt);

                Log.e("currency", "Currecncy==>" + yourFormattedString);


                text_total.setText(yourFormattedString);
                session.setWeighable_total(yourFormattedString);

            } else {


                try {
                    pri = Double.parseDouble(session.getCountable());
                    w_prize = (int) pri;
                    Log.e("W-pRIZE", "double==>" + pri + "\nInteger::w-Prize===>" + w_prize);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                try {

                    count = Integer.parseInt(text_b.getText().toString());
                    MLog.e(TAG, "count==>" + count);
                    total_countable = count * w_prize;


                    MLog.e(TAG, "total_count==>" + total_countable);
                    tot_amt_countable = String.valueOf(total_countable);
                    MLog.e(TAG, "total_amt_string==>" + tot_amt_countable);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String yourFormattedString = formatter.format(total_countable);


                //NumberFormat format = NumberFormat.getCurrencyInstance();
                //   String currency = format.format(total_amt);

                Log.e("currency", "Currecncy==>" + yourFormattedString);

                text_prize.setText(yourFormattedString);

            }


            Util.hideProgress();


        }
    };

    TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", s.toString());
            editor.commit();


        }
    };

    TextWatcher mobileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {


            SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("msidn", s.toString());
            editor.commit();

        }
    };


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        switch (position) {
            case 0:
                Log.e(TAG, "Pos 0:==>");

                //  Intent intent = new Intent(NavigationActivity.this, NavigationActivity.class);
                //  startActivity(intent);
                ///  overridePendingTransition(R.anim.enter, R.anim.exit);

                drawer.closeDrawer(Gravity.LEFT);
                break;
            case 1:
                Log.e(TAG, "Pos 1:==>");

                Consts.Setting = "true";
                Intent intent1 = new Intent(NavigationActivity.this, SettingActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;
            case 2:
                Log.e(TAG, "Pos 2:==>");
                session.setReport_type("today");
                Intent intent2 = new Intent(NavigationActivity.this, ReportActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

            case 3:
                Log.e(TAG, "Pos 3:==>");
                session.setReport_type("yesterday");
                Intent intent = new Intent(NavigationActivity.this, ReportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 4:
                Log.e(TAG, "Pos 3:==>");
             /*   Intent intent4 = new Intent(NavigationActivity.this, Change_PasswordActivity.class);
                startActivity(intent4);
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                break;


            default:
                Log.e("default", "default");
                break;
        }
        drawer.closeDrawer(Gravity.LEFT);

    }

    public void initViews() {

        session = new Session(context);
        MLog.e(TAG, "name==>" + session.getUsername());

        //  toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        linear_submit = (LinearLayout) findViewById(R.id.linear_submit);


        linear_s = (LinearLayout) findViewById(R.id.linear_s);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu = (ImageView) findViewById(R.id.menu);
        navDrawerList = (ListView) findViewById(R.id.navDrawerList);
        logout_button = (CustomTextViewBold) findViewById(R.id.logout_button);
//Main Activity
        text_total = (CustomTextviewRegular) findViewById(R.id.text_total);

        text_box = (CustomEditTextviewRegular) findViewById(R.id.text_box);
        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.url)
                // .baseUrl("https://192.168.1.2/hostel/public/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(RetrofitInterface.class);

        text_p = (CustomEditTextviewRegular) findViewById(R.id.text_p);
        // spinner_orgin = (Spinner) findViewById(R.id.spinner_orgin);
        //spinner_destination = (Spinner) findViewById(R.id.spinner_destination);

        edit_name = (CustomEditTextviewRegular) findViewById(R.id.edit_name);
        edit_mobile = (CustomEditTextviewRegular) findViewById(R.id.edit_mobile);
        edit_fleet_no = (CustomEditTextviewRegular) findViewById(R.id.edit_fleet_no);

        text_name = (TextInputLayout) findViewById(R.id.text_name);
        text_mobile = (TextInputLayout) findViewById(R.id.text_mobile);
        text_fleet_no = (TextInputLayout) findViewById(R.id.text_fleet_no);
        orgin_value = (CustomTextviewRegular) findViewById(R.id.orgin_value);
        Destination_value = (CustomTextviewRegular) findViewById(R.id.Destination_value);


        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        linear_l_w = (LinearLayout) findViewById(R.id.linear_l_w);
        linear_w = (LinearLayout) findViewById(R.id.linear_w);
        linear_l_c = (LinearLayout) findViewById(R.id.linear_l_c);
        linear_c = (LinearLayout) findViewById(R.id.linear_c);

        linear_weight = (LinearLayout) findViewById(R.id.linear_weight);


        Get_Orgin();


        //Count

        databaseHelper = new DatabaseHelper(NavigationActivity.this);
        text_cat = (CustomTextviewRegular) findViewById(R.id.text_cat);
        text_prod = (CustomTextviewRegular) findViewById(R.id.text_prod);

        text_total_count = (CustomTextviewRegular) findViewById(R.id.text_total_count);

        text_b = (CustomEditTextviewRegular) findViewById(R.id.text_b);

        text_prize = (CustomTextviewRegular) findViewById(R.id.text_prize);


        linear_cat = (LinearLayout) findViewById(R.id.linear_cat);
        linear_p = (LinearLayout) findViewById(R.id.linear_p);
        linear_relative = (LinearLayout) findViewById(R.id.linear_relative);
        Count_recyclerview = (RecyclerView) findViewById(R.id.Count_recyclerview);

        linear_cou = (LinearLayout) findViewById(R.id.linear_cou);
        Get_Category();

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setUser_id("");

                Intent intent1 = new Intent(NavigationActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void Get_Orgin() {

        Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_id = session.getUser_id();

        Call<ListDetails> oR = apiInterface.orgin(commonRequest);
        oR.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                if (response.body() != null) {
                    MLog.e("Branch=>", "Branch==>" + response.body().msg);

                    MLog.e("Branch=>", "Branch==>" + response.body().status);

                    if (response.body().status.equalsIgnoreCase("success")) {

                        obj_orgin = response.body().origin;

                        if (!obj_orgin.isEmpty()) {
                            obj_orgin = response.body().origin;
                            Log.e(TAG, "Orgin:==>" + obj_orgin.get(0).title);
                            MLog.e(TAG, "Orgin_size==>" + obj_orgin.size());

                            ArrayList<String> list = new ArrayList<>();
                            for (int arrIndx = 0; arrIndx < obj_orgin.size(); arrIndx++) {
                                list.add(obj_orgin.get(arrIndx).title);
                                Log.e(TAG, "Brand_name:==>" + obj_orgin.get(arrIndx).title);

                            }


                    /*    obj_language_adapter = new Language_Adapter(MainActivity.this, obj_orgin);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        language_recyclerview.setLayoutManager(layoutManager);
                        language_recyclerview.setAdapter(obj_language_adapter);
                        obj_language_adapter.notifyDataSetChanged();*/
                        } else {
                            Toast.makeText(NavigationActivity.this, "list_empty!", Toast.LENGTH_LONG).show();

                        }
                    } else {

                    }
                } else {
                    Toast.makeText(NavigationActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }

    public void Get_Destination() {

        Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.origin_id = session.getOrgin();

        Call<ListDetails> Desitination = apiInterface.Destin(commonRequest);
        Desitination.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                if (response.body() != null) {
                    MLog.e("Desitination=>", "Desitination==>" + response.body().msg);

                    MLog.e("Desitination=>", "Desitination==>" + response.body().status);

                    obj_destinations = response.body().destinations;

                    if (!obj_destinations.isEmpty()) {
                        obj_destinations = response.body().destinations;
                        Log.e(TAG, "Destination:==>" + obj_destinations.get(0).title);
                        MLog.e(TAG, "Destination size==>" + obj_destinations.size());

                        ArrayList<String> list = new ArrayList<>();
                        for (int arrIndx = 0; arrIndx < obj_destinations.size(); arrIndx++) {
                            list.add(obj_destinations.get(arrIndx).title);
                            Log.e(TAG, "Destination_name:==>" + obj_destinations.get(arrIndx).title);


                        }
                        Destination_Dialog();

                    } else {
                        Toast.makeText(NavigationActivity.this, "Select the Orgin!", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(NavigationActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void Get_Weight_Prize() {

        Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.origin_id = session.getOrgin();
        commonRequest.destination_id = session.getDestination();

        Call<ListDetails> w_prize = apiInterface.weight_price(commonRequest);
        w_prize.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                if (response.body() != null) {
                    MLog.e("Weight_Prize=>", "Weight_Prize==>" + response.body().msg);

                    MLog.e("Weight_Prize=>", "Weight_Prize==>" + response.body().status);

                    obj_group = response.body().groups;

                    if (response.body().groups != null) {
                        obj_group = response.body().groups;
                       /* MLog.e(TAG,"price_we==>"+obj_group.rateperkg);
                        double price = Double.parseDouble(obj_group.rateperkg);
                        MLog.e(TAG,"double_price==>"+price);
                        int price_wei = (int) price;
                        MLog.e(TAG,"intger_price==>"+price_wei);
                        int count = Integer.parseInt(text_p.getText().toString());
                        int total = count * price_wei;
                        MLog.e(TAG,"total==>"+total);
                        text_total.setText(""+total);

                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(total);


                        //NumberFormat format = NumberFormat.getCurrencyInstance();
                        //   String currency = format.format(total_amt);

                        Log.e("currency", "Currecncy==>" + yourFormattedString);


                        text_total.setText(yourFormattedString);*/

                        session.setW_Prize(obj_group.rateperkg);


                    }
                    Util.hideProgress();


                } else {
                    Toast.makeText(NavigationActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void Weighable() {

        Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_id = session.getUser_id();
        commonRequest.method = Consts.Method;
        commonRequest.name = session.getName();
        commonRequest.phone = session.getU_Mobile();
        commonRequest.fleetno = session.getFleet();
        commonRequest.origin_id = session.getOrgin();
        commonRequest.destination_id = session.getDestination();
        commonRequest.count = text_box.getText().toString().trim();
        commonRequest.weight = text_p.getText().toString().trim();
        commonRequest.total_amount = total_amt;
        commonRequest.payment_mode = session.getPayment_mode();
        MLog.e(TAG, "post_values:==>" + "iser_id==>" + commonRequest.user_id + "\nmethod==>" + commonRequest.method + "\nname==>" + commonRequest.name + "\nphone==>" + commonRequest.phone +
                "\nfleetNo==>" + commonRequest.fleetno + "\norigin_id==>" + commonRequest.origin_id + "\ndestination_id==>" + commonRequest.destination_id + "\ncount==>" + commonRequest.count +
                "\nweight==>" + commonRequest.weight + "\ntotal_amopunr==>" + commonRequest.total_amount);


        Call<ListDetails> weight = apiInterface.Weight(commonRequest);
        weight.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                if (response.body() != null) {
                    MLog.e("Weight_Insert=>", "Weight_Insert==>" + response.body().msg);

                    MLog.e("Weight_Insert=>", "Weight_Insert==>" + response.body().status);

                    session.setConsigment_no(response.body().consignmentno);


                    Toast.makeText(NavigationActivity.this, response.body().msg, Toast.LENGTH_LONG).show();


                    Util.hideProgress();


                    if (wei) {
                        wei = false;

                    } else {

                        if (session.getImg_log().equalsIgnoreCase("")) {


                            printPurcase(false, false);

                            if (DEVICE_MODEL == 800) {
                                try {
                                    //半切
                                    //  printVamplify(1);
                                    mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
                                    mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
                                    mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }


                            }


                        } else {

                            //String inn = session.getImg_log();
                            //  StringToBitMap(inn);

                            printPurcase(true, false);

                            if (DEVICE_MODEL == 800) {
                                try {
                                    //半切
                                    // printVamplify(1);
                                    mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
                                    mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
                                    mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }


                            }


                        }

                    }


                    linear_l_w.setVisibility(View.VISIBLE);
                    linear_w.setVisibility(View.GONE);
                    linear_l_c.setVisibility(View.VISIBLE);
                    linear_c.setVisibility(View.GONE);
                    linear_weight.setVisibility(View.GONE);
                    linear_cou.setVisibility(View.GONE);
                    edit_name.setText("");
                    edit_mobile.setText("");
                    edit_fleet_no.setText("");
                    orgin_value.setText("Orgin");
                    Destination_value.setText("Destination");
                   // linear_submit.setVisibility(View.GONE);
                    linear_new_submit.setVisibility(View.GONE);


                } else {
                    Toast.makeText(NavigationActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                //Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void Orgin_Dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
        LayoutInflater inflater = LayoutInflater.from(NavigationActivity.this);
        // builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View content = inflater.inflate(R.layout.language_dialog, null);
        builder.setView(content);

        dialog_linear = (LinearLayout) content.findViewById(R.id.dialog_linear);

        frame = (FrameLayout) content.findViewById(R.id.frame);
        // dialog_linear.getBackground().setAlpha(150);

        sub_title = (CustomTextViewBold) content.findViewById(R.id.sub_title);

        //  Util.setFont(3, context, sub_title, "Select Your Preferred Language");

        close_icon = (ImageView) content.findViewById(R.id.close_icon);

        //recyclerview
        language_recyclerview = (RecyclerView) content.findViewById(R.id.language_recyclerview);
        linear_re = (LinearLayout) content.findViewById(R.id.linear_re);


        obj_orgin.size();

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_terms));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_language;

        if (Consts.orgin_value.equalsIgnoreCase("true")) {
            alertDialog.dismiss();
            Consts.orgin_value = "false";
            // session.getOrgin_value()
            orgin_value.setText(session.getOrgin_value());

        } else {


            Name = edit_name.getText().toString();
            Mobile = edit_mobile.getText().toString();
            Fleet = edit_fleet_no.getText().toString();


            session.setName(Name);
            session.setU_Mobile(Mobile);
            session.setFleet(Fleet);


            obj_language_adapter = new Language_Adapter(NavigationActivity.this, obj_orgin);
            LinearLayoutManager layoutManager = new LinearLayoutManager(NavigationActivity.this);
            language_recyclerview.setLayoutManager(layoutManager);
            language_recyclerview.setAdapter(obj_language_adapter);
            obj_language_adapter.notifyDataSetChanged();

        }


        //GET THE SELECTED DATA..

        //  getSelectedData();


        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    public void Destination_Dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
        LayoutInflater inflater = LayoutInflater.from(NavigationActivity.this);
        // builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View content = inflater.inflate(R.layout.destination_dialog, null);
        builder.setView(content);

        dialog_linear = (LinearLayout) content.findViewById(R.id.dialog_linear);
        // dialog_linear.getBackground().setAlpha(150);

        sub_title = (CustomTextViewBold) content.findViewById(R.id.sub_title);

        //  Util.setFont(3, context, sub_title, "Select Your Preferred Language");

        close_icon = (ImageView) content.findViewById(R.id.close_icon);

        //recyclerview
        des_recyclerview = (RecyclerView) content.findViewById(R.id.des_recyclerview);
        linear_re = (LinearLayout) content.findViewById(R.id.linear_re);

        obj_destinations.size();

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_terms));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_language;


        if (Consts.Des_value.equalsIgnoreCase("true")) {
            alertDialog.dismiss();
            Consts.Des_value = "false";
            // session.getOrgin_value()
            Destination_value.setText(session.getDes_value());

        } else {

            Name = edit_name.getText().toString();
            Mobile = edit_mobile.getText().toString();
            Fleet = edit_fleet_no.getText().toString();
            session.setName(Name);
            session.setU_Mobile(Mobile);
            session.setFleet(Fleet);
            destination_adapter = new Destination_Adapter(NavigationActivity.this, obj_destinations);
            LinearLayoutManager layoutManager = new LinearLayoutManager(NavigationActivity.this);
            des_recyclerview.setLayoutManager(layoutManager);
            des_recyclerview.setAdapter(destination_adapter);
            destination_adapter.notifyDataSetChanged();

        }


        //GET THE SELECTED DATA..

        //  getSelectedData();


        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        linear_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    //Print Functionality


    class DetectPrinterThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (runFlag) {
                float start_time = SystemClock.currentThreadTimeMillis();
                float end_time = 0;
                float time_lapse = 0;
                if (detectFlag) {
                    //检测打印是否正常 detect if printer is normal
                    try {
                        if (mIzkcService != null) {
                            String printerSoftVersion = mIzkcService.getFirmwareVersion1();
                            if (TextUtils.isEmpty(printerSoftVersion)) {
                                mIzkcService.setModuleFlag(module_flag);
                                end_time = SystemClock.currentThreadTimeMillis();
                                time_lapse = end_time - start_time;
//								enableOrDisEnableKey(false);
                                if (time_lapse > PINTER_LINK_TIMEOUT_MAX) {
                                    detectFlag = false;
                                    //打印机连接超时 printer link timeout
                                    sendEmptyMessage(MessageType.BaiscMessage.PRINTER_LINK_TIMEOUT);
                                }
                            } else {
                                //打印机连接成功 printer link success
                                sendMessage(MessageType.BaiscMessage.DETECT_PRINTER_SUCCESS, printerSoftVersion);
                                detectFlag = false;
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
                SystemClock.sleep(1);
            }
        }
    }

    @Override
    protected void onResume() {
        //开始检测打印机 begin to detect printer
        detectFlag = true;
        enableOrDisEnableKey(false);
        super.onResume();
    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            //服务绑定成功 service bind success
            case MessageType.BaiscMessage.SEVICE_BIND_SUCCESS:
//				Toast.makeText(this, getString(R.string.service_bind_success), Toast.LENGTH_SHORT).show();
                break;
            //服务绑定失败 service bind fail
            case MessageType.BaiscMessage.SEVICE_BIND_FAIL:
//				Toast.makeText(this, getString(R.string.service_bind_fail), Toast.LENGTH_SHORT).show();
                break;
            //打印机连接成功 printer link success
            case MessageType.BaiscMessage.DETECT_PRINTER_SUCCESS:
                String msg = (String) message.obj;
                checkPrintStateAndDisplayPrinterInfo(msg);
                break;
            //打印机连接超时 printer link timeout
            case MessageType.BaiscMessage.PRINTER_LINK_TIMEOUT:
                Toast.makeText(this, getString(R.string.printer_link_timeout), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkPrintStateAndDisplayPrinterInfo(String msg) {
        enableOrDisEnableKey(true);
        if (DEVICE_MODEL == 800) radio_cut.setVisibility(View.VISIBLE);
        if (!checkedPicFlag) generateBarCode();
        String status;
        String aidlServiceVersion;
        try {
            mIzkcService.getPrinterStatus();
            status = mIzkcService.getPrinterStatus();
            // tv_printStatus.setText(status);
            aidlServiceVersion = mIzkcService.getServiceVersion();
            //tv_printer_soft_version.setText(msg + "\nAIDL Service Version:" + aidlServiceVersion);
            //打印自检信息
//			mIzkcService.printerSelfChecking();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 构造SimpleAdapter的第二个参数，类型为List<Map<?,?>>
     *
     * @param
     * @return
     */
    private List<LanguageModel> getData() {
        Resources res = getResources();
        String[] cmdStr = res.getStringArray(R.array.language);
        List<LanguageModel> languageModelList = new ArrayList<>();
        for (int i = 0; i < cmdStr.length; i++) {
            String[] cmdArray = cmdStr[i].split(",");
            if (cmdArray.length == 3) {
                LanguageModel languageModel = new LanguageModel();
                languageModel.code = Integer.parseInt(cmdArray[0]);
                languageModel.language = cmdArray[1];
                languageModel.description = cmdArray[1] + " " + cmdArray[2];
                languageModelList.add(languageModel);
            }
        }
        return languageModelList;
    }




   /* printPurcase(false, false);

                if (DEVICE_MODEL == 800) {
        try {
            //半切
            mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }*/


    boolean run;

    private void MutiThreadPrint() {
        showStopMutiThreadPrintDialog();
        run = true;
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    printPic();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }


            }
        });
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    printGBKText();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    printPurcase(true, false);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    printPic();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });

    }

    private void showStopMutiThreadPrintDialog() {
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);
        builderDialog.setMessage("是否停止多线程打印......");
        builderDialog.setCancelable(false);
        builderDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                run = false;
            }
        });
        builderDialog.show();

    }

    private void enableOrDisEnableKey(boolean enable) {
        //  btnPrint.setEnabled(enable);
        //  btnUnicode.setEnabled(enable);
        // btnPrintPic.setEnabled(enable);
        //  btnPrintModelOne.setEnabled(enable);
        //  btnPrintModelTwo.setEnabled(enable);
        //  btnPrintModelThree.setEnabled(enable);
        // spinner_pic_style.setEnabled(enable);
        // spinnerLanguage.setEnabled(enable);
    }

    private void printBitmapUnicode1F30() {
        //  text = et_printText.getText().toString() + "\n";
        try {
            // mIzkcService.printUnicode_1F30(text);
            if (autoOutputPaper) {
                mIzkcService.generateSpace();
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printBitmapRaster() {
        try {
            if (mBitmap != null) {
                mIzkcService.printRasterImage(mBitmap);
                if (autoOutputPaper) {
                    mIzkcService.generateSpace();
                }
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printBitmapGray() {
        try {
            if (mBitmap != null) {
                mIzkcService.printImageGray(mBitmap);
                if (autoOutputPaper) {
                    mIzkcService.generateSpace();
                }
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }


    private void printPurcase(boolean hasStartPic, boolean hasEndPic) {
        SupermakerBill bill = PrinterHelper.getInstance(this).getSupermakerBill(mIzkcService, hasStartPic, hasEndPic);
        PrinterHelper.getInstance(this).printPurchaseBillModelOne(mIzkcService, bill, imageType);
    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            bit = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bit;


        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    private void printBitmapAlgin(int alginStyle) {
        //  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zkc);
//		Bitmap bitmap1 = resizeImage(bitmap, 376, 120);
        try {
            mIzkcService.printBitmapAlgin(bit, 376, 120, alginStyle);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void printTextAlgin(int alginStyle) {
        String pString = "智能打印\n";
        try {
            mIzkcService.printTextAlgin(pString, 0, 1, alginStyle);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void printVamplify(int type) {
        try {
            mIzkcService.printTextWithFont(text, fontType, type);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }


    private void printFont(int type) {
        try {
//			mIzkcService.setTypeface(type);
//			mIzkcService.printGBKText(text);
            mIzkcService.printTextWithFont(text, type, 0);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkedPicFlag = true;
        if (requestCode == REQUEST_EX && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            //   iv_printPic.setImageURI(selectedImage);
            mBitmap = BitmapFactory.decodeFile(picturePath);
            // iv_printPic.setImageBitmap(mBitmap);
            if (mBitmap.getHeight() > 384) {
                // iv_printPic.setImageBitmap(resizeImage(mBitmap, 384, 384));
            }
            cursor.close();
        }

        if (requestCode == REQUEST_ENABLE)
        {
            if (resultCode == RESULT_OK)
                Toast.makeText(activity, "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
            bt_disconnected.setVisibility(View.GONE);
            bt_connected.setVisibility(View.VISIBLE);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        if (width >= newWidth) {
            float scaleWidth = ((float) newWidth) / width;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {
            Bitmap bitmap2 = Bitmap.createBitmap(newWidth, newHeight,
                    bitmap.getConfig());
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawColor(Color.WHITE);

            canvas.drawBitmap(BitmapOrg, (newWidth - width) / 2, 0, null);

            return bitmap2;
        }
    }

    private void selectPic() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_EX);

    }

    private void generateQrCode() {
        try {
            mBitmap = mIzkcService.createQRCode("http://www.sznewbest.com", 384, 384);
            //   iv_printPic.setImageBitmap(mBitmap);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void generateBarCode() {
        try {
            mBitmap = mIzkcService.createBarCode("4333333367", 1, 384, 120, true);
            //iv_printPic.setImageBitmap(mBitmap);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printUnicode() {
        //  text = et_printText.getText().toString() + "\n";
        try {
            mIzkcService.printUnicodeText(text);
            if (autoOutputPaper) {
                mIzkcService.generateSpace();
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printGBKText() {
        // text = et_printText.getText().toString();

        try {
//			mIzkcService.printerInit();
//			mIzkcService.sendRAWData("printer", new byte[] { 0x1E, 0x04, 0x00, (byte) 0xBF, (byte) 0xD8, (byte) 0xD6, (byte) 0xC6});
            mIzkcService.printGBKText(text);
//			mIzkcService.printGBKText("\nАа Бб Вв Гг Дд  Ее Ёё  Жж Зз Ии Йй Кк Лл Мм Нн Оо Пп Рр Сс Тт Уу Фф Хх Цц Чч Шш Щщ ъ ы ь Ээ Юю Яя\n");
            if (autoOutputPaper) {
                mIzkcService.generateSpace();
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printPic() {
        try {
            if (mBitmap != null) {
                switch (imageType) {
                    case 0:
                        mIzkcService.printBitmap(mBitmap);
                        break;
                    case 1:
                        mIzkcService.printImageGray(mBitmap);
                        break;
                    case 2:
                        mIzkcService.printRasterImage(mBitmap);
                        break;
                }
                if (autoOutputPaper) {
                    mIzkcService.generateSpace();
                }
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

   /* @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_fontOne) {
            fontType = 0;
        } else if (checkedId == R.id.rb_fontTwo) {
            fontType = 1;
        }
    }*/

   /* private void wordToPic() {
        String str = et_printText.getText().toString();
        mBitmap = Bitmap.createBitmap(384, 30, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        canvas.drawColor(Color.WHITE);
        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25.0F);
        StaticLayout layout = new StaticLayout(str, textPaint,
                mBitmap.getWidth(), Layout.Alignment.ALIGN_NORMAL, (float) 1.0,
                (float) 0.0, true);
        layout.draw(canvas);
        iv_printPic.setImageBitmap(mBitmap);
    }*/

  /*  @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_language:
                setPrinterLanguage(position);
                break;
            case R.id.spinner_pic_style:
                imageType = position;
                break;
        }
    }*/

    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "发送指令");
        return super.onCreateOptionsMenu(menu);
    }*/

    private void showCmd() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "cmd.txt";
        //读取模板数据，按行保存
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(NavigationActivity.this,
                    "请按规定格式将指令保存在名为cmd.txt文件中，并复制在终端根目录下",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        CmdDialog cmdDialog = new CmdDialog(this, new CmdDialog.DialogCallBack() {
            @Override
            public void submit(String cmd) {
                String cmds = cmd;
                if (mIzkcService != null) {
                    byte[] buffer = hexStringToBytes(cmds);
                    try {
                        mIzkcService.sendRAWData("printer", buffer);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        cmdDialog.show();
    }

    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toLowerCase();
        String[] hexStrings = hexString.split(" ");
        byte[] bytes = new byte[hexStrings.length];
        for (int i = 0; i < hexStrings.length; i++) {
            char[] hexChars = hexStrings[i].toCharArray();
            bytes[i] = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
        }
        return bytes;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            showCmd();
        }
        return super.onOptionsItemSelected(item);
    }*/

    /* private void setPrinterLanguage(int position) {
         LanguageModel map = (LanguageModel) spinnerLanguage.getItemAtPosition(position);
         String languageStr = map.language;
         //语言描述
         String description = map.description;
         //语言指令
         int code = map.code;
         Log.d(TAG, "onItemClick: spinner_language=" + description + "," + code);

         //发送语言切换指令
 //		byte[] cmd_language=new byte[]{0x1B,0x74,0x00};
 //		cmd_language[2]=(byte)code;
         try {
 //            mIzkcService.sendRAWData("print", cmd_language);
             if (mIzkcService != null) mIzkcService.setPrinterLanguage(languageStr, code);
             //设置打印语言
         } catch (RemoteException e) {
             e.printStackTrace();
         }
     }
 */
  /*  @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
*/
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        //禁止打印
        if (mIzkcService != null) try {
            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x03, 0x00,
                    (byte) 0xBF, (byte) 0xD8, (byte) 0xD6, (byte) 0xC6});
        } catch (RemoteException e) {
            e.printStackTrace();
        }
       /* if (mAutoPrintThread != null) {
            //  mAutoPrintThread.interrupt();
            mAutoPrintThread = null;
        }*/
        if (mDetectPrinterThread != null) {
            runFlag = false;
            mDetectPrinterThread.interrupt();
            mDetectPrinterThread = null;
        }
        super.onDestroy();
    }

/*
    private class AutoPrintThread extends Thread {
        public void run() {
            // Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
            // R.drawable.order1);
            // Bitmap myBitmap = resizeImage(mBitmap, 384, 748);
            // Bitmap printBitmap = getBitmapPrint(myBitmap);
            while (isPrint) {
                startTimes = 0;
                endTimes = 0;
                timeSpace = 0;
                startTimes = System.currentTimeMillis();
                try {
                    if (cbAutoPrint.isChecked() && mIzkcService != null) {
                        printPurcase(false, true);
                        if (radio_cut.getCheckedRadioButtonId() == R.id.radioButton_cut) {
                            mIzkcService.sendRAWData("printer", new byte[]{0x1b, 0x6d});
                        } else if (radio_cut.getCheckedRadioButtonId() == R.id.radioButton_cutall) {
                            mIzkcService.sendRAWData("printer", new byte[]{0x1b, 0x69});
                        }
                        endTimes = System.currentTimeMillis();
                        timeSpace = Math.abs(endTimes - startTimes - 4000);
                        SystemClock.sleep(timeSpace);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/


    private void PrintPopUp() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        //builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View content = inflater.inflate(R.layout.print_popup, null);
        builder.setView(content);

        //  AlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        li_ok = (LinearLayout) content.findViewById(R.id.li_ok);
        lin_ca = (LinearLayout) content.findViewById(R.id.lin_ca);


        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view));


        li_ok.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {


                if (Consts.Method.equalsIgnoreCase("weighable")) {


                    Weighable();

                    alertDialog.dismiss();

                } else {
                    alertDialog.dismiss();


                    count_list = databaseHelper.getAllNotes();
                    linear_l_w.setVisibility(View.VISIBLE);
                    linear_w.setVisibility(View.GONE);
                    linear_l_c.setVisibility(View.VISIBLE);
                    linear_c.setVisibility(View.GONE);
                    linear_weight.setVisibility(View.GONE);
                    linear_cou.setVisibility(View.GONE);
                    edit_name.setText("");
                    edit_mobile.setText("");
                    edit_fleet_no.setText("");
                    orgin_value.setText("Orgin");
                    Destination_value.setText("Destination");
                 //   linear_submit.setVisibility(View.GONE);
                    linear_new_submit.setVisibility(View.GONE);


                    if (session.getImg_log().equalsIgnoreCase("")) {


                        printPurcase(false, false);

                        if (DEVICE_MODEL == 800) {
                            try {
                                //半切
                                // printVamplify(1);
                                mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
                                mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
                                mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }


                        }


                    } else {

                        //  String inn = session.getImg_log();
                        //  StringToBitMap(inn);

                        printPurcase(true, false);

                        if (DEVICE_MODEL == 800) {
                            try {
                                //半切
                                // printVamplify(1);
                                mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
                                mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
                                mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }


                        }


                    }


                }


            }
        });

        lin_ca.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (Consts.Method.equalsIgnoreCase("weighable")) {

                    wei = true;
                    Weighable();

                    alertDialog.dismiss();

                } else {

                    databaseHelper.deleteAllRecord();
                    count_list = databaseHelper.getAllNotes();
                    linear_l_w.setVisibility(View.VISIBLE);
                    linear_w.setVisibility(View.GONE);
                    linear_l_c.setVisibility(View.VISIBLE);
                    linear_c.setVisibility(View.GONE);
                    linear_weight.setVisibility(View.GONE);
                    linear_cou.setVisibility(View.GONE);
                    edit_name.setText("");
                    edit_mobile.setText("");
                    edit_fleet_no.setText("");
                    orgin_value.setText("Orgin");
                    Destination_value.setText("Destination");
                   // linear_submit.setVisibility(View.GONE);
                    linear_new_submit.setVisibility(View.GONE);


                    alertDialog.dismiss();
                }
            }
        });

    }


    //Count


    public void Get_Category() {

        Call<ListDetails> Category = apiInterface.Cat();
        Category.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                try {


                    if (response.isSuccessful()) {
                        MLog.e("Cat=>", "Cat==>" + response.body().msg);

                        MLog.e("Cat=>", "Cat==>" + response.body().status);


                        if (response.body().status.equalsIgnoreCase("success")) {

                            obj_categories = response.body().category;

                            if (!obj_categories.isEmpty()) {
                                obj_categories = response.body().category;
                                Log.e(TAG, "Cat:==>" + obj_categories.get(0).title);
                                MLog.e(TAG, "Cat_size==>" + obj_categories.size());

                                ArrayList<String> list = new ArrayList<>();
                                for (int arrIndx = 0; arrIndx < obj_categories.size(); arrIndx++) {
                                    list.add(obj_categories.get(arrIndx).title);
                                    Log.e(TAG, "Cat_name:==>" + obj_categories.get(arrIndx).title);
                                }
                      /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_text, list);
                        adapter.setDropDownViewResource(R.layout.spinner_selecter);
                        spinner_orgin.setAdapter(adapter);
                        spinner_orgin.getBackground().setColorFilter(android.graphics.Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);*/

                            } else {
                                Toast.makeText(NavigationActivity.this, "list_empty!", Toast.LENGTH_LONG).show();

                            }

                        } else {


                        }
                    } else {
                        Toast.makeText(NavigationActivity.this, "response null!", Toast.LENGTH_LONG).show();

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {
                Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();


            }


        });


    }


    public void Get_Product() {

        Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.category_id = session.getCategory();

        Call<ListDetails> product_list = apiInterface.product_list(commonRequest);
        product_list.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                if (response.body() != null) {
                    MLog.e("pro_list=>", "pro_list==>" + response.body().msg);

                    MLog.e("pro_list=>", "pro_list==>" + response.body().status);

                    obj_products = response.body().products;

                    if (!obj_products.isEmpty()) {
                        obj_products = response.body().products;
                        Log.e(TAG, "Product:==>" + obj_products.get(0).title);
                        MLog.e(TAG, "Product size==>" + obj_products.size());

                        ArrayList<String> list = new ArrayList<>();
                        for (int arrIndx = 0; arrIndx < obj_products.size(); arrIndx++) {
                            list.add(obj_products.get(arrIndx).title);
                            Log.e(TAG, "Product:==>" + obj_products.get(arrIndx).title);


                        }
                        Product_Dialog();

                    } else {
                        Toast.makeText(NavigationActivity.this, "Product Empty!", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(NavigationActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void Count_Prize() {
        Util.hideProgress();

        //   Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.origin_id = session.getOrgin();
        commonRequest.destination_id = session.getDestination();
        commonRequest.product_id = session.getProduct();

        Call<ListDetails> C_prize = apiInterface.Count_Prize(commonRequest);
        C_prize.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {
                //  Util.hideProgress();
                if (response.body() != null) {
                    MLog.e("Count_Prize=>", "Count_Prize==>" + response.body().msg);

                    MLog.e("Count_Prize=>", "Count_Prize==>" + response.body().status);

                    MLog.e("Count_Prize=>", "Count_Prize==>" + response.body().price);


                    if (response.body().status.equalsIgnoreCase("success")) {

                        session.setCountable(response.body().price);
                        try {
                            double pri = 0.0;
                            MLog.e(TAG, "price==>" + response.body().price);
                            pri = Double.parseDouble(response.body().price);
                            MLog.e(TAG, "double_price==>" + pri);
                            int count = Integer.parseInt(text_b.getText().toString());
                            int price = (int) pri;
                            MLog.e(TAG, "int_ptice==>" + price);
                            int total = count * price;
                            MLog.e(TAG, "total==>" + total);
                            text_prize.setText("" + total);
                            tot_amt_countable = String.valueOf(total);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }


                    } else {

                    }


                } else {
                    Toast.makeText(NavigationActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void Countable() {

        Consts.Method = "countable";

        Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_id = session.getUser_id();
        commonRequest.method = Consts.Method;
        commonRequest.name = session.getName();
        commonRequest.phone = session.getU_Mobile();
        commonRequest.fleetno = session.getFleet();
        commonRequest.origin_id = session.getOrgin();
        commonRequest.destination_id = session.getDestination();
        commonRequest.category_id = session.getCategory();
        commonRequest.product_id = session.getProduct();
        commonRequest.count = text_b.getText().toString().trim();
        commonRequest.price = tot_amt_countable;
        commonRequest.payment_mode = session.getPayment_mode();

        MLog.e(TAG, "post_values:==>" + "iser_id==>" + commonRequest.user_id + "\nmethod==>" + commonRequest.method + "\nname==>" + commonRequest.name + "\nphone==>" + commonRequest.phone +
                "\nfleetNo==>" + commonRequest.fleetno + "\norigin_id==>" + commonRequest.origin_id + "\ndestination_id==>" + commonRequest.destination_id + "\ncount==>" + commonRequest.count +
                "\nweight==>" + commonRequest.count + "\ntotal_amopunr==>" + commonRequest.price);


        Category = text_cat.getText().toString().trim();
        Product = text_prod.getText().toString().trim();


        Call<ListDetails> c_store = apiInterface.count_store(commonRequest);
        c_store.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {


                if (response.body() != null) {
                    if (response.body().status.equalsIgnoreCase("success")) {
                        MLog.e("Countable_Insert=>", "Countable_Insert==>" + response.body().msg);

                        MLog.e("Countable_Insert=>", "Countable_Insert==>" + response.body().status);

                        Toast.makeText(NavigationActivity.this, response.body().msg, Toast.LENGTH_LONG).show();

                        linear_relative.setVisibility(View.VISIBLE);
                        databaseHelper.insertCountable(Category, Product, text_b.getText().toString().trim(), tot_amt_countable);

                        session.setConsigment_no(response.body().consignmentno);

                        // for (int i = 0; i < obj_shipments.size(); i++) {
                        //       databaseHelper.insertCountable(obj_shipments.get(i).category_id, obj_shipments.get(i).product_id, obj_shipments.get(i).count, obj_shipments.get(i).price);
                        //   }


                        count_list = databaseHelper.getAllNotes();
                        databaseHelper.sum();
                        Log.e("sum", "sum==>" + databaseHelper.sum());


                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(databaseHelper.sum());


                        Log.e("currency", "Currecncy==>" + yourFormattedString);


                        text_total_count.setText(yourFormattedString);

                        SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("amount", yourFormattedString);
                        editor.commit();

                        session.setCount_Prize(yourFormattedString);
                   /* shipment_adapter = new Shipment_adapter(NavigationActivity.this, count_list);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(NavigationActivity.this);
                    Count_recyclerview.setLayoutManager(layoutManager);
                    Count_recyclerview.setAdapter(shipment_adapter);
                    shipment_adapter.notifyDataSetChanged();*/
                        //    Toast.makeText(NavigationActivity.this, databaseHelper.sum(), Toast.LENGTH_LONG).show();

                        View_Count();


                        session.setProduct("");
                        session.setProduct_value("Product");
                        session.setCategory("");
                        session.setCategory_value("Category");
                        text_b.setText("1");
                        text_prize.setText("");
                        text_prod.setText("Product");
                        text_cat.setText("Category");

                        Util.hideProgress();
                     //   linear_submit.setVisibility(View.VISIBLE);
                        linear_new_submit.setVisibility(View.VISIBLE);
                    } else if (response.body().status.equalsIgnoreCase("error")) {
                        Toast.makeText(NavigationActivity.this, response.body().msg, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(NavigationActivity.this, "null", Toast.LENGTH_SHORT).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void View_Count() {

        count_list = databaseHelper.getAllNotes();


        Log.e("db=>", "db==>" + count_list.size());

        if (count_list.size() > 0) {

            linear_relative.setVisibility(View.VISIBLE);
            text_total_count.setText(session.getCount_Prize());

            SharedPreferences preferences = getSharedPreferences("customer", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("amount", session.getCount_Prize());
            editor.commit();
            shipment_adapter = new Shipment_adapter(NavigationActivity.this, count_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(NavigationActivity.this);
            Count_recyclerview.setLayoutManager(layoutManager);
            Count_recyclerview.setAdapter(shipment_adapter);
            shipment_adapter.notifyDataSetChanged();

        } else {

            // Toast.makeText(NavigationActivity.this, "No List", Toast.LENGTH_LONG).show();

        }

    }

    public void Viewdf_Count() {

        //  Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_id = session.getUser_id();
        commonRequest.method = Consts.Method;

        final Call<ListDetails> c_list = apiInterface.count_list(commonRequest);
        c_list.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {
                Util.hideProgress();
                if (response.body() != null) {
                    MLog.e("ViewCount=>", "ViewCount==>" + response.body().msg);

                    MLog.e("ViewCount=>", "ViewCount==>" + response.body().status);

                    if (response.body().status.equalsIgnoreCase("success")) {
                        obj_shipments = response.body().shipment;
                        if (!obj_shipments.isEmpty()) {

                            linear_relative.setVisibility(View.VISIBLE);
                            //linear_submit.setVisibility(View.VISIBLE);
                            //linear_new_submit.setVisibility(View.VISIBLE);
                            obj_shipments = response.body().shipment;
                            MLog.e(TAG, "Ship size==>" + obj_shipments.size());
                            // databaseHelper.deleteAllRecord();
                            // count_list.clear();
                            // shipment_adapter = new Shipment_adapter(NavigationActivity.this, obj_shipments);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(NavigationActivity.this);
                            Count_recyclerview.setLayoutManager(layoutManager);
                            Count_recyclerview.setAdapter(shipment_adapter);
                            shipment_adapter.notifyDataSetChanged();


                        } else {
                            Toast.makeText(NavigationActivity.this, "Empty!", Toast.LENGTH_LONG).show();

                        }
                        if (!obj_shipments.isEmpty()) {

                            for (int i = 0; i < obj_shipments.size(); i++) {
                                databaseHelper.insertCountable(obj_shipments.get(i).category_id, obj_shipments.get(i).product_id, obj_shipments.get(i).count, obj_shipments.get(i).price);
                            }
                            count_list = databaseHelper.getAllNotes();
                        }


                        //databaseHelper.insertCountable();


                    } else if (response.body().status.equalsIgnoreCase("error")) {
                        linear_relative.setVisibility(View.GONE);
                    }


                } else {
                    Toast.makeText(NavigationActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void Delete() {

        //Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.order_id = session.getClose_cat_id();

        Call<ListDetails> del = apiInterface.delete(commonRequest);
        del.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {
                Util.hideProgress();
                if (response.body() != null) {
                    MLog.e("DEL_list=>", "DEL_list==>" + response.body().msg);

                    MLog.e("DEL_list=>", "DEL_list==>" + response.body().status);


                    if (response.body().status.equalsIgnoreCase("success")) {

                        // View_Count();


                    } else {
                    }
                } else

                {
                    Toast.makeText(NavigationActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();

            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(NavigationActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void Category_Dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
        LayoutInflater inflater = LayoutInflater.from(NavigationActivity.this);
        // builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View content = inflater.inflate(R.layout.countable_dialog, null);
        builder.setView(content);

        dialog_linear = (LinearLayout) content.findViewById(R.id.dialog_linear);
        // dialog_linear.getBackground().setAlpha(150);

        sub_title = (CustomTextViewBold) content.findViewById(R.id.sub_title);

        //  Util.setFont(3, context, sub_title, "Select Your Preferred Language");

        close_icon = (ImageView) content.findViewById(R.id.close_icon);

        //recyclerview
        cat_recyclerview = (RecyclerView) content.findViewById(R.id.cat_recyclerview);
        linear_re = (LinearLayout) content.findViewById(R.id.linear_re);

        obj_categories.size();

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_terms));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_language;


        if (Consts.Cat_value.equalsIgnoreCase("true")) {
            alertDialog.dismiss();
            Consts.Cat_value = "false";
            // session.getOrgin_value()
            text_cat.setText(session.getCategory_value());

        } else {

            category_adapter = new Category_Adapter(NavigationActivity.this, obj_categories);
            LinearLayoutManager layoutManager = new LinearLayoutManager(NavigationActivity.this);
            cat_recyclerview.setLayoutManager(layoutManager);
            cat_recyclerview.setAdapter(category_adapter);
            category_adapter.notifyDataSetChanged();

        }


        //GET THE SELECTED DATA..

        //  getSelectedData();


        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        linear_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    public void Product_Dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
        LayoutInflater inflater = LayoutInflater.from(NavigationActivity.this);
        // builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View content = inflater.inflate(R.layout.product_dialog, null);
        builder.setView(content);

        dialog_linear = content.findViewById(R.id.dialog_linear);
        // dialog_linear.getBackground().setAlpha(150);

        sub_title = content.findViewById(R.id.sub_title);

        //  Util.setFont(3, context, sub_title, "Select Your Preferred Language");

        close_icon = content.findViewById(R.id.close_icon);

        //recyclerview
        pro_recyclerview = (RecyclerView) content.findViewById(R.id.pro_recyclerview);
        linear_re = (LinearLayout) content.findViewById(R.id.linear_re);

        obj_products.size();

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_terms));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_language;


        if (Consts.Pro_Value.equalsIgnoreCase("true")) {
            alertDialog.dismiss();
            Consts.Pro_Value = "false";
            // session.getOrgin_value()
            text_prod.setText(session.getProduct_value());

            Count_Prize();

        } else {

            product_adapter = new Product_Adapter(NavigationActivity.this, obj_products);
            LinearLayoutManager layoutManager = new LinearLayoutManager(NavigationActivity.this);
            pro_recyclerview.setLayoutManager(layoutManager);
            pro_recyclerview.setAdapter(product_adapter);
            product_adapter.notifyDataSetChanged();

        }


        //GET THE SELECTED DATA..

        //  getSelectedData();


        close_icon.setOnClickListener(v -> alertDialog.dismiss());

        linear_re.setOnClickListener(v -> alertDialog.dismiss());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Application");
        builder.setMessage("Do you want to close the Application?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Exit", (dialog, which) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                MLog.e("ok", "ok");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                NavigationActivity.this.finish();
                System.exit(0);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
            }
        });

        builder.setNeutralButton("Reload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                edit_name.setText("");
                edit_mobile.setText("");
                edit_fleet_no.setText("");
                orgin_value.setText("Origin");
                Destination_value.setText("Destination");

                databaseHelper.deleteAllRecord();

                if (linear_l_c.getVisibility() == View.GONE)
                    linear_l_c.setVisibility(View.VISIBLE);

                if (linear_l_w.getVisibility() == View.GONE)
                    linear_l_w.setVisibility(View.VISIBLE);

                if (linear_c.getVisibility() == View.VISIBLE)
                    linear_c.setVisibility(View.GONE);

                if (linear_w.getVisibility() == View.VISIBLE)
                    linear_w.setVisibility(View.GONE);

                if (linear_new_submit.getVisibility() == View.VISIBLE)
                    linear_new_submit.setVisibility(View.GONE);

                if (linear_weight.getVisibility() == View.VISIBLE)
                    linear_weight.setVisibility(View.GONE);

                if (linear_cou.getVisibility() == View.VISIBLE)
                    linear_cou.setVisibility(View.GONE);

            }
        });
        builder.setOnCancelListener(null);
        builder.show();

        return true;
    }
}