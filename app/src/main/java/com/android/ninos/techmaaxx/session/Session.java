package com.android.ninos.techmaaxx.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.ninos.techmaaxx.Setting.SettingActivity;
import com.android.ninos.techmaaxx.retrofit.APIClient;

/**
 * Created by l on 22-09-2018.
 */

public class Session {

    public SharedPreferences prefss;
    public SharedPreferences.Editor editorr;

    public final String user_id = "user_id";
    public final String username = "username";
    public final String password = "password";
    public final String email = "email";
    public final String mobile = "mobile";
    public final String status = "status";
    public final String approve_status = "approv_status";

    public final String Orgin_value = "orgin";
    public final String Des_value = "des";


    public final String Name = "name";
    public final String U_Mobile = "u_mobile";
    public final String Fleet = "fleet";
    public final String Orgin = "org";
    public final String Destination = "destination";
    public final String Method = "method";
    public final String Box = "box";
    public final String Countable = "count";
    public final String W_Prize = "w_p";

    public final String W_Weight = "w_wei";
    public final String Weighable_total = "w_to";


    public final String Category = "category";
    public final String Product = "product";
    public final String Category_value = "c_value";
    public final String Product_value = "p_value";

    public final String Close_cat_id = "close";

    public final String Currency_Symbol = "currency";


    public final String Company_Name = "com_name";
    public final String Company_add1 = "add1";
    public final String Company_add2 = "add2";
    public final String Count_Prize = "c_p";
    public final String Power_text = "pow";
    public final String Selected_method = "method";
    public final String report_type = "type_report";

    public final String Img_log = "log";

    public final String Company_domain = "nam";

    public final String Report_total = "r_total";

    public final String Payment_mode = "Payment_mode";

    public final String mobile_money_price = "mobile_money_price";
    public final String cash_price = "cash_price";
    public final String mac_address = "mac_address";

    public final String testPrint = "testPrint";

    public final String consigment_no = "consigmentNo";


    public Session(Context cntx) {
        // TODO Auto-generated constructor stub

        prefss = PreferenceManager.getDefaultSharedPreferences(cntx);
        editorr = prefss.edit();
    }

    public String getConsigment_no() {
        return prefss.getString(consigment_no,"NA");
    }

    public void setConsigment_no(String no) {
        prefss.edit().putString(consigment_no, no).apply();
    }

    public boolean getTestPrint() {
        return prefss.getBoolean(testPrint,false);
    }

    public void setTestPrint(boolean test) {
        prefss.edit().putBoolean(testPrint, test).apply();
    }

    public String getMac_address() {
        return prefss.getString(mac_address,"some text");
    }

    public void setMac_address(String mac) {
        prefss.edit().putString(mac_address, mac).apply();
    }


    public String getMobile_money_price() {
        String mobile = prefss.getString(mobile_money_price,"0");
        return mobile;
    }

    public String getCash_price() {
        String cash = prefss.getString(cash_price,"0");
        return cash;
    }

    public void setMobile_money_price(String price) {
        prefss.edit().putString(mobile_money_price, price).apply();
    }
    public void setCash_price(String price) {
        prefss.edit().putString(cash_price, price).apply();
    }


    public String getPayment_mode() {
        String Payment = prefss.getString(Payment_mode,"Cash");
        return Payment;
    }

    public void setPayment_mode(String payment) {
        prefss.edit().putString(Payment_mode, payment).apply();
    }

    public void setReport_total(String Report) {
        prefss.edit().putString(Report_total, Report).apply();
    }

    public String getReport_total() {
        String Report = prefss.getString(Report_total, "");
        return Report;
    }


    public void setCompany_domain(String domain) {
        prefss.edit().putString(Company_domain, domain).apply();
    }

    public String getCompany_domain() {
        String domain = prefss.getString(Company_domain, "");
        return domain;
    }


    public void setImg_log(String img) {
        prefss.edit().putString(Img_log, img).apply();
    }

    public String getImg_log() {
        String img = prefss.getString(Img_log, "");
        return img;
    }


    public void setPower_text(String power_text) {
        prefss.edit().putString(Power_text, power_text).apply();
    }

    public String getPower_text() {
        String power_text = prefss.getString(Power_text, "");
        return power_text;
    }

    public void setSelected_method(String s_method) {
        prefss.edit().putString(Selected_method, s_method).apply();
    }

    public String getSelected_method() {
        String s_method = prefss.getString(Selected_method, "");
        return s_method;
    }

    public void setReport_type(String r_type) {
        prefss.edit().putString(report_type, r_type).apply();
    }

    public String getReport_type() {
        String r_type = prefss.getString(report_type, "");
        return r_type;
    }

    public void setCount_Prize(String count_Prize) {
        prefss.edit().putString(Count_Prize, count_Prize).apply();
    }

    public String getCount_Prize() {
        String count_Prize = prefss.getString(Count_Prize, "");
        return count_Prize;
    }

    public void setCompany_Name(String company_Name) {
        prefss.edit().putString(Company_Name, company_Name).apply();
    }

    public String getCompany_Name() {
        String company_Name = prefss.getString(Company_Name, "");
        return company_Name;
    }


    public void setCompany_add1(String company_add1) {
        prefss.edit().putString(Company_add1, company_add1).apply();
    }

    public String getCompany_add1() {
        String company_add1 = prefss.getString(Company_add1, "");
        return company_add1;
    }


    public void setCompany_add2(String company_add2) {
        prefss.edit().putString(Company_add2, company_add2).apply();
    }

