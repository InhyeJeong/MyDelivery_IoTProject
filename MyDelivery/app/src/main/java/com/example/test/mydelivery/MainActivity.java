package com.example.test.mydelivery;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.mydelivery.SecondDepth.ReceiverActivity;
import com.example.test.mydelivery.SecondDepth.SenderActivity;

//  AsynkTask활용하기
public class MainActivity extends AppCompatActivity {
    // 전역 변수 선언
    Button btn_sender;
    Button btn_receiver;
    TextView tv_subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  객체 찾기
        btn_sender = (Button)findViewById(R.id.btn_sender);
        btn_receiver = (Button)findViewById(R.id.btn_receiver);
        tv_subtitle = (TextView)findViewById(R.id.tv_subtitle);

        //  해당 버튼 클릭시 다른 xml파일로 이동 !
        btn_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SenderActivity.class);
                startActivity(intent);
            }
        });


        btn_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ReceiverActivity.class);
                startActivity(intent);
            }
        });
        //  Text Underline
        tv_subtitle.setPaintFlags(tv_subtitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }   //  oncreate
}

