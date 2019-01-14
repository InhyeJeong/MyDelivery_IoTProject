package com.example.test.mydelivery.Model;


import android.support.annotation.NonNull;

/**
 * Created by inhye on 2018-12-13.
 */


public class receiver_listviewitem implements Comparable<receiver_listviewitem> {
    String senderName;
    String senderPhone;

    String receiver_qr; //  이미지 얻어오기
    String state;

    String SenderOpenTime;
    String SenderCloseTime;
    String ReceiverOpenTime;
    String ReceiverCloseTime;
    String CreatedAt;

    public receiver_listviewitem(){}

    public receiver_listviewitem(String senderName, String senderPhone, String receiver_qr, String state, String senderOpenTime, String senderCloseTime, String receiverOpenTime, String receiverCloseTime, String createdAt) {
        this.senderName = senderName;
        this.senderPhone = senderPhone;
        this.receiver_qr = receiver_qr;
        this.state = state;
        SenderOpenTime = senderOpenTime;
        SenderCloseTime = senderCloseTime;
        ReceiverOpenTime = receiverOpenTime;
        ReceiverCloseTime = receiverCloseTime;
        CreatedAt = createdAt;
    }

    // 우선순위 큐를 위한 state끼리 비교 메서드
    @Override
    public int compareTo(@NonNull receiver_listviewitem target) {
        if(this.state.equals(target.getState())){
            return this.CreatedAt.compareTo(target.getCreatedAt());
        }else {

            // locked -> registered -> received 순서로 우선순위 매기기
            // 비교하기 위한 state를 담을 변수 m, o
            String m;
            String o;
            if(this.state.equals("0")) m = "1";
            else if(this.state.equals("1")) m = "0";
            else m = "2";

            if(target.state.equals("0")) o = "1";
            else if(target.state.equals("1")) o = "0";
            else o = "2";

            return m.compareTo(o);
        }
    }

    //  getter and setter
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getReceiver_qr() {
        return receiver_qr;
    }

    public void setReceiver_qr(String receiver_qr) {
        this.receiver_qr = receiver_qr;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSenderOpenTime() {
        return SenderOpenTime;
    }

    public void setSenderOpenTime(String senderOpenTime) {
        SenderOpenTime = senderOpenTime;
    }

    public String getSenderCloseTime() {
        return SenderCloseTime;
    }

    public void setSenderCloseTime(String senderCloseTime) {
        SenderCloseTime = senderCloseTime;
    }

    public String getReceiverOpenTime() {
        return ReceiverOpenTime;
    }

    public void setReceiverOpenTime(String receiverOpenTime) {
        ReceiverOpenTime = receiverOpenTime;
    }

    public String getReceiverCloseTime() {
        return ReceiverCloseTime;
    }

    public void setReceiverCloseTime(String receiverCloseTime) {
        ReceiverCloseTime = receiverCloseTime;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
