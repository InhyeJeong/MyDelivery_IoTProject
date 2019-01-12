package com.example.test.mydelivery.ThirdDepth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        //  data 받아오기
        Intent intent = getIntent();
        tv_s_open.setText(intent.getStringExtra("SenderOpenTime"));
        tv_s_close.setText(intent.getStringExtra("SenderCloseTime"));
        tv_r_open.setText(intent.getStringExtra("ReceiverOpenTime"));
        tv_r_close.setText(intent.getStringExtra("ReceiverCloseTime"));

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
