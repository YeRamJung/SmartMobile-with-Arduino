package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean isUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void mClick(View v){

        Toast.makeText(MainActivity.this, "로그인 테스트", Toast.LENGTH_SHORT).show();

        Intent mainView = new Intent(this, swudoit_main.class);

        startActivity(mainView);
    }


}
