package com.android.ninos.techmaaxx.Report;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.ninos.techmaaxx.BeanClass.Orgin_list;
import com.android.ninos.techmaaxx.BeanClass.Report;
import com.android.ninos.techmaaxx.Language_Adapter;
import com.android.ninos.techmaaxx.NavigationActivity;
import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.android.ninos.techmaaxx.session.Session;

import java.util.ArrayList;

/**
 * Created by bala on 10/16/2018.
 */

public class Report_adapter extends RecyclerView.Adapter<Report_adapter.ViewHolder> {

    public Activity activity;

    public ArrayList<Report> obj_report = new ArrayList<>();
    public ArrayList<String> mSelectedList;

    Session session;

    public int id = 0;


    public Report_adapter(Activity activity, ArrayList<Report> obj_report) {

        this.activity = activity;
        this.obj_report = obj_report;
        session = new Session(activity);
    }


    @Override
    public Report_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_report, parent, false);


        return new Report_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Report_adapter.ViewHolder holder, final int position) {


        holder.text_date.setText(obj_report.get(position).date);
        holder.text_cons.setText(obj_report.get(position).consignmentno);
        holder.text_orgin.setText(obj_report.get(position).origin);
        holder.text_destination.setText(obj_report.get(position).destination);
        holder.text_name.setText(obj_report.get(position).name);
        holder.text_prize.setText(obj_report.get(position).freight);
        holder.text_pay_value.setText(obj_report.get(position).payment_mode);

    }


    @Override
    public int getItemCount() {
        return obj_report.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomTextviewRegular text_date, text_cons, text_orgin, text_destination, text_name, text_prize, text_pay_value;
        ImageView profile_icon, edit_icon;
        public int position;
        LinearLayout item_linear;

        public ViewHolder(View itemView) {
            super(itemView);


            text_date = (CustomTextviewRegular) itemView.findViewById(R.id.text_date);
            text_cons = (CustomTextviewRegular) itemView.findViewById(R.id.text_cons);
            text_orgin = (CustomTextviewRegular) itemView.findViewById(R.id.text_orgin);
            text_destination = (CustomTextviewRegular) itemView.findViewById(R.id.text_destination);
            text_name = (CustomTextviewRegular) itemView.findViewById(R.id.text_name);
            text_prize = (CustomTextviewRegular) itemView.findViewById(R.id.text_prize);

            text_pay_value = (CustomTextviewRegular) itemView.findViewById(R.id.text_pay_value);


        }
    }
}

