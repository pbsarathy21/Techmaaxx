package com.android.ninos.techmaaxx.Utils;

/**
 * Created by l on 02-08-2018.
 */

public class HoursList {

    public String hour;
    public String hh_mm_ss;

    public HoursList(String hour, String hh_mm_ss) {
        this.hour = hour;
        this.hh_mm_ss = hh_mm_ss;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getHh_mm_ss() {
        return hh_mm_ss;
    }

    public void setHh_mm_ss(String hh_mm_ss) {
        this.hh_mm_ss = hh_mm_ss;
    }
}
