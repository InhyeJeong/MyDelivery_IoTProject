package com.example.test.mydelivery.ThirdDepth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.mydelivery.R;

public class QrcodeInfoActivity extends AppCompatActivity {
    //  선언
    TextView tv_s_open;
    TextView tv_s_close;
    TextView tv_r_open;
    TextView tv_r_close;
    ImageView iv_qrcode_plus;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_info);  //  listview 클릭시 나오는 정보
        //  객체찾기
        tv_s_open = (TextView)findViewById(R.id.tv_s_open);
        tv_s_close = (TextView)findViewById(R.id.tv_s_close);
        tv_r_open = (TextView)findViewById(R.id.tv_r_open);
        tv_r_close = (TextView)findViewById(R.id.tv_r_close);
        iv_qrcode_plus = (ImageView)findViewById(R.id.iv_qrcode_plus);
        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);

        //  Text Underline
        tv1.setPaintFlags(tv1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv2.setPaintFlags(tv2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv3.setPaintFlags(tv3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv4.setPaintFlags(tv4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //  data 받아오기
        Intent intent = getIntent();
        DateFormat dateFormat = new DateFormat();

        // SenderOpenTime
        dateFormat.set_data(intent.getStringExtra("SenderOpenTime"));
        tv_s_open.setText(dateFormat.parse());
        //  SenderCloseTime
        dateFormat.set_data(intent.getStringExtra("SenderCloseTime"));
        tv_s_close.setText(dateFormat.parse());
        //  ReceiverOpenTime
        dateFormat.set_data(intent.getStringExtra("ReceiverOpenTime"));
        tv_r_open.setText(dateFormat.parse());
        //  ReceiverCloseTime
        dateFormat.set_data(intent.getStringExtra("ReceiverCloseTime"));
        tv_r_close.setText(dateFormat.parse());

        if(intent.getStringExtra("whichImage").equals("bitmap")){
            //  QR코드 사진 data 받아오기(byte -> bitmap)
            byte[] bytes = intent.getByteArrayExtra("QRcode");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            iv_qrcode_plus.setImageBitmap(bitmap);
        }else if(intent.getStringExtra("whichImage").equals("0")){
            iv_qrcode_plus.setImageResource(R.drawable.registered);
        }else if(intent.getStringExtra("whichImage").equals("1")){
            iv_qrcode_plus.setImageResource(R.drawable.locked);
        }else if(intent.getStringExtra("whichImage").equals("2")){
            iv_qrcode_plus.setImageResource(R.drawable.received);
        }
    }
}
