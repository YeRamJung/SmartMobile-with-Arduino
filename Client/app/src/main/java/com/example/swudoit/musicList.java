package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class musicList extends AppCompatActivity {
    private ImageButton music1;
    private ImageButton music2;
    private int num1, num2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

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

    }
}
