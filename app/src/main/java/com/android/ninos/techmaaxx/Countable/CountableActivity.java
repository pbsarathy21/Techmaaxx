package com.android.ninos.techmaaxx.Countable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.ninos.techmaaxx.BeanClass.Category_list;
import com.android.ninos.techmaaxx.BeanClass.CountList;
import com.android.ninos.techmaaxx.BeanClass.Product_list;
import com.android.ninos.techmaaxx.BeanClass.Ship;
import com.android.ninos.techmaaxx.NavigationActivity;
import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.CustomEditTextviewRegular;
import com.android.ninos.techmaaxx.Utils.CustomTextViewBold;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.android.ninos.techmaaxx.Utils.MLog;
import com.android.ninos.techmaaxx.Utils.Util;
import com.android.ninos.techmaaxx.Utils.Validation;
import com.android.ninos.techmaaxx.database.Bluetooth;
import com.android.ninos.techmaaxx.database.DatabaseHelper;
import com.android.ninos.techmaaxx.request.CommonRequest;
import com.android.ninos.techmaaxx.retrofit.ListDetails;
import com.android.ninos.techmaaxx.retrofit.RetrofitInterface;
import com.android.ninos.techmaaxx.session.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CountableActivity extends AppCompatActivity {
    RetrofitInterface apiInterface;
    public ArrayList<Category_list> obj_categories = new ArrayList<>();
    public ArrayList<Product_list> obj_products = new ArrayList<>();
    public ArrayList<Ship> obj_shipments = new ArrayList<>();

    private Vibrator vib;
    Animation animShake;
    public static final String TAG = CountableActivity.class.getSimpleName();

    Activity activity;
    Context context;
    Session session;

    LinearLayout dialog_linear, linear_re, linear_submit;
    CustomTextViewBold sub_title;
    ImageView close_icon;
    RecyclerView cat_recyclerview, pro_recyclerview, Count_recyclerview;
    Category_Adapter category_adapter;
    Product_Adapter product_adapter;

    Shipment_adapter shipment_adapter;
    CustomTextviewRegular text_cat, text_prod, text_total_count;
    CustomEditTextviewRegular text_b, text_prize;
    LinearLayout linear_s, linear_cat, linear_p, linear_relative;
    List<Bluetooth> count_list = new ArrayList<>();

    ImageView menu;
    Validation validation;

    public int total_count = 0, count = 0, wallet, total;

    public String total_amt;

    String Box, Prize;
    boolean box, prize;
    public CountList obj_count_p;

    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countable);

        activity = CountableActivity.this;
        context = CountableActivity.this;
        //  validation = new Validation();
        session = new Session(this);
        validation = new Validation();
        initViews();

        text_cat.setText(session.getCategory_value());
        text_prod.setText(session.getProduct_value());
        Util.hideProgress();

        View_Count();
        if (Consts.Cat_value.equalsIgnoreCase("true")) {
            Category_Dialog();
        } else {


        }

        if (Consts.Pro_Value.equalsIgnoreCase("true")) {
            Product_Dialog();
        } else {


        }

        if (Consts.ship_value.equalsIgnoreCase("true")) {
            Delete();

        } else {

        }


        text_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category_Dialog();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CountableActivity.this, NavigationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();

            }
        });

        text_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Get_Product();

            }
        });


        linear_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Box = text_b.getText().toString().trim();
                Prize = text_prize.getText().toString().trim();


                if (!validation.isEmpty(Box)) {
                    box = true;
                    text_b.setError(null);

                } else {
                    box = false;
                    text_b.setError("Enter Your Box!");
                    text_b.setAnimation(animShake);
                    text_b.startAnimation(animShake);
                    vib.vibrate(120);
                }

                if (!validation.isEmpty(Prize)) {
                    prize = true;
                    text_prize.setError(null);

                } else {
                    prize = false;
                    text_prize.setError("Enter Box !!");
                    text_prize.setAnimation(animShake);
                    text_prize.startAnimation(animShake);
                    vib.vibrate(120);
                }


                if (box && prize) {

                    Countable();

                } else {

                    Toast.makeText(CountableActivity.this, "Select the Category ,Product", Toast.LENGTH_SHORT).show();


                }
            }
        });

        text_b.addTextChangedListener(textWatcher);

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
                total_count = count * w_prize;
                MLog.e(TAG, "total_count==>" + total_count);
                total_amt = String.valueOf(total_count);
                MLog.e(TAG, "total_amt_string==>" + total_amt);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            text_prize.setText(total_amt);

            Util.hideProgress();
        }
    };


    public void initViews() {

        session = new Session(context);
        MLog.e(TAG, "name==>" + session.getUsername());

        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(RetrofitInterface.class);


        databaseHelper = new DatabaseHelper(CountableActivity.this);
        text_cat = (CustomTextviewRegular) findViewById(R.id.text_cat);
        text_prod = (CustomTextviewRegular) findViewById(R.id.text_prod);

        text_total_count = (CustomTextviewRegular) findViewById(R.id.text_total_count);

        text_b = (CustomEditTextviewRegular) findViewById(R.id.text_b);

        text_prize = (CustomEditTextviewRegular) findViewById(R.id.text_prize);

        linear_s = (LinearLayout) findViewById(R.id.linear_s);
        menu = (ImageView) findViewById(R.id.menu);
        linear_cat = (LinearLayout) findViewById(R.id.linear_cat);
        linear_p = (LinearLayout) findViewById(R.id.linear_p);
        linear_relative = (LinearLayout) findViewById(R.id.linear_relative);
        Count_recyclerview = (RecyclerView) findViewById(R.id.Count_recyclerview);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Get_Category();


    }

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
                                Toast.makeText(CountableActivity.this, "list_empty!", Toast.LENGTH_LONG).show();

                            }

                        } else {


                        }
                    } else {
                        Toast.makeText(CountableActivity.this, "response null!", Toast.LENGTH_LONG).show();

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {
                Toast.makeText(CountableActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();


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
                        Toast.makeText(CountableActivity.this, "Product Empty!", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(CountableActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(CountableActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
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


                    } else {

                    }


                } else {
                    Toast.makeText(CountableActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(CountableActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void Countable() {

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
        commonRequest.price = text_prize.getText().toString().trim();


        Call<ListDetails> c_store = apiInterface.count_store(commonRequest);
        c_store.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                if (response.body() != null) {
                    MLog.e("Countable_Insert=>", "Countable_Insert==>" + response.body().msg);

                    MLog.e("Countable_Insert=>", "Countable_Insert==>" + response.body().status);


                    Toast.makeText(CountableActivity.this, response.body().msg, Toast.LENGTH_LONG).show();

                    View_Count();
                    Util.hideProgress();


                } else {
                    Toast.makeText(CountableActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(CountableActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void View_Count() {

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
                            obj_shipments = response.body().shipment;
                            MLog.e(TAG, "Ship size==>" + obj_shipments.size());


                         //   shipment_adapter = new Shipment_adapter(CountableActivity.this, obj_shipments);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(CountableActivity.this);
                            Count_recyclerview.setLayoutManager(layoutManager);
                            Count_recyclerview.setAdapter(shipment_adapter);
                            shipment_adapter.notifyDataSetChanged();


                            text_total_count.setText(response.body().total_price);


                        } else {
                            Toast.makeText(CountableActivity.this, "Empty!", Toast.LENGTH_LONG).show();

                        }
                        if (!obj_shipments.isEmpty()) {

                            for (int i = 0; i < obj_shipments.size(); i++) {
                                databaseHelper.insertCountable(obj_shipments.get(i).category_id, obj_shipments.get(i).product_id, obj_shipments.get(i).count, obj_shipments.get(i).price);
                            }
                            count_list = databaseHelper.getAllNotes();

                        }

                        Log.e("db=>", "db==>" + count_list.size());
                        //databaseHelper.insertCountable();


                    } else if (response.body().status.equalsIgnoreCase("error")) {
                        linear_relative.setVisibility(View.GONE);
                    }


                } else {
                    Toast.makeText(CountableActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(CountableActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
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

                        View_Count();
                        Consts.ship_value = "false";


                    } else {
                    }
                } else

                {
                    Toast.makeText(CountableActivity.this, "response null!", Toast.LENGTH_LONG).show();
                }

                Util.hideProgress();
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(CountableActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                Util.hideProgress();
            }


        });
    }


    public void Category_Dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CountableActivity.this);
        LayoutInflater inflater = LayoutInflater.from(CountableActivity.this);
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

            category_adapter = new Category_Adapter(CountableActivity.this, obj_categories);
            LinearLayoutManager layoutManager = new LinearLayoutManager(CountableActivity.this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(CountableActivity.this);
        LayoutInflater inflater = LayoutInflater.from(CountableActivity.this);
        // builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View content = inflater.inflate(R.layout.product_dialog, null);
        builder.setView(content);

        dialog_linear = (LinearLayout) content.findViewById(R.id.dialog_linear);
        // dialog_linear.getBackground().setAlpha(150);

        sub_title = (CustomTextViewBold) content.findViewById(R.id.sub_title);

        //  Util.setFont(3, context, sub_title, "Select Your Preferred Language");

        close_icon = (ImageView) content.findViewById(R.id.close_icon);

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
            linear_s.setVisibility(View.VISIBLE);
            Count_Prize();

        } else {

            product_adapter = new Product_Adapter(CountableActivity.this, obj_products);
            LinearLayoutManager layoutManager = new LinearLayoutManager(CountableActivity.this);
            pro_recyclerview.setLayoutManager(layoutManager);
            pro_recyclerview.setAdapter(product_adapter);
            product_adapter.notifyDataSetChanged();

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(CountableActivity.this, NavigationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();

        }
        return true;
    }
}
