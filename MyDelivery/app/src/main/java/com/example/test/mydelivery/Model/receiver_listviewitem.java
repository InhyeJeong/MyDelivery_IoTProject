package com.example.test.mydelivery.Model;



/**
 * Created by inhye on 2018-12-13.
 */


public class receiver_listviewitem {
    String title;
    String date;
    int img_id; //  이미지 얻어오기

    public receiver_listviewitem(String title, String date, int img_id) {
        this.title = title;
        this.date = date;
        this.img_id = img_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }
}
