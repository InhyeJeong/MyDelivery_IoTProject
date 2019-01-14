package com.example.test.mydelivery.ThirdDepth;

/**
 * Created by okooo on 2019-01-14.
 */

public class DateFormat {
    private String date;
    DateFormat(String date){
        this.date = date;
    }
    DateFormat(){

    }
    public void set_data(String date){
        this.date = date;
    }
    public String get_year(){
        return date.substring(0, 4);
    }
    public String get_month(){
        return date.substring(5, 7);
    }
    public String get_day(){
        return date.substring(8, 10);
    }
    public String get_hour(){
        return date.substring(11, 13);
    }
    public String get_min(){
        return date.substring(14, 16);
    }
    public String get_sec(){
        return date.substring(17, 19);
    }
    public String parse(){
        if (this.date.length() > 10){
            return get_year()+"/"+get_month()+"/"+get_day()+" "+get_hour()+":"+get_min()+":"+get_sec();
        }
        else{
            return "WAITING";
        }
    }
}
