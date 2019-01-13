package com.example.test.mydelivery.SecondDepth;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.test.mydelivery.Adapter.SenderListViewAdapter;
import com.example.test.mydelivery.Model.sender_listviewitem;
import com.example.test.mydelivery.R;


import org.json.JSONArray;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collections;


import androidmads.library.qrgenearator.QRGEncoder;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

//  AsynkTask활용하기
public class SenderActivity extends AppCompatActivity {
    //  리스트 뷰 객체를 저장하는 변수
    ListView listview_sender;
    SenderListViewAdapter listViewAdapter;

    TextView tv_mynum2;

    //  sender item list
    TextView tv_receiver_name;
    TextView tv_receiver_address;
    TextView tv_receiver_phone;
    TextView tv_s_state;
    ImageView iv_sender_qr;
    TextView tv_s_mynum;
    ArrayList<sender_listviewitem> arrayList = new ArrayList<>();

    //  QR코드 활용
    String TAG = "GenerateQRCode";
    //String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    //  QR코드 라이브러리 사용
    QRGEncoder qrgEncoder;

    //  멀캠 : String host = "http://70.12.244.171:3000";
    String host = "http://192.168.0.5:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        //  객체 찾기

        tv_mynum2 = (TextView) findViewById(R.id.tv_mynum2);
        tv_receiver_name = (TextView) findViewById(R.id.tv_receiver_name);
        tv_receiver_address = (TextView) findViewById(R.id.tv_receiver_address);
        tv_receiver_phone = (TextView) findViewById(R.id.tv_receiver_phone);
        tv_s_mynum = (TextView)findViewById(R.id.tv_s_mynum);

        tv_s_state = (TextView) findViewById(R.id.tv_s_state);
        iv_sender_qr = (ImageView) findViewById(R.id.iv_sender_qr);
        //  text underline
        tv_s_mynum.setPaintFlags(tv_s_mynum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_mynum2.setPaintFlags(tv_mynum2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        try {


            //  권한받기
            int permissionCheck = ContextCompat.checkSelfPermission(SenderActivity.this,
                    Manifest.permission.INTERNET);
            int permissionCheck_read = ContextCompat.checkSelfPermission(SenderActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheck_write = ContextCompat.checkSelfPermission(SenderActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            //  인터넷
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(SenderQRcodeActivity.this,
                        //"Internet 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {    //  NO눌렀을때
                //Toast.makeText(SenderQRcodeActivity.this,
                        //"Internet 수신 권한 없음", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(SenderActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
            }
            //  read
            if (permissionCheck_read == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(SenderQRcodeActivity.this,
                        //"read 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(SenderQRcodeActivity.this,
                        //"read 수신 권한 없음", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(SenderActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            //  write
            if (permissionCheck_write == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(SenderQRcodeActivity.this,
                        //"write 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(SenderQRcodeActivity.this,
                        //"write 수신 권한 없음", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(SenderActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            //  내번호 찾기
            TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //  내번호 찾기 권한 설정
            int permissionCheck_number = ContextCompat.checkSelfPermission(SenderActivity.this,
                    Manifest.permission.READ_PHONE_STATE);

            if (permissionCheck_number == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(SenderQRcodeActivity.this,
                        //"phone 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {    //  NO눌렀을때
                //Toast.makeText(SenderQRcodeActivity.this,
                        //"phone 수신 권한 없음", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(SenderActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
            //  내번호 출력하기
            String number = phoneMgr.getLine1Number();
            number = number.replace("+82", "");
            number = "0" + number.substring(0, 2) + "-" + number.substring(2, 6) + "-" + number.substring(6, 10);
            tv_mynum2.setText(number);

            //  서버
            String url = this.host;
            url = url + "/sender/" + number;


            //  리스트 뷰 객체 생성
            listview_sender = (ListView) findViewById(R.id.listview_sender);

            //  Adapter에게 전달할 data 구성 해야함
            arrayList = new ArrayList<>();
            new HttpTask().execute(url);
        } catch (Exception e) {
        }
    }   //  oncreate


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
                            sender_listviewitem item = new sender_listviewitem();
                            JSONObject json_item = json_array.getJSONObject(i);
                            item.setReceiverName(json_item.getString("receiverName"));
                            item.setReceiverAddress(json_item.getString("receiverAddress"));
                            item.setReceiverPhone(json_item.getString("receiverPhone"));
                            item.setSender_qr(json_item.getString("senderQR"));
                            item.setState(json_item.getString("state"));
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
                        SenderListViewAdapter listViewAdapter = new SenderListViewAdapter(SenderActivity.this, R.layout.sender_listview_item, arrayList);
                        listview_sender.setAdapter(listViewAdapter);

                        //  ArrayList의 자료들로 리스트 뷰를 갱신하는 함수
                        listViewAdapter.notifyDataSetChanged();

                        //  리스트 뷰에  OnItemClickListener 등록하기
                        listview_sender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //Toast.makeText(SenderQRcodeActivity.this, i + " 선택함", Toast.LENGTH_LONG).show();
                            }
                        });

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
            try{
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}