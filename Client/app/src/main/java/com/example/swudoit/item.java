package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class item extends AppCompatActivity {

    String title;
    String content;
    String today;
    String image_name;
    String image_copied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

    }

    public static void dataTest(String title, String content, String today, String image_name, String image_copied){
        Log.d("Message", "title : " + title);
        Log.d("Message", " content : " + content);
        Log.d("Message", " today : " + today);
        Log.d("Message", " image_name : " + image_name);
        Log.d("Message", " image_copied : " + image_copied);
    }
}
