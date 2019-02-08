package com.android.ninos.techmaaxx.database;

/**
 * Created by ravi on 20/02/18.
 */

public class Bluetooth {
    public static final String TABLE_NAME = "notes";


    // Sign up database
    public static final String TABLE_SIGN_UP_NAME = "signup";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_MOBILE_NUMBER = "mobile_number";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ID = "id";

    public static final String TABLE_BOOKING = "booking";
    public static final String TABLE_SELECTED_DATE = "selected_date";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DATE_MM_YYYY = "date_mm_yy";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_DAY_FULLNAME = "day_full_name";
    public static final String COLUMN_BREAKFAST_ID = "breakfast_id";
    public static final String COLUMN_LUNCH_ID = "lunch_id";
    public static final String COLUMN_DINNER_ID = "dinner_id";
    public static final String COLUMN_BREAKFAST = "breakfast";
    public static final String COLUMN_LUNCH = "lunch";
    public static final String COLUMN_DINNER = "dinner";


    public static final String TABLE_SELECTED_BOOKING = "table_selected_item";

    public static final String KEY_Selected_Item = "selected_item";
    public static final String KEY_Selected_lunch = "selected_lunch";
    public static final String KEY_Selected_dinner = "selected_dinner";


    public static final String TABLE_COUNT = "table_co";

    public static final String KEY_CAT = "category";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_BOX = "box";
    public static final String KEY_PRIZE = "prize";


    public static final String TABLE_REPORT = "report";
    public static final String KEY_REPORT_NAME = "r_name";
    public static final String KEY_REPORT_CO = "r_co";
    public static final String KEY_REPORT_ORGIN = "r_orgin";
    public static final String KEY_REPORT_DESTINATION = "r_des";
    public static final String KEY_REPORT_FREIGHT = "r_freight";
    public static final String KEY_REPORT_DATE = "r_date";
    public static final String KEY_PAYMENT_MODE = "p_mode";


    public static final String REPORT_LIST = "CREATE TABLE " + TABLE_REPORT + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_REPORT_NAME + " TEXT,"
            + KEY_REPORT_CO + " TEXT,"
            + KEY_REPORT_ORGIN + " TEXT,"
            + KEY_REPORT_DESTINATION + " TEXT,"
            + KEY_REPORT_FREIGHT + " TEXT,"
            + KEY_REPORT_DATE + " TEXT,"
            + KEY_PAYMENT_MODE + " TEXT)";


    public static final String COUNTABLE_LIST = "CREATE TABLE " + TABLE_COUNT + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CAT + " TEXT,"
            + KEY_PRODUCT + " TEXT,"
            + KEY_BOX + " TEXT,"
            + KEY_PRIZE + " TEXT)";


    public static final String SELECTED_BOOKING_TABLE = "CREATE TABLE " + TABLE_SELECTED_BOOKING + "("
            + KEY_Selected_Item + " TEXT,"
            + KEY_Selected_lunch + " TEXT,"
            + KEY_Selected_dinner + " TEXT)";


    //create signup database
    public static final String CREATE_SIGN_UP_TABLE =
            "CREATE TABLE " + TABLE_SIGN_UP_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FIRST_NAME + " TEXT,"
                    + COLUMN_LAST_NAME + " TEXT,"
                    + COLUMN_MOBILE_NUMBER + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT)";
    //table inspections
    public static final String CREATE_TABLE_BOOKING = "CREATE TABLE " + TABLE_BOOKING + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DATE + " TEXT, "
            + COLUMN_TIME + " TEXT,"
            + COLUMN_BREAKFAST_ID + " TEXT,"
            + COLUMN_BREAKFAST + " TEXT,"
            + COLUMN_LUNCH_ID + " TEXT ,"
            + COLUMN_LUNCH + " TEXT ,"
            + COLUMN_DINNER_ID + " TEXT ,"
            + COLUMN_DINNER + " TEXT )";

    //table inspections
    public static final String CREATE_TABLE_SELECTED_DATE = "CREATE TABLE " + TABLE_SELECTED_DATE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DATE + " TEXT, "
            + COLUMN_DATE_MM_YYYY + " TEXT, "
            + COLUMN_DAY_FULLNAME + " TEXT, "
            + COLUMN_DAY + " TEXT )";


    private int id;
    String inspect_date;
    String position, date, time, break_fast, lunch, dinner, breakfast_id, lunch_id, dinner_id, day, day_full_name, date_mm_yyy;
    String selected_item;


    public String cat, pro, box, prize;


    public String r_name, r_con, r_orgin, r_des, r_freight, r_date, p_mode;

    public String getP_mode() {
        return p_mode;
    }

    public void setP_mode(String p_mode) {
        this.p_mode = p_mode;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getR_con() {
        return r_con;
    }

    public void setR_con(String r_con) {
        this.r_con = r_con;
    }

    public String getR_orgin() {
        return r_orgin;
    }

    public void setR_orgin(String r_orgin) {
        this.r_orgin = r_orgin;
    }

    public String getR_des() {
        return r_des;
    }

    public void setR_des(String r_des) {
        this.r_des = r_des;
    }

    public String getR_freight() {
        return r_freight;
    }

    public void setR_freight(String r_freight) {
        this.r_freight = r_freight;
    }

    public String getR_date() {
        return r_date;
    }

    public void setR_date(String r_date) {
        this.r_date = r_date;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }


    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }


    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay_full_name() {
        return day_full_name;
    }

    public void setDay_full_name(String day_full_name) {
        this.day_full_name = day_full_name;
    }

    public String getDate_mm_yyy() {
        return date_mm_yyy;
    }

    public void setDate_mm_yyy(String date_mm_yyy) {
        this.date_mm_yyy = date_mm_yyy;
    }

    public String getBreakfast_id() {
        return breakfast_id;
    }

    public void setBreakfast_id(String breakfast_id) {
        this.breakfast_id = breakfast_id;
    }

    public String getLunch_id() {
        return lunch_id;
    }

    public void setLunch_id(String lunch_id) {
        this.lunch_id = lunch_id;
    }

    public String getDinner_id() {
        return dinner_id;
    }

    public void setDinner_id(String dinner_id) {
        this.dinner_id = dinner_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInspect_date() {
        return inspect_date;
    }

    public void setInspect_date(String inspect_date) {
        this.inspect_date = inspect_date;
    }

    public String getSelected_item() {
        return selected_item;
    }

    public void setSelected_item(String selected_item) {
        this.selected_item = selected_item;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBreak_fast() {
        return break_fast;
    }

    public void setBreak_fast(String break_fast) {
        this.break_fast = break_fast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }
}
