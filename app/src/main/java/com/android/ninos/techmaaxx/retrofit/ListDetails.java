package com.android.ninos.techmaaxx.retrofit;


import com.android.ninos.techmaaxx.BeanClass.Category_list;
import com.android.ninos.techmaaxx.BeanClass.CountList;
import com.android.ninos.techmaaxx.BeanClass.CustomerDetails;
import com.android.ninos.techmaaxx.BeanClass.Destination_list;
import com.android.ninos.techmaaxx.BeanClass.Orgin_list;
import com.android.ninos.techmaaxx.BeanClass.Product_list;
import com.android.ninos.techmaaxx.BeanClass.Report;
import com.android.ninos.techmaaxx.BeanClass.Ship;
import com.android.ninos.techmaaxx.BeanClass.Weight_list;

import java.util.ArrayList;
import java.util.List;


public class ListDetails {
    public String status;
    public String user_id;

    public String obvious;

    public String free_code_count;
    public CustomerDetails customer;

    public Weight_list groups;
    public CountList countable_price;
    public String name;
    public String mobile;
    public String email;
    public String branch_name;
    public String price;
    public String room_no;
    public String total_price;
    public String mobile_money_price;
    public String cash_price;
    public String consignmentno;


    public ArrayList<Orgin_list> origin = new ArrayList<>();

    public ArrayList<Ship> shipment = new ArrayList<>();

    public ArrayList<Destination_list> destinations = new ArrayList<>();
    public ArrayList<Category_list> category = new ArrayList<>();
    public ArrayList<Product_list> products = new ArrayList<>();

    public ArrayList<Report> today_report = new ArrayList<>();
    public ArrayList<Report> yesterday_report = new ArrayList<>();


    public String msg;
    public String user_bot_report_id;


    public String remaining_count;

}
