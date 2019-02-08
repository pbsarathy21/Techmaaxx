package com.android.ninos.techmaaxx;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ninos.techmaaxx.BeanClass.Destination_list;
import com.android.ninos.techmaaxx.BeanClass.Orgin_list;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.android.ninos.techmaaxx.session.Session;

import java.util.ArrayList;

/**
 * Created by bala on 10/12/2018.
 */

public class Language_Adapter extends RecyclerView.Adapter<Language_Adapter.ViewHolder> {

    public Activity activity;

    public ArrayList<Orgin_list> obj_orgin = new ArrayList<>();
    public ArrayList<String> mSelectedList;

    Session session;

    public int id = 0;


    public Language_Adapter(Activity activity, ArrayList<Orgin_list> obj_orgin) {

        this.activity = activity;
        this.obj_orgin = obj_orgin;
        session = new Session(activity);
    }


    @Override
    public Language_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_adapter, parent, false);


        return new Language_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Language_Adapter.ViewHolder holder, final int position) {


        holder.item_title.setText(obj_orgin.get(position).title);

        holder.item_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //  MainActivity mainActivity = new MainActivity();
                // activity.onOptionsItemSelected(Consts.Spinner_destination);



                //   Consts.Spinner_destination = obj_destinations.get(position).id;

                Consts.orgin_value = "true";
                session.setOrgin(obj_orgin.get(position).id);
                session.setOrgin_value(obj_orgin.get(position).title);
                Intent intent = new Intent(activity, NavigationActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                activity.finish();

                //  Toast.makeText(activity, Consts.Spinner_destination, Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return obj_orgin.size();
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
