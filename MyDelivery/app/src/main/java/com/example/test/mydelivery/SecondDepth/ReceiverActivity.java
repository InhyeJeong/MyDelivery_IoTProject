package com.example.test.mydelivery.SecondDepth;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import android.widget.ListView;
import android.widget.TextView;


import com.example.test.mydelivery.Adapter.ReceiverListViewAdapter;
import com.example.test.mydelivery.Adapter.SenderListViewAdapter;
import com.example.test.mydelivery.Model.receiver_listviewitem;
import com.example.test.mydelivery.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class ReceiverActivity extends AppCompatActivity {
    //  리스트 뷰 객체를 저장하는 변수
    ListView listview_receiver;
    SenderListViewAdapter listViewAdapter;

    TextView tv_r_mynum2;
    TextView tv_r_mynum;

    //  receiver item list;
    TextView tv_sender_name;
    TextView tv_sender_phone;
    TextView tv_r_state;

    ArrayList<receiver_listviewitem> arrayList = new ArrayList<>();

    String host = "http://70.12.244.171:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        // 객체 찾기
        tv_r_mynum2 = (TextView)findViewById(R.id.tv_r_mynum2);
        tv_sender_name = (TextView)findViewById(R.id.tv_sender_name);
        tv_sender_phone = (TextView)findViewById(R.id.tv_sender_phone);
        tv_r_state = (TextView)findViewById(R.id.tv_r_state);
        tv_r_mynum = (TextView)findViewById(R.id.tv_r_mynum);

        //  text underline
        tv_r_mynum.setPaintFlags(tv_r_mynum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_r_mynum2.setPaintFlags(tv_r_mynum2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        try{
            //  권한받기
            int permissionCheck = ContextCompat.checkSelfPermission(ReceiverActivity.this,
                    Manifest.permission.INTERNET);

            //  인터넷
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(SenderQRcodeActivity.this,
                //"Internet 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {    //  NO눌렀을때
//                Toast.makeText(SenderQRcodeActivity.this,
//                "Internet 수신 권한 없음", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(ReceiverActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
            }

            //  내번호 찾기
            TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //  내번호 찾기 권한 설정
            int permissionCheck_number = ContextCompat.checkSelfPermission(ReceiverActivity.this,
                    Manifest.permission.READ_PHONE_STATE);

            if (permissionCheck_number == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(SenderQRcodeActivity.this,
                //"phone 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {    //  NO눌렀을때
                //Toast.makeText(SenderQRcodeActivity.this,
                //"phone 수신 권한 없음", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(ReceiverActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
            //  내번호 출력하기
            String number = phoneMgr.getLine1Number();
            number = number.replace("+82", "");
            number = "0" + number.substring(0, 2) + "-" + number.substring(2, 6) + "-" + number.substring(6, 10);
            tv_r_mynum2.setText(number);

            //  서버
            String url = this.host;
            url = url + "/receiver/" + number;

            //  리스트 뷰 객체 생성
            listview_receiver = (ListView)findViewById(R.id.listview_receiver);
            //  Adapter에게 전달할 data 구성 해야함
            arrayList = new ArrayList<>();
            new HttpTask().execute(url);

        } catch(Exception e){
            e.printStackTrace();
        }
    }//  oncreate

    class HttpTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            InputStream is = getInputStreamFromUrl(params[0]);
            String result = convertStreamToString(is);//이 함수는 이 페이지를 참고
            return result;
        }

        protected void onPostExecute(final String result) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Toast.makeText(SenderQRcodeActivity.this, result, Toast.LENGTH_SHORT).show();
                        // json 객체를 json 배열 형태로 변환
                        JSONArray json_array = new JSONArray(result);
                        // json 배열을 순회하며 파싱
                        for (int i = 0; i < json_array.length(); i++) {
                            receiver_listviewitem item = new receiver_listviewitem();
                            JSONObject json_item = json_array.getJSONObject(i);
                            item.setSenderName(json_item.getString("senderName"));
                            item.setSenderPhone(json_item.getString("senderPhone"));
                            item.setState(json_item.getString("state"));
                            item.setReceiver_qr(json_item.getString("receiverQR"));
                            item.setSenderOpenTime(json_item.getString("senderOpenTime"));
                            item.setSenderCloseTime(json_item.getString("senderCloseTime"));
                            item.setReceiverOpenTime(json_item.getString("receiverOpenTime"));
                            item.setReceiverCloseTime(json_item.getString("receiverCloseTime"));
                            item.setCreatedAt(json_item.getString("createdAt"));
                            arrayList.add(item);
                        }
                        // state순서, createAt 순서로 정렬
                        Collections.sort(arrayList);

                        //  새로만든 어댑터를 등록한다.
                        ReceiverListViewAdapter listViewAdapter = new ReceiverListViewAdapter(ReceiverActivity.this, R.layout.receiver_listview_item, arrayList);
                        listview_receiver.setAdapter(listViewAdapter);

                        //  ArrayList의 자료들로 리스트 뷰를 갱신하는 함수
                        listViewAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public InputStream getInputStreamFromUrl(String url) {
        InputStream content = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            content = response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try{
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}