package com.example.test.mydelivery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.mydelivery.Model.sender_listviewitem;
import com.example.test.mydelivery.QrcodeInfoActivity;
import com.example.test.mydelivery.R;
import com.google.zxing.WriterException;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.PriorityQueue;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by inhye on 2018-12-13.
 */
//  커스터마이징 리스트 뷰를 위하 어뎁터 만들기
public class SenderListViewAdapter extends BaseAdapter {
    //  멤버변수

    ArrayList<sender_listviewitem> list;
    Context context;
    int item_layout;
    LayoutInflater layoutInflater;

    TextView tv_receiver_name;
    TextView tv_receiver_address;
    TextView tv_receiver_phone;

    TextView tv_s_state;
    ImageView iv_sender_qr;
    TextView tv_d_state;
    ConstraintLayout s_layout;

    //  QR코드 활용
    String TAG = "GenerateQRCode";
    //String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    //  QR코드 라이브러리 사용
    QRGEncoder qrgEncoder;

    //  생성자
    public SenderListViewAdapter(
            Context context,
            int item_layout,
            ArrayList<sender_listviewitem> list) {
        this.list = list;
        this.context = context;
        this.item_layout = item_layout;
        layoutInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }



    // getCount / getItem / getItemId / getView 는 반드시 오버라이드 해야 됨!
    @Override
    public int getCount() { //  리스트뷰에 출력할 data 갯수
      return list.size();
    }

    @Override
    public Object getItem(int i) {  //  arrayList 안에서 객체 1개를 얻는 함수.
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {  //  저장된 항목들의 번호를 return하는 함수.
        return i;   // 순서대로 값 저장 and 순서대로 list에 값 들어가니까 i 그대로 쓰자
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {    //  만든 layout에 data매칭시켜주는 함수.
        final int pos = i;  //  변수

        if(view == null) {  //  view는 1개의 항목, 처음에는 아무것도 없으니 null > 레이아웃을 만듦
            view = layoutInflater.inflate(
                    item_layout, viewGroup, false);
        }

        //  뷰 찾기
        ImageView iv_thumb = (ImageView) view.findViewById(R.id.iv_sender_qr);
        tv_receiver_name = (TextView)view.findViewById(R.id.tv_receiver_name);
        tv_receiver_address=(TextView)view.findViewById(R.id.tv_receiver_address);
        tv_receiver_phone = (TextView)view.findViewById(R.id.tv_receiver_phone);
        tv_s_state = (TextView)view.findViewById(R.id.tv_s_state);
        iv_sender_qr=(ImageView)view.findViewById(R.id.iv_sender_qr);
        tv_d_state = (TextView)view.findViewById(R.id.tv_d_state);
        s_layout = (ConstraintLayout)view.findViewById(R.id.s_layout); // 배달 상태에 따른 배경 색상 변경

        //  배달이 완료되면 배경색 회색 변경
        if(list.get(pos).getState().equals("0")){   // reg
            s_layout.setBackgroundColor(Color.rgb(255,255,255));
            tv_d_state.setText("");
        }else if(list.get(pos).getState().equals("1")){//   locked
            s_layout.setBackgroundColor(Color.rgb(150,150,150));
            tv_d_state.setText("배달 완료");
            tv_d_state.setTextColor(Color.rgb(0,0,255));
        }else if(list.get(pos).getState().equals("2")){ //  received
            s_layout.setBackgroundColor(Color.rgb(100,100,100));
            tv_d_state.setText("수신 완료");
            tv_d_state.setTextColor(Color.rgb(255,0,0));
        }

        // 뷰에 넣을 문자열 구성
        String state_show = "state : ";
        if(list.get(pos).getState().equals("0")){
            state_show += "registration";
        }else if(list.get(pos).getState().equals("1")){
            state_show += "locked";
        }else if(list.get(pos).getState().equals("2")){
            state_show += "received";
        }

        // 뷰에 스트링 담아주기
        tv_receiver_name.setText(list.get(pos).getReceiverName());
        tv_receiver_address.setText(list.get(pos).getReceiverAddress());
        tv_receiver_phone.setText(list.get(pos).getReceiverPhone());
        tv_s_state.setText(state_show);
        String sender_qr = list.get(pos).getSender_qr();

        // sender_qr (String -> bitmap 변환)
        if(sender_qr.length()>0) {
            WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
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

        // 이미지 클릭 시 동작
        iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  QR코드 확대해서 보여주기
                Intent intent  = new Intent(context,QrcodeInfoActivity.class);
                intent.putExtra("SenderOpenTime", list.get(pos).getSenderOpenTime());
                intent.putExtra("SenderCloseTime", list.get(pos).getSenderCloseTime());
                intent.putExtra("ReceiverOpenTime", list.get(pos).getReceiverOpenTime());
                intent.putExtra("ReceiverCloseTime", list.get(pos).getReceiverCloseTime());

                //  bitmap이미지 -> byte로
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("SenderQRcode",bytes);

                context.startActivity(intent);
            }
        });

        return view;
    }


}
