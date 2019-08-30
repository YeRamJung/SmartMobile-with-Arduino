package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Note extends AppCompatActivity {

    // 필요한 변수 : 현재 날짜(today), 내용(content), 제목(title), 현재 유저(id), 사진
    // 카메라 : 카메라 버튼 클릭 -> 카메라 권한 허용 -> 사진 찍음 -> 디바이스 내 혹은 리소스에 적재 -> 데이터베이스 연동 -> 이름, 카피 업로드
    // 내용 입력 -> return String
    // 타이틀 입력 -> return String
    // 내용 or 타이틀 입력 하지 않음 -> 입력하라고 알람 뜨기

    String today;
    String title;
    String content;

    String image_name;
    String image_copied;

    String id;

    ImageButton imgCamera;      // 카메라
    ImageButton btnreg;         // 작성
    EditText noteTitle;         // 타이틀
    EditText noteContent;       // 내용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        imgCamera = findViewById(R.id.imgCamera);
        btnreg = findViewById(R.id.btnreg);
        noteTitle = findViewById(R.id.noteContent);
        noteContent = findViewById(R.id.noteContent);

        id = MainActivity.sharedPreferences.getString("id", null);

        // 이미지 버튼 클릭 리스너, 카메라, 작성 버튼
        ImageButton.OnClickListener imageClickListener = new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.imgCamera :
                        Log.d("Message", "카메라 입력하기");
                        break;
                    case R.id.btnreg :
                        Log.d("Message", "다이어리 입력하기");

                        // 에딧트 텍스트
                        title = noteTitle.getText().toString();
                        content = noteContent.getText().toString();
                        today = uploadToday();

                        Log.d("Message", "Title : " + title);
                        Log.d("Message", "Content : " + content);
                        Log.d("Message", "Today : " + today);

                        if(title.isEmpty()){
                            AlertDialog.Builder ab = new AlertDialog.Builder(Note.this);

                            ab.setTitle("Error");
                            ab.setMessage("제목을 입력해주세요!");

                            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            ab.create();
                            ab.show();

                            return;
                        }

                        // 서버 통신 후 성공하면 Intent, 실패하면 Alert

                        break;
                }
            }
        };

        imgCamera.setOnClickListener(imageClickListener);
        btnreg.setOnClickListener(imageClickListener);

    }

    private String uploadToday(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date time = new Date();

        String today = dateFormat.format(time);

        return today;
    }

    public class ConnectServer{
        OkHttpClient client = new OkHttpClient();

        String url = "http://13.125.111.255:3000/";

        public void requestPost(final String id){
            RequestBody body = new FormBody.Builder()
                    .add("id", id)
                    .build();

            final Request request = new Request.Builder()
                    .url(url+"user/selectIdx")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        Gson gson = new Gson();

                        String stl = gson.toJson(response.body().string());

                        Log.d("Response ", "Response Body is " + stl);

                        if(response.isSuccessful()){

                        }else{
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    // Toast를 위한 함수
    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
