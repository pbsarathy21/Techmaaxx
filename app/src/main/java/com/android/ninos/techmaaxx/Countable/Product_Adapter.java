package com.android.ninos.techmaaxx.Countable;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ninos.techmaaxx.BeanClass.Category_list;
import com.android.ninos.techmaaxx.BeanClass.Product_list;
import com.android.ninos.techmaaxx.NavigationActivity;
import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.android.ninos.techmaaxx.session.Session;

import java.util.ArrayList;

/**
 * Created by bala on 10/12/2018.
 */

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.ViewHolder> {

    public Activity activity;

    public ArrayList<Product_list> obj_products = new ArrayList<>();
    public ArrayList<String> mSelectedList;

    Session session;

    public int id = 0;


    public Product_Adapter(Activity activity, ArrayList<Product_list> obj_products) {

        this.activity = activity;
        this.obj_products = obj_products;
        session = new Session(activity);
    }


    @Override
    public Product_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_adapter, parent, false);


        return new Product_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Product_Adapter.ViewHolder holder, final int position) {


        holder.item_title.setText(obj_products.get(position).title);

        holder.item_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Consts.Pro_Value = "true";
                session.setProduct(obj_products.get(position).id);
                session.setProduct_value(obj_products.get(position).title);
                Intent intent = new Intent(activity, NavigationActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                activity.finish();


            }
        });

    }


    @Override
    public int getItemCount() {
        return obj_products.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomTextviewRegular item_title;
        ImageView profile_icon, edit_icon;
        public int position;
        LinearLayout item_linear;

        public ViewHolder(View itemView) {
            super(itemView);


            item_title = (CustomTextviewRegular) itemView.findViewById(R.id.item_title);
            item_linear = (LinearLayout) itemView.findViewById(R.id.item_linear);


        }
    }
}
