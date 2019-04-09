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
import com.android.ninos.techmaaxx.BeanClass.Destination_list;
import com.android.ninos.techmaaxx.Destination_Adapter;
import com.android.ninos.techmaaxx.NavigationActivity;
import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.android.ninos.techmaaxx.session.Session;

import java.util.ArrayList;

/**
 * Created by bala on 10/12/2018.
 */

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {

    public Activity activity;

    public ArrayList<Category_list> obj_categories = new ArrayList<>();
    public ArrayList<String> mSelectedList;

    Session session;

    public int id = 0;


    public Category_Adapter(Activity activity, ArrayList<Category_list> obj_categories) {

        this.activity = activity;
        this.obj_categories = obj_categories;
        session = new Session(activity);
    }


    @Override
    public Category_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_adapter, parent, false);


        return new Category_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Category_Adapter.ViewHolder holder, final int position) {


        holder.item_title.setText(obj_categories.get(position).title);

        holder.item_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Consts.Cat_value = "true";
                session.setCategory(obj_categories.get(position).id);
                session.setCategory_value(obj_categories.get(position).title);
                Intent intent = new Intent(activity, NavigationActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                activity.finish();


            }
        });

    }


    @Override
    public int getItemCount() {
        return obj_categories.size();
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
