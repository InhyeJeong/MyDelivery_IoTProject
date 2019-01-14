package com.example.student.deliveryregistration;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//  택배 접수 어플
public class MainActivity extends AppCompatActivity {
    //   변수선언
    EditText receiverName;
    EditText senderName;
    EditText receiverAddredd;
    EditText receiverPhone;
    EditText senderPhone;
    EditText companyKey;
    EditText locationCode;
    TextView tv_register_title;

    TextView tv_register_sender;
    TextView tv_register_receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  객체 찾기
        receiverName = (EditText)findViewById(R.id.receiverName);
        senderName = (EditText)findViewById(R.id.senderName);
        receiverAddredd = (EditText)findViewById(R.id.receiverAddress);
        receiverPhone = (EditText)findViewById(R.id.receiverPhone);
        senderPhone = (EditText)findViewById(R.id.senderPhone);
        companyKey = (EditText)findViewById(R.id.companyKey);
        locationCode = (EditText)findViewById(R.id.locationCode);
        tv_register_title = (TextView)findViewById(R.id.tv_register_title);

        //  text underline
//        tv_register_title.setPaintFlags(tv_register_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        tv_register_sender.setPaintFlags(tv_register_sender.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        tv_register_receiver.setPaintFlags(tv_register_receiver.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //  번호 "-" 자동 생성
        receiverPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        senderPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

    }

    //  토스트 함수 : 메인 스레드가 아닌 곳에서 쓰일 때 사용하기 위함
    public void postToastMessage(final String message) {
        //  so, 핸들러 이용
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
    //  sendPost http 통신
    //  handler에서 해야함
    public void sendPost(View view){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                http_post();
            }
        });
    }
    //  post 통신
    private void http_post(){
        HttpURLConnection urlConn = null;
        StringBuffer sbParams = new StringBuffer();
        sbParams.append("receiverName").append("=").append(receiverName.getText().toString()).append("&");
        sbParams.append("senderName").append("=").append(senderName.getText().toString()).append("&");
        sbParams.append("receiverAddress").append("=").append(receiverAddredd.getText().toString()).append("&");
        sbParams.append("receiverPhone").append("=").append(receiverPhone.getText().toString()).append("&");
        sbParams.append("senderPhone").append("=").append(senderPhone.getText().toString()).append("&");
        sbParams.append("companyKey").append("=").append(companyKey.getText().toString()).append("&");
        sbParams.append("locationCode").append("=").append(locationCode.getText().toString());

        //  등록 완료시 토스트
        //  서버에서 검사해서 -> return값을 활요해서 message 출력으로 수정하기 !
        String successMessage = receiverName.getText().toString() + "고객님 등록 완료";
        postToastMessage(successMessage);

        receiverName.setText("");
        senderName.setText("");
        receiverAddredd.setText("");
        receiverPhone.setText("");
        senderPhone.setText("");
        companyKey.setText("");
        locationCode.setText("");


        //  서버통신
        try{
            //  멀캠 : http://172.30.1.9:3000
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

//    public String getInputStreamFromUrl(String url) {
//        InputStream content = null;
//        try{
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpResponse response = httpclient.execute(new HttpGet(url));
//            content = response.getEntity().getContent();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String data = content.toString();
//
//        return data;
//    }
}


