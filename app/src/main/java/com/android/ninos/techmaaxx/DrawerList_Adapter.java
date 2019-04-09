package com.android.ninos.techmaaxx;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.android.ninos.techmaaxx.BeanClass.NavigationList;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by l on 09-02-2018.
 */

public class DrawerList_Adapter extends BaseAdapter {

    public static Activity activity;
    public List<NavigationList> obj_navigation_list;


    public static final String TAG = DrawerList_Adapter.class.getSimpleName();

    public DrawerList_Adapter(Activity activity, List<NavigationList> obj_navigation_list) {

        this.activity = activity;
        this.obj_navigation_list = obj_navigation_list;
    }

    @Override
    public int getCount() {
        return obj_navigation_list.size();
    }

    @Override
    public Object getItem(int position) {
        return obj_navigation_list.size();
    }

    @Override
    public long getItemId(int position) {
        return obj_navigation_list.size();
    }
    public class Holder{
        CustomTextviewRegular name;
        ImageView image,edit_icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View view;
        LayoutInflater inflater = ((Activity) activity).getLayoutInflater();

        view = inflater.inflate(R.layout.navigation_list, parent,false);

        holder.name = (CustomTextviewRegular) view.findViewById(R.id.name);
        holder.image = (ImageView) view.findViewById(R.id.image);
        holder.name.setText(obj_navigation_list.get(position).name);
        Picasso.with(activity.getApplicationContext()).load(obj_navigation_list.get(position).icon).into(holder.image);


        return view;
    }
}
