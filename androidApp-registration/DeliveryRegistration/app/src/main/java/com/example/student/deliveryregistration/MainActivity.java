package com.example.student.deliveryregistration;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class MainActivity extends AppCompatActivity {

    EditText receiverName;
    EditText receiverAddredd;
    EditText receiverPhone;
    EditText senderPhone;
    EditText companyKey;
    EditText locationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiverName = (EditText)findViewById(R.id.receiverName);
        receiverAddredd = (EditText)findViewById(R.id.receiverAddress);
        receiverPhone = (EditText)findViewById(R.id.receiverPhone);
        senderPhone = (EditText)findViewById(R.id.senderPhone);
        companyKey = (EditText)findViewById(R.id.companyKey);
        locationCode = (EditText)findViewById(R.id.locationCode);

    }

    public void postToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void sendPost(View view){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                http_post();
            }
        });
    }

    private void http_post(){
        HttpURLConnection urlConn = null;
        StringBuffer sbParams = new StringBuffer();
        sbParams.append("receiverName").append("=").append(receiverName.getText().toString()).append("&");
        sbParams.append("receiverAddress").append("=").append(receiverAddredd.getText().toString()).append("&");
        sbParams.append("receiverPhone").append("=").append(receiverPhone.getText().toString()).append("&");
        sbParams.append("senderPhone").append("=").append(senderPhone.getText().toString()).append("&");
        sbParams.append("companyKey").append("=").append(companyKey.getText().toString()).append("&");
        sbParams.append("locationCode").append("=").append(locationCode.getText().toString());

        String successMessage = receiverName.getText().toString() + "고객님 등록 완료";

        postToastMessage(successMessage);

        receiverName.setText("");
        receiverAddredd.setText("");
        receiverPhone.setText("");
        senderPhone.setText("");
        companyKey.setText("");
        locationCode.setText("");



        try{
            URL url = new URL("http://70.12.244.171:3000/users");
            urlConn = (HttpURLConnection) url.openConnection();

            // [2-1]. urlConn 설정.
            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            // [2-2]. parameter 전달 및 데이터 읽어오기.
            String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
            OutputStream os = urlConn.getOutputStream();
            os.write(strParams.getBytes("UTF-8")); // 출력 스트림에 출력.
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return;

            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null){
                page += line;
            }

            return;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }
    }

    public String getInputStreamFromUrl(String url) {
        InputStream content = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            content = response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String data = content.toString();

        return data;
    }
}


