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

    String userIdx;

    boolean isCamera;

    ImageButton imgCamera;      // 카메라
    ImageButton btnreg;         // 작성
    EditText noteTitle;         // 타이틀
    EditText noteContent;       // 내용

    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        imgCamera = findViewById(R.id.imgCamera);
        btnreg = findViewById(R.id.btnreg);
        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteContent);

        //prf = getSharedPreferences("ID", Context.MODE_PRIVATE);
        prf = MainActivity.sharedPreferences;

        userIdx = prf.getString("userIdx", null);

        Log.d("userIdx", userIdx);

        // 이미지 버튼 클릭 리스너, 카메라, 작성 버튼, 카메라 찍고 확인하면 true, 아니면 false
        ImageButton.OnClickListener imageClickListener = new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.imgCamera :
                        Log.d("Message", "카메라 입력하기");
                        isCamera = false;
                        break;
                    case R.id.btnreg :
                        Log.d("Message", "다이어리 입력하기");

                        // 에딧트 텍스트
                        title = noteTitle.getText().toString();
                        content = noteContent.getText().toString();
                        today = uploadToday();

                        // 카메라
                        if(!isCamera){
                            image_name = "";
                            image_copied = "";
                        }else{
                            image_name = "";
                            image_copied = "";
                        }

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
                        ConnectServer connectServerPost = new ConnectServer();
                        connectServerPost.requestPost(title, content, today, userIdx, image_name, image_copied);

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

        public void requestPost(String title, String content, String today, String userIdx, String image_name, String image_copied){
            RequestBody body = new FormBody.Builder()
                    .add("title", title)
                    .add("content", content)
                    .add("today", today)
                    .add("userIdx", userIdx)
                    .add("image_name", image_name)
                    .add("image_copied", image_copied)
                    .build();

            final Request request = new Request.Builder()
                    .url(url+"user/diary")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                    backgroundThreadShortToast(Note.this, "서버 통신에 실패하였습니다.");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        Gson gson = new Gson();

                        String stl = gson.toJson(response.body().string());

                        if(response.isSuccessful()){
                            backgroundThreadShortToast(Note.this, "다이어리 업로드하였습니다.");
                            Intent noteListView = new Intent(Note.this, NoteList.class);
                            startActivity(noteListView);
                        }else{
                            backgroundThreadShortToast(Note.this, "서버 통신에 실패하였습니다.");
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
