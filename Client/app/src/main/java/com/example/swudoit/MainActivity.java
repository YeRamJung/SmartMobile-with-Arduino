package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;

public class MainActivity extends AppCompatActivity {

    boolean isUser;

    EditText idE;

    EditText passE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idE = (EditText)findViewById(R.id.idEdt);
        passE = (EditText)findViewById(R.id.passEdit);
    }

    protected void mClick(View v){

        String id = idE.getText().toString();

        String pass = passE.getText().toString();

        //Toast.makeText(MainActivity.this, pass, Toast.LENGTH_SHORT).show();

        isUser = signIn(id, pass);

        if(isUser){
            Toast.makeText(MainActivity.this, id + "님 환영합니다!!", Toast.LENGTH_SHORT).show();

            Intent mainView = new Intent(this, swudoit_main.class);

            startActivity(mainView);
        }else{
            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("Error");
            ab.setMessage("아이디, 비밀번호를 확인하세요!");

            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            ab.create();
            ab.show();
        }
    }

    private boolean signIn(String id, String pass){

    }


}
