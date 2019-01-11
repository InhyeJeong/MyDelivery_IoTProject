package com.example.test.mydelivery.Model;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by inhye on 2018-12-13.
 */


public class sender_listviewitem extends RealmObject implements Comparable<sender_listviewitem>  {
    String receiverName;
    String receiverAddress;
    String receiverPhone;

    String sender_qr; //  이미지 얻어오기
    String state;

    String SenderOpenTime;
    String SenderCloseTime;
    String ReceiverOpenTime;
    String ReceiverCloseTime;
    String CreatedAt;

    public sender_listviewitem() {
    }

    public sender_listviewitem(String receiverName, String receiverAddress, String receiverPhone, String sender_qr, String state, String senderOpenTime, String senderCloseTime, String receiverOpenTime, String receiverCloseTime, String createdAt) {
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverPhone = receiverPhone;
        this.sender_qr = sender_qr;
        this.state = state;
        this.SenderOpenTime = senderOpenTime;
        this.SenderCloseTime = senderCloseTime;
        this.ReceiverOpenTime = receiverOpenTime;
        this.ReceiverCloseTime = receiverCloseTime;
        this.CreatedAt = createdAt;
    }

    // 우선순위 큐를 위한 state끼리 비교 메서드
    @Override
    public int compareTo(@NonNull sender_listviewitem target) {
        if(this.state.equals(target.getState())){
            return this.CreatedAt.compareTo(target.getCreatedAt());
        }else {
            return this.state.compareTo(target.getState());
        }
    }

    //  getter and setter
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getSender_qr() {
        return sender_qr;
    }

    public void setSender_qr(String sender_qr) {
        this.sender_qr = sender_qr;
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
