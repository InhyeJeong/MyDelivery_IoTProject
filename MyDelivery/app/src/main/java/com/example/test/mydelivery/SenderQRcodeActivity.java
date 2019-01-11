package com.example.test.mydelivery;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.mydelivery.Adapter.SenderListViewAdapter;
import com.example.test.mydelivery.Model.sender_listviewitem;
import com.google.zxing.WriterException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

//  AsynkTask활용하기
public class SenderQRcodeActivity extends AppCompatActivity {
    //  리스트 뷰 객체를 저장하는 변수
    ListView listview_sender;
    SenderListViewAdapter listViewAdapter;

   //TextView tv_mynum;
    TextView tv_mynum2;
    //  sender item list
    TextView tv_receiver_name;
    TextView tv_receiver_address;
    TextView tv_receiver_phone;
    TextView tv_locationcode;
    TextView tv_companykey;
    TextView tv_s_state;
    ImageView iv_sender_qr;

    //  QR코드 활용
    String TAG = "GenerateQRCode";
    //String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    //  QR코드 라이브러리 사용
    QRGEncoder qrgEncoder;

    String host = "http://70.12.244.171:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_qrcode);

        //  객체 찾기
        //tv_mynum = (TextView)findViewById(R.id.tv_mynum);
        tv_mynum2 = (TextView)findViewById(R.id.tv_mynum2);
        tv_receiver_name = (TextView)findViewById(R.id.tv_receiver_name);
        tv_receiver_address=(TextView)findViewById(R.id.tv_receiver_address);
        tv_receiver_phone = (TextView)findViewById(R.id.tv_receiver_phone);

        tv_s_state = (TextView)findViewById(R.id.tv_s_state);
        iv_sender_qr=(ImageView)findViewById(R.id.iv_sender_qr);

