package com.android.ninos.techmaaxx.Countable;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ninos.techmaaxx.BeanClass.Product_list;
import com.android.ninos.techmaaxx.BeanClass.Ship;
import com.android.ninos.techmaaxx.NavigationActivity;
import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.SplashActivity;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.android.ninos.techmaaxx.database.Bluetooth;
import com.android.ninos.techmaaxx.database.DatabaseHelper;
import com.android.ninos.techmaaxx.retrofit.RetrofitInterface;
import com.android.ninos.techmaaxx.session.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by bala on 10/12/2018.
 */

public class Shipment_adapter extends RecyclerView.Adapter<Shipment_adapter.ViewHolder> {

    public Activity activity;

    //   public ArrayList<Ship> obj_shipments = new ArrayList<>();

    List<Bluetooth> count_list = new ArrayList<>();
    public ArrayList<String> mSelectedList;
    Session session;
    DatabaseHelper databaseHelper;
    public int id = 0;


    public Shipment_adapter(Activity activity, List<Bluetooth> count_list) {

        this.activity = activity;
        this.count_list = count_list;
        session = new Session(activity);
        databaseHelper = new DatabaseHelper(activity);

    }


    @Override
    public Shipment_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ship, parent, false);


        return new Shipment_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Shipment_adapter.ViewHolder holder, final int position) {
        count_list = databaseHelper.getAllNotes();

        holder.text_cat.setText(count_list.get(position).cat);
        holder.text_prod.setText(count_list.get(position).pro);
        holder.text_b.setText(count_list.get(position).box);
        holder.text_prize.setText(count_list.get(position).prize);

      /*  for (int i = 0; i < count_list.size(); i++) {


        }*/


        holder.close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    databaseHelper.deleteRow(count_list.get(position).cat);
                    //count_list.remove(position - 1);


                    count_list = databaseHelper.getAllNotes();

                    Log.e("db=>", "db==>" + count_list.size());


                    Consts.ship_value = "true";
                Intent intent = new Intent(activity, NavigationActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                activity.finish();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();

                }


              /*  Consts.ship_value = "true";
                session.setClose_cat_id(count_list.get(position).id);
                Toast.makeText(activity, session.getClose_cat_id(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, NavigationActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                activity.finish();*/

            }
        });

    }


    @Override
    public int getItemCount() {
        return count_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomTextviewRegular text_cat, text_prod, text_b, text_prize;
        ImageView close_icon;
        public int position;


        public ViewHolder(View itemView) {
            super(itemView);


            text_cat = (CustomTextviewRegular) itemView.findViewById(R.id.text_cat);
            text_prod = (CustomTextviewRegular) itemView.findViewById(R.id.text_prod);
            text_b = (CustomTextviewRegular) itemView.findViewById(R.id.text_b);
            text_prize = (CustomTextviewRegular) itemView.findViewById(R.id.text_prize);
            close_icon = (ImageView) itemView.findViewById(R.id.close_icon);


        }
    }
}

