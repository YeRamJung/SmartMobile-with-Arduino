package com.example.swudoit.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.swudoit.MainActivity;
import com.example.swudoit.Note;
import com.example.swudoit.R;
import com.example.swudoit.musicList;

public class FragmentHome extends Fragment {

    private ImageButton lamp;
    private ImageButton humi;
    private ImageButton aircon;
    private ImageButton mobile;

    private int lnum, hnum, anum, mnum, snum = 0;

    TextView userId;

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        lamp = view.findViewById(R.id.btnLamp);
        humi = view.findViewById(R.id.btnHumi);
        aircon = view.findViewById(R.id.btnAircon);
        mobile = view.findViewById(R.id.btnMobile);

        try{
            userId = view.findViewById(R.id.txtPerson);

            String user = MainActivity.sharedPreferences.getString("id", null);

            userId.setText(user);
        }catch (NullPointerException n){
            n.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        //무드등 on/off
        lamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lnum==0){
                    lamp.setSelected(true);
                    lnum=1;
                }
                else{
                    lamp.setSelected(false);
                    lnum=0;
                }
            }
        });

        //가습기 on/off
        humi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hnum==0){humi.setSelected(true);
                    hnum=1;
                }
                else{
                    humi.setSelected(false);
                    hnum=0;
                }
            }
        });

        //에어컨 on/off
        aircon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(anum==0){
                    aircon.setSelected(true);
                    anum=1;
                }
                else{
                    aircon.setSelected(false);
                    anum=0;
                }
            }
        });

        //모빌 on/off
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mnum==0){
                    mobile.setSelected(true);
                    mnum=1;
                }
                else{
                    mobile.setSelected(false);
                    mnum=0;
                }
            }
        });

        //노래 버튼 눌러서 페이지 이동
        ImageButton btnSound = view.findViewById(R.id.btnSound);
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), musicList.class);
                startActivity(i);
            }
        });

        return view;

    }
}