    public String getCompany_add2() {
        String company_add2 = prefss.getString(Company_add2, "");
        return company_add2;
    }


    public void setCurrency_Symbol(String symbol) {
        prefss.edit().putString(Currency_Symbol, symbol).apply();
    }

    public String getCurrency_Symbol() {
        String symbol = prefss.getString(Currency_Symbol, "");
        return symbol;
    }

    public void setW_Weight(String we) {
        prefss.edit().putString(W_Weight, we).apply();
    }

    public String getW_Weight() {
        String we = prefss.getString(W_Weight, "");
        return we;
    }


    public void setWeighable_total(String w_t) {
        prefss.edit().putString(Weighable_total, w_t).apply();
    }

    public String getWeighable_total() {
        String w_t = prefss.getString(Weighable_total, "");
        return w_t;
    }


    public void setClose_cat_id(String clo) {
        prefss.edit().putString(Close_cat_id, clo).apply();
    }

    public String getClose_cat_id() {
        String clo = prefss.getString(Close_cat_id, "");
        return clo;
    }


    public void setCategory(String category) {
        prefss.edit().putString(Category, category).apply();
    }

    public String getCategory() {
        String category = prefss.getString(Category, "");
        return category;
    }

    public void setCategory_value(String c_value) {
        prefss.edit().putString(Category_value, c_value).apply();
    }

    public String getCategory_value() {
        String c_value = prefss.getString(Category_value, "");
        return c_value;
    }

    public void setProduct(String pro) {
        prefss.edit().putString(Product, pro).apply();
    }

    public String getProduct() {
        String pro = prefss.getString(Product, "");
        return pro;
    }

    public void setProduct_value(String p_value) {
        prefss.edit().putString(Product_value, p_value).apply();
    }

    public String getProduct_value() {
        String p_value = prefss.getString(Product_value, "");
        return p_value;
    }


    public void setW_Prize(String wprize) {
        prefss.edit().putString(W_Prize, wprize).apply();
    }

    public String getW_Prize() {
        String wprize = prefss.getString(W_Prize, "");
        return wprize;
    }

    public void setName(String username) {
        prefss.edit().putString(Name, username).apply();
    }

    public String getName() {
        String username = prefss.getString(Name, "");
        return username;
    }

    public void setU_Mobile(String user_mobile) {
        prefss.edit().putString(U_Mobile, user_mobile).apply();
    }

    public String getU_Mobile() {
        String user_mobile = prefss.getString(U_Mobile, "");
        return user_mobile;
    }


    public void setFleet(String fleet) {
        prefss.edit().putString(Fleet, fleet).apply();
    }

    public String getFleet() {
        String fleet = prefss.getString(Fleet, "");
        return fleet;
    }


    public void setOrgin(String orgin) {
        prefss.edit().putString(Orgin, orgin).apply();
    }

    public String getOrgin() {
        String orgin = prefss.getString(Orgin, "");
        return orgin;
    }


    public void setDestination(String destination) {
        prefss.edit().putString(Destination, destination).apply();
    }

    public String getDestination() {
        String destination = prefss.getString(Destination, "");
        return destination;
    }


    public void setMethod(String method) {
        prefss.edit().putString(Method, method).apply();
    }

    public String getMethod() {
        String method = prefss.getString(Method, "");
        return method;
    }


    public void setBox(String box) {
        prefss.edit().putString(Box, box).apply();
    }

    public String getBox() {
        String box = prefss.getString(Box, "");
        return box;
    }

    public void setCountable(String count) {
        prefss.edit().putString(Countable, count).apply();
    }

    public String getCountable() {
        String count = prefss.getString(Countable, "");
        return count;
    }


    public void setOrgin_value(String orgin_value) {
        prefss.edit().putString(Orgin_value, orgin_value).apply();
    }

    public String getOrgin_value() {
        String orgin_value = prefss.getString(Orgin_value, "");
        return orgin_value;
    }


    public void setDes_value(String des_value) {
        prefss.edit().putString(Des_value, des_value).apply();
    }

    public String getDes_value() {
        String des_value = prefss.getString(Des_value, "");
        return des_value;
    }


    public void setUser_id(String id) {
        prefss.edit().putString(user_id, id).apply();
    }

    public String getUser_id() {
        String id = prefss.getString(user_id, "");
        return id;
    }

    public String getStatus() {
        String sta = prefss.getString(status, "");
        return sta;
    }

    public void setStatus(String status_user) {
        prefss.edit().putString(status, status_user).apply();
    }

    public String getApprove_status() {
        String sta = prefss.getString(approve_status, "");
        return sta;
    }

    public void setApprove_status(String status_approval) {
        prefss.edit().putString(approve_status, status_approval).apply();
    }


    public void setMobile(String number) {
        prefss.edit().putString(mobile, number).apply();
    }

    public String getMobile() {
        String number = prefss.getString(mobile, "");
        return number;
    }

    public void setUsername(String name) {
        prefss.edit().putString(username, name).apply();
    }

    public String getUsername() {
        String name = prefss.getString(username, "");
        return name;
    }

    public void setPassword(String pwd) {
        prefss.edit().putString(password, pwd).apply();
    }

    public String getPassword() {
        String name = prefss.getString(password, "");
        return name;
    }

    public void setEmail(String mail) {
        prefss.edit().putString(email, mail).apply();
    }

    public String getEmail() {
        String mail = prefss.getString(email, "");
        return mail;
    }
}