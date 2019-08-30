package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class musicList extends AppCompatActivity {
    private ImageButton music1;
    private ImageButton music2;
    private int num1, num2 = 0;

    private BluetoothSPP bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        bt = new BluetoothSPP(this); //Initializing

        music1 = (ImageButton)findViewById(R.id.musicPlayer);
        music2 = (ImageButton)findViewById(R.id.musicPlayer2);

        //곰세마리
        music1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num1==0){
                    music1.setSelected(true);
                    num1=1;
                }
                else{
                    music1.setSelected(false);
                    num1=0;
                }
            }
        });

        //학교종이 땡땡땡
        music2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num2==0){
                    music2.setSelected(true);
                    num2=1;
                }
                else{
                    music2.setSelected(false);
                    num2=0;
                }
            }
        });

    }//end onCreate

    public void setup() {
        //곰세마리
        ImageButton musicPlayer = findViewById(R.id.musicPlayer); //데이터 전송
        musicPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("1", true);
            }
        });

        //학교종
        ImageButton musicPlayer2 = findViewById(R.id.musicPlayer2); //데이터 전송
        musicPlayer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("2", true);
            }
        });
    }
}
