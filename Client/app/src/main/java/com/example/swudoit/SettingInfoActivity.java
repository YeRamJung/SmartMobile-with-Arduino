package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SettingInfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);

        try{
            TextView userId = findViewById(R.id.infoID);
            TextView userpass = findViewById(R.id.infoPass);

            String user = MainActivity.sharedPreferences.getString("id", null);
            String userPW = MainActivity.sharedPreferences.getString(null, "1234");

            userId.setText(user);
            userpass.setText(userPW);
        }catch (NullPointerException n){
            n.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