        try{
            //  서버
            String url = this.host;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("senderPhone","010-3333-3333");

            //  onCreate함수 안에서 서버에서 가져오면 다른 것들이 동작 x
            // 따라서 asynkTask를 활용해서 data를 불러와야함
//            MyHttpTask myHttpTask = new MyHttpTask(url, map);
//            myHttpTask.execute();

            //  권한받기
            int permissionCheck = ContextCompat.checkSelfPermission(SenderQRcodeActivity.this,
                    Manifest.permission.INTERNET);
            int permissionCheck_read =ContextCompat.checkSelfPermission(SenderQRcodeActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheck_write =  ContextCompat.checkSelfPermission(SenderQRcodeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            //  인터넷
            if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SenderQRcodeActivity.this,
                        "Internet 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {    //  NO눌렀을때
                Toast.makeText(SenderQRcodeActivity.this,
                        "Internet 수신 권한 없음", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(SenderQRcodeActivity.this, new String[] {Manifest.permission.INTERNET}, 1);
            }
            //  read
            if(permissionCheck_read == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SenderQRcodeActivity.this,
                        "read 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SenderQRcodeActivity.this,
                        "read 수신 권한 없음", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(SenderQRcodeActivity.this,new String[] { Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            //  write
            if(permissionCheck_write == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SenderQRcodeActivity.this,
                        "write 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SenderQRcodeActivity.this,
                        "write 수신 권한 없음", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(SenderQRcodeActivity.this,new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            //  내번호 찾기
            TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //  내번호 찾기 권한 설정
            int permissionCheck_number = ContextCompat.checkSelfPermission(SenderQRcodeActivity.this,
                    Manifest.permission.READ_PHONE_STATE);

            if(permissionCheck_number == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SenderQRcodeActivity.this,
                        "phone 수신 권한 있음", Toast.LENGTH_LONG).show();
            } else {    //  NO눌렀을때
                Toast.makeText(SenderQRcodeActivity.this,
                        "phone 수신 권한 없음", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(SenderQRcodeActivity.this, new String[] {Manifest.permission.READ_PHONE_STATE}, 1);
            }
            //  내번호 출력하기
            String number = phoneMgr.getLine1Number();
            tv_mynum2.setText(number);



            //  2. 리스트 뷰 객체 생성
            listview_sender = (ListView)findViewById(R.id.listview_sender);

            //  Adapter에게 전달할 data 구성 해야함
            ArrayList<sender_listviewitem> arrayList = new ArrayList<>();
            arrayList.add(new sender_listviewitem("3","Seoul","010-4444-4444",  "38478954495843", "0", "3시","4시", "4시반", "5시","2019-01-03" ));
            arrayList.add(new sender_listviewitem("5","Seoul","010-4444-4444",  "38478954495843", "0", "3시","4시", "4시반", "5시","2019-01-05" ));
            arrayList.add(new sender_listviewitem("2","Seoul","010-4444-4444",  "38478954495843", "1", "3시","4시", "4시반", "5시","2019-01-02" ));
            arrayList.add(new sender_listviewitem("1","Seoul","010-4444-4444",  "38478954495843", "0", "3시","4시", "4시반", "5시","2019-01-01" ));
            arrayList.add(new sender_listviewitem("4","Seoul","010-4444-4444",  "38478954495843", "2", "3시","4시", "4시반", "5시","2019-01-04" ));
            arrayList.add(new sender_listviewitem("6","Seoul","010-4444-4444",  "38478954495843", "0", "3시","4시", "4시반", "5시","2019-01-06" ));
            // state순서, createAt 순서로 정렬
            Collections.sort(arrayList);







            //  새로만든 어댑터를 등록한다.
            SenderListViewAdapter listViewAdapter = new SenderListViewAdapter(SenderQRcodeActivity.this, R.layout.sender_listview_item, arrayList);
            listview_sender.setAdapter(listViewAdapter);

            //  ArrayList의 자료들로 리스트 뷰를 갱신하는 함수
            listViewAdapter.notifyDataSetChanged();

            //  리스트 뷰에  OnItemClickListener 등록하기
            listview_sender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   Toast.makeText(SenderQRcodeActivity.this, i + " 선택함", Toast.LENGTH_LONG).show();
                }
            });


        } catch(Exception e){
        }
    }   //  oncreate


    //  data 받아오기
    class MyHttpTask extends AsyncTask<Void, Void, String> {

        String url_str;
        HashMap<String, String> map;

        public MyHttpTask(String url_str, HashMap<String, String> map) {
            super();

            this.url_str = url_str;
            this.map = map;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            String post_query = "";
            PrintWriter printWriter = null;
            BufferedReader bufferedReader = null;

            try {
                URL text = new URL(url_str);
                HttpURLConnection http = (HttpURLConnection)text.openConnection();
                http.setRequestProperty("Content-type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
                http.setConnectTimeout(10000);
                http.setReadTimeout(10000);
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                if(map != null && map.size() > 0) {

                    Iterator<String> keys = map.keySet().iterator();

                    boolean first_query_part = true;
                    while(keys.hasNext()) {

                        if(!first_query_part) {
                            post_query += "&";
                        }

                        String key = keys.next();
                        post_query += (key + "=" + URLEncoder.encode(map.get(key), "UTF-8"));

                        first_query_part = false;
                    }

                    // sending to server
                    printWriter = new PrintWriter(new OutputStreamWriter(
                            http.getOutputStream(), "UTF-8"));
                    printWriter.write(post_query);
                    printWriter.flush();

                    // receive from server
                    //  데이터의 수신
                    //  버퍼에 저장
                    bufferedReader = new BufferedReader(new InputStreamReader(
                            http.getInputStream(), "UTF-8"));
                    //  1줄씩 읽음
                    StringBuffer stringBuffer = new StringBuffer(); //  대용량일때는 stringbuffer를 자주쓴다.
                    String line;

                    while((line = bufferedReader.readLine()) != null) { // buffer에 있는 내용을 1개 씩 불러옴.
                        stringBuffer.append(line);
                    }
                    //  result에 senddate라는 값이 저장되어 보내짐
                    result = stringBuffer.toString();
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally { // finally로 묶어서 예왜처리
                try{
                    if(printWriter != null) printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if(bufferedReader != null) bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // do something


            try {
                JSONObject root = new JSONObject(s);
                //  log출력
                Log.d("HttpConnectionLog", root.getString("senderPhone"));
                Log.d("HttpConnectionLog", root.getString("receiverName"));
                Log.d("HttpConnectionLog", root.getString("receiverAddress"));
                Log.d("HttpConnectionLog", root.getString("receiverPhone"));
                Log.d("HttpConnectionLog", root.getString("locationCode"));
                Log.d("HttpConnectionLog", root.getString("state"));
                Log.d("HttpConnectionLog", root.getString("companyKey"));
                Log.d("HttpConnectionLog", root.getString("senderQR"));

                //  json파일에서 원하는 data parsing
                final String mynum2 = root.getString("senderPhone");
                final String receiverName = root.getString("receiverName");
                final String receiverAddress = root.getString("receiverAddress");
                final String receiverPhone = root.getString("receiverPhone");
                final String locationCode = root.getString("locationCode");
                final String state = root.getString("state");
                final String companykey = root.getString("companyKey");
                final String sender_qr = root.getString("senderQR");

                // sender_qr (String -> bitmap 변환)
                if(sender_qr.length()>0) {

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            sender_qr, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        iv_sender_qr.setImageBitmap(bitmap); //qr코드를 비트맵으로 변환하여 넣기
                    } catch (WriterException e) {
                        Log.v(TAG, e.toString());
                    }
                } else {
                   //required토스트 띄우기
                }

                //  data채워주기
                tv_mynum2.setText(mynum2);
                tv_receiver_name.setText(receiverName);
                tv_receiver_address.setText(receiverAddress);
                tv_receiver_phone.setText(receiverPhone);
                tv_locationcode.setText(locationCode);
                tv_companykey.setText(companykey);
                tv_s_state.setText(state);

                listViewAdapter.notifyDataSetChanged();

            } catch(Exception e) {
                e.printStackTrace();
            }
            this.cancel(true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //  ImageView로 부터 이미지를 bitmap으로 변환한다
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has backgrouㅅnd drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }
}

