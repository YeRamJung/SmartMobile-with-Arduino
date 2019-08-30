package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;

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

                        // 서버 통신 후 성공하면 Intent, 실패하면 Alert

                        Intent noteListView = new Intent(Note.this, NoteList.class);

                        startActivity(noteListView);

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
}
