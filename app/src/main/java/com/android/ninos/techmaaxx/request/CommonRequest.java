package com.android.ninos.techmaaxx.request;


import com.google.gson.annotations.SerializedName;


public class CommonRequest {


    //SERVICE
    public String user_query;
    public String is_android;
    public String user_id;
    public String method;
    public String name;
    public String mobile;
    public String email;
    public String password;
    public String phone;
    public String fleetno;
    public String new_password;
    public String confirm_password;
    public String room_id;
    public String branch_id;
    public String origin_id;
    public String category_id;
    public String order_id;
    public String destination_id;
    public String product_id;
    public String count;
    public String total_amount;
    public String price;
    public String weight;
    public String age;
    public String gender;
    public String user_bot_report_id;
    public String type;
    public String lat;
    public String id;
    public String old_password;
    public String payment_mode;


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @SerializedName("long")

    public String longitude;

}
